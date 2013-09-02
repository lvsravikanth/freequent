package com.scalar.core.request;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;

import com.scalar.freequent.web.spring.controller.AbstractControllerUtil;
import com.scalar.core.response.Response;

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
}
