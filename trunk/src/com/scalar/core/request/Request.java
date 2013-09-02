package com.scalar.core.request;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Enumeration;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 24, 2013
 * Time: 4:52:20 PM
 */
public interface Request extends Serializable {
    /**
     * Constant used to load the method parameter from the <code>Properties</code>.
     */
    public static final String METHOD = "method";

    /**
     * Constant used to load the request id parameter from the <code>Properties</code>.
     */
    public static final String ID = "id";

    /**
     * Constant used to load the action key parameter from the <code>Properties</code>.
     */
    public static final String ACTION_KEY = "action.key";

    /**
     * Constant used to load the response data format key parameter from the <code>Properties</code>.
     */
    public static final String RESPONSE_DATA_FORMAT = "response.data.format";

    /**
     * Constant used to identify the <code>Request</code> attribute.
     */
    public static final String REQUEST_ATTRIBUTE = "com.scalar.freequent.core.request.Request";

    /**
     * Constant used to identify the request content attribute.
     */
    public static final String REQUEST_CONTENT = "request.content";

    /**
     * Sets the <code>Service</code> that generated this <code>Request</code>.
     *
     * @param service the <code>Service</code>
     * @see #getService()
     */
    public void setWrappedObject(Object wrappedObject);

    /**
     * Returns the <code>Service</code> that generated this <code>Request</code>.
     *
     * @return the <code>Service</code>
     * @see #setService(Service)
     */
    public Object getWrappedObject();

    /**
     * Sets the id.
     *
     * @param id the id
     * @see #getId()
     */
    public void setId(String id);

    /**
     * Returns the id that uniquely identifies this <code>Request</code>.
     *
     * @return the unique <code>Request</code> id
     * @see #setId(String)
     */
    public String getId();

    /**
     * Sets the action key.
     *
     * @param key the action key
     * @see #getActionKey()
     */
    public void setActionKey(String key);

    /**
     * Returns the key that identifies the <code>Action</code> objects to invoke.
     *
     * @return the <code>Action</code> invocation key
     * @see #setActionKey(String)
     */
    public String getActionKey();

    /**
     * Returns the <code>Locale</code> for this <code>Request</code>.
     *
     * @return the <code>Locale</code>
     */
    public Locale getLocale();

    /**
     * Returns the <code>TimeZone</code> for this <code>Request</code>.
     *
     * @return the <code>TimeZone</code>
     */
    public TimeZone getTimeZone();


    /**
     * Sets this <code>Request</code> object's cache.
     *
     * @param cache the cache to use
     * @see #getCache()
     * @see #putCachedData(Object, Object)
     * @see #getCachedData(Object)
     * @see #removeCachedData(Object)
     */
    public void setCache(Map<Object, Object> cache);

    /**
     * Returns this <code>Request</code> object's cache.
     *
     * @return the <code>Map</code> used for the cache
     * @see #setCache(Map)
     * @see #putCachedData(Object, Object)
     * @see #getCachedData(Object)
     * @see #removeCachedData(Object)
     */
    public Map<Object, Object> getCache();

    /**
     * Stores data in the cache using the given key.
     *
     * @param key  the cache key
     * @param data the data to store
     * @see #setCache(Map)
     * @see #getCache()
     * @see #getCachedData(Object)
     * @see #removeCachedData(Object)
     */
    public void putCachedData(Object key, Object data);

    /**
     * Returns data from the cache stored using the given key.
     *
     * @param key the cache key
     * @return the data in the cache
     * @see #setCache(Map)
     * @see #getCache()
     * @see #putCachedData(Object, Object)
     * @see #removeCachedData(Object)
     */
    public Object getCachedData(Object key);

    /**
     * Returns data from the cache stored using the given key in the given type. If the object is not of the type
     * specified, <code>null</code> is returned.
     *
     * @param <T>       the type
     * @param key       the cache key
     * @param typeClass the type class
     * @return the data in the cache in the given type if it matches; otherwise <code>null</code>
     */
    public <T> T getCachedData(Object key, Class<T> typeClass);

    /**
     * Removes data from the cache stored using the given key.
     *
     * @param key the cache key
     * @return the removed data
     * @see #setCache(Map)
     * @see #getCache()
     * @see #putCachedData(Object, Object)
     * @see #getCachedData(Object)
     */
    public Object removeCachedData(Object key);


    /**
     * Returns the time when the <code>Request</code> was created in millis (i.e. <code>System.currentTimeMillis()</code>).
     *
     * @return the time when the <code>Request</code> was created in millis
     */
    public long getCreateTime();

    /**
     * Sets the method.
     *
     * @param method the method
     * @see #getMethod()
     */
    public void setMethod(String method);

    /**
     * Returns the method for the <code>Action</code> objects.
     *
     * @return the method for the <code>Action<code> objects
     * @see #setMethod(String)
     */
    public String getMethod();

    /**
	 * Returns a <code>Map</code> of parameter names and values.
	 *
	 * @return a <code>Map</code> of parameter names and values.
	 */
    public Map<String, String[]> getParameterMap();

    /**
	 * Returns an <code>Enumeration</code> of parameter names.
	 *
	 * @return a <code>Enumeration</code> of parameter names.
	 */
	public Enumeration<String> getParameterNames();

    public String getParameter(String name);

    public String[] getParameterValues(String name);

    public void setAttribute(String name, Object value);

    public Object getAttribute(String name);

    public void removeAttribute(String name);

    public Enumeration<String> getAttributeNames();

    public String getResponseDataFormat();
}
