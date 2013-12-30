package com.scalar.freequent.dao;

import com.scalar.core.ScalarException;
import com.scalar.core.jdbc.AbstractDAO;
import com.scalar.core.util.GUID;
import com.scalar.freequent.common.OrderLineItemData;
import com.scalar.freequent.common.InvoiceLineItemData;
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
 * Time: 8:56:05 PM
 */
public class InvoiceLineItemDataDAO extends AbstractDAO {
	protected static final Log logger = LogFactory.getLog(InvoiceLineItemDataDAO.class);
	public static final String TABLE_NAME = "ftInvoiceLineItem";
	public static final String COL_ID = "id";
	public static final String COL_INVOICE_ID = "orderId";
	public static final String COL_LINE_NUMBER = "lineNumber";
	public static final String COL_ITEM_ID = "itemId";
	public static final String COL_ITEM_NAME = "itemName";
	public static final String COL_QTY = "qty";
	public static final String COL_PRICE = "price";
	public static final String COL_TAXABLE = "taxable";

	static final String SQL_SelectAllColumns =
			"select " +
					COL_ID + ", " +
					COL_INVOICE_ID + ", " +
					COL_LINE_NUMBER + ", " +
					COL_ITEM_ID + ", " +
					COL_ITEM_NAME + ", " +
					COL_QTY + ", " +
					COL_PRICE + ", " +
					COL_TAXABLE +
					" from " + TABLE_NAME + " ";

	public static final Map<String, Integer> COL_SQL_TYPES = new HashMap<String, Integer>();

	static {
		COL_SQL_TYPES.put(COL_ID, Types.VARCHAR);
		COL_SQL_TYPES.put(COL_INVOICE_ID, Types.VARCHAR);
		COL_SQL_TYPES.put(COL_LINE_NUMBER, Types.INTEGER);
		COL_SQL_TYPES.put(COL_ITEM_ID, Types.VARCHAR);
		COL_SQL_TYPES.put(COL_ITEM_NAME, Types.VARCHAR);
		COL_SQL_TYPES.put(COL_QTY, Types.INTEGER);
		COL_SQL_TYPES.put(COL_PRICE, Types.DOUBLE);
		COL_SQL_TYPES.put(COL_TAXABLE, Types.INTEGER);
	}

	public boolean existsByLineNumber(String invoiceId, int lineNumber) {
		String query = "SELECT COUNT(*) FROM " + TABLE_NAME
				+ " WHERE " + COL_INVOICE_ID + " = ? AND " + COL_LINE_NUMBER + " = ?";
		return getJdbcTemplate().queryForInt(query, invoiceId, lineNumber) != 0;
	}

	/**
	 * Returns the InvoiceLineItemDataRow object for the given line item id.
	 *
	 * @param id invoice line item id. 
	 * @return the InvoiceLineItemDataRow object for the given line item id.
	 */
	public InvoiceLineItemDataRow findByPrimaryKey(String id) {
		String query = SQL_SelectAllColumns +
				" WHERE " + COL_ID + " = ? ";
		List<InvoiceLineItemDataRow> lineItems = getJdbcTemplate().query(query,
				new RowMapper<InvoiceLineItemDataRow>() {
					public InvoiceLineItemDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
						InvoiceLineItemDataRow row = new InvoiceLineItemDataRow();
						row.setId(new GUID(rs.getString(COL_ID)));
						row.setInvoiceId(new GUID(rs.getString(COL_INVOICE_ID)));
						row.setLineNumber(rs.getInt(COL_LINE_NUMBER));
						row.setItemId(new GUID(rs.getString(COL_ITEM_ID)));
						row.setItemName(rs.getString(COL_ITEM_NAME));
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
	 * Returns the InvoiceLineItemDataRow object for the given invoice id and line number.
	 *
	 * @param invoiceId
	 * @param lineNumber
	 * @return the InvoiceLineItemDataRow object for the given invoice id and line number.
	 */
	public InvoiceLineItemDataRow findByLineNumber(String invoiceId, int lineNumber) {
		String query = SQL_SelectAllColumns +
				" WHERE " + COL_INVOICE_ID + " = ? AND " + COL_LINE_NUMBER + " = ?";
		List<InvoiceLineItemDataRow> items = getJdbcTemplate().query(query,
				new RowMapper<InvoiceLineItemDataRow>() {
					public InvoiceLineItemDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
						InvoiceLineItemDataRow row = new InvoiceLineItemDataRow();
						row.setId(new GUID(rs.getString(COL_ID)));
						row.setInvoiceId(new GUID(rs.getString(COL_INVOICE_ID)));
						row.setLineNumber(rs.getInt(COL_LINE_NUMBER));
						row.setItemId(new GUID(rs.getString(COL_ITEM_ID)));
						row.setItemName(rs.getString(COL_ITEM_NAME));
						row.setQty(rs.getInt(COL_QTY));
						row.setPrice(rs.getDouble(COL_PRICE));
						row.setTaxable(rs.getInt(COL_TAXABLE));
						row.clean();
						return row;
					}
				}, invoiceId, lineNumber);

		return items.size() > 0 ? items.get(0) : null;
	}

	/**
	 * Returns all the invoice line items of an invoice in the system.
	 *
	 * @return the List of InvoiceLineItemDataRow object for the given invoice id.
	 */
	public List<InvoiceLineItemDataRow> findAll(String invoiceId) {
		String query = SQL_SelectAllColumns +
				" WHERE " + COL_INVOICE_ID + " = ? order by " + COL_LINE_NUMBER + " asc";

		return getJdbcTemplate().query(query,
				new RowMapper<InvoiceLineItemDataRow>() {
					public InvoiceLineItemDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
						InvoiceLineItemDataRow row = new InvoiceLineItemDataRow();
						row.setId(new GUID(rs.getString(COL_ID)));
						row.setInvoiceId(new GUID(rs.getString(COL_INVOICE_ID)));
						row.setLineNumber(rs.getInt(COL_LINE_NUMBER));
						row.setItemId(new GUID(rs.getString(COL_ITEM_ID)));
						row.setItemName(rs.getString(COL_ITEM_NAME));
						row.setQty(rs.getInt(COL_QTY));
						row.setPrice(rs.getDouble(COL_PRICE));
						row.setTaxable(rs.getInt(COL_TAXABLE));
						row.clean();
						return row;
					}
				}, invoiceId);
	}


	public int insert(InvoiceLineItemDataRow row) throws ScalarException {
		StringBuilder query = new StringBuilder();
		String sep = "";
		query.append("insert into " + TABLE_NAME + " (");
		query.append(sep).append(COL_ID);
		sep = ",";
		query.append(sep).append(COL_INVOICE_ID);
		query.append(sep).append(COL_LINE_NUMBER);
		query.append(sep).append(COL_ITEM_ID);
		query.append(sep).append(COL_ITEM_NAME);
		query.append(sep).append(COL_QTY);
		query.append(sep).append(COL_PRICE);
		query.append(sep).append(COL_TAXABLE);
		query.append(") values (?, ?, ?, ?, ?, ?, ?, ?)");

		return getJdbcTemplate().update(query.toString(),
				row.getId().toString(),
				row.getInvoiceId().toString(),
				row.getLineNumber(),
				row.getItemId().toString(),
				row.getItemName(),
				row.getQty(),
				row.getPrice(),
				row.getTaxable()
		);
	}

	public int update(InvoiceLineItemDataRow row) throws ScalarException {
		StringBuilder query = new StringBuilder();
		String sep = "";
		query.append("update " + TABLE_NAME + " set ");
		if (row.modInvoiceId()) {
			query.append(sep).append(COL_INVOICE_ID).append(" = ?");
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
		if (row.modItemName()) {
			query.append(sep).append(COL_ITEM_NAME).append(" = ?");
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
		if (row.modInvoiceId()) {
			args.add(row.getInvoiceId());
			argTypes.add(COL_SQL_TYPES.get(COL_INVOICE_ID));
		}
		if (row.modLineNumber()) {
			args.add(row.getLineNumber());
			argTypes.add(COL_SQL_TYPES.get(COL_LINE_NUMBER));
		}
		if (row.modItemId()) {
			args.add(row.getItemId().toString());
			argTypes.add(COL_SQL_TYPES.get(COL_ITEM_ID));
		}
		if (row.modItemName()) {
			args.add(row.getItemName());
			argTypes.add(COL_SQL_TYPES.get(COL_ITEM_NAME));
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

	public int removeByInvoiceId(String invoiceId) {
		String query = "delete from " + TABLE_NAME + " where " + COL_INVOICE_ID + " = ?";

		return getJdbcTemplate().update(query, invoiceId);
	}

	public int removeByLineNumber(String invoiceId, int lineNumber) {
		String query = "delete from " + TABLE_NAME + " where " + COL_INVOICE_ID + " = ? and " + COL_LINE_NUMBER + " = ?";

		return getJdbcTemplate().update(query, invoiceId, lineNumber);
	}

	public int removeById(String id) {
		String query = "delete from " + TABLE_NAME + " where " + COL_ID + " = ?";

		return getJdbcTemplate().update(query, id);
	}

	public static InvoiceLineItemData rowToData(InvoiceLineItemDataRow row) {
		InvoiceLineItemData invoiceLineItemData = new InvoiceLineItemData();
		invoiceLineItemData.setId(row.getId().toString());
		invoiceLineItemData.setInvoiceId(row.getInvoiceId().toString());
		invoiceLineItemData.setLineNumber(row.getLineNumber());
		invoiceLineItemData.setItemId(row.getItemId().toString());
		invoiceLineItemData.setItemName(row.getItemName());
		invoiceLineItemData.setQty(row.getQty());
		invoiceLineItemData.setPrice(row.getPrice());
		invoiceLineItemData.setTaxable(row.getTaxable()!=0);

		return invoiceLineItemData;
	}

	public static InvoiceLineItemDataRow dataToRow(InvoiceLineItemData invoiceLineItemData) {
		InvoiceLineItemDataRow row = new InvoiceLineItemDataRow();
		row.setId(new GUID(invoiceLineItemData.getId()));
		row.setInvoiceId(new GUID(invoiceLineItemData.getInvoiceId()));
		row.setLineNumber(invoiceLineItemData.getLineNumber());
		row.setItemId(new GUID(invoiceLineItemData.getItemId()));
		row.setItemName(invoiceLineItemData.getItemName());
		row.setQty(invoiceLineItemData.getQty());
		row.setPrice(invoiceLineItemData.getPrice());
		row.setTaxable(invoiceLineItemData.isTaxable()?1:0);

		return row;
	}
}