/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.event.service.impl.BatchEventServiceImpl
   Module Description   :

   Date Created      : 2011/2/9
   Original Author   : jeffma
   Team              : yaodian100
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.event.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cc.macloud.core.account.entity.User;
import cc.macloud.core.account.entity.Permission.Type;
import cc.macloud.core.account.utils.AdminHelper;
import cc.macloud.core.common.batch.AbstractRunJob;
import cc.macloud.core.common.dao.ObjectDao;
import cc.macloud.core.common.dao.impl.CommonCriteria;
import cc.macloud.core.common.entity.Menu;
import cc.macloud.core.common.exception.CoreException;
import cc.macloud.core.common.service.DomainService;
import cc.macloud.core.common.service.MenuService;
import cc.macloud.core.common.service.ThreadService;
import cc.macloud.core.common.service.impl.DomainServiceImpl;
import cc.macloud.core.common.utils.DateUtils;
import cc.macloud.core.common.utils.convert.TimestampConverter;
import cc.macloud.core.event.batch.ExecuteDetailJob;
import cc.macloud.core.event.batch.UpdateEventJob;
import cc.macloud.core.event.entity.BatchDetail;
import cc.macloud.core.event.entity.BatchEvent;
import cc.macloud.core.event.service.BatchEventService;

/**
 * @author jeffma
 * 
 */
public class BatchEventServiceImpl extends DomainServiceImpl<BatchEvent> implements BatchEventService,
		ApplicationContextAware, InitializingBean {

	@Autowired
	private ThreadService threadService;
	@Autowired
	private MenuService menuService;
	// @Autowired
	// private BaseManager baseManager;

	private ApplicationContext ctx;

	private ObjectDao<BatchDetail> detailDao;
	private Menu jobMenu;

	/** default constructor */
	public BatchEventServiceImpl() {
		super();
		ConvertUtils.register(new TimestampConverter(), Date.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.
	 * ApplicationContext)
	 */
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		this.ctx = ctx;
	}

	/**
	 * @param detailDao
	 *           the detailDao to set
	 */
	public void setDetailDao(ObjectDao<BatchDetail> detailDao) {
		this.detailDao = detailDao;
	}

	/**
	 * @return the jobMenu
	 */
	public Menu getJobMenu() {
		if (jobMenu == null) {
			jobMenu = menuService.get("BatchEvent.name");
		}
		return jobMenu;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// 檢查 BatchEvent.Status=QUEUE 但是 BatchDetail.executeStatus=null 的, 表示這是重開不確定狀況的資料
		CommonCriteria cri = new CommonCriteria();
		cri.addEq("status", BatchEvent.Status.QUEUE);
		cri.addLe("startDate", DateUtils.getCurrentTime());
		List<BatchEvent> events = getList(0, -1, cri, null);
		for (BatchEvent event : events) {
			logger.warn("update fail batch:" + event.getUuid());
			// 將有問題的批次更新狀態
			try {
				updateBatchEvent(event.getUuid(), true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yaodian100.core.common.service.impl.DomainServiceImpl#delete(java.lang.Object)
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(BatchEvent entity) throws CoreException {
		User user = AdminHelper.getUser();
		if ((entity != null) && (entity.getModifyDate() != null) && (user != null)) {
			if (!(entity.getCreateUser().equals(user.getUsername()) || AdminHelper.hasPermission(user, "ADM", Type.SYSTEM))) {
				// 非 owner 亦非 admin
				throw new CoreException("errors.common.event.noowner", user.getUsername(), entity.getBatchName());
			}
		}
		// 刪除 detail
		deleteDetails(getDetails(entity.getUuid()));
		// 刪除 event
		super.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yaodian100.core.common.service.impl.DomainServiceImpl#get(java.io.Serializable)
	 */
	@Override
	public BatchEvent get(Serializable oid) throws CoreException {
		BatchEvent entity = super.get(oid);
		User user = AdminHelper.getUser();
		if ((entity != null) && (user != null)) {
			if (!(entity.getCreateUser().equals(user.getUsername()) || AdminHelper.hasPermission(user, "ADM", Type.SYSTEM))) {
				// 非 owner 亦非 admin
				throw new CoreException("errors.common.event.noowner", user.getUsername(), entity.getBatchName());
			}
		}

		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yaodian100.core.common.service.impl.DomainServiceImpl#save(java.lang.Object)
	 */
	@Override
	@Transactional(readOnly = false)
	public BatchEvent save(BatchEvent entity) throws CoreException {
		User user = AdminHelper.getUser();
		if ((entity != null) && (entity.getModifyDate() != null) && (user != null)) {
			if (!(entity.getCreateUser().equals(user.getUsername()) || AdminHelper.hasPermission(user, "ADM", Type.SYSTEM))) {
				// 非 owner 亦非 admin
				throw new CoreException("errors.common.event.noowner", user.getUsername(), entity.getBatchName());
			}
		}
		return super.save(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yaodian100.ec.batch.service.BatchEventService#executeJob(com.yaodian100.ec.batch.entity.BatchDetail)
	 */
	@Override
	@Transactional(readOnly = false, timeout = 30)
	public void executeJob(BatchDetail jobDetail) {
		try {
			// 這邊要圈一個 tx
			// execute sql, hql or call service
			String function = jobDetail.getBatchEvent().getExecuteFunction();
			if (BatchEvent.ExecuteType.HQL.equals(jobDetail.getBatchEvent().getExecuteType())) {
				String hql = jobDetail.getBatchEvent().getExecuteService();
				Map parameters = new HashMap();
				parameters.putAll(jobDetail.getDataNewMap());
				parameters.putAll(jobDetail.getDataOldMap());
				parameters.put("dataKey", jobDetail.getDataKey());
				Object o = getDao().executeUpdate(hql, parameters);
				if (o instanceof Number) {
					Number n = (Number) o;
					if (n.intValue() == 0) {
						// 處理 0 筆, 條件不符
						jobDetail.setExecuteStatus(Boolean.FALSE);
						jobDetail.setFailMessage(StringUtils.abbreviate("原值不符,parameters:" + parameters, 200));
						detailDao.saveOrUpdate(jobDetail);
					} else {
						// 處理大於 0 筆, 視為成功
						logger.info("hql:{}, result:{}", hql, o);
						jobDetail.setExecuteStatus(Boolean.TRUE);
						detailDao.saveOrUpdate(jobDetail);
					}
				} else {
					// 回傳值不為 Number, 無法判斷是否處理成功, 一律視為成功
					logger.info("hql:{}, result:{}", hql, o);
					jobDetail.setExecuteStatus(Boolean.TRUE);
					detailDao.saveOrUpdate(jobDetail);
				}
			} else if (BatchEvent.ExecuteType.CTX.equals(jobDetail.getBatchEvent().getExecuteType())) {
				@SuppressWarnings("rawtypes")
				DomainService service = (DomainService) ctx.getBean(jobDetail.getBatchEvent().getExecuteService());

				Object obj = null;
				if (BatchEvent.Type.CREATE.equals(jobDetail.getBatchEvent().getType())) {
					// BatchEvent EntityInfo 必須有 class full name, 並且該 class 必須提供一個空的建構子
					obj = ConstructorUtils.invokeConstructor(Class.forName(jobDetail.getBatchEvent().getEntityInfo()), null);
				} else {

					if ("get".equals(jobDetail.getBatchEvent().getExecuteGet())) {
						try {
							obj = service.get(jobDetail.getDataKey());
						} catch (Exception e) {
							obj = service.get(new Long(jobDetail.getDataKey()));
						}
					} else {

						// obj = service.get(jobDetail.getDataKey());
						if (service.getClass().getMethod(jobDetail.getBatchEvent().getExecuteGet(), String.class) != null) {
							obj = MethodUtils.invokeMethod(service, jobDetail.getBatchEvent().getExecuteGet(),
									jobDetail.getDataKey());
						} else if (service.getClass().getMethod(jobDetail.getBatchEvent().getExecuteGet(), Long.class) != null) {
							obj = MethodUtils.invokeMethod(service, jobDetail.getBatchEvent().getExecuteGet(),
									Long.valueOf(jobDetail.getDataKey()));
						} else {
							throw new CoreException("Service 型态不正确, method:" + jobDetail.getBatchEvent().getExecuteGet());
						}
					}
				}
				StringBuffer errorMsg = new StringBuffer();
				Map<String, String> oldValueMap = jobDetail.getDataOldMap();
				Map<String, String> newValueMap = jobDetail.getDataNewMap();
				if (BatchEvent.Type.UPDATE.equals(jobDetail.getBatchEvent().getType())) {
					// 僅 UPDATE 需要比對
					for (String attr : jobDetail.getBatchEvent().getEditAttributes().split(",")) {
						Object oldValue = oldValueMap.get(attr);
						if (BeanUtils.getProperty(obj, attr) == null) {
							if ((oldValue instanceof JSONObject) && !((JSONObject) oldValue).isNullObject()) {
								errorMsg.append("JSONnullattr:" + attr + ",old:" + oldValue + ",new:"
										+ BeanUtils.getProperty(obj, attr) + ";");
							} else if (!(oldValue instanceof JSONObject) && (oldValue != null) && !" ".equals(oldValue)
									&& !"null".equals(oldValue)) {
								errorMsg.append("nullattr:" + attr + ",old:" + oldValue + ",new:"
										+ BeanUtils.getProperty(obj, attr) + ";");

							}
						} else if (!BeanUtils.getProperty(obj, attr).equals(oldValue) && !"modifyUser".equals(attr)) {
							errorMsg.append("attr:" + attr + ",old:" + oldValue + ",new:" + BeanUtils.getProperty(obj, attr)
									+ ";");
						}
					}
				}
				if (errorMsg.length() > 0) {
					jobDetail.setExecuteStatus(Boolean.FALSE);
					jobDetail.setFailMessage(StringUtils.abbreviate("原值不符," + errorMsg.toString(), 200));
					detailDao.saveOrUpdate(jobDetail);
				} else {
					if (!BatchEvent.Type.DELETE.equals(jobDetail.getBatchEvent().getType())) {

						// 僅 UPDATE, CREATE 需要由 newValueMap copy 值
						for (String attr : jobDetail.getBatchEvent().getEditAttributes().split(",")) {
							BeanUtils.setProperty(obj, attr, newValueMap.get(attr));
						}
					}
					// MethodUtils.invokeExactMethod(service, jobDetail.getBatchEvent().getExecuteMethod(), obj);
					MethodUtils.invokeMethod(service, jobDetail.getBatchEvent().getExecuteMethod(), obj);

					jobDetail.setExecuteStatus(Boolean.TRUE);
					jobDetail.setFailMessage("-,Success");
					detailDao.saveOrUpdate(jobDetail);
				}

			} else {
				// unknow function
				jobDetail.setExecuteStatus(Boolean.FALSE);
				jobDetail.setFailMessage(StringUtils.abbreviate("系统异常,function:" + function + ",exeType:"
						+ jobDetail.getBatchEvent().getExecuteType(), 200));
				detailDao.saveOrUpdate(jobDetail);
			}
		} catch (Exception e) {
			logger.error("jobDetail:" + jobDetail, e);
			jobDetail.setExecuteStatus(Boolean.FALSE);
			jobDetail.setFailMessage(StringUtils.abbreviate("系统异常," + e.getMessage(), 200));
			detailDao.saveOrUpdate(jobDetail);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yaodian100.ec.batch.service.BatchEventService#addBatch(com.yaodian100.ec.batch.entity.BatchEvent)
	 */
	@Override
	@Transactional(propagation = Propagation.NEVER)
	public int addBatch(String batchId) {
		BatchEvent event = get(batchId);
		if (!BatchEvent.Status.FINISH.equals(event.getStatus())) {
			throw new CoreException("errors.common.event.status", event.getBatchName(), event.getStatus().getDesc());
		}
		event.setStatus(BatchEvent.Status.QUEUE);
		event.setRunDate(DateUtils.getCurrentTime());
		save(event);

		int count = 0;
		CommonCriteria criteria = new CommonCriteria();
		criteria.addEq("batchEvent", event);
		List<BatchDetail> details = detailDao.getList(criteria, null);
		for (BatchDetail detail : details) {
			try {
				count++;
				AbstractRunJob runJob = new ExecuteDetailJob(detail, this);
				threadService.executeInBackground(runJob);
				logger.debug("job add {}/{}, detail:{}", new Object[] { count, details.size(), detail });
			} catch (Exception e) {
				logger.error("job add fail " + count + "/" + details.size() + ", detail:" + detail, e);
			}
		}

		// 最後排入一個更新 eventjob 的工作
		try {
			threadService.scheduleInBackground(new UpdateEventJob(event.getUuid(), this), 60);
		} catch (Exception e) {
			logger.error("update event job add fail, event:" + event, e);
		}
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yaodian100.ec.batch.service.BatchEventService#updateBatchEvent(com.yaodian100.ec.batch.entity.BatchEvent)
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateBatchEvent(String eventOid, boolean fail) {
		BatchEvent event = get(eventOid);
		if (fail) {
			CommonCriteria criteria = new CommonCriteria();
			criteria.addEq("batchEvent", event);
			criteria.addEq("executeStatus", null);
			List<BatchDetail> details = detailDao.getList(criteria, null);
			if (details.size() == 0) {
				// 若 waitJob 為0, 表示全部執行完畢, 更新為 END
				event.setStatus(BatchEvent.Status.END);
				event.setEndDate(DateUtils.getCurrentTime());
				super.save(event);
			} else {
				// 若 waitJob 不為0, 表示有尚未執行完畢, 更新為 FAIL, detail 設系統錯誤
				event.setStatus(BatchEvent.Status.FAIL);
				event.setEndDate(DateUtils.getCurrentTime());
				for (BatchDetail detail : details) {
					detail.setExecuteStatus(Boolean.FALSE);
					detail.setFailMessage("系统异常,批次异常终止");
				}
				saveOrUpdateDetails(details);
				super.save(event);
			}
		} else {
			CommonCriteria criteria = new CommonCriteria();
			criteria.addEq("batchEvent", event);
			criteria.addEq("executeStatus", null);
			long waitJob = detailDao.getListSize(criteria).longValue();
			if (waitJob == 0) {
				event.setStatus(BatchEvent.Status.END);
				event.setEndDate(DateUtils.getCurrentTime());
				super.save(event);
			} else {
				// 若 waitJob 不為0, 表示尚未執行完畢, 則再次排定 update job
				try {
					threadService.scheduleInBackground(new UpdateEventJob(eventOid, this), 60);
				} catch (Exception e) {
					logger.error("update event job add fail, event:" + event, e);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yaodian100.ec.batch.service.BatchEventService#saveOrUpdateDetails(java.util.Collection)
	 */
	@Override
	@Transactional(readOnly = false)
	public void saveOrUpdateDetails(Collection<BatchDetail> details) {
		for (BatchDetail d : details) {
			d.getDataOld();
			d.getDataNew();
		}
		detailDao.saveOrUpdateBatch(details);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yaodian100.ec.batch.service.BatchEventService#deleteDetails(java.util.Collection)
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteDetails(Collection<BatchDetail> details) {
		detailDao.deleteBatch(details);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yaodian100.ec.batch.service.BatchEventService#getDetails(java.lang.String)
	 */
	@Override
	public List<BatchDetail> getDetails(String batchId) {
		CommonCriteria criteria = new CommonCriteria();
		criteria.addEq("batchEvent.oid", batchId);
		List<BatchDetail> details = detailDao.getList(criteria, new String[] { "dataKey desc" });
		return details;
	}

}
