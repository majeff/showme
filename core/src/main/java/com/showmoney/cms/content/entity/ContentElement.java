/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.cms.content.entity.ContentElement
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
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import com.showmoney.cms.content.entity.ContentTemplateElement.Type;

/**
 * 動態表單欄位值
 * 
 * @author jeffma
 * 
 */
@Entity
@Table(name = "CMS_CONTENT_ELEMENT", uniqueConstraints = @UniqueConstraint(columnNames = { "CONTENT_UUID", "OBJ_UUID" }))
public class ContentElement implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -2813052463086954357L;

	/** uuid, PK, 自動產生 */
	@Id
	@GeneratedValue(generator = "gen_uuid")
	@GenericGenerator(name = "gen_uuid", strategy = "uuid")
	@Column(name = "OBJ_UUID", length = 32)
	private String uuid;
	/** content, 對應之表單, LAZY, 不應讀取, 純為關連使用 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTENT_UUID")
	private Content content;
	/** code, 欄位代碼, 每一 content.elements 不應該重覆, 對應 templateContentElement.code */
	@Column(name = "ELEMENT_CODE", length = 20)
	private String code;
	/** type, 欄位格式, 對應 templateContentElement.type */
	@Column(name = "ELEMENT_TYPE", updatable = false, length = 15, nullable = false)
	@Enumerated(EnumType.STRING)
	private Type type = Type.TEXT;

	/** text, 大部分的值放置於此 */
	@Column(name = "VALUE_TEXT", length = 1000)
	private String text;
	/** number, DOUBLE 放置於此 */
	@Column(name = "VALUE_NUM")
	private BigDecimal number;
	/** clob, TEXTAREA, RICH, 放置於此 */
	@Lob
	@Column(name = "VALUE_LOB")
	private String clob;

	/** sortOrder, 欄位排序, 由 templateContentElement.sortOrder 複製 */
	@Column(name = "SORT_ORDER")
	private short sortOrder;

	/** default constructor */
	public ContentElement() {
		super();
	}

	/**
	 * @param content
	 */
	public ContentElement(Content content, ContentTemplateElement element) {
		super();
		this.content = content;
		this.code = element.getCode();
		if (element != null) {
			this.type = element.getType();
		}
		this.sortOrder = element.getSortOrder();
		if (StringUtils.isNotBlank(element.getDefaultValue())) {
			this.setValue(element.getDefaultValue());
		}
	}

	public String getValue() {
		switch (type) {
		case TEXT:
		case DATE:
		case TIME:
		case TIMESTAMP:
		case CHECKBOX:
		case RADIO:
		case MENU:
		case MULTIMENU:
		case FILE:
		case IMAGE:
			return text;
		case TEXTAREA:
		case RICH:
			return clob;
		case NUMBER:
			return number != null ? number.toString() : null;
		default:
			return null;
		}
	}

	public String[] getValues() {

		return StringUtils.split(text, ",");
	}

	public void setValue(Object obj) {
		switch (type) {
		case TEXT:
		case DATE:
		case TIME:
		case TIMESTAMP:
		case CHECKBOX:
		case RADIO:
		case MENU:
		case MULTIMENU:
		case FILE:
		case IMAGE:
			text = obj != null ? obj.toString() : null;
			break;
		case TEXTAREA:
		case RICH:
			clob = obj != null ? obj.toString() : null;
			break;
		case NUMBER:
			number = obj != null ? new BigDecimal(obj.toString()) : null;
			break;
		default:
		}
	}

	public void setValues(String[] obj) {
		if (obj != null) {
			text = StringUtils.join(obj, ",");
		}
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
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *           the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the number
	 */
	public BigDecimal getNumber() {
		return number;
	}

	/**
	 * @param number
	 *           the number to set
	 */
	public void setNumber(BigDecimal number) {
		this.number = number;
	}

	/**
	 * @return the clob
	 */
	public String getClob() {
		return clob;
	}

	/**
	 * @param clob
	 *           the clob to set
	 */
	public void setClob(String clob) {
		this.clob = clob;
	}

	/**
	 * @return the content
	 */
	public Content getContent() {
		return content;
	}

	/**
	 * @param content
	 *           the content to set
	 */
	public void setContent(Content content) {
		this.content = content;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ContentElement [code=" + code + ", type=" + type + ", sortOrder=" + sortOrder + ", value=" + getValue()
				+ "]";
	}

}
