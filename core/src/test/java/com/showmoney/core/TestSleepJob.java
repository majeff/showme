/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.TestSleepJob
   Module Description   :

   Date Created      : 2012/5/28
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package com.showmoney.core;

import com.showmoney.core.common.batch.AbstractRunJob;

/**
 * @author jeffma
 * 
 */
public class TestSleepJob extends AbstractRunJob {

	private int sleep;

	/**
	 * @param code
	 */
	public TestSleepJob(String code, int sleep) {
		super(code);
		this.sleep = sleep;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.showmoney.core.common.batch.AbstractRunJob#execute()
	 */
	@Override
	public void execute() {
		logger.debug("start, job:{}, sleep:{}", getCode(), sleep);
		try {
			Thread.sleep(sleep * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.debug("end, job:{}", getCode());
	}

}
