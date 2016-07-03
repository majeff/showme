/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.form.entity.Template
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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.showmoney.core.common.entity.BaseEntity;

/**
 * 動態表單樣板
 * 
 * @author jeffma
 * 
 */
@Entity
@Cacheable
@Table(name = "CMS_TEMP_MAIN")
public class ContentTemplate extends BaseEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -4751605476862475172L;

	/** code, PK, 樣板代碼 */
	@Id
	@GeneratedValue(generator = "assigned")
	@GenericGenerator(name = "assigned", strategy = "assigned")
	@Column(name = "TEMP_CODE", length = 50)
	private String code;

	/** description, 樣板簡短描述 */
	@Column(name = "TEMP_DESC", length = 100)
	private String description;
	/** elements, 動態表單應有欄位定義列表 */
	@OneToMany(mappedBy = "template", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("sortOrder asc")
	private List<ContentTemplateElement> elements;

	/** content 是否需要作版本控制, 若為 true, 則[已發佈]的紀錄將保留, 修改歷程將視系統記錄有無決定 */
	@Column(name = "IS_CONTENT_VC")
	private boolean contentVC = false;
	/** workflowCode, 對應流程名稱 */
	@Column(name = "WORLFLOW_CODE", length = 50)
	public String workflowCode;

	/** default constructor */
	@SuppressWarnings("unused")
	private ContentTemplate() {
		super();
	}

	/**
	 * @param code
	 * @param description
	 * @param contentVC
	 * @param workflowCode
	 */
	public ContentTemplate(String code, String description, boolean contentVC, String workflowCode) {
		super();
		this.code = code;
		this.description = description;
		this.contentVC = contentVC;
		this.workflowCode = workflowCode;
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

	/**
	 * @param description
	 *           the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the elements
	 */
	public List<ContentTemplateElement> getElements() {
		if (elements == null) {
			elements = new ArrayList<ContentTemplateElement>();
		}
		return elements;
	}

	/**
	 * @param elements
	 *           the elements to set
	 */
	public void setElements(List<ContentTemplateElement> elements) {
		this.elements = elements;
	}

	public ContentTemplate addElement(ContentTemplateElement element) {
		short sort = 0;
		for (ContentTemplateElement e : getElements()) {
			if (sort < e.getSortOrder()) {
				sort = e.getSortOrder();
			}
		}
		sort++;
		element.setSortOrder(sort);
		element.setTemplate(this);
		getElements().add(element);
		return this;
	}

	/**
	 * @return the contentVC
	 */
	public boolean isContentVC() {
		return contentVC;
	}

	/**
	 * @param contentVC
	 *           the contentVC to set
	 */
	public void setContentVC(boolean contentVC) {
		this.contentVC = contentVC;
	}

	/**
	 * @return the workflowCode
	 */
	public String getWorkflowCode() {
		return workflowCode;
	}

	/**
	 * @param workflowCode
	 *           the workflowCode to set
	 */
	public void setWorkflowCode(String workflowCode) {
		this.workflowCode = workflowCode;
	}

}
