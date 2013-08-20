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
	public static final String DEFAULT_METHOD_NAME = "defaultProcess";

	public UrlMethodNameResolver() {
	}

    protected String getHandlerMethodNameForUrlPath(String urlPath) {
        int startIdx = urlPath.indexOf("/");
        int lastIdx = urlPath.lastIndexOf("/");
        if (lastIdx == startIdx) {
            // urlPath does not have the method name in the path, hence forwarding to default method
            return DEFAULT_METHOD_NAME;
        }

        String methodName = super.getHandlerMethodNameForUrlPath(urlPath);
        if (methodName == null) {
            return DEFAULT_METHOD_NAME;
        }

        return methodName;
    }
}