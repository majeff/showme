package com.showmoney.core.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Date Utility Class This is used to convert Strings to Dates and Timestamps
 * 
 * <p>
 * <a href="DateUtil.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a> Modified by <a href="mailto:dan@getrolling.com">Dan
 *         Kibler </a> to correct time pattern. Minutes should be mm not MM (MM is month).
 * @version $Revision: 1.7 $ $Date: 2005/05/04 04:57:41 $
 */
public class DateUtils {

	// ~ Static fields/initializers =============================================
	private static Logger logger = LoggerFactory.getLogger(DateUtils.class);
	public static final String PATTERN_DATE = "yyyy/MM/dd";
	public static final String PATTERN_DATE2 = "yyyyMMdd";
	public static final String PATTERN_DATE3 = "yyyy-MM-dd";
	public static final String PATTERN_TIME = "HH:mm";
	public static final String PATTERN_TIMESTAMP = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm";

	// ~ Methods ================================================================

	public static final String convertDateToString(Date aDate) {
		return convertDateToString(null, aDate);
	}

	public static final String convertDateToString(String pattern, Date aDate) {
		if (pattern == null) {
			return getDateTime(PATTERN_DATE, aDate);
		}
		return getDateTime(pattern, aDate);
	}

	public static final Date convertStringToDate(String strDate) {
		return convertStringToDate(null, strDate);
	}

	public static final Date convertStringToDate(String aMask, String strDate) {
		SimpleDateFormat df = null;
		Date date = null;
		if (aMask != null) {
			df = new SimpleDateFormat(aMask);
		} else {
			if (strDate.indexOf("/") != -1) {
				df = new SimpleDateFormat(PATTERN_DATE);
			} else if (strDate.indexOf("-") != -1) {
				df = new SimpleDateFormat(PATTERN_DATE3);
			} else {
				df = new SimpleDateFormat(PATTERN_DATE3);
			}
		}

		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			logger.warn("aMask:{},strDate:{}", df.toPattern(), strDate);
		}

		return (date);
	}

	public static Date getCurrentTime() {
		Date currentTime = Calendar.getInstance().getTime();
		return currentTime;
	}

	/**
	 * This method generates a string representation of a date's date/time in the format you specify on input
	 * 
	 * @param aMask
	 *            the date pattern the string is in
	 * @param aDate
	 *            a date object
	 * @return a formatted string representation of the date
	 * 
	 * @see java.text.SimpleDateFormat
	 */
	public static final String getDateTime(String aMask, Date aDate) {
		String returnValue = "";

		if (aDate != null) {
			SimpleDateFormat df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		}

		return returnValue;
	}

	/**
	 * This method returns the current date in the format: MM/dd/yyyy
	 * 
	 * @return the current date
	 * @throws ParseException
	 */
	public static Calendar getToday() {
		Calendar cal = Calendar.getInstance();
		Date today = new Date();
		cal.setTime(today);
		return cal;
	}

	/**
	 * This method returns the current date add days in the format: MM/dd/yyyy
	 * 
	 * @return the current date
	 * @throws ParseException
	 */
	public static Calendar getTodayAddDays(int days) {
		Calendar cal = getToday();
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal;
	}

	/**
	 * field=Calendar.YEAR 回傳 date 那一年最大, 重設 MMdd HHmmssSSS
	 * field=Calendar.MONTH 回傳 date 那一月最大, 重設 dd HHmmssSSS
	 * field=Calendar.DAY_OF_MONTH 回傳 date 那一天最大, 重設 HHmmssSSS
	 * field=Calendar.HOUR_OF_DAY 回傳 date 那一小時最大, 重設 mmssSSS
	 * field=Calendar.MINUTE 回傳 date 那一分最大, 重設 ssSSS
	 * 
	 * @param field
	 * @param date
	 * @return
	 */
	public static Date getLastTimestamp(int field, Date date) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		} else {
			cal.setTime(getCurrentTime());
		}
		switch (field) {
		case Calendar.YEAR:
			cal.set(Calendar.MONTH, cal.getActualMaximum(Calendar.MONTH));
		case Calendar.MONTH:
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		case Calendar.DAY_OF_MONTH:
			cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
		case Calendar.HOUR_OF_DAY:
			cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
		case Calendar.MINUTE:
			cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
			cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
			break;
		default:
			logger.warn("unknow field: {}, default: DAY_OF_MONTH", field);
			cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
			cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
			cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
		}
		return cal.getTime();
	}

	/**
	 * field=Calendar.YEAR 回傳 date 那一年最小, 重設 MMdd HHmmssSSS
	 * field=Calendar.MONTH 回傳 date 那一月最小, 重設 dd HHmmssSSS
	 * field=Calendar.DAY_OF_MONTH 回傳 date 那一天最小, 重設 HHmmssSSS
	 * field=Calendar.HOUR_OF_DAY 回傳 date 那一小時最小, 重設 mmssSSS
	 * field=Calendar.MINUTE 回傳 date 那一分最小, 重設 ssSSS
	 * 
	 * @param field
	 * @param date
	 * @return
	 */
	public static Date getFirstTimestamp(int field, Date date) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		} else {
			cal.setTime(getCurrentTime());
		}
		switch (field) {
		case Calendar.YEAR:
			cal.set(Calendar.MONTH, cal.getActualMinimum(Calendar.MONTH));
		case Calendar.MONTH:
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		case Calendar.DAY_OF_MONTH:
			cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
		case Calendar.HOUR_OF_DAY:
			cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
		case Calendar.MINUTE:
			cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
			cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
			break;
		default:
			logger.warn("unknow field: {}, default: DAY_OF_MONTH", field);
			cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
			cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
			cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
		}
		return cal.getTime();
	}

	public static boolean checkDateBetween(Date target, Date start, Date end, boolean startEq, boolean endEq) {
		boolean result = true;
		if (target == null) {
			target = getCurrentTime();
		}
		if (start != null) {
			int s = target.compareTo(start);
			if (s < 0) {
				result = false;
			} else if (s == 0 && startEq == false) {
				result = false;
			}
		}
		if (end != null) {
			int s = target.compareTo(end);
			if (s > 0) {
				result = false;
			} else if (s == 0 && endEq == false) {
				result = false;
			}
		}
		return result;
	}

	public static Calendar getTargetDateAddDays(int days, Date targetDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(targetDate);
		cal.add(Calendar.DAY_OF_WEEK, days);
		return cal;
	}

	/**
	 * This method returns the current date add minutes
	 * 
	 * @param minutes
	 * @return
	 */
	public static Calendar incrementMinutes(int minutes) {
		Calendar cal = getToday();
		cal.add(Calendar.MINUTE, minutes);
		return cal;
	}
}
