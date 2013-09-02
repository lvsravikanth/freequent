package com.scalar.core.response;

import com.scalar.core.request.Request;

import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 24, 2013
 * Time: 5:01:47 PM
 */
public interface Response {
    /**
	 * Constant used to identify the JSON format.
	 */
	public static final String JSON_FORMAT = "json";

	/**
	 * Constant used to identify the XML format.
	 */
	public static final String XML_FORMAT = "xml";

    /**
	 * Constant used to identify the TEMPLATE format.
	 */
	public static final String TEMPLATE_FORMAT = "template";

    /**
	 * Constant used to identify the default format.
	 */
	public static final String DEFAULT_FORMAT = "template";

    /**
	 * Constant used to identify the TEMPLATE attribute.
	 */
	public static final String TEMPLATE_ATTRIBUTE = ".freequent.template";

    /**
	 * Constant used to identify the response attribute.
	 */
	public static final String RESPONSE_ATTRIBUTE = "response";

    /**
	 * Constant used to identify the exception attribute.
	 */
	public static final String EXCEPTIOIN_ATTRIBUTE = ".freequent.exception";


    public void setWrappedObject(Object wrappedObject);

    public Object getWrappedObject();

    /**
	 * Sets the <code>Request</code>.
	 *
	 * @param request the <code>Request</code>
	 * @see #getRequest()
	 */
	public void setRequest(Request request);

    /**
	 * Returns the <code>Request</code> that generated this <code>Response</code>.
	 *
	 * @return the <code>Request</code> that generated this <code>Response</code>
	 * @see #setRequest(Request)
	 */
	public Request getRequest();

    /**
	 * Loads the data for this <code>Response</code>.
	 *
	 * @param data the <code>Map</code> of the data to load
	 */
	public void load(Map<String, Object> data);

    public String getViewName ();
}
