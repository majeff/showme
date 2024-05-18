/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.common.dao.impl.CommonCriteria
   Module Description   :

   Date Created      : 2009/10/8
   Original Author   : jeffma
   Team              :
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.common.utils.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cc.macloud.core.common.exception.CoreException;
import cc.macloud.core.common.utils.StringUtils;

/**
 * @author jeffma
 *
 */
public class CommonCriteria implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 7028666707777334979L;
	private Map<String, Serializable> eq = null;
	private Map<String, Serializable> ne = null;
	private Map<String, Serializable> ge = null;
	private Map<String, Serializable> le = null;
	private Map<String, String> rlike = null;
	private Map<String, Collection<Serializable>> in = null;

	/** default constructors */
	public CommonCriteria() {
	}

	/**
	 * @param eq
	 * @param ne
	 * @param ge
	 * @param le
	 * @param rlike
	 * @param in
	 * @param order
	 */
	public CommonCriteria(Map<String, Serializable> eq, Map<String, Serializable> ne, Map<String, Serializable> ge,
			Map<String, Serializable> le, Map<String, String> rlike, Map<String, Collection<Serializable>> in) {
		this.eq = eq;
		this.ne = ne;
		this.ge = ge;
		this.le = le;
		this.rlike = rlike;
		this.in = in;
	}

	public final String buildHql(String className, String[] sortOrder, List values)
			throws CoreException {
		StringBuffer hql = new StringBuffer();

		if ((getEq() != null) && (getEq().size() > 0)) {
			Iterator it = getEq().keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				Object value = getEq().get(key);
				if (hql.length() > 0) {
					hql.append(" and");
				}
				if (value != null) {
					hql.append(" o." + key + "=?");
					values.add(value);
				} else {
					hql.append(" o." + key + " is null");
				}
			}
		}
		if ((getNe() != null) && (getNe().size() > 0)) {
			Iterator it = getNe().keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				Object value = getNe().get(key);
				if (hql.length() > 0) {
					hql.append(" and");
				}
				if (value != null) {
					hql.append(" o." + key + "!=?");
					values.add(value);
				} else {
					hql.append(" o." + key + " is not null");
				}
			}
		}
		if ((getGe() != null) && (getGe().size() > 0)) {
			Iterator it = getGe().keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				Object value = getGe().get(key);
				if (value != null) {
					if (hql.length() > 0) {
						hql.append(" and");
					}
					hql.append(" o." + key + ">=?");
					values.add(value);
				}
			}
		}
		if ((getLe() != null) && (getLe().size() > 0)) {
			Iterator it = getLe().keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				Object value = getLe().get(key);
				if (value != null) {
					if (hql.length() > 0) {
						hql.append(" and");
					}
					hql.append(" o." + key + "<=?");
					values.add(value);
				}
			}
		}
		if ((getRlike() != null) && (getRlike().size() > 0)) {
			Iterator it = getRlike().keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				Object value = getRlike().get(key);
				if (value != null) {
					if (hql.length() > 0) {
						hql.append(" and");
					}
					hql.append(" o." + key + " like ?");
					values.add(value + "%");
				}
			}
		}
		if (this instanceof CriteriaInRlike) {
			CriteriaInRlike criInlike = (CriteriaInRlike) this;
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
						values.add(o + "%");
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
		if ((getIn() != null) && (getIn().size() > 0)) {
			Iterator it = getIn().keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				Collection value = getIn().get(key);
				if (value != null) {
					if (hql.length() > 0) {
						hql.append(" and");
					}
					hql.append(" o." + key + " in (");
					for (int i = 0; i < value.size(); i++) {
						hql.append(i == 0 ? "?" : ", ?");
						values.add(value.toArray()[i]);
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

	/**
	 * @return the eq
	 */
	public Map<String, Serializable> getEq() {
		return eq;
	}

	/**
	 * @return the criteria
	 */
	public CommonCriteria addEq(String key, Serializable value) {
		if (eq == null) {
			eq = new HashMap<String, Serializable>();
		}
		eq.put(key, value);
		return this;
	}

	/**
	 * @param eq
	 *           the eq to set
	 */
	public void setEq(Map<String, Serializable> eq) {
		this.eq = eq;
	}

	/**
	 * @return the ne
	 */
	public Map<String, Serializable> getNe() {
		return ne;
	}

	/**
	 * @return the criteria
	 */
	public CommonCriteria addNe(String key, Serializable value) {
		if (ne == null) {
			ne = new HashMap<String, Serializable>();
		}
		ne.put(key, value);
		return this;
	}

	/**
	 * @param ne
	 *           the ne to set
	 */
	public void setNe(Map<String, Serializable> ne) {
		this.ne = ne;
	}

	/**
	 * @return the ge
	 */
	public Map<String, Serializable> getGe() {
		return ge;
	}

	/**
	 * @return the criteria
	 */
	public CommonCriteria addGe(String key, Serializable value) {
		if (ge == null) {
			ge = new HashMap<String, Serializable>();
		}
		ge.put(key, value);
		return this;
	}

	/**
	 * @param ge
	 *           the ge to set
	 */
	public void setGe(Map<String, Serializable> ge) {
		this.ge = ge;
	}

	/**
	 * @return the le
	 */
	public Map<String, Serializable> getLe() {
		return le;
	}

	/**
	 * @return the criteria
	 */
	public CommonCriteria addLe(String key, Serializable value) {
		if (le == null) {
			le = new HashMap<String, Serializable>();
		}
		le.put(key, value);
		return this;
	}

	/**
	 * @param le
	 *           the le to set
	 */
	public void setLe(Map<String, Serializable> le) {
		this.le = le;
	}

	/**
	 * @return the rlike
	 */
	public Map<String, String> getRlike() {
		return rlike;
	}

	/**
	 * @return the criteria
	 */
	public CommonCriteria addRlike(String key, String value) {
		if (rlike == null) {
			rlike = new HashMap<String, String>();
		}
		rlike.put(key, value);
		return this;
	}

	/**
	 * @param rlike
	 *           the rlike to set
	 */
	public void setRlike(Map<String, String> rlike) {
		this.rlike = rlike;
	}

	/**
	 * @return the in
	 */
	public Map<String, Collection<Serializable>> getIn() {
		return in;
	}

	/**
	 * @return the criteria
	 */
	public CommonCriteria addIn(String key, Collection<Serializable> value) {
		if (in == null) {
			in = new HashMap<String, Collection<Serializable>>();
		}
		in.put(key, value);
		return this;
	}

	/**
	 * @param in
	 *           the in to set
	 */
	public void setIn(Map<String, Collection<Serializable>> in) {
		this.in = in;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CommonCriteria [eq=" + eq + ", ge=" + ge + ", in=" + in + ", le=" + le + ", ne=" + ne + ", rlike="
				+ rlike + "]";
	}

}
