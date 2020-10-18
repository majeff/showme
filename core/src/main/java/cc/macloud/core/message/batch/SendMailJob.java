package cc.macloud.core.message.batch;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import cc.macloud.core.account.entity.User;
import cc.macloud.core.common.utils.StringUtils;
import cc.macloud.core.message.entity.Mail;
import cc.macloud.core.message.service.MailService;

/**
 * @author jeff.ma
 * 
 */
public class SendMailJob {

	/** logger */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected static ClassPathXmlApplicationContext ctx = null;

	private MailService mailService;
	private static String prefix = null;

	/**
	 * 
	 */
	public SendMailJob() {
		mailService = ctx.getBean(MailService.class);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ctx = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml", "applicationContext-*.xml" });
		String username = System.getenv("USERNAME");
		if (StringUtils.isBlank(username)) {
			username = System.getenv("USER");
		}
		if (StringUtils.isBlank(username)) {
			username = "test00";
		}

		User tester = new User(username);
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(tester, "password"));

		if (args != null && args.length >= 1) {
			SendMailJob.prefix = args[0];
		}

		SendMailJob job = new SendMailJob();
		job.execute();
	}

	public void execute() {
		List<Mail> mails = mailService.getNoneSendMails(0, -1);

		for (Mail m : mails) {
			if (StringUtils.isNotBlank(prefix)) {
				m.setSubject(prefix + m.getSubject());
			}
			try {
				mailService.sendMail(m);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
