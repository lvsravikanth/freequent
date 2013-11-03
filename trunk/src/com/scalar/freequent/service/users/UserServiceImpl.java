package com.scalar.freequent.service.users;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.core.service.AbstractService;
import com.scalar.core.ScalarServiceException;
import com.scalar.core.ScalarException;
import com.scalar.core.jdbc.DAOFactory;
import com.scalar.freequent.auth.User;
import com.scalar.freequent.auth.UserCapability;
import com.scalar.freequent.dao.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Oct 10, 2013
 * Time: 8:43:03 PM
 */
public class UserServiceImpl extends AbstractService implements UserService {
    protected static final Log logger = LogFactory.getLog(UserServiceImpl.class);

    public List<User> getAllUsers() throws ScalarServiceException {
        UserDataDAO userDataDAO = DAOFactory.getDAO(UserDataDAO.class, getRequest());
        List<UserDataRow> userRows = userDataDAO.getAllUsers();
        List<User> users = new ArrayList<User>(userRows.size());
        for (UserDataRow row: userRows) {
            users.add (UserDataDAO.rowToData(row));
        }

        return users;
    }

	/**
	 * Method to perform user defined search.
	 *
	 * @return the user object for the given search parameters.
	 */
	public List<User> getUsers(Map<String, String> searchParam) throws ScalarServiceException {
		UserDataDAO userDataDAO = DAOFactory.getDAO(UserDataDAO.class, getRequest());
		List<UserDataRow> userRows = userDataDAO.getUsers(searchParam);
		List<User> users = new ArrayList<User>(userRows.size());
		for (UserDataRow row: userRows) {
			users.add (UserDataDAO.rowToData(row));
		}

		return users;
    }

	public User findById(String userId) throws ScalarServiceException {
		UserDataDAO userDataDAO = DAOFactory.getDAO(UserDataDAO.class, getRequest());
		UserDataRow userDataRow = userDataDAO.findByPrimaryKey(userId);
		if (userDataRow == null) {
			return null;
		}

		User user = UserDataDAO.rowToData(userDataRow);

		// set capabilities
		UserCapabilityInfoDAO userCapabilityInfoDAO = DAOFactory.getDAO(UserCapabilityInfoDAO.class, getRequest());
		List<UserCapabilityInfoRow> rows = userCapabilityInfoDAO.findByUserId(userId);
		List<UserCapability> userCapabilities = new ArrayList<UserCapability>();
		for (UserCapabilityInfoRow row: rows) {
			userCapabilities.add (UserCapabilityInfoDAO.rowToData(row));
		}
		user.setUserCapabilities(userCapabilities);

		return user;
	}

	public boolean exists(String userId) throws ScalarServiceException {
		UserDataDAO userDataDAO = DAOFactory.getDAO(UserDataDAO.class, getRequest());
		return userDataDAO.exists(userId);
	}

	public boolean insertOrUpdate(User user, boolean insert, boolean updatedPwd) throws ScalarServiceException {
		UserDataDAO userDataDAO = DAOFactory.getDAO(UserDataDAO.class, getRequest());
		if (insert) {
			try {
				UserDataRow row = UserDataDAO.dataToRow(user, !updatedPwd, updatedPwd);
				row.setCreatedBy(User.getActiveUser().getUserId());
				userDataDAO.insert(row);
			} catch (ScalarException e) {
				throw ScalarServiceException.create(e.getMsgObject(), e);
			}
		} else {
			// update user
			try {
				UserDataRow row = UserDataDAO.dataToRow(user, !updatedPwd, updatedPwd);
				row.setModifiedBy(User.getActiveUser().getUserId());
				userDataDAO.update(row);
			} catch (ScalarException e) {
				throw ScalarServiceException.create(e.getMsgObject(), e);
			}
		}

		// update capabilities
		// delete existing capabilities
		UserCapabilityInfoDAO userCapabilityInfoDAO = DAOFactory.getDAO(UserCapabilityInfoDAO.class, getRequest());
		userCapabilityInfoDAO.deleteByUserId(user.getUserId());

		// insert capabilities
		List<UserCapability> userCapabilities = user.getUserCapabilities();
		List<UserCapabilityInfoRow> userCapabilityInfoRows = new ArrayList<UserCapabilityInfoRow>();
		for (UserCapability userCapability: userCapabilities) {
			userCapability.setUserId(user.getUserId());
			userCapabilityInfoRows.add (UserCapabilityInfoDAO.dataToRow(userCapability));
		}
		userCapabilityInfoDAO.insert(userCapabilityInfoRows.toArray(new UserCapabilityInfoRow[userCapabilityInfoRows.size()]));

		return true;
	}
}
