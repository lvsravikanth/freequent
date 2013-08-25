package com.scalar.core.factory;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 24, 2013
 * Time: 5:26:21 PM
 */
public interface Factory {

    /**
	 * Constant that identifies the classpath.
	 */
	public static final String CONFIG_CLASSPATH = "/com/scalar/config/";

    /**
	 * Constant that identifies the customer config location.
	 */
	public static final String CONFIG_LOCATION = "classpath*:" + CONFIG_CLASSPATH;
}
