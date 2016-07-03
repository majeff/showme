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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Properties;

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
 * @author jeffma
 */
public class SeqStringGenerator extends SequenceGenerator {
	/** logger */
	private final Logger logger = LoggerFactory.getLogger(getClass());
	/** 'pattern' configuration key word */
	public static final String CONFIG_PATTERN = "pattern";
	/** foramtPattern, default: '00000000' */
	protected String pattern = "00000000";
	/** format */
	private NumberFormat formatter = null;

	/** default constructors */
	public SeqStringGenerator() {
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
			this.formatter = new DecimalFormat(pattern);
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
		result = formatter.format(Long.valueOf(result.toString()));
		return result;
	}
}
