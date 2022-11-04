/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.event.batch.UpdateEventJob
   Module Description   :

   Date Created      : 2011/3/29
   Original Author   : jeffma
   Team              : yaodian100
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.event.batch;

import cc.macloud.core.common.batch.AbstractRunJob;
import cc.macloud.core.event.service.BatchEventService;

/**
 * @author jeffma
 * 
 */
public class UpdateEventJob extends AbstractRunJob {

	private String eventOid;
	private BatchEventService service;

	/** default constructor */
	public UpdateEventJob(String eventOid, BatchEventService service) {
		super("UpdateEventJob_" + eventOid);
		this.eventOid = eventOid;
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
			service.updateBatchEvent(eventOid, false);
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			// 避免內存洩漏問題
			service = null;
		}
	}

}
