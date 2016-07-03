/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.cms.content.service.ContentService
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
package com.showmoney.cms.content.service;

import java.util.List;

import com.showmoney.cms.content.entity.Content;
import com.showmoney.core.common.exception.CoreException;
import com.showmoney.core.common.service.DomainService;

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
