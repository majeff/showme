/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.cms.content.entity.Category
   Module Description   :

   Date Created      : 2012/5/17
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package com.showmoney.cms.content.entity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.showmoney.core.common.entity.BaseEntity;

/**
 * 網站 sitemap 定義
 * 
 * @author jeffma
 */
@Entity
@Cacheable
@Table(name = "CMS_CATEGORY_MAIN")
public class Category extends BaseEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -1495964645397670531L;

	/** uuid, PK, 自動產生 */
	@Id
	@GeneratedValue(generator = "gen_uuid")
	@GenericGenerator(name = "gen_uuid", strategy = "uuid")
	@Column(name = "OBJ_UUID", length = 32)
	public String uuid;

	/** name, 目錄簡短名稱 */
	@Column(name = "CATEGORY_NAME", length = 10)
	public String name;
	/** description, 目錄說明 */
	@Column(name = "CATEGORY_DESC", length = 30)
	public String description;
	/** site, 網站HOSTNAME */
	@Column(name = "SITE", length = 50)
	public String site = "localhost";

	/** template, 對應主樣板名稱 */
	@Column(name = "TEMPLATE_CODE", length = 50)
	public String template;
	/** parent, 上一層目錄 */
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "PARENT_UUID", referencedColumnName = "OBJ_UUID")
	@NotFound(action = NotFoundAction.IGNORE)
	public Category parent;

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
	 * @return the site
	 */
	public String getSite() {
		return site;
	}

	/**
	 * @param site
	 *           the site to set
	 */
	public void setSite(String site) {
		this.site = site;
	}

	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * @param template
	 *           the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * @return the parent
	 */
	public Category getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *           the parent to set
	 */
	public void setParent(Category parent) {
		this.parent = parent;
	}

}
