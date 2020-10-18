/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.content.web.ContentAction
   Module Description   :

   Date Created      : 2012/5/31
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.cms.content.web;

import java.security.KeyRep.Type;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cc.macloud.cms.content.entity.Content;
import cc.macloud.cms.content.entity.ContentElement;
import cc.macloud.cms.content.entity.ContentTemplate;
import cc.macloud.cms.content.entity.ContentTemplateElement;
import cc.macloud.cms.content.service.ContentService;
import cc.macloud.cms.content.service.ContentTemplateService;
import cc.macloud.cms.workflow.service.WorkflowService;
import cc.macloud.core.common.dao.impl.CommonCriteria;
import cc.macloud.core.common.entity.SimplePager;
import cc.macloud.core.common.utils.DateUtils;
import cc.macloud.core.common.utils.StringUtils;
import cc.macloud.core.common.web.AbstractAction;

/**
 * @author jeffma
 * 
 */
@Controller
@RequestMapping(value = "/content/content")
public class ContentAction extends AbstractAction {

	@Autowired
	private ContentService contentService;
	@Autowired
	private ContentTemplateService contentTemplateService;
	@Autowired
	private WorkflowService workflowService;

	@RequestMapping(value = "/list/{templateCode}.do")
	public String list(@PathVariable("templateCode") String templateCode, Model model) {
		model.addAttribute("pager", new SimplePager());
		model.addAttribute("objs", ListUtils.EMPTY_LIST);

		model.addAttribute("templateCode", templateCode);
		return returnList(model, templateCode, null, null);
	}

	@RequestMapping(value = "/search/{templateCode}.do")
	public String search(@PathVariable("templateCode") String templateCode, @ModelAttribute("pager") SimplePager pager,
			@ModelAttribute("qryForm") Content qry, @RequestParam(value = "isActive", required = false) String isActive,
			Errors result, Model model) {
		CommonCriteria cri = new CommonCriteria();
		cri.addEq("templateCode", templateCode);
		if (qry.getStartDate() != null) {
			cri.addGe("startDate", DateUtils.getFirstTimestamp(Calendar.DAY_OF_MONTH, qry.getStartDate()));
		}
		if (qry.getEndDate() != null) {
			cri.addLe("endDate", DateUtils.getLastTimestamp(Calendar.DAY_OF_MONTH, qry.getEndDate()));
		}
		if ("true".equals(isActive)) {
			cri.addEq("status", Type.PUBLIC.name());
		} else if ("false".equals(isActive)) {
			cri.addNe("status", Type.PUBLIC.name());
		}
		if (StringUtils.isNotBlank(qry.getTitle())) {
			cri.addRlike("title", "%" + qry.getTitle());
		}
		cri.addEq("childUuid", null);
		pager.setTotalSize(contentService.getListSize(cri).longValue());
		List<Content> objs = contentService.getList(pager.getStartRecord(), pager.getPageRecord(), cri, null);

		logger.debug("errors:{}", result);
		return returnList(model, templateCode, pager, objs);
	}

	@RequestMapping(value = "/view/{templateCode}/{contentUuid}.do")
	public String view(@PathVariable("templateCode") String templateCode,
			@PathVariable("contentUuid") String contentUuid, Model model) {
		ContentTemplate template = contentTemplateService.get(templateCode);
		Content content = contentService.get(contentUuid);
		return returnView(model, template, content);
	}

	@RequestMapping(value = "/edit/{templateCode}/{contentUuid}.do")
	public String edit(@PathVariable("templateCode") String templateCode,
			@PathVariable("contentUuid") String contentUuid, @ModelAttribute("content") Content form, Errors error,
			Model model) {
		ContentTemplate template = contentTemplateService.get(templateCode);
		Content content = contentService.get(contentUuid);
		return returnEdit(model, template, content);
	}

	@RequestMapping(value = "/create/{templateCode}.do")
	public String create(@PathVariable("templateCode") String templateCode, @ModelAttribute("content") Content form,
			Errors error, Model model) {
		ContentTemplate template = contentTemplateService.get(templateCode);
		return returnEdit(model, template, new Content(template));
	}

	@RequestMapping(value = "/save/{templateCode}/{contentUuid}.do")
	public String save(@PathVariable("templateCode") String templateCode,
			@PathVariable("contentUuid") String contentUuid, @ModelAttribute("content") Content form, Errors error,
			Model model) {
		ContentTemplate template = contentTemplateService.get(templateCode);

		Content content = null;
		if (StringUtils.isNotBlank(form.getUuid())) {
			content = contentService.get(form.getUuid());
		} else {
			content = new Content(template);
		}
		try {
			logger.debug("element form:{}, db:{}", form, content);
			content.setTitle(form.getTitle());
			content.setSortOrder(form.getSortOrder());
			if (form.getStartDate() != null) {
				content.setStartDate(form.getStartDate());
			}
			if (form.getEndDate() != null) {
				content.setEndDate(form.getEndDate());
			}
			for (ContentTemplateElement cte : template.getElements()) {
				if (content.getElements().get(cte.getCode()) == null) {
					ContentElement element = new ContentElement(content, cte);
					content.addElement(element);
				}
				content.getElements().get(cte.getCode()).setValue(form.getElements().get(cte.getCode()).getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		content = contentService.save(content);
		return returnView(model, template, content);
	}

	@RequestMapping(value = "/delete/{templateCode}/{contentUuid}.do")
	public String delete(@PathVariable("templateCode") String templateCode,
			@PathVariable("contentUuid") String contentUuid, Model model) {
		Content content = null;
		if (StringUtils.isNotBlank(contentUuid)) {
			content = contentService.get(contentUuid);
		}
		contentService.delete(content);
		return returnList(model, templateCode, null, null);
	}

	@RequestMapping(value = "/publish/{templateCode}/{contentUuid}.do")
	public String publish(@PathVariable("templateCode") String templateCode,
			@PathVariable("contentUuid") String contentUuid, @RequestParam(value = "actionCode") String actionCode,
			@RequestParam(value = "publishCommand", required = false) String command, Model model) {
		ContentTemplate template = contentTemplateService.get(templateCode);
		Content content = contentService.get(contentUuid);
		content = contentService.publish(content, actionCode, command);
		return returnView(model, template, content);
	}

	private String returnList(Model model, String templateCode, SimplePager pager, List<Content> objs) {
		model.addAttribute("pager", pager);
		model.addAttribute("objs", objs);

		model.addAttribute("templateCode", templateCode);
		ContentTemplate template = contentTemplateService.get(templateCode);
		model.addAttribute("nodes", workflowService.getNodes(template.getWorkflowCode()));

		return "content.content.list";
	}

	private String returnEdit(Model model, ContentTemplate template, Content content) {
		model.addAttribute("templateCode", template.getCode());
		model.addAttribute("nodes", workflowService.getNodes(template.getWorkflowCode()));
		model.addAttribute("template", template);
		model.addAttribute("content", content);
		return "content.content.edit";
	}

	private String returnView(Model model, ContentTemplate template, Content content) {
		model.addAttribute("nodes", workflowService.getNodes(template.getWorkflowCode()));
		model.addAttribute("template", template);
		model.addAttribute("templateCode", template.getCode());
		model.addAttribute("content", content);
		return "content.content.view";
	}
}
