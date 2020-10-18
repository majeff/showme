/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.message.service.MailService
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
package cc.macloud.core.message.service;

import java.util.List;
import java.util.Map;

import cc.macloud.core.common.exception.CoreException;
import cc.macloud.core.common.service.DomainService;
import cc.macloud.core.message.entity.Mail;

/**
 * @author jeffma
 * 
 */
public interface MailService extends DomainService<Mail> {

	/**
	 * send mail to SMTP Server
	 * 
	 * @param to
	 * @param cc
	 * @param bcc
	 * @param subject
	 * @param body
	 * @param inlines
	 * @param attachments
	 * @param mailFrom
	 * @throws CoreException
	 */
	public void sendMail(String to, String cc, String bcc, String subject, String body, Map<String, String> inlines,
			Map<String, String> attachments, String mailFrom) throws CoreException;

	/**
	 * send mail to SMTP Server
	 * 
	 * @param mail
	 * @return
	 * @throws CoreException
	 */
	public boolean sendMail(Mail mail) throws CoreException;

	/**
	 * 取得未寄送電子郵件
	 * 
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws CoreException
	 */
	public List<Mail> getNoneSendMails(int firstResult, int maxResults) throws CoreException;

	/**
	 * save mail entity for new tx
	 * 
	 * @param entity
	 * @return
	 * @throws CoreException
	 */
	public Mail saveMust(Mail entity) throws CoreException;
}
