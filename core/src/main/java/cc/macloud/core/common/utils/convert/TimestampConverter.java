package cc.macloud.core.common.utils.convert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;

import cc.macloud.core.common.utils.DateUtils;
import cc.macloud.core.common.utils.StringUtils;

/**
 * This class is converts a java.util.Date to a String and a String to a java.util.Date for use as a Timestamp. It is
 * used by BeanUtils when copying properties.
 * 
 * <p>
 * <a href="TimestampConverter.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:dan@getrolling.com">Dan Kibler</a>
 */
@SuppressWarnings("rawtypes")
public class TimestampConverter extends DateConverter {
	private static final DateFormat TS_FORMAT = new SimpleDateFormat(DateUtils.PATTERN_TIMESTAMP);

	@Override
	protected Object convertToDate(Class type, Object value) {

		if (value instanceof String) {
			try {
				if (StringUtils.isEmpty(value.toString())) {
					return null;
				}

				return DateUtils.convertStringToDate(DateUtils.PATTERN_TIMESTAMP, (String) value);
			} catch (Exception pe) {
				throw new ConversionException("Error converting String to Timestamp");
			}
		}

		throw new ConversionException("Could not convert " + value.getClass().getName() + " to " + type.getName());
	}

	@Override
	protected Object convertToString(Class type, Object value) {
		if (value instanceof Date) {
			try {
				return TS_FORMAT.format(value);
			} catch (Exception e) {
				throw new ConversionException("Error converting Timestamp to String");
			}
		}

		return value.toString();
	}
}