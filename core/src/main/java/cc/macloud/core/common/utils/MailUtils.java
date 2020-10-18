/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.common.utils.MailUtils
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
package cc.macloud.core.common.utils;

import java.io.UnsupportedEncodingException;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jeffma
 * 
 */
public final class MailUtils {

	/** logger */
	private static Logger logger = LoggerFactory.getLogger(MailUtils.class);

	/** default constructors */
	private MailUtils() {
	}

	/**
	 * @param orgemail
	 * @param orgname
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws AddressException
	 */
	public final static Address getAddress(final String orgemail, final String orgname)
			throws UnsupportedEncodingException, AddressException {
		Address addr = null;
		String email = null, name = null;
		if (StringUtils.isEmpty(orgemail)) {
			return addr;
		} else {
			email = String.valueOf(orgemail).trim();
		}

		if (StringUtils.isNotEmpty(orgname)) {
			name = MimeUtility.encodeText(orgname, "gb2312", "B");
			addr = new InternetAddress(email, name);
		} else {
			addr = new InternetAddress(email);
		}
		return addr;
	}

	/**
	 * @param orgemail
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws AddressException
	 */
	public final static Address getAddress(final String orgemail) throws UnsupportedEncodingException, AddressException {
		Address addr = null;

		if (StringUtils.isNotEmpty(orgemail)) {
			String email = null, name = null;

			email = String.valueOf(orgemail).trim();
			if (orgemail.indexOf(" ") != -1) {
				name = email.substring(0, email.lastIndexOf(" "));
				name = StringUtils.remove(name, "\"");
				email = email.substring(email.lastIndexOf(" ") + 1);
				email = StringUtils.remove(email, "<");
				email = StringUtils.remove(email, ">");
				logger.debug("name:{}, email:{}", name, email);
			} else {
				email = orgemail;
			}
			addr = getAddress(email, name);
		}
		return addr;
	}
}
