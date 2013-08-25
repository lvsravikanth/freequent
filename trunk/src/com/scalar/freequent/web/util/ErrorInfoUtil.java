package com.scalar.freequent.web.util;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.context.support.AbstractMessageSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletRequest;
import java.util.Map;

import com.scalar.freequent.web.request.RequestParameters;
import com.scalar.freequent.util.ResourceUtil;
import com.scalar.core.ScalarRuntimeException;
import com.scalar.core.request.Request;

/**
 * User: sujan
 * Date: Mar 25, 2012
 * Time: 2:47:27 PM
 */
public class ErrorInfoUtil {
	private ErrorInfoUtil() {
	}

	public static void addError(Request request, String resourceName, String key, String params[]) {
		Map<String, Object> errors = getErrors(request);
        String msg = ResourceUtil.getMessage(resourceName, request.getLocale(), key, params);
		errors.put(msg, null);
	}

	public static void addInfo(Request request, String resourceName, String key, String params[]) {
		Map<String, Object> info = getInfos(request);
		String msg = ResourceUtil.getMessage(resourceName, request.getLocale(), key, params);
		info.put(msg, null);
	}

	public static void addErrorMsg(Request request, String errorMsgKey, String errorMsgValue) {
		Map<String, Object> errors = getErrors(request);
		errors.put(errorMsgKey, errorMsgValue);
	}

	public static void addInfoMsg(Request request, String infoMsgKey, String infoMsgValue) {
		Map<String, Object> info = getInfos(request);
		info.put(infoMsgKey, infoMsgValue);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getErrors(Request request) {
		Object obj = ((HttpServletRequest) request.getWrappedObject()).getAttribute(RequestParameters.ATTRIBUTE_ERROR_MESSAGES);
		if (obj == null || (obj instanceof Map)) {
			return (Map<String, Object>) obj;
		}

		throw ScalarRuntimeException.create("",null,"invalid type",null);//todo need to define from resource
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getInfos(Request request) {
		Object obj = ((HttpServletRequest) request.getWrappedObject()).getAttribute(RequestParameters.ATTRIBUTE_INFO_MESSAGES);
		if (obj == null || (obj instanceof Map)) {
			return (Map<String, Object>) obj;
		}

		throw ScalarRuntimeException.create("",null,"invalid type",null);//todo need to define from resource
	}
}
