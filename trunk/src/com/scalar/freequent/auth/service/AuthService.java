package com.scalar.freequent.auth.service;

import com.scalar.core.ScalarServiceException;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 24, 2013
 * Time: 10:53:16 AM
 */
public interface AuthService {
    public void dbTransactionTest() throws Exception;
    public void noDbTransactionTest() throws Exception;

    public boolean checkCredentials (String username, String password) throws ScalarServiceException;
}
