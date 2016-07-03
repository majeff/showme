package com.showmoney.core.common.utils.convert;

import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import com.showmoney.core.common.utils.DateUtils;
import com.showmoney.core.common.utils.StringUtils;

/**
 * This class is converts a java.util.Date to a String and a String to a java.util.Date.
 * 
 * <p>
 * <a href="DateConverter.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@SuppressWarnings("rawtypes")
public class DateConverter implements Converter {

	public Object convert(Class type, Object value) {
		if (value == null) {
			return null;
		} else if (type == Date.class) {
			return convertToDate(type, value);
		} else if (type == String.class) {
			return convertToString(type, value);
		}

		throw new ConversionException("Could not convert " + value.getClass().getName() + " to " + type.getName());
	}

	protected Object convertToDate(Class type, Object value) {
		if (value instanceof String) {
			try {
				if (StringUtils.isEmpty(value.toString())) {
					return null;
				}

				return DateUtils.convertStringToDate((String) value);
			} catch (Exception pe) {
				throw new ConversionException("Error converting String to Date");
			}
		}

		throw new ConversionException("Could not convert " + value.getClass().getName() + " to " + type.getName());
	}

	protected Object convertToString(Class type, Object value) {

		if (value instanceof Date) {
			try {
				return DateUtils.convertDateToString((Date) value);
			} catch (Exception e) {
				throw new ConversionException("Error converting Date to String");
			}
		}

		return value.toString();
	}
}
