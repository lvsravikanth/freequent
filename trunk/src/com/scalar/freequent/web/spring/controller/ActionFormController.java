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

/**
 * User: .sujan.
 * Date: Mar 25, 2012
 * Time: 3:42:46 PM
 */
@SuppressWarnings("deprecation")
public abstract class ActionFormController extends SimpleFormController {
	protected final Log logger = LogFactory.getLog(getClass());
	private MethodNameResolver methodNameResolver = new UrlMethodNameResolver();

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);
		binder.registerCustomEditor(java.lang.Integer.TYPE, new CustomPrimitiveNumberEditor(java.lang.Integer.class, null, null));
		DateFormat df = new SimpleDateFormat();
		binder.registerCustomEditor(Date.class, new CustomDateEditor(df, true));
	}

	public abstract ModelAndView defaultProcess(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception;

	public ModelAndView processException(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors, Throwable exception) throws Exception {
		ReflectionUtils.rethrowException(exception);
		return showForm(request, response, errors); //no use return...above line will throw the exception
	}


	protected final ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		//return super.processFormSubmission(request, response, command, errors);    //To change body of overridden methods use File | Settings | File Templates.
		if (errors.hasErrors() || isFormChangeRequest(request)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Data binding errors: " + errors.getErrorCount());
			}
			return showForm(request, response, errors);
		} else {
			try {
				String methodName = this.methodNameResolver.getHandlerMethodName(request);
                if (logger.isDebugEnabled()) {
				    logger.debug("================METHOD NAME=" + methodName);
                }
				return invokeNamedMethod(methodName, request, response, command, errors);
			}
			catch (NoSuchRequestHandlingMethodException ex) {
				return handleNoSuchRequestHandlingMethod(ex, request, response);
			}
		}
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.warn(ex);
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
		return null;
	}

	private ModelAndView invokeNamedMethod(String methodName, HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		ModelAndView modelAndView = null;
		Method method = this.getClass().getMethod(methodName, new Class[]{HttpServletRequest.class,
				HttpServletResponse.class,
				Object.class,
				BindException.class});
		if (method == null) {
			throw new NoSuchRequestHandlingMethodException(methodName, getClass());
		}
		List<Object> params = new ArrayList<Object>(4);
		params.add(request);
		params.add(response);
		params.add(command);
		params.add(errors);
		try {
			modelAndView = (ModelAndView) method.invoke(this, params.toArray(new Object[params.size()]));
		}
		catch (InvocationTargetException ex) {
			processException(request, response, command, errors, ex.getTargetException());
		}
		return modelAndView;
	}
}