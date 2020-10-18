/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.account.service.PermissionService
   Module Description   :

   Date Created      : 2008/12/24
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.account.service;

import cc.macloud.core.account.entity.Permission;
import cc.macloud.core.common.exception.CoreException;
import cc.macloud.core.common.service.DomainService;

/**
 * @author jeffma
 */
public interface PermissionService extends DomainService<Permission> {

	public Permission get(String key, Permission.Type type) throws CoreException;
}
