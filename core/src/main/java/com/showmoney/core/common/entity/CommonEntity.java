/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.common.entity.BaseEntity
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
package com.showmoney.core.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author jeffma
 * 
 */
@MappedSuperclass
public abstract class CommonEntity implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -3140312755979345441L;

	private String createUser;

	private Date createDate;

	private String modifyUser;

	private Date modifyDate;

	/**
	 * @return the updateUser
	 */
	@Column(length = 30)
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
	@Column(length = 30, updatable = false)
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
	@Temporal(TemporalType.TIMESTAMP)
	public Date getModifyDate() {
		return modifyDate;
	}

	/**
	 * @param updateDT
	 *           the updateDT to set
	 */
	public void setModifyDate(Date updateDT) {
		this.modifyDate = updateDT;
	}

	/**
	 * @return the createDT
	 */
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("updateDT", this.modifyDate)
				.append("updateUser", this.modifyUser).toString();
	}

}
