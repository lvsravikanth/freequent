package com.scalar.core.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.core.request.sanitizer.Sanitizer;
import com.scalar.core.ScalarActionException;
import com.scalar.freequent.util.StringUtil;

/**
 * User: Sujan Kumar Suppala
 * Date: Oct 26, 2013
 * Time: 3:38:22 PM
 */
/**
 * The <code>SanitizerUtil</code> class provides utility functions for sanitizing data.
 *
 */
public class SanitizerUtil {
	/**
	 * Logging facilities for this class.
	 */
	protected static final Log log = LogFactory.getLog(SanitizerUtil.class);

	/**
	 * Returns a sanitized <code>String</code> for the given value.
	 *
	 * The sanitization includes:
	 *
	 * @param value the value to sanitize
	 * @param type the value's type
	 * @return the sanitized value
	 * @throws ScalarActionException if there is a problem sanitizing the value
	 */
	public static String sanitize(String value, String type) throws ScalarActionException {
		// Only do work if we have to
		if ( (null == value) || (value.length() == 0) ) {
			return value;
		}

		String temp;
		if ( Sanitizer.STRING_TYPE.equals(type) ) {
			// Don't allow any HTML
			temp = StringUtil.htmlEscape(value);
			if ( (temp.compareTo(value) != 0) && log.isErrorEnabled() ) {
				log.error("Sanitized string: " + value + " to: " + temp);
			}

			value = temp;
		} else if ( Sanitizer.BOOLEAN_TYPE.equals(type) ) {
			// Only allow true or false
			if ( ("true".compareToIgnoreCase(value) != 0) && ("false".compareToIgnoreCase(value) != 0) ) {
				if ( log.isErrorEnabled() ) {
					log.error("Sanitized boolean: " + value + " to empty");
				}

				value = "";
			}
		} else if ( Sanitizer.INTEGER_TYPE.equals(type) ) {
			try {
				int i = Integer.parseInt(value);
			} catch(NumberFormatException e){
				if ( log.isErrorEnabled() ) {
					log.error("Sanitized integer: " + value + " to empty");
				}

				value = "";
			}
		} else if ( Sanitizer.JSON_TYPE.equals(type) ) {
			// Nothing for now as it is anti-performant
		} else if ( Sanitizer.XML_TYPE.equals(type) ) {
			// Nothing for now as it is anti-performant
		}

		return value;
	}
}
