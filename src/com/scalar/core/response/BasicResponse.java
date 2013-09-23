package com.scalar.core.response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.freequent.util.DebugUtil;
import com.scalar.core.request.Request;

import java.util.Map;
import java.util.Collections;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 24, 2013
 * Time: 5:02:47 PM
 */
public class BasicResponse implements Response {
    protected static final Log logger = LogFactory.getLog(BasicResponse.class);

    /**
	 * Constant used as the default tag name for this <code>Response</code>.
	 *
	 * @see #getTag()
	 */
	protected static final String FREEQUENT_RESPONSE = "freequentResponse";

    private Object wrappedObject;
    /**
	 * The <code>Request</code> which generated this <code>Response</code>.
	 *
	 * @see #getRequest()
	 */
	protected Request request = null;

	/**
	 * The data created by the <code>Action</code> for this <code>Response</code>.
	 *
	 * @see #load(java.util.Map)
	 */
	protected Map<String, Object> data = null;


    public BasicResponse() {
        //Default constuctor
    }

    public void setWrappedObject(Object wrappedObject) {
        this.wrappedObject = wrappedObject;
    }

    public Object getWrappedObject() {
        return wrappedObject;
    }

    public void setRequest(Request request) {
		this.request = request;
	}

	public Request getRequest() {
		return request;
	}

    /**
	 * Loads the <code>Action</code> data for this <code>Response</code>.
	 */
	public void load(Map<String, Object> loadData) {
		if ( logger.isDebugEnabled() ) {
			DebugUtil.logMap(logger, loadData, "Loading response data");
		}

		// no null data!
		if ( null == loadData ) {
			return;
		}

		this.data = loadData;
	}

    /**
	 * Returns the data created for this <code>Response</code> by the <code>Action</code>.
	 *
	 * @return a <code>Map</code> containing the data created by the <code>Action</code>
	 */
	public Map<String, ? extends Object> getActionData() {
		if ( null == data ) {
			return Collections.emptyMap();
		}

		return data;
	}

    public String getViewName() {
        return (String)data.get(Response.TEMPLATE_ATTRIBUTE);
    }

    public String getTemplateName() {
        return (String)data.get(Response.TEMPLATE_ATTRIBUTE);
    }

    public String getTag() {
		return FREEQUENT_RESPONSE;
	}

}
