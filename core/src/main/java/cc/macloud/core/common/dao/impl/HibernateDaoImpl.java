/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.common.dao.impl.HibernateDaoImpl
   Module Description   :

   Date Created      : Mar 21, 2008
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.persister.collection.mutation.RowMutationOperations.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import cc.macloud.core.account.entity.User;
import cc.macloud.core.account.utils.AdminHelper;
import cc.macloud.core.common.dao.CoreDao;
import cc.macloud.core.common.entity.BaseEntity;
import cc.macloud.core.common.entity.UserDetails;
import cc.macloud.core.common.exception.CoreException;
import cc.macloud.core.common.utils.StringUtils;
import jakarta.persistence.criteria.CriteriaQuery;
import net.sf.ehcache.search.expression.Criteria;

/**
 * @author jeffma
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class HibernateDaoImpl extends HibernateDaoSupport implements CoreDao {

	/** logger */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/** default constructors */
	public HibernateDaoImpl() {
		super();
	}

	/**
	 * @param className
	 * @param equal
	 * @param nonequal
	 * @param moreequal
	 * @param lessequal
	 * @param like
	 * @param sortOrder
	 * @return
	 * @throws BTException
	 */
	protected final CriteriaQuery buildCriteria(Class c, Map equal, Map nonequal, Map moreequal, Map lessequal,
			Map like, Map in, String[] sortOrder) throws CoreException {
		Session session = this.currentSession();
		Criteria result = null;
		try {
			CriteriaQuery q = new CriteriaQuery();
			result = session.createQuery(q);

			if (MapUtils.isNotEmpty(equal)) {
				Iterator it = equal.keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					Object value = equal.get(key);
					if (value == null) {
						result.add(Restrictions.isNull(key));
					} else {
						result.add(Restrictions.eq(key, value));
					}
				}
			}

			if (MapUtils.isNotEmpty(nonequal)) {
				Iterator it = nonequal.keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					Object value = nonequal.get(key);
					if (value == null) {
						result.add(Restrictions.isNotNull(key));
					} else {
						result.add(Restrictions.ne(key, value));
					}
				}
			}

			if (MapUtils.isNotEmpty(moreequal)) {
				Iterator it = moreequal.keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					Object value = moreequal.get(key);
					result.add(Restrictions.ge(key, value));
				}
			}

			if (MapUtils.isNotEmpty(lessequal)) {
				Iterator it = lessequal.keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					Object value = lessequal.get(key);
					result.add(Restrictions.le(key, value));
				}
			}

			if (MapUtils.isNotEmpty(like)) {
				Iterator it = like.keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					String value = String.valueOf(like.get(key)) + "%";
					result.add(Restrictions.like(key, value));
				}
			}

			if (MapUtils.isNotEmpty(in)) {
				Iterator it = in.keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					Collection value = (Collection) in.get(key);
					result.add(Restrictions.in(key, value));
				}
			}

			if (sortOrder != null) {
				for (String orderBy : sortOrder) {
					String[] o = StringUtils.split(orderBy, " ");
					if (o.length == 1) {
						result.addOrder(Order.desc(orderBy));
					} else if ((o.length > 1) && "desc".equalsIgnoreCase(o[1])) {
						result.addOrder(Order.desc(o[0]));
					} else if ((o.length > 1) && "asc".equalsIgnoreCase(o[1])) {
						result.addOrder(Order.asc(o[0]));
					} else {
						throw new CoreException(CoreException.ERROR_DB, "unknow order type", orderBy);
					}
				}
			}
			logger.debug("criteria:{}", result.toString());
		} catch (HibernateException e) {
			throw new CoreException(CoreException.ERROR_DB, e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * cc.macloud.core.common.dao.CoreDao#executeUpdate(java.lang.String,
	 * java.util.Map)
	 */

	/**
	 * @param conditions
	 * @param nonconditions
	 * @param more
	 * @param less
	 * @param sortOrder
	 * @param objs
	 * @return hql string
	 */
	protected final String buildHql(String className, CommonCriteria cri, String[] sortOrder, List objs)
			throws CoreException {
		StringBuffer hql = new StringBuffer();
		if (cri == null) {
			cri = new CommonCriteria();
		}

		if ((cri.getEq() != null) && (cri.getEq().size() > 0)) {
			Iterator it = cri.getEq().keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				Object value = cri.getEq().get(key);
				if (hql.length() > 0) {
					hql.append(" and");
				}
				if (value != null) {
					hql.append(" o." + key + "=?");
					objs.add(value);
				} else {
					hql.append(" o." + key + " is null");
				}
			}
		}
		if ((cri.getNe() != null) && (cri.getNe().size() > 0)) {
			Iterator it = cri.getNe().keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				Object value = cri.getNe().get(key);
				if (hql.length() > 0) {
					hql.append(" and");
				}
				if (value != null) {
					hql.append(" o." + key + "!=?");
					objs.add(value);
				} else {
					hql.append(" o." + key + " is not null");
				}
			}
		}
		if ((cri.getGe() != null) && (cri.getGe().size() > 0)) {
			Iterator it = cri.getGe().keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				Object value = cri.getGe().get(key);
				if (value != null) {
					if (hql.length() > 0) {
						hql.append(" and");
					}
					hql.append(" o." + key + ">=?");
					objs.add(value);
				}
			}
		}
		if ((cri.getLe() != null) && (cri.getLe().size() > 0)) {
			Iterator it = cri.getLe().keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				Object value = cri.getLe().get(key);
				if (value != null) {
					if (hql.length() > 0) {
						hql.append(" and");
					}
					hql.append(" o." + key + "<=?");
					objs.add(value);
				}
			}
		}
		if ((cri.getRlike() != null) && (cri.getRlike().size() > 0)) {
			Iterator it = cri.getRlike().keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				Object value = cri.getRlike().get(key);
				if (value != null) {
					if (hql.length() > 0) {
						hql.append(" and");
					}
					hql.append(" o." + key + " like ?");
					objs.add(value + "%");
				}
			}
		}
		if (cri instanceof CriteriaInRlike) {
			CriteriaInRlike criInlike = (CriteriaInRlike) cri;
			if ((criInlike.getInRlike() != null) && (criInlike.getInRlike().size() > 0)) {
				StringBuffer subHql = new StringBuffer();
				Iterator it = criInlike.getInRlike().keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					Collection value = criInlike.getInRlike().get(key);
					for (Object o : value) {
						if (subHql.length() > 0) {
							subHql.append(" or");
						}
						subHql.append(" o." + key + " like ?");
						objs.add(o + "%");
					}
				}
				if ((hql.length() > 0) && (subHql.length() > 0)) {
					hql.append(" and");
				}
				if (subHql.length() > 0) {
					hql.append(" (").append(subHql).append(" )");
				}
			}
		}
		if ((cri.getIn() != null) && (cri.getIn().size() > 0)) {
			Iterator it = cri.getIn().keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				Collection value = cri.getIn().get(key);
				if (value != null) {
					if (hql.length() > 0) {
						hql.append(" and");
					}
					hql.append(" o." + key + " in (");
					for (int i = 0; i < value.size(); i++) {
						hql.append(i == 0 ? "?" : ", ?");
						objs.add(value.toArray()[i]);
					}
					hql.append(")");
				}
			}
		}
		if (hql.length() > 0) {
			hql.insert(0, "from " + className + " o where");
		} else {
			hql.append("from " + className + " o");
		}
		if ((sortOrder != null) && (sortOrder.length > 0)) {
			hql.append(" order by");
			for (int i = 0; i < sortOrder.length; i++) {
				String key = sortOrder[i];
				String[] o = StringUtils.split(key, " ");
				if (i != 0) {
					hql.append(" ,");
				} else {
					hql.append(" ");
				}
				if (o.length == 1) {
					hql.append("o." + key + " desc");
				} else if ((o.length > 1) && "desc".equalsIgnoreCase(o[1])) {
					hql.append("o." + o[0] + " desc");

				} else if ((o.length > 1) && "asc".equalsIgnoreCase(o[1])) {
					hql.append("o." + o[0] + " asc");
				} else {
					throw new CoreException(CoreException.ERROR_DB, "unknow order type", o[1]);
				}
			}
		}

		return hql.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.CoreDao#delete(java.lang.Object)
	 */

	public void delete(final Object obj) throws CoreException {
		getHibernateTemplate().delete(obj);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.CoreDao#deleteBatch(java.util.Collection)
	 */

	public void deleteBatch(final Collection objects) throws CoreException {
		getHibernateTemplate().deleteAll(objects);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.CoreDao#deleteByAttributes(java.lang.Class,
	 * java.util.Map, java.util.Map, java.util.Map, java.util.Map)
	 */

	public int deleteByAttributes(final Class classObj, final CommonCriteria criteria) throws CoreException {
		int result = 0;
		try {
			ArrayList objs = new ArrayList();
			String hql = "delete " + buildHql(classObj.getName(), criteria, null, objs);

			logger.debug("hql:{}/{}", hql, criteria);
			if (!objs.isEmpty()) {
				result = getHibernateTemplate().bulkUpdate(hql, objs.toArray());
			} else {
				throw new CoreException(CoreException.ERROR_DB, "couldn't delete all objects", classObj.getName());
			}
		} catch (RuntimeException re) {
			throw new CoreException(CoreException.ERROR_DB, re, re.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.CoreDao#deleteByPK(java.lang.Class,
	 * java.io.Serializable)
	 */

	public void deleteByPK(final Class classObj, final Serializable oid) throws CoreException {
		Object obj = get(classObj, oid);
		getHibernateTemplate().delete(obj);
	}

	public int executeUpdate(String queryName, Map attrs) throws CoreException {
		Query queryObject = currentSession().getNamedQuery(queryName);
		if (MapUtils.isNotEmpty(attrs)) {
			Iterator<String> it = attrs.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				queryObject.setParameter(key, attrs.get(key));
			}
		}

		return queryObject.executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.CoreDao#flush()
	 */

	public void flush() throws CoreException {
		getHibernateTemplate().flush();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.CoreDao#get(java.lang.Class,
	 * java.io.Serializable)
	 */

	public Object get(final Class classObj, final Serializable oid) throws CoreException {
		Object result = null;
		try {
			logger.debug("get {}, oid:{}", classObj.getName(), oid);
			result = getHibernateTemplate().load(classObj, oid);
		} catch (Exception e) {
			throw new CoreException(CoreException.ERROR_DB, e, e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.CoreDao#getAttributes(java.lang.Class,
	 * java.lang.String[], java.util.Map, java.util.Map, java.util.Map,
	 * java.util.Map, java.lang.String[])
	 */

	public List getAttributes(final Class classObj, final String[] attributeNames, final CommonCriteria cri,
			final String[] sortOrder) throws CoreException {
		List result = null;
		try {
			Criteria criteria = null;
			if (cri != null) {
				criteria = buildCriteria(classObj.getName(), cri.getEq(), cri.getNe(), cri.getGe(), cri.getLe(),
						cri.getRlike(), cri.getIn(), sortOrder);
			} else {
				criteria = buildCriteria(classObj.getName(), null, null, null, null, null, null, sortOrder);
			}

			ProjectionList projection = Projections.projectionList();
			for (String attribute : attributeNames) {
				projection.add(Projections.property(attribute));
			}
			criteria.setProjection(projection);

			result = criteria.list();
		} catch (HibernateException e) {
			throw new CoreException(CoreException.ERROR_DB, e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * cc.macloud.core.dao.CoreDao#getAttributesPageable(java.lang.Class,
	 * java.lang.String[], java.util.Map, java.util.Map, java.util.Map,
	 * java.util.Map, java.lang.String[], int, int)
	 */

	public List getAttributesPageable(final Class classObj, final String[] attributeNames, final CommonCriteria cri,
			final String[] sortOrder, final int startNode, final int returnSize) throws CoreException {
		List result = null;
		try {
			Criteria criteria = null;
			if (cri != null) {
				criteria = buildCriteria(classObj.getName(), cri.getEq(), cri.getNe(), cri.getGe(), cri.getLe(),
						cri.getRlike(), cri.getIn(), sortOrder);
			} else {
				criteria = buildCriteria(classObj.getName(), null, null, null, null, null, null, sortOrder);
			}

			ProjectionList projection = Projections.projectionList();
			for (String attribute : attributeNames) {
				projection.add(Projections.property(attribute));
			}
			criteria.setProjection(projection);
			criteria.setFirstResult(startNode);
			if (returnSize > 0) {
				criteria.setMaxResults(returnSize);
			}

			result = criteria.list();
		} catch (HibernateException e) {
			throw new CoreException(CoreException.ERROR_DB, e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.CoreDao#getList(java.lang.Class,
	 * java.util.Map, java.util.Map, java.util.Map, java.util.Map,
	 * java.lang.String[])
	 */

	public List getList(final Class classObj, final CommonCriteria cri, final String[] sortOrder) throws CoreException {
		List result = null;
		try {
			// Criteria criteria = null;
			// if (cri != null) {
			// criteria = buildCriteria(classObj.getName(), cri.getEq(),
			// cri.getNe(), cri.getGe(), cri.getLe(), cri
			// .getRlike(), cri.getIn(), sortOrder);
			// } else {
			// criteria = buildCriteria(classObj.getName(), null, null, null,
			// null,
			// null, null, sortOrder);
			// }
			//
			// result = criteria.list();

			String hql = null;
			List objs = new ArrayList();
			if (cri != null) {
				hql = buildHql(classObj.getName(), cri, sortOrder, objs);
			} else {
				hql = buildHql(classObj.getName(), null, sortOrder, objs);
			}
			logger.debug("hql:{} / data:{}", hql, objs);
			result = getHibernateTemplate().find(hql, objs.toArray());
		} catch (RuntimeException re) {
			re.printStackTrace();
			throw new CoreException(CoreException.ERROR_DB, re, re.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.CoreDao#getListPageable(java.lang.Class,
	 * java.util.Map, java.util.Map, java.util.Map, java.util.Map,
	 * java.lang.String[], int, int)
	 */

	public List getListPageable(final Class classObj, final CommonCriteria cri, final String[] sortOrder,
			final int startNode, final int returnSize) throws CoreException {
		if (startNode < 0) {
			throw new CoreException(CoreException.ERROR_DB, "startNode less than zero");
		}

		List result = null;
		try {
			// Criteria criteria = null;
			// if (cri != null) {
			// criteria = buildCriteria(classObj.getName(), cri.getEq(),
			// cri.getNe(), cri.getGe(), cri.getLe(), cri
			// .getRlike(), cri.getIn(), sortOrder);
			// } else {
			// criteria = buildCriteria(classObj.getName(), null, null, null,
			// null,
			// null, null, sortOrder);
			// }
			//
			// criteria.setFirstResult(startNode);
			// if (returnSize > 0) {
			// criteria.setMaxResults(returnSize);
			// }
			//
			// result = criteria.list();
			String hql = null;
			List objs = new ArrayList();
			if (cri != null) {
				hql = buildHql(classObj.getName(), cri, sortOrder, objs);
			} else {
				hql = buildHql(classObj.getName(), null, sortOrder, objs);
			}
			logger.debug("hql:{}, attrs:{}", hql, objs);

			Session s = getHibernateTemplate().getSessionFactory().getCurrentSession();
			Query q = s.createQuery(hql);
			for (int i = 0; i < objs.size(); i++) {
				Object obj = objs.get(i);
				q.setParameter(i, obj);
			}
			q.setFirstResult(startNode);
			if (returnSize > 0) {
				q.setMaxResults(returnSize);
			}
			result = q.list();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new CoreException(CoreException.ERROR_DB, e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.CoreDao#getListSize(java.lang.Class,
	 * java.util.Map, java.util.Map, java.util.Map, java.util.Map)
	 */

	public Number getListSize(Class classObj, CommonCriteria cri) throws CoreException { // NOPMD
		Number result = null;
		try {
			String hql = null;
			List objs = new ArrayList();
			if (cri != null) {
				hql = buildHql(classObj.getName(), cri, null, objs);
			} else {
				hql = buildHql(classObj.getName(), null, null, objs);
			}
			List r = getHibernateTemplate().find("select count(*) " + hql, objs.toArray());
			if ((r != null) && (r.size() > 0)) {
				result = (Number) r.get(0);
			} else {
				result = Integer.valueOf(0);
			}

		} catch (HibernateException e) {
			throw new CoreException(CoreException.ERROR_DB, e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.CoreDao#getMap(java.lang.Class,
	 * java.lang.String, java.util.Map, java.util.Map, java.util.Map,
	 * java.util.Map, java.lang.String[])
	 */

	public Map getMap(Class classObj, String mapKey, CommonCriteria cri, String[] sortOrder) throws CoreException {
		Map result = null;
		try {
			List objs = getList(classObj, cri, sortOrder);
			if (objs != null) {
				result = new HashMap();
				Iterator it = objs.iterator();
				while (it.hasNext()) {
					Object obj = it.next();
					String key = BeanUtils.getProperty(obj, mapKey);
					result.put(key, obj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CoreException(CoreException.ERROR_DB, e, e.getMessage());
		}
		return result;
	}

	public List getNameQuery(String queryName, Map attrs, int firstResult, int maxResults) throws CoreException {
		List result = null;
		try {
			logger.debug("hql:{}/{}", queryName, attrs);

			Query queryObject = currentSession().getNamedQuery(queryName);
			if (MapUtils.isNotEmpty(attrs)) {
				Iterator<String> it = attrs.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					queryObject.setParameter(key, attrs.get(key));
				}
			}
			if (firstResult > 0) {
				queryObject.setFirstResult(firstResult);
			}
			if (maxResults > 0) {
				queryObject.setMaxResults(maxResults);
			}
			result = queryObject.list();
		} catch (HibernateException he) {
			throw new CoreException(CoreException.ERROR_DB, he, he.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * cc.macloud.core.common.dao.CoreDao#getQueryByList(java.lang.String,
	 * java.util.List, int, int)
	 */

	public List getQueryByList(String queryString, List attrs, int firstResult, int maxResults) throws CoreException {
		List result = null;
		try {
			logger.debug("hql:{}/{}", queryString, attrs);
			Query queryObject = this.currentSession().createQuery(queryString);
			if (attrs != null) {
				for (int i = 0; i < attrs.size(); i++) {
					queryObject.setParameter(i, attrs.get(i));
				}
			}
			if (firstResult > 0) {
				queryObject.setFirstResult(firstResult);
			}
			if (maxResults > 0) {
				queryObject.setMaxResults(maxResults);
			}
			result = queryObject.list();
		} catch (Exception e) {
			throw new CoreException(CoreException.ERROR_DB, e, e.getMessage());
		}
		return result;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * cc.macloud.core.common.dao.CoreDao#getQueryByMap(java.lang.String,
	 * java.util.Map, int, int)
	 */

	public List getQueryByMap(String queryString, Map attrs, int firstResult, int maxResults) throws CoreException {
		List result = null;
		try {
			logger.debug("hql:{}/{}", queryString, attrs);
			Query queryObject = this.currentSession().createQuery(queryString);
			if (MapUtils.isNotEmpty(attrs)) {
				Iterator<String> it = attrs.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					queryObject.setParameter(key, attrs.get(key));
				}
			}
			if (firstResult > 0) {
				queryObject.setFirstResult(firstResult);
			}
			if (maxResults > 0) {
				queryObject.setMaxResults(maxResults);
			}
			result = queryObject.list();
		} catch (Exception e) {
			throw new CoreException(CoreException.ERROR_DB, e, e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.CoreDao#getSingle(java.lang.Class,
	 * java.util.Map, java.util.Map, java.util.Map, java.util.Map,
	 * java.lang.String[])
	 */

	public Object getSingle(final Class classObj, final CommonCriteria cri, final String[] sortOrder)
			throws CoreException {
		List objs = getListPageable(classObj, cri, sortOrder, 0, 1);
		return ((objs != null) && (objs.size() > 0)) ? objs.get(0) : null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.CoreDao#getSingle(java.lang.Class,
	 * java.lang.String, java.io.Serializable)
	 */

	public Object getSingle(final Class classObj, final String key, final Serializable value) throws CoreException {
		Map conditions = new HashMap();
		conditions.put(key, value);
		List objects = getListPageable(classObj, new CommonCriteria(), null, 0, 1);
		return ((objects != null) && !objects.isEmpty()) ? objects.get(0) : null;
	}

	public List getSQLQueryByList(String queryString, List attrs, int firstResult, int maxResults)
			throws CoreException {
		List result = null;
		try {
			logger.debug("hql:{}/{}", queryString, attrs);
			SQLQuery sqlQueryObject = this.currentSession().createSQLQuery(queryString);
			if (attrs != null) {
				for (int i = 0; i < attrs.size(); i++) {
					sqlQueryObject.setParameter(i, attrs.get(i));
				}
			}
			if (firstResult > 0) {
				sqlQueryObject.setFirstResult(firstResult);
			}
			if (maxResults > 0) {
				sqlQueryObject.setMaxResults(maxResults);
			}
			result = sqlQueryObject.list();
		} catch (Exception e) {
			throw new CoreException(CoreException.ERROR_DB, e, e.getMessage());
		}
		return result;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.CoreDao#load(java.lang.Class,
	 * java.io.Serializable)
	 */

	public Object load(final Class classObj, final Serializable oid) throws CoreException {
		return getHibernateTemplate().load(classObj, oid);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.CoreDao#save(java.lang.Object)
	 */

	public Object save(Object obj) throws CoreException {
		if (obj instanceof BaseEntity) {
			updateCommonAttribute((BaseEntity) obj);
		}
		getHibernateTemplate().save(obj);
		return obj;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.CoreDao#saveOrUpdate(java.lang.Object)
	 */

	public Object saveOrUpdate(Object obj) throws CoreException {
		if (obj instanceof BaseEntity) {
			updateCommonAttribute((BaseEntity) obj);
		}
		getHibernateTemplate().saveOrUpdate(obj);
		return obj;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * cc.macloud.core.dao.CoreDao#saveOrUpdateBatch(java.util.Collection)
	 */

	public void saveOrUpdateBatch(Collection objs) throws CoreException {
		for (Object obj : objs) {
			if (obj instanceof BaseEntity) {
				updateCommonAttribute((BaseEntity) obj);
				getHibernateTemplate().saveOrUpdate(obj);
			} else {
				getHibernateTemplate().saveOrUpdate(obj);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.CoreDao#update(java.lang.Object)
	 */
	public Object update(Object obj) throws CoreException {
		if (obj instanceof BaseEntity) {
			updateCommonAttribute((BaseEntity) obj);
		}
		getHibernateTemplate().update(obj);
		return obj;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.dao.CoreDao#updateAttributes(java.lang.Class,
	 * java.lang.String, java.io.Serializable, java.util.Map)
	 */
	public void updateAttributes(Class classObj, String pkcolumnName, Serializable oid, Map attributes)
			throws CoreException {
		try {
			String className = classObj.getName().substring(classObj.getName().lastIndexOf(".") + 1);
			List conditions = new ArrayList();
			StringBuffer hql = new StringBuffer("update " + className + " o set");
			Object[] keys = attributes.keySet().toArray();
			for (int i = 0; i < keys.length; i++) {
				Object value = attributes.get(keys[i]);
				hql.append((i != 0) ? " ," : " ");
				if (value != null) {
					hql.append("o." + keys[i] + "=?");
					conditions.add(value);
				} else {
					hql.append("o." + keys[i] + "=null");
				}
			}
			hql.append(" where o." + pkcolumnName + "=?");
			conditions.add(oid);
			logger.debug("hql:{}/{}", hql, conditions);
			getHibernateTemplate().bulkUpdate(hql.toString(), conditions.toArray());
			getHibernateTemplate().flush();
		} catch (RuntimeException re) {
			re.printStackTrace();
			throw new CoreException(CoreException.ERROR_DB, re, re.getMessage());
		}
	}

	/**
	 * 更新 createDate, createUser, updateUser
	 *
	 * @param entity
	 */
	protected void updateCommonAttribute(BaseEntity entity) {
		if (entity.getCreateDate() == null) {
			entity.setCreateDate(Calendar.getInstance().getTime());

			User user = AdminHelper.getUser();
			if (user != null) {
				if (StringUtils.isBlank(entity.getOwnerGroup())) {
					entity.setOwnerGroup(user.getGroupCode());
				}
			} else {
				logger.warn("user not found. entity: {}", entity);
			}
		}

		UserDetails actor = AdminHelper.getUserDetails();
		if (actor != null) {
			if (entity.getModifyDate() == null) {
				entity.setCreateUser(actor.getUsername());
			}
			entity.setModifyUser(actor.getUsername());
		} else {
			logger.warn("user not found. entity: {}", entity);
		}

	}
}
