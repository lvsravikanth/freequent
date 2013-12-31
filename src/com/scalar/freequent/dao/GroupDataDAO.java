package com.scalar.freequent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.jdbc.core.RowMapper;
import com.scalar.core.jdbc.AbstractDAO;
import com.scalar.core.util.GUID;
import com.scalar.core.ScalarException;
import com.scalar.freequent.common.GroupData;
import com.scalar.freequent.common.ObjectType;
import com.scalar.freequent.util.StringUtil;

import java.util.*;
import java.sql.Types;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 7, 2013
 * Time: 8:08:05 PM
 */
public class GroupDataDAO extends AbstractDAO {
	protected static final Log logger = LogFactory.getLog(GroupDataDAO.class);
	public static final String TABLE_NAME = "ftGroup";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_DESCRIPTION = "description";

	static final String SQL_SelectAllColumns =
		"select " +
		COL_ID + ", " +
		COL_NAME + ", " +
		COL_DESCRIPTION +
		" from " + TABLE_NAME + " ";

	public static final Map<String, Integer> COL_SQL_TYPES = new HashMap<String, Integer>();

	static {
		COL_SQL_TYPES.put (COL_ID, Types.VARCHAR);
		COL_SQL_TYPES.put (COL_NAME, Types.VARCHAR);
		COL_SQL_TYPES.put (COL_DESCRIPTION, Types.VARCHAR);
	}

	public boolean existsByName (String groupName) {
		String query = "SELECT COUNT(*) FROM " + TABLE_NAME
                            + " WHERE " + COL_NAME + " = ? ";
		return getJdbcTemplate().queryForInt(query, groupName) != 0;
	}

	/**
     * Returns the User object for the given userId.
     *
     * @param id
     *
     * @return the User object for the given userId.
     */
    public GroupDataRow findByPrimaryKey(String id) {
        String query = SQL_SelectAllColumns +
                        " WHERE " + COL_ID + " = ? ";
        List<GroupDataRow> groups = getJdbcTemplate().query(query,
        new RowMapper<GroupDataRow>() {
            public GroupDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                GroupDataRow row = new GroupDataRow();
                row.setId(new GUID(rs.getString(COL_ID)));
                row.setName(rs.getString(COL_NAME));
                row.setDescription(rs.getString(COL_DESCRIPTION));
				row.clean();
                return row;
            }
        }, id);

        return groups.get(0);
    }

    /**
     * Returns the User object for the given userId.
     *
     * @param searchParams
     *
     * @return the User object for the given userId.
     */
    public List<GroupDataRow> getGroups(Map<String, Object> searchParams) {
        String query = SQL_SelectAllColumns;
        List<Object> args = new ArrayList<Object>();
        String groupName = (String)searchParams.get(GroupData.PARAM_NAME);
        if (!StringUtil.isEmpty(groupName)) {
            query +="WHERE " + COL_NAME + " like ? ";
            args.add(groupName + "%");
        }
        return getJdbcTemplate().query(query, args.toArray(new Object[args.size()]),
            new RowMapper<GroupDataRow>() {
                public GroupDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                    GroupDataRow row = new GroupDataRow();
                    row.setName(rs.getString(COL_NAME));
                    row.setDescription(rs.getString(COL_DESCRIPTION));
                    row.setId(new GUID(rs.getString(COL_ID)));
                    row.clean();
                    return row;
                }
        });
    }

	/**
     * Returns the Group object for the given groupId.
     *
     * @param groupId
     *
     * @return the Group object for the given groupId.
     */
    public GroupDataRow findById(String groupId) {
        String query = SQL_SelectAllColumns +
                        " WHERE " + COL_ID + " = ? ";
        List<GroupDataRow> groups = getJdbcTemplate().query(query,
        new RowMapper<GroupDataRow>() {
            public GroupDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                GroupDataRow row = new GroupDataRow();
                row.setId(new GUID(rs.getString(COL_ID)));
                row.setName(rs.getString(COL_NAME));
                row.setDescription(rs.getString(COL_DESCRIPTION));
				row.clean();
                return row;
            }
        }, groupId);

        return groups.get(0);
    }
    
    /**
     * Returns the Group object for the given groupName.
     *
     * @param groupName
     *
     * @return the Group object for the given groupName.
     */
    public GroupDataRow findByName(String groupName) {
        String query = SQL_SelectAllColumns +
                        " WHERE " + COL_NAME + " = ? ";
        List<GroupDataRow> groups = getJdbcTemplate().query(query,
        new RowMapper<GroupDataRow>() {
            public GroupDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                GroupDataRow row = new GroupDataRow();
                row.setId(new GUID(rs.getString(COL_ID)));
                row.setName(rs.getString(COL_NAME));
                row.setDescription(rs.getString(COL_DESCRIPTION));
				row.clean();
                return row;
            }
        }, groupName);

        return groups.get(0);
    }

	/**
     * Returns all the users in the system.
     *
     * @return the User object for the given userId.
     */
    public List<GroupDataRow> findAll() {
        String query = SQL_SelectAllColumns;

        return getJdbcTemplate().query(query,
        new RowMapper<GroupDataRow>() {
            public GroupDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                GroupDataRow row = new GroupDataRow();
                row.setId(new GUID(rs.getString(COL_ID)));
                row.setName(rs.getString(COL_NAME));
                row.setDescription(rs.getString(COL_DESCRIPTION));
				row.clean();
                return row;
            }
        });
    }

	public int insert (GroupDataRow row) throws ScalarException {
		insertRecord(row.getId(), ObjectType.GROUP);
        StringBuilder query = new StringBuilder();
		String sep = "";
        query.append ("insert into " + TABLE_NAME + " (");
        query.append(sep).append (COL_ID); sep = ",";
        query.append(sep).append(COL_NAME);
        query.append(sep).append(COL_DESCRIPTION);
		query.append(") values (?, ?, ?)");

		return getJdbcTemplate().update(query.toString(),
                row.getId().toString(),
                row.getName(),
                row.getDescription()
		);
    }

    public int update (GroupDataRow row) throws ScalarException {
		updateRecord(row.getId());
        StringBuilder query = new StringBuilder();
		String sep = "";
        query.append ("update " + TABLE_NAME + " set ");
        if( row.modName() ){ query.append(sep).append(COL_NAME).append(" = ?"); sep = ","; }
        if( row.modDescription() ){ query.append(sep).append(COL_DESCRIPTION).append(" = ?"); sep = ","; }
		query.append(" where ").append(COL_ID).append(" = ?");

		List<Object> args = new ArrayList<Object>();
		List<Integer> argTypes = new ArrayList<Integer>();
        if (row.modName()) { args.add (row.getName()); argTypes.add(COL_SQL_TYPES.get(COL_NAME)); }
        if (row.modDescription()) { args.add (row.getDescription()); argTypes.add(COL_SQL_TYPES.get(COL_DESCRIPTION)); }
		args.add (row.getId()); argTypes.add(COL_SQL_TYPES.get(COL_ID));

        return getJdbcTemplate().update(query.toString(), args.toArray(new Object[args.size()]), ArrayUtils.toPrimitive(argTypes.toArray(new Integer[argTypes.size()])));
    }

	public int removeByName(String name) {
		String query = "delete from " + TABLE_NAME + " where " + COL_NAME + " = ?";

		return getJdbcTemplate().update(query, name);
	}

	public int removeById(String id) {
		String query = "delete from " + TABLE_NAME + " where " + COL_ID + " = ?";

		return getJdbcTemplate().update(query, id);
	}
	
    public static GroupData rowToData (GroupDataRow row) {
		GroupData groupData = new GroupData();
        groupData.setId(row.getId().toString());
        groupData.setName(row.getName());
        groupData.setDescription(row.getDescription());

		return groupData;
    }

    public static GroupDataRow dataToRow (GroupData groupData) {
		GroupDataRow row = new GroupDataRow();
        row.setId(new GUID(groupData.getId()));
        row.setName(groupData.getName());
        row.setDescription(groupData.getDescription());

        return row;
    }
}
