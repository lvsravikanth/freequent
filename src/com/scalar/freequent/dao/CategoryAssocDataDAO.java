package com.scalar.freequent.dao;

import com.scalar.core.ScalarException;
import com.scalar.core.jdbc.AbstractDAO;
import com.scalar.core.util.GUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 13, 2013
 * Time: 8:08:05 PM
 */
public class CategoryAssocDataDAO extends AbstractDAO {
	protected static final Log logger = LogFactory.getLog(CategoryAssocDataDAO.class);
	public static final String TABLE_NAME = "ftCategoryAssoc";
	public static final String COL_OBJECT_ID = "objId";
	public static final String COL_CATEGORY_ID = "categoryId";

	static final String SQL_SelectAllColumns =
			"select " +
					COL_OBJECT_ID + ", " +
					COL_CATEGORY_ID +
					" from " + TABLE_NAME + " ";

	public static final Map<String, Integer> COL_SQL_TYPES = new HashMap<String, Integer>();

	static {
		COL_SQL_TYPES.put(COL_OBJECT_ID, Types.VARCHAR);
		COL_SQL_TYPES.put(COL_CATEGORY_ID, Types.VARCHAR);
	}

	public boolean exists(String objectId, String categoryId) {
		String query = "SELECT COUNT(*) FROM " + TABLE_NAME
				+ " WHERE " + COL_OBJECT_ID + " = ? and " + COL_CATEGORY_ID + " = ?";
		return getJdbcTemplate().queryForInt(query, objectId, categoryId) != 0;
	}

	/**
	 * Returns all the users in the system.
	 *
	 * @return the User object for the given userId.
	 */
	public List<CategoryAssocDataRow> findAll() {
		String query = SQL_SelectAllColumns;

		return getJdbcTemplate().query(query,
				new RowMapper<CategoryAssocDataRow>() {
					public CategoryAssocDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
						CategoryAssocDataRow row = new CategoryAssocDataRow();
						row.setObjectId(new GUID(rs.getString(COL_OBJECT_ID)));
						row.setCategoryId(new GUID(rs.getString(COL_CATEGORY_ID)));
						row.clean();
						return row;
					}
				});
	}

	/**
	 * Returns all the users in the system.
	 *
	 * @param objectId
	 * @return the User object for the given userId.
	 */
	public List<CategoryAssocDataRow> findByObjectId(String objectId) {
		String query = SQL_SelectAllColumns + " where " + COL_OBJECT_ID + " = ?";

		return getJdbcTemplate().query(query,
				new RowMapper<CategoryAssocDataRow>() {
					public CategoryAssocDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
						CategoryAssocDataRow row = new CategoryAssocDataRow();
						row.setObjectId(new GUID(rs.getString(COL_OBJECT_ID)));
						row.setCategoryId(new GUID(rs.getString(COL_CATEGORY_ID)));
						row.clean();
						return row;
					}
				}, objectId);
	}

	/**
	 * @param row
	 * @return
	 * @throws ScalarException
	 */
	public int insert(CategoryAssocDataRow row) throws ScalarException {
		StringBuilder query = new StringBuilder();
		String sep = "";
		query.append("insert into " + TABLE_NAME + " (");
		query.append(sep).append(COL_OBJECT_ID);
		sep = ",";
		query.append(sep).append(COL_CATEGORY_ID);
		query.append(") values (?, ?)");

		return getJdbcTemplate().update(query.toString(),
				row.getObjectId().toString(),
				row.getCategoryId().toString()
		);
	}

	/**
	 * @param objectId
	 * @param categoryIds
	 */
	public void insert(final String objectId, final String[] categoryIds) {
		StringBuilder query = new StringBuilder();
		String sep = "";
		query.append("insert into ").append(TABLE_NAME).append("(");
		query.append(sep).append(COL_OBJECT_ID);
		sep = ",";
		query.append(sep).append(COL_CATEGORY_ID);
		query.append(") values (?, ?)");

		getJdbcTemplate().batchUpdate(
				query.toString(),
				new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ps.setString(1, objectId);
						ps.setString(2, categoryIds[i]);
					}

					public int getBatchSize() {
						return categoryIds.length;
					}
				});
	}

	/**
	 * @param objectId
	 * @return
	 * @throws ScalarException
	 */
	public int removeByObjectId(String objectId) {
		String query = "delete from " + TABLE_NAME + " where " + COL_OBJECT_ID + " = ?";

		return getJdbcTemplate().update(query, objectId);
	}


}