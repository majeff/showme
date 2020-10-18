/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.common.service.impl.DomainServiceImpl
   Module Description   :

   Date Created      : 2008/12/19
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.common.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import cc.macloud.core.cache.exception.CacheException;
import cc.macloud.core.cache.service.CacheService;
import cc.macloud.core.common.dao.ObjectDao;
import cc.macloud.core.common.dao.impl.CommonCriteria;
import cc.macloud.core.common.exception.CoreException;
import cc.macloud.core.common.service.DomainService;
import cc.macloud.core.logger.service.AnnotationLogger;

/**
 * @author jeffma
 */
@AnnotationLogger(monitor = true)
@Transactional(readOnly = true, rollbackFor = Throwable.class)
public class DomainServiceImpl<T> implements DomainService<T> {

	/** logger */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/** domain 物件操作 dao */
	private ObjectDao<T> dao;
	/** cache */
	private CacheService<T> cache;
	/** cacheAttribute */
	private String cacheAttribute = null;
	/** defaultSort: "updateDate", "createDate" */
	private String[] defaultSort = new String[] { "modifyDate", "createDate" };

	/**
	 * @return the dao
	 */
	public ObjectDao<T> getDao() {
		return dao;
	}

	/**
	 * @param dao
	 *           the dao to set
	 */
	public void setDao(ObjectDao<T> dao) {
		this.dao = dao;
	}

	/**
	 * @return the defaultSort
	 */
	public String[] getDefaultSort() {
		return defaultSort;
	}

	/**
	 * @return the cache
	 */
	protected CacheService<T> getCache() {
		return cache;
	}

	/**
	 * @param cache
	 *           the cache to set
	 */
	public void setCache(CacheService<T> cache) {
		this.cache = cache;
	}

	/**
	 * @param cacheAttribute
	 *           the cacheAttribute to set
	 */
	public void setCacheAttribute(String cacheAttribute) {
		this.cacheAttribute = cacheAttribute;
	}

	/**
	 * @param defaultSort
	 *           the defaultSort to set
	 */
	public void setDefaultSort(String[] defaultSort) {
		this.defaultSort = defaultSort;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cc.macloud.core.common.service.DomainService#delete(java.lang.Object)
	 */

	@Transactional(readOnly = false)
	public void delete(T entity) throws CoreException {
		logger.debug("entity:{}", entity);
		if (cache != null) {
			try {
				cache.remove(BeanUtils.getSimpleProperty(entity, cacheAttribute));
			} catch (Exception e) {
				logger.warn("remove cache fail, Object:{}", entity);
			}
		}
		dao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cc.macloud.core.common.service.DomainService#get(java.io.Serializable)
	 */

	public T get(Serializable oid) throws CoreException {
		T entity = null;
		if (cache != null) {
			try {
				entity = cache.get(String.valueOf(oid));
			} catch (CacheException e) {
				entity = dao.get(oid);
				try {
					if (entity != null) {
						cache.put(BeanUtils.getSimpleProperty(entity, cacheAttribute), entity);
					}
				} catch (Exception e1) {
					logger.warn("put cache fail, Object:{}", entity);
				}
			}
		} else {
			entity = dao.get(oid);
		}
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cc.macloud.core.common.service.DomainService#getAll(int, int)
	 */

	public List<T> getAll(int firstResult, int maxResults, String[] sortOrder) throws CoreException {
		if (sortOrder == null) {
			return dao.getListPageable(new CommonCriteria(), defaultSort, firstResult, maxResults);
		}
		return dao.getListPageable(new CommonCriteria(), sortOrder, firstResult, maxResults);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cc.macloud.core.common.service.DomainService#save(java.lang.Object)
	 */

	@Transactional(readOnly = false)
	public T save(T entity) throws CoreException {
		if (cache != null) {
			try {
				cache.remove(BeanUtils.getSimpleProperty(entity, cacheAttribute));
			} catch (Exception e) {
				logger.warn("remove cache fail, Object:{}", entity);
			}
		}
		return dao.saveOrUpdate(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cc.macloud.core.common.service.DomainService#getAllSize()
	 */

	public Number getAllSize() throws CoreException {
		return dao.getListSize(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cc.macloud.core.common.service.DomainService#getList(int, int,
	 * cc.macloud.core.common.dao.impl.CommonCriteria)
	 */

	public List<T> getList(int firstResult, int maxResults, CommonCriteria criteria, String[] sortOrder)
			throws CoreException {
		if (sortOrder == null) {
			return dao.getListPageable(criteria, defaultSort, firstResult, maxResults);
		}
		return dao.getListPageable(criteria, sortOrder, firstResult, maxResults);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cc.macloud.core.common.service.DomainService#getListSize(com.yaodian .core.common.dao.impl.CommonCriteria)
	 */

	public Number getListSize(CommonCriteria criteria) throws CoreException {
		return dao.getListSize(criteria);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cc.macloud.core.common.service.DomainService#getSingle(cc.macloud.core.common.dao.impl.CommonCriteria,
	 * java.lang.String[])
	 */

	public T getSingle(CommonCriteria criteria, String[] sortOrder) throws CoreException {
		T result = null;
		List<T> resultList = dao.getListPageable(criteria, sortOrder, 0, 1);
		if ((resultList != null) && (resultList.size() > 0)) {
			result = resultList.get(0);
		}
		return result;
	}
}
