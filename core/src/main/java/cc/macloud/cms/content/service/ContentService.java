/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.cms.content.service.ContentService
   Module Description   :

   Date Created      : 2012/5/23
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.cms.content.service;

import java.util.List;

import cc.macloud.cms.content.entity.Content;
import cc.macloud.core.common.exception.CoreException;
import cc.macloud.core.common.service.DomainService;

/**
 * @author jeffma
 * 
 */
public interface ContentService extends DomainService<Content> {

	public Content publish(Content content, String actionCode, String command) throws CoreException;

	public Content createContent(String templateCode, String categoryUuid) throws CoreException;

	public List<Content> getPublish(String templateCode, int firstResult, int maxResults, String[] sortOrder);

	public Number getPublishSize(String templateCode);
}
