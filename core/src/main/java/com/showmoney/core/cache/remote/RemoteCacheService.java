/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.cache.remote.RemoteCacheService
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
package com.showmoney.core.cache.remote;

import javax.jws.WebService;

import com.showmoney.core.common.exception.CoreException;

/**
 * @author jeffma
 * 
 */
@WebService(serviceName = "remoteCacheService")
public interface RemoteCacheService {

	public boolean clean(String cache) throws CoreException;
}
