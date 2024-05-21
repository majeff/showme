/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.account.entity.Sidebar
   Module Description   :

   Date Created      : 2011/4/20
   Original Author   : jeffma
   Team              :
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.account.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import cc.macloud.core.common.entity.BaseEntity;
import cc.macloud.core.common.utils.StringUtils;

/**
 * @author jeffma
 *
 */
@Entity(name = "Sidebar")
@Table(name = "CORE_ACC_SIDEBAR")
public class Sidebar extends BaseEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 4196636549168317209L;
	/** code */
	@Id
	@Column(name = "SIDEBAR_CODE", length = 30)
	private String code;
	@Column(name = "SIDEBAR_NAME", length = 30)
	private String name;
	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "CORE_ACC_SIDEBAR_PERMISSION", joinColumns = @JoinColumn(name = "SIDEBAR_CODE", referencedColumnName = "SIDEBAR_CODE"))
	@Column(name = "SIDEBAR_PERMISSION", length = 50)
	private Set<String> permissions;
	@Column(name = "SIDEBAR_URL", length = 1000)
	private String url;
	@Column(name = "ACEGI_PATTERN", length = 200)
	private String pattern;
	@Column(name = "SORT_ORDER")
	private int sortOrder = 0;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "parent")
	@NotFound(action = NotFoundAction.IGNORE)
	private List<Sidebar> childData;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "PARENT_CODE")
	@NotFound(action = NotFoundAction.IGNORE)
	private Sidebar parent;
	@Column(name = "IS_VISIBLE")
	private boolean visible = true;
	/** requestURI, /ecadmin /erp /scm ... etc */
	@Column(name = "SYSTEM", length = 50)
	private String system = "";

	/** default constructor */
	public Sidebar() {
		super();
	}

	/** default constructor */
	public Sidebar(String code, String name, String url) {
		super();
		this.code = code;
		this.name = name;
		this.url = url;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *           the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *           the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the permissions
	 */
	public Set<String> getPermissions() {
		if (permissions == null) {
			permissions = new HashSet<String>();
		}
		return permissions;
	}

	/**
	 * @param permissions
	 *           the permissions to set
	 */
	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	public String getPermissionStr() {
		return StringUtils.join(getPermissions(), ",");
	}

	/**
	 * @return the childData
	 */
	public List<Sidebar> getChildData() {
		if (childData == null) {
			childData = new ArrayList<Sidebar>();
		}
		return childData;
	}

	public List<String> getChildCode() {
		List<String> codeList = new ArrayList<String>();
		for (Sidebar s : getChildData()) {
			codeList.add(s.getCode());
		}
		return codeList;
	}

	/**
	 * @param childData
	 *           the childData to set
	 */
	public void setChildData(List<Sidebar> childData) {
		this.childData = childData;
	}

	public Sidebar addChild(Sidebar child) {
		getChildData().add(child);
		child.setSortOrder(getChildData().size());
		child.setParent(this);
		return this;
	}

	/**
	 * @return the sortOrder
	 */
	public int getSortOrder() {
		return sortOrder;
	}

	/**
	 * @param sortOrder
	 *           the sortOrder to set
	 */
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * @return the parent
	 */
	public Sidebar getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *           the parent to set
	 */
	public void setParent(Sidebar parent) {
		this.parent = parent;
	}

	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern
	 *           the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible
	 *           the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * @return the system
	 */
	public String getSystem() {
		return system;
	}

	/**
	 * @param system
	 *           the system to set
	 */
	public void setSystem(String system) {
		this.system = system;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Sidebar [code=" + code + ", name=" + name + ", permissions=" + permissions + ", url=" + url
				+ ", pattern=" + pattern + ", visible=" + visible + "]";
	}

}
