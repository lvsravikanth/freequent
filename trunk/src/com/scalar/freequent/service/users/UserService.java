package com.scalar.freequent.service.users;

import com.scalar.freequent.auth.User;
import com.scalar.core.ScalarServiceException;

import java.util.List;

/**
 * User: Sujan Kumar Suppala
 * Date: Oct 10, 2013
 * Time: 8:41:16 PM
 */
public interface UserService {
    public List<User> getAllUsers() throws ScalarServiceException;

	public List<User> manageUserSearch() throws ScalarServiceException;
}
