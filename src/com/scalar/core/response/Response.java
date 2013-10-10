package com.scalar.core.response;

import com.scalar.core.request.Request;
import com.scalar.core.ScalarActionException;

import java.util.Map;
import java.io.Writer;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 24, 2013
 * Time: 5:01:47 PM
 */
public interface Response {

    /**
	 * Constant used to identify an error <code>Response</code> implementation.
	 *
	 */
	public static final String ERROR = "error";

	/**
	 * Constant used to identify a message <code>Response</code> implementation.
	 *
	 */
	public static final String MESSAGE = "message";

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

    /**
	 * Constant used to identify the items attribute.
	 */
	public static final String ITEMS_ATTRIBUTE = "items";


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

    /**
	 * Returns the data created for this <code>Response</code> by the <code>Action</code>.
	 *
	 * @return a <code>Map</code> containing the data created by the <code>Action</code>
	 */
	public Map<String, ? extends Object> getActionData();

    public String getViewName ();

    public String getTemplateName ();

    /**
	 * Returns the tag name for this <code>Response</code>.
	 *
	 * @return the tag name
	 */
	public String getTag();

    /**
	 * Cleans up anything after the output is finished.
	 */
	public void cleanup();

    /**
	 * Creates the output for this <code>Response</code> using the <code>Transport</code> and <code>Writer</code>.
	 *
	 * @param writer the <code>Writer</code> for the output
	 * @throws ScalarActionException if there is a problem creating the output
	 */
	public void createOutput(Writer writer) throws ScalarActionException;

	/**
	 * Returns the <code>Response</code> output attributes.
	 *
	 * @return the output attributes
	 */
	public Map<String, String> getOutputAttributes();
}
