/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.common.entity.BaseEntity
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
package cc.macloud.core.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * @author jeffma
 * 
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -7699551082675380085L;

	@Column(name = "CREATE_USER", length = 30, updatable = false)
	private String createUser;
	@Column(name = "CREATE_DATE", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@Column(name = "MODIFY_USER", length = 30)
	private String modifyUser;
	@Version
	@Column(name = "MODIFY_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyDate;

	@Column(name = "OWNER_GROUP", length = 50)
	private String ownerGroup;

	/**
	 * @return the updateUser
	 */
	public String getModifyUser() {
		return modifyUser;
	}

	/**
	 * @param updateUser
	 *           the updateUser to set
	 */
	public void setModifyUser(String updateUser) {
		this.modifyUser = updateUser;
	}

	/**
	 * @return the createUser
	 */
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * @param createUser
	 *           the createUser to set
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	/**
	 * @return the updateDT
	 */
	public Date getModifyDate() {
		return modifyDate;
	}

	/**
	 * @param updateDT
	 *           the updateDT to set
	 */
	@SuppressWarnings("unused")
	private void setModifyDate(Date updateDT) {
		this.modifyDate = updateDT;
	}

	/**
	 * @return the createDT
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDT
	 *           the createDT to set
	 */
	public void setCreateDate(Date createDT) {
		this.createDate = createDT;
	}

	/**
	 * @return the ownerGroup
	 */
	public String getOwnerGroup() {
		return ownerGroup;
	}

	/**
	 * @param ownerGroup
	 *           the ownerGroup to set
	 */
	public void setOwnerGroup(String ownerGroup) {
		this.ownerGroup = ownerGroup;
	}
	//
	// /**
	// * @see java.lang.Object#toString()
	// */
	// @Override
	// public String toString() {
	// return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("updateDT", this.modifyDate)
	// .append("updateUser", this.modifyUser).toString();
	// }

}
