/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.account.web.UserAction
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
package com.showmoney.core.account.web;

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

import com.showmoney.core.account.entity.Group;
import com.showmoney.core.account.entity.User;
import com.showmoney.core.account.service.GroupService;
import com.showmoney.core.account.service.RoleService;
import com.showmoney.core.account.service.UserService;
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
@RequestMapping("/account/user")
public class UserAction extends AbstractAction {

	@Autowired
	private UserService userService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private RoleService roleService;

	@RequestMapping(value = "/list.do")
	public String list(@ModelAttribute("qryForm") User qry, @ModelAttribute("pager") SimplePager pager, Errors result,
			Model model) {
		model.addAttribute("pager", new SimplePager());
		model.addAttribute("users", ListUtils.EMPTY_LIST);
		return returnList(model, null, null);
	}

	@RequestMapping(value = "/search.do")
	public String search(@ModelAttribute("qryForm") User qry, @ModelAttribute("pager") SimplePager pager,
			@RequestParam(value = "isActive", required = false) Boolean isActive, Errors result, Model model) {
		CommonCriteria cri = new CommonCriteria();
		if (StringUtils.isNotBlank(qry.getLoginId())) {
			cri.addRlike("loginId", qry.getLoginId());
		}
		if (StringUtils.isNotBlank(qry.getNameNative())) {
			cri.addRlike("nameNative", qry.getNameNative());
		}
		if (StringUtils.isNotBlank(qry.getGroupCode())) {
			cri.addRlike("groupCode", qry.getGroupCode());
		}
		if (Boolean.TRUE.equals(isActive)) {
			cri.addGe("accountExpireDate", DateUtils.getCurrentTime());
			cri.addEq("accountNonLocked", true);
		} else if (Boolean.FALSE.equals(isActive)) {
			cri.addLe("accountExpireDate", DateUtils.getCurrentTime());
			cri.addEq("accountNonLocked", false);
		}
		pager.setTotalSize(userService.getListSize(cri).longValue());
		List<User> users = userService.getList(pager.getStartRecord(), pager.getPageRecord(), cri, null);
		return returnList(model, pager, users);
	}

	@RequestMapping(value = "/view/{loginId}.do")
	public String view(@PathVariable("loginId") String loginId, @ModelAttribute("user") User form, Errors result,
			Model model) {
		User user = null;
		if (StringUtils.isNotBlank(loginId)) {
			user = userService.get(loginId);
		}
		if (user == null) {
			result.reject("errors.account.user.empty");
			return returnList(model, null, null);
		} else {
		}
		return returnView(model, user);
	}

	@RequestMapping(value = "/edit/{loginId}.do")
	public String edit(@PathVariable("loginId") String loginId, @ModelAttribute("user") User form, Errors result,
			Model model) {
		User user = userService.get(loginId);
		return returnEdit(model, user);
	}

	@RequestMapping(value = "/resetPwd/{loginId}.do")
	public String resetPassword(@PathVariable("loginId") String loginId, @ModelAttribute("user") User form,
			Errors result, Model model) {
		User user = userService.get(loginId);
		user = userService.resetPassword(user, null, true);
		return returnView(model, user);
	}

	@RequestMapping(value = "/create.do")
	public String create(@ModelAttribute("user") User form, Errors result, Model model) {
		return returnEdit(model, new User());
	}

	@RequestMapping(value = "/save/{loginId}.do")
	public String save(@PathVariable("loginId") String loginId, @ModelAttribute("user") User form, Errors result,
			Model model) {
		User user = userService.get(loginId);
		if (user == null) {
			user = new User(loginId);
		}
		beanValidator.validate(form, result);

		if (result.hasErrors()) {
			return returnEdit(model, form);
		}

		BeanUtils.copyProperties(form, user, new String[] { "roles", "loginIP", "permissions", "errorCount", "group",
				"groupCode", "loginId", "password", "modifyUser", "modifyDate", "createUser", "createDate" });
		if (user.getGroup() != null) {
			if (!user.getGroup().getCode().equals(form.getGroupCode())) {
				Group group = groupService.get(form.getGroupCode());
				user.setGroup(group);
			}
			user = userService.save(user);
		} else {
			user = userService.createUser(user, null, form.getGroupCode(), true);
		}
		return returnView(model, user);
	}

	private String returnList(Model model, SimplePager pager, List<User> users) {
		model.addAttribute("pager", pager);
		model.addAttribute("objs", users);
		return "account.user.list";
	}

	private String returnEdit(Model model, User user) {
		List<Group> groups = groupService.getList(0, -1, null, null);
		model.addAttribute("groups", groups);

		model.addAttribute("user", user);
		return "account.user.edit";
	}

	private String returnView(Model model, User user) {
		model.addAttribute("user", user);
		return "account.user.view";
	}
}
