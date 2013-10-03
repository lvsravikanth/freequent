package com.scalar.freequent.action.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.freequent.web.spring.controller.AbstractActionController;
import com.scalar.freequent.auth.Capability;
import com.scalar.core.request.Request;
import com.scalar.core.ScalarActionException;
import com.scalar.core.util.MsgObjectUtil;
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

	public void demotemplate (Request request, Object command, Map<String, Object> data) throws ScalarActionException {
		data.put (Response.TEMPLATE_ATTRIBUTE, "demo/demotemplate");
	}

    public void testexception (Request request, Object command, Map<String, Object> data) throws ScalarActionException {
        Exception rootEx = new Exception ("This is a root exception");
        throw ScalarActionException.create (MsgObjectUtil.getMsgObject("action message:"), rootEx);
    }

    public void testauthorization (Request request, Object command, Map<String, Object> data) throws ScalarActionException {
        // authorization will fail for this method
        data.put("message", "You are a admin user, hence you are seeing this message instead of authorization message");
    }

    @Override
    public Capability[] getRequiredCapabilities(Request request) {
        String method = request.getMethod();
        if ("testauthorization".equals(method)) {
            return new Capability[] {new Capability("dummy capability")};
        }
        return super.getRequiredCapabilities(request);
    }
}
