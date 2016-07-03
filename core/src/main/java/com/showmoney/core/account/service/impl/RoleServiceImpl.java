/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.account.service.impl.RoleServiceImpl
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
package com.showmoney.core.account.service.impl;

import org.springframework.transaction.annotation.Transactional;

import com.showmoney.core.account.entity.Role;
import com.showmoney.core.account.entity.Role.Type;
import com.showmoney.core.account.service.RoleService;
import com.showmoney.core.common.exception.CoreException;
import com.showmoney.core.common.service.impl.DomainServiceImpl;

/**
 * @author jeffma
 * 
 */
public class RoleServiceImpl extends DomainServiceImpl<Role> implements RoleService {

	/** default constructors */
	public RoleServiceImpl() {
		super();
		setDefaultSort(new String[] { "type asc", "key asc" });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.showmoney.core.account.service.RoleService#get(java.lang.String,
	 * com.showmoney.core.account.entity.Role.Type)
	 */
	public Role get(String key, Type type) throws CoreException {
		return this.get((type.name() + "_" + key).toUpperCase());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.showmoney.core.common.service.impl.DomainServiceImpl#save(java.lang.Object)
	 */
	@Override
	@Transactional(readOnly = false)
	public Role save(Role entity) throws CoreException {
		entity.getMainPermission().setDescription(entity.getDescription() + " 权限");
		return super.save(entity);
	}
}
