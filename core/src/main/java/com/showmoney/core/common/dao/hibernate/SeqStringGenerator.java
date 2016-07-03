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
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jeffma
 */
public class SeqStringGenerator extends SequenceStyleGenerator {
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
	 * @see
	 * org.hibernate.id.enhanced.SequenceStyleGenerator#configure(org.hibernate.
	 * type.Type, java.util.Properties, org.hibernate.service.ServiceRegistry)
	 */
	@Override
	public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
		super.configure(type, params, serviceRegistry);
		try {
			this.pattern = params.getProperty(CONFIG_PATTERN, pattern);
			this.formatter = new DecimalFormat(pattern);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.hibernate.id.enhanced.SequenceStyleGenerator#generate(org.hibernate.
	 * engine.spi.SharedSessionContractImplementor, java.lang.Object)
	 */
	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		Serializable result = super.generate(session, object);
		logger.debug("oid:{}", result);
		result = formatter.format(Long.valueOf(result.toString()));

		return result;
	}

}
