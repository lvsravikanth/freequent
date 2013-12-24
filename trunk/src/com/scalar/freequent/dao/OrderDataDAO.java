package com.scalar.freequent.dao;

import com.scalar.core.ScalarException;
import com.scalar.core.jdbc.AbstractDAO;
import com.scalar.core.util.GUID;
import com.scalar.freequent.common.ObjectType;
import com.scalar.freequent.common.OrderData;
import com.scalar.freequent.util.StringUtil;
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
public class OrderDataDAO extends AbstractDAO {
	protected static final Log logger = LogFactory.getLog(OrderDataDAO.class);
	public static final String TABLE_NAME = "ftOrder";
	public static final String COL_ID = "id";
	public static final String COL_ORDER_NUMBER = "orderNumber";
	public static final String COL_ORDER_DATE = "orderDate";
	public static final String COL_CUST_NAME = "custName";
	public static final String COL_STATUS = "status";
	public static final String COL_REVISION = "revision";
	public static final String COL_REMARKS = "remarks";
	public static final String COL_TAX_PERCENTAGE = "taxPercentage";
	public static final String COL_DISCOUNT = "discount";

	static final String SQL_SelectAllColumns =
			"select " +
					COL_ID + ", " +
					COL_ORDER_NUMBER + ", " +
					COL_ORDER_DATE + ", " +
					COL_CUST_NAME + ", " +
					COL_STATUS + ", " +
					COL_REVISION + ", " +
					COL_TAX_PERCENTAGE + ", " +
					COL_DISCOUNT + ", " +
					COL_REMARKS +
					" from " + TABLE_NAME + " ";

	public static final Map<String, Integer> COL_SQL_TYPES = new HashMap<String, Integer>();

	static {
		COL_SQL_TYPES.put(COL_ID, Types.VARCHAR);
		COL_SQL_TYPES.put(COL_ORDER_NUMBER, Types.VARCHAR);
		COL_SQL_TYPES.put(COL_ORDER_DATE, Types.TIMESTAMP);
		COL_SQL_TYPES.put(COL_CUST_NAME, Types.VARCHAR);
		COL_SQL_TYPES.put(COL_STATUS, Types.VARCHAR);
		COL_SQL_TYPES.put(COL_REVISION, Types.INTEGER);
		COL_SQL_TYPES.put(COL_REMARKS, Types.VARCHAR);
		COL_SQL_TYPES.put(COL_TAX_PERCENTAGE, Types.DOUBLE);
		COL_SQL_TYPES.put(COL_DISCOUNT, Types.DOUBLE);
	}

	public boolean existsByOrderNumber(String orderNumber) {
		String query = "SELECT COUNT(*) FROM " + TABLE_NAME
				+ " WHERE " + COL_ORDER_NUMBER + " = ? ";
		return getJdbcTemplate().queryForInt(query, orderNumber) != 0;
	}

	/**
	 * Returns the User object for the given userId.
	 *
	 * @param id
	 * @return the User object for the given userId.
	 */
	public OrderDataRow findByPrimaryKey(String id) {
		String query = SQL_SelectAllColumns +
				" WHERE " + COL_ID + " = ? ";
		List<OrderDataRow> items = getJdbcTemplate().query(query,
				new RowMapper<OrderDataRow>() {
					public OrderDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
						OrderDataRow row = new OrderDataRow();
						row.setId(new GUID(rs.getString(COL_ID)));
						row.setOrderNumber(rs.getString(COL_ORDER_NUMBER));
						row.setOrderDate(rs.getTimestamp(COL_ORDER_DATE));
						row.setCustName(rs.getString(COL_CUST_NAME));
						row.setStatus(rs.getString(COL_STATUS));
						row.setRevision(rs.getInt(COL_REVISION));
						row.setTaxPercentage(rs.getDouble(COL_TAX_PERCENTAGE));
						row.setDiscount(rs.getDouble(COL_DISCOUNT));
						row.setRemarks(rs.getString(COL_REMARKS));
						row.clean();
						return row;
					}
				}, id);

		return items.isEmpty() ? null : items.get(0);
	}

	/**
	 * Returns the OrderDataRow object for the given itemName.
	 *
	 * @param orderNumber
	 * @return the User object for the given userId.
	 */
	public OrderDataRow findByOrderNumber(String orderNumber) {
		String query = SQL_SelectAllColumns +
				" WHERE " + COL_ORDER_NUMBER + " = ? ";
		List<OrderDataRow> items = getJdbcTemplate().query(query,
				new RowMapper<OrderDataRow>() {
					public OrderDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
						OrderDataRow row = new OrderDataRow();
						row.setId(new GUID(rs.getString(COL_ID)));
						row.setOrderNumber(rs.getString(COL_ORDER_NUMBER));
						row.setOrderDate(rs.getTimestamp(COL_ORDER_DATE));
						row.setCustName(rs.getString(COL_CUST_NAME));
						row.setStatus(rs.getString(COL_STATUS));
						row.setRevision(rs.getInt(COL_REVISION));
						row.setTaxPercentage(rs.getDouble(COL_TAX_PERCENTAGE));
						row.setDiscount(rs.getDouble(COL_DISCOUNT));
						row.setRemarks(rs.getString(COL_REMARKS));
						row.clean();
						return row;
					}
				}, orderNumber);

		return items.size() > 0 ? items.get(0) : null;
	}

	/**
	 * Returns all the users in the system.
	 *
	 * @return the User object for the given userId.
	 */
	public List<OrderDataRow> findAll() {
		String query = SQL_SelectAllColumns;

		return getJdbcTemplate().query(query,
				new RowMapper<OrderDataRow>() {
					public OrderDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
						OrderDataRow row = new OrderDataRow();
						row.setId(new GUID(rs.getString(COL_ID)));
						row.setOrderNumber(rs.getString(COL_ORDER_NUMBER));
						row.setOrderDate(rs.getTimestamp(COL_ORDER_DATE));
						row.setCustName(rs.getString(COL_CUST_NAME));
						row.setStatus(rs.getString(COL_STATUS));
						row.setRevision(rs.getInt(COL_REVISION));
						row.setTaxPercentage(rs.getDouble(COL_TAX_PERCENTAGE));
						row.setDiscount(rs.getDouble(COL_DISCOUNT));
						row.setRemarks(rs.getString(COL_REMARKS));
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
	public List<OrderDataRow> search(Map<String, Object> params) {
		String query = SQL_SelectAllColumns;
		StringBuilder whereCaluse = new StringBuilder();
		List<Object> args = new ArrayList<Object>();
		boolean joinOrderLineItemTable = false;

		String orderNumber = (String)params.get(OrderData.PARAM_ORDER_NUMBER);
		Object itemId = params.get(OrderData.PARAM_ITEM_ID);
		Object fromDate = params.get(OrderData.PARAM_FROM_DATE);
		Object toDate = params.get(OrderData.PARAM_TO_DATE);

		String AND = " ";
		final String LIKE = " like ";
		final String EQUAL_TO = " = ";
		final String AND_STR = " AND ";
		if (!StringUtil.isEmpty(orderNumber)) {
			orderNumber += "%";
			whereCaluse.append(AND).append(COL_ORDER_NUMBER).append(LIKE).append("?");
			args.add(orderNumber);
			AND = AND_STR;
		}

		if (!StringUtil.isEmpty(fromDate)) {
			whereCaluse.append(AND).append(COL_ORDER_DATE).append(">=").append("?");
			args.add(fromDate);
			AND = AND_STR;
		}

		if (!StringUtil.isEmpty(toDate)) {
			whereCaluse.append(AND).append(COL_ORDER_DATE).append("<=").append("?");
			args.add(toDate);
			AND = AND_STR;
		}

		// todo
		/*if (!StringUtil.isEmpty(itemId)) {
			whereCaluse.append(AND).append(CategoryAssocDataDAO.TABLE_NAME + "." + CategoryAssocDataDAO.COL_CATEGORY_ID).append(EQUAL_TO).append("?");
			args.add(category);
			AND = AND_STR;
			joinOrderLineItemTable = true;
		}*/

		/*if (joinOrderLineItemTable) {
			query = query + ", " + CategoryAssocDataDAO.TABLE_NAME;
		}*/
		if (whereCaluse.length() > 0) {
			query = query + " WHERE " + whereCaluse.toString();
		}
		/*if (joinOrderLineItemTable) {
			query = query + AND + CategoryAssocDataDAO.COL_OBJECT_ID + EQUAL_TO + COL_ID;
		}*/

		return getJdbcTemplate().query(query, args.toArray(new Object[args.size()]),
				new RowMapper<OrderDataRow>() {
					public OrderDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
						OrderDataRow row = new OrderDataRow();
						row.setId(new GUID(rs.getString(COL_ID)));
						row.setOrderNumber(rs.getString(COL_ORDER_NUMBER));
						row.setOrderDate(rs.getTimestamp(COL_ORDER_DATE));
						row.setCustName(rs.getString(COL_CUST_NAME));
						row.setStatus(rs.getString(COL_STATUS));
						row.setRevision(rs.getInt(COL_REVISION));
						row.setTaxPercentage(rs.getDouble(COL_TAX_PERCENTAGE));
						row.setDiscount(rs.getDouble(COL_DISCOUNT));
						row.setRemarks(rs.getString(COL_REMARKS));
						row.clean();
						return row;
					}
				});
	}

	public int insert(OrderDataRow row) throws ScalarException {
		insertRecord(row.getId(), ObjectType.ORDER);
		StringBuilder query = new StringBuilder();
		String sep = "";
		query.append("insert into " + TABLE_NAME + " (");
		query.append(sep).append(COL_ID);
		sep = ",";
		query.append(sep).append(COL_ORDER_NUMBER);
		query.append(sep).append(COL_ORDER_DATE);
		query.append(sep).append(COL_CUST_NAME);
		query.append(sep).append(COL_STATUS);
		query.append(sep).append(COL_REVISION);
		query.append(sep).append(COL_TAX_PERCENTAGE);
		query.append(sep).append(COL_DISCOUNT);
		query.append(sep).append(COL_REMARKS);
		query.append(") values (?, ?, ?, ?, ?, ?, ?, ?, ?)");

		return getJdbcTemplate().update(query.toString(),
				row.getId().toString(),
				row.getOrderNumber(),
				row.getOrderDate(),
				row.getCustName(),
				row.getStatus(),
				row.getRevision(),
				row.getTaxPercentage(),
				row.getDiscount(),
				row.getRemarks()
		);
	}

	public int update(OrderDataRow row) throws ScalarException {
		updateRecord(row.getId());
		StringBuilder query = new StringBuilder();
		String sep = "";
		query.append("update " + TABLE_NAME + " set ");
		if (row.modOrderNumber()) {
			query.append(sep).append(COL_ORDER_NUMBER).append(" = ?");
			sep = ",";
		}
		if (row.modOrderDate()) {
			query.append(sep).append(COL_ORDER_DATE).append(" = ?");
			sep = ",";
		}
		if (row.modCustName()) {
			query.append(sep).append(COL_CUST_NAME).append(" = ?");
			sep = ",";
		}
		if (row.modStatus()) {
			query.append(sep).append(COL_STATUS).append(" = ?");
			sep = ",";
		}
		if (row.modRevision()) {
			query.append(sep).append(COL_REVISION).append(" = ?");
			sep = ",";
		}
		if (row.modTaxPercentage()) {
			query.append(sep).append(COL_TAX_PERCENTAGE).append(" = ?");
			sep = ",";
		}
		if (row.modDiscount()) {
			query.append(sep).append(COL_DISCOUNT).append(" = ?");
			sep = ",";
		}
		if (row.modRemarks()) {
			query.append(sep).append(COL_REMARKS).append(" = ?");
			sep = ",";
		}
		query.append(" where ").append(COL_ID).append(" = ?");

		List<Object> args = new ArrayList<Object>();
		List<Integer> argTypes = new ArrayList<Integer>();
		if (row.modOrderNumber()) {
			args.add(row.getOrderNumber());
			argTypes.add(COL_SQL_TYPES.get(COL_ORDER_NUMBER));
		}
		if (row.modOrderDate()) {
			args.add(row.getOrderDate());
			argTypes.add(COL_SQL_TYPES.get(COL_ORDER_DATE));
		}
		if (row.modCustName()) {
			args.add(row.getCustName());
			argTypes.add(COL_SQL_TYPES.get(COL_CUST_NAME));
		}
		if (row.modStatus()) {
			args.add(row.getStatus().toString());
			argTypes.add(COL_SQL_TYPES.get(COL_STATUS));
		}
		if (row.modRevision()) {
			args.add(row.getRevision());
			argTypes.add(COL_SQL_TYPES.get(COL_REVISION));
		}
		if (row.modTaxPercentage()) {
			args.add(row.getTaxPercentage());
			argTypes.add(COL_SQL_TYPES.get(COL_TAX_PERCENTAGE));
		}
		if (row.modDiscount()) {
			args.add(row.getDiscount());
			argTypes.add(COL_SQL_TYPES.get(COL_DISCOUNT));
		}
		if (row.modRemarks()) {
			args.add(row.getRemarks());
			argTypes.add(COL_SQL_TYPES.get(COL_REMARKS));
		}
		args.add(row.getId());
		argTypes.add(COL_SQL_TYPES.get(COL_ID));

		return getJdbcTemplate().update(query.toString(), args.toArray(new Object[args.size()]), ArrayUtils.toPrimitive(argTypes.toArray(new Integer[argTypes.size()])));
	}

	public int removeByOrderNumber(String orderNumber) {
		String query = "delete from " + TABLE_NAME + " where " + COL_ORDER_NUMBER + " = ?";

		return getJdbcTemplate().update(query, orderNumber);
	}

	public int removeById(String id) {
		String query = "delete from " + TABLE_NAME + " where " + COL_ID + " = ?";

		return getJdbcTemplate().update(query, id);
	}

	public long getOrdersCount() {
		String query = "select count(*) from " + TABLE_NAME;
		return getJdbcTemplate().queryForLong(query);
	}

	public static OrderData rowToData(OrderDataRow row) {
		OrderData orderData = new OrderData();
		orderData.setId(row.getId().toString());
		orderData.setOrderNumber(row.getOrderNumber());
		orderData.setOrderDate(row.getOrderDate());
		orderData.setCustName(row.getCustName());
		orderData.setStatus(OrderData.OrderStatus.getInstance(row.getStatus()));
		orderData.setRevision(row.getRevision());
		orderData.setRemarks(row.getRemarks());
		orderData.setTaxPercentage(row.getTaxPercentage());
		orderData.setDiscount(row.getDiscount());

		return orderData;
	}

	public static OrderDataRow dataToRow(OrderData orderData) {
		OrderDataRow row = new OrderDataRow();
		row.setId(new GUID(orderData.getId()));
		row.setOrderNumber(orderData.getOrderNumber());
		row.setOrderDate(orderData.getOrderDate());
		row.setCustName(orderData.getCustName());
		row.setStatus(orderData.getStatus().toString());
		row.setRevision(orderData.getRevision());
		row.setRemarks(orderData.getRemarks());
		row.setTaxPercentage(orderData.getTaxPercentage());
		row.setDiscount(orderData.getDiscount());

		return row;
	}
}