package com.scalar.freequent.dao;

import com.scalar.core.ScalarException;
import com.scalar.core.jdbc.AbstractDAO;
import com.scalar.core.util.GUID;
import com.scalar.freequent.common.ObjectType;
import com.scalar.freequent.common.OrderData;
import com.scalar.freequent.common.InvoiceData;
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
 * Date: Dec 27, 2013
 * Time: 8:59:05 PM
 */
public class InvoiceDataDAO extends AbstractDAO {
	protected static final Log logger = LogFactory.getLog(InvoiceDataDAO.class);
	public static final String TABLE_NAME = "ftInvoice";
	public static final String COL_ID = "id";
	public static final String COL_ORDER_ID = "orderId";
	public static final String COL_INVOICE_NUMBER = "invoiceNumber";
	public static final String COL_INVOICE_DATE = "date";
	public static final String COL_CUST_NAME = "custName";
	public static final String COL_STATUS = "status";
	public static final String COL_REMARKS = "remarks";
	public static final String COL_TAX_NAME = "taxName";
	public static final String COL_TAX_PERCENTAGE = "taxPercentage";
	public static final String COL_DISCOUNT = "discount";

	static final String SQL_SelectAllColumns =
			"select " +
					COL_ID + ", " +
					COL_ORDER_ID + ", " +
					COL_INVOICE_NUMBER + ", " +
					COL_INVOICE_DATE + ", " +
					COL_CUST_NAME + ", " +
					COL_STATUS + ", " +
					COL_TAX_NAME + ", " +
					COL_TAX_PERCENTAGE + ", " +
					COL_DISCOUNT + ", " +
					COL_REMARKS +
					" from " + TABLE_NAME + " ";

	public static final Map<String, Integer> COL_SQL_TYPES = new HashMap<String, Integer>();

	static {
		COL_SQL_TYPES.put(COL_ID, Types.VARCHAR);
		COL_SQL_TYPES.put(COL_ORDER_ID, Types.VARCHAR);
		COL_SQL_TYPES.put(COL_INVOICE_NUMBER, Types.VARCHAR);
		COL_SQL_TYPES.put(COL_INVOICE_DATE, Types.TIMESTAMP);
		COL_SQL_TYPES.put(COL_CUST_NAME, Types.VARCHAR);
		COL_SQL_TYPES.put(COL_STATUS, Types.VARCHAR);
		COL_SQL_TYPES.put(COL_REMARKS, Types.VARCHAR);
		COL_SQL_TYPES.put(COL_TAX_PERCENTAGE, Types.DOUBLE);
		COL_SQL_TYPES.put(COL_DISCOUNT, Types.DOUBLE);
	}

	public boolean existsByInvoiceNumber(String invoiceNumber) {
		String query = "SELECT COUNT(*) FROM " + TABLE_NAME
				+ " WHERE " + COL_INVOICE_NUMBER + " = ? ";
		return getJdbcTemplate().queryForInt(query, invoiceNumber) != 0;
	}

	public boolean existsByOrderId(String orderId) {
		String query = "SELECT COUNT(*) FROM " + TABLE_NAME
				+ " WHERE " + COL_ORDER_ID + " = ? ";
		return getJdbcTemplate().queryForInt(query, orderId) != 0;
	}

	/**
	 * Returns the InvoiceDataRow object for the given invoice id.
	 *
	 * @param id invoice id.
	 * @return the InvoiceDataRow object for the given userId.
	 */
	public InvoiceDataRow findByPrimaryKey(String id) {
		String query = SQL_SelectAllColumns +
				" WHERE " + COL_ID + " = ? ";
		List<InvoiceDataRow> invoices = getJdbcTemplate().query(query,
				new RowMapper<InvoiceDataRow>() {
					public InvoiceDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
						InvoiceDataRow row = new InvoiceDataRow();
						row.setId(new GUID(rs.getString(COL_ID)));
						row.setOrderId(new GUID(rs.getString(COL_ORDER_ID)));
						row.setInvoiceNumber(rs.getString(COL_INVOICE_NUMBER));
						row.setInvoiceDate(rs.getTimestamp(COL_INVOICE_DATE));
						row.setCustName(rs.getString(COL_CUST_NAME));
						row.setStatus(rs.getString(COL_STATUS));
						row.setTaxName(rs.getString(COL_TAX_NAME));
						row.setTaxPercentage(rs.getDouble(COL_TAX_PERCENTAGE));
						row.setDiscount(rs.getDouble(COL_DISCOUNT));
						row.setRemarks(rs.getString(COL_REMARKS));
						row.clean();
						return row;
					}
				}, id);

		return invoices.isEmpty() ? null : invoices.get(0);
	}

	/**
	 * Returns the InvoiceDataRow object for the given invoice number.
	 *
	 * @param invoiceNumber
	 * @return the InvoiceDataRow object for the given invoice number.
	 */
	public InvoiceDataRow findByInvoiceNumber(String invoiceNumber) {
		String query = SQL_SelectAllColumns +
				" WHERE " + COL_INVOICE_NUMBER + " = ? ";
		List<InvoiceDataRow> invoices = getJdbcTemplate().query(query,
				new RowMapper<InvoiceDataRow>() {
					public InvoiceDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
						InvoiceDataRow row = new InvoiceDataRow();
						row.setId(new GUID(rs.getString(COL_ID)));
						row.setOrderId(new GUID(rs.getString(COL_ORDER_ID)));
						row.setInvoiceNumber(rs.getString(COL_INVOICE_NUMBER));
						row.setInvoiceDate(rs.getTimestamp(COL_INVOICE_DATE));
						row.setCustName(rs.getString(COL_CUST_NAME));
						row.setStatus(rs.getString(COL_STATUS));
						row.setTaxName(rs.getString(COL_TAX_NAME));
						row.setTaxPercentage(rs.getDouble(COL_TAX_PERCENTAGE));
						row.setDiscount(rs.getDouble(COL_DISCOUNT));
						row.setRemarks(rs.getString(COL_REMARKS));
						row.clean();
						return row;
					}
				}, invoiceNumber);

		return invoices.size() > 0 ? invoices.get(0) : null;
	}

	/**
	 * Returns the list of InvoiceDataRow objects for the given order id.
	 *
	 * @param orderId order id.
	 * @return the list of InvoiceDataRow object for the given order id.
	 */
	public List<InvoiceDataRow> findByOrderId(String orderId) {
		String query = SQL_SelectAllColumns +
				" WHERE " + COL_ORDER_ID + " = ? ";
		List<InvoiceDataRow> invoices = getJdbcTemplate().query(query,
				new RowMapper<InvoiceDataRow>() {
					public InvoiceDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
						InvoiceDataRow row = new InvoiceDataRow();
						row.setId(new GUID(rs.getString(COL_ID)));
						row.setOrderId(new GUID(rs.getString(COL_ORDER_ID)));
						row.setInvoiceNumber(rs.getString(COL_INVOICE_NUMBER));
						row.setInvoiceDate(rs.getTimestamp(COL_INVOICE_DATE));
						row.setCustName(rs.getString(COL_CUST_NAME));
						row.setStatus(rs.getString(COL_STATUS));
						row.setTaxName(rs.getString(COL_TAX_NAME));
						row.setTaxPercentage(rs.getDouble(COL_TAX_PERCENTAGE));
						row.setDiscount(rs.getDouble(COL_DISCOUNT));
						row.setRemarks(rs.getString(COL_REMARKS));
						row.clean();
						return row;
					}
				}, orderId);

		return invoices;
	}

	/**
	 * Returns all the users in the system.
	 *
	 * @return the User object for the given userId.
	 */
	public List<InvoiceDataRow> findAll() {
		String query = SQL_SelectAllColumns;

		return getJdbcTemplate().query(query,
				new RowMapper<InvoiceDataRow>() {
					public InvoiceDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
						InvoiceDataRow row = new InvoiceDataRow();
						row.setId(new GUID(rs.getString(COL_ID)));
						row.setOrderId(new GUID(rs.getString(COL_ORDER_ID)));
						row.setInvoiceNumber(rs.getString(COL_INVOICE_NUMBER));
						row.setInvoiceDate(rs.getTimestamp(COL_INVOICE_DATE));
						row.setCustName(rs.getString(COL_CUST_NAME));
						row.setStatus(rs.getString(COL_STATUS));
						row.setTaxName(rs.getString(COL_TAX_NAME));
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
	 * @param params
	 * @return the User object for the given userId.
	 */
	public List<InvoiceDataRow> search(Map<String, Object> params) {
		String query = "select distinct " +
					TABLE_NAME + "." + COL_ID + ", " +
					TABLE_NAME + "." + COL_ORDER_ID + ", " +
					TABLE_NAME + "." + COL_INVOICE_NUMBER + ", " +
					TABLE_NAME + "." + COL_INVOICE_DATE + ", " +
					TABLE_NAME + "." + COL_CUST_NAME + ", " +
					TABLE_NAME + "." + COL_STATUS + ", " +
					TABLE_NAME + "." + COL_TAX_NAME + ", " +
					TABLE_NAME + "." + COL_TAX_PERCENTAGE + ", " +
					TABLE_NAME + "." + COL_DISCOUNT + ", " +
					TABLE_NAME + "." + COL_REMARKS +
					" from " + TABLE_NAME + " ";
		StringBuilder whereCaluse = new StringBuilder();
		List<Object> args = new ArrayList<Object>();
		boolean joinInvoiceLineItemTable = false;

		String invoiceNumber = (String)params.get(InvoiceData.PARAM_INVOICE_NUMBER);
		Object orderId = params.get(InvoiceData.PARAM_ORDER_ID);
		Object itemId = params.get(InvoiceData.PARAM_ITEM_ID);
		String status = (String)params.get(InvoiceData.PARAM_STATUS);
		Object fromDate = params.get(InvoiceData.PARAM_FROM_DATE);
		Object toDate = params.get(InvoiceData.PARAM_TO_DATE);

		String AND = " ";
		final String LIKE = " like ";
		final String EQUAL_TO = " = ";
		final String AND_STR = " AND ";
		if (!StringUtil.isEmpty(invoiceNumber)) {
			invoiceNumber += "%";
			whereCaluse.append(AND).append(COL_INVOICE_NUMBER).append(LIKE).append("?");
			args.add(invoiceNumber);
			AND = AND_STR;
		}

		if (!StringUtil.isEmpty(status)) {
			invoiceNumber += "%";
			whereCaluse.append(AND).append(COL_STATUS).append(EQUAL_TO).append("?");
			args.add(status);
			AND = AND_STR;
		}

		if (!StringUtil.isEmpty(fromDate)) {
			whereCaluse.append(AND).append(COL_INVOICE_DATE).append(">=").append("?");
			args.add(fromDate);
			AND = AND_STR;
		}

		if (!StringUtil.isEmpty(toDate)) {
			whereCaluse.append(AND).append(COL_INVOICE_DATE).append("<=").append("?");
			args.add(toDate);
			AND = AND_STR;
		}

		if (!StringUtil.isEmpty(itemId)) {
			whereCaluse.append(AND).append(InvoiceLineItemDataDAO.TABLE_NAME + "." + InvoiceLineItemDataDAO.COL_ITEM_ID).append(EQUAL_TO).append("?");
			args.add(itemId);
			AND = AND_STR;
			joinInvoiceLineItemTable = true;
		}

		if (joinInvoiceLineItemTable) {
			query = query + ", " + InvoiceLineItemDataDAO.TABLE_NAME;
		}
		if (whereCaluse.length() > 0) {
			query = query + " WHERE " + whereCaluse.toString();
		}
		if (joinInvoiceLineItemTable) {
			query = query + AND + InvoiceLineItemDataDAO.TABLE_NAME + "." + InvoiceLineItemDataDAO.COL_INVOICE_ID + EQUAL_TO + TABLE_NAME + "." + COL_ID;
		}

		return getJdbcTemplate().query(query, args.toArray(new Object[args.size()]),
				new RowMapper<InvoiceDataRow>() {
					public InvoiceDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
						InvoiceDataRow row = new InvoiceDataRow();
						row.setId(new GUID(rs.getString(COL_ID)));
						row.setOrderId(new GUID(rs.getString(COL_ORDER_ID)));
						row.setInvoiceNumber(rs.getString(COL_INVOICE_NUMBER));
						row.setInvoiceDate(rs.getTimestamp(COL_INVOICE_DATE));
						row.setCustName(rs.getString(COL_CUST_NAME));
						row.setStatus(rs.getString(COL_STATUS));
						row.setTaxName(rs.getString(COL_TAX_NAME));
						row.setTaxPercentage(rs.getDouble(COL_TAX_PERCENTAGE));
						row.setDiscount(rs.getDouble(COL_DISCOUNT));
						row.setRemarks(rs.getString(COL_REMARKS));
						row.clean();
						return row;
					}
				});
	}

	public int insert(InvoiceDataRow row) throws ScalarException {
		insertRecord(row.getId(), ObjectType.ORDER);
		StringBuilder query = new StringBuilder();
		String sep = "";
		query.append("insert into " + TABLE_NAME + " (");
		query.append(sep).append(COL_ID);
		sep = ",";
		query.append(sep).append(COL_ORDER_ID);
		query.append(sep).append(COL_INVOICE_NUMBER);
		query.append(sep).append(COL_INVOICE_DATE);
		query.append(sep).append(COL_CUST_NAME);
		query.append(sep).append(COL_STATUS);
		query.append(sep).append(COL_TAX_NAME);
		query.append(sep).append(COL_TAX_PERCENTAGE);
		query.append(sep).append(COL_DISCOUNT);
		query.append(sep).append(COL_REMARKS);
		query.append(") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		return getJdbcTemplate().update(query.toString(),
				row.getId().toString(),
				row.getOrderId().toString(),
				row.getInvoiceNumber(),
				row.getInvoiceDate(),
				row.getCustName(),
				row.getStatus(),
				row.getTaxName(),
				row.getTaxPercentage(),
				row.getDiscount(),
				row.getRemarks()
		);
	}

	public int update(InvoiceDataRow row) throws ScalarException {
		updateRecord(row.getId());
		StringBuilder query = new StringBuilder();
		String sep = "";
		query.append("update " + TABLE_NAME + " set ");
		if (row.modOrderId()) {
			query.append(sep).append(COL_ORDER_ID).append(" = ?");
			sep = ",";
		}
		if (row.modInvoiceNumber()) {
			query.append(sep).append(COL_INVOICE_NUMBER).append(" = ?");
			sep = ",";
		}
		if (row.modInvoiceDate()) {
			query.append(sep).append(COL_INVOICE_DATE).append(" = ?");
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
		if (row.modTaxName()) {
			query.append(sep).append(COL_TAX_NAME).append(" = ?");
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
		if (row.modOrderId()) {
			args.add(row.getOrderId().toString());
			argTypes.add(COL_SQL_TYPES.get(COL_ORDER_ID));
		}
		if (row.modInvoiceNumber()) {
			args.add(row.getInvoiceNumber());
			argTypes.add(COL_SQL_TYPES.get(COL_INVOICE_NUMBER));
		}
		if (row.modInvoiceDate()) {
			args.add(row.getInvoiceDate());
			argTypes.add(COL_SQL_TYPES.get(COL_INVOICE_DATE));
		}
		if (row.modCustName()) {
			args.add(row.getCustName());
			argTypes.add(COL_SQL_TYPES.get(COL_CUST_NAME));
		}
		if (row.modStatus()) {
			args.add(row.getStatus());
			argTypes.add(COL_SQL_TYPES.get(COL_STATUS));
		}
		if (row.modTaxName()) {
			args.add(row.getTaxName());
			argTypes.add(COL_SQL_TYPES.get(COL_TAX_NAME));
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

	public int removeByInvoiceNumber(String invoiceNumber) {
		String query = "delete from " + TABLE_NAME + " where " + COL_INVOICE_NUMBER + " = ?";

		return getJdbcTemplate().update(query, invoiceNumber);
	}

	public int removeById(String id) {
		String query = "delete from " + TABLE_NAME + " where " + COL_ID + " = ?";

		return getJdbcTemplate().update(query, id);
	}

	public long getInvoicesCount() {
		String query = "select count(*) from " + TABLE_NAME;
		return getJdbcTemplate().queryForLong(query);
	}

	public static InvoiceData rowToData(InvoiceDataRow row) {
		InvoiceData invoiceData = new InvoiceData();
		invoiceData.setId(row.getId().toString());
		invoiceData.setOrderId(row.getOrderId().toString());
		invoiceData.setInvoiceNumber(row.getInvoiceNumber());
		invoiceData.setDate(row.getInvoiceDate());
		invoiceData.setCustName(row.getCustName());
		invoiceData.setStatus(InvoiceData.InvoiceStatus.getInstance(row.getStatus()));
		invoiceData.setRemarks(row.getRemarks());
		invoiceData.setTaxName(row.getTaxName());
		invoiceData.setTaxPercentage(row.getTaxPercentage());
		invoiceData.setDiscount(row.getDiscount());

		return invoiceData;
	}

	public static InvoiceDataRow dataToRow(InvoiceData invoiceData) {
		InvoiceDataRow row = new InvoiceDataRow();
		row.setId(new GUID(invoiceData.getId()));
		row.setOrderId(new GUID(invoiceData.getOrderId()));
		row.setInvoiceNumber(invoiceData.getInvoiceNumber());
		row.setInvoiceDate(invoiceData.getDate());
		row.setCustName(invoiceData.getCustName());
		row.setStatus(invoiceData.getStatus().toString());
		row.setRemarks(invoiceData.getRemarks());
		row.setTaxName(invoiceData.getTaxName());
		row.setTaxPercentage(invoiceData.getTaxPercentage());
		row.setDiscount(invoiceData.getDiscount());

		return row;
	}
}