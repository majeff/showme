/*
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * Module Name : com.showmoney.core.account.entity.User
 * Module Description :
 * 
 * Date Created : Apr 16, 2008
 * Original Author : jeffma
 * Team : 
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * MODIFICATION HISTORY
 * ------------------------------------------------------------------------------
 * Date Modified Modified by Comments
 * ------------------------------------------------------------------------------
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */
package com.showmoney.core.account.entity;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.security.core.GrantedAuthority;

import com.showmoney.core.common.entity.BaseEntity;
import com.showmoney.core.common.entity.UserDetails;
import com.showmoney.core.common.utils.DateUtils;

/**
 * 使用者基本資料
 */
@Entity
@Cacheable
@Table(name = "CORE_ACC_USER")
public class User extends BaseEntity implements UserDetails {

	/** serialVersionUID */
	private static final long serialVersionUID = -3421287084848468867L;

	/** loginId */
	@Id
	@GeneratedValue(generator = "assigned")
	@GenericGenerator(name = "assigned", strategy = "assigned")
	@Column(name = "LOGIN_ID", length = 30, updatable = false)
	private String loginId;

	/** password */
	@Column(name = "USER_PASSWORD", length = 100, nullable = false)
	private String password;

	/** group */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GROUP_CODE", nullable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private Group group;

	@Column(name = "GROUP_CODE", insertable = false, updatable = false)
	private String groupCode;

	/** roles */
	@ManyToMany(cascade = CascadeType.REFRESH)
	@JoinTable(name = "CORE_ACC_USER_ROLE", joinColumns = @JoinColumn(name = "LOGIN_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_CODE"))
	@NotFound(action = NotFoundAction.IGNORE)
	@OrderBy("code")
	private Set<Role> roles;

	@OneToMany(targetEntity = UserPermission.class, mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@MapKeyColumn(name = "PERMISSION_KEY", length = 50)
	private Map<String, UserPermission> permissions;

	@Column(name = "LOGIN_IP", length = 15)
	private String loginIP;

	@Column(name = "USER_LANG", length = 5)
	private String lang = "zh_TW";

	@Column(name = "ERROR_COUNT", length = 5)
	private int errorCount = 0;

	@Column(name = "IS_PWD_NEED_CHANGE")
	private boolean needChangePassword = false;

	@Column(name = "ACC_EXPIRE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date accountExpireDate;

	@Column(name = "IS_ACC_NOT_LOCK")
	private boolean accountNonLocked = true;

	@Column(name = "PWD_EXPIRE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date credentialsExpireDate;

	@Column(name = "NATIVE_NAME", length = 50, nullable = false)
	private String nameNative;

	@Column(name = "USER_PHONE", length = 50)
	private String phone;

	@Column(name = "USER_MOBILE", length = 50)
	private String mobile;

	@Column(name = "USER_EMAIL", length = 50)
	private String email;

	/** default constructors */
	public User() {
	}

	/** default constructors */
	public User(String loginId) {
		this.loginId = loginId;
		this.nameNative = loginId;
		this.accountExpireDate = DateUtils.convertStringToDate("yyyy/MM/dd", "9999/12/31");
		this.credentialsExpireDate = DateUtils.convertStringToDate("yyyy/MM/dd", "9999/12/31");
	}

	/**
	 * @return the roles
	 */
	public Set<Role> getRoles() {
		if (roles == null) {
			roles = new HashSet<Role>();
		}
		return roles;
	}

	/**
	 * @param roles
	 *           the roles to set
	 */
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	/**
	 * @return the permissions
	 */
	public Map<String, UserPermission> getPermissions() {
		if (permissions == null) {
			permissions = new HashMap<String, UserPermission>();
		}
		return permissions;
	}

	/**
	 * @return the permissions
	 */
	public Map<String, UserPermission> getPermissions(Permission.Type type) {
		Map<String, UserPermission> result = new HashMap<String, UserPermission>();
		if (type == null) {
			result.putAll(getPermissions());
		} else {
			for (String k : getPermissions().keySet()) {
				UserPermission up = getPermissions().get(k);
				if (type.equals(up.getPermissionType())) {
					result.put(k, up);
				}
			}
		}
		return result;
	}

	/**
	 * @param permissions
	 *           the permissions to set
	 */
	public void setPermissions(Map<String, UserPermission> permissions) {
		this.permissions = permissions;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *           the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the lang
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * @param lang
	 *           the lang to set
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}

	/**
	 * @return the errorCount
	 */
	public int getErrorCount() {
		return errorCount;
	}

	/**
	 * @param errorCount
	 *           the errorCount to set
	 */
	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}

	/**
	 * @return the group
	 */
	public Group getGroup() {
		return group;
	}

	/**
	 * @param group
	 *           the group to set
	 */
	public void setGroup(Group group) {
		this.group = group;
	}

	/**
	 * @return the loginIP
	 */
	public String getLoginIP() {
		return loginIP;
	}

	/**
	 * @param loginIP
	 *           the loginIP to set
	 */
	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile
	 *           the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the nameNative
	 */
	public String getNameNative() {
		return nameNative;
	}

	/**
	 * @param nameNative
	 *           the nameNative to set
	 */
	public void setNameNative(String nameNative) {
		this.nameNative = nameNative;
	}

	public String getCodeName() {
		return loginId + "/" + nameNative;
	}

	/**
	 * @return the needChangePassword
	 */
	public boolean isNeedChangePassword() {
		return needChangePassword;
	}

	/**
	 * @param needChangePassword
	 *           the needChangePassword to set
	 */
	public void setNeedChangePassword(boolean needChangePassword) {
		this.needChangePassword = needChangePassword;
	}

	/**
	 * @return the password
	 */

	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *           the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *           the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.userdetails.UserDetails#getAuthorities()
	 */

	public Collection<GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> auths = new HashSet<GrantedAuthority>();
		Iterator<UserPermission> it = getPermissions().values().iterator();
		while (it.hasNext()) {
			auths.add(it.next());
		}
		return auths;
	}

	/**
	 * @return the accountNonLocked
	 */

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	/**
	 * @param accountNonLocked
	 *           the accountNonLocked to set
	 */
	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	/**
	 * @return the loginId
	 */
	public String getLoginId() {
		return loginId;
	}

	/**
	 * @param loginId
	 *           the loginId to set
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	/**
	 * @return the groupCode
	 */
	public String getGroupCode() {
		return groupCode;
	}

	/**
	 * @param groupCode
	 *           the groupCode to set
	 */
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	/**
	 * @return the accountExpireDate
	 */
	public Date getAccountExpireDate() {
		return accountExpireDate;
	}

	/**
	 * @param accountExpireDate
	 *           the accountExpireDate to set
	 */
	public void setAccountExpireDate(Date accountExpireDate) {
		this.accountExpireDate = accountExpireDate;
	}

	/**
	 * @return the credentialsExpireDate
	 */
	public Date getCredentialsExpireDate() {
		return credentialsExpireDate;
	}

	/**
	 * @param credentialsExpireDate
	 *           the credentialsExpireDate to set
	 */
	public void setCredentialsExpireDate(Date credentialsExpireDate) {
		this.credentialsExpireDate = credentialsExpireDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#getUsername()
	 */

	public String getUsername() {
		return loginId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
	 */

	public boolean isAccountNonExpired() {
		return accountExpireDate.after(DateUtils.getCurrentTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
	 */

	public boolean isCredentialsNonExpired() {
		return credentialsExpireDate.after(DateUtils.getCurrentTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
	 */

	public boolean isEnabled() {
		return accountNonLocked && isAccountNonExpired();
	}

}