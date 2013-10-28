package com.scalar.core.request.sanitizer;

import com.scalar.core.request.Request;
import com.scalar.core.ScalarActionException;

/**
 * User: Sujan Kumar Suppala
 * Date: Oct 26, 2013
 * Time: 3:35:11 PM
 */
/**
 * The <code>Sanitizer</code> interface represents an entity which encompasses sanitization services for a
 * <code>Request</code>.
 *
 */
public interface Sanitizer {
	/**
	 * Constant used to identify the string type.
	 */
	public static final String STRING_TYPE = "string";

	/**
	 * Constant used to identify the boolean type.
	 */
	public static final String BOOLEAN_TYPE = "boolean";

	/**
	 * Constant used to identify the integer type.
	 */
	public static final String INTEGER_TYPE = "integer";

	/**
	 * Constant used to identify the JSON type.
	 */
	public static final String JSON_TYPE = "json";

	/**
	 * Constant used to identify the XML type.
	 */
	public static final String XML_TYPE = "xml";

	/**
	 * Sanitizes a <code>Request</code>.
	 *
	 * @param request the <code>Request</code> to be sanitized
	 * @throws ScalarActionException if there is a problem sanitizing the <code>Request</code>
	 */
	public void sanitize(Request request) throws ScalarActionException;

	/**
	 * Returns a sanitized <code>String</code> for the given value.
	 *
	 * @param value the value to sanitize
	 * @param type the value's type
	 * @return the sanitized value
	 * @throws ScalarActionException if there is a problem sanitizing the value
	 */
	public String sanitize(String value, String type) throws ScalarActionException;
}

