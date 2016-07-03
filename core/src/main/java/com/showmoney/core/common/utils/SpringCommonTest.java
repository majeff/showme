/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.common.utils.SpringCommonTest
   Module Description   :

   Date Created      : 2008/12/24
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package com.showmoney.core.common.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.showmoney.core.account.entity.User;

/**
 * @author jeffma
 * 
 */
@Ignore
public class SpringCommonTest {

	/** logger */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private static SessionFactory sessionFactory;
	protected static ClassPathXmlApplicationContext ctx = null;

	public static void configCtx() {
		try {
			ctx = new ClassPathXmlApplicationContext(new String[] { "classpath*:conf/applicationContext.xml",
					"classpath*:conf/applicationContext-*.xml" });
			String username = System.getenv("USERNAME");
			if (StringUtils.isBlank(username)) {
				username = System.getenv("USER");
			}
			if (StringUtils.isBlank(username)) {
				username = "test00";
			}
			if ((ctx != null) && (ctx.getBean(SessionFactory.class) != null)) {
				sessionFactory = ctx.getBean(SessionFactory.class);
			}
			User tester = new User(username);
			tester.setGroupCode("A-00-000-000");
			SecurityContextHolder.getContext().setAuthentication(
					new UsernamePasswordAuthenticationToken(tester, "password"));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Before
	public void setUpMethod() throws Exception {
		if (sessionFactory != null) {
			Session session = SessionFactoryUtils.getSession(sessionFactory, true);
			TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
		}
	}

	@After
	public void tearDownMethod() throws Exception {
		if (sessionFactory != null) {
			SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
			SessionFactoryUtils.closeSession(sessionHolder.getSession());
		}
	}
}
