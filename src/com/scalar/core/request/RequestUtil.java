package com.scalar.core.request;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.scalar.freequent.web.spring.controller.AbstractControllerUtil;
import com.scalar.freequent.web.session.SessionParameters;
import com.scalar.freequent.auth.User;
import com.scalar.core.response.Response;
import com.scalar.core.ScalarActionException;

import java.util.Properties;
import java.util.Enumeration;
import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 31, 2013
 * Time: 7:22:15 PM
 */
public class RequestUtil {
    protected static final Log logger = LogFactory.getLog(RequestUtil.class);

    public static Request prepareRequest(HttpServletRequest httpRequest) throws NoSuchRequestHandlingMethodException {
        BasicRequest request = new BasicRequest();
        request.setWrappedObject(httpRequest);
        request.setMethod(AbstractControllerUtil.getMethodNameResolver().getHandlerMethodName(httpRequest));
        httpRequest.setAttribute(Request.REQUEST_ATTRIBUTE, request);
        String urlPath = AbstractControllerUtil.getUrlPathHelper().getLookupPathForRequest(httpRequest);
        request.setResponseDataFormat(getResponseDataFormat(urlPath));
        request.setActiveUser(getUser(httpRequest));
        return request;
    }

    private static String getResponseDataFormat(String urlPath) {
        if (urlPath != null) {
            int idx = urlPath.lastIndexOf(".");
            if (idx != -1) {
                String format = urlPath.substring(idx + 1);
                if (Response.XML_FORMAT.equals(format)) {
                    return Response.XML_FORMAT;
                } else if (Response.JSON_FORMAT.equals(format)) {
                    return Response.JSON_FORMAT;
                }
            }
        }

        return Response.DEFAULT_FORMAT;
    }

    public static Request getRequest (HttpServletRequest httpRequest) {
        return (Request)httpRequest.getAttribute(Request.REQUEST_ATTRIBUTE);
    }

    public static Throwable getException (HttpServletRequest httpRequest) {
        return (Throwable)httpRequest.getAttribute(Response.EXCEPTIOIN_ATTRIBUTE);
    }

    private static User getUser (HttpServletRequest httpServletRequest) {
         HttpSession session = httpServletRequest.getSession();
        if (session == null) {
            return null;
        }
        return (User)session.getAttribute(SessionParameters.ATTRIBUTE_USER);
    }

    public static StringBuffer getRequestURL(Request request) {
		return ((HttpServletRequest)request.getWrappedObject()).getRequestURL();
	}

	/**
	 * Returns the properties from the request.
	 *
	 * @param request the current <code>RequestService</code>
	 * @param context the context
	 * @return the properties from the request
	 * @throws ScalarActionException if there is a problem getting the properties
	 */
	public static Properties getProperties(Request request, Context context)  throws ScalarActionException {
		if ( logger.isDebugEnabled() ) {
			logger.debug("Building request properties");
		}

		// Create the attributes
		Properties properties = new Properties();
		Enumeration<?> enumeration = request.getParameterNames();
		while ( enumeration.hasMoreElements() ) {
			String name = (String)enumeration.nextElement();
			String value = request.getParameter(name);
			if ( name.startsWith(Context.PREFIX) ) {
				context.put(name.substring(Context.PREFIX_LENGTH), value);
			} else {
				properties.setProperty(name, value);
			}
		}

		// Sanitize the context
		context.sanitize();

		return properties;
	}
}
