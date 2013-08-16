package com.scalar.freequent.web.spring.controller;

import org.springframework.web.servlet.mvc.multiaction.ParameterMethodNameResolver;
import com.scalar.freequent.web.request.RequestParameters;

/**
 * User: .sujan.
 * Date: Mar 25, 2012
 * Time: 3:35:35 PM
 */
public class ParamMethodNameResolver extends ParameterMethodNameResolver {
	public static final String DEFAULT_METHOD_NAME = "defaultMethod";

	public ParamMethodNameResolver() {
		setParamName(RequestParameters.PARAM_METHOD_NAME);
		setDefaultMethodName(DEFAULT_METHOD_NAME);
	}
}