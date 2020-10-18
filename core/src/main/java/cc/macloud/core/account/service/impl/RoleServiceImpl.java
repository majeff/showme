/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.account.service.impl.RoleServiceImpl
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
package cc.macloud.core.account.service.impl;

import org.springframework.transaction.annotation.Transactional;

import cc.macloud.core.account.entity.Role;
import cc.macloud.core.account.entity.Role.Type;
import cc.macloud.core.account.service.RoleService;
import cc.macloud.core.common.exception.CoreException;
import cc.macloud.core.common.service.impl.DomainServiceImpl;

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
	 * @see cc.macloud.core.account.service.RoleService#get(java.lang.String,
	 * cc.macloud.core.account.entity.Role.Type)
	 */
	public Role get(String key, Type type) throws CoreException {
		return this.get((type.name() + "_" + key).toUpperCase());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cc.macloud.core.common.service.impl.DomainServiceImpl#save(java.lang.Object)
	 */
	@Override
	@Transactional(readOnly = false)
	public Role save(Role entity) throws CoreException {
		entity.getMainPermission().setDescription(entity.getDescription() + " 权限");
		return super.save(entity);
	}
}
