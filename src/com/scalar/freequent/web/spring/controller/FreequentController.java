package com.scalar.freequent.web.spring.controller;

import com.scalar.core.request.Request;
import com.scalar.core.ScalarAuthException;
import com.scalar.freequent.auth.Capability;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 19, 2013
 * Time: 9:00:05 PM
 *
 */
public interface FreequentController {

    /**
     * Check whether the request is authenticated.
     *
     * @param request request to be authenticated.
     * @return true - if request is authenticated successfully otherwise false.
     * @see #getAuthenticationRequired(com.scalar.core.request.Request)
     * @see #getAuthorized(com.scalar.core.request.Request)
     */
    public boolean isAuthenticated(Request request);

    /**
     * Determine whether authentication is required for this request.at
     *
     * @param request <code>Request</code> to be determined for authenticated.
     * @return true - if authentication is requried for this request otherwise false.
     * @see #getAuthorized(com.scalar.core.request.Request)
     */
    public boolean getAuthenticationRequired(Request request);

    /**
     * Method to check for the capability to execute the given request. This method will be executed only if the request is
     * marked for the authenticate required.
     *
     * By default this method checks for the Capabilities returned by {@link FreequentController#getAuthorized(com.scalar.core.request.Request)} 
     *
     *
     * @param request Request to be check for isAuthorized.
     *
     * @throws ScalarAuthException if the request is not authorized.
     *
     * @see #getAuthenticationRequired(com.scalar.core.request.Request)
     * @see #getRequiredCapabilities(com.scalar.core.request.Request)
     *
     */
    public void getAuthorized(Request request) throws ScalarAuthException;

    /**
     * The implementors should override this method to check for the capabilities for the respective Action.
     *
     * This method will be called by the {@link FreequentController#getAuthorized(com.scalar.core.request.Request)}
     *
     * @param request Request to be check for isAuthorized.
     *
     * @return array of <code>Capability</code> object which are requried for this request to process.
     */
    public Capability[] getRequiredCapabilities (Request request);
}
