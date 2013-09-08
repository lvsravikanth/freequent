package com.scalar.freequent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import com.scalar.core.jdbc.AbstractDAO;
import com.scalar.freequent.auth.UserCapability;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

/**
 * User: Sujan Kumar Suppala
 * Date: Sep 7, 2013
 * Time: 7:07:04 PM
 */
public class UserCapabilityInfoDAO extends AbstractDAO {
    protected static final Log logger = LogFactory.getLog(UserCapabilityInfoDAO.class);
    
    public static final String TABLE_NAME = "ftUserCapabilityInfo";
    public static final String COL_CAPABILITY_NAME = "capabilityName";
    public static final String COL_HAS_READ = "hasRead";
    public static final String COL_HAS_WRITE = "hasWrite";
    public static final String COL_HAS_DELETE = "hasDelete";
    public static final String COL_USER_ID = "userId";

    static final String SQL_SelectAllColumns = 
            "select " +
                    COL_CAPABILITY_NAME + "," +
                    COL_HAS_READ + "," +
                    COL_HAS_WRITE + "," +
                    COL_HAS_DELETE + "," +
                    COL_USER_ID + "," +
                " from " + TABLE_NAME + " ";

    public List<UserCapabilityInfoRow> findAll () {
        String query = SQL_SelectAllColumns ;
        List<UserCapabilityInfoRow> capabilities = getJdbcTemplate().query(query,
        new RowMapper<UserCapabilityInfoRow>() {
            public UserCapabilityInfoRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                UserCapabilityInfoRow row = new UserCapabilityInfoRow();
                row.setCapabilityName(rs.getString(COL_CAPABILITY_NAME));
                row.setHasRead(rs.getInt(COL_HAS_READ));
                row.setHasWrite(rs.getInt(COL_HAS_WRITE));
                row.setHasDelete(rs.getInt(COL_HAS_DELETE));
                row.setUserId(rs.getString(COL_USER_ID));
                row.clean();
                return row;
            }
        });
        
        return capabilities;
    }

    public List<UserCapabilityInfoRow> findByUserId (String userId) {
        String query = SQL_SelectAllColumns +
                " where " + COL_USER_ID + " = ?";
        List<UserCapabilityInfoRow> capabilities = getJdbcTemplate().query(query,
        new RowMapper<UserCapabilityInfoRow>() {
            public UserCapabilityInfoRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                UserCapabilityInfoRow row = new UserCapabilityInfoRow();
                row.setCapabilityName(COL_CAPABILITY_NAME);
                row.setHasRead(rs.getInt(COL_HAS_READ));
                row.setHasWrite(rs.getInt(COL_HAS_WRITE));
                row.setHasDelete(rs.getInt(COL_HAS_DELETE));
                row.setUserId(rs.getString(COL_USER_ID));
                row.clean();
                return row;
            }
        }, userId);

        return capabilities;
    }

    public void insert (final UserCapabilityInfoRow[] rows) {
        StringBuilder query = new StringBuilder();
        String sep = "";
        query.append ("insert into ").append(TABLE_NAME).append("(");
        query.append(sep).append (COL_CAPABILITY_NAME); sep = ",";
        query.append(sep).append(COL_USER_ID);
        query.append(sep).append(COL_HAS_READ);
        query.append(sep).append(COL_HAS_WRITE);
        query.append(sep).append(COL_HAS_DELETE);
        query.append(") values (?, ?, ?, ?, ?)");

        getJdbcTemplate().batchUpdate(
                query.toString(),
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, rows[i].getCapabilityName());
                        ps.setString(2, rows[i].getUserId());
                        ps.setInt(3, rows[i].getHasRead());
                        ps.setInt(4, rows[i].getHasWrite());
                        ps.setInt(5, rows[i].getHasDelete());
                    }

                    public int getBatchSize() {
                        return rows.length;
                    }
                } );
    }

    public void deleteByUserId (String userId) {
        StringBuilder query = new StringBuilder();
        query.append ("delete from ").append(TABLE_NAME).append(" where ").append(COL_USER_ID).append(" = ?");
        getJdbcTemplate().update(query.toString(), userId);
    }

    public static UserCapability rowToData (UserCapabilityInfoRow row) {
        UserCapability data = new UserCapability();
        data.setCapabilityName(row.getCapabilityName());
        data.setHasRead(row.getHasRead() != 0);
        data.setHasWrite(row.getHasWrite() != 0);
        data.setHasDelete(row.getHasDelete() != 0);
        data.setUserId(row.getUserId());

        return data;
    }

    public static UserCapabilityInfoRow dataToRow (UserCapability data) {
        UserCapabilityInfoRow row = new UserCapabilityInfoRow();
        row.setUserId(data.getUserId());
        row.setCapabilityName(data.getCapabilityName());
        row.setHasRead(data.isHasRead() ? 1 : 0);
        row.setHasWrite(data.isHasWrite() ? 1 : 0);
        row.setHasDelete(data.isHasDelete() ? 1 : 0);

        return row;
    }
            
}