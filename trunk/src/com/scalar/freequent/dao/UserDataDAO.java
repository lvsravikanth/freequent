package com.scalar.freequent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;
import com.scalar.core.jdbc.AbstractDAO;
import com.scalar.core.ScalarException;
import com.scalar.freequent.auth.User;
import com.scalar.freequent.util.StringUtil;
import com.scalar.freequent.util.MessageDigestUtils;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    static final String SQL_SelectAllColumns =
		"select " +
		COL_USER_ID + ", " +
		COL_FIRST_NAME + ", " +
		COL_MIDDLE_NAME + ", " +
		COL_LAST_NAME + ", " +
		COL_PASSWORD +
		" from " + TABLE_NAME + " ";


    public boolean checkUserCredentials (String username, String password) {
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME
                            + " WHERE " + COL_USER_ID + " = ? "
                            + " AND " + COL_PASSWORD + " = ?";

        return getJdbcTemplate().queryForInt(query, username, password) != 0;
    }

    /**
     * Returns the User object for the given userId.
     *
     * @param userId
     *
     * @return the User object for the given userId.
     */
    public UserDataRow findByPrimaryKey(String userId) {
        String query = SQL_SelectAllColumns +
                        " WHERE " + COL_USER_ID + " = ? ";
        List<UserDataRow> users = getJdbcTemplate().query(query,
        new RowMapper<UserDataRow>() {
            public UserDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                UserDataRow row = new UserDataRow();
                row.setUserId(rs.getString(COL_USER_ID));
                row.setFirstName(rs.getString(COL_FIRST_NAME));
                row.setMiddleName(rs.getString(COL_MIDDLE_NAME));
                row.setLastName(rs.getString(COL_LAST_NAME));
                row.setLastName(rs.getString(COL_PASSWORD));
                row.clean();
                return row;
            }
        }, userId);

        return users.get(0);
    }

    public static User rowToData (UserDataRow row) {
        User user = new User();
        user.setUserId(row.getUserId());
        user.setFirstName(row.getFirstName());
        user.setMiddleName(row.getMiddleName());
        user.setLastName(row.getLastName());
        user.setPassword(row.getPassword());

        return user;
    }

    public static UserDataRow dataToRow (User user, boolean skipPwd, boolean encryptPwd) throws ScalarException {
        UserDataRow row = new UserDataRow();
        row.setUserId(user.getUserId());
        row.setFirstName(user.getFirstName());
        row.setMiddleName(user.getMiddleName());
        row.setLastName(user.getLastName());
        if (!skipPwd) {
            row.setPassword(encryptPwd ? StringUtil.encrypt(user.getPassword()) : user.getPassword());
        }

        return row;
    }

}
