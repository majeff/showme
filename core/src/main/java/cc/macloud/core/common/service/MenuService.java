/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.common.service.MenuService
   Module Description   :

   Date Created      : 2009/12/14
   Original Author   : jeff.ma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.common.service;

import cc.macloud.core.common.entity.Menu;
import cc.macloud.core.common.exception.CoreException;

/**
 * @author jeff.ma
 * 
 */
public interface MenuService extends DomainService<Menu> {
	public Menu getClone(String menuKey) throws CoreException;
}
