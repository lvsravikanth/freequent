package com.scalar.freequent.web.spring.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import com.scalar.freequent.web.request.RequestParameters;
import com.scalar.freequent.web.util.ErrorInfoUtil;
import com.scalar.core.request.BasicRequest;
import com.scalar.core.request.Request;
import com.scalar.core.util.MsgObject;

/**
 * User: ssuppala
 * Date: Mar 25, 2012
 * Time: 2:25:59 PM
 */
public class AddErrorInfoObjInterceptor extends HandlerInterceptorAdapter {
	//protected final Log logger = LogFactory.getLog(getClass());
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
		//logger.info ( "Checking Errors, Info Objects in Request" );
        Request request = new BasicRequest();
        request.setWrappedObject(httpServletRequest);
		List<MsgObject> errors = ErrorInfoUtil.getErrors(request);
		List<MsgObject> info = ErrorInfoUtil.getInfos(request);
		if (errors == null) {
			//logger.info ( "Adding Errors object to the Request" );
			httpServletRequest.setAttribute(RequestParameters.ATTRIBUTE_ERROR_MESSAGES, new LinkedList<MsgObject>());
		}
		if (info == null) {
			//logger.info ( "Adding Info Object to the Request" );
			httpServletRequest.setAttribute(RequestParameters.ATTRIBUTE_INFO_MESSAGES, new LinkedList<MsgObject>());
		}
		return true;
	}
}
