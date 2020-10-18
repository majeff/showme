/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.account.service.impl.GroupServiceImpl
   Module Description   :

   Date Created      : 2008/4/24
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

import cc.macloud.core.account.entity.Group;
import cc.macloud.core.account.service.GroupService;
import cc.macloud.core.common.exception.CoreException;
import cc.macloud.core.common.service.impl.DomainServiceImpl;

/**
 * @author jeffma
 */
public class GroupServiceImpl extends DomainServiceImpl<Group> implements GroupService {

	/** default constructors */
	public GroupServiceImpl() {
		super();
		setDefaultSort(new String[] { "type asc", "code asc" });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cc.macloud.core.common.service.impl.DomainServiceImpl#save(java.lang.Object)
	 */
	@Override
	@Transactional(readOnly = false)
	public Group save(Group entity) throws CoreException {
		entity.getManager().setDescription(entity.getDescription() + " 部门主管");
		entity.getManager().getMainPermission().setDescription(entity.getDescription() + " 部门主管权限");
		entity.getRole().setDescription(entity.getDescription() + " 部门成员");
		entity.getRole().getMainPermission().setDescription(entity.getDescription() + " 部门成员权限");
		return super.save(entity);
	}
}
