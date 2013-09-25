package com.scalar.core.service;

import com.scalar.core.factory.AbstractFactory;
import com.scalar.core.request.Request;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Hashtable;
import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 24, 2013
 * Time: 5:44:32 PM
 */
public class ServiceFactory extends AbstractFactory {
    protected static final Log logger = LogFactory.getLog(ServiceFactory.class);

    /**
     * A <code>Map</code> of <code>Map</code> objects which contain <code>Logic</code> objects stored by prefix.
     * This map uses {@link Request} objects as the key.
     */
    private static Map<Request, Map<String, Object>> cacheMap = new Hashtable<Request, Map<String, Object>>();

    public ServiceFactory() {

    }

    /**
     * Returns an object that implements <code>Logic</code> for the given key and <code>Request</code>. The
     * <code>Logic</code> objects are cached by the key and the <code>Request</code>. If one does not exist in the
     * cache, it will be created.
     *
     * @param type    the logic class type
     * @param request the <code>Request</code> being handled
     * @return an object that implements <code>Logic</code> if the key is valid; otherwise <code>null</code>
     */
    public static <T> T getService(Class<T> type, Request request) {
        String name = type.getName();

        if (logger.isDebugEnabled()) {
            logger.debug("Finding service for key: " + name);
        }

        // Get request cache
        Map<String, Object> servicesMap = cacheMap.get(request);
        if (null == servicesMap) {
            servicesMap = new Hashtable<String, Object>();
            cacheMap.put(request, servicesMap);
        }

        // Get service from cache
        Object object = servicesMap.get(name);
        if (null != object) {
            if (logger.isDebugEnabled()) {
                logger.debug("Using service from cache for request: " + request);
            }

            return type.cast(object);
        }

        // Get the service, this should be prototype scope
        T bean = getFactory().getBean(name, type);
        if (null == bean) {
            if (logger.isDebugEnabled()) {
                logger.debug("Unable to find service for interface: " + name);
            }

            return null;
        }

        // Set request if we can
        if (Service.class.isInstance(bean)) {
            Service.class.cast(bean).setRequest(request);
        }

        // Store in cache
        servicesMap.put(name, bean);

        return bean;
    }

    /**
	 * Flushes the logic cache for the <code>Request</code>.
	 *
	 * @param request the <code>Request</code> which is being handled.
	 */
	public static void flushCache(Request request) {
		if ( logger.isDebugEnabled() ) {
			logger.debug("Clearing service cache for request: " + request);
		}

		cacheMap.remove(request);
	}
}
