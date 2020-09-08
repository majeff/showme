/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.CoreServiceTest
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
package com.showmoney.core;

import static org.junit.Assert.fail;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import com.showmoney.core.account.entity.Group;
import com.showmoney.core.account.entity.Permission;
import com.showmoney.core.account.entity.Role;
import com.showmoney.core.account.entity.Sidebar;
import com.showmoney.core.account.entity.User;
import com.showmoney.core.account.service.GroupService;
import com.showmoney.core.account.service.PermissionService;
import com.showmoney.core.account.service.RoleService;
import com.showmoney.core.account.service.SidebarService;
import com.showmoney.core.account.service.UserService;
import com.showmoney.core.common.entity.Menu;
import com.showmoney.core.common.service.MenuService;
import com.showmoney.core.common.service.ThreadService;
import com.showmoney.core.common.utils.SpringCommonTest;
import com.showmoney.core.common.utils.StringUtils;
import com.showmoney.core.message.entity.Mail;
import com.showmoney.core.message.service.MailService;

/**
 * @author jeffma
 *
 */
public class CoreServiceTest extends SpringCommonTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		configCtx();
	}

	/**
	 * Test method for
	 * {@link com.showmoney.core.account.service.UserService#createUser(com.showmoney.core.account.entity.User, java.lang.String, java.lang.String, boolean)}
	 * .
	 */
	@Test
	public void testCRUDUser() {
		GroupService groupService = ctx.getBean(GroupService.class);
		Group g = groupService.get("TEST");
		if (g == null) {
			g = new Group("TEST", "測試1");
			groupService.save(g);
		}

		UserService userService = ctx.getBean(UserService.class);
		User user = new User("test01");
		try {
			user = userService.createUser(user, "12345678", "TEST", true);
		} catch (Exception e) {
		}
		try {
			userService.delete(user);
		} catch (Exception e) {
		}
		try {
			groupService.delete(g);
		} catch (Exception e) {
		}
	}

	@Test
	public void testCUser() {
		PermissionService permissionService = ctx.getBean(PermissionService.class);
		Permission p = permissionService.get("USER", Permission.Type.SYSTEM);
		if (p == null) {
			p = new Permission("USER", "可登入会员", Permission.Type.SYSTEM);
			permissionService.save(p);
		}

		GroupService groupService = ctx.getBean(GroupService.class);
		Group g = groupService.get("A-00-000-000");
		if (g == null) {
			g = new Group("A-00-000-000", "系统");
			g.getRole().addPermission(p);
			groupService.save(g);
		}

		UserService userService = ctx.getBean(UserService.class);
		User user = userService.get("jeffma");
		if (user == null) {
			user = new User("jeffma");
			userService.createUser(user, "12345678", "A-00-000-000", false);
		}
	}

	@Test
	public void testCRUDPermission() {
		PermissionService permissionService = ctx.getBean(PermissionService.class);
		Permission p = new Permission("TEST2", "測試2", Permission.Type.OTHER);
		try {
			p = permissionService.save(p);

			p.setDescription("測試測試");
			p = permissionService.save(p);
		} catch (Exception e) {
		}
		try {
			permissionService.delete(p);
		} catch (Exception e) {
		}
	}

	@Test
	public void testCRUDSidebar() {
		SidebarService service = ctx.getBean(SidebarService.class);
		Sidebar obj = service.get("0000");
		// if (obj != null) {
		// obj = new Sidebar("0000", "測試", "/");
		// obj.getPermissions().add("SYSTEM_USR");
		// service.save(obj);
		// }
		obj = service.get("Y000");
		if (obj == null) {
			obj = new Sidebar("Y000", "系統管理", "/");
			obj.getPermissions().add("SYSTEM_USR");
			Sidebar child = null;
			child = new Sidebar("Y100", "公告管理", "/content/content/list/COMP_ANNOUNCE.do");
			obj.addChild(child);
			child = new Sidebar("Y200", "帳號管理", "/account/user/list.do");
			obj.addChild(child);
			service.save(obj);
		}
		obj = service.get("Z000");
		if (obj != null) {
			obj = new Sidebar("Z000", "登出", "/j_spring_security_logout");
			obj.getPermissions().add("SYSTEM_USR");
			service.save(obj);
		}
	}

	@Test
	public void testCRUDRole() {
		RoleService roleService = ctx.getBean(RoleService.class);
		Role r = new Role("TEST1", "測試1", Role.Type.OTHER);
		roleService.save(r);
		logger.debug("role:{}", StringUtils.toJSON(r));

		r.setDescription("測試測試");
		roleService.save(r);
		logger.debug("role:{}", StringUtils.toJSON(r));

		PermissionService permissionService = ctx.getBean(PermissionService.class);
		Permission p = null;
		if (permissionService.get("TEST1") == null) {
			p = new Permission("TEST1", "測試1", Permission.Type.OTHER);
			permissionService.save(p);
		}
		r.getPermissions().put("TEST1", p);
		roleService.save(r);
		logger.debug("role:{}", StringUtils.toJSON(r));

		r.getPermissions().remove("TEST1");
		roleService.save(r);
		logger.debug("role:{}", StringUtils.toJSON(r));

		roleService.delete(r);
	}

	@Test
	public void testCRUDGroup() {
		GroupService groupService = ctx.getBean(GroupService.class);
		Group g = new Group("TEST1", "測試1");
		groupService.save(g);
		logger.debug("group:{}", StringUtils.toJSON(g));

		g.setDescription("測試測試");
		groupService.save(g);
		logger.debug("group:{}", StringUtils.toJSON(g));

		groupService.delete(g);
	}

	@Test
	public void testCRUDMenu() {
		MenuService menuService = ctx.getBean(MenuService.class);
		Menu m = new Menu("TEST", "測試");
		menuService.save(m);
		logger.debug("menu:{}", m);

		m.setDescription("測試測試");
		menuService.save(m);
		logger.debug("menu:{}", m);

		m.addOption("OPT1", "測試OPT1");
		menuService.save(m);
		logger.debug("menu:{}", m);

		m.getOptions().values().iterator().next().setMemo1("OPT1 meno1");
		m.addOption("OPT2", "測試OPT2");
		menuService.save(m);
		logger.debug("menu:{}", m);

		m.getOptions().remove("OPT1");
		menuService.save(m);
		logger.debug("menu:{}", m);

		menuService.delete(m);
	}

	@Test
	public void testCMail() {
		MailService mailService = ctx.getBean(MailService.class);
		Mail m = new Mail("主旨", "這是什麼鬼", "jeffma0521@gmail.com", null);
		m.getAttachments().put("test.txt", "file://c:/test.txt");
		m.getInlines().put("icon12345", "http://tw.yahoo.com/icon.jpg");
		mailService.save(m);
	}

	@Test
	public void testThread() {
		ThreadService threadService = ctx.getBean(ThreadService.class);
		threadService.setTimeout(5);
		for (int i = 0; i < 10; i++) {
			int sleep = RandomUtils.nextInt(12);
			threadService.executeInBackground(new TestSleepJob("job" + i, sleep));
		}
		for (int i = 0; i < 12; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			threadService.showInfo();
		}
	}

	/**
	 * Test method for
	 * {@link com.showmoney.core.account.service.UserService#getByRole(java.lang.String, com.showmoney.core.account.entity.Role.Type, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetByRole() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.showmoney.core.account.service.UserService#getByPermission(java.lang.String, com.showmoney.core.account.entity.Permission.Type, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetByPermission() {
		// fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.showmoney.core.account.service.UserService#getByGroup(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetByGroup() {
		// fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.showmoney.core.account.service.UserService#getRandPassword()}.
	 */
	@Test
	public void testGetRandPassword() {
		// fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.showmoney.core.account.service.UserService#getListByRole(int, int, com.showmoney.core.common.dao.impl.CommonCriteria, java.lang.String[], com.showmoney.core.account.entity.Role.Type, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetListByRole() {
		// fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.showmoney.core.account.service.UserService#getListSizeByRole(com.showmoney.core.common.dao.impl.CommonCriteria, com.showmoney.core.account.entity.Role.Type, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetListSizeByRole() {
		// fail("Not yet implemented");
	}

}
