package com.scalar.freequent.auth.action;

import com.scalar.core.request.Request;
import com.scalar.core.response.Response;
import com.scalar.core.service.ServiceFactory;
import com.scalar.core.ScalarActionException;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.core.util.MsgObject;
import com.scalar.freequent.auth.service.AuthService;
import com.scalar.freequent.web.spring.controller.AbstractActionController;
import com.scalar.freequent.web.util.ErrorInfoUtil;
import com.scalar.freequent.l10n.MessageResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 18, 2013
 * Time: 3:20:47 PM
 */
public class AuthAction extends AbstractActionController {
    protected static final Log logger = LogFactory.getLog(AuthAction.class);
	private static final String METHOD_LOGIN = "login";
	private static final String METHOD_LOGOUT = "logout";
	private static final String METHOD_AUTHENTICATE = "authenticate";

	public ModelAndView defaultProcess(Request request, Response response, Object command, BindException errors) throws ScalarActionException {
        ModelAndView modelAndView = new ModelAndView("auth/login");
        AuthService authService = ServiceFactory.getService(AuthService.class, request);
		try {
			authService.dbTransactionTest();
        	authService.noDbTransactionTest();
		} catch (Exception e) {
			throw ScalarActionException.create (MsgObjectUtil.getMsgObject("Exception in defaultProcess"), null);
		}
		return modelAndView;
    }

	protected boolean getAuthenticationRequired(Request request) {
		String method = request.getMethod();
		if (METHOD_LOGIN.equals (method) ||
				METHOD_LOGOUT.equals (method) ||
				METHOD_AUTHENTICATE.equals(method)) {
			return false;
		}
		return super.getAuthenticationRequired(request);
	}

	public ModelAndView login (Request request, Response response, Object command, BindException errors) throws ScalarActionException {
		if (authenticate(request)) {
			// if authenticated show the home page
			return new ModelAndView ("common/home"); 
		}
		return new ModelAndView("auth/login");
	}

	public ModelAndView authenticate (Request request, Response response, Object command, BindException errors) throws ScalarActionException {
		if (authenticate(request)) {
			// if authenticated successfully show the home page
			return new ModelAndView ("common/home");
		} else {
			MsgObject msgObject = MsgObjectUtil.getMsgObject(MessageResource.BASE_NAME, MessageResource.INVALID_CREDENTIALS);
			ErrorInfoUtil.addError(request, msgObject);
			return new ModelAndView("auth/login");
		}
	}
}
