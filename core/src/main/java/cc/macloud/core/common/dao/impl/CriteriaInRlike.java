/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.common.dao.impl.CriteriaInRlike
   Module Description   :

   Date Created      : 2011/2/11
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
import java.util.Map;

/**
 * @author jeffma
 * 
 */
public class CriteriaInRlike extends CommonCriteria {

	/** serialVersionUID */
	private static final long serialVersionUID = 4523605276171523898L;
	private Map<String, Collection<Serializable>> inRlike = null;

	/** default constructor */
	public CriteriaInRlike() {
		super();
	}

	/** default constructor */
	public CriteriaInRlike(CommonCriteria cri) {
		super();
		if (cri != null) {
			this.setEq(cri.getEq());
			this.setNe(cri.getNe());
			this.setGe(cri.getGe());
			this.setLe(cri.getLe());
			this.setRlike(cri.getRlike());
			this.setIn(cri.getIn());
		}
	}

	/**
	 * @return the criteria
	 */
	public CommonCriteria addInRlike(String key, Collection<Serializable> value) {
		getInRlike().put(key, value);
		return this;
	}

	/**
	 * @return the inRlike
	 */
	public Map<String, Collection<Serializable>> getInRlike() {
		if (inRlike == null) {
			inRlike = new HashMap<String, Collection<Serializable>>();
		}
		return inRlike;
	}

	/**
	 * @param inRlike
	 *           the inRlike to set
	 */
	public void setInRlike(Map<String, Collection<Serializable>> inRlike) {
		this.inRlike = inRlike;
	}

}
