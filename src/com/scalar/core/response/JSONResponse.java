package com.scalar.core.response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.Writer;
import java.io.IOException;

import com.scalar.core.ScalarActionException;
import com.scalar.core.util.MsgObject;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.freequent.util.JSONUtil;
import com.scalar.freequent.l10n.FrameworkResource;

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

    /**
	 * Creates the JSON formatted data output for this <code>Response</code> using the <code>Writer</code>. The
	 * data for output is retrieved using {@link #getActionData()}.
	 */
	public void createOutput(Writer writer) throws ScalarActionException {
		if ( logger.isDebugEnabled() ) {
			logger.debug("Creating JSON output");
		}

		// Context context = (Context)service.getRequest().getAttribute(Context.CONTEXT_ATTRIBUTE);

		try {
			writer.write(JSONUtil.SCRIPT_SECURITY_PREFIX);
			JSONObject json = JSONUtil.buildJSONObjectFromMap(getActionData(),
			                                                  getRequest().getLocale(),
			                                                  getRequest().getTimeZone());
			json.write(writer);
			writer.write(JSONUtil.SCRIPT_SECURITY_SUFFIX);
		} catch ( IOException e ) {
            MsgObject msgObject = MsgObjectUtil.getMsgObject(FrameworkResource.BASE_NAME, FrameworkResource.WRITE_OUTPUT_FAILURE);
			throw ScalarActionException.create(msgObject, e);
		} catch ( JSONException e ) {
            MsgObject msgObject = MsgObjectUtil.getMsgObject(FrameworkResource.BASE_NAME, FrameworkResource.JSON_OUTPUT_FAILURE);
			throw ScalarActionException.create(msgObject, e);
		}
	}
}
