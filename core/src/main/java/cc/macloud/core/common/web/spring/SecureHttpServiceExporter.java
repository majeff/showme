/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.common.web.spring.SecureHttpServiceExporter
   Module Description   :

   Date Created      : 2008/4/23
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.common.web.spring;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

import cc.macloud.core.cache.exception.CacheException;
import cc.macloud.core.cache.service.CacheService;

/**
 * @author jeffma
 * 
 */
public class SecureHttpServiceExporter extends HttpInvokerServiceExporter {

	public final static String HEADER_ACTOR_KEY = "ACEGI_ACTORID";
	private final Logger logger = LoggerFactory.getLogger(SecureHttpServiceExporter.class);

	/**
	 * allowRegExp, default : null If allowRegExp isn't null and caller ip not match it than system will throw
	 * IOException
	 */
	private String allowRegExp = null;
	private Pattern allowPattern = null;
	private CacheService<Boolean> cache = null;

	/**
	 * @param allowRegExp
	 *           the allowRegExp to set
	 */
	public void setAllowRegExp(String allowRegExp) {
		this.allowRegExp = allowRegExp;
		this.allowPattern = Pattern.compile(allowRegExp, Pattern.CASE_INSENSITIVE);
	}

	/**
	 * @param cache
	 *           the cache to set
	 */
	public void setCache(CacheService<Boolean> cache) {
		this.cache = cache;
	}

	/** default constructors */
	public SecureHttpServiceExporter() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.springframework.remoting.httpinvoker.HttpInvokerServiceExporter#handleRequest(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		Boolean allow = Boolean.TRUE;
		String callerIP = request.getRemoteAddr();
		if (allowRegExp != null) {
			if (cache != null) {
				try {
					allow = cache.get(callerIP);
					logger.debug("Hit cache, callerIP:{}", callerIP);
				} catch (CacheException e) {
					Matcher matcher = allowPattern.matcher(callerIP);
					allow = matcher.find();
					cache.put(callerIP, allow);
					logger.debug("NeedsRefreshException callerIP:{},allow:{}", callerIP, allow);
				}
			} else {
				allow = false;
				Matcher matcher = allowPattern.matcher(callerIP);
				allow = matcher.find();
			}
		}
		logger.debug("request contentPath:{}", request.getContextPath());

		if (allow) {
			super.handleRequest(request, response);
		}
		throw new IOException("not allow IP (" + callerIP + ")");
	}

}
