package com.scalar.freequent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.jdbc.core.RowMapper;
import com.scalar.core.jdbc.AbstractDAO;
import com.scalar.core.util.GUID;
import com.scalar.freequent.common.CategoryData;

import java.util.*;
import java.sql.Types;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 7, 2013
 * Time: 8:08:05 PM
 */
public class CategoryDataDAO extends AbstractDAO {
	protected static final Log logger = LogFactory.getLog(CategoryDataDAO.class);
	public static final String TABLE_NAME = "ftCategory";
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
    public CategoryDataRow findByPrimaryKey(String id) {
        String query = SQL_SelectAllColumns +
                        " WHERE " + COL_ID + " = ? ";
        List<CategoryDataRow> categories = getJdbcTemplate().query(query,
        new RowMapper<CategoryDataRow>() {
            public CategoryDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                CategoryDataRow row = new CategoryDataRow();
                row.setId(new GUID(rs.getString(COL_ID)));
                row.setName(rs.getString(COL_NAME));
                row.setDescription(rs.getString(COL_DESCRIPTION));
				row.clean();
                return row;
            }
        }, id);

        return categories.get(0);
    }

	/**
     * Returns the User object for the given userId.
     *
     * @param categoryName
     *
     * @return the User object for the given userId.
     */
    public CategoryDataRow findByName(String categoryName) {
        String query = SQL_SelectAllColumns +
                        " WHERE " + COL_NAME + " = ? ";
        List<CategoryDataRow> categories = getJdbcTemplate().query(query,
        new RowMapper<CategoryDataRow>() {
            public CategoryDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                CategoryDataRow row = new CategoryDataRow();
                row.setId(new GUID(rs.getString(COL_ID)));
                row.setName(rs.getString(COL_NAME));
                row.setDescription(rs.getString(COL_DESCRIPTION));
				row.clean();
                return row;
            }
        }, categoryName);

        return categories.get(0);
    }

	/**
     * Returns all the users in the system.
     *
     * @return the User object for the given userId.
     */
    public List<CategoryDataRow> findAll() {
        String query = SQL_SelectAllColumns;

        return getJdbcTemplate().query(query,
        new RowMapper<CategoryDataRow>() {
            public CategoryDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                CategoryDataRow row = new CategoryDataRow();
                row.setId(new GUID(rs.getString(COL_ID)));
                row.setName(rs.getString(COL_NAME));
                row.setDescription(rs.getString(COL_DESCRIPTION));
				row.clean();
                return row;
            }
        });
    }

	public int insert (CategoryDataRow row) {
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

    public int update (CategoryDataRow row) {
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

    public static CategoryData rowToData (CategoryDataRow row) {
		CategoryData CategoryData = new CategoryData();
        CategoryData.setId(row.getId().toString());
        CategoryData.setName(row.getName());
        CategoryData.setDescription(row.getDescription());

		return CategoryData;
    }

    public static CategoryDataRow dataToRow (CategoryData CategoryData) {
		CategoryDataRow row = new CategoryDataRow();
        row.setId(new GUID(CategoryData.getId()));
        row.setName(CategoryData.getName());
        row.setDescription(CategoryData.getDescription());

        return row;
    }
}