/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.cms.workflow.entity.Workflow
   Module Description   :

   Date Created      : 2012/5/24
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.cms.workflow.entity;

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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import cc.macloud.core.common.entity.BaseEntity;

/**
 * 簡易工作流程設定
 * 
 * @author jeffma
 */
@Entity
@Cacheable
@Table(name = "CMS_WORKFLOW_MAIN")
public class Workflow extends BaseEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -6006367164262142823L;
	/** code, PK, 由 ContentTemplate.workflowCode 指定對應工作流程 */
	@Id
	@GeneratedValue(generator = "assigned")
	@GenericGenerator(name = "assigned", strategy = "assigned")
	@Column(name = "WORKFLOW_CODE", length = 50)
	private String code;
	/** description, 簡單描述此工作流程 */
	@Column(name = "WORKFLOW_DESC", length = 100)
	private String description;

	/** startNode, 這個工作流程的第一個節點, flowNode.status 建議為 '0' 初始編輯 */
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "NODE_UUID")
	@NotFound(action = NotFoundAction.EXCEPTION)
	private FlowNode startNode;

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private Workflow() {
		super();
	}

	/**
	 * @param code
	 * @param description
	 * @param startNode
	 */
	public Workflow(String code, String description) {
		super();
		this.code = code;
		this.description = description;
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
	 * @return the startNode
	 */
	public FlowNode getStartNode() {
		return startNode;
	}

	/**
	 * @param startNode
	 *           the startNode to set
	 */
	public void setStartNode(FlowNode startNode) {
		this.startNode = startNode;
	}

}
