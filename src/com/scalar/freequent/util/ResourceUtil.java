package com.scalar.freequent.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Locale;
import java.util.Collection;
import java.util.ResourceBundle;
import java.text.MessageFormat;

/**
 * The <code>ResourceUtil</code> class provides utility functions for working with localized resources.
 *
 * @author .sujan.
 * @version $Revision: #1 $ $Date: 2011/11/08 $
 */
public final class ResourceUtil {
	/**
	 * Logging facilities for this class.
	 */
	private static final Log log = LogFactory.getLog(ResourceUtil.class);

	/**
	 * Returns a localized, HTML-escaped message for the given resource base
	 * name, <code>Locale</code> and message key.
	 *
	 * @param baseName the resource base name
	 * @param locale the <code>Locale</code>
	 * @param key the message key
	 * @return the localized message
	 */
	public static String getMessage(String baseName, Locale locale, String key) {
		return getMessage(baseName, locale, key, null);
	}

	/**
	 * Returns a localized and formatted, HTML-escaped message for the given
	 * resource base name, <code>Locale</code>, message key and message
	 * parameter.
	 *
	 * @param baseName the resource base name
	 * @param locale the <code>Locale</code>
	 * @param key the message key
	 * @param param the message parameter
	 * @return the localized and formatted message
	 */
	public static String getMessage(String baseName, Locale locale, String key, Object param) {
		if(param != null){
			if(param instanceof Collection<?>){
				Collection<?> paramCollection=(Collection<?>)param;
				Object[] params=paramCollection.toArray();
				return getMessage(baseName, locale, key, params);
			} else {
				Object[] params = { param };
				return getMessage(baseName, locale, key, params);
			}
		} else {
			return getMessage(baseName, locale, key);
		}
	}

	/**
	 * Returns a localized and formatted, HTML-escaped message for the given
	 * resource base name, <code>Locale</code>, message key and message
	 * parameters.
	 *
	 * @param baseName the resource base name
	 * @param locale the <code>Locale</code>
	 * @param key the message key
	 * @param params the message parameters
	 * @return the localized and formatted message
	 */
	public static String getMessage(String baseName, Locale locale, String key, Object[] params) {
		return getMessage(baseName, locale, key, params, -1);
	}

	public static String getMessage(String baseName, Locale locale, String key, Object[] params, int maxLength ) {
		return getMessage(baseName, locale, key, params, maxLength, null);
	}
	/**
	 * Returns a localized and formatted, HTML-escaped message for the given
	 * resource base name, <code>Locale</code>, message key and message
	 * parameters.
	 *
	 * @param baseName the resource base name
	 * @param locale the <code>Locale</code>
	 * @param key the message key
	 * @param params the message parameters
	 * @param maxLength the maximum length of the message. -1 means no limit
	 * @return the localized and formatted message
	 */
	public static String getMessage(String baseName, Locale locale, String key, Object[] params, int maxLength, ClassLoader loader) {
		String message = null;

		// Make sure we have a locale
		if ( null == locale ) {
			locale = Locale.getDefault();
		}

		try {
			ResourceBundle bundle = loader == null ?
					ResourceBundle.getBundle(baseName, locale) :
					ResourceBundle.getBundle(baseName, locale, loader);
			if ( null != bundle ) {
				message = bundle.getString(key);
			}
		} catch ( Throwable t ) {
			// Catching just throwable on purpose
			if ( log.isErrorEnabled() ) {
				log.error(t);
			}
		}

		// Make sure we found something
		if ( null == message ) {
			if ( log.isErrorEnabled() ) {
				log.error("Unable to find resource bundle for baseName: " + baseName + " key: " + key);
			}

			return baseName + '_' + locale.toString() + '_' + key;
		}

		if(-1 < maxLength && maxLength < message.length()){
			message = message.substring(0, maxLength);
		}

		// HTML-escape the base message and convert Unicode escape characters
		// to HTML numerical entities before adding the params
		message = StringUtil.unicodeEscape(StringUtil.htmlEscape(message));

		// Format the message
		if ( null != params ) {
			MessageFormat formatter = new MessageFormat(message, locale);
			for ( int i = 0; i < params.length; i++ ) {
				// Avoid passing type params:
				// for Choice Formats, i.e., {0,choice,0#...}
				// the params need to be Integer; Strings throw exceptions
				if(params[i] != null){
					String currParam=params[i].toString();
					try {
						int testInt = Integer.parseInt(currParam);
						params[i] = testInt;
						if ( log.isDebugEnabled() ) {
							log.debug("converted to an int " + params[i]);
						}
					} catch ( NumberFormatException e ) {
						// Ignore. keep it a string.
					}
				} else {
					params[i] = "";
				}
			}
			message = formatter.format(params);
		}

		if ( log.isTraceEnabled() ) {
			log.trace("message is " + message);
		}

		return message;
	}
}
