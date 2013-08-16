package com.scalar.freequent.web.spring.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.LinkedHashMap;

import com.scalar.freequent.web.request.RequestParameters;
import com.scalar.freequent.web.util.ErrorInfoUtil;

/**
 * User: ssuppala
 * Date: Mar 25, 2012
 * Time: 2:25:59 PM
 */
public class AddErrorInfoObjInterceptor extends HandlerInterceptorAdapter {
	//protected final Log logger = LogFactory.getLog(getClass());
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
		//logger.info ( "Checking Errors, Info Objects in Request" );
		Map<String, Object> errors = ErrorInfoUtil.getErrors(httpServletRequest);
		Map<String, Object> info = ErrorInfoUtil.getInfos(httpServletRequest);
		if (errors == null) {
			//logger.info ( "Adding Errors object to the Request" );
			httpServletRequest.setAttribute(RequestParameters.ATTRIBUTE_ERROR_MESSAGES, new LinkedHashMap<String, Object>());
		}
		if (info == null) {
			//logger.info ( "Adding Info Object to the Request" );
			httpServletRequest.setAttribute(RequestParameters.ATTRIBUTE_INFO_MESSAGES, new LinkedHashMap<String, Object>());
		}
		return true;
	}
}
