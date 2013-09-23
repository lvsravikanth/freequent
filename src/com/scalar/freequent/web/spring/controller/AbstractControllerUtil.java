package com.scalar.freequent.web.spring.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.validation.BindException;
import org.springframework.util.ReflectionUtils;
import com.scalar.core.request.Request;
import com.scalar.core.response.Response;
import com.scalar.core.ScalarActionException;
import com.scalar.core.ScalarAuthException;
import com.scalar.core.ScalarException;
import com.scalar.core.util.MsgObject;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.freequent.l10n.MessageResource;
import com.scalar.freequent.l10n.FrameworkResource;
import com.scalar.freequent.web.util.ErrorInfoUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 19, 2013
 * Time: 8:50:52 PM
 */
public final class AbstractControllerUtil {
    protected static final Log logger = LogFactory.getLog(AbstractControllerUtil.class);
    private static MethodNameResolver methodNameResolver = new UrlMethodNameResolver();
    private static UrlPathHelper urlPathHelper = new UrlPathHelper();

    public static MethodNameResolver getMethodNameResolver() {
        return methodNameResolver;
    }

    public static UrlPathHelper getUrlPathHelper() {
        return urlPathHelper;
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

    public void invokeNamedMethod(Object tthis, String methodName, Request request, Object command, Map<String, Object> data) throws Exception {
		ModelAndView modelAndView = null;
		Method method = tthis.getClass().getMethod(methodName, new Class[]{Request.class,
				Object.class,
				Map.class});
		if (method == null) {
            MsgObject authenticationMsg = MsgObjectUtil.getMsgObject(FrameworkResource.BASE_NAME, FrameworkResource.NO_SUCH_METHOD_EXCEPTION, methodName);
			throw ScalarActionException.create(authenticationMsg, new NoSuchRequestHandlingMethodException(methodName, getClass()));
		}
		List<Object> params = new ArrayList<Object>(4);
		params.add(request);
		params.add(command);
		params.add(data);
		try {
			modelAndView = (ModelAndView) method.invoke(tthis, params.toArray(new Object[params.size()]));
		}
		catch (InvocationTargetException ex) {
			processException(request, command, data, ex.getTargetException());
		}
	}

     public ModelAndView processException(Request request, Object command, Map<String, Object> data, Throwable exception) throws Exception {
        ReflectionUtils.rethrowException(exception);
        return null; //no use return...above line will throw the exception
    }

    /**
	 * Returns a <code>ModelAndView</code> that is properly setup for a response view.
	 *
	 * @param response the <code>Response</code>
	 * @return a <code>ModelAndView</code>
	 */
	public static ModelAndView createResponseModelAndView(Response response) {
		if ( logger.isDebugEnabled() ) {
			logger.debug("Creating response ModelAndView");
		}

		ModelAndView mav = new ModelAndView(response.getViewName());
		mav.addObject(Response.RESPONSE_ATTRIBUTE, response);
        mav.addAllObjects(response.getActionData());

		return mav;
	}

    /**
	 * Returns a <code>ModelAndView</code> that is properly setup for a response view.
	 *
	 * @param response the <code>Response</code>
	 * @return a <code>ModelAndView</code>
	 */
	public static ModelAndView createResponseModelAndView(Response response, Throwable th) {
		if ( logger.isDebugEnabled() ) {
			logger.debug("Creating response ModelAndView");
		}
        //todo need to consider the json, xml response types
		ModelAndView mav = new ModelAndView();
		mav.addObject (Response.RESPONSE_ATTRIBUTE, response);
        mav.addObject (Response.EXCEPTIOIN_ATTRIBUTE, th);
        if (ScalarException.class.isInstance(th)) {
            mav.setViewName("common/actionexception");
            if (ScalarAuthException.class.isInstance(th)) {
                // check if authentication failed
                ScalarAuthException sae = ScalarAuthException.class.cast(th);
                MsgObject msgObject = sae.getMsgObject();
                MsgObject authenticationMsg = MsgObjectUtil.getMsgObject(FrameworkResource.BASE_NAME, FrameworkResource.AUTHENTICATION_REQUIRED);
                if (msgObject.localize().equals(authenticationMsg.localize())) {
                    ErrorInfoUtil.addError(response.getRequest(), sae.getMsgObject());
                    mav.setViewName("auth/login");
                }
            }
        } else {
            //todo set the error msg
            // some unexpected exception has occcured
            mav.setViewName("common/unknownexception");
        }

		return mav;
	}
}
