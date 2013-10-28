package com.scalar.core.request;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

import com.scalar.core.ScalarActionException;

/**
 * User: Sujan Kumar Suppala
 * Date: Oct 26, 2013
 * Time: 3:31:26 PM
 */
/**
 * The <code>Context</code> interface extends <code>Map</code> to represent the request context values.
 *
 */
public interface Context extends Map<String, Object> {
	/**
	 * Constant used to identify the context attribute.
	 */
	public static final String CONTEXT_ATTRIBUTE = "com.scalar.core.request.Context";

	/**
	 * Constant used to identify the context prefix.
	 */
	public static final String PREFIX = ".fui.context.";

	/**
	 * Constant used to identify the context format prefix.
	 */
	public static final String FORMAT_PREFIX = ".fui.context.format";

	/**
	 * Constant used to identify the context prefix length.
	 */
	public static final int PREFIX_LENGTH = PREFIX.length();

	/**
	 * Constant used to identify the context format prefix length.
	 */
	public static final int FORMAT_PREFIX_LENGTH = FORMAT_PREFIX.length();

	/**
	 * Constant used to identify the root type key.
	 */
	public static final String ROOT_TYPE_KEY = "rootType";

	/**
	 * Constant used to identify the root id key.
	 */
	public static final String ROOT_ID_KEY = "rootId";

	/**
	 * Constant used to identify the root name key.
	 */
	public static final String ROOT_NAME_KEY = "rootName";

	/**
	 * Constant used to identify the root XML name key.
	 */
	public static final String ROOT_XML_NAME_KEY = "rootXmlName";

	/**
	 * Constant used to identify the locale key.
	 */
	public static final String LOCALE_KEY = "locale";

	/**
	 * Constant used to identify the time zone key.
	 */
	public static final String TIME_ZONE_KEY = "timeZone";

	public static final String FORMAT_KEY = "format";

	public static final String FORMAT_DATE_KEY = "date";

	public static final String FORMAT_TIME_KEY = "time";

	public static final String FORMAT_DATE_TIME_KEY = "datetime";

	/**
	 * Constant used to identify the active container type key.
	 */
	public static final String ACTIVE_CONTAINER_TYPE = "activeContainerType";

	/**
	 * Constant used to identify the active container id key.
	 */
	public static final String ACTIVE_CONTAINER_ID = "activeContainerId";

	/**
	 * Sanitizes the values in the context.
	 *
	 * @throws ScalarActionException if there is a problem sanitizing
	 */
	public void sanitize() throws ScalarActionException;
}

