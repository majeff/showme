/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.account.service.SidebarService
   Module Description   :

   Date Created      : 2011/4/20
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.account.service;

import java.util.Date;
import java.util.List;

import cc.macloud.core.account.entity.Sidebar;
import cc.macloud.core.account.entity.User;
import cc.macloud.core.common.service.DomainService;

/**
 * @author jeffma
 * 
 */
public interface SidebarService extends DomainService<Sidebar> {

	/**
	 * 建立 user 的 sidebar html 资料
	 * 
	 * @param user
	 * @return sidebar html
	 */
	public String buildSidebar(User user);

	public String getSidebar(User user);

	/**
	 * 取得第一层的 Sidebar 物件(parent=null)
	 * 
	 * @return
	 */
	public List<Sidebar> getTop();

	public Date getLastModify();

	public void checkLastModify();
}
