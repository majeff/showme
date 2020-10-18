/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.cms.workflow.service.WorkflowService
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
package cc.macloud.cms.workflow.service;

import java.util.Map;

import cc.macloud.cms.workflow.entity.FlowNode;
import cc.macloud.cms.workflow.entity.Workflow;
import cc.macloud.core.common.exception.CoreException;
import cc.macloud.core.common.service.DomainService;

/**
 * @author jeffma
 * 
 */
public interface WorkflowService extends DomainService<Workflow> {

	public FlowNode getNode(String nodeUuid) throws CoreException;

	public Map<String, FlowNode> getNodes(String workflowCode) throws CoreException;

	public FlowNode saveNode(FlowNode flowNode) throws CoreException;

}
