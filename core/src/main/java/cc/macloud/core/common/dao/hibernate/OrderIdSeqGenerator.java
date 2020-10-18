/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.common.dao.hibernate.SeqStringGenerator
   Module Description   :

   Date Created      : 2008/4/25
   Original Author   : jeffma
   Team              :
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.common.dao.hibernate;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.math.RandomUtils;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jinwei.lin
 */
public class OrderIdSeqGenerator extends SequenceStyleGenerator {
	/** logger */
	private final Logger logger = LoggerFactory.getLogger(getClass());
	/** 'pattern' configuration key word */
	private final String CONFIG_PATTERN = "pattern";
	private final String DATE_PATTERN = "datePattern";

	/** foramtPattern, default: '00000000' */
	private String pattern = "000000";
	private String datePattern = "yyMMdd";

	/** format */
	private NumberFormat formatter = null;
	private DateFormat dateformatter = null;

	private String numDate;

	/** default constructors */
	public OrderIdSeqGenerator() {
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.hibernate.id.enhanced.SequenceStyleGenerator#configure(org.hibernate.
	 * type.Type, java.util.Properties, org.hibernate.service.ServiceRegistry)
	 */
	@Override
	public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
		super.configure(type, params, serviceRegistry);
		try {
			this.pattern = params.getProperty(CONFIG_PATTERN, pattern);
			this.datePattern = params.getProperty(DATE_PATTERN, datePattern);

			this.formatter = new DecimalFormat(pattern);
			this.dateformatter = new SimpleDateFormat(datePattern);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.hibernate.id.IdentifierGenerator#generate(org.hibernate.engine.
	 * SessionImplementor, java.lang.Object)
	 */
	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		Serializable result = super.generate(session, object);
		logger.debug("oid:{}", result);
		Date now = Calendar.getInstance().getTime();
		numDate = dateformatter.format(now);
		String seq = formatter.format(Long.valueOf(result.toString()));
		result = numDate + seq.substring(0, 3) + String.valueOf(RandomUtils.nextInt(9))
				+ seq.substring(3, seq.length());

		return result;
	}
}
