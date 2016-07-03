/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.common.service.ThreadService
   Module Description   :

   Date Created      : 2008/6/2
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package com.showmoney.core.common.service;

import com.showmoney.core.common.batch.AbstractRunJob;

/**
 * @author jeffma
 * 
 */
public interface ThreadService {

	/**
	 * Execute runnable in background (asynchronously).
	 * 
	 * @param runnable
	 * @throws java.lang.InterruptedException
	 */
	public void executeInBackground(AbstractRunJob runnable);

	public void scheduleInBackground(AbstractRunJob runnable, long second);

	public void scheduleFixdInBackground(AbstractRunJob runnable, long second, long initDelay);

	public void scheduleRateInBackground(AbstractRunJob runnable, long period, long initDelay);

	public String showInfo();

	public void setTimeout(int timeout);

}
