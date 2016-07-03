/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.account.dao.UserDao
   Module Description   :

   Date Created      : 2009/11/19
   Original Author   : jeff.ma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package com.showmoney.core.account.dao;

import java.util.List;

import com.showmoney.core.account.entity.Role;
import com.showmoney.core.account.entity.User;
import com.showmoney.core.common.dao.ObjectDao;
import com.showmoney.core.common.dao.impl.CommonCriteria;
import com.showmoney.core.common.exception.CoreException;

/**
 * @author jeff.ma
 * 
 */
public interface UserDao extends ObjectDao<User> {
	public List<User> getByRole(Role role) throws CoreException;
	
public List<User> getListPageableByRole(final CommonCriteria cri, final String[] sortOrder,final int startNode, final int returnSize,Role.Type roleType,String roleKey) throws CoreException;
	
	public Number getListSizeByRole(CommonCriteria cri,Role.Type roleType,String roleKey) throws CoreException;
}


