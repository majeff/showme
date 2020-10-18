/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.account.service.impl.SidebarServiceImpl
   Module Description   :

   Date Created      : 2011/4/20
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.account.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import cc.macloud.core.account.entity.Sidebar;
import cc.macloud.core.account.entity.User;
import cc.macloud.core.account.service.SidebarService;
import cc.macloud.core.account.utils.AdminHelper;
import cc.macloud.core.common.dao.impl.CommonCriteria;
import cc.macloud.core.common.exception.CoreException;
import cc.macloud.core.common.service.TemplateService;
import cc.macloud.core.common.service.impl.DomainServiceImpl;
import cc.macloud.core.common.utils.DateUtils;
import cc.macloud.core.common.utils.FileUtils;

/**
 * @author jeffma
 * 
 */
public class SidebarServiceImpl extends DomainServiceImpl<Sidebar> implements SidebarService, InitializingBean {

	private static SidebarServiceImpl instance = new SidebarServiceImpl();

	@Autowired
	private TemplateService templateService;
	private String checkDate = "99999";
	private List<Sidebar> activeSidebars = new ArrayList<Sidebar>();
	private String staticHtmlFolder = "/static/private";

	/**
	 * @param staticHtmlFolder
	 *           the staticHtmlFolder to set
	 */
	public void setStaticHtmlFolder(String staticHtmlFolder) {
		this.staticHtmlFolder = staticHtmlFolder;
	}

	private Date lastModify = new Date(100l);

	/** default constructor */
	private SidebarServiceImpl() {
		super();
		setDefaultSort(new String[] { "sortOrder asc", "code asc" });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		checkLastModify();
	}

	/**
	 * @return the instance
	 */
	public static SidebarServiceImpl getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cc.macloud.core.common.service.impl.DomainServiceImpl#save(java.lang.Object)
	 */

	@Override
	@Transactional(readOnly = false)
	public Sidebar save(Sidebar entity) throws CoreException {
		Sidebar s = super.save(entity);
		lastModify = s.getModifyDate();
		return s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cc.macloud.core.sidebar.service.SidebarService#buildSidebar(cc.macloud.core.account.entity.User)
	 */

	public String buildSidebar(User user) {
		String userSidebar = null;
		List<Sidebar> sidebars = getTop();
		if (user == null) {
			user = AdminHelper.getUser();
		}
		if (user != null) {
			String userSidebarPath = staticHtmlFolder + File.separator + "user" + File.separator + user.getUsername()
					+ ".html";
			File userSidebarFile = new File(userSidebarPath);
			Map<String, Object> objs = new HashMap<String, Object>();
			objs.put("user", user);
			objs.put("sidebars", sidebars);
			userSidebar = templateService.format("account/User.sidebar", objs);
			try {
				FileUtils.writeStringToFile(userSidebarFile, userSidebar);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			logger.warn("user is null");
		}
		return userSidebar;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cc.macloud.core.account.service.SidebarService#getTop()
	 */

	public synchronized List<Sidebar> getTop() {
		Date now = DateUtils.getCurrentTime();
		String nowString = DateUtils.convertDateToString("ddHHmm", now);
		if (!nowString.startsWith(checkDate)) {
			CommonCriteria cri = new CommonCriteria();
			cri.addEq("parent", null);
			activeSidebars = getList(0, -1, cri, getDefaultSort());
			logger.info("renew activeSidebars:{}", activeSidebars);

			// 取前五碼, 每 10min 更新一次
			// checkDate = DateUtil.convertDateToString("ddHHmm", now).substring(0, 5);

			// 测试中, 不切, 每分钟更新一次
			checkDate = DateUtils.convertDateToString("ddHHmm", now);
		}
		return activeSidebars;
	}

	/**
	 * @return the lastModify
	 */

	public Date getLastModify() {
		return lastModify;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cc.macloud.core.account.service.SidebarService#checkLastModify()
	 */

	public void checkLastModify() {
		List<Sidebar> sidebars = getTop();
		for (Sidebar s : sidebars) {
			loopLastModify(s);
		}
	}

	private void loopLastModify(Sidebar entity) {
		if (entity.getModifyDate().after(lastModify)) {
			lastModify = entity.getModifyDate();
		}
		for (Sidebar s : entity.getChildData()) {
			loopLastModify(s);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cc.macloud.core.account.service.SidebarService#getSidebar(cc.macloud.core.account.entity.User)
	 */
	@Override
	public String getSidebar(User user) {
		String result = null;
		String userSidebarPath = staticHtmlFolder + File.separator + "user" + File.separator + user.getUsername()
				+ ".html";
		File userSidebarFile = new File(userSidebarPath);
		boolean build = false;
		if (userSidebarFile.exists()) {
			Date fileLastModify = new Date(userSidebarFile.lastModified());
			if (fileLastModify.before(getLastModify())) {
				build = true;
			}
			if (fileLastModify.before(user.getModifyDate())) {
				build = true;
			}
		} else {
			build = true;
		}
		if (build) {
			result = buildSidebar(user);
		} else {
			try {
				FileUtils.readFileToString(userSidebarFile);
			} catch (IOException e) {
				logger.error("errors.common.sidebar.io", e);
			}
		}
		return result;
	}

}
