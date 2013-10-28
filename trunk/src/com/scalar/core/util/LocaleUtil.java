package com.scalar.core.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletRequest;
import java.util.*;
import java.util.prefs.Preferences;

import com.scalar.core.service.Service;
import com.scalar.core.request.Context;
import com.scalar.core.request.Request;

/**
 * User: Sujan Kumar Suppala
 * Date: Oct 26, 2013
 * Time: 3:53:29 PM
 */
/**
 * A bunch of convenience methods to deal with {@link java.util.Locale}
 * objects.
 *
 */
public class LocaleUtil {
	protected static final Log logger = LogFactory.getLog(LocaleUtil.class);
	/** Delimiter between the language, country, and variant strings. */
	static final public String DELIM = "_";

	/** Cache of available locales with appropriate display names. */
	static private HashMap<String, Set<Map.Entry<String, String>>> availLocales_ = new HashMap<String, Set<Map.Entry<String, String>>>();

	/**
	 * A collection to make searching easier.
	 */
	private static List<String> supportedLocales = null;

	/**
	 * Set the supported locales. The values should be of the form language_country (e.g. "en", "en_us") and all
	 * lowercase.
	 *
	 * @param locales the supported locales.
	 */
	public static void setSupportedLocales(String[] locales) {
		setSupportedLocales(Arrays.asList(locales));
	}

	/**
	 * Set the supported locales. The values should be of the form language_country (e.g. "en", "en_us") and all
	 * lowercase.
	 *
	 * @param locales the supported locales.
	 */
	public static void setSupportedLocales(List<String> locales) {
		supportedLocales = locales;

		// Clear out the cache
		availLocales_.clear();
	}

	//=======================================================================
	/**
	 * Returns the locale id for the user.
	 *
	 * @param request the current <code>Request</code>
	 * @return the locale id if found; otherwise <code>null</code>
	 */
	public static String getUserLocaleId(Request request) {
		//todo currently we don't have user preferences
		//Preferences userRoot = PreferencesUtil.getUserRoot(service);
		String localeId = null;//PreferencesUtil.getPreferenceValue(userRoot, PreferencesConst.LOCALE_COMBOKEY, null);
		if (localeId == null) {
			localeId = getLocaleId(request.getLocale());
		}

		return localeId;
	}

	/**
	 * Returns a {@link Locale}, in accordance with the <code>locale</code>
	 * value in the given {@link javax.servlet.ServletRequest} object.
	 *
	 * If you already have a {@link Context} object, then
	 * {@link #getLocale(Context)} is more efficient.
	 *
	 * @param request A {@link javax.servlet.ServletRequest} object.
	 * @return A {@link Locale} object, or <code>null</code> if the
	 *    {@link javax.servlet.ServletRequest} object does not have a {@link Context}
	 *    object with a <code>locale</code> value.
	 * @see #getLocale(Context)
	 */
	static public Locale getLocale(ServletRequest request) {
		return getLocale((Context)request.getAttribute(Context.CONTEXT_ATTRIBUTE));
	}

	//=======================================================================
	/**
	 * Returns a {@link Locale}, in accordance with the <code>locale</code>
	 * value in the given {@link Context} object.
	 *
	 * @param ctx A {@link Context} object.
	 * @return A {@link Locale} object, or <code>null</code> if the
	 *    {@link Context} object does not have a <code>locale</code> value.
	 */
	static public Locale getLocale(Context ctx) {
		return (null == ctx) ? null : getLocale((String)ctx.get(Context.LOCALE_KEY));
	}

	//=======================================================================
	/**
	 * Returns a {@link Locale}, given a locale identifier made
	 * up of its language and optional country strings, separated by a
	 * {@link #DELIM delimiter}.  Examples are "en" and "es_MX".
	 * The variant string containing vendor or browser-specific codes
	 * is ignored.
	 *
	 * @param localeId A locale identifier, made up of its language and
	 *    optional country strings, each separated by the
	 *    {@link #DELIM delimiter}.
	 * @return A {@link Locale} object.
	 */
	static public Locale getLocale(String localeId) {
		if (localeId != null) {
			String[] toks = localeId.split(DELIM);
			switch (toks.length) {
				case 1:
					return new Locale(toks[0]);
				case 2:
				default:
					return new Locale(toks[0], toks[1]);
			}
		}

		return null;
	}

	//=======================================================================
	/**
	 * Returns a locale identifer for the given {@link Locale}, made
	 * up of its language and optional country strings, separated by a
	 * {@link #DELIM delimiter}.  Examples are "en" and "es_MX".
	 * The variant string containing vendor or browser-specific codes
	 * is ignored.
	 *
	 * @param locale A {@link Locale} object.
	 * @return A locale identifier.
	 */
	static public String getLocaleId(Locale locale) {
		String country = locale.getCountry();
		if (country.length() > 0) {
			return locale.getLanguage() + DELIM + locale.getCountry();
		} else {
			return locale.getLanguage();
		}
	}

	//=======================================================================
	/**
	 * Returns a {@link Set} of available locales, with display names
	 * presented in the specified locale.
	 *
	 * @param locale The {@link Locale} in which to present display names.
	 * @return A {@link Set} of {@link Map} entries, where the key for each
	 *    {@link java.util.Map.Entry} is the localized display name, and the value
	 *    is the locale identifier.
	 * @see #getLocaleId
	 */
	static public Set<Map.Entry<String, String>> getAvailableLocales(Locale locale) {
		String localeId = getLocaleId(locale);
		if (availLocales_.containsKey(localeId)) {
			return availLocales_.get(localeId);
		} else {
			Set<Map.Entry<String, String>> newSet = newAvailableLocaleSet(locale);
			availLocales_.put(localeId, newSet);
			return newSet;
		}
	}

	//=======================================================================
	/**
	 * Returns a {@link Set} of available locales, with display names
	 * presented in the specified locale identifier.
	 *
	 * @param localeId The locale identifier in which to present display
	 *    names.
	 * @return A {@link Set} of {@link Map} entries, where the key for each
	 *    {@link java.util.Map.Entry} is the localized display name, and the value
	 *    is the locale identifier.
	 * @see #getLocaleId
	 */
	static public Set<Map.Entry<String, String>> getAvailableLocales(String localeId) {
		if (availLocales_.containsKey(localeId)) {
			return availLocales_.get(localeId);
		} else {
			Set<Map.Entry<String, String>> newSet = newAvailableLocaleSet(getLocale(localeId));
			availLocales_.put(localeId, newSet);
			return newSet;
		}
	}

	//=======================================================================
	/**
	 * Helper method which creates a new {@link Set} of available locales,
	 * with display names presented in the specified locale identifier.
	 *
	 * @param locale The {@link Locale} in which to present display names.
	 * @return A {@link Set} of {@link Map} entries, where the key for each
	 *    {@link java.util.Map.Entry} is the localized display name, and the value
	 *    is the locale identifier.
	 */
	static private Set<Map.Entry<String, String>> newAvailableLocaleSet(Locale locale) {
		// using a TreeMap to get keys in ascending sort order
		TreeMap<String, String> newAvailLocales = new TreeMap<String, String>();
		Locale[] availLocales = Locale.getAvailableLocales();
		for (Locale availLocale : availLocales) {
			String localeId = getLocaleId(availLocale);

			// Only include supported locales if specified
			if ( (null == supportedLocales) || supportedLocales.contains(localeId.toLowerCase()) ) {
				newAvailLocales.put(availLocale.getDisplayName(locale), localeId);
			}
		}

		return newAvailLocales.entrySet();
	}
}

