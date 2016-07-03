/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.account.service.RoleService
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
package com.showmoney.core.account.service;

import com.showmoney.core.account.entity.Role;
import com.showmoney.core.common.exception.CoreException;
import com.showmoney.core.common.service.DomainService;

public interface RoleService extends DomainService<Role> {

	public Role get(String key, Role.Type type) throws CoreException;
}
