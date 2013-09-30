package com.scalar.core.response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.freequent.util.DebugUtil;
import com.scalar.core.request.Request;
import com.scalar.core.service.ServiceFactory;
import com.scalar.core.ScalarActionException;

import java.util.Map;
import java.util.Collections;
import java.io.Writer;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 24, 2013
 * Time: 5:02:47 PM
 */
public class BasicResponse extends AbstractResponse implements Response {
    protected static final Log logger = LogFactory.getLog(BasicResponse.class);

    public BasicResponse() {
        //Default constuctor
    }

    public void createOutput(Writer writer) throws ScalarActionException {
        
    }
}
