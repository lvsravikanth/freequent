
package com.scalar.freequent.util;

import java.util.Properties;

/**
 * The <code>Global</code> interface provides global values for the framework and application.
 *
 * @author .sujan.
 * @version $Revision: #1 $ $Date: 2011/11/08 $
 */
public class Global {
	/**
	 * The one global to rule them all.
	 */
	private static Global global = new Global();

	/**
	 * Returns the global object.
	 *
	 * @return the global object
	 */
	public static Global get() {
		return global;
	}

	/**
	 * The global properties.
	 */
	private Properties properties = new Properties();

	/**
	 * Constructs a <code>Global</code> object. This is the default constructor and is unavailable to others.
	 */
	private Global() {
	}

	/**
	 * Sets the global properties.
	 *
	 * @param properties the global properties
	 */
	public void setProperties(Properties properties) {
		this.properties.putAll(properties);
	}

	/**
	 * Returns the global <code>String</code> value for the given key.
	 *
	 * @param key the key
	 * @return the global <code>String</code> value
	 */
	public static String getString(String key) {
		String value = global.properties.getProperty(key);
		return (null == value) ? "" : value;
	}

	/**
	 * Returns the global <code>boolean</code> value for the given key. If the property is not defined,
	 * <code>false</code> will be returned.
	 *
	 * @param key the key
	 * @return the global boolean value
	 */
	public static boolean getBoolean(String key) {
		String value = getString(key);
		return Boolean.valueOf(value).booleanValue();
	}

	/**
	 * Returns the global <code>int</code> value for the given key. If the property is not defined, the default
	 * value will be returned.
	 *
	 * @param key the key
	 * @param def the default value
	 * @return the global int value if it exists; otherwise the default value
	 */
	public static int getInt(String key, int def) {
		String value = getString(key);
		if ( value.length() > 0 ) {
			return Integer.parseInt(value);
		}

		return def;
	}
}
