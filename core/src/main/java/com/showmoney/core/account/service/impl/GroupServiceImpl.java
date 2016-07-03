/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.account.service.impl.GroupServiceImpl
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
package com.showmoney.core.account.service.impl;

import org.springframework.transaction.annotation.Transactional;

import com.showmoney.core.account.entity.Group;
import com.showmoney.core.account.service.GroupService;
import com.showmoney.core.common.exception.CoreException;
import com.showmoney.core.common.service.impl.DomainServiceImpl;

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
	 * @see com.showmoney.core.common.service.impl.DomainServiceImpl#save(java.lang.Object)
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
