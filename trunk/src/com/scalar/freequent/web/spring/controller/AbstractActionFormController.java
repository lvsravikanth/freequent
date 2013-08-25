package com.scalar.freequent.web.spring.controller;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.util.ReflectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import com.scalar.freequent.web.spring.propertyeditor.CustomPrimitiveNumberEditor;
import com.scalar.core.request.Request;
import com.scalar.core.request.BasicRequest;
import com.scalar.core.response.Response;
import com.scalar.core.response.BasicResponse;

/**
 * User: .sujan.
 * Date: Mar 25, 2012
 * Time: 3:42:46 PM
 */
@SuppressWarnings("deprecation")
public abstract class AbstractActionFormController extends SimpleFormController implements FreequentController {
	protected final Log logger = LogFactory.getLog(getClass());
	private MethodNameResolver methodNameResolver = AbstractControllerUtil.getMethodNameResolver();

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);
		binder.registerCustomEditor(java.lang.Integer.TYPE, new CustomPrimitiveNumberEditor(java.lang.Integer.class, null, null));
		DateFormat df = new SimpleDateFormat();
		binder.registerCustomEditor(Date.class, new CustomDateEditor(df, true));
	}

	public abstract ModelAndView defaultProcess(Request request, Response response, Object command, BindException errors) throws Exception;

	protected final ModelAndView processFormSubmission(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object command, BindException errors) throws Exception {
		//return super.processFormSubmission(request, response, command, errors);    //To change body of overridden methods use File | Settings | File Templates.
		if (errors.hasErrors() || isFormChangeRequest(httpServletRequest)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Data binding errors: " + errors.getErrorCount());
			}
			return showForm(httpServletRequest, httpServletResponse, errors);
		} else {
			try {
				String methodName = this.methodNameResolver.getHandlerMethodName(httpServletRequest);
                if (logger.isDebugEnabled()) {
				    logger.debug("================METHOD NAME=" + methodName);
                }
                Request request = new BasicRequest();
                request.setWrappedObject(httpServletRequest);
                request.setMethod(methodName);

                Response response = new BasicResponse();
                response.setWrappedObject(httpServletResponse);
				return AbstractControllerUtil.getInstance().invokeNamedMethod(this, methodName, request, response, command, errors);
			}
			catch (NoSuchRequestHandlingMethodException ex) {
				return handleNoSuchRequestHandlingMethod(ex, httpServletRequest, httpServletResponse);
			}
		}
	}


	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.warn(ex);
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
		return null;
	}


}