/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.account.web.tag.CheckPermissionTag
   Module Description   :

   Date Created      : 2009/12/10
   Original Author   : jeff.ma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package com.showmoney.core.account.web.tag;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.jstl.core.ConditionalTagSupport;

import com.showmoney.core.account.utils.AdminHelper;
import com.showmoney.core.common.utils.StringUtils;

/**
 * @author jeff.ma
 * 
 */
public class CheckPermissionTag extends ConditionalTagSupport {

	/** serialVersionUID */
	private static final long serialVersionUID = 9062191633392781510L;
	private String permissions = null;

	/** default constructor */
	public CheckPermissionTag() {
		super();
	}

	/**
	 * @param permissions
	 *           the permissions to set
	 */
	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.jstl.core.ConditionalTagSupport#condition()
	 */
	@Override
	protected boolean condition() throws JspTagException {
		if ((permissions != null) && (permissions.indexOf(",") != -1)) {
			String[] pArray = StringUtils.split(permissions, ",");
			for (int i = 0; i < pArray.length; i++) {
				if (AdminHelper.hasPermission(pArray[i])) {
					return true;
				}
			}
		} else if (permissions != null) {
			return AdminHelper.hasPermission(permissions);
		}
		return false;
	}

}
