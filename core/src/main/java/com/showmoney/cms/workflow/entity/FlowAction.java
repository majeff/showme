/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.cms.workflow.entity.FlowAction
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

/**
 * 描述工作流程的節點對應的動作(button)
 * 
 * @author jeffma
 */
@Entity
@Table(name = "CMS_FLOW_ACTION", uniqueConstraints = @UniqueConstraint(columnNames = { "PARENT_NODE_UUID", "OBJ_UUID" }))
public class FlowAction implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -6656481411354677140L;
	/** uuid, PK, 自動產生 */
	@Id
	@GeneratedValue(generator = "gen_uuid")
	@GenericGenerator(name = "gen_uuid", strategy = "uuid")
	@Column(name = "OBJ_UUID", length = 32)
	private String uuid;
	/** parentNode, 歸屬之工作流程, LAZY, 不應讀取, 純為關連使用 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_NODE_UUID")
	private FlowNode parentNode;

	/** actionNode, 下一工作節點 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ACTION_NODE_UUID", nullable = true)
	private FlowNode actionNode;
	/** sortOrder, 排序, button 產生順序由此決定 */
	@Column(name = "SORT_ORDER")
	private short sortOrder = 0;
	/** code, 由此辨別執行哪一個 action */
	@Column(name = "ACT_CODE", length = 30)
	private String code;
	/** description, button 的 value, 提示操作者, 此 action 的用途 */
	@Column(name = "ACT_DESC", length = 100)
	private String description;
	/** needPermision, 按下此 button 需要的權限, 多個應該用 ',' 區隔 */
	@Column(name = "NEED_PERMISSION", length = 500)
	private String needPermision;

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private FlowAction() {
		super();
	}

	/**
	 * @param actionNode
	 * @param code
	 * @param description
	 */
	public FlowAction(FlowNode actionNode, String code, String description) {
		super();
		this.actionNode = actionNode;
		this.code = code;
		this.description = description;
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
	 * @return the needPermision
	 */
	public String getNeedPermision() {
		return needPermision;
	}

	/**
	 * @param needPermision
	 *           the needPermision to set
	 */
	public void setNeedPermision(String needPermision) {
		this.needPermision = needPermision;
	}

	/**
	 * @return the actionNode
	 */
	public FlowNode getActionNode() {
		return actionNode;
	}

	/**
	 * @param actionNode
	 *           the actionNode to set
	 */
	public void setActionNode(FlowNode actionNode) {
		this.actionNode = actionNode;
	}

	/**
	 * @return the parentNode
	 */
	public FlowNode getParentNode() {
		return parentNode;
	}

	/**
	 * @param parentNode
	 *           the parentNode to set
	 */
	public void setParentNode(FlowNode parentNode) {
		this.parentNode = parentNode;
	}

}
