package com.scalar.freequent.web.spring.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.ArrayList;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 19, 2013
 * Time: 8:48:34 PM
 */
public abstract class AbstractActionController extends AbstractController implements FreequentController {
    protected static final Log logger = LogFactory.getLog(AbstractActionController.class);
    private MethodNameResolver methodNameResolver = AbstractControllerUtil.getMethodNameResolver();

    public abstract ModelAndView defaultProcess(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception;

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String methodName = this.methodNameResolver.getHandlerMethodName(request);
            if (logger.isDebugEnabled()) {
                logger.debug("================METHOD NAME=" + methodName);
            }
            return invokeNamedMethod(methodName, request, response, null, null);
        }
        catch (NoSuchRequestHandlingMethodException ex) {
            return handleNoSuchRequestHandlingMethod(ex, request, response);
        }
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

    public ModelAndView processException(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors, Throwable exception) throws Exception {
        ReflectionUtils.rethrowException(exception);
        return null; //no use return...above line will throw the exception
    }

    protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.warn(ex);
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return null;
    }
}

