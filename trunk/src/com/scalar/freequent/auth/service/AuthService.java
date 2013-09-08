package com.scalar.freequent.auth.service;

import com.scalar.core.ScalarServiceException;
import com.scalar.core.ScalarException;
import com.scalar.core.request.Request;
import com.scalar.freequent.auth.User;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 24, 2013
 * Time: 10:53:16 AM
 */
public interface AuthService {
    public void dbTransactionTest() throws Exception;
    public void noDbTransactionTest() throws Exception;

    public boolean checkCredentials (String username, String password) throws ScalarServiceException;

    public User getUser (String username) throws ScalarServiceException;

    /**
     * Creates the given user.
     *
     * @param user user object to be created.
     *
     * @throws ScalarServiceException if any errors.
     */
    public void createUser (User user) throws ScalarServiceException;

    /**
     * Creates the given user.
     *
     * @param user user object to be created.
     * @param skipPwd true if skip pwd updation, otherwise false.
     * @param encryptPwd true if pwd needs to to encrypted if it is updated, otherwise false.
     *
     * @throws ScalarServiceException if any errors.
     */
    public void updateUser (User user, boolean skipPwd, boolean encryptPwd) throws ScalarServiceException;

}
