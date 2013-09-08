package com.scalar.freequent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;
import com.scalar.core.jdbc.AbstractDAO;
import com.scalar.freequent.auth.Capability;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: Sujan Kumar Suppala
 * Date: Sep 7, 2013
 * Time: 7:07:04 PM
 */
public class CapabilityInfoDAO extends AbstractDAO {
    protected static final Log logger = LogFactory.getLog(CapabilityInfoDAO.class);

    public static final String TABLE_NAME = "ftCapabilityInfo";
    public static final String COL_NAME = "name";
    public static final String COL_SUPPORTS_READ = "supportsRead";
    public static final String COL_SUPPORTS_WRITE = "supportsWrite";
    public static final String COL_SUPPORTS_DELETE = "supportsDelete";

    static final String SQL_SelectAllColumns =
            "select " +
                    COL_NAME + "," +
                    COL_SUPPORTS_READ + "," +
                    COL_SUPPORTS_WRITE + "," +
                    COL_SUPPORTS_DELETE + "," +
                " from " + TABLE_NAME + " ";

    public List<CapabilityInfoRow> findAll () {
        String query = SQL_SelectAllColumns ;
        List<CapabilityInfoRow> capabilities = getJdbcTemplate().query(query,
        new RowMapper<CapabilityInfoRow>() {
            public CapabilityInfoRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                CapabilityInfoRow row = new CapabilityInfoRow();
                row.setName(rs.getString(COL_NAME));
                row.setSupportsRead(rs.getInt(COL_SUPPORTS_READ));
                row.setSupportsWrite(rs.getInt(COL_SUPPORTS_WRITE));
                row.setSupportsDelete(rs.getInt(COL_SUPPORTS_DELETE));
                row.clean();
                return row;
            }
        });

        return capabilities;
    }

    public static Capability rowToData (CapabilityInfoRow row) {
        Capability capability = new Capability();
        capability.setName(row.getName());
        capability.setSupportsRead(row.getSupportsRead()!=0);
        capability.setSupportsWrite(row.getSupportsWrite()!=0);
        capability.setSupportsDelete(row.getSupportsDelete()!=0);

        return capability;
    }

    public static CapabilityInfoRow dataToRow (Capability data) {
        CapabilityInfoRow row = new CapabilityInfoRow();
        row.setName(data.getName());
        row.setSupportsRead(data.isSupportsRead() ? 1 : 0);
        row.setSupportsWrite(data.isSupportsWrite() ? 1 : 0);
        row.setSupportsDelete(data.isSupportsDelete() ? 1 : 0);

        return row;
    }

}
