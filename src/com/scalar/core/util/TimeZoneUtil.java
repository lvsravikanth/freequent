package com.scalar.core.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletRequest;
import java.util.prefs.Preferences;
import java.util.TimeZone;
import java.util.Arrays;

import com.scalar.core.request.Request;
import com.scalar.core.request.Context;

/**
 * User: Sujan Kumar Suppala
 * Date: Oct 26, 2013
 * Time: 3:57:06 PM
 */
/**
 * A bunch of convenience methods to deal with {@link java.util.TimeZone}
 * objects.
 *
 */
public class TimeZoneUtil {
	protected static final Log logger = LogFactory.getLog(TimeZoneUtil.class);
	/** Cache of available time zones. */
	static private String[] availTimeZones_ = null;

	//=======================================================================
	/**
	 * Returns the timezone id for the user.
	 *
	 * @param request the current <code>Request</code>
	 * @return the timezone id
	 */
	public static String getUserTimeZoneId(Request request) {
		Preferences userRoot = null;//PreferencesUtil.getUserRoot(service);
		String timeZoneId = null;//PreferencesUtil.getPreferenceValue(userRoot, PreferencesConst.TIME_ZONE_COMBOKEY, null);
		if (timeZoneId == null) {
			timeZoneId = TimeZone.getDefault().getID();
		}

		return timeZoneId;
	}

	/**
	 * Returns a {@link TimeZone}, in accordance with the <code>timeZone</code>
	 * value in the given {@link javax.servlet.ServletRequest} object.
	 *
	 * If you already have a {@link Context} object, then
	 * {@link #getTimeZone(Context)} is more efficient.
	 *
	 * @param request A {@link javax.servlet.ServletRequest} object.
	 * @return A {@link TimeZone} object, or <code>null</code> if the
	 *    {@link javax.servlet.ServletRequest} object does not have a {@link Context}
	 *    object with a <code>timeZone</code> value.
	 * @see #getTimeZone(Context)
	 */
	static public TimeZone getTimeZone(ServletRequest request) {
		return getTimeZone((Context)request.getAttribute(Context.CONTEXT_ATTRIBUTE));
	}

	//=======================================================================
	/**
	 * Returns a {@link TimeZone}, in accordance with the <code>timeZone</code>
	 * value in the given {@link Context} object.
	 *
	 * @param ctx A {@link Context} object.
	 * @return A {@link TimeZone} object, or <code>null</code> if the
	 *    {@link Context} object does not have a <code>timeZone</code> value.
	 */
	static public TimeZone getTimeZone(Context ctx) {
		if ( null == ctx ) {
			return null;
		}

		String timeZone = (String)ctx.get(Context.TIME_ZONE_KEY);
		return ( null == timeZone ) ? null : TimeZone.getTimeZone(timeZone);
	}

	//=======================================================================
	/**
	 * Returns a sorted array of available time zone identifiers.
	 *
	 * @return A sorted array of avaialble time zone identifiers.
	 */
	static public String[] getAvailableIDs() {
		if (availTimeZones_ == null) {
			availTimeZones_ = TimeZone.getAvailableIDs();
			Arrays.sort(availTimeZones_);
		}
		return availTimeZones_;
	}

	/**
	 * Returns the timezone id for the user.
	 *
	 * @param request the current <code>Request</code>
	 * @return the timezone id
	 */
	public static String getUserDateFormat(Request request) {
		Preferences userRoot = null;//PreferencesUtil.getUserRoot(service);
		String dateFormat = null;//PreferencesUtil.getPreferenceValue(userRoot, PreferencesConst.TIME_ZONE_COMBOKEY, null);
		if (dateFormat == null) {
			dateFormat = TimeZone.getDefault().getID();
		}

		return dateFormat;
	}
}

