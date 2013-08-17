package com.scalar.freequent.web.spring.controller;

import org.springframework.web.servlet.mvc.multiaction.ParameterMethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.AbstractUrlMethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.InternalPathMethodNameResolver;
import com.scalar.freequent.web.request.RequestParameters;

/**
 * User: .sujan.
 * Date: Mar 25, 2012
 * Time: 3:35:35 PM
 */
public class UrlMethodNameResolver extends InternalPathMethodNameResolver {
	public static final String DEFAULT_METHOD_NAME = "defaultMethod";

	public UrlMethodNameResolver() {
	}

    protected String getHandlerMethodNameForUrlPath(String s) {
        String methodName = super.getHandlerMethodNameForUrlPath(s);
        if (methodName == null) {
            return DEFAULT_METHOD_NAME;
        }

        return methodName;
    }
}