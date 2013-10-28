package com.scalar.freequent.web.spring.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.scalar.core.request.*;
import com.scalar.core.response.Response;
import com.scalar.core.response.BasicResponse;
import com.scalar.core.response.ResponseFactory;
import com.scalar.core.response.ErrorResponse;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.core.util.MsgObject;
import com.scalar.core.util.LocaleUtil;
import com.scalar.core.util.TimeZoneUtil;
import com.scalar.core.ScalarActionException;
import com.scalar.core.ScalarLoggedException;
import com.scalar.core.ScalarAuthException;
import com.scalar.freequent.web.session.SessionParameters;
import com.scalar.freequent.web.util.ErrorInfoUtil;
import com.scalar.freequent.l10n.MessageResource;
import com.scalar.freequent.l10n.FrameworkResource;
import com.scalar.freequent.auth.Capability;
import com.scalar.freequent.auth.User;
import com.scalar.freequent.util.StringUtil;
import com.scalar.freequent.util.DateTimeUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.text.SimpleDateFormat;

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
		// The context
		Context context = new BasicContext();

		// Get properties
		Properties properties = RequestUtil.getProperties(request, context);

		request.setProperties(properties);

        HashMap<String, Object> data = new HashMap<String, Object>();
        if (logger.isDebugEnabled()) {
            logger.debug("================METHOD NAME=" + request.getMethod());
        }

        try {
            boolean authenticateRequired = getAuthenticationRequired(request);
            if (authenticateRequired) {
                boolean authenticated = isAuthenticated(request);
                if (authenticated) {
                    try {
                        getAuthorized(request);
                    } catch (ScalarAuthException e) {
                        // forward to a not authorized page
                        MsgObject msgObject = MsgObjectUtil.getMsgObject(FrameworkResource.BASE_NAME, FrameworkResource.NOT_AUTHORIZED);
                        throw ScalarActionException.create (msgObject, e);
                    }
                } else {
                    // forward to login page as the request is not authenticated.
                    httpServletResponse.setStatus(401);
                    MsgObject msgObject = MsgObjectUtil.getMsgObject(FrameworkResource.BASE_NAME, FrameworkResource.AUTHENTICATION_REQUIRED);
                    throw ScalarAuthException.create (msgObject, null);
                }
            }

			// Get the locale and time zone preferences values
			String localeId = LocaleUtil.getUserLocaleId(request);
			String timeZone = TimeZoneUtil.getUserTimeZoneId(request);
			String datePattern = ((SimpleDateFormat)DateTimeUtil.getDateFormat(LocaleUtil.getLocale(localeId))).toPattern();
			String timePattern = ((SimpleDateFormat)DateTimeUtil.getTimeFormat(LocaleUtil.getLocale(localeId))).toPattern();
			String dateTimePattern = ((SimpleDateFormat)DateTimeUtil.getDateTimeFormat(LocaleUtil.getLocale(localeId))).toPattern();


			Context ctx = request.getContext();
			ctx.put(Context.LOCALE_KEY, localeId);
			ctx.put(Context.TIME_ZONE_KEY, timeZone);
			Map<String,String> formatsMap = null;//(Map)ctx.get(Context.FORMAT_KEY);
			if (formatsMap == null) {
				formatsMap = new HashMap<String, String>();
				ctx.put(Context.FORMAT_KEY, formatsMap);
			}
			formatsMap.put(Context.FORMAT_DATE_KEY, datePattern);
			formatsMap.put(Context.FORMAT_TIME_KEY, timePattern);
			formatsMap.put(Context.FORMAT_DATE_TIME_KEY, dateTimePattern);
			// Push the context into data
			data.put(Context.CONTEXT_ATTRIBUTE, request.getContext());

            // delegate to action method
            AbstractControllerUtil.getInstance().invokeNamedMethod(this, request.getMethod(), request, null, data);
            Response response = ResponseFactory.createResponse(request.getResponseDataFormat(), request, data);
            response.setWrappedObject(httpServletResponse);
            response.load(data);

            return AbstractControllerUtil.createResponseModelAndView(response);

        } catch (Exception ee) {
            // something bad happened
            if (! (ee instanceof ScalarLoggedException)) {
                logger.error("Exception while processing the action", ee);
            }
            ErrorResponse.prepareData(data, ee);
            Response response = ResponseFactory.createResponse(Response.ERROR, request, data);
            response.setWrappedObject(httpServletResponse);
            return AbstractControllerUtil.createResponseModelAndView(response);
        }
    }

    protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.warn(ex);
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return null;
    }

    public boolean getAuthenticationRequired(Request request) {
        return true;
    }

    public boolean isAuthenticated(Request request) {
        return request.getActiveUser()!=null;
    }

    public void getAuthorized(Request request) throws ScalarAuthException {
        Capability[] capabilities = getRequiredCapabilities(request);
        if (StringUtil.isEmpty(capabilities)) {
            // there are no capabilities to check for, so authorized by default
            return;
        } else {
            User user = request.getActiveUser();
            user.checkCapabilities(capabilities);
        }
    }

    public Capability[] getRequiredCapabilities(Request request) {
        return new Capability[0];
    }
}

