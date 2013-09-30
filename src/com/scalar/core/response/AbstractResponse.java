package com.scalar.core.response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.Collections;

import com.scalar.core.request.Request;
import com.scalar.core.service.ServiceFactory;
import com.scalar.freequent.util.DebugUtil;

/**
 * User: ssuppala
 * Date: Sep 30, 2013
 * Time: 1:48:53 PM
 */
public abstract class AbstractResponse implements Response {

	 protected static final Log logger = LogFactory.getLog(AbstractResponse.class);

	/**
	 * Constant used as the default tag name for this <code>Response</code>.
	 *
	 * @see #getTag()
	 */
	protected static final String FUI_RESPONSE = "fuiResponse";

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
		return FUI_RESPONSE;
	}

    public void cleanup() {
		// Make sure to flush request cache
		if ( null != request ) {
			ServiceFactory.flushCache(request);
		}
	}

	/**
	 * Adds request attributes to the <code>Response</code> output.
	 *
	 * @return the output attributes
	 */
	public Map<String, String> getOutputAttributes() {
		if ( null != request ) {
			return request.getOutputAttributes();
		} else {
			return Collections.emptyMap();
		}
	}

	/**
	 * Returns the value for the key in the <code>Action</code> data.
	 *
	 * @param key the key for the value
	 * @return the value from the <code>Action</code> data if it exists; otherwise <code>null</code>
	 */
	protected String getValue(String key) {
		String value = null;

		if ( null != data ) {
			Object obj = data.get(key);
			if ( null != obj ) {
				value = obj.toString();
			}
		}

		// fallback on request attributes
		if ( null == value ) {
			Object obj = request.getAttribute(key);
			if ( null != obj ) {
				value = obj.toString();
			}
		}

		return value;
	}
}
