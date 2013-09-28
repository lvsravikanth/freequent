package com.scalar.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 24, 2013
 * Time: 5:34:53 PM
 */
public class ContextUtil {
    protected static final Log logger = LogFactory.getLog(ContextUtil.class);
    protected static ApplicationContext context = null;

    /**
	 * Constant used to identify an include context path attribute.
	 */
	public static final String INCLUDE_CONTEXT_PATH = "javax.servlet.include.context_path";


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

    /**
	 * Returns the context path for the current request. Will check the include context path first before falling back
	 * to the default request context path.
	 *
	 * @param request the current request
	 * @return the context path
	 */
	public static String getContextPath(HttpServletRequest request) {
		// Try include context path first
		String contextPath = (String)request.getAttribute(INCLUDE_CONTEXT_PATH);

		// Fallback to default if the web app is invoked directly and not through the include
		if ( null == contextPath ) {
			contextPath = request.getContextPath();
		}

		return contextPath;
	}
}
