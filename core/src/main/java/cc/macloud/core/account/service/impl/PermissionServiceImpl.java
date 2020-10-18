/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.account.service.impl.PermissionServiceImpl
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

import cc.macloud.core.account.entity.Permission;
import cc.macloud.core.account.entity.Permission.Type;
import cc.macloud.core.account.service.PermissionService;
import cc.macloud.core.common.exception.CoreException;
import cc.macloud.core.common.service.impl.DomainServiceImpl;

/**
 * @author jeffma
 * 
 */
public class PermissionServiceImpl extends DomainServiceImpl<Permission> implements PermissionService {

	/** default constructors */
	public PermissionServiceImpl() {
		super();
		setDefaultSort(new String[] { "type asc", "key asc" });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cc.macloud.core.common.service.impl.DomainServiceImpl#delete(java.lang.Object)
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(Permission entity) throws CoreException {
		if (!Permission.Type.OTHER.equals(entity.getType())) {
			throw new CoreException("errors.account.permission.delete", entity.getKey(), entity.getType().toString());
		}
		super.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cc.macloud.core.account.service.PermissionService#get(java.lang.String,
	 * cc.macloud.core.account.entity.Permission.Type)
	 */
	public Permission get(String key, Type type) throws CoreException {
		return this.get((type.name() + "_" + key).toUpperCase());
	}
}
