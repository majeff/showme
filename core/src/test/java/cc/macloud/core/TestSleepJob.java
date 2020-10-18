/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.TestSleepJob
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
package cc.macloud.core;

import cc.macloud.core.common.batch.AbstractRunJob;

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
	 * @see cc.macloud.core.common.batch.AbstractRunJob#execute()
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
