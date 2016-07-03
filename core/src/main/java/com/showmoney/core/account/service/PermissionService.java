/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.account.service.PermissionService
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
package com.showmoney.core.account.service;

import com.showmoney.core.account.entity.Permission;
import com.showmoney.core.common.exception.CoreException;
import com.showmoney.core.common.service.DomainService;

/**
 * @author jeffma
 */
public interface PermissionService extends DomainService<Permission> {

	public Permission get(String key, Permission.Type type) throws CoreException;
}
