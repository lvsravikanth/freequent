package com.scalar.core.response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 31, 2013
 * Time: 7:50:50 PM
 */
public class JSONResponse extends BasicResponse {
    protected static final Log logger = LogFactory.getLog(JSONResponse.class);
    public final String VIEW_NAME = "jsonView";

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }
}
