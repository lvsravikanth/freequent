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

import com.scalar.core.request.Request;
import com.scalar.core.request.BasicRequest;
import com.scalar.core.response.Response;
import com.scalar.core.response.BasicResponse;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 19, 2013
 * Time: 8:48:34 PM
 */
public abstract class AbstractActionController extends AbstractController implements FreequentController {
    protected static final Log logger = LogFactory.getLog(AbstractActionController.class);
    private MethodNameResolver methodNameResolver = AbstractControllerUtil.getMethodNameResolver();

    public abstract ModelAndView defaultProcess(Request request, Response response, Object command, BindException errors) throws Exception;

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
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

            return AbstractControllerUtil.getInstance().invokeNamedMethod(this, methodName, request, response, null, null);
        }
        catch (NoSuchRequestHandlingMethodException ex) {
            return handleNoSuchRequestHandlingMethod(ex, httpServletRequest, httpServletResponse);
        }
    }

    protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.warn(ex);
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return null;
    }
}

