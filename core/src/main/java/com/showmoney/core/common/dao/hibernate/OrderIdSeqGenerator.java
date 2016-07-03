/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.common.dao.hibernate.SeqStringGenerator
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
package com.showmoney.core.common.dao.hibernate;

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
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.SequenceGenerator;
import org.hibernate.type.Type;
import org.hibernate.util.PropertiesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jinwei.lin
 */
public class OrderIdSeqGenerator extends SequenceGenerator {
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
	 * @see org.hibernate.id.Configurable#configure(org.hibernate.type.Type, java.util.Properties,
	 * org.hibernate.dialect.Dialect)
	 */
	@Override
	public void configure(Type type, Properties params, Dialect dialect) throws MappingException {
		super.configure(type, params, dialect);
		try {
			this.pattern = PropertiesHelper.getString(CONFIG_PATTERN, params, pattern);
			this.datePattern = PropertiesHelper.getString(DATE_PATTERN, params, datePattern);

			this.formatter = new DecimalFormat(pattern);
			this.dateformatter = new SimpleDateFormat(datePattern);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.id.IdentifierGenerator#generate(org.hibernate.engine.SessionImplementor, java.lang.Object)
	 */
	@Override
	public Serializable generate(SessionImplementor session, Object obj) throws HibernateException {
		Serializable result = super.generate(session, obj);
		logger.debug("oid:{}", result);
		Date now = Calendar.getInstance().getTime();
		numDate = dateformatter.format(now);
		String seq = formatter.format(Long.valueOf(result.toString()));
		result = numDate + seq.substring(0, 3) + String.valueOf(RandomUtils.nextInt(9)) + seq.substring(3, seq.length());

		return result;
	}
}
