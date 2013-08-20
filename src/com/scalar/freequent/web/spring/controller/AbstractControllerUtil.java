package com.scalar.freequent.web.spring.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 19, 2013
 * Time: 8:50:52 PM
 */
public final class AbstractControllerUtil {
    protected static final Log logger = LogFactory.getLog(AbstractControllerUtil.class);
    private static MethodNameResolver methodNameResolver = new UrlMethodNameResolver();

    public static MethodNameResolver getMethodNameResolver() {
        return methodNameResolver;
    }
}
