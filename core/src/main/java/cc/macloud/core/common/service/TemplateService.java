/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.common.service.TemplateService
   Module Description   :

   Date Created      : 2008/3/14
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.common.service;

import java.util.Map;

import cc.macloud.core.common.exception.CoreException;
import cc.macloud.core.message.entity.Mail;

/**
 * @author jeffma
 * 
 */
public interface TemplateService {

	/**
	 * @param templateName
	 *           template name
	 * @param objs
	 *           input data
	 * @return result string
	 * @throws CoreException
	 */
	public String format(String templateName, Map<String, Object> objs) throws CoreException;

	/**
	 * @param template
	 * @param objs
	 * @return string
	 * @throws CoreException
	 */
	public String formatByStringTemplate(String template, Map<String, Object> objs) throws CoreException;

	/**
	 * @param template
	 * @param name
	 * @param values
	 * @return string
	 * @throws CoreException
	 */
	public String formatByString(String template, String[] name, Object[] values) throws CoreException;

	/**
	 * @param templateName
	 * @param objs
	 * @return
	 * @throws CoreException
	 */
	public Mail formatToMail(String templateName, Map<String, Object> objs) throws CoreException;
}
