/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.cache.exception.CacheException
   Module Description   :

   Date Created      : 2008/3/14
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package com.showmoney.core.cache.exception;

/**
 * @author jeffma
 * 
 */
public class CacheException extends Exception {

	/** serialVersionUID */
	private static final long serialVersionUID = -1922818869703224814L;

	/** default constructors */
	public CacheException() {
		super();
	}

	/** default constructors */
	public CacheException(String key) {
		super(key);
	}

	/** default constructors */
	public CacheException(String key, Throwable e) {
		super(key, e);
	}
}
