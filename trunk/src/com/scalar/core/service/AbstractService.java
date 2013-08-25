package com.scalar.core.service;

import com.scalar.core.request.Request;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 24, 2013
 * Time: 4:38:24 PM
 */
public class AbstractService implements Service {
    protected static final Log logger = LogFactory.getLog(AbstractService.class);

    /**
     * The <code>Request</code> that is currently being handled. This is useful for utilizing the built-in cache.
     *
     * @see #setRequest(Request)
     * @see #getRequest()
     * @see Request#putCachedData(Object, Object)
     * @see Request#getCachedData(Object)
     */
    private Request request;

    /**
     * This is the default constructor for <code>AbstractService</code>.
     */
    public AbstractService() {
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
