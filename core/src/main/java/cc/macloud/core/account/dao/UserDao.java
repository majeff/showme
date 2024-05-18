/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.account.dao.UserDao
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
package cc.macloud.core.account.dao;

import java.util.List;

import cc.macloud.core.account.entity.Permission;
import cc.macloud.core.account.entity.Role;
import cc.macloud.core.account.entity.User;
import cc.macloud.core.common.dao.ObjectDao;
import cc.macloud.core.common.dao.impl.CommonCriteria;
import cc.macloud.core.common.exception.CoreException;

/**
 * @author jeff.ma
 *
 */
public interface UserDao extends ObjectDao<User> {
   public List<User> getByRole(Role role) throws CoreException;

   public List<User> getListPageableByRole(final CommonCriteria cri, final String[] sortOrder, final int startNode,
         final int returnSize, Role.Type roleType, String roleKey) throws CoreException;

   public Number getListSizeByRole(CommonCriteria cri, Role.Type roleType, String roleKey) throws CoreException;

   public List<User> getByPermission(Permission p) throws CoreException;

}
