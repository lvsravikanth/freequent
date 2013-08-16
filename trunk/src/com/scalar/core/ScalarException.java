package com.scalar.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Locale;

import com.scalar.freequent.util.StringUtil;
import com.scalar.freequent.util.ResourceUtil;

/**
 * Signals that an exception has occurred in the UI. This class is the general class of exceptions produced by failed UI
 * operations.
 *
 * @author .sujan.
 */
public class ScalarException extends Exception {
	/**
	 * Serialization UID.
	 */
	private static final long serialVersionUID = 6681176735351813564L;

	/**
	 * Logging facilities for this class and subclasses.
	 */
	protected final Log log = LogFactory.getLog(getClass());

	/**
	 * The localized message for this exception.
	 */
	private String message = null;

	/**
	 * Returns the first available localized message from the throwable stack. If there are none, then returns
	 * empty <code>String</code>.
	 *
	 * @param throwable the throwable for the localized message
	 *
	 * @return the localized message if available; otherwise <code>String</code>
	 */
	public static String getLocalizedMessage(Throwable throwable) {
		String message = null;

		while ( (null == message) && (null != throwable) ) {
			message = throwable.getLocalizedMessage();
			throwable = throwable.getCause();
		}

		return (null == message) ? "" : message;
	}

	/**
	 * Returns the root localized message from the throwable stack. If there are none, then returns
	 * empty <code>String</code>.
	 *
	 * @param throwable the throwable for the localized message
	 *
	 * @return the root localized message if available; otherwise empty <code>String</code>
	 */
	public static String getRootLocalizedMessage(Throwable throwable) {
		if ( null == throwable ) {
			return "";
		}

		Throwable root = null;
		while ( throwable.getCause() != null ) {
			root = throwable.getCause();
			throwable = root;
		}

		return (null == root) ? "" : root.getLocalizedMessage();
	}

	/**
	 * Constructs a new <code>ScalarException</code> with a localized message.
	 *
	 * @param baseName the base name for use with <code>ResourceUtil.getMessage</code>
	 * @param locale the current <code>Locale</code> for use with <code>ResourceUtil.getMessage</code>
	 * @param key the key for use with <code>ResourceUtil.getMessage</code>
	 * @param throwable the parent cause of this exception. (A <code>null</code> value is permitted, and indicates
	 *            that the cause is nonexistent or unknown.)
	 * @return a new <code>ScalarException</code>
	 * @see com.scalar.freequent.util.ResourceUtil#getMessage(String, java.util.Locale , String)
	 */
	public static ScalarException create(String baseName, Locale locale, String key, Throwable throwable) {
		return create(baseName, locale, key, null, throwable);
	}

	/**
	 * Constructs a new <code>ScalarException</code> with a localized message.
	 *
	 * @param baseName the base name for use with <code>ResourceUtil.getMessage</code>
	 * @param locale the current <code>Locale</code> for use with <code>ResourceUtil.getMessage</code>
	 * @param key the key for use with <code>ResourceUtil.getMessage</code>
	 * @param param the message parameter for use with <code>ResourceUtil.getMessage</code>
	 * @param throwable the parent cause of this exception. (A <code>null</code> value is permitted, and indicates
	 *            that the cause is nonexistent or unknown.)
	 * @return a new <code>ScalarException</code>
	 * @see com.scalar.freequent.util.ResourceUtil#getMessage(String, Locale, String, Object)
	 */
	public static ScalarException create(String baseName, Locale locale, String key, Object param, Throwable throwable) {
		return new ScalarException(baseName, locale, key, param, throwable);
	}

	/**
	 * Constructs a new <code>ScalarException</code> with a localized message.
	 *
	 * @param baseName the base name for use with <code>ResourceUtil.getMessage</code>
	 * @param locale the current <code>Locale</code> for use with <code>ResourceUtil.getMessage</code>
	 * @param key the key for use with <code>ResourceUtil.getMessage</code>
	 * @param params the message parameters for use with <code>ResourceUtil.getMessage</code>
	 * @param throwable the parent cause of this exception. (A <code>null</code> value is permitted, and indicates
	 *            that the cause is nonexistent or unknown.)
	 * @return a new <code>ScalarException</code>
	 * @see ResourceUtil#getMessage(String, Locale, String, Object[])
	 */
	public static ScalarException create(String baseName, Locale locale, String key, Object[] params, Throwable throwable) {
		return new ScalarException(baseName, locale, key, params, throwable);
	}

	/**
	 * Constructs a new <code>ScalarException</code> with a localized message.
	 *
	 * @param baseName the base name for use with <code>ResourceUtil.getMessage</code>
	 * @param locale the current <code>Locale</code> for use with <code>ResourceUtil.getMessage</code>
	 * @param key the key for use with <code>ResourceUtil.getMessage</code>
	 * @param param the message parameter for use with <code>ResourceUtil.getMessage</code>
	 * @param throwable the parent cause of this exception. (A <code>null</code> value is permitted, and indicates
	 *            that the cause is nonexistent or unknown.)
	 * @see com.scalar.freequent.util.ResourceUtil#getMessage(String, Locale, String, Object)
	 */
	protected ScalarException(String baseName, Locale locale, String key, Object param, Throwable throwable) {
		super(throwable);

		// Get it from the resource
		message = StringUtil.htmlUnescape(ResourceUtil.getMessage(baseName, locale, key, param));
	}

	/**
	 * Constructs a new <code>ScalarException</code> with a localized message.
	 *
	 * @param baseName the base name for use with <code>ResourceUtil.getMessage</code>
	 * @param locale the current <code>Locale</code> for use with <code>ResourceUtil.getMessage</code>
	 * @param key the key for use with <code>ResourceUtil.getMessage</code>
	 * @param params the message parameters for use with <code>ResourceUtil.getMessage</code>
	 * @param throwable the parent cause of this exception. (A <code>null</code> value is permitted, and indicates
	 *            that the cause is nonexistent or unknown.)
	 * @see com.scalar.freequent.util.ResourceUtil#getMessage(String, Locale, String, Object[])
	 */
	protected ScalarException(String baseName, Locale locale, String key, Object[] params, Throwable throwable) {
		super(throwable);

		// Get it from the resource
		message = StringUtil.htmlUnescape(ResourceUtil.getMessage(baseName, locale, key, params));
	}

	/**
	 * Returns the localized message <code>String</code>for this exception.
	 *
	 * @return the localized message for this exception
	 */
	@Override
	public String getMessage() {
		return message;
	}
}

