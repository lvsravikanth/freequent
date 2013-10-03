package com.scalar.core.response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.io.Writer;

import com.scalar.core.ScalarActionException;

/**
 * User: Sujan Kumar Suppala
 * Date: Oct 1, 2013
 * Time: 8:03:53 PM
 *
 * The <code>MessageResponse</code> class extends <code>AbstractResponse</code> to implement message output.
 * <p>
 * The message is provided by {@link #getMessage()}.
 *
 */
public class MessageResponse extends AbstractResponse {
    protected static final Log logger = LogFactory.getLog(MessageResponse.class);
	/**
	 * Constant used to load and output the message from the {@link com.vignette.ui.core.action.Action} data.
	 *
	 * @see #load
	 * @see #getOutputAttributes()
	 */
	protected static final String MESSAGE = "vui.message";

	/**
	 * Constant used as the tag name for this <code>Response</code>.
	 *
	 * @see #getTag()
	 */
	protected static final String VUI_MESSAGE = "vuiMessage";

	/**
	 * Constant used for message attribute output.
	 */
	protected static final String MESSAGE_ATTRIBUTE = "message";

	/**
	 * The message to use as output.
	 */
	protected String message = null;

	/**
	 * Prepares the data with the provided message.
	 *
	 * @param data the data to prepare
	 * @param message the message
	 */
	public static void prepareData(Map<String, Object> data, String message) {
		data.put(MESSAGE, message);
	}

	/**
	 * Returns <code>true</code> if the data <code>Map</code> has message response data, otherwise <code>false</code>.
	 *
	 * @param data the data
	 * @return <code>true</code> if the data <code>Map</code> has message response data, otherwise <code>false</code>
	 */
	public static boolean hasMessageData(Map<String, Object> data) {
		return ( null != data.get(MESSAGE) );
	}

	/**
	 * Constructs a new <code>MessageResponse</code>. This is the default constructor.
	 */
	public MessageResponse() {
	}

	/**
	 * Constructs a new <code>MessageResponse</code> with the given message.
	 *
	 * @param message the message
	 */
	public MessageResponse(String message) {
		setMessage(message);
	}

	@Override
	public String getTag() {
		return VUI_MESSAGE;
	}

	/**
	 * Creates no output for this <code>Response</code>. The actual output occurs in
	 * {@link #getOutputAttributes()}.
	 */
    public void createOutput(Writer writer) throws ScalarActionException {
        // Nothing to do here
    }

    /**
	 * Adds the message as an attribute to the <code>Response</code> output. The attribute name is {@link #MESSAGE}.
	 */
	@Override
	public Map<String, String> getOutputAttributes() {
		Map<String, String> attributes = super.getOutputAttributes();
		attributes.put(MESSAGE_ATTRIBUTE, getMessage());
		return attributes;
	}

	/**
	 * Loads the message data for this <code>Response</code>.
	 */
	@Override
	public void load(Map<String, Object> loadData) {
		super.load(loadData);

		setMessage(getValue(MESSAGE));
	}

	/**
	 * Sets the message to use as output.
	 *
	 * @param message the message
	 * @see #getMessage()
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Returns the message to use as output.
	 *
	 * @return the message
	 * @see #setMessage(String)
	 */
	public String getMessage() {
		return message;
	}
}

