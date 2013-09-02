package com.scalar.freequent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.core.jdbc.AbstractDAO;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 31, 2013
 * Time: 1:18:12 PM
 */
public class UserDataDAO extends AbstractDAO {
    protected static final Log logger = LogFactory.getLog(UserDataDAO.class);
    public static final String TABLE_NAME = "FTUSERS";
    public static final String COL_USER_ID = "USER_ID";
    public static final String COL_FIRST_NAME = "FIRST_NAME";
    public static final String COL_MIDDLE_NAME = "MIDDLE_NAME";
    public static final String COL_LAST_NAME = "LAST_NAME";
    public static final String COL_PASSWORD = "PASSWORD";


    public boolean checkUserCredentials (String username, String password) {
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME
                            + " WHERE " + COL_USER_ID + " = ? "
                            + " AND " + COL_PASSWORD + " = ?";

        return getJdbcTemplate().queryForInt(query, username, password) != 0;
    }

}
