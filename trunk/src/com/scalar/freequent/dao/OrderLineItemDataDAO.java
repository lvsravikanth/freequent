package com.scalar.freequent.dao;

import com.scalar.core.ScalarException;
import com.scalar.core.jdbc.AbstractDAO;
import com.scalar.core.util.GUID;
import com.scalar.freequent.common.OrderLineItemData;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 13, 2013
 * Time: 8:08:05 PM
 */
public class OrderLineItemDataDAO extends AbstractDAO {
	protected static final Log logger = LogFactory.getLog(OrderLineItemDataDAO.class);
	public static final String TABLE_NAME = "ftOrderLineItem";
	public static final String COL_ID = "id";
	public static final String COL_ORDER_ID = "orderId";
	public static final String COL_LINE_NUMBER = "lineNumber";
	public static final String COL_ITEM_ID = "itemId";
	public static final String COL_QTY = "qty";
	public static final String COL_PRICE = "price";
	public static final String COL_TAXABLE = "taxable";

	static final String SQL_SelectAllColumns =
			"select " +
					COL_ID + ", " +
					COL_ORDER_ID + ", " +
					COL_LINE_NUMBER + ", " +
					COL_ITEM_ID + ", " +
					COL_QTY + ", " +
					COL_PRICE + ", " +
					COL_TAXABLE +
					" from " + TABLE_NAME + " ";

	public static final Map<String, Integer> COL_SQL_TYPES = new HashMap<String, Integer>();

	static {
		COL_SQL_TYPES.put(COL_ID, Types.VARCHAR);
		COL_SQL_TYPES.put(COL_ORDER_ID, Types.VARCHAR);
		COL_SQL_TYPES.put(COL_LINE_NUMBER, Types.INTEGER);
		COL_SQL_TYPES.put(COL_ITEM_ID, Types.VARCHAR);
		COL_SQL_TYPES.put(COL_QTY, Types.INTEGER);
		COL_SQL_TYPES.put(COL_PRICE, Types.DOUBLE);
		COL_SQL_TYPES.put(COL_TAXABLE, Types.INTEGER);
	}

	public boolean existsByLineNumber(String orderId, int lineNumber) {
		String query = "SELECT COUNT(*) FROM " + TABLE_NAME
				+ " WHERE " + COL_ORDER_ID + " = ? AND " + COL_LINE_NUMBER + " = ?";
		return getJdbcTemplate().queryForInt(query, orderId, lineNumber) != 0;
	}

	/**
	 * Returns the User object for the given userId.
	 *
	 * @param id
	 * @return the User object for the given userId.
	 */
	public OrderLineItemDataRow findByPrimaryKey(String id) {
		String query = SQL_SelectAllColumns +
				" WHERE " + COL_ID + " = ? ";
		List<OrderLineItemDataRow> lineItems = getJdbcTemplate().query(query,
				new RowMapper<OrderLineItemDataRow>() {
					public OrderLineItemDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
						OrderLineItemDataRow row = new OrderLineItemDataRow();
						row.setId(new GUID(rs.getString(COL_ID)));
						row.setOrderId(new GUID(rs.getString(COL_ORDER_ID)));
						row.setLineNumber(rs.getInt(COL_LINE_NUMBER));
						row.setItemId(new GUID(rs.getString(COL_ITEM_ID)));
						row.setQty(rs.getInt(COL_QTY));
						row.setPrice(rs.getDouble(COL_PRICE));
						row.setTaxable(rs.getInt(COL_TAXABLE));
						row.clean();
						return row;
					}
				}, id);

		return lineItems.isEmpty() ? null : lineItems.get(0);
	}

	/**
	 * Returns the OrderLineItemDataRow object for the given itemName.
	 *
	 * @param orderId
	 * @param lineNumber
	 * @return the User object for the given userId.
	 */
	public OrderLineItemDataRow findByLineNumber(String orderId, int lineNumber) {
		String query = SQL_SelectAllColumns +
				" WHERE " + COL_ORDER_ID + " = ? AND " + COL_LINE_NUMBER + " = ?";
		List<OrderLineItemDataRow> items = getJdbcTemplate().query(query,
				new RowMapper<OrderLineItemDataRow>() {
					public OrderLineItemDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
						OrderLineItemDataRow row = new OrderLineItemDataRow();
						row.setId(new GUID(rs.getString(COL_ID)));
						row.setOrderId(new GUID(rs.getString(COL_ORDER_ID)));
						row.setLineNumber(rs.getInt(COL_LINE_NUMBER));
						row.setItemId(new GUID(rs.getString(COL_ITEM_ID)));
						row.setQty(rs.getInt(COL_QTY));
						row.setPrice(rs.getDouble(COL_PRICE));
						row.setTaxable(rs.getInt(COL_TAXABLE));
						row.clean();
						return row;
					}
				}, orderId, lineNumber);

		return items.size() > 0 ? items.get(0) : null;
	}

	/**
	 * Returns all the order line items of an order in the system.
	 *
	 * @return the List of OrderLineItemDataRow object for the given orderId.
	 */
	public List<OrderLineItemDataRow> findAll(String orderId) {
		String query = SQL_SelectAllColumns +
				" WHERE " + COL_ORDER_ID + " = ?";

		return getJdbcTemplate().query(query,
				new RowMapper<OrderLineItemDataRow>() {
					public OrderLineItemDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
						OrderLineItemDataRow row = new OrderLineItemDataRow();
						row.setId(new GUID(rs.getString(COL_ID)));
						row.setOrderId(new GUID(rs.getString(COL_ORDER_ID)));
						row.setLineNumber(rs.getInt(COL_LINE_NUMBER));
						row.setItemId(new GUID(rs.getString(COL_ITEM_ID)));
						row.setQty(rs.getInt(COL_QTY));
						row.setPrice(rs.getDouble(COL_PRICE));
						row.setTaxable(rs.getInt(COL_TAXABLE));
						row.clean();
						return row;
					}
				}, orderId);
	}


	public int insert(OrderLineItemDataRow row) throws ScalarException {
		StringBuilder query = new StringBuilder();
		String sep = "";
		query.append("insert into " + TABLE_NAME + " (");
		query.append(sep).append(COL_ID);
		sep = ",";
		query.append(sep).append(COL_ORDER_ID);
		query.append(sep).append(COL_LINE_NUMBER);
		query.append(sep).append(COL_ITEM_ID);
		query.append(sep).append(COL_QTY);
		query.append(sep).append(COL_PRICE);
		query.append(sep).append(COL_TAXABLE);
		query.append(") values (?, ?, ?, ?, ?, ?, ?)");

		return getJdbcTemplate().update(query.toString(),
				row.getId().toString(),
				row.getOrderId(),
				row.getLineNumber(),
				row.getItemId().toString(),
				row.getQty(),
				row.getPrice(),
				row.getTaxable()
		);
	}

	public int update(OrderLineItemDataRow row) throws ScalarException {
		StringBuilder query = new StringBuilder();
		String sep = "";
		query.append("update " + TABLE_NAME + " set ");
		if (row.modOrderId()) {
			query.append(sep).append(COL_ORDER_ID).append(" = ?");
			sep = ",";
		}
		if (row.modLineNumber()) {
			query.append(sep).append(COL_LINE_NUMBER).append(" = ?");
			sep = ",";
		}
		if (row.modItemId()) {
			query.append(sep).append(COL_ITEM_ID).append(" = ?");
			sep = ",";
		}
		if (row.modQty()) {
			query.append(sep).append(COL_QTY).append(" = ?");
			sep = ",";
		}
		if (row.modPrice()) {
			query.append(sep).append(COL_PRICE).append(" = ?");
			sep = ",";
		}
		if (row.modTaxable()) {
			query.append(sep).append(COL_TAXABLE).append(" = ?");
			sep = ",";
		}
		query.append(" where ").append(COL_ID).append(" = ?");

		List<Object> args = new ArrayList<Object>();
		List<Integer> argTypes = new ArrayList<Integer>();
		if (row.modOrderId()) {
			args.add(row.getOrderId());
			argTypes.add(COL_SQL_TYPES.get(COL_ORDER_ID));
		}
		if (row.modLineNumber()) {
			args.add(row.getLineNumber());
			argTypes.add(COL_SQL_TYPES.get(COL_LINE_NUMBER));
		}
		if (row.modItemId()) {
			args.add(row.getItemId().toString());
			argTypes.add(COL_SQL_TYPES.get(COL_ITEM_ID));
		}
		if (row.modQty()) {
			args.add(row.getQty());
			argTypes.add(COL_SQL_TYPES.get(COL_QTY));
		}
		if (row.modPrice()) {
			args.add(row.getPrice());
			argTypes.add(COL_SQL_TYPES.get(COL_PRICE));
		}
		if (row.modTaxable()) {
			args.add(row.getTaxable());
			argTypes.add(COL_SQL_TYPES.get(COL_TAXABLE));
		}
		args.add(row.getId());
		argTypes.add(COL_SQL_TYPES.get(COL_ID));

		return getJdbcTemplate().update(query.toString(), args.toArray(new Object[args.size()]), ArrayUtils.toPrimitive(argTypes.toArray(new Integer[argTypes.size()])));
	}

	public int removeByOrderId(String orderId) {
		String query = "delete from " + TABLE_NAME + " where " + COL_ORDER_ID + " = ?";

		return getJdbcTemplate().update(query, orderId);
	}

	public int removeByLineNumber(String orderId, int lineNumber) {
		String query = "delete from " + TABLE_NAME + " where " + COL_ORDER_ID + " = ? and " + COL_LINE_NUMBER + " = ?";

		return getJdbcTemplate().update(query, orderId, lineNumber);
	}

	public int removeById(String id) {
		String query = "delete from " + TABLE_NAME + " where " + COL_ID + " = ?";

		return getJdbcTemplate().update(query, id);
	}

	public static OrderLineItemData rowToData(OrderLineItemDataRow row) {
		OrderLineItemData orderLineItemData = new OrderLineItemData();
		orderLineItemData.setId(row.getId().toString());
		orderLineItemData.setOrderId(row.getOrderId().toString());
		orderLineItemData.setLineNumber(row.getLineNumber());
		orderLineItemData.setItemId(row.getItemId().toString());
		orderLineItemData.setQty(row.getQty());
		orderLineItemData.setPrice(row.getPrice());
		orderLineItemData.setTaxable(row.getTaxable()!=0);

		return orderLineItemData;
	}

	public static OrderLineItemDataRow dataToRow(OrderLineItemData orderLineItemData) {
		OrderLineItemDataRow row = new OrderLineItemDataRow();
		row.setId(new GUID(orderLineItemData.getId()));
		row.setOrderId(new GUID(orderLineItemData.getOrderId()));
		row.setLineNumber(orderLineItemData.getLineNumber());
		row.setItemId(new GUID(orderLineItemData.getItemId()));
		row.setQty(orderLineItemData.getQty());
		row.setPrice(orderLineItemData.getPrice());
		row.setTaxable(orderLineItemData.isTaxable()?1:0);

		return row;
	}
}