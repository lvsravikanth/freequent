package com.scalar.freequent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.jdbc.core.RowMapper;
import com.scalar.core.jdbc.AbstractDAO;
import com.scalar.core.util.GUID;
import com.scalar.core.ScalarException;
import com.scalar.freequent.common.*;
import com.scalar.freequent.util.StringUtil;

import java.util.*;
import java.sql.Types;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 13, 2013
 * Time: 8:08:05 PM
 */
public class ItemDataDAO extends AbstractDAO {
	protected static final Log logger = LogFactory.getLog(ItemDataDAO.class);
	public static final String TABLE_NAME = "ftItem";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_CODE = "code";
	public static final String COL_GROUP_NAME = "groupName";
	public static final String COL_PRICE = "price";
	public static final String COL_PRICE_QTY = "priceQty";
	public static final String COL_UNIT = "unit";
	public static final String COL_DESCRIPTION = "description";
	public static final String COL_TAXABLE = "taxable";

	static final String SQL_SelectAllColumns =
		"select " +
		COL_ID + ", " +
		COL_NAME + ", " +
		COL_CODE + ", " +
		COL_GROUP_NAME + ", " +
		COL_PRICE + ", " +
		COL_PRICE_QTY + ", " +
		COL_DESCRIPTION + ", " +
		COL_TAXABLE + ", " +
		COL_UNIT +
		" from " + TABLE_NAME + " ";

	public static final Map<String, Integer> COL_SQL_TYPES = new HashMap<String, Integer>();

	static {
		COL_SQL_TYPES.put (COL_ID, Types.VARCHAR);
		COL_SQL_TYPES.put (COL_NAME, Types.VARCHAR);
		COL_SQL_TYPES.put (COL_CODE, Types.VARCHAR);
		COL_SQL_TYPES.put (COL_GROUP_NAME, Types.VARCHAR);
		COL_SQL_TYPES.put (COL_PRICE, Types.DOUBLE);
		COL_SQL_TYPES.put (COL_PRICE_QTY, Types.INTEGER);
		COL_SQL_TYPES.put (COL_UNIT, Types.VARCHAR);
		COL_SQL_TYPES.put (COL_DESCRIPTION, Types.VARCHAR);
		COL_SQL_TYPES.put (COL_TAXABLE, Types.BIT);
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
    public ItemDataRow findByPrimaryKey(String id) {
        String query = SQL_SelectAllColumns +
                        " WHERE " + COL_ID + " = ? ";
        List<ItemDataRow> items = getJdbcTemplate().query(query,
        new RowMapper<ItemDataRow>() {
            public ItemDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                ItemDataRow row = new ItemDataRow();
                row.setId(new GUID(rs.getString(COL_ID)));
                row.setName(rs.getString(COL_NAME));
                row.setCode(rs.getString(COL_CODE));
                row.setGroupName(rs.getString(COL_GROUP_NAME));
				row.setPrice(rs.getDouble(COL_PRICE));
				row.setPriceQty(rs.getInt(COL_PRICE_QTY));
				row.setDescription(rs.getString(COL_DESCRIPTION));
				row.setTaxable(rs.getInt(COL_TAXABLE));
				row.setUnit(rs.getString(COL_UNIT));
				row.clean();
                return row;
            }
        }, id);

        return items.isEmpty() ? null : items.get(0);
    }

	/**
     * Returns the ItemDataRow object for the given itemName.
     *
     * @param itemName
     *
     * @return the User object for the given userId.
     */
    public ItemDataRow findByName(String itemName) {
        String query = SQL_SelectAllColumns +
                        " WHERE " + COL_NAME + " = ? ";
        List<ItemDataRow> items = getJdbcTemplate().query(query,
        new RowMapper<ItemDataRow>() {
            public ItemDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                ItemDataRow row = new ItemDataRow();
                row.setId(new GUID(rs.getString(COL_ID)));
                row.setName(rs.getString(COL_NAME));
				row.setCode(rs.getString(COL_CODE));
                row.setGroupName(rs.getString(COL_GROUP_NAME));
				row.setPrice(rs.getDouble(COL_PRICE));
				row.setPriceQty(rs.getInt(COL_PRICE_QTY));
				row.setDescription(rs.getString(COL_DESCRIPTION));
				row.setTaxable(rs.getInt(COL_TAXABLE));
				row.setUnit(rs.getString(COL_UNIT));
				row.clean();
                return row;
            }
        }, itemName);

        return items.size()>0 ? items.get(0) : null;
    }

	/**
     * Returns all the users in the system.
     *
     * @return the User object for the given userId.
     */
    public List<ItemDataRow> findAll() {
        String query = SQL_SelectAllColumns;

        return getJdbcTemplate().query(query,
        new RowMapper<ItemDataRow>() {
            public ItemDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                ItemDataRow row = new ItemDataRow();
                row.setId(new GUID(rs.getString(COL_ID)));
                row.setName(rs.getString(COL_NAME));
                row.setCode(rs.getString(COL_CODE));
                row.setGroupName(rs.getString(COL_GROUP_NAME));
				row.setPrice(rs.getDouble(COL_PRICE));
				row.setPriceQty(rs.getInt(COL_PRICE_QTY));
				row.setDescription(rs.getString(COL_DESCRIPTION));
				row.setTaxable(rs.getInt(COL_TAXABLE));
				row.setUnit(rs.getString(COL_UNIT));
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
    public List<ItemDataRow> search(Map<String, String> params) {
        String query = SQL_SelectAllColumns;
		StringBuilder whereCaluse = new StringBuilder();
		List<Object> args = new ArrayList<Object>();
		boolean joinCategoryAssocTable = false;

		String name = params.get(Item.PARAM_NAME);
		String group = params.get(Item.PARAM_GROUP);
		String category = params.get(Item.PARAM_CATEGORY);

		String AND = " ";
		final String LIKE = " like ";
		final String EQUAL_TO = " = ";
		final String AND_STR = " AND ";
		if (!StringUtil.isEmpty(name)) {
			name += "%";
			whereCaluse.append(AND).append(COL_NAME).append(LIKE).append("?");
			args.add (name);
			AND = AND_STR;
		}

		if (!StringUtil.isEmpty(group)) {
			whereCaluse.append(AND).append(COL_GROUP_NAME).append(EQUAL_TO).append("?");
			args.add(group);
			AND = AND_STR;
		}

		if (!StringUtil.isEmpty(category)) {
			name += "%";
			whereCaluse.append(AND).append(CategoryAssocDataDAO.TABLE_NAME + "." + CategoryAssocDataDAO.COL_CATEGORY_ID).append(EQUAL_TO).append("?");
			args.add(category);
			AND = AND_STR;
			joinCategoryAssocTable = true;
		}

		if (joinCategoryAssocTable) {
			query = query + ", " + CategoryAssocDataDAO.TABLE_NAME;
		}
		if (whereCaluse.length() > 0) {
			query = query + " WHERE " + whereCaluse.toString();
		}
		if (joinCategoryAssocTable) {
			query = query + AND + CategoryAssocDataDAO.COL_OBJECT_ID + EQUAL_TO + COL_ID;
		}

        return getJdbcTemplate().query(query, args.toArray(new Object[args.size()]),
        new RowMapper<ItemDataRow>() {
            public ItemDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                ItemDataRow row = new ItemDataRow();
                row.setId(new GUID(rs.getString(COL_ID)));
                row.setName(rs.getString(COL_NAME));
                row.setCode(rs.getString(COL_CODE));
                row.setGroupName(rs.getString(COL_GROUP_NAME));
				row.setPrice(rs.getDouble(COL_PRICE));
				row.setPriceQty(rs.getInt(COL_PRICE_QTY));
				row.setDescription(rs.getString(COL_DESCRIPTION));
				row.setTaxable(rs.getInt(COL_TAXABLE));
				row.setUnit(rs.getString(COL_UNIT));
				row.clean();
                return row;
            }
        });
    }

	public int insert (ItemDataRow row) throws ScalarException {
		insertRecord(row.getId(), ObjectType.ITEM);
        StringBuilder query = new StringBuilder();
		String sep = "";
        query.append ("insert into " + TABLE_NAME + " (");
        query.append(sep).append (COL_ID); sep = ",";
        query.append(sep).append(COL_NAME);
        query.append(sep).append(COL_CODE);
        query.append(sep).append(COL_GROUP_NAME);
        query.append(sep).append(COL_PRICE);
        query.append(sep).append(COL_PRICE_QTY);
        query.append(sep).append(COL_DESCRIPTION);
        query.append(sep).append(COL_TAXABLE);
        query.append(sep).append(COL_UNIT);
		query.append(") values (?, ?, ?, ?, ?, ?, ?, ?, ?)");

		return getJdbcTemplate().update(query.toString(),
                row.getId().toString(),
                row.getName(),
                row.getCode(),
				row.getGroupName(),
				row.getPrice(),
				row.getPriceQty(),
				row.getDescription(),
				row.getTaxable(),
				row.getUnit()
		);
    }

    public int update (ItemDataRow row) throws ScalarException {
		updateRecord(row.getId());
        StringBuilder query = new StringBuilder();
		String sep = "";
        query.append ("update " + TABLE_NAME + " set ");
        if( row.modName() ){ query.append(sep).append(COL_NAME).append(" = ?"); sep = ","; }
        if( row.modCode() ){ query.append(sep).append(COL_CODE).append(" = ?"); sep = ","; }
        if( row.modGroupName() ){ query.append(sep).append(COL_GROUP_NAME).append(" = ?"); sep = ","; }
        if( row.modPrice() ){ query.append(sep).append(COL_PRICE).append(" = ?"); sep = ","; }
        if( row.modPriceQty() ){ query.append(sep).append(COL_PRICE_QTY).append(" = ?"); sep = ","; }
        if( row.modDescription() ){ query.append(sep).append(COL_DESCRIPTION).append(" = ?"); sep = ","; }
        if( row.modTaxable() ){ query.append(sep).append(COL_TAXABLE).append(" = ?"); sep = ","; }
        if( row.modUnit() ){ query.append(sep).append(COL_UNIT).append(" = ?"); sep = ","; }
		query.append(" where ").append(COL_ID).append(" = ?");

		List<Object> args = new ArrayList<Object>();
		List<Integer> argTypes = new ArrayList<Integer>();
        if (row.modName()) { args.add (row.getName()); argTypes.add(COL_SQL_TYPES.get(COL_NAME)); }
        if (row.modCode()) { args.add (row.getCode()); argTypes.add(COL_SQL_TYPES.get(COL_CODE)); }
        if (row.modGroupName()) { args.add (row.getGroupName()); argTypes.add(COL_SQL_TYPES.get(COL_GROUP_NAME)); }
        if (row.modPrice()) { args.add (row.getPrice()); argTypes.add(COL_SQL_TYPES.get(COL_PRICE)); }
        if (row.modPriceQty()) { args.add (row.getPriceQty()); argTypes.add(COL_SQL_TYPES.get(COL_PRICE_QTY)); }
        if (row.modDescription()) { args.add (row.getDescription()); argTypes.add(COL_SQL_TYPES.get(COL_DESCRIPTION)); }
        if (row.modTaxable()) { args.add (row.getTaxable()); argTypes.add(COL_SQL_TYPES.get(COL_TAXABLE)); }
        if (row.modUnit()) { args.add (row.getUnit()); argTypes.add(COL_SQL_TYPES.get(COL_UNIT)); }
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

    public static Item rowToData (ItemDataRow row) {
		Item itemData = new Item();
        itemData.setId(row.getId().toString());
        itemData.setName(row.getName());
        itemData.setCode(row.getCode());
		GroupData groupData = new GroupData();
		groupData.setName(row.getGroupName());
        itemData.setGroupData(groupData);
		itemData.setPrice(row.getPrice());
		itemData.setPriceQty(row.getPriceQty());
		UnitData unitData = new UnitData();
		unitData.setName(row.getUnit());
		itemData.setUnitData(unitData);
		itemData.setDescription(row.getDescription());
		itemData.setTaxable(row.getTaxable()!=0);

		return itemData;
    }

    public static ItemDataRow dataToRow (Item itemData) {
		ItemDataRow row = new ItemDataRow();
        row.setId(new GUID(itemData.getId()));
        row.setName(itemData.getName());
		row.setCode(itemData.getCode());
        row.setGroupName(itemData.getGroupData()==null ? null : itemData.getGroupData().getName());
		row.setPrice(itemData.getPrice());
		row.setPriceQty(itemData.getPriceQty());
		row.setUnit(itemData.getUnitData() == null ? null : itemData.getUnitData().getName());
		row.setDescription(itemData.getDescription());
		row.setTaxable(itemData.getTaxable() ? 1 : 0);

        return row;
    }
}