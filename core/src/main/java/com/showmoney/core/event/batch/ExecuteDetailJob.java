/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.event.batch.ExecuteDetailJob
   Module Description   :

   Date Created      : 2011/2/9
   Original Author   : jeffma
   Team              : yaodian100
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package com.showmoney.core.event.batch;

import com.showmoney.core.common.batch.AbstractRunJob;
import com.showmoney.core.event.entity.BatchDetail;
import com.showmoney.core.event.service.BatchEventService;

/**
 * @author jeffma
 * 
 */
public class ExecuteDetailJob extends AbstractRunJob {

	private BatchDetail jobDetail;
	private BatchEventService service;

	/** default constructor */
	public ExecuteDetailJob(BatchDetail jobDetail, BatchEventService service) {
		super("ExecuteDetailJob_" + jobDetail.getUuid());
		this.jobDetail = jobDetail;
		this.service = service;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yaodian100.core.thread.batch.AbstractRunJob#execute()
	 */
	@Override
	public void execute() {
		try {
			service.executeJob(jobDetail);
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			// 避免內存洩漏問題
			jobDetail = null;
			service = null;
		}
	}

}
