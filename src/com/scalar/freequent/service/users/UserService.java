package com.scalar.freequent.service.users;

import com.scalar.freequent.auth.User;
import com.scalar.core.ScalarServiceException;

import java.util.List;
import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Oct 10, 2013
 * Time: 8:41:16 PM
 */
public interface UserService {
    public List<User> getAllUsers() throws ScalarServiceException;

	public List<User> getUsers(Map<String, String> searchParams) throws ScalarServiceException;

	public User findById(String userId) throws ScalarServiceException;

	public boolean exists (String userId) throws ScalarServiceException;

	public boolean insertOrUpdate (User user, boolean insert, boolean updatedPwd) throws ScalarServiceException;

}
