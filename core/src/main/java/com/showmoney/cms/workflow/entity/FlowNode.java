/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.cms.workflow.entity.FlowNode
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
package com.showmoney.cms.workflow.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * 工作流程節點
 * 
 * @author jeffma
 */
@Entity
@Cacheable
@Table(name = "CMS_FLOW_NODE")
public class FlowNode implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -5964347101460587922L;
	/** uuid, PK, 自動產生 */
	@Id
	@GeneratedValue(generator = "gen_uuid")
	@GenericGenerator(name = "gen_uuid", strategy = "uuid")
	@Column(name = "OBJ_UUID", length = 32)
	private String uuid;
	/** workflow, 對應之工作流程, LAZY, 不應讀取, 純為關連使用 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "WORKFLOW_CODE", referencedColumnName = "WORKFLOW_CODE")
	@NotFound(action = NotFoundAction.IGNORE)
	private Workflow workflow;

	/** description, 工作節點描述, 可以顯示為表單狀態說明 */
	@Column(name = "NODE_DESC", length = 100)
	private String description;
	/** status, 表單對應狀態, 初始為 '0', 終結為 '9' */
	@Column(name = "ENTITY_STATUS", length = 10)
	private String status;
	/** actions, 此節點允許的動作, 會對應產生 button, 若為空, 表示此節點為終端 (一般 status為 '9') */
	@OneToMany(mappedBy = "parentNode", cascade = { CascadeType.ALL, CascadeType.REMOVE }, fetch = FetchType.EAGER, orphanRemoval = true)
	@OrderBy("sortOrder asc, code asc")
	private List<FlowAction> actions;

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private FlowNode() {
		super();
	}

	/**
	 * @param workflow
	 * @param description
	 * @param status
	 */
	public FlowNode(Workflow workflow, String description, String status) {
		super();
		this.workflow = workflow;
		this.description = description;
		this.status = status;
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
	 * @return the workflow
	 */
	public Workflow getWorkflow() {
		return workflow;
	}

	/**
	 * @param workflow
	 *           the workflow to set
	 */
	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
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
	 * @return the actions
	 */
	public List<FlowAction> getActions() {
		if (actions == null) {
			actions = new ArrayList<FlowAction>();
		}
		return actions;
	}

	public List<FlowAction> addAction(FlowAction action) {
		getActions().add(action);
		action.setParentNode(this);
		return actions;
	}

	/**
	 * @param actions
	 *           the actions to set
	 */
	public void setActions(List<FlowAction> actions) {
		this.actions = actions;
	}

}
