/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.cache.remote.RemoteCacheServiceImpl
   Module Description   :

   Date Created      : 2010/7/30
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.cache.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cc.macloud.core.cache.service.CacheService;
import cc.macloud.core.common.exception.CoreException;
import cc.macloud.core.common.utils.StringUtils;

/**
 * @author jeffma
 * 
 */
public class RemoteCacheServiceImpl implements RemoteCacheService, ApplicationContextAware {

	ApplicationContext ctx = null;
	private static Logger logger = LoggerFactory.getLogger(RemoteCacheServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see cc.macloud.core.cache.remote.RemoteCacheService#clean(java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	public boolean clean(String cache) throws CoreException {
		boolean result = false;
		try {
			if (ctx != null) {
				if (StringUtils.isNotBlank(cache)) {
					CacheService cacheObj = ctx.getBean(cache, CacheService.class);
					if (cacheObj != null) {
						cacheObj.destroy();
						logger.warn("cache({}) clean", cache);
						result = true;
					} else {
						logger.warn("cache({}) not found", cache);
					}
				} else {
					String[] cacheNames = ctx.getBeanNamesForType(CacheService.class);
					for (String s : cacheNames) {
						CacheService cacheObj = ctx.getBean(s, CacheService.class);
						if (cacheObj != null) {
							cacheObj.destroy();
							logger.warn("cache({}) clean", s);
							result = true;
						} else {
							logger.warn("cache({}) not found", s);
						}
					}
				}
			} else {
				logger.warn("spring config fail, ctx is null");
			}
		} catch (Exception e) {
			throw new CoreException("clean cache false", e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.
	 * ApplicationContext)
	 */
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ctx = applicationContext;
	}
}
