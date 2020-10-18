/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.account.service.RoleService
   Module Description   :

   Date Created      : Apr 16, 2008
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.account.service;

import cc.macloud.core.account.entity.Role;
import cc.macloud.core.common.exception.CoreException;
import cc.macloud.core.common.service.DomainService;

public interface RoleService extends DomainService<Role> {

	public Role get(String key, Role.Type type) throws CoreException;
}
