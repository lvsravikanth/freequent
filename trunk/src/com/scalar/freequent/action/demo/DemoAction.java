package com.scalar.freequent.action.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.freequent.web.spring.controller.AbstractActionController;
import com.scalar.core.request.Request;
import com.scalar.core.ScalarActionException;
import com.scalar.core.response.Response;

import java.util.Map;


/**
 * User: Sujan Kumar Suppala
 * Date: Sep 21, 2013
 * Time: 7:18:37 PM
 */
public class DemoAction  extends AbstractActionController {
    protected static final Log logger = LogFactory.getLog(DemoAction.class);

    public void defaultProcess(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
        data.put (Response.TEMPLATE_ATTRIBUTE, "demo/demo");
    }

    public void simpleJson (Request request, Object command, Map<String, Object> data) throws ScalarActionException {
        data.put ("jsonKey1", "jsonValue1");
        data.put ("jsonKey2", "jsonValue2");
    }
}
