/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.cms.content.entity.ContentTemplateElement
   Module Description   :

   Date Created      : 2012/5/16
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package com.showmoney.cms.content.entity;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import net.sf.json.JSONObject;

import org.hibernate.annotations.GenericGenerator;

import com.showmoney.core.common.utils.StringUtils;

/**
 * 動態表單欄位定義
 * 
 * @author jeffma
 * 
 */
@Entity
@Table(name = "CMS_TEMP_ELEMENT", uniqueConstraints = @UniqueConstraint(columnNames = { "TEMP_CODE", "ELEMENT_CODE" }))
public class ContentTemplateElement implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 3119750455012092070L;

	public enum Type {
		TEXT, TEXTAREA, RICH, NUMBER, DATE, TIME, TIMESTAMP, CHECKBOX, RADIO, MENU, MULTIMENU, FILE, IMAGE;
	}

	public enum Style {
		LENGTH
	}

	/** uuid, PK, 自動產生 */
	@Id
	@GeneratedValue(generator = "gen_uuid")
	@GenericGenerator(name = "gen_uuid", strategy = "uuid")
	@Column(name = "OBJ_UUID", length = 32)
	private String uuid;
	/** template, 對應之樣板, LAZY, 不應讀取, 純為關連使用 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEMP_CODE")
	private ContentTemplate template;
	/** code, 欄位對應代碼, 每一 template.elements 不應該重覆 */
	@Column(name = "ELEMENT_CODE", length = 30, nullable = false)
	private String code;

	/** name, 欄位對應說明, 一般用於 table 提示欄位名稱 */
	@Column(name = "ELEMENT_NAME", length = 30, nullable = false)
	private String name;
	/** description, 欄位詳細說明, 一般用於內部提示用途 */
	@Column(name = "ELEMENT_DESC", length = 50)
	private String description;
	/** type, 欄位格式 */
	@Column(name = "ELEMENT_TYPE", updatable = false, length = 15, nullable = false)
	@Enumerated(EnumType.STRING)
	private Type type = Type.TEXT;

	/** regexpValid, 執行正則式判斷條件 */
	@Column(name = "VALID", length = 200)
	private String regexpValid;
	/** style, 用於 TEXTAREA, IMAGE, FILE 等額外樣式設定 */
	@Column(name = "STYLE", length = 500)
	private String style;
	/** defaultValue, 預設值 */
	@Column(name = "DEFAULT_VALUE", length = 100)
	private String defaultValue;

	/** scale, DOUBLE 等小數位數設定 */
	@Column(name = "SCALE")
	private short scale = 0;
	/** notNull, 是否允許為空 */
	@Column(name = "IS_NOTNULL")
	private boolean notNull = false;
	/** sortOrder, 欄位排序使用 */
	@Column(name = "SORT_ORDER")
	private short sortOrder = 0;
	/** jsonMenu, 多選項之 label/value, 可使用 getLabels(), getValues() 或 getLabelValueMap() 來呈現 */
	@Column(name = "VALUE_JSON", length = 1000)
	private String jsonMenu;

	/**
	 * 
	 */
	public ContentTemplateElement() {
		super();
	}

	/**
	 * @param code
	 * @param name
	 * @param description
	 * @param type
	 */
	public ContentTemplateElement(String code, String name, String description, Type type) {
		super();
		this.code = code;
		this.name = name;
		this.description = description;
		this.type = type;
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
	 * @return the template
	 */
	public ContentTemplate getTemplate() {
		return template;
	}

	/**
	 * @param template
	 *           the template to set
	 */
	public void setTemplate(ContentTemplate template) {
		this.template = template;
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
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type
	 *           the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * @return the regexpValid
	 */
	public String getRegexpValid() {
		return regexpValid;
	}

	/**
	 * @param regexpValid
	 *           the regexpValid to set
	 */
	public void setRegexpValid(String regexpValid) {
		this.regexpValid = regexpValid;
	}

	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * @param style
	 *           the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * @return the scale
	 */
	public short getScale() {
		return scale;
	}

	/**
	 * @param scale
	 *           the scale to set
	 */
	public void setScale(short scale) {
		this.scale = scale;
	}

	/**
	 * @return the notNull
	 */
	public boolean isNotNull() {
		return notNull;
	}

	/**
	 * @param notNull
	 *           the notNull to set
	 */
	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue
	 *           the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return the sortOrder
	 */
	public short getSortOrder() {
		return sortOrder;
	}

	/**
	 * @param sortOrder
	 *           the sortOrder to set
	 */
	public void setSortOrder(short sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * @return the jsonMenu
	 */
	public String getJsonMenu() {
		return jsonMenu;
	}

	/**
	 * @param jsonMenu
	 *           the jsonMenu to set
	 */
	public void setJsonMenu(String jsonMenu) {
		this.jsonMenu = jsonMenu;
	}

	public String[] getLabels() {
		String[] result = new String[] {};
		if (StringUtils.isNotBlank(jsonMenu)) {
			JSONObject json = JSONObject.fromObject(jsonMenu);
			result = (String[]) json.getJSONArray("labels").toArray(result);
		}
		return result;
	}

	public String[] getValues() {
		String[] result = new String[] {};
		if (StringUtils.isNotBlank(jsonMenu)) {
			JSONObject json = JSONObject.fromObject(jsonMenu);
			result = (String[]) json.getJSONArray("values").toArray(result);
		}
		return result;
	}

	public Map<String, String> getLabelValueMap() {
		Map<String, String> result = new TreeMap<String, String>();
		String[] label = getLabels(), value = getValues();
		for (int i = 0; i < label.length; i++) {
			result.put(value[i], label[i]);
		}
		return result;
	}
}
