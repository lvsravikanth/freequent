package com.scalar.freequent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.jdbc.core.RowMapper;
import com.scalar.core.jdbc.AbstractDAO;
import com.scalar.core.ScalarException;
import com.scalar.freequent.auth.User;
import com.scalar.freequent.util.StringUtil;
import com.scalar.freequent.common.ObjectType;

import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

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
	public static final String COL_DISABLED = "DISABLED";
	public static final String COL_EXPIRESON = "EXPIRESON";
	public static final String COL_CREATEDBY = "CREATEDBY";
	public static final String COL_CREATEDON = "CREATEDON";
	public static final String COL_MODIFIEDBY = "MODIFIEDBY";
	public static final String COL_MODIFIEDON = "MODIFIEDON";


	static final String SQL_SelectAllColumns =
		"select " +
		COL_USER_ID + ", " +
		COL_FIRST_NAME + ", " +
		COL_MIDDLE_NAME + ", " +
		COL_LAST_NAME + ", " +
		COL_PASSWORD + ", " +
		COL_DISABLED + ", " +
		COL_EXPIRESON + ", " +
		COL_CREATEDBY + ", " +
		COL_CREATEDON + ", " +
		COL_MODIFIEDBY + ", " +
		COL_MODIFIEDON +
		" from " + TABLE_NAME + " ";

	public static final Map<String, Integer> COL_SQL_TYPES = new HashMap<String, Integer>();

	static {
		COL_SQL_TYPES.put (COL_USER_ID, Types.VARCHAR);
		COL_SQL_TYPES.put (COL_FIRST_NAME, Types.VARCHAR);
		COL_SQL_TYPES.put (COL_MIDDLE_NAME, Types.VARCHAR);
		COL_SQL_TYPES.put (COL_LAST_NAME, Types.VARCHAR);
		COL_SQL_TYPES.put (COL_PASSWORD, Types.VARCHAR);
		COL_SQL_TYPES.put (COL_DISABLED, Types.BIT);
		COL_SQL_TYPES.put (COL_EXPIRESON, Types.DATE);
		COL_SQL_TYPES.put (COL_CREATEDBY, Types.VARCHAR);
		COL_SQL_TYPES.put (COL_CREATEDON, Types.DATE);
		COL_SQL_TYPES.put (COL_MODIFIEDBY, Types.VARCHAR);
		COL_SQL_TYPES.put (COL_MODIFIEDON, Types.DATE);
	}

    public boolean checkUserCredentials (String username, String password) {
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME
                            + " WHERE " + COL_USER_ID + " = ? "
                            + " AND " + COL_PASSWORD + " = ?";

        return getJdbcTemplate().queryForInt(query, username, password) != 0;
    }

	public boolean exists (String userId) {
		String query = "SELECT COUNT(*) FROM " + TABLE_NAME
                            + " WHERE " + COL_USER_ID + " = ? ";
		return getJdbcTemplate().queryForInt(query, userId) != 0;
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
                row.setPassword(rs.getString(COL_PASSWORD));
				row.setDisabled(rs.getInt(COL_DISABLED));
				row.setExpiresOn(rs.getDate(COL_EXPIRESON));
				row.setCreatedBy(rs.getString(COL_CREATEDBY));
				row.setModifiedBy(rs.getString(COL_MODIFIEDBY));
				row.setCreatedOn(rs.getDate(COL_CREATEDON));
				row.setModifiedOn(rs.getDate(COL_MODIFIEDON));
				row.clean();
                return row;
            }
        }, userId);

        return users.get(0);
    }

	/**
     * Returns the User object for the given userId or first name or last name.
     *
     * @param searchParams search query parameters
     *
     * @return the User object for the given userId.
     */
	public List<UserDataRow> getUsers(Map<String, String> searchParams) {
		String query = SQL_SelectAllColumns;
		String userId = searchParams.get(User.USER_ID);
		String fName = searchParams.get(User.FIRST_NAME);
		String lName = searchParams.get(User.LAST_NAME);
		List<String> list = new ArrayList<String>();
		if ( !StringUtil.isEmpty(userId) || !StringUtil.isEmpty(fName) || !StringUtil.isEmpty(lName)) {
			query += "WHERE ";
		}
		if (!StringUtil.isEmpty(userId)) {
			query += COL_USER_ID + " like ? ";
			list.add(userId + "%");
		}
		if (!StringUtil.isEmpty(fName)) {
			query += COL_FIRST_NAME + " = ? ";
			list.add(fName + "%");
		}
		if (!StringUtil.isEmpty(lName)) {
			query += COL_LAST_NAME + " = ? ";
			list.add(lName + "%");
		}

		return getJdbcTemplate().query(query, list.toArray(),
		new RowMapper<UserDataRow>() {
			public UserDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserDataRow row = new UserDataRow();
				row.setUserId(rs.getString(COL_USER_ID));
				row.setFirstName(rs.getString(COL_FIRST_NAME));
				row.setMiddleName(rs.getString(COL_MIDDLE_NAME));
				row.setLastName(rs.getString(COL_LAST_NAME));
				row.setPassword(rs.getString(COL_PASSWORD));
				row.setDisabled(rs.getInt(COL_DISABLED));
				row.setExpiresOn(rs.getDate(COL_EXPIRESON));
				row.setCreatedBy(rs.getString(COL_CREATEDBY));
				row.setModifiedBy(rs.getString(COL_MODIFIEDBY));
				row.setCreatedOn(rs.getDate(COL_CREATEDON));
				row.setModifiedOn(rs.getDate(COL_MODIFIEDON));
				row.clean();
				return row;
			}
		});
	}

	/**
     * Returns all the users in the system.
     *
     * @return the User object for the given userId.
     */
    public List<UserDataRow> getAllUsers() {
        String query = SQL_SelectAllColumns;

        return getJdbcTemplate().query(query,
        new RowMapper<UserDataRow>() {
            public UserDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                UserDataRow row = new UserDataRow();
                row.setUserId(rs.getString(COL_USER_ID));
                row.setFirstName(rs.getString(COL_FIRST_NAME));
                row.setMiddleName(rs.getString(COL_MIDDLE_NAME));
                row.setLastName(rs.getString(COL_LAST_NAME));
                row.setPassword(rs.getString(COL_PASSWORD));
				row.setDisabled(rs.getInt(COL_DISABLED));
				row.setExpiresOn(rs.getDate(COL_EXPIRESON));
				row.setCreatedBy(rs.getString(COL_CREATEDBY));
				row.setModifiedBy(rs.getString(COL_MODIFIEDBY));
				row.setCreatedOn(rs.getDate(COL_CREATEDON));
				row.setModifiedOn(rs.getDate(COL_MODIFIEDON));
				row.clean();
                return row;
            }
        });
    }

    public int insert (UserDataRow row) throws ScalarException {
		insertRecord(row.getUserId(), ObjectType.USER);
        StringBuilder query = new StringBuilder();
		Calendar now = Calendar.getInstance();
		String sep = "";
        query.append ("insert into " + TABLE_NAME + " (");
        query.append(sep).append (COL_USER_ID); sep = ",";
        query.append(sep).append(COL_PASSWORD);
        query.append(sep).append(COL_FIRST_NAME);
        query.append(sep).append(COL_MIDDLE_NAME);
        query.append(sep).append(COL_LAST_NAME);
		query.append(sep).append(COL_DISABLED);
		query.append(sep).append(COL_EXPIRESON);
		query.append(sep).append(COL_CREATEDBY);
		query.append(sep).append(COL_CREATEDON);
		query.append(sep).append(COL_MODIFIEDBY);
		query.append(sep).append(COL_MODIFIEDON);
		query.append(") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		return getJdbcTemplate().update(query.toString(),
                row.getUserId(),
                row.getPassword(),
                row.getFirstName(),
                row.getMiddleName(),
                row.getLastName(),
				row.getDisabled(),
				row.getExpiresOn(),
				row.getCreatedBy(),
				now.getTime(),
				row.getCreatedBy(),
				now.getTime()
		);
    }

    public int update (UserDataRow row) throws ScalarException {
		updateRecord(row.getUserId());
        StringBuilder query = new StringBuilder();
		Calendar now = Calendar.getInstance();
		String sep = "";
        query.append ("update " + TABLE_NAME + " set ");
        if( row.modPassword() ){ query.append(sep).append(COL_PASSWORD).append(" = ?"); sep = ","; }
        if( row.modFirstName() ){ query.append(sep).append(COL_FIRST_NAME).append(" = ?"); sep = ","; }
        if( row.modMiddleName() ){ query.append(sep).append(COL_MIDDLE_NAME).append(" = ?"); sep = ","; }
        if( row.modLastName() ){ query.append(sep).append(COL_LAST_NAME).append(" = ?"); sep = ","; }
		if( row.modDisabled() ){ query.append(sep).append(COL_DISABLED).append(" = ?"); sep = ","; }
		if( row.modExpiresOn() ){ query.append(sep).append(COL_EXPIRESON).append(" = ?"); sep = ","; }
		if( row.modModifiedBy() ){ query.append(sep).append(COL_MODIFIEDBY).append(" = ?"); sep = ","; }
		query.append(sep).append(COL_MODIFIEDON).append(" = ?"); sep = ",";
		query.append(" where ").append(COL_USER_ID).append(" = ?");

		List<Object> args = new ArrayList<Object>();
		List<Integer> argTypes = new ArrayList<Integer>();
        if (row.modPassword()) { args.add (row.getPassword()); argTypes.add(COL_SQL_TYPES.get(COL_PASSWORD)); }
        if (row.modFirstName()) { args.add (row.getFirstName()); argTypes.add(COL_SQL_TYPES.get(COL_FIRST_NAME)); }
        if (row.modMiddleName()) { args.add (row.getMiddleName()); argTypes.add(COL_SQL_TYPES.get(COL_MIDDLE_NAME)); }
        if (row.modLastName()) { args.add (row.getLastName()); argTypes.add(COL_SQL_TYPES.get(COL_LAST_NAME)); }
        if (row.modDisabled()) { args.add (row.getDisabled()); argTypes.add(COL_SQL_TYPES.get(COL_DISABLED)); }
        if (row.modExpiresOn()) { args.add (row.getExpiresOn()); argTypes.add(COL_SQL_TYPES.get(COL_EXPIRESON)); }
        if (row.modModifiedBy()) { args.add (row.getModifiedBy()); argTypes.add(COL_SQL_TYPES.get(COL_MODIFIEDBY)); }
		args.add (now.getTime()); argTypes.add(COL_SQL_TYPES.get(COL_MODIFIEDON));
		args.add (row.getUserId()); argTypes.add(COL_SQL_TYPES.get(COL_USER_ID));

        return getJdbcTemplate().update(query.toString(), args.toArray(new Object[args.size()]), ArrayUtils.toPrimitive(argTypes.toArray(new Integer[argTypes.size()])));
    }

    public static User rowToData (UserDataRow row) {
		User user = new User();
        user.setUserId(row.getUserId());
        user.setFirstName(row.getFirstName());
        user.setMiddleName(row.getMiddleName());
        user.setLastName(row.getLastName());
        user.setPassword(row.getPassword());
		user.setDisabled(row.getDisabled() == 0);
		user.setExpiresOn(row.getExpiresOn());
		user.setCreatedBy(row.getCreatedBy());
		user.setModifiedBy(row.getModifiedBy());
		user.setCreatedOn(row.getCreatedOn());
		user.setModifiedOn(row.getModifiedOn());

		return user;
    }

    public static UserDataRow dataToRow (User user, boolean skipPwd, boolean encryptPwd) throws ScalarException {
		UserDataRow row = new UserDataRow();
        row.setUserId(user.getUserId());
        row.setFirstName(user.getFirstName());
        row.setMiddleName(user.getMiddleName());
        row.setLastName(user.getLastName());
		row.setDisabled(user.isDisabled() ? 1 : 0);
		row.setExpiresOn(user.getExpiresOn());
		row.setCreatedBy(user.getCreatedBy());
		row.setModifiedBy(user.getModifiedBy());
		row.setCreatedOn(user.getCreatedOn());
		row.setModifiedOn(user.getModifiedOn());

		if (!skipPwd) {
            row.setPassword(encryptPwd ? StringUtil.encrypt(user.getPassword()) : user.getPassword());
        }

        return row;
    }

}
