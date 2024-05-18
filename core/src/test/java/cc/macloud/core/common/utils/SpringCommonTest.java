/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.common.utils.SpringCommonTest
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
package cc.macloud.core.common.utils;

import org.hibernate.SessionFactory;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import cc.macloud.core.account.entity.User;

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
			SecurityContextHolder.getContext()
					.setAuthentication(new UsernamePasswordAuthenticationToken(tester, "password"));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

//	@Before
//	public void setUpMethod() throws Exception {
//		if (sessionFactory != null) {
//			Session session = SessionFactoryUtils.getDataSource(sessionFactory);
//			TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
//		}
//	}
//
//	@After
//	public void tearDownMethod() throws Exception {
//		if (sessionFactory != null) {
//			SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
//			SessionFactoryUtils.closeSession(sessionHolder.getSession());
//		}
//	}
}
