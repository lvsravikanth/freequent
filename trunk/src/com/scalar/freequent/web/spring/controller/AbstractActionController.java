package com.scalar.freequent.web.spring.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
import com.scalar.core.ScalarValidationException;
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
			httpServletResponse.setStatus(500);
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

	/**
	 * Create a new binder instance for the given command and request.
	 * <p>Called by {@link #bindAndValidate}. Can be overridden to plug in
	 * custom ServletRequestDataBinder instances.
	 * <p>The default implementation creates a standard ServletRequestDataBinder
	 * and invokes {@link #prepareBinder} and {@link #initBinder}.
	 * <p>Note that neither {@link #prepareBinder} nor {@link #initBinder} will
	 * be invoked automatically if you override this method! Call those methods
	 * at appropriate points of your overridden method.
	 * @param request current HTTP request
	 * @param command the command to bind onto
	 * @return the new binder instance
	 * @throws Exception in case of invalid state or arguments
	 * @see #bindAndValidate
	 * @see #prepareBinder
	 * @see #initBinder
	 */
	protected final ServletRequestDataBinder createBinder(HttpServletRequest request, Object command)
	    throws ScalarActionException {

		ServletRequestDataBinder binder = new ServletRequestDataBinder(command, "command");
		initBinder(request, binder);
		return binder;
	}

	/**
	 * Override this method to initialize the binder with any custom editors.
	 *
	 * @param request
	 * @param binder
	 * @throws Exception
	 */
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ScalarActionException {

	}

	/**
	 * Callback for custom post-processing in terms of binding.
	 * Called on each submit, after standard binding but before validation.
	 * <p>The default implementation delegates to {@link #onBind(HttpServletRequest, Object)}.
	 * @param request current HTTP request
	 * @param command the command object to perform further binding on
	 * @param errors validation errors holder, allowing for additional
	 * custom registration of binding errors
	 * @throws Exception in case of invalid state or arguments
	 */
	protected void onBind(HttpServletRequest request, Object command, BindException errors) throws ScalarActionException {

	}

	/**
	 * Callback for custom post-processing in terms of binding and validation.
	 * Called on each submit, after standard binding and validation,
	 * but before error evaluation.
	 * <p>The default implementation is empty.
	 * @param request current HTTP request
	 * @param command the command object, still allowing for further binding
	 * @param errors validation errors holder, allowing for additional
	 * custom validation
	 * @throws Exception in case of invalid state or arguments
	 * @see #bindAndValidate
	 * @see org.springframework.validation.Errors
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors)
			throws ScalarActionException {
	}

	/**
	 * Bind the parameters of the given request to the given command object.
	 * @param request current HTTP request
	 * @param command the command to bind onto
	 * @return the ServletRequestDataBinder instance for additional custom validation
	 * @throws Exception in case of invalid state or arguments
	 */
	protected final ServletRequestDataBinder bindAndValidate(Object command, HttpServletRequest request) throws ScalarActionException, ScalarValidationException {
		ServletRequestDataBinder binder = createBinder(request, command);
		initBinder(request, binder);
		binder.bind(request);
		BindException errors = new BindException(binder.getBindingResult());
		onBind(request, command, errors);
		validate(command, errors);
		onBindAndValidate(request, command, errors);

		return binder;
	}

	/**
	 * Validation method which will be called upon calling bindAndValidate method.
	 *
	 * @param command
	 * @param errors
	 * @throws ScalarValidationException
	 *
	 * @see #bindAndValidate(Object, javax.servlet.http.HttpServletRequest)
	 */
	protected void validate(Object command, BindException errors) throws ScalarValidationException {

	}

	protected ScalarActionException getActionException(Throwable th) {
		if (th instanceof ScalarActionException) {
			return (ScalarActionException)th;
		}
		MsgObject msgObject = MsgObjectUtil.getMsgObject(FrameworkResource.BASE_NAME, FrameworkResource.UNABLE_TO_PERFORM_ACTION);
		return ScalarActionException.create(msgObject, th);
	}
}

