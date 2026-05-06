/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.account.dao.impl.UserDaoImpl
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
package cc.macloud.core.account.dao.impl;

import java.util.ArrayList;
import java.util.List;

import cc.macloud.core.account.dao.UserDao;
import cc.macloud.core.account.entity.Permission;
import cc.macloud.core.account.entity.Role;
import cc.macloud.core.account.entity.User;
import cc.macloud.core.common.dao.impl.JpaObjectDaoImpl;
import cc.macloud.core.common.exception.CoreException;
import cc.macloud.core.common.utils.dao.CommonCriteria;
import jakarta.persistence.Query;

/**
 * @author jeff.ma
 *
 */
@SuppressWarnings("unchecked")
public class UserDaoImpl extends JpaObjectDaoImpl<User> implements UserDao {

	public UserDaoImpl() throws ClassNotFoundException {
		super(User.class.getName());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.account.dao.UserDao#getByRole(cc.macloud.core.
	 * account.entity.Role)
	 */
	public List<User> getByRole(final Role role) throws CoreException {
		return (List<User>) em.createNamedQuery("from User u where ? in elements(u.roles) order by u.username").setParameter(0, role).getResultList();
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

			Query q = em.createQuery(hql.toString()); // Replace createQuery(String) with createQuery(String, Class)
			for (int i = 0; i < objs.size(); i++) {
				Object obj = objs.get(i);
				q.setParameter(i, obj);
			}
			q.setFirstResult(startNode);
			if (returnSize > 0) {
				q.setMaxResults(returnSize);
			}
			result = q.getResultList();
		} catch (Exception e) {
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
			List<Number> r = (List<Number>) em.createNamedQuery(hql.toString()).getResultList();
			if ((r != null) && (r.size() > 0)) {
				result = r.get(0);
			} else {
				result = Integer.valueOf(0);
			}

		} catch (Exception e) {
			throw new CoreException(CoreException.ERROR_DB, e);
		}
		return result;
	}

	private String buildHqlByRole(CommonCriteria cri, String[] sortOrder, List<Object> objs, Role.Type roleType,
			String roleKey) {
		StringBuffer hql = new StringBuffer(cri.buildHql(User.class.getName(), sortOrder, objs));
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

	@Override
	public List<User> getByPermission(Permission p) throws CoreException {
		return (List<User>) em.createQuery("select u from User u join u.roles r join r.permissions p where p = :p", User.class)
				.setParameter("p", p).getResultList();
	}
}
