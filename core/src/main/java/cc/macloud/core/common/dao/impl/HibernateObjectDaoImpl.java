/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.common.dao.impl.HibernateObjectDaoImpl
   Module Description   :

   Date Created      : 2008/3/14
   Original Author   : jeffma
   Team              :
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.common.dao.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import cc.macloud.core.common.dao.ObjectDao;
import cc.macloud.core.common.entity.BaseEntity;
import cc.macloud.core.common.exception.CoreException;

/**
 * @author jeffma
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class HibernateObjectDaoImpl<T> extends HibernateDaoImpl implements ObjectDao<T> {

	private String className;
	private Class classObj;

	/** default constructors */
	public HibernateObjectDaoImpl() {
		super();
	}

	/**
	 * default constructors
	 *
	 * @throws ClassNotFoundException
	 */
	public HibernateObjectDaoImpl(String className) throws ClassNotFoundException {
		super();
		this.className = className;
		this.classObj = Class.forName(className);
	}

	/**
	 * default constructors
	 *
	 * @throws ClassNotFoundException
	 * @throws ClassNotFoundException
	 */
	public HibernateObjectDaoImpl(String entityName, SessionFactory sessionFactory) throws ClassNotFoundException {
		super();
		setSessionFactory(sessionFactory);
		this.className = entityName;
		this.classObj = Class.forName(entityName);
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            the className to set
	 * @throws ClassNotFoundException
	 */
	public void setClassName(String className) throws ClassNotFoundException {
		this.className = className;
		classObj = Class.forName(className);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.ObjectDao#delete(java.lang.Object)
	 */

	public void delete(final Object obj) throws CoreException {
		try {
			logger.debug("object: {}", obj);
			super.delete(obj);
		} catch (HibernateException e) {
			throw new CoreException(CoreException.ERROR_DB, e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.ObjectDao#deleteBatch(java.util.Collection)
	 */

	public void deleteBatch(final Collection objects) throws CoreException {
		try {
			super.deleteBatch(objects);
		} catch (HibernateException e) {
			throw new CoreException("errors.system.db", e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.ObjectDao#deleteByAttributes(java.util.Map,
	 * java.util.Map, java.util.Map, java.util.Map)
	 */

	public int deleteByAttributes(final CommonCriteria cri) throws CoreException {
		return super.deleteByAttributes(classObj, cri);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.ObjectDao#deleteByPK(java.io.Serializable)
	 */

	public void deleteByPK(final Serializable oid) throws CoreException {
		try {
			logger.debug("class: {}/{}", className, oid);
			Object obj = this.get(oid);
			super.delete(obj);
		} catch (HibernateException e) {
			throw new CoreException(CoreException.ERROR_DB, e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.ObjectDao#flush()
	 */

	public void flush() throws CoreException {
		try {
			super.flush();
		} catch (HibernateException e) {
			throw new CoreException(CoreException.ERROR_DB, e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.ObjectDao#get(java.io.Serializable)
	 */

	public T get(final Serializable oid) throws CoreException {
		T result = null;
		try {
			logger.debug("object: {}, oid: {}", className, oid);
			result = (T) getHibernateTemplate().get(className, oid);
		} catch (HibernateException e) {
			throw new CoreException(CoreException.ERROR_DB, e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.ObjectDao#getAttributes(java.lang.String[],
	 * java.util.Map, java.util.Map, java.util.Map, java.util.Map,
	 * java.lang.String[])
	 */

	public List getAttributes(final String[] attributeNames, final CommonCriteria cri, final String[] sortOrder)
			throws CoreException {
		return super.getAttributes(classObj, attributeNames, cri, sortOrder);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * cc.macloud.core.dao.ObjectDao#getAttributesPageable(java.lang.String[]
	 * , java.util.Map, java.util.Map, java.util.Map, java.util.Map,
	 * java.lang.String[], int, int)
	 */

	public List getAttributesPageable(final String[] attributeNames, final CommonCriteria cri, final String[] sortOrder,
			final int startNode, final int returnSize) throws CoreException {
		List<T> result = null;
		if ((startNode == 0) && (returnSize == -1)) {
			result = this.getAttributes(attributeNames, cri, sortOrder);
		} else {
			result = super.getAttributesPageable(classObj, attributeNames, cri, sortOrder, startNode, returnSize);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.ObjectDao#getList(java.util.Map,
	 * java.util.Map, java.util.Map, java.util.Map, java.lang.String[])
	 */

	public List<T> getList(final CommonCriteria cri, final String[] sortOrder) throws CoreException {
		return super.getList(classObj, cri, sortOrder);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.ObjectDao#getListPageable(java.util.Map,
	 * java.util.Map, java.util.Map, java.util.Map, java.lang.String[], int,
	 * int)
	 */

	public List<T> getListPageable(final CommonCriteria cri, final String[] sortOrder, final int startNode,
			final int returnSize) throws CoreException {
		List<T> result = null;
		if ((startNode == 0) && (returnSize == -1)) {
			result = this.getList(classObj, cri, sortOrder);
		} else {
			result = super.getListPageable(classObj, cri, sortOrder, startNode, returnSize);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.ObjectDao#getListSize(java.util.Map,
	 * java.util.Map, java.util.Map, java.util.Map)
	 */

	public Number getListSize(final CommonCriteria cri) throws CoreException {
		return super.getListSize(classObj, cri);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.ObjectDao#getMap(java.lang.String,
	 * java.util.Map, java.util.Map, java.util.Map, java.util.Map,
	 * java.lang.String[])
	 */

	public Map getMap(final String mapKey, final CommonCriteria cri, final String[] sortOrder) throws CoreException {
		return super.getMap(classObj, mapKey, cri, sortOrder);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.ObjectDao#getSingle(java.util.Map,
	 * java.util.Map, java.util.Map, java.util.Map, java.lang.String[])
	 */

	public T getSingle(final CommonCriteria cri, final String[] sortOrder) throws CoreException {
		List objs = getListPageable(cri, sortOrder, 0, 1);
		return (T) (((objs != null) && (objs.size() > 0)) ? objs.get(0) : null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.ObjectDao#getSingle(java.lang.String,
	 * java.io.Serializable)
	 */

	public T getSingle(final String key, final Serializable value) throws CoreException {
		Map conditions = new HashMap();
		conditions.put(key, value);
		return getSingle(new CommonCriteria(), null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.ObjectDao#load(java.io.Serializable)
	 */

	public T load(final Serializable oid) throws CoreException {
		T result = null;
		try {
			result = (T) getHibernateTemplate().load(className, oid);
		} catch (HibernateException e) {
			throw new CoreException(CoreException.ERROR_DB, e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.ObjectDao#save(java.lang.Object)
	 */

	public T save(final Object obj) throws CoreException {
		try {
			if (obj instanceof BaseEntity) {
				updateCommonAttribute((BaseEntity) obj);
			}
			getHibernateTemplate().save(obj);
		} catch (HibernateException e) {
			throw new CoreException(CoreException.ERROR_DB, e);
		}
		return (T) obj;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.ObjectDao#saveOrUpdate(java.lang.Object)
	 */

	public T saveOrUpdate(final Object obj) throws CoreException {
		try {
			if (obj instanceof BaseEntity) {
				updateCommonAttribute((BaseEntity) obj);
			}
			getHibernateTemplate().saveOrUpdate(obj);
		} catch (HibernateException e) {
			throw new CoreException(CoreException.ERROR_DB, e);
		}
		return (T) obj;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * cc.macloud.core.dao.ObjectDao#saveOrUpdateBatch(java.util.Collection)
	 */

	public void saveOrUpdateBatch(final Collection objs) throws CoreException {
		try {
			for (Object obj : objs) {
				if (obj instanceof BaseEntity) {
					updateCommonAttribute((BaseEntity) obj);
					getHibernateTemplate().saveOrUpdate(obj);
				} else {
					getHibernateTemplate().saveOrUpdate(obj);
				}
			}

		} catch (HibernateException e) {
			throw new CoreException(CoreException.ERROR_DB, e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.ObjectDao#update(java.lang.Object)
	 */
	public T update(final Object obj) throws CoreException {
		try {
			if (obj instanceof BaseEntity) {
				updateCommonAttribute((BaseEntity) obj);
			}
			getHibernateTemplate().update(obj);
		} catch (HibernateException e) {
			throw new CoreException(CoreException.ERROR_DB, e);
		}
		return (T) obj;
	}
}
