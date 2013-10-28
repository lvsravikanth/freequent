package com.scalar.core.request;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

import com.scalar.freequent.util.CollectionsUtil;
import com.scalar.freequent.util.StringUtil;
import com.scalar.freequent.auth.User;
import com.scalar.freequent.web.session.SessionParameters;
import com.scalar.core.response.Response;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 24, 2013
 * Time: 4:56:53 PM
 */
public class BasicRequest extends AbstractRequest implements Request {
    protected static final Log logger = LogFactory.getLog(BasicRequest.class);
    /**
     * Serialization UID.
     */
    private static final long serialVersionUID = 5443323282436177418L;

    /**
     * The id for this <code>Request</code>.
     */
    protected String id = null;

    /**
     * The <code>Action</code> key.
     */
    protected String actionKey = null;

    /**
     * The method.
     */
    protected String method = null;

    /**
     * The response data format.
     */
    protected String responseFormat = null;

    /**
     * The <code>Map</code> that stores user set attributes which override the <code>Attributes</code>.
     *
     * @see #setAttribute(String, String)
     * @see #getAttribute(String)
     * @see #getAttribute(String, String)
     * @see #getAttributes()
     */
    protected Map<String, String> userAttributes = new HashMap<String, String>();

    /**
     * The <code>Map</code> that provides storage for the cache. This is set externally by the framework.
     */
    protected transient Map<Object, Object> cache = null;

    /**
     * The create time.
     */
    protected long createTime = 0L;

    /**
     * Actual request object.
     */
    protected Object wrappedObject;

	/**
	 * The <code>Context</code> for this <code>Request</code>.
	 */
	protected Context context = new BasicContext();

	/**
	 * The properties for this <code>Request</code>.
	 */
	protected Properties properties = new Properties();

    /**
     * Constructs a new <code>BasicRequest</code> object. This is the default constructor.
     */
    public BasicRequest() {
        createTime = System.currentTimeMillis();
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setActionKey(String key) {
        this.actionKey = key;
    }

    public String getActionKey() {
        return actionKey;
    }

    /**
     * Sets the method.
     *
     * @param method the method
     * @see #getMethod()
     */
    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public void setCache(Map<Object, Object> cache) {
        this.cache = cache;
    }

    /**
     * Returns this <code>Request</code> object's cache.
     *
     * @return the <code>Map</code> used for the cache
     * @see #setCache(Map)
     * @see #putCachedData(Object, Object)
     * @see #getCachedData(Object)
     * @see #removeCachedData(Object)
     */
    public Map<Object, Object> getCache() {
        return cache;
    }

    public void putCachedData(Object key, Object data) {
        if (null != cache) {
            cache.put(key, data);
        }
    }

    public Object getCachedData(Object key) {
        return (null == cache) ? null : cache.get(key);
    }

    public <T> T getCachedData(Object key, Class<T> typeClass) {
        Object object = getCachedData(key);
        return typeClass.isInstance(object) ? typeClass.cast(object) : null;
    }

    public Object removeCachedData(Object key) {
        return (null == cache) ? null : cache.remove(key);
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setWrappedObject(Object wrappedObject) {
        this.wrappedObject = wrappedObject;
    }

    public Object getWrappedObject() {
        return wrappedObject;
    }

    public Locale getLocale() {
        return ((HttpServletRequest)getWrappedObject()).getLocale();
    }

    public TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }

    /**
	 * Returns a <code>Map</code> of parameter names and values.
	 *
	 * @return a <code>Map</code> of parameter names and values.
	 */
	public Map<String, String[]> getParameterMap() {
		return CollectionsUtil.uncheckedConvert((Map<?, ?>)((HttpServletRequest)wrappedObject).getParameterMap());
	}

	/**
	 * Returns an <code>Enumeration</code> of parameter names.
	 *
	 * @return a <code>Enumeration</code> of parameter names.
	 */
	public Enumeration<String> getParameterNames() {
		return CollectionsUtil.uncheckedConvert((Enumeration<?>)((HttpServletRequest)wrappedObject).getParameterNames());
	}

	public String getParameter(String name) {
		return ((HttpServletRequest)wrappedObject).getParameter(name);
	}

    public String[] getParameterValues(String name) {
		return ((HttpServletRequest)wrappedObject).getParameterValues(name);
	}

	public void setAttribute(String name, Object value) {
		((HttpServletRequest)wrappedObject).setAttribute(name, value);
	}

	public Object getAttribute(String name) {
		return ((HttpServletRequest)wrappedObject).getAttribute(name);
	}

	public void removeAttribute(String name) {
		((HttpServletRequest)wrappedObject).removeAttribute(name);
	}

	/**
	 * Returns the attribute names.
	 *
	 * @return an <code>Enumeration</code> of the attribute names
	 */
	public Enumeration<String> getAttributeNames() {
		return CollectionsUtil.uncheckedConvert((Enumeration<?>)((HttpServletRequest)wrappedObject).getAttributeNames());
	}

    /**
	 * Sets the response data format.
	 *
	 * @param format the format
	 * @see #getResponseDataFormat()
	 */
	public void setResponseDataFormat(String format) {
		this.responseFormat = format;
	}
    
    public String getResponseDataFormat() {
        if ( (null == responseFormat) || ( responseFormat.length() == 0) ) {
			responseFormat = Response.DEFAULT_FORMAT;
		}

		return responseFormat;
    }

	/**
	 * Returns the <code>Request</code> output attributes.
	 * <p>
	 * The following attributes are returned:
	 * <dl compact>
	 * <dt>{@link #ID}
	 * <dd>The id for this <code>Request</code>
	 * </dl>
	 *
	 * @return the output attributes
	 */
	public Map<String, String> getOutputAttributes() {
		Map<String, String> attributes = new HashMap<String, String>();

		attributes.put(ID, getId());

		return attributes;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Properties getProperties() {
		return properties;
	}
}
