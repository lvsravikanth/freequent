package com.scalar.freequent.web.spring.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.view.JstlView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.io.Writer;
import java.io.IOException;

import com.scalar.core.response.Response;
import com.scalar.core.ScalarActionException;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.core.util.MsgObject;
import com.scalar.freequent.util.StringUtil;
import com.scalar.freequent.l10n.FrameworkResource;

/**
 * User: Sujan Kumar Suppala
 * Date: Sep 21, 2013
 * Time: 8:08:42 PM
 */
public class JsonView extends JstlView {
    protected static final Log logger = LogFactory.getLog(JsonView.class);

    @Override
    public boolean isUrlRequired() {
        return false;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        Response response = (Response)model.get (Response.RESPONSE_ATTRIBUTE);
        if (StringUtil.isEmpty(response.getTemplateName())) {
            // create json from the action data
        } else {
            // create json from the template output
            setUrl(response.getTemplateName());
            super.render(model, request, response);
        }
    }

    protected void startResponseOutput(Response response, Writer writer) throws ScalarActionException {
        
    }

    /**
	 * Finishes the output process for the <code>Response</code> by ending the wrapping with JSON.
	 */
	protected void finishResponseOutput(Response response, Writer writer) throws ScalarActionException {
		// No wrapping if there is no tag
		String tag = response.getTag();
		if ( (null == tag) || (tag.length() == 0) ) {
			return;
		}

		try {
			writer.write("\", \"end\": \"---end-marker---\"}");
		} catch ( IOException e ) {
            MsgObject msgObject = MsgObjectUtil.getMsgObject(FrameworkResource.BASE_NAME, FrameworkResource.WRITE_OUTPUT_FAILURE);
			throw ScalarActionException.create(msgObject, e);
		}
	}
}
