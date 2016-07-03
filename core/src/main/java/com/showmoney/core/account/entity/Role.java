/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 Module Name          : com.showmoney.core.account.entity.Role
 Module Description   :

 Date Created      : Apr 16, 2008
 Original Author   : jeffma
 Team              : 
 ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 MODIFICATION HISTORY
 ------------------------------------------------------------------------------
 Date Modified       Modified by       Comments
 ------------------------------------------------------------------------------
 ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package com.showmoney.core.account.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.showmoney.core.common.entity.BaseEntity;

/**
 * @author jeffma
 */
@Entity
@Cacheable
@Table(name = "CORE_ACC_ROLE")
public class Role extends BaseEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 3129346244295131533L;

	public enum Type {
		/** 系统 */
		SYSTEM("系统"),
		/** 其他 */
		OTHER("其他"),
		/** 群组 */
		GROUP("群组"),
		/** 主管 */
		MANAGER("主管");

		String desc;

		Type(String desc) {
			this.desc = desc;
		}

		public String getCode() {
			return name();
		}

		public String getDesc() {
			return desc;
		}
	};

	@Column(name = "ROLE_KEY", length = 30)
	private String key;
	@Id
	@GeneratedValue(generator = "assigned")
	@GenericGenerator(name = "assigned", strategy = "assigned")
	@Column(name = "ROLE_CODE", length = 50)
	private String code;
	@Column(name = "ROLE_DESC", length = 30)
	private String description;
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "MAIN_PERMISSION_KEY")
	private Permission mainPermission;
	/** type */
	@Enumerated(EnumType.STRING)
	@Column(name = "ROLE_TYPE", length = 20, updatable = false, nullable = false)
	private Type type = Type.OTHER;
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@MapKeyColumn(name = "MAP_KEY", length = 30, nullable = false)
	@JoinTable(name = "CORE_ACC_ROLE_PERMISSION", joinColumns = @JoinColumn(name = "ROLE_KEY"), inverseJoinColumns = @JoinColumn(name = "PERMISSION_KEY"))
	@NotFound(action = NotFoundAction.IGNORE)
	private Map<String, Permission> permissions;

	/** default constructors */
	@SuppressWarnings("unused")
	private Role() {
		super();
	}

	/** default constructors */
	public Role(String key, String description, Role.Type type) {
		this.key = key;
		this.type = type;
		this.description = description;
		this.setCode((type.name() + "_" + key).toUpperCase());
		switch (type) {
		case GROUP:
			this.mainPermission = new Permission(key, description, Permission.Type.GROUP);
			break;
		case MANAGER:
			this.mainPermission = new Permission(key, description, Permission.Type.MANAGER);
			break;
		default:
			this.mainPermission = new Permission(key, description, Permission.Type.ROLE);
		}
		this.getPermissions().put(mainPermission.getName(), mainPermission);
	}

	/**
	 * @return the name
	 */
	public String getCode() {
		if (code == null) {
			code = (type.name() + "_" + key).toUpperCase();
		}
		return code;
	}

	/**
	 * @param name
	 *           the name to set
	 */
	private void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type
	 *           the type to set
	 */
	@SuppressWarnings("unused")
	private void setType(Type type) {
		this.type = type;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *           the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the permissions
	 */
	public Map<String, Permission> getPermissions() {
		if (permissions == null) {
			permissions = new HashMap<String, Permission>();
		}
		return permissions;
	}

	public Map<String, Permission> addPermission(Permission p) {
		Map<String, Permission> permissions = getPermissions();
		permissions.put(p.getKey(), p);
		return permissions;
	}

	/**
	 * @param permissions
	 *           the permissions to set
	 */
	@SuppressWarnings("unused")
	private void setPermissions(Map<String, Permission> permissions) {
		this.permissions = permissions;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the key/description
	 */
	public String getKeyDesc() {
		return key + "/" + description;
	}

	/**
	 * @param key
	 *           the key to set
	 */
	@SuppressWarnings("unused")
	private void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the mainPermission
	 */
	public Permission getMainPermission() {
		return mainPermission;
	}

	/**
	 * @param mainPermission
	 *           the mainPermission to set
	 */
	public void setMainPermission(Permission mainPermission) {
		this.mainPermission = mainPermission;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("key", this.key)
				.append("type", this.type).append("permissions", this.permissions).toString();
	}
}