/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.cms.workflow.service.impl.WorkflowServiceImpl
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
package cc.macloud.cms.workflow.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import cc.macloud.cms.workflow.entity.FlowNode;
import cc.macloud.cms.workflow.entity.Workflow;
import cc.macloud.cms.workflow.service.WorkflowService;
import cc.macloud.core.common.dao.ObjectDao;
import cc.macloud.core.common.exception.CoreException;
import cc.macloud.core.common.service.impl.DomainServiceImpl;
import cc.macloud.core.common.utils.dao.CommonCriteria;

/**
 * @author jeffma
 *
 */
public class WorkflowServiceImpl extends DomainServiceImpl<Workflow> implements WorkflowService {

	private ObjectDao<FlowNode> flowNodeDao;

	/**
	 * @param flowNodeDao
	 *           the flowNodeDao to set
	 */
	public void setFlowNodeDao(ObjectDao<FlowNode> flowNodeDao) {
		this.flowNodeDao = flowNodeDao;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.cms.workflow.service.WorkflowService#getNode(java.lang.String)
	 */
	public FlowNode getNode(String nodeUuid) throws CoreException {
		return flowNodeDao.get(nodeUuid);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.cms.workflow.service.WorkflowService#saveNode(cc.macloud.cms.workflow.entity.FlowNode)
	 */
	@Transactional(readOnly = false)
	public FlowNode saveNode(FlowNode flowNode) throws CoreException {
		if (flowNode.getWorkflow() == null) {
			throw new CoreException("errors.workflow.flownode.workflowNull", flowNode.getUuid());
		}
		return flowNodeDao.save(flowNode);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.common.service.impl.DomainServiceImpl#delete(java.lang.Object)
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(Workflow entity) throws CoreException {
		CommonCriteria cri = new CommonCriteria();
		cri.addEq("workflow", entity);
		List<FlowNode> nodes = flowNodeDao.getList(cri, null);
		flowNodeDao.deleteBatch(nodes);
		super.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.cms.workflow.service.WorkflowService#getNodes(java.lang.String)
	 */
	@Override
	public Map<String, FlowNode> getNodes(String workflowCode) throws CoreException {
		CommonCriteria cri = new CommonCriteria();
		cri.addEq("workflow.code", workflowCode);

		return flowNodeDao.getMap("status", cri, null);
	}
}
