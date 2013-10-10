package com.scalar.freequent.service.users;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.core.service.AbstractService;
import com.scalar.core.ScalarServiceException;
import com.scalar.core.jdbc.DAOFactory;
import com.scalar.freequent.auth.User;
import com.scalar.freequent.dao.UserDataDAO;
import com.scalar.freequent.dao.UserDataRow;

import java.util.List;
import java.util.ArrayList;

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
}
