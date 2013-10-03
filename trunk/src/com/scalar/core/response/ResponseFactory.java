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

    public static Response createResponse(String responseType, Request request, Map<String, Object> data) {
        Response response = null;
        String responseFormat = request.getResponseDataFormat();
        if (Response.ERROR.equals(responseType)) {
            response = new ErrorResponse();
        } else if (Response.MESSAGE.equals(responseType)) {
            response = new MessageResponse();
        } else if (Response.JSON_FORMAT.equals(responseType)) {
			if (data.containsKey(Response.TEMPLATE_ATTRIBUTE)) {
				response = new JSONTemplateResponse();
			} else {
				response = new JSONResponse();
			}
		} else if (Response.XML_FORMAT.equals(responseType)) {
            response = new BasicResponse();

        } else if (Response.TEMPLATE_FORMAT.equals(responseType)) {
            response = new BasicResponse();

        }

        if (null == response) {
            if (logger.isDebugEnabled()) {
                logger.debug("Unable to find response for key: " + responseType);
            }
            return null;
        }

        response.setRequest(request);

        response.load(data);

        return response;
    }
}
