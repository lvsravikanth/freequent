package com.scalar.core.jdbc;

import com.scalar.core.request.Request;

/**
 * The <code>DAO</code> interface represents an entity which is used for executing business logic.
 * <p/>
 * A <code>Logic</code> should never store any state information since it must be invocable from a variety of
 * transient sources.
 * <p/>
 * User: Sujan Kumar Suppala
 * Date: Aug 24, 2013
 * Time: 4:21:09 PM
 */
public interface DAO {
    /**
     * Sets the active <code>Request</code>.
     *
     * @param request the <code>Request</code>
     * @see #getRequest()
     */
    public void setRequest(Request request);

    /**
     * Returns the active <code>Request</code>.
     *
     * @return the <code>Request</code>
     * @see #setRequest(com.scalar.core.request.Request)
     */
    public Request getRequest();
}