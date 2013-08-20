package com.scalar.freequent.web.util;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.context.support.AbstractMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import com.scalar.freequent.web.request.RequestParameters;
import com.scalar.core.ScalarRuntimeException;

/**
 * User: sujan
 * Date: Mar 25, 2012
 * Time: 2:47:27 PM
 */
public class ErrorInfoUtil {
	private ErrorInfoUtil() {
	}

	public static void addError(HttpServletRequest request, String errorCode, String args[]) {
		Map<String, Object> errors = getErrors(request);
		WebApplicationContext wac = RequestContextUtils.getWebApplicationContext(request);
		AbstractMessageSource msgSource = (AbstractMessageSource) (wac.getBean("messageSource"));
		String msg = msgSource.getMessage(errorCode, args, null);
		errors.put(msg, null);
	}

	public static void addInfo(HttpServletRequest request, String infoCode, String args[]) {
		Map<String, Object> info = getInfos(request);
		WebApplicationContext wac = RequestContextUtils.getWebApplicationContext(request);
		AbstractMessageSource msgSource = (AbstractMessageSource) (wac.getBean("messageSource"));
		String msg = msgSource.getMessage(infoCode, args, null);
		info.put(msg, null);
	}

	public static void addErrorMsg(HttpServletRequest request, String errorMsgKey, String errorMsgValue) {
		Map<String, Object> errors = getErrors(request);
		errors.put(errorMsgKey, errorMsgValue);
	}

	public static void addInfoMsg(HttpServletRequest request, String infoMsgKey, String infoMsgValue) {
		Map<String, Object> info = getInfos(request);
		info.put(infoMsgKey, infoMsgValue);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getErrors(HttpServletRequest request) {
		Object obj = request.getAttribute(RequestParameters.ATTRIBUTE_ERROR_MESSAGES);
		if (obj == null || (obj instanceof Map)) {
			return (Map<String, Object>) obj;
		}

		throw ScalarRuntimeException.create("",null,"invalid type",null);//todo need to define from resource
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getInfos(HttpServletRequest request) {
		Object obj = request.getAttribute(RequestParameters.ATTRIBUTE_INFO_MESSAGES);
		if (obj == null || (obj instanceof Map)) {
			return (Map<String, Object>) obj;
		}

		throw ScalarRuntimeException.create("",null,"invalid type",null);//todo need to define from resource
	}
}
