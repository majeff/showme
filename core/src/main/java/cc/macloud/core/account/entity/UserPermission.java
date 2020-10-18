/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.account.entity.UserPermission
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
package cc.macloud.core.account.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import cc.macloud.core.common.utils.StringUtils;

/**
 * @author jeffma
 */
@Entity
@Table(name = "CORE_ACC_USER_PERMISSION", uniqueConstraints = { @UniqueConstraint(columnNames = { "USER_NAME",
		"PERMISSION_KEY" }) })
public class UserPermission implements GrantedAuthority {

	/** serialVersionUID */
	private static final long serialVersionUID = -4644124967784324988L;
	@Id
	@GeneratedValue(generator = "gen_uuid")
	@GenericGenerator(name = "gen_uuid", strategy = "uuid")
	@Column(name = "OBJ_UUID", length = 32)
	private String uuid;
	@ManyToOne
	@JoinColumn(name = "USER_NAME")
	private User user;
	@Column(name = "PERMISSION_KEY", length = 50)
	private String permissionKey;
	@Enumerated(EnumType.STRING)
	@Column(name = "PERMISSION_TYPE", length = 20, updatable = false, nullable = false)
	private Permission.Type permissionType;
	@Column(name = "END_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	/** default constructors */
	@SuppressWarnings("unused")
	private UserPermission() {
	}

	/** default constructors */
	public UserPermission(User user, String permissionKey, Permission.Type permissionType) {
		super();
		this.user = user;
		this.permissionKey = permissionKey;
		this.permissionType = permissionType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.GrantedAuthority#getAuthority()
	 */

	public String getAuthority() {
		return permissionKey;
	}

	/**
	 * @return the permissionType
	 */
	public Permission.Type getPermissionType() {
		return permissionType;
	}

	/**
	 * @param permissionType
	 *           the permissionType to set
	 */
	public void setPermissionType(Permission.Type permissionType) {
		this.permissionType = permissionType;
	}

	/**
	 * @return the user
	 */
	@SuppressWarnings("unused")
	private User getUser() {
		return user;
	}

	/**
	 * @param user
	 *           the user to set
	 */
	@SuppressWarnings("unused")
	private void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the permissionKey
	 */
	public String getPermissionKey() {
		return permissionKey;
	}

	/**
	 * @return the permissionCode
	 */
	@Transient
	public String getPermissionCode() {
		String code = "";
		if (StringUtils.isNotBlank(permissionKey) && (permissionType != null)) {
			code = StringUtils.removeStart(permissionKey, permissionType.name() + "_");
		}
		return code;
	}

	/**
	 * @param permissionKey
	 *           the permissionKey to set
	 */
	public void setPermissionKey(String permissionKey) {
		this.permissionKey = permissionKey;
	}

	/**
	 * @return the endDT
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDT
	 *           the endDT to set
	 */
	public void setEndDate(Date endDT) {
		this.endDate = endDT;
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

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("permissionKey", this.permissionKey)
				.append("uuid", this.uuid).toString();
	}

	/**
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(Object object) {
		UserPermission myClass = (UserPermission) object;
		return new CompareToBuilder().append(this.permissionKey, myClass.permissionKey)
				.append(this.permissionType, myClass.permissionType).append(this.user, myClass.user).toComparison();
	}

}
