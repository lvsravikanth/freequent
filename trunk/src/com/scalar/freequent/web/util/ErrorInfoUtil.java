package com.scalar.freequent.web.util;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.context.support.AbstractMessageSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletRequest;
import java.util.Map;
import java.util.LinkedList;
import java.util.List;

import com.scalar.freequent.web.request.RequestParameters;
import com.scalar.freequent.util.ResourceUtil;
import com.scalar.core.ScalarRuntimeException;
import com.scalar.core.util.MsgObject;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.core.request.Request;

/**
 * User: sujan
 * Date: Mar 25, 2012
 * Time: 2:47:27 PM
 */
public class ErrorInfoUtil {
	private ErrorInfoUtil() {
	}

	public static void addError(Request request, MsgObject msgObject) {
		List<MsgObject> errors = getErrors(request);
		errors.add(msgObject);
	}

	public static void addInfo(Request request, MsgObject msgObject) {
		List<MsgObject> info = getInfos(request);
		info.add(msgObject);
	}

	@SuppressWarnings("unchecked")
	public static List<MsgObject> getErrors(Request request) {
		Object obj = ((HttpServletRequest) request.getWrappedObject()).getAttribute(RequestParameters.ATTRIBUTE_ERROR_MESSAGES);
		if (obj == null || (obj instanceof List)) {
			return (List<MsgObject>) obj;
		}

		throw ScalarRuntimeException.create(MsgObjectUtil.getMsgObject("invalid type"), null);
	}

	@SuppressWarnings("unchecked")
	public static List<MsgObject> getInfos(Request request) {
		Object obj = ((HttpServletRequest) request.getWrappedObject()).getAttribute(RequestParameters.ATTRIBUTE_INFO_MESSAGES);
		if (obj == null || (obj instanceof List)) {
			return (List<MsgObject>) obj;
		}

		throw ScalarRuntimeException.create(MsgObjectUtil.getMsgObject("invalid type"), null);
	}
}
