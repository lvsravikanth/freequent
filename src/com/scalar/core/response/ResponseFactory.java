package com.scalar.core.response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

import com.scalar.core.request.Request;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 31, 2013
 * Time: 7:48:46 PM
 */
public class ResponseFactory {
    protected static final Log logger = LogFactory.getLog(ResponseFactory.class);

    public static Response createResponse(String responseFormat, Request request, Map<String, Object> data) {
        Response response = null;
        if (Response.JSON_FORMAT.equals(responseFormat)) {
            response = new JSONResponse();

        } else if (Response.XML_FORMAT.equals(responseFormat)) {
            response = new BasicResponse();

        } else if (Response.TEMPLATE_FORMAT.equals(responseFormat)) {
            response = new BasicResponse();

        }

        if (null == response) {
            if (logger.isDebugEnabled()) {
                logger.debug("Unable to find response for key: " + responseFormat);
            }
            return null;
        }

        response.setRequest(request);

        response.load(data);

        return response;
    }
}
