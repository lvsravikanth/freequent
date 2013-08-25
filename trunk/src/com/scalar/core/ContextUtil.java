package com.scalar.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 24, 2013
 * Time: 5:34:53 PM
 */
public class ContextUtil {
    protected static final Log logger = LogFactory.getLog(ContextUtil.class);
    protected static ApplicationContext context = null;


    public ContextUtil() {

    }

    private static boolean initialized = false;

    public static void init(ApplicationContext applicationContext) {
        context = applicationContext;
        initialized = true;
    }


    public static ApplicationContext getApplicationContext() {
        if (!initialized) {
            loadContext();
            initialized = true;
        }

        return context;
    }

    protected static void loadContext() {
        // todo need to load from the classpath
    }
}
