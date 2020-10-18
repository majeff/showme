/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.account.web.acegi.UserDetailsAuthenticationProvider
   Module Description   :

   Date Created      : 2008/4/24
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.account.web.acegi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import cc.macloud.core.account.entity.User;
import cc.macloud.core.account.service.SidebarService;
import cc.macloud.core.account.service.UserService;
import cc.macloud.core.common.exception.CoreException;

/**
 * @author jeffma
 */
public class UserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	/** logger */
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private UserService userService;
	@Autowired
	private SidebarService sidebarService;
	private String staticHtmlFolder = "/static/private";

	/**
	 * @param staticHtmlFolder
	 *           the staticHtmlFolder to set
	 */
	public void setStaticHtmlFolder(String staticHtmlFolder) {
		this.staticHtmlFolder = staticHtmlFolder;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * @param sidebarService
	 *           the sidebarService to set
	 */
	public void setSidebarService(SidebarService sidebarService) {
		this.sidebarService = sidebarService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.springframework.security.providers.dao. AbstractUserDetailsAuthenticationProvider
	 * #additionalAuthenticationChecks(org .springframework.security.userdetails.UserDetails,
	 * org.springframework.security .providers.UsernamePasswordAuthenticationToken)
	 */
	@Override
	protected void additionalAuthenticationChecks(UserDetails userdetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.springframework.security.providers.dao.
	 * AbstractUserDetailsAuthenticationProvider#retrieveUser(java.lang.String, org
	 * .springframework.security.providers.UsernamePasswordAuthenticationToken)
	 */
	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		String remoteIp = null;
		try {
			User user = userService.get(username);

			Object detail = authentication.getDetails();
			if (detail instanceof WebAuthenticationDetails) {
				WebAuthenticationDetails webDetail = (WebAuthenticationDetails) detail;
				remoteIp = webDetail.getRemoteAddress();
			}

			if (user == null) {
				logger.error("user not found, username:" + username + ",ip:" + remoteIp);
				throw new UsernameNotFoundException("username:" + username + ",ip:" + remoteIp);
			} else {
				if (!userService.validatePassword(user, authentication.getCredentials().toString())) {
					if (user.getErrorCount() > 5) {
						logger.error("password incorrect, username:" + username + ",ip:" + remoteIp + ",count:"
								+ user.getErrorCount());
					} else {
						logger.warn("password incorrect, username:" + username + ",ip:" + remoteIp);
					}
					throw new BadCredentialsException(messages.getMessage("error.admin.badCredentials", "帐号/密码错误"));
				}
			}

			try {
				if (sidebarService != null) {
					sidebarService.getSidebar(user);
				}
			} catch (Exception e) {
				logger.error("build sidebar fail", e);
			}

			return user;
		} catch (CoreException e) {
			if ("errors.account.lock".equals(e.getMessage())) {
				logger.error("user locked, username:" + username + ",ip:" + remoteIp);
				throw new LockedException(messages.getMessage(e.getMessage(), "帐号已被锁定, 请联络系统管理员"));
			} else {
				e.printStackTrace();
				logger.error("exception, username:" + username + ",ip:" + remoteIp, e);
				throw new BadCredentialsException(messages.getMessage(e.getMessage(), "帐号/密码错误"));
			}
		}
	}
}
