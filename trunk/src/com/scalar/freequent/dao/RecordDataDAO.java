package com.scalar.freequent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.jdbc.core.RowMapper;
import com.scalar.core.jdbc.AbstractDAO;
import com.scalar.core.util.GUID;
import com.scalar.freequent.common.Record;

import java.util.*;
import java.sql.Types;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 7, 2013
 * Time: 8:08:05 PM
 */
public class RecordDataDAO extends AbstractDAO {
	protected static final Log logger = LogFactory.getLog(RecordDataDAO.class);
	public static final String TABLE_NAME = "ftRecord";
    public static final String COL_RECORD_ID = "recordId";
    public static final String COL_OBJECT_ID = "objectId";
    public static final String COL_OBJECT_TYPE = "objectType";
    public static final String COL_CREATED_BY = "createdBy";
    public static final String COL_CREATED_ON = "createdOn";
    public static final String COL_MODIFIED_BY = "modifiedBy";
    public static final String COL_MODIFIED_ON = "modifiedOn";

	static final String SQL_SelectAllColumns =
		"select " +
			COL_RECORD_ID + ", " +
			COL_OBJECT_ID + ", " +
			COL_OBJECT_TYPE + ", " +
			COL_CREATED_BY + ", " +
			COL_CREATED_ON + ", " +
			COL_MODIFIED_BY + ", " +
			COL_MODIFIED_ON +
		" from " + TABLE_NAME + " ";

	public static final Map<String, Integer> COL_SQL_TYPES = new HashMap<String, Integer>();

	static {
		COL_SQL_TYPES.put (COL_RECORD_ID, Types.VARCHAR);
		COL_SQL_TYPES.put (COL_OBJECT_ID, Types.VARCHAR);
		COL_SQL_TYPES.put (COL_OBJECT_TYPE, Types.VARCHAR);
		COL_SQL_TYPES.put (COL_CREATED_BY, Types.VARCHAR);
		COL_SQL_TYPES.put (COL_CREATED_ON, Types.DATE);
		COL_SQL_TYPES.put (COL_MODIFIED_BY, Types.VARCHAR);
		COL_SQL_TYPES.put (COL_MODIFIED_ON, Types.DATE);
	}

	public boolean existByObjectId (String objectId) {
		String query = "SELECT COUNT(*) FROM " + TABLE_NAME
                            + " WHERE " + COL_OBJECT_ID + " = ? ";
		return getJdbcTemplate().queryForInt(query, objectId) != 0;
	}

	/**
     * Returns the Record object for the given userId.
     *
     * @param objectId
     *
     * @return the User object for the given userId.
     */
    public RecordDataRow findByObjectId(String objectId) {
        String query = SQL_SelectAllColumns +
                        " WHERE " + COL_OBJECT_ID + " = ? ";
        List<RecordDataRow> recordDataRows = getJdbcTemplate().query(query,
        new RowMapper<RecordDataRow>() {
            public RecordDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                RecordDataRow row = new RecordDataRow();
                row.setRecordId(new GUID(rs.getString(COL_RECORD_ID)));
                row.setObjectId(new GUID(rs.getString(COL_OBJECT_ID)));
                row.setObjectType(rs.getString(COL_OBJECT_TYPE));
                row.setCreatedBy(rs.getString(COL_CREATED_BY));
                row.setCreatedOn(rs.getDate(COL_CREATED_ON));
                row.setModifiedBy(rs.getString(COL_MODIFIED_BY));
                row.setModifiedOn(rs.getDate(COL_MODIFIED_ON));
				row.clean();
                return row;
            }
        }, objectId);

        return recordDataRows.get(0);
    }

	/**
     * Returns the User object for the given userId.
     *
     * @param objectType
     *
     * @return the User object for the given userId.
     */
    public List<RecordDataRow> findByObjectType(String objectType) {
        String query = SQL_SelectAllColumns +
                        " WHERE " + COL_OBJECT_TYPE + " = ? ";
        List<RecordDataRow> recordDataRows = getJdbcTemplate().query(query,
        new RowMapper<RecordDataRow>() {
            public RecordDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                RecordDataRow row = new RecordDataRow();
                row.setRecordId(new GUID(rs.getString(COL_RECORD_ID)));
                row.setObjectId(new GUID(rs.getString(COL_OBJECT_ID)));
                row.setObjectType(rs.getString(COL_OBJECT_TYPE));
                row.setCreatedBy(rs.getString(COL_CREATED_BY));
                row.setCreatedOn(rs.getDate(COL_CREATED_ON));
                row.setModifiedBy(rs.getString(COL_MODIFIED_BY));
                row.setModifiedOn(rs.getDate(COL_MODIFIED_ON));
				row.clean();
                return row;
            }
        }, objectType);

        return recordDataRows;
    }

	/**
     * Returns all the users in the system.
     *
     * @return the User object for the given userId.
     */
    public List<RecordDataRow> findAll() {
        String query = SQL_SelectAllColumns;

        return getJdbcTemplate().query(query,
        new RowMapper<RecordDataRow>() {
            public RecordDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                RecordDataRow row = new RecordDataRow();
                row.setRecordId(new GUID(rs.getString(COL_RECORD_ID)));
                row.setObjectId(new GUID(rs.getString(COL_OBJECT_ID)));
                row.setObjectType(rs.getString(COL_OBJECT_TYPE));
                row.setCreatedBy(rs.getString(COL_CREATED_BY));
                row.setCreatedOn(rs.getDate(COL_CREATED_ON));
                row.setModifiedBy(rs.getString(COL_MODIFIED_BY));
                row.setModifiedOn(rs.getDate(COL_MODIFIED_ON));
				row.clean();
                return row;
            }
        });
    }

	public int insert (RecordDataRow row) {
        StringBuilder query = new StringBuilder();
		Calendar now = Calendar.getInstance();
		String sep = "";
        query.append ("insert into " + TABLE_NAME + " (");
        query.append(sep).append (COL_RECORD_ID); sep = ",";
        query.append(sep).append (COL_OBJECT_ID);
        query.append(sep).append (COL_OBJECT_TYPE);
        query.append(sep).append (COL_CREATED_BY);
        query.append(sep).append (COL_CREATED_ON);
        query.append(sep).append (COL_MODIFIED_BY);
        query.append(sep).append (COL_MODIFIED_ON);
		query.append(") values (?, ?, ?, ?, ?, ?, ?)");

		return getJdbcTemplate().update(query.toString(),
                row.getRecordId().toString(),
                row.getObjectId().toString(),
                row.getObjectType(),
				row.getCreatedBy(),
				now.getTime(),
				row.getCreatedBy(),
				now.getTime()
		);
    }

    public int updateByObjectId (RecordDataRow row) {
        StringBuilder query = new StringBuilder();
		Calendar now = Calendar.getInstance();
		String sep = "";
        query.append ("update " + TABLE_NAME + " set ");
        if( row.modModifiedBy() ){ query.append(sep).append(COL_MODIFIED_BY).append(" = ?"); sep = ","; }
		query.append(sep).append(COL_MODIFIED_ON).append(" = ?");
		query.append(" where ").append(COL_OBJECT_ID).append(" = ?");

		List<Object> args = new ArrayList<Object>();
		List<Integer> argTypes = new ArrayList<Integer>();
        if (row.modModifiedBy()) { args.add (row.getModifiedBy()); argTypes.add(COL_SQL_TYPES.get(COL_MODIFIED_BY)); }
        args.add (now.getTime()); argTypes.add(COL_SQL_TYPES.get(COL_MODIFIED_ON));

        return getJdbcTemplate().update(query.toString(), args.toArray(new Object[args.size()]), ArrayUtils.toPrimitive(argTypes.toArray(new Integer[argTypes.size()])));
    }

    public static Record rowToData (RecordDataRow row) {
		Record recordData = new Record();
		recordData.setRecordId(row.getRecordId().toString());
		recordData.setObjectId(row.getObjectId().toString());
		recordData.setObjectType(row.getObjectType());
		recordData.setCreatedBy(row.getCreatedBy());
		recordData.setCreatedOn(row.getCreatedOn());
		recordData.setModifiedBy(row.getModifiedBy());
		recordData.setModifiedOn(row.getModifiedOn());

		return recordData;
    }

    public static RecordDataRow dataToRow (Record record) {
		RecordDataRow row = new RecordDataRow();
        row.setRecordId(new GUID(record.getRecordId()));
        row.setObjectId(new GUID(record.getObjectId()));
        row.setObjectType(record.getObjectType());
		row.setCreatedBy(record.getCreatedBy());
		row.setCreatedOn(record.getCreatedOn());
		row.setModifiedBy(record.getModifiedBy());
		row.setModifiedOn(record.getModifiedOn());

        return row;
    }
}