/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.account.entity.Group
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
package cc.macloud.core.account.entity;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import cc.macloud.core.common.entity.BaseEntity;
import cc.macloud.core.common.utils.StringUtils;

/**
 * 部門或使用者歸屬群組
 * 
 */
@Entity
@Cacheable
@Table(name = "CORE_ACC_GROUP")
public class Group extends BaseEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 300397718210564709L;

	public enum Type {
		A("一般"), C("客服"), S("供应商"), L("物流商"), W("仓库");
		String desc;

		Type(String desc) {
			this.desc = desc;
		}
	}

	@Id
	@GeneratedValue(generator = "assigned")
	@GenericGenerator(name = "assigned", strategy = "assigned")
	@Column(name = "GROUP_CODE", length = 30)
	private String code;
	@Column(name = "GROUP_DESC", length = 30)
	private String description;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "GROUP_MANAGER")
	@NotFound(action = NotFoundAction.IGNORE)
	private Role manager;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "GROUP_ROLE")
	@NotFound(action = NotFoundAction.IGNORE)
	private Role role;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_GROUP")
	@NotFound(action = NotFoundAction.IGNORE)
	private Group parentGroup;
	@Column(name = "PARENT_GROUP", length = 30, insertable = false, updatable = false)
	private String parentGroupName;

	/**
	 * 群組型態
	 */
	@Column(name = "GROUP_TYPE", length = 1)
	private String type = Type.A.name();

	/** default constructors */
	public Group() {
		super();
	}

	/** default constructors */
	public Group(String code, String description) {
		super();
		this.code = code;
		this.description = description;
		this.manager = new Role(code, description, Role.Type.MANAGER);
		this.role = new Role(code, description, Role.Type.GROUP);
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *           the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	@Transient
	public Type getTypeEnum() {
		return Type.valueOf(type);
	}

	@Transient
	public void setTypeEnum(Type type) {
		if (type != null) {
			this.type = type.name();
		}
	}

	/**
	 * @return the parentGroupName
	 */
	public String getParentGroupName() {
		return parentGroupName;
	}

	/**
	 * @param parentGroupName
	 *           the parentGroupName to set
	 */
	public void setParentGroupName(String parentGroupName) {
		this.parentGroupName = parentGroupName;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *           the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	public String getCodeDesc() {
		return code + " / " + description;
	}

	/**
	 * @param description
	 *           the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the mainRole
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * @param mainRole
	 *           the mainRole to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @return the manager
	 */
	public Role getManager() {
		return manager;
	}

	/**
	 * @param manager
	 *           the manager to set
	 */
	public void setManager(Role manager) {
		this.manager = manager;
	}

	/**
	 * @return the parentGroup
	 */
	public Group getParentGroup() {
		return parentGroup;
	}

	/**
	 * @param parentGroup
	 *           the parentGroup to set
	 */
	public void setParentGroup(Group parentGroup) {
		this.parentGroup = parentGroup;
	}

	@Transient
	public String getTrimCode() {
		String trimCode = new String(code);
		if (StringUtils.isNotBlank(trimCode)) {
			do {
				if (trimCode.endsWith("-0") || trimCode.endsWith("_0")) {
					trimCode = trimCode.substring(0, trimCode.length() - 2);
				}
				if (trimCode.endsWith("-00") || trimCode.endsWith("_00")) {
					trimCode = trimCode.substring(0, trimCode.length() - 3);
				}
				if (trimCode.endsWith("-000") || trimCode.endsWith("_000")) {
					trimCode = trimCode.substring(0, trimCode.length() - 4);
				}
			} while (trimCode.endsWith("-0") || trimCode.endsWith("-00") || trimCode.endsWith("-000")
					|| trimCode.endsWith("_0") || trimCode.endsWith("_00") || trimCode.endsWith("_000"));
		}
		return trimCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Group [code=");
		builder.append(code);
		builder.append(", description=");
		builder.append(description);
		builder.append("]");
		return builder.toString();
	}
}