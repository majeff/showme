/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.account.dao.impl.UserDaoImpl
   Module Description   :

   Date Created      : 2009/11/19
   Original Author   : jeff.ma
   Team              :
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package com.showmoney.core.account.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.showmoney.core.account.dao.UserDao;
import com.showmoney.core.account.entity.Role;
import com.showmoney.core.account.entity.User;
import com.showmoney.core.common.dao.impl.CommonCriteria;
import com.showmoney.core.common.dao.impl.HibernateObjectDaoImpl;
import com.showmoney.core.common.exception.CoreException;

/**
 * @author jeff.ma
 *
 */
@SuppressWarnings("unchecked")
public class UserDaoImpl extends HibernateObjectDaoImpl<User> implements UserDao {

	public UserDaoImpl() throws ClassNotFoundException {
		super(User.class.getName());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.showmoney.core.account.dao.UserDao#getByRole(com.showmoney.core.
	 * account.entity.Role)
	 */
	public List<User> getByRole(final Role role) throws CoreException {
		return (List<User>) getHibernateTemplate().find("from User u where ? in elements(u.roles) order by u.username",
				role);
	}

	public List<User> getListPageableByRole(final CommonCriteria cri, final String[] sortOrder, final int startNode,
			final int returnSize, Role.Type roleType, String roleKey) throws CoreException {
		if (startNode < 0) {
			throw new CoreException(CoreException.ERROR_DB, "startNode less than zero");
		}

		List<User> result = null;
		try {
			List<Object> objs = new ArrayList<Object>();
			StringBuffer hql = new StringBuffer();
			hql.append("select distinct o ").append(this.buildHqlByRole(cri, null, objs, roleType, roleKey));
			logger.debug("hql:{}, attrs:{}", hql, objs);

			Session s = getHibernateTemplate().getSessionFactory().getCurrentSession();
			Query q = s.createQuery(hql.toString());
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

	public Number getListSizeByRole(CommonCriteria cri, Role.Type roleType, String roleKey) throws CoreException {
		Number result = null;
		try {
			List<Object> objs = new ArrayList<Object>();
			StringBuffer hql = new StringBuffer();
			hql.append("select count( distinct o) ").append(this.buildHqlByRole(cri, null, objs, roleType, roleKey));
			List<Number> r = (List<Number>) getHibernateTemplate().find(hql.toString(), objs.toArray());
			if ((r != null) && (r.size() > 0)) {
				result = r.get(0);
			} else {
				result = Integer.valueOf(0);
			}

		} catch (HibernateException e) {
			throw new CoreException(CoreException.ERROR_DB, e);
		}
		return result;
	}

	private String buildHqlByRole(CommonCriteria cri, String[] sortOrder, List<Object> objs, Role.Type roleType,
			String roleKey) {
		StringBuffer hql = new StringBuffer(super.buildHql(User.class.getName(), cri, sortOrder, objs));
		int index1 = hql.indexOf("User o");
		hql.insert(index1 + 6, ",Role r");
		int index2 = hql.indexOf("where", index1);
		boolean hasMore = true;
		if (index2 == -1) {
			index2 = hql.indexOf("Role r");
			hql.insert(index2 + 6, " where");
			index2 = hql.indexOf("where", index1);
			hasMore = false;
		}
		String str = null;
		if (roleKey != null) {
			str = " r in elements(o.roles) and r.key = ? ";
			if (hasMore) {
				str = str + " and ";
			}
			objs.add(0, roleKey);
		}
		if (roleType != null) {
			str = " r in elements(o.roles) and r.type = ? ";
			if (hasMore) {
				str = str + " and ";
			}
			objs.add(0, roleType);
		}
		hql.insert(index2 + 5, str);

		return hql.toString();

	}
}
