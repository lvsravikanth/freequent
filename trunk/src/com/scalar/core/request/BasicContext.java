package com.scalar.core.request;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;

import com.scalar.core.ScalarActionException;
import com.scalar.core.request.sanitizer.Sanitizer;
import com.scalar.core.request.sanitizer.FUISanitizer;

/**
 * User: Sujan Kumar Suppala
 * Date: Oct 26, 2013
 * Time: 3:33:40 PM
 */
/**
 * The <code>BasicContext</code> class extends <code>HashMap</code> to provide the basic context functionality.
 *
 */
public class BasicContext extends HashMap<String, Object> implements Context {
	protected static final Log logger = LogFactory.getLog(BasicContext.class);
	/**
	 * Serialization UID.
	 */
	private static final long serialVersionUID = 2231846266381748238L;

	public void sanitize() throws ScalarActionException {
		Sanitizer sanitizer = new FUISanitizer();

		put(ROOT_TYPE_KEY, sanitizer.sanitize((String)get(ROOT_TYPE_KEY), Sanitizer.STRING_TYPE));
		put(ROOT_ID_KEY, sanitizer.sanitize((String)get(ROOT_ID_KEY), Sanitizer.STRING_TYPE));
		put(ROOT_NAME_KEY, sanitizer.sanitize((String)get(ROOT_NAME_KEY), Sanitizer.STRING_TYPE));
		put(ROOT_XML_NAME_KEY, sanitizer.sanitize((String)get(ROOT_XML_NAME_KEY), Sanitizer.STRING_TYPE));

		put(LOCALE_KEY, sanitizer.sanitize((String)get(LOCALE_KEY), Sanitizer.STRING_TYPE));
		put(TIME_ZONE_KEY, sanitizer.sanitize((String)get(TIME_ZONE_KEY), Sanitizer.JSON_TYPE));

		put(ACTIVE_CONTAINER_TYPE, sanitizer.sanitize((String)get(ACTIVE_CONTAINER_TYPE), Sanitizer.STRING_TYPE));
		put(ACTIVE_CONTAINER_ID, sanitizer.sanitize((String)get(ACTIVE_CONTAINER_ID), Sanitizer.STRING_TYPE));
	}
}
