package com.scalar.core.jdbc;

import com.scalar.core.request.Request;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 24, 2013
 * Time: 4:38:24 PM
 */
public class AbstractDAO extends JdbcDaoSupport implements DAO {
    protected static final Log logger = LogFactory.getLog(AbstractDAO.class);

    /**
     * The <code>Request</code> that is currently being handled. This is useful for utilizing the built-in cache.
     *
     * @see #setRequest(com.scalar.core.request.Request)
     * @see #getRequest()
     * @see com.scalar.core.request.Request#putCachedData(Object, Object)
     * @see com.scalar.core.request.Request#getCachedData(Object)
     */
    private Request request;

    /**
     * This is the default constructor for <code>AbstractService</code>.
     */
    public AbstractDAO() {
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}