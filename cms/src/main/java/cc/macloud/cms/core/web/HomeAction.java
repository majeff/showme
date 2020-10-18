/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.cms.core.web.HomeController
   Module Description   :

   Date Created      : 2012/5/17
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.cms.core.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cc.macloud.cms.content.entity.Content;
import cc.macloud.cms.content.service.ContentService;
import cc.macloud.core.account.utils.AdminHelper;
import cc.macloud.core.common.web.AbstractAction;

/**
 * @author jeffma
 * 
 */
@Controller
public class HomeAction extends AbstractAction {

	@Autowired
	private ContentService contentService;

	@RequestMapping(value = "/login.do")
	public String login() {
		logger.debug("HomeController login {}: Passing through...", AdminHelper.getUser());
		return "login";
	}

	@RequestMapping(value = "/index.do")
	public String index(Model model) {
		logger.debug("HomeController index {}: Passing through...", AdminHelper.getUser());

		List<Content> announces = contentService.getPublish("COMP_ANNOUNCE", 0, 15, null);
		model.addAttribute("contents", announces);
		return "index";
	}

}
