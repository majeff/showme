/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.cms.content.service.impl.ContentServiceImpl
   Module Description   :

   Date Created      : 2012/5/23
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package com.showmoney.cms.content.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.showmoney.cms.content.entity.Content;
import com.showmoney.cms.content.entity.ContentElement;
import com.showmoney.cms.content.entity.ContentTemplate;
import com.showmoney.cms.content.entity.ContentTemplateElement;
import com.showmoney.cms.content.service.ContentService;
import com.showmoney.cms.content.service.ContentTemplateService;
import com.showmoney.cms.workflow.entity.FlowAction;
import com.showmoney.cms.workflow.entity.FlowNode;
import com.showmoney.cms.workflow.service.WorkflowService;
import com.showmoney.core.common.dao.impl.CommonCriteria;
import com.showmoney.core.common.exception.CoreException;
import com.showmoney.core.common.service.impl.DomainServiceImpl;
import com.showmoney.core.common.utils.DateUtils;

/**
 * @author jeffma
 * 
 */
public class ContentServiceImpl extends DomainServiceImpl<Content> implements ContentService {

	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private ContentTemplateService contentTemplateService;

	/**
	 * 
	 */
	public ContentServiceImpl() {
		super();
		setDefaultSort(new String[] { "sortOrder asc", "startDate desc", "uuid" });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.showmoney.core.common.service.impl.DomainServiceImpl#save(java.lang.Object)
	 */
	@Override
	@Transactional(readOnly = false)
	public Content save(Content entity) throws CoreException {
		Content result = null;
		ContentTemplate template = contentTemplateService.get(entity.getTemplateCode());

		if (template.isContentVC() && Content.Status.PUBLISH.getCode().equals(entity.getStatus())) {
			result = new Content(template);
			BeanUtils.copyProperties(entity, result, new String[] { "uuid", "createDate", "createUser", "status",
					"parentUuid", "childUuid" });
			for (ContentTemplateElement te : template.getElements()) {
				ContentElement clone = new ContentElement(result, te);
				BeanUtils.copyProperties(entity.getElements().get(te.getCode()), clone, new String[] { "content", "uuid" });
				result.addElement(clone);
			}
			result.setParentUuid(entity.getUuid());
			super.save(result);

			entity = get(entity.getUuid());
			entity.setChildUuid(result.getUuid());
			super.save(entity);
		} else {
			result = super.save(entity);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.showmoney.cms.content.service.ContentService#publish(com.showmoney.cms.content.entity.Content,
	 * java.lang.String, java.lang.String)
	 */
	@Transactional(readOnly = false)
	public Content publish(final Content content, String actionCode, String command) throws CoreException {
		logger.debug("publish content:{}, action:{}, command:{}", new Object[] { content, actionCode, command });
		Content result = null;
		ContentTemplate contentTemplate = contentTemplateService.get(content.getTemplateCode());
		FlowNode node = null;

		node = workflowService.getNodes(contentTemplate.getWorkflowCode()).get(content.getStatus());
		for (FlowAction action : node.getActions()) {
			if (action.getCode().equals(actionCode)) {
				content.setStatus(action.getActionNode().getStatus());
				break;
			}
		}

		if (Content.Status.PUBLISH.getCode().equals(content.getStatus())
				&& StringUtils.isNotBlank(content.getParentUuid())) {
			// 若 content.status 為 publish 且 parentUuid 不為 null, 表示此 content 為 clone且編輯完畢, 應該要複製回 parent
			Content parent = get(content.getParentUuid());
			BeanUtils.copyProperties(content, parent, new String[] { "uuid", "createDate", "createUser", "modifyDate",
					"modifyUser", "status", "parentUuid", "childUuid", "elements" });
			for (ContentTemplateElement te : contentTemplate.getElements()) {
				if ((content.getElements().get(te.getCode()) != null) && (parent.getElements().get(te.getCode()) != null)) {
					BeanUtils.copyProperties(content.getElements().get(te.getCode()),
							parent.getElements().get(te.getCode()), new String[] { "content", "uuid" });
				} else if ((content.getElements().get(te.getCode()) != null)
						&& (parent.getElements().get(te.getCode()) == null)) {
					ContentElement ce = new ContentElement(parent, te);
					BeanUtils.copyProperties(content.getElements().get(te.getCode()),
							parent.getElements().get(te.getCode()), new String[] { "content", "uuid" });
					parent.addElement(ce);
				} else if ((content.getElements().get(te.getCode()) == null)
						&& (parent.getElements().get(te.getCode()) == null)) {
					ContentElement ce = new ContentElement(parent, te);
					parent.addElement(ce);
				}
			}
			parent.setChildUuid(null);
			result = super.save(parent);

			content.setDelete(true);
			super.save(content);
		} else if (Content.Status.PUBLISH.getCode().equals(content.getStatus())
				&& StringUtils.isBlank(content.getParentUuid())) {
			// 若 content.status 為 publish 且 parentUuid 為 null, 表示此 content 為第一次發佈, clone 一個 child 備份
			Content child = new Content(contentTemplate);
			BeanUtils.copyProperties(content, child, new String[] { "uuid", "createDate", "createUser", "modifyDate",
					"modifyUser", "status", "parentUuid", "childUuid", "elements" });
			for (ContentTemplateElement te : contentTemplate.getElements()) {
				if ((content.getElements().get(te.getCode()) != null) && (child.getElements().get(te.getCode()) != null)) {
					BeanUtils.copyProperties(content.getElements().get(te.getCode()), child.getElements().get(te.getCode()),
							new String[] { "content", "uuid" });
				}
			}
			child.setParentUuid(content.getUuid());
			child.setDelete(true);
			super.save(child);

			result = super.save(content);
		} else {
			result = super.save(content);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.showmoney.core.common.service.impl.DomainServiceImpl#delete(java.lang.Object)
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(Content entity) throws CoreException {
		// child 存在, 一併刪除
		if (StringUtils.isNotBlank(entity.getChildUuid())) {
			Content child = get(entity.getChildUuid());
			super.delete(child);
		}
		// parent 存在, 將 parent.childUuid 清空
		if (StringUtils.isNotBlank(entity.getParentUuid())) {
			Content parent = get(entity.getParentUuid());
			parent.setChildUuid(null);
			super.save(parent);
		}
		// 已發佈, 則 mark delete. 否則實體刪除
		if (Content.Status.PUBLISH.getCode().equals(entity.getStatus())) {
			entity.setDelete(true);
			super.save(entity);
		} else {
			super.delete(entity);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.showmoney.core.common.service.impl.DomainServiceImpl#get(java.io.Serializable)
	 */
	@Override
	public Content get(Serializable oid) throws CoreException {
		Content result = super.get(oid);
		if (result.isDelete()) {
			result = null;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.showmoney.core.common.service.impl.DomainServiceImpl#getList(int, int,
	 * com.showmoney.core.common.dao.impl.CommonCriteria, java.lang.String[])
	 */
	@Override
	public List<Content> getList(int firstResult, int maxResults, CommonCriteria criteria, String[] sortOrder)
			throws CoreException {
		if (criteria == null) {
			criteria = new CommonCriteria();
		}
		criteria.addEq("delete", false);
		return super.getList(firstResult, maxResults, criteria, sortOrder);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.showmoney.core.common.service.impl.DomainServiceImpl#getListSize(com.showmoney.core.common.dao.impl.CommonCriteria
	 * )
	 */
	@Override
	public Number getListSize(CommonCriteria criteria) throws CoreException {
		if (criteria == null) {
			criteria = new CommonCriteria();
		}
		criteria.addEq("delete", false);
		return super.getListSize(criteria);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.showmoney.core.common.service.impl.DomainServiceImpl#getSingle(com.showmoney.core.common.dao.impl.CommonCriteria
	 * , java.lang.String[])
	 */
	@Override
	public Content getSingle(CommonCriteria criteria, String[] sortOrder) throws CoreException {
		if (criteria == null) {
			criteria = new CommonCriteria();
		}
		criteria.addEq("delete", false);
		return super.getSingle(criteria, sortOrder);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.showmoney.cms.content.service.ContentService#createContent(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = false)
	public Content createContent(String templateCode, String categoryUuid) throws CoreException {
		ContentTemplate contentTemplate = contentTemplateService.get(templateCode);
		Content result = new Content(contentTemplate);
		for (ContentTemplateElement cte : contentTemplate.getElements()) {
			ContentElement element = new ContentElement(result, cte);
			result.getElements().put(cte.getCode(), element);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.showmoney.cms.content.service.ContentService#getPublih(java.lang.String, int, int, java.lang.String[])
	 */
	public List<Content> getPublish(String templateCode, int firstResult, int maxResults, String[] sortOrder) {
		CommonCriteria cri = new CommonCriteria();
		cri.addEq("templateCode", templateCode);
		cri.addEq("status", Content.Status.PUBLISH.getCode());
		cri.addLe("startDate", DateUtils.getCurrentTime());
		cri.addGe("endDate", DateUtils.getCurrentTime());
		List<Content> result = getList(firstResult, maxResults, cri, sortOrder);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.showmoney.cms.content.service.ContentService#getPublihSize(java.lang.String)
	 */
	@Override
	public Number getPublishSize(String templateCode) {
		CommonCriteria cri = new CommonCriteria();
		cri.addEq("templateCode", templateCode);
		cri.addEq("status", Content.Status.PUBLISH.getCode());
		cri.addLe("startDate", DateUtils.getCurrentTime());
		cri.addGe("endDate", DateUtils.getCurrentTime());
		return getListSize(cri);
	}

}
