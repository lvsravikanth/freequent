package com.scalar.freequent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.jdbc.core.RowMapper;
import com.scalar.core.jdbc.AbstractDAO;
import com.scalar.core.util.GUID;
import com.scalar.core.ScalarException;
import com.scalar.freequent.common.TaxRateData;
import com.scalar.freequent.common.ObjectType;

import java.util.*;
import java.sql.Types;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 7, 2013
 * Time: 8:08:05 PM
 */
public class TaxRateDataDAO extends AbstractDAO {
	protected static final Log logger = LogFactory.getLog(TaxRateDataDAO.class);
	public static final String TABLE_NAME = "ftTaxRate";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_PERCENTAGE = "percentage";
    public static final String COL_DESCRIPTION = "description";

	static final String SQL_SelectAllColumns =
		"select " +
		COL_ID + ", " +
		COL_NAME + ", " +
		COL_PERCENTAGE + ", " +
		COL_DESCRIPTION +
		" from " + TABLE_NAME + " ";

	public static final Map<String, Integer> COL_SQL_TYPES = new HashMap<String, Integer>();

	static {
		COL_SQL_TYPES.put (COL_ID, Types.VARCHAR);
		COL_SQL_TYPES.put (COL_NAME, Types.VARCHAR);
		COL_SQL_TYPES.put (COL_PERCENTAGE, Types.DOUBLE);
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
    public TaxRateDataRow findByPrimaryKey(String id) {
        String query = SQL_SelectAllColumns +
                        " WHERE " + COL_ID + " = ? ";
        List<TaxRateDataRow> TaxRateDataRows = getJdbcTemplate().query(query,
        new RowMapper<TaxRateDataRow>() {
            public TaxRateDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                TaxRateDataRow row = new TaxRateDataRow();
                row.setId(new GUID(rs.getString(COL_ID)));
                row.setName(rs.getString(COL_NAME));
                row.setPercentage(rs.getDouble(COL_PERCENTAGE));
                row.setDescription(rs.getString(COL_DESCRIPTION));
				row.clean();
                return row;
            }
        }, id);

        return TaxRateDataRows.get(0);
    }

	/**
     * Returns the User object for the given userId.
     *
     * @param unitName
     *
     * @return the User object for the given userId.
     */
    public TaxRateDataRow findByName(String unitName) {
        String query = SQL_SelectAllColumns +
                        " WHERE " + COL_NAME + " = ? ";
        List<TaxRateDataRow> TaxRateDataRows = getJdbcTemplate().query(query,
        new RowMapper<TaxRateDataRow>() {
            public TaxRateDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                TaxRateDataRow row = new TaxRateDataRow();
                row.setId(new GUID(rs.getString(COL_ID)));
                row.setName(rs.getString(COL_NAME));
                row.setPercentage(rs.getDouble(COL_PERCENTAGE));
                row.setDescription(rs.getString(COL_DESCRIPTION));
				row.clean();
                return row;
            }
        }, unitName);

        return TaxRateDataRows.get(0);
    }

	/**
     * Returns all the users in the system.
     *
     * @return the User object for the given userId.
     */
    public List<TaxRateDataRow> findAll() {
        String query = SQL_SelectAllColumns;

        return getJdbcTemplate().query(query,
        new RowMapper<TaxRateDataRow>() {
            public TaxRateDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                TaxRateDataRow row = new TaxRateDataRow();
                row.setId(new GUID(rs.getString(COL_ID)));
                row.setName(rs.getString(COL_NAME));
				row.setPercentage(rs.getDouble(COL_PERCENTAGE));
                row.setDescription(rs.getString(COL_DESCRIPTION));
				row.clean();
                return row;
            }
        });
    }

	public int insert (TaxRateDataRow row) throws ScalarException {
		insertRecord(row.getId(), ObjectType.TAX_RATE);
        StringBuilder query = new StringBuilder();
		String sep = "";
        query.append ("insert into " + TABLE_NAME + " (");
        query.append(sep).append (COL_ID); sep = ",";
        query.append(sep).append(COL_NAME);
        query.append(sep).append(COL_PERCENTAGE);
        query.append(sep).append(COL_DESCRIPTION);
		query.append(") values (?, ?, ?, ?)");

		return getJdbcTemplate().update(query.toString(),
                row.getId().toString(),
                row.getName(),
                row.getPercentage(),
                row.getDescription()
		);
    }

    public int update (TaxRateDataRow row) throws ScalarException {
		updateRecord(row.getId());
        StringBuilder query = new StringBuilder();
		String sep = "";
        query.append ("update " + TABLE_NAME + " set ");
        if( row.modName() ){ query.append(sep).append(COL_NAME).append(" = ?"); sep = ","; }
        if( row.modPercentage() ){ query.append(sep).append(COL_PERCENTAGE).append(" = ?"); sep = ","; }
        if( row.modDescription() ){ query.append(sep).append(COL_DESCRIPTION).append(" = ?"); sep = ","; }
		query.append(" where ").append(COL_ID).append(" = ?");

		List<Object> args = new ArrayList<Object>();
		List<Integer> argTypes = new ArrayList<Integer>();
        if (row.modName()) { args.add (row.getName()); argTypes.add(COL_SQL_TYPES.get(COL_NAME)); }
        if (row.modPercentage()) { args.add (row.getPercentage()); argTypes.add(COL_SQL_TYPES.get(COL_PERCENTAGE)); }
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

    public static TaxRateData rowToData (TaxRateDataRow row) {
		TaxRateData TaxRateData = new TaxRateData();
        TaxRateData.setId(row.getId().toString());
        TaxRateData.setName(row.getName());
        TaxRateData.setPercentage(row.getPercentage());
        TaxRateData.setDescription(row.getDescription());

		return TaxRateData;
    }

    public static TaxRateDataRow dataToRow (TaxRateData TaxRateData) {
		TaxRateDataRow row = new TaxRateDataRow();
        row.setId(new GUID(TaxRateData.getId()));
        row.setName(TaxRateData.getName());
        row.setPercentage(TaxRateData.getPercentage());
        row.setDescription(TaxRateData.getDescription());

        return row;
    }
}