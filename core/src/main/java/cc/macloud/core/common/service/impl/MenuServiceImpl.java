/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.common.service.impl.MenuServiceImpl
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
package cc.macloud.core.common.service.impl;

import cc.macloud.core.common.entity.Menu;
import cc.macloud.core.common.exception.CoreException;
import cc.macloud.core.common.service.MenuService;

/**
 * @author jeff.ma
 * 
 */
public class MenuServiceImpl extends DomainServiceImpl<Menu> implements MenuService {

	public Menu getClone(String menuKey) throws CoreException {
		Menu menu = get(menuKey);
		if (menu != null) {
			try {
				menu = menu.clone();
			} catch (CloneNotSupportedException e) {
				logger.error(e.getMessage(), e);
				throw new CoreException(e.getMessage());
			}
		}

		return menu;
	}

}
