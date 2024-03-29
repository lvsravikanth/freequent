package com.scalar.freequent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.jdbc.core.RowMapper;
import com.scalar.core.jdbc.AbstractDAO;
import com.scalar.core.util.GUID;
import com.scalar.core.ScalarException;
import com.scalar.freequent.common.UnitData;
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
public class UnitDataDAO extends AbstractDAO {
	protected static final Log logger = LogFactory.getLog(UnitDataDAO.class);
	public static final String TABLE_NAME = "ftUnit";
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
    public UnitDataRow findByPrimaryKey(String id) {
        String query = SQL_SelectAllColumns +
                        " WHERE " + COL_ID + " = ? ";
        List<UnitDataRow> unitDataRows = getJdbcTemplate().query(query,
        new RowMapper<UnitDataRow>() {
            public UnitDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                UnitDataRow row = new UnitDataRow();
                row.setId(new GUID(rs.getString(COL_ID)));
                row.setName(rs.getString(COL_NAME));
                row.setDescription(rs.getString(COL_DESCRIPTION));
				row.clean();
                return row;
            }
        }, id);

        return unitDataRows.get(0);
    }

    /**
     * Returns the Unit object for the given unitName.
     *
     * @param searchParams
     *
     * @return the Unit object for the given userId.
     */
    public List<UnitDataRow> getUnits(Map<String, Object> searchParams) {
        String query = SQL_SelectAllColumns;
        List<Object> args = new ArrayList<Object>();
        String unitName = (String)searchParams.get(UnitData.PARAM_NAME);
        if (!StringUtil.isEmpty(unitName)) {
            query +="WHERE " + COL_NAME + " like ? ";
            args.add(unitName + "%");
        }
        return getJdbcTemplate().query(query, args.toArray(new Object[args.size()]),
            new RowMapper<UnitDataRow>() {
                public UnitDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                    UnitDataRow row = new UnitDataRow();
                    row.setName(rs.getString(COL_NAME));
                    row.setDescription(rs.getString(COL_DESCRIPTION));
                    row.setId(new GUID(rs.getString(COL_ID)));
                    row.clean();
                    return row;
                }
        });
    }

    /**
     * Returns the Unit object for the given unitId.
     *
     * @param unitId
     *
     * @return the Unit object for the given unitId.
     */
    public UnitDataRow findById(String unitId) {
        String query = SQL_SelectAllColumns +
                        " WHERE " + COL_ID + " = ? ";
        List<UnitDataRow> units = getJdbcTemplate().query(query,
        new RowMapper<UnitDataRow>() {
            public UnitDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                UnitDataRow row = new UnitDataRow();
                row.setId(new GUID(rs.getString(COL_ID)));
                row.setName(rs.getString(COL_NAME));
                row.setDescription(rs.getString(COL_DESCRIPTION));
				row.clean();
                return row;
            }
        }, unitId);

        return units.get(0);
    }

	/**
     * Returns the User object for the given userId.
     *
     * @param unitName
     *
     * @return the User object for the given userId.
     */
    public UnitDataRow findByName(String unitName) {
        String query = SQL_SelectAllColumns +
                        " WHERE " + COL_NAME + " = ? ";
        List<UnitDataRow> unitDataRows = getJdbcTemplate().query(query,
        new RowMapper<UnitDataRow>() {
            public UnitDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                UnitDataRow row = new UnitDataRow();
                row.setId(new GUID(rs.getString(COL_ID)));
                row.setName(rs.getString(COL_NAME));
                row.setDescription(rs.getString(COL_DESCRIPTION));
				row.clean();
                return row;
            }
        }, unitName);

        return unitDataRows.get(0);
    }

	/**
     * Returns all the users in the system.
     *
     * @return the User object for the given userId.
     */
    public List<UnitDataRow> findAll() {
        String query = SQL_SelectAllColumns;

        return getJdbcTemplate().query(query,
        new RowMapper<UnitDataRow>() {
            public UnitDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                UnitDataRow row = new UnitDataRow();
                row.setId(new GUID(rs.getString(COL_ID)));
                row.setName(rs.getString(COL_NAME));
                row.setDescription(rs.getString(COL_DESCRIPTION));
				row.clean();
                return row;
            }
        });
    }

	public int insert (UnitDataRow row) throws ScalarException {
		insertRecord(row.getId(), ObjectType.UNIT);
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

    public int update (UnitDataRow row) throws ScalarException {
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

    public static UnitData rowToData (UnitDataRow row) {
		UnitData unitData = new UnitData();
        unitData.setId(row.getId().toString());
        unitData.setName(row.getName());
        unitData.setDescription(row.getDescription());

		return unitData;
    }

    public static UnitDataRow dataToRow (UnitData unitData) {
		UnitDataRow row = new UnitDataRow();
        row.setId(new GUID(unitData.getId()));
        row.setName(unitData.getName());
        row.setDescription(unitData.getDescription());

        return row;
    }
}