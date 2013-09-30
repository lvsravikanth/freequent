package com.scalar.core.response;

import com.scalar.freequent.util.StringUtil;
import com.scalar.freequent.l10n.FrameworkResource;
import com.scalar.core.ScalarException;
import com.scalar.core.ScalarActionException;
import com.scalar.core.util.MsgObject;
import com.scalar.core.util.MsgObjectUtil;

import java.util.Map;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * User: ssuppala
 * Date: Sep 30, 2013
 * Time: 2:10:59 PM
 */
/**
 * The <code>ErrorResponse</code> class extends <code>AbstractResponse</code> to implement error output.
 * <p>
 * The error message is provided by {@link #getMessage()}.
 *
 * @author .r.
 * @version $Revision: #1 $ $Date: 2013/06/19 $
 */
public class ErrorResponse extends AbstractResponse {
	/**
	 * Constant used to load and output the message from the Action data.
	 *
	 * @see #load
	 * @see #getOutputAttributes()
	 */
	protected static final String ERROR_MESSAGE = "fui.error.message";

	/**
	 * Constant used to load and output the throwable from the Action data.
	 *
	 * @see #load
	 * @see #getOutputAttributes()
	 */
	protected static final String THROWABLE = "fui.error.throwable";

	/**
	 * Constant used as the tag name for this <code>Response</code>.
	 *
	 * @see #getTag()
	 */
	public static final String FUI_ERROR = "fuiError";

	/**
	 * Constant used for message attribute output.
	 */
	public static final String MESSAGE_ATTRIBUTE = "message";

	/**
	 * Constant used for root message attribute output.
	 */
	public static final String ROOT_MESSAGE_ATTRIBUTE = "rootMessage";

	/**
	 * The message to use as output.
	 */
	protected String message = null;

	/**
	 * The cause of the error. Can be <code>null</code>.
	 */
	protected Throwable throwable = null;

	/**
	 * Prepares the data with the provided message.
	 *
	 * @param data the data to prepare
	 * @param message the message
	 */
	public static void prepareData(Map<String, Object> data, String message) {
		data.put(ERROR_MESSAGE, message);
	}

	/**
	 * Prepares the data with the provided throwable.
	 *
	 * @param data the data to prepare
	 * @param throwable the throwable
	 */
	public static void prepareData(Map<String, Object> data, Throwable throwable) {
		data.put(THROWABLE, throwable);
	}

	/**
	 * Returns <code>true</code> if the data <code>Map</code> has error response data, otherwise <code>false</code>.
	 *
	 * @param data the data
	 * @return <code>true</code> if the data <code>Map</code> has error response data, otherwise <code>false</code>
	 */
	public static boolean hasErrorData(Map<String, Object> data) {
		return ( (null != data.get(ERROR_MESSAGE) ) || (null != data.get(THROWABLE)) );
	}

	/**
	 * Constructs a new <code>ErrorResponse</code>. This is the default constructor.
	 */
	public ErrorResponse() {
	}

	/**
	 * Constructs a new <code>ErrorResponse</code> with the given message.
	 *
	 * @param message the error message
	 */
	public ErrorResponse(String message) {
		setMessage(message);
	}

	/**
	 * Constructs a new <code>ErrorResponse</code> with the given cause.
	 *
	 * @param throwable the cause of the error
	 */
	public ErrorResponse(Throwable throwable) {
		setThrowable(throwable);
	}

	@Override
	public String getTag() {
		return FUI_ERROR;
	}

	/**
	 * Adds the message and root message as attributes to the <code>Response</code> output.
	 * The attribute names are {@link #MESSAGE_ATTRIBUTE} and {@link #ROOT_MESSAGE_ATTRIBUTE}.
	 */
	@Override
	public Map<String, String> getOutputAttributes() {
		Map<String, String> attributes = super.getOutputAttributes();

		// Regular message
		String msg = getMessage();
		if ( !StringUtil.isEmpty(msg) ) {
			attributes.put(MESSAGE_ATTRIBUTE, msg);
		}

		// Root message
		msg = ScalarException.getRootLocalizedMessage(getThrowable());
		if ( !StringUtil.isEmpty(msg) ) {
			attributes.put(ROOT_MESSAGE_ATTRIBUTE, msg);
		}

		return attributes;
	}

	/**
	 * Creates the error output for this <code>Response</code> using the <code>Writer</code>. If there is a
	 * <code>Throwable</code> cause, the stack trace is added to the output.
	 */
	@Override
	public void createOutput(Writer writer) throws ScalarActionException {
		Throwable t = getThrowable();
		if ( null == t ) {
			return;
		}

		// Get transport encoded writer
		//writer = transport.getEncodingWriter(writer);

		// Output the message first
		try {
			writer.write(getMessage());

			// Only output stack trace when...wait for it...trace is enabled. You're welcome.
			if ( logger.isTraceEnabled() ) {
				writer.write('\n');
				PrintWriter printWriter = new PrintWriter(writer);
				t.printStackTrace(printWriter);
			}
		} catch ( IOException e ) {
			MsgObject msgObject = MsgObjectUtil.getMsgObject(FrameworkResource.BASE_NAME, FrameworkResource.WRITE_OUTPUT_FAILURE);
			throw ScalarActionException.create(msgObject, e);
		}
	}

	/**
	 * Loads the message data for this <code>Response</code>.
	 */
	@Override
	public void load(Map<String, Object> loadData) {
		super.load(loadData);

		setMessage(getValue(ERROR_MESSAGE));

		Object obj = loadData.get(THROWABLE);
		if ( (null != obj) && Throwable.class.isInstance(obj) ) {
			setThrowable(Throwable.class.cast(obj));
		}
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

	/**
	 * Sets the cause of the error.
	 *
	 * @param throwable the cause of the error
	 * @see #getThrowable()
	 */
	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;

		if ( null != throwable ) {
			setMessage(ScalarException.getLocalizedMessage(throwable));
		}
	}

	/**
	 * Returns the cause of the error.
	 *
	 * @return the cause of the error
	 * @see #setThrowable(Throwable)
	 */
	public Throwable getThrowable() {
		return throwable;
	}
}
