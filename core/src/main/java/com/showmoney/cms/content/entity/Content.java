/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.cms.content.entity.Content
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

import com.showmoney.core.common.entity.BaseEntity;
import com.showmoney.core.common.utils.DateUtils;

/**
 * 動態表單
 * 
 * @author jeffma
 */
@Entity
@Table(name = "CMS_CONTENT_MAIN")
public class Content extends BaseEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 8219723622967903497L;

	/**
	 * 動態表單預設狀態
	 * 
	 * @author jeffma
	 */
	public enum Status {
		PUBLISH("9", "發佈"), EDITING("0", "編輯");

		String code;
		String desc;

		Status(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public String getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	};

	/**
	 * 
	 */
	public Content() {
		super();
	}

	/** uuid, PK, 自動產生 */
	@Id
	@GeneratedValue(generator = "gen_uuid")
	@GenericGenerator(name = "gen_uuid", strategy = "uuid")
	@Column(name = "OBJ_UUID", length = 32)
	private String uuid;

	/** category, 此資料應當歸屬哪個目錄節點 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORY_UUID", referencedColumnName = "OBJ_UUID")
	@NotFound(action = NotFoundAction.IGNORE)
	private Category category;
	/** templateCode, 表單樣板, 不應該修改, 不可為空 */
	@Column(name = "TEMP_CODE", length = 50, updatable = false, nullable = false)
	private String templateCode;
	/** title, 表單 title */
	@Column(name = "CONTENT_TITLE", length = 200)
	private String title;

	/** status, 表單狀態, 初始為 '0' */
	@Column(name = "CONTENT_STATUS", length = 10)
	private String status = "0";
	/** childUuid, 若 contentTemplate.contentVC=true, 表示要作版本控管, 此欄表示有一未發佈表單 */
	@Column(name = "CHILD_UUID", length = 32)
	private String childUuid;
	/** parentUuid, 若 contentTemplate.contentVC=true, 表示要作版本控管, 此欄表示此資料為一已發佈表單之修改資料 */
	@Column(name = "PARENT_UUID", length = 32)
	private String parentUuid;

	/** elements, 此表單對應的動態資料 */
	@OneToMany(mappedBy = "content", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@MapKeyColumn(name = "ELEMENT_CODE", length = 20)
	@OrderBy("sortOrder asc")
	private Map<String, ContentElement> elements;
	/** delete, 若是已發佈的表單, 都應作 mark delete, 由此控制 */
	@Column(name = "IS_DELETE")
	private boolean delete = false;
	/** startDate, 表單有效期限起始 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE")
	private Date startDate;
	/** endDate, 表單有效期限結束 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE")
	private Date endDate;
	/** sortOrder, 表單排序權重 */
	@Column(name = "SORT_ORDER")
	private int sortOrder = 500;

	/**
	 * 
	 */
	public Content(ContentTemplate template) {
		super();
		for (ContentTemplateElement e : template.getElements()) {
			this.addElement(new ContentElement(this, e));
		}
		templateCode = template.getCode();
		startDate = DateUtils.getCurrentTime();
		endDate = DateUtils.convertStringToDate("yyyy-MM-dd HH:mm:ss", "9999-12-31 23:59:59");
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
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category
	 *           the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * @return the elements
	 */
	public Map<String, ContentElement> getElements() {
		if (elements == null) {
			elements = new HashMap<String, ContentElement>();
		}
		return elements;
	}

	/**
	 * @param elements
	 *           the elements to set
	 */
	public void setElements(Map<String, ContentElement> elements) {
		this.elements = elements;
	}

	public Content addElement(ContentElement element) {
		element.setContent(this);
		getElements().put(element.getCode(), element);
		return this;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *           the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the templateCode
	 */
	public String getTemplateCode() {
		return templateCode;
	}

	/**
	 * @param templateCode
	 *           the templateCode to set
	 */
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	/**
	 * @return the delete
	 */
	public boolean isDelete() {
		return delete;
	}

	/**
	 * @param delete
	 *           the delete to set
	 */
	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	/**
	 * @return the childUuid
	 */
	public String getChildUuid() {
		return childUuid;
	}

	/**
	 * @param childUuid
	 *           the childUuid to set
	 */
	public void setChildUuid(String childUuid) {
		this.childUuid = childUuid;
	}

	/**
	 * @return the parentUuid
	 */
	public String getParentUuid() {
		return parentUuid;
	}

	/**
	 * @param parentUuid
	 *           the parentUuid to set
	 */
	public void setParentUuid(String parentUuid) {
		this.parentUuid = parentUuid;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *           the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *           the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *           the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Content [uuid=" + uuid + ", templateCode=" + templateCode + ", status=" + status + ", title=" + title
				+ ", elements=" + elements + ", parentUuid=" + parentUuid + ", childUuid=" + childUuid + "]";
	}

}
