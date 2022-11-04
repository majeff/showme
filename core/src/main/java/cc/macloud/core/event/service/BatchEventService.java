/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.event.service.BatchEventService
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
package cc.macloud.core.event.service;

import java.util.Collection;
import java.util.List;

import cc.macloud.core.common.service.DomainService;
import cc.macloud.core.event.entity.BatchDetail;
import cc.macloud.core.event.entity.BatchEvent;

/**
 * @author jeffma
 * 
 */
public interface BatchEventService extends DomainService<BatchEvent> {

	/**
	 * remoteBatchJob 呼叫此 method, 将 detail 加入 queue 中.
	 * 
	 * @param batchId
	 * @return
	 */
	public int addBatch(String batchId);

	/**
	 * multi-thread 呼叫此 method 执行 job
	 * 
	 * @param jobDetail
	 */
	public void executeJob(BatchDetail jobDetail);

	/**
	 * 排程检核 detail 是否执行完毕(fail=false), <br/>
	 * 系统启动时, 将已经进 Queue 的批次检核设定系统错误(fail=true)
	 * 
	 * @param event
	 * @param fail
	 */
	public void updateBatchEvent(String eventOid, boolean fail);

	/**
	 * 由 batchId 取得所有的 job details
	 * 
	 * @param batchId
	 * @return
	 */
	public List<BatchDetail> getDetails(String batchId);

	/**
	 * 批次存 details
	 * 
	 * @param details
	 */
	public void saveOrUpdateDetails(Collection<BatchDetail> details);

	/**
	 * 批次刪除傳入 details
	 * 
	 * @param details
	 *           預定刪除物件
	 */
	public void deleteDetails(Collection<BatchDetail> details);
}
