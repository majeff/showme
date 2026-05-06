/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.account.web.acegi.SidebarSecurityFilter
   Module Description   :

   Date Created      : 2011/4/21
   Original Author   : jeffma
   Team              :
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.account.web.acegi;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.GenericFilterBean;

import cc.macloud.core.account.entity.Sidebar;
import cc.macloud.core.account.entity.User;
import cc.macloud.core.account.service.SidebarService;
import cc.macloud.core.account.utils.AdminHelper;
import cc.macloud.core.common.utils.StringUtils;

/**
 * @author jeffma
 *
 */
public class SidebarSecurityFilter extends GenericFilterBean {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired(required = false)
	private SidebarService sidebarService;
	/** sidebarLastModify, 蝝���� sidebar entity 銝� modifyDate ������ */
	private Date sidebarLastModify = new Date(100l);;
	/** permissionMap, cache <url pattern, permissions> */
	private Map<String, String> permissionMap = new HashMap();

	private Map<String, String> rebuildRequestMap(List<Sidebar> sidebars) {
		Map<String, String> newSidebarMap = new HashMap<String, String>();
		for (Sidebar s : sidebars) {
			if (StringUtils.isNotBlank(s.getPattern())) {
				String url = s.getPattern();
				if (newSidebarMap.get(url) != null) {
					StringBuffer permissions = new StringBuffer();
					permissions.append(newSidebarMap.get(url));
					permissions.append(",");
					permissions.append(StringUtils.join(s.getPermissions(), ","));
					newSidebarMap.put(url, permissions.toString());
				} else {
					String permissions = StringUtils.join(s.getPermissions(), ",");
					newSidebarMap.put(url, permissions);
				}
			}
			newSidebarMap.putAll(rebuildRequestMap(s.getChildData()));
		}
		return newSidebarMap;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
	 * javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {
		try {
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) res;
			User user = AdminHelper.getUser();
			if ((user != null) && (sidebarService != null)) {
				String url = request.getRequestURI();
				logger.debug("user:{},url:{}", new Object[] { user.getUsername(), url });
				List sideBars = sidebarService.getTop();
				req.setAttribute("sideBars", sideBars);
				if (sidebarService.getLastModify().after(sidebarLastModify)) {
					sidebarLastModify = sidebarService.getLastModify();
					permissionMap = rebuildRequestMap(sideBars);
				}

				url = StringUtils.removeStart(url, request.getContextPath());
				for (String checkUrl : permissionMap.keySet()) {
					if (url.startsWith(checkUrl) && StringUtils.isNotBlank(permissionMap.get(checkUrl))) {
						boolean hasPermission = AdminHelper.hasPermissions(user, permissionMap.get(checkUrl));

						if (!hasPermission) {
							logger.error("�����, user:" + user.getUsername() + ",url:" + url + ",permission:"
									+ permissionMap.get(checkUrl));
							response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User " + user.getUsername()
									+ " not permission");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		chain.doFilter(req, res);
	}

}
