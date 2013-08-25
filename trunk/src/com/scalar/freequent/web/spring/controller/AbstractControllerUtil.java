package com.scalar.freequent.web.spring.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import org.springframework.util.ReflectionUtils;
import com.scalar.core.request.Request;
import com.scalar.core.response.Response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.ArrayList;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 19, 2013
 * Time: 8:50:52 PM
 */
public final class AbstractControllerUtil {
    protected static final Log logger = LogFactory.getLog(AbstractControllerUtil.class);
    private static MethodNameResolver methodNameResolver = new UrlMethodNameResolver();

    public static MethodNameResolver getMethodNameResolver() {
        return methodNameResolver;
    }

    private AbstractControllerUtil() {

    }

    protected static AbstractControllerUtil abstractControllerUtil;
    public static AbstractControllerUtil getInstance() {
        if (abstractControllerUtil == null) {
            abstractControllerUtil = new AbstractControllerUtil();
        }

        return abstractControllerUtil;
    }

    public ModelAndView invokeNamedMethod(Object tthis, String methodName, Request request, Response response, Object command, BindException errors) throws Exception {
		ModelAndView modelAndView = null;
		Method method = tthis.getClass().getMethod(methodName, new Class[]{Request.class,
				Response.class,
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
			modelAndView = (ModelAndView) method.invoke(tthis, params.toArray(new Object[params.size()]));
		}
		catch (InvocationTargetException ex) {
			processException(request, response, command, errors, ex.getTargetException());
		}
		return modelAndView;
	}

     public ModelAndView processException(Request request, Response response, Object command, BindException errors, Throwable exception) throws Exception {
        ReflectionUtils.rethrowException(exception);
        return null; //no use return...above line will throw the exception
    }
}
