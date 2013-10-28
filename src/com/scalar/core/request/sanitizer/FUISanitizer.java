package com.scalar.core.request.sanitizer;

import java.util.Properties;
import java.util.Map;

import com.scalar.core.request.Request;
import com.scalar.core.ScalarActionException;
import com.scalar.core.util.SanitizerUtil;

/**
 * User: Sujan Kumar Suppala
 * Date: Oct 26, 2013
 * Time: 3:36:52 PM
 */
/**
 * The <code>VUISanitizer</code> class is an implementation of the <code>Sanitizer</code> interface and provides
 * system parameter sanitization.
 *
 * @author .r.
 * @version $Revision: #1 $ $Date: 2011/11/08 $
 */
public class FUISanitizer implements Sanitizer {
	/**
	 * Constant used to identify the VUI system parameter prefix.
	 */
	protected static final String FUI_SYSTEM_PARAMETER_PREFIX = ".fui.";

	/**
	 * Constant used to identify the cache buster parameter.
	 */
	protected static final String CACHE_BUSTER_PARAMETER = "_";

	/**
	 * Sanitizes the properties and context in a <code>Request</code>.
	 *
	 * The system properties are identified by the prefix {@link #FUI_SYSTEM_PARAMETER_PREFIX}. In addition, the
	 * {@link #CACHE_BUSTER_PARAMETER} is also removed.
	 *
	 * Everything in the context is sanitized.
	 *
	 * @param request the <code>Request</code> to be sanitized
	 * @throws ScalarActionException if there is a problem sanitizing the <code>Request</code>
	 */
	public void sanitize(Request request) throws ScalarActionException {
		Properties properties = request.getProperties();

		// Sanitize system parameters
		for ( Map.Entry<Object, Object> entry : properties.entrySet() ) {
			String key = (String)entry.getKey();
			if (key.startsWith(FUI_SYSTEM_PARAMETER_PREFIX) ) {
				entry.setValue(sanitize((String)entry.getValue(), STRING_TYPE));
			}
		}

		// Remove cache buster
		properties.remove(CACHE_BUSTER_PARAMETER);

		request.setProperties(properties);
	}

	/**
	 * Returns a sanitized <code>String</code> for the given value.
	 *
	 * The default sanitizer {@link SanitizerUtil#sanitize(String, String)} is used.
	 *
	 * @param value the value to sanitize
	 * @param type the value's type
	 * @return the sanitized value
	 * @throws ScalarActionException if there is a problem sanitizing the value
	 */
	public String sanitize(String value, String type) throws ScalarActionException {
		return SanitizerUtil.sanitize(value, type);
	}
}

