/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.content.web.ContentTemplateAction
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
package com.showmoney.cms.content.web;

import java.security.KeyRep.Type;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.showmoney.cms.content.entity.Content;
import com.showmoney.cms.content.entity.ContentTemplate;
import com.showmoney.cms.content.entity.ContentTemplateElement;
import com.showmoney.cms.content.service.ContentTemplateService;
import com.showmoney.cms.workflow.service.WorkflowService;
import com.showmoney.core.common.dao.impl.CommonCriteria;
import com.showmoney.core.common.entity.SimplePager;
import com.showmoney.core.common.utils.DateUtils;
import com.showmoney.core.common.utils.StringUtils;
import com.showmoney.core.common.web.AbstractAction;

/**
 * @author jeffma
 * 
 */
@Controller
@RequestMapping(value = "/content/template")
public class ContentTemplateAction extends AbstractAction {

	@Autowired
	private ContentTemplateService contentTemplateService;
	@Autowired
	private WorkflowService workflowService;

	@RequestMapping(value = "/list.do")
	public String list(Model model) {
		model.addAttribute("pager", new SimplePager());
		model.addAttribute("objs", ListUtils.EMPTY_LIST);

		return returnList(model, null, null);
	}

	@RequestMapping(value = "/search.do")
	public String search(@ModelAttribute("pager") SimplePager pager, @ModelAttribute("qryForm") Content qry,
			@RequestParam(value = "isActive", required = false) String isActive, Errors result, Model model) {
		CommonCriteria cri = new CommonCriteria();
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
		pager.setTotalSize(contentTemplateService.getListSize(cri).longValue());
		List<ContentTemplate> objs = contentTemplateService.getList(pager.getStartRecord(), pager.getPageRecord(), cri,
				null);

		logger.debug("errors:{}", result);
		return returnList(model, pager, objs);
	}

	@RequestMapping(value = "/view/{templateCode}.do")
	public String view(@PathVariable("templateCode") String templateCode, Model model) {
		ContentTemplate template = contentTemplateService.get(templateCode);
		return returnView(model, template);
	}

	@RequestMapping(value = "/edit/{templateCode}.do")
	public String edit(@PathVariable("templateCode") String templateCode, @ModelAttribute("content") Content form,
			Errors error, Model model) {
		ContentTemplate template = contentTemplateService.get(templateCode);
		return returnEdit(model, template);
	}

	@RequestMapping(value = "/create/{templateCode}.do")
	public String create(@PathVariable("templateCode") String templateCode, @ModelAttribute("content") Content form,
			Errors error, Model model) {
		ContentTemplate template = contentTemplateService.get(templateCode);
		return returnEdit(model, template);
	}

	@RequestMapping(value = "/save/{templateCode}.do")
	public String save(@PathVariable("templateCode") String templateCode,
			@ModelAttribute("template") ContentTemplate form, Errors error, Model model) {
		ContentTemplate template = contentTemplateService.get(templateCode);

		if (template == null) {
			template = new ContentTemplate(form.getCode(), form.getDescription(), form.isContentVC(),
					form.getWorkflowCode());
		}
		try {
			logger.debug("element form:{}, db:{}", form, template);
			BeanUtils.copyProperties(form, template, new String[] { "modifyDate", "modfiyUser", "createDate",
					"createUser", "code", "elements" });
			// 先檢查有沒有被移掉的 element
			Iterator<ContentTemplateElement> iterator = template.getElements().iterator();
			for (ContentTemplateElement fe : template.getElements()) {
				boolean removeObj = true;
				for (ContentTemplateElement te : form.getElements()) {
					if (te.getCode().equals(fe.getCode())) {
						removeObj = false;
						break;
					}
				}
				if (removeObj) {
					iterator.remove();
				}
			}
			// 複製舊 element, 並將新的 element 建立
			for (int i = 0; i < form.getElements().size(); i++) {
				ContentTemplateElement fe = form.getElements().get(i);
				boolean newObj = true;
				for (ContentTemplateElement te : template.getElements()) {
					if (te.getCode().equals(fe.getCode())) {
						BeanUtils.copyProperties(fe, te, new String[] { "uuid", "template", "code", "type", "sortOrder" });
						te.setSortOrder((short) i);
						newObj = false;
						break;
					}
				}
				if (newObj) {
					ContentTemplateElement te = new ContentTemplateElement(fe.getCode(), fe.getName(), fe.getDescription(),
							fe.getType());
					BeanUtils.copyProperties(fe, te, new String[] { "uuid", "template", "code", "type", "sortOrder" });
					template.addElement(te);
					te.setSortOrder((short) i);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		template = contentTemplateService.save(template);
		return returnView(model, template);
	}

	@RequestMapping(value = "/delete/{templateCode}.do")
	public String delete(@PathVariable("templateCode") String templateCode, Model model) {
		ContentTemplate template = null;
		if (StringUtils.isNotBlank(templateCode)) {
			template = contentTemplateService.get(templateCode);
		}
		contentTemplateService.delete(template);
		return returnList(model, null, null);
	}

	private String returnList(Model model, SimplePager pager, List<ContentTemplate> objs) {
		model.addAttribute("pager", pager);
		model.addAttribute("objs", objs);

		return "content.content.list";
	}

	private String returnEdit(Model model, ContentTemplate template) {
		model.addAttribute("nodes", workflowService.getNodes(template.getWorkflowCode()));
		model.addAttribute("template", template);
		return "content.content.edit";
	}

	private String returnView(Model model, ContentTemplate template) {
		model.addAttribute("nodes", workflowService.getNodes(template.getWorkflowCode()));
		model.addAttribute("template", template);
		return "content.content.view";
	}
}
