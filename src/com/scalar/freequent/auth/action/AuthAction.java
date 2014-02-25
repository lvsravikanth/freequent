package com.scalar.freequent.auth.action;

import com.scalar.core.request.Request;
import com.scalar.core.request.AbstractRequest;
import com.scalar.core.response.Response;
import com.scalar.core.service.ServiceFactory;
import com.scalar.core.ScalarActionException;
import com.scalar.core.ScalarServiceException;
import com.scalar.core.ScalarException;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.core.util.MsgObject;
import com.scalar.freequent.auth.service.AuthService;
import com.scalar.freequent.auth.User;
import com.scalar.freequent.web.spring.controller.AbstractActionController;
import com.scalar.freequent.web.util.ErrorInfoUtil;
import com.scalar.freequent.web.session.SessionParameters;
import com.scalar.freequent.l10n.MessageResource;
import com.scalar.freequent.l10n.ServiceResource;
import com.scalar.freequent.l10n.FrameworkResource;
import com.scalar.freequent.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

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

	public void defaultProcess(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
        ModelAndView modelAndView;
        if (isAuthenticated(request)) {
            data.put(Response.TEMPLATE_ATTRIBUTE, "common/home");
        } else {
            data.put(Response.TEMPLATE_ATTRIBUTE, "auth/login");
        }
    }

	public boolean getAuthenticationRequired(Request request) {
		String method = request.getMethod();
		if (METHOD_LOGIN.equals (method) ||
				METHOD_LOGOUT.equals (method) ||
				METHOD_AUTHENTICATE.equals(method)) {
			return false;
		}
		return super.getAuthenticationRequired(request);
	}

	public void login (Request request,Object command, Map<String, Object> data) throws ScalarActionException {
		if (isAuthenticated(request)) {
			// if authenticated show the home page
			data.put(Response.TEMPLATE_ATTRIBUTE, "common/home");
		} else {
			data.put(Response.TEMPLATE_ATTRIBUTE, "auth/login");
		}
	}

    public void logout (Request request, Object command, Map<String, Object> data) throws ScalarActionException {
        HttpSession session = ((HttpServletRequest)request.getWrappedObject()).getSession();
        if (session != null) {
            session.invalidate();
        }
        User.unset();
        data.put(Response.TEMPLATE_ATTRIBUTE, "auth/login");
    }

	public void authenticate (Request request,Object command, Map<String, Object> data) throws ScalarActionException {
        String uname = request.getParameter ("username");
        String pwd = request.getParameter ("password");

        if (validate(uname, pwd, request)) {
            AuthService authService = ServiceFactory.getService(AuthService.class, request);
            try {
                boolean isValid = authService.checkCredentials(uname, pwd);

                if (isValid) {
                    // if authenticated successfully show the home page
                    User user = authService.getUser(uname);
                    ((HttpServletRequest)request.getWrappedObject()).getSession(true).setAttribute(SessionParameters.ATTRIBUTE_USER, user);
                    ((AbstractRequest)request).setActiveUser(user);
                    data.put(Response.TEMPLATE_ATTRIBUTE, "common/home");
                } else {
                    MsgObject msgObject = MsgObjectUtil.getMsgObject(FrameworkResource.BASE_NAME, FrameworkResource.INVALID_CREDENTIALS);
                    ErrorInfoUtil.addError(request, msgObject);
                    data.put(Response.TEMPLATE_ATTRIBUTE, "auth/login");
                }
            } catch (ScalarServiceException e) {
                MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.CHECK_CREDENTIALS_FAILED);
                throw ScalarActionException.create(msgObject, e);
            }
        } else {
            data.put(Response.TEMPLATE_ATTRIBUTE, "auth/login");
        }
	}

    private boolean validate (String uname, String pwd, Request request) {
        boolean success = true;
        if (StringUtil.isEmpty(uname)) {
            MsgObject msgObject = MsgObjectUtil.getMsgObject(FrameworkResource.BASE_NAME, FrameworkResource.USER_NAME_REQUIRED);
            ErrorInfoUtil.addError(request, msgObject);
            success = false;
        }
        if (StringUtil.isEmpty(pwd)) {
            MsgObject msgObject = MsgObjectUtil.getMsgObject(FrameworkResource.BASE_NAME, FrameworkResource.PASSWORD_REQURIED);
            ErrorInfoUtil.addError(request, msgObject);
            success = false;
        }

        return success;
    }
}
