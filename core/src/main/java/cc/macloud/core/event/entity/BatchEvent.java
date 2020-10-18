/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.event.entity.BatchEvent
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
package cc.macloud.core.event.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

import cc.macloud.core.common.entity.BaseEntity;
import cc.macloud.core.common.entity.Option;
import cc.macloud.core.common.utils.DateUtils;
import cc.macloud.core.common.utils.StringUtils;

/**
 * @author jeffma
 * 
 */
@Entity
@Table(name = "CORE_EVENT_MAIN")
public class BatchEvent extends BaseEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 8266567562391199842L;

	public enum Status {
		INIT("編輯中"), FINISH("待排程"), QUEUE("排程中"), RUNING("执行中"), END("已执行"), CANCEL("已取消"), FAIL("执行异常");
		private String desc;

		Status(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}

		public String getCode() {
			return name();
		}
	}

	public enum ExecuteType {
		HQL(), CTX();
	}

	public enum Type {
		CREATE("新增"), UPDATE("編輯"), DELETE("刪除");
		private String desc;

		Type(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}

		public String getCode() {
			return name();
		}
	}

	@Id
	@GeneratedValue(generator = "gen_uuid")
	@GenericGenerator(name = "gen_uuid", strategy = "uuid")
	@Column(name = "OBJ_UUID", length = 32)
	private String uuid;

	/**
	 * batchName, 批次代码, <br/>
	 * 建立批次时, 于 option.code 取得, 格式应为 domain/method <br/>
	 * 例如 "product/delay" 或 "shelf/add"
	 */
	@Column(name = "BATCH_NAME", length = 50, nullable = false)
	private String batchName;

	/** startDate, 預計開始時間 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE")
	private Date startDate;
	/** runDate, 被加入 Queue 時間 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RUN_DATE")
	private Date runDate;
	/** endDate, 被監測到全部執行完畢 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE")
	private Date endDate;

	/** status, 批次状态 */
	@Enumerated(EnumType.STRING)
	@Column(name = "BATCH_STATUS", length = 10, nullable = false)
	private Status status = Status.INIT;
	/**
	 * type, 批次执行模式 <br/>
	 * 新增(CREATE)不比对 oldData,<br/>
	 * 修改(UPDATE)比对 oldData, <br/>
	 * 删除(DELETE)无 newData
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "BATCH_TYPE", length = 10, nullable = false)
	private Type type = Type.UPDATE;

	/** recordSize, 批次笔数 */
	@Formula("(select count(1) from CORE_EVENT_DETAIL bd where bd.PARENT_UUID=uuid)")
	private long recordSize = 0;
	/** successSize, 执行成功笔数 */
	@Formula("(select count(1) from CORE_EVENT_DETAIL bd where bd.PARENT_UUID=uuid and bd.RUN_STATUS=1)")
	private long successSize = 0;
	/** failSize, 执行失败笔数 */
	@Formula("(select count(1) from CORE_EVENT_DETAIL bd where bd.PARENT_UUID=uuid and bd.RUN_STATUS=0)")
	private long failSize = 0;

	/** memo, 批次备注 */
	@Column(name = "MEMO", length = 300)
	private String memo;

	/** executeFunction */
	@Column(name = "EXEC_FUNC", nullable = false)
	private String executeFunction;
	/** execute get function for update or delete, default get */
	@Column(name = "EXEC_GET", nullable = false)
	private String executeGet = "get";
	/** entityInfo */
	@Column(name = "ENTITY_INFO", nullable = false, length = 500)
	private String entityInfo;
	/** editAttributes */
	@Column(name = "EDIT_ATTRS", length = 1000)
	private String editAttributes;

	@Transient
	private ExecuteType executeType;
	/** executeService or hql */
	@Transient
	private String executeService;
	@Transient
	private String executeMethod;

	/** default constructor */
	@SuppressWarnings("unused")
	private BatchEvent() {
		super();
	}

	/** default constructor */
	public BatchEvent(Option option) {
		super();
		this.batchName = option.getCode();
		JSONObject json = JSONObject.fromObject(option.getMemo2());
		this.entityInfo = json.getString("entity");
		this.type = Type.valueOf(json.getString("type"));
		this.editAttributes = json.getString("editAttributes");
		try {
			this.executeGet = json.getString("executeGet");
		} catch (JSONException e) {
			this.executeGet = "get";
		}

		this.setExecuteFunction(option.getMemo1());

	}

	/**
	 * @return the executeType
	 */
	public ExecuteType getExecuteType() {
		if (executeType == null) {
			String[] s = StringUtils.split(executeFunction, ":");
			executeType = ExecuteType.valueOf(s[0]);
		}
		return executeType;
	}

	/**
	 * @return the executeService
	 */
	public String getExecuteService() {
		if (executeService == null) {
			String[] s = StringUtils.split(executeFunction, ":");
			switch (getExecuteType()) {
			case CTX:
				String[] t = StringUtils.split(s[1], ".");
				executeService = t[0];
				executeMethod = t[1];
				break;
			case HQL:
				executeService = s[1];
				break;
			}
		}
		return executeService;
	}

	/**
	 * @return the executeMethod
	 */
	public String getExecuteMethod() {
		if (executeMethod == null) {
			String[] s = StringUtils.split(executeFunction, ":");
			switch (getExecuteType()) {
			case CTX:
				String[] t = StringUtils.split(s[1], ".");
				executeMethod = t[1];
				break;
			}
		}
		return executeMethod;
	}

	/**
	 * @return the batchName
	 */
	public String getBatchName() {
		return batchName;
	}

	/**
	 * @param batchName
	 *           the batchName to set
	 */
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *           the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the runDate
	 */
	public Date getRunDate() {
		return runDate;
	}

	/**
	 * @param runDate
	 *           the runDate to set
	 */
	public void setRunDate(Date runDate) {
		this.runDate = runDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *           the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status
	 *           the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type
	 *           the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * @return the recordSize
	 */
	public long getRecordSize() {
		return recordSize;
	}

	/**
	 * @param recordSize
	 *           the recordSize to set
	 */
	public void setRecordSize(long recordSize) {
		this.recordSize = recordSize;
	}

	/**
	 * @return the successSize
	 */
	public long getSuccessSize() {
		return successSize;
	}

	/**
	 * @param successSize
	 *           the successSize to set
	 */
	public void setSuccessSize(long successSize) {
		this.successSize = successSize;
	}

	/**
	 * @return the failSize
	 */
	public long getFailSize() {
		return failSize;
	}

	/**
	 * @param failSize
	 *           the failSize to set
	 */
	public void setFailSize(long failSize) {
		this.failSize = failSize;
	}

	/**
	 * @return the executeFunction
	 */
	public String getExecuteFunction() {
		return executeFunction;
	}

	/**
	 * @param executeFunction
	 *           the executeFunction to set
	 */
	public void setExecuteFunction(String executeFunction) {
		this.executeFunction = executeFunction;
		String[] s = StringUtils.split(executeFunction, ":");
		executeType = ExecuteType.valueOf(s[0]);
		switch (executeType) {
		case CTX:
			String[] t = StringUtils.split(s[1], ".");
			executeService = t[0];
			executeMethod = t[1];
			break;
		case HQL:
			executeService = s[1];
			break;
		}
	}

	/**
	 * @return the entityInfo
	 */
	public String getEntityInfo() {
		return entityInfo;
	}

	/**
	 * @param entityInfo
	 *           the entityInfo to set
	 */
	public void setEntityInfo(String entityInfo) {
		this.entityInfo = entityInfo;
	}

	/**
	 * @return the editAttributes
	 */
	public String getEditAttributes() {
		return editAttributes;
	}

	/**
	 * @param editAttributes
	 *           the editAttributes to set
	 */
	public void setEditAttributes(String editAttributes) {
		this.editAttributes = editAttributes;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo
	 *           the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	public boolean isEditable() {
		boolean result = true;
		if (startDate == null) {
			result = true;
		} else if (Status.INIT.equals(status) || Status.FINISH.equals(status)) {
			Calendar c = Calendar.getInstance();
			c.setTime(startDate);
			c.add(Calendar.MINUTE, -5);
			result = DateUtils.getCurrentTime().before(c.getTime());
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid
	 *           the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public boolean isCancelable() {
		boolean result = true;
		if (startDate == null) {
			result = true;
		} else if (Status.INIT.equals(status) || Status.FINISH.equals(status)) {
			Calendar c = Calendar.getInstance();
			c.setTime(startDate);
			c.add(Calendar.MINUTE, -2);
			result = DateUtils.getCurrentTime().before(c.getTime());
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * @return the executeGet
	 */
	public String getExecuteGet() {
		if (StringUtils.isBlank(executeGet)) {
			executeGet = "get";
		}
		return executeGet;
	}

	/**
	 * @param executeGet
	 *           the executeGet to set
	 */
	@SuppressWarnings("unused")
	private void setExecuteGet(String executeGet) {
		this.executeGet = executeGet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BatchEvent [uuid=" + uuid + ", batchName=" + batchName + ", startDate=" + startDate + ", runDate="
				+ runDate + ", endDate=" + endDate + ", status=" + status + ", executeFunction=" + executeFunction + "]";
	}

}
