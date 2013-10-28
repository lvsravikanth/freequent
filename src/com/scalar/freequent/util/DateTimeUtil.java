package com.scalar.freequent.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.scalar.core.request.Context;
import com.scalar.core.util.LocaleUtil;
import com.scalar.core.util.TimeZoneUtil;

/**
 * The <code>DateTimeUtil</code> class provides utility methods for working with date and time.
 *
 * User: Sujan Kumar Suppala
 * Date: Sep 22, 2013
 * Time: 12:04:31 PM
 */
public class DateTimeUtil {

    protected static final Log logger = LogFactory.getLog(DateTimeUtil.class);

	public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

	public static final String DEFAULT_TIME_FORMAT = "hh:mm:ss a";

	public static final String DEFAULT_DATE_TIME_FORMAT =  DEFAULT_DATE_FORMAT + " " + DEFAULT_TIME_FORMAT;

	/**
	 * Constant used to define the number of milliseconds in a day.
	 */
	public static final long MILLISECONDS_IN_A_DAY = 86400000;

	/**
	 * Returns a localized value for an object.
	 *
	 * @param obj the object
	 * @param locale the <code>Locale</code>
	 * @param timeZone the <code>TimeZone</code>
	 * @return the localized value
	 */
	public static String getLocalizedValue(Object obj, Locale locale, TimeZone timeZone) {
		// No date no value
		if ( null == obj ) {
			return "";
		}

		// Default locale if needed
		if ( null == locale ) {
			locale = Locale.getDefault();
		}

		DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG, locale);
		if ( null != timeZone ) {
			format.setTimeZone(timeZone);
		}

		return format.format(obj);
	}

	/**
	 * Returns a localized <code>DateFormat</code> object using the
	 * <code>DateFormat.SHORT</code> convention for formatting the date.
	 *
	 * @param locale the <code>Locale</code>
	 * @return localized <code>DateFormat</code>
	 */
	public static DateFormat getDateFormat(Locale locale) {
		//return DateFormat.getDateInstance(DateFormat.SHORT, locale);
		return new SimpleDateFormat(DEFAULT_DATE_FORMAT, locale);
	}

	/**
	 * Returns a localized <code>DateFormat</code> object using the
	 * <code>DateFormat.SHORT</code> convention for formatting the date and time.
	 *
	 * @param locale <code>Locale</code> object which represents a specific geographical, political, or cultural region.
	 * @return <code>DateFormat</code> object which formats and parses dates or time in a language-independent manner.
	 */
	public static DateFormat getDateTimeFormat(Locale locale) {
		//return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
		return new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT, locale);
	}

	public static DateFormat getTimeFormat(Locale locale) {
		//return DateFormat.getTimeInstance(DateFormat.SHORT, locale);
		return new SimpleDateFormat(DEFAULT_TIME_FORMAT, locale);
	}

	/**
	 * Returns the end of day for the given date.
	 *
	 * The date will have the time fields set to 23:59:59.999.
	 *
	 * @param date the date
	 * @param timezone the timezone
	 * @return the end of day
	 */
	public static Date getEndOfDay(Date date, TimeZone timezone) {
		Calendar calendar = Calendar.getInstance(timezone);
		calendar.setTime(date);

		// Zero out the time
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		// Add 23:59:59.999
		calendar.add(Calendar.MILLISECOND, (int)(MILLISECONDS_IN_A_DAY - 1));
		return calendar.getTime();
	}

	/**
	 * Returns the start of the following day for the given date.
	 *
	 * The date will have the time fields set to 00:00:00.000.
	 *
	 * @param date the date
	 * @param timezone the timezone
	 * @return the start of the following day
	 */
	public static Date getStartOfNextDay(Date date, TimeZone timezone) {
		Calendar calendar = Calendar.getInstance(timezone);
		calendar.setTime(date);

		// Add a day
		calendar.add(Calendar.DAY_OF_MONTH, 1);

		// Zero out the time
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	/**
	 * Returns the date pattern for this context.
	 *
	 * @param ctx
	 * @return
	 */
	public static String getDatePattern(Context ctx) {
		String datePattern = null;
		if (ctx != null) {
			Object format = ctx.get(Context.FORMAT_KEY);
			if (format instanceof Map) {
				datePattern = (String)((Map)format).get(Context.FORMAT_DATE_KEY);
			}
		}

		return datePattern==null ? DEFAULT_DATE_FORMAT : datePattern;
	}

	/**
	 * Returns the time pattern for this context.
	 *
	 * @param ctx
	 * @return
	 */
	public static String getTimePattern(Context ctx) {
		String timePattern = null;
		if (ctx != null) {
			Object format = ctx.get(Context.FORMAT_KEY);
			if (format instanceof Map) {
				timePattern = (String)((Map)format).get(Context.FORMAT_TIME_KEY);
			}
		}

		return timePattern==null ? DEFAULT_TIME_FORMAT : timePattern;
	}

	/**
	 * Returns the datetime pattern for this context.
	 *
	 * @param ctx
	 * @return
	 */
	public static String getDateTimePattern(Context ctx) {
		String dateTimePattern = null;
		if (ctx != null) {
			Object format = ctx.get(Context.FORMAT_KEY);
			if (format instanceof Map) {
				dateTimePattern = (String)((Map)format).get(Context.FORMAT_DATE_TIME_KEY);
			}
		}

		return dateTimePattern==null ? DEFAULT_DATE_TIME_FORMAT : dateTimePattern;
	}

	public static DateFormat getDateFormatter(Context ctx) {
		Locale locale = LocaleUtil.getLocale(ctx);
		TimeZone timeZone = TimeZoneUtil.getTimeZone(ctx);
		DateFormat dateFormatter = getDateFormat(locale);
		dateFormatter.setTimeZone(timeZone);

		return dateFormatter;
	}
}

