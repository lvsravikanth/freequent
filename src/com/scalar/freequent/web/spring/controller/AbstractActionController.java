package com.scalar.freequent.web.spring.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.scalar.core.request.Request;
import com.scalar.core.request.BasicRequest;
import com.scalar.core.request.RequestUtil;
import com.scalar.core.response.Response;
import com.scalar.core.response.BasicResponse;
import com.scalar.core.response.ResponseFactory;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.core.util.MsgObject;
import com.scalar.core.ScalarActionException;
import com.scalar.freequent.web.session.SessionParameters;
import com.scalar.freequent.web.util.ErrorInfoUtil;
import com.scalar.freequent.l10n.MessageResource;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 19, 2013
 * Time: 8:48:34 PM
 */
public abstract class AbstractActionController extends AbstractController implements FreequentController {
    protected static final Log logger = LogFactory.getLog(AbstractActionController.class);
    private MethodNameResolver methodNameResolver = AbstractControllerUtil.getMethodNameResolver();

    public abstract void defaultProcess(Request request, Object command, Map<String, Object> data) throws ScalarActionException;

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        Request request = RequestUtil.prepareRequest(httpServletRequest);
        HashMap<String, Object> data = new HashMap<String, Object>();
        if (logger.isDebugEnabled()) {
            logger.debug("================METHOD NAME=" + request.getMethod());
        }

        try {
            boolean authenticateRequired = getAuthenticationRequired(request);
            if (authenticateRequired) {
                boolean authenticated = isAuthenticated(request);
                if (authenticated) {
                    boolean authorized = getAuthorized(request);
                    if (!authorized) {
                        // forward to a not authorized page
                        MsgObject msgObject = MsgObjectUtil.getMsgObject(MessageResource.BASE_NAME, MessageResource.NOT_AUTHORIZED);
                        throw ScalarActionException.create (msgObject, null);
                        //ErrorInfoUtil.addError(request, msgObject);
                        //return new ModelAndView("auth/notauthorized");
                    }
                } else {
                    // forward to login page as the request is not authenticated.
                    MsgObject msgObject = MsgObjectUtil.getMsgObject(MessageResource.BASE_NAME, MessageResource.AUTHENTICATION_REQUIRED);
                    throw ScalarActionException.create (msgObject, null);
                    //ErrorInfoUtil.addError(request, msgObject);
                    //return new ModelAndView("auth/login");
                }
            }

            // delegate to action method
            AbstractControllerUtil.getInstance().invokeNamedMethod(this, request.getMethod(), request, null, data);
            Response response = ResponseFactory.createResponse(request.getResponseDataFormat(), request, data);
            response.setWrappedObject(httpServletResponse);
            response.load(data);

            return AbstractControllerUtil.createResponseModelAndView(response);

        } catch (Exception ee) {
            // something bad happened
            Response response = ResponseFactory.createResponse(request.getResponseDataFormat(), request, data);
            response.setWrappedObject(httpServletResponse);
            return AbstractControllerUtil.createResponseModelAndView(response, ee);
        }
    }

    protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.warn(ex);
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return null;
    }

    /**
     * Determine whether authentication is required for this request.at
     *
     * @param request <code>Request</code> to be determined for authenticated.
     * @return true - if authentication is requried for this request otherwise false.
     * @see #getAuthorized(com.scalar.core.request.Request)
     */
    protected boolean getAuthenticationRequired(Request request) {
        return true;
    }

    /**
     * Check whether the request is authenticated.
     *
     * @param request request to be authenticated.
     * @return true - if request is authenticated successfully otherwise false.
     * @see #getAuthenticationRequired(com.scalar.core.request.Request)
     * @see #getAuthorized(com.scalar.core.request.Request)
     */
    protected boolean isAuthenticated(Request request) {
        HttpSession session = ((HttpServletRequest) request.getWrappedObject()).getSession();
        if (session == null) {
            return false;
        }
        return session.getAttribute(SessionParameters.ATTRIBUTE_USER) != null;
    }

    /**
     * Method to check for the capability to execute the given request. This method will be executed only if the request is
     * marked for the authenticate required.
     * The implementors should override this method to check for the capabilities for the respective Action.
     *
     * @param request Request to be check for isAuthorized.
     * @return true - if the request is authorized otherwise false.
     * @see #getAuthenticationRequired(com.scalar.core.request.Request)
     */
    protected boolean getAuthorized(Request request) {
        return true;
    }
}

