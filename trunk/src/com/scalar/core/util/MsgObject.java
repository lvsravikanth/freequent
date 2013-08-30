package com.scalar.core.util;

import com.scalar.core.ScalarRuntimeException;
import com.scalar.freequent.util.ResourceUtil;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: ssuppala
 * Date: Aug 30, 2013
 * Time: 2:26:05 PM
 */
public class MsgObject implements Serializable {
   protected static final Log logger = LogFactory.getLog(MsgObject.class);
	/**
		 * Default message serves as a fallback in case resource bundles
		 * are missing
		 * @vgnInternal
		 */
		protected String mDefaultMsg = null;

	/**
	 * These attributes completely define the message.
	 * @vgnInternal
	 */
	protected String mBundleName;

	/**
	 * These attributes completely define the message.
	 * @vgnInternal
	 */
	protected String mMsgKey;

	/**
	 * These attributes completely define the message.
	 * @vgnInternal
	 */
	protected Object[] mArgs;

	/**
	 * Constructor which specifies the classloader to use to construct
	 * the automatically-generated default msg.  Typically MsgObjects are
	 * only created by either subclasses of MsgCat or by CustomerMsg.
	 *
	 * @param bundleName The bundle name.
	 * @param msgKey The resource bundle key for this message.
	 * @param args The message arguments.
	 * @param loader The classloader to use when constructing default message.
	 * @vgnInternal
	 */
	protected MsgObject( String bundleName,
						 String msgKey,
						 Object[] args,
						 ClassLoader loader
						 ) {
		mBundleName = bundleName;
		mMsgKey = msgKey;
		mArgs = args;

		// Generate a default message based on the default locale.
		mDefaultMsg = localize( loader );
	}

	/**
	 * Generates a localized <code>String</code> based on the given locale.
	 * The specified <code>classloader</code> is used to look up the required
	 * resource bundle.  If loader is <code>null</code>, then the default classloader
	 * is used.  If locale is <code>null</code>, then the default locale is used.
	 *  <p>
	 * If any problems are encountered while constructing
	 * the localized <code>String</code>, the default <code>String</code> is returned,
	 * or <code>null</code> if there is no default <code>String</code>.
	 *
	 * @param locale the locale to which to convert the <code>String</code>.
	 * @param loader the <code>classloader</code> to use for looking up resource bundles.
	 *
	 * @return the localized <code>String</code>. <code>null</code> if there is no default <code>String</code>.
	 */
	public String localize( Locale locale, ClassLoader loader ) {
		if ( mBundleName == null ) return mDefaultMsg;

		return ResourceUtil.getMessage(mBundleName, locale, mMsgKey, mArgs, -1, loader);
	}

	/**
	 * Generates a localized <code>String</code> based on the given locale, using the
	 * default <code>classloader</code>.  If locale is <code>null</code>, then the default locale
	 * is used.
	 *           <p>
	 * If any problems are encountered while constructing
	 * the localized <code>String</code>, the default <code>String</code> is returned,
	 * or <code>null</code> if there is no default <code>String</code>.
	 *
	 * @param locale the locale to which to convert the <code>String</code>.
	 *
	 * @return the localized <code>String</code>. <code>null</code> if there is no default <code>String</code>.
	 */
	public String localize( Locale locale ) {
		return localize( locale, null );
	}

	/**
	 * Generates a localized <code>String</code> based on the default locale, using
	 * the specified <code>classloader</code> to look up the required
	 * resource bundle.  If loader is <code>null</code>, then the default classloader
	 * is used.
	 *          <p>
	 * If any problems are encountered while constructing
	 * the localized <code>String</code>, the default <code>String</code> is returned,
	 * or <code>null</code> if there is no default <code>String</code>.
	 *
	 * @param loader the <code>classloader</code> to use for looking up resource bundles.
	 *
	 * @return the localized <code>String</code>. <code>null</code> if there is no default <code>String</code>.
	 */
	public String localize( ClassLoader loader ) {
		return localize( null, loader );
	}

	/**
	 * Generates a localized <code>String</code> based on the default locale and
	 * default <code>classloader</code>.
	 * <p>
	 * If any problems are encountered while constructing
	 * the localized <code>String</code>, the default <code>String</code> is returned,
	 * or <code>null</code> if there is no default <code>String</code>.
	 *
	 * @return the localized <code>String</code>. <code>null</code> if there is no default <code>String</code>.
	 */
	public String localize() {
		return localize( null, null );
	}

	/**
	 * Returns this message localized for the default locale.
	 * @return localized message.
	 */
	public String toString() {
		try {
			return localize();
		} catch (Exception ignore) {
			return toBundleString();
		}
	}

	/**
	 * Converts a bundle to a <code>String</code>.
	 *
	 * @return <code>str</code> - <code>String</code> representation of the bundle.
	 */
	public String toBundleString() {
		StringBuilder buf = new StringBuilder(128);
		buf.append("{bundlename=").append(mBundleName);
		buf.append("|msgkey=").append(mMsgKey);

		if ( mArgs != null )
		{
			buf.ensureCapacity( mArgs.length << 6);
			for (int i=0; i<mArgs.length; i++) {

				buf.append("|arg");
				buf.append(i);
				buf.append('=');

				Object o = mArgs[i];
				if ( null != o && o.getClass().isArray() ) {
					Object[] array = (Object[])o;
					buf.append(ArrayUtil.toString(array,1000));
				} else {
					buf.append(o);
				}
			}
		}

		buf.append('}');
		return buf.toString();
	}

	/**
	 * Returns the bundle name of this MsgObject.
	 * @return the bundle name
	 */
	public String getBundleName() {
		return mBundleName;
	}

	/**
	 * Returns the msgKey of this MsgObject.
	 * @return the msgKey of this MsgObject.
	 */
	public String getMsgKey() {
		return mMsgKey;
	}
}
