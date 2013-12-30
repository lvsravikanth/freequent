package com.scalar.freequent.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * User: Sujan Kumar Suppala
 * Date: Dec 27, 2013
 * Time: 8:10:41 PM
 */
public class InvoiceData implements HasRecord {
	protected static final Log logger = LogFactory.getLog(InvoiceData.class);

	public enum InvoiceStatus {
		ACTIVE("Active"), CANCELLED("Cancelled");

		String status;

		private InvoiceStatus(String status) {
			this.status = status;
		}

		public static InvoiceStatus getInstance(String status) {
			for (InvoiceStatus invoiceStatus : InvoiceStatus.values()) {
				if (invoiceStatus.toString().equals(status)) {
					return invoiceStatus;
				}
			}

			return null;
		}

		@Override
		public String toString() {
			return status;
		}

		public boolean isActive() {
			return this == ACTIVE;
		}

		public boolean isCancelled() {
			return this == CANCELLED;
		}
	}

	private String id;
	private String orderId;
	private String invoiceNumber;
	private Date date;
	private String custName;
	private InvoiceStatus status = InvoiceStatus.ACTIVE;
	private String remarks;
	private String taxName;
	private double taxPercentage;
	private double discount;
	private Record record;
	private List<InvoiceLineItemData> lineItems = null;
	private double taxAmount;
	private double totalAmount;
	private double grandTotal;

	public static final String PARAM_INVOICE_NUMBER = "invoicenumber";
	public static final String PARAM_ORDER_ID = "orderid";
	public static final String PARAM_ITEM_ID = "itemid";
	public static final String PARAM_FROM_DATE = "fromdate";
	public static final String PARAM_TO_DATE = "todate";
	public static final String PARAM_STATUS = "status";

	public static final String ATTR_ID = "id";
	public static final String ATTR_ORDER_ID = "orderId";
	public static final String ATTR_INVOICE_NUMBER = "invoiceNumber";
	public static final String ATTR_INVOICE_DATE = "date";
	public static final String ATTR_CUST_NAME = "custName";
	public static final String ATTR_STATUS = "status";
	public static final String ATTR_REMARKS = "remarks";
	public static final String ATTR_TAX_NAME = "taxName";
	public static final String ATTR_TAX_PERCENTAGE = "taxPercentage";
	public static final String ATTR_DISCOUNT = "discount";
	public static final String ATTR_LINEITEMS = "lineItems";
	public static final String ATTR_TAX_AMOUNT = "taxAmount";
	public static final String ATTR_TOTAL_AMOUNT = "totalAmount";
	public static final String ATTR_GRAND_TOTAL = "grandTotal";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public InvoiceStatus getStatus() {
		return status;
	}

	public void setStatus(InvoiceStatus status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public List<InvoiceLineItemData> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<InvoiceLineItemData> lineItems) {
		this.lineItems = lineItems;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Map<String, Object> toMap() {
		return toMap(this);
	}

	public static Map<String, Object> toMap(InvoiceData invoiceData) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(ATTR_ID, invoiceData.getId());
		map.put ("name", invoiceData.getOrderId());
		map.put(ATTR_ORDER_ID, invoiceData.getOrderId());
		map.put(ATTR_INVOICE_NUMBER, invoiceData.getInvoiceNumber());
		map.put(ATTR_INVOICE_DATE, invoiceData.getDate());
		map.put(ATTR_CUST_NAME, invoiceData.getCustName());
		map.put(ATTR_STATUS, invoiceData.getStatus().toString());
		map.put(ATTR_REMARKS, invoiceData.getRemarks());
		map.put(ATTR_TAX_NAME, invoiceData.getTaxName());
		map.put(ATTR_TAX_PERCENTAGE, invoiceData.getTaxPercentage());
		map.put(ATTR_DISCOUNT, invoiceData.getDiscount());
		map.put(ATTR_RECORD, invoiceData.getRecord().toMap());

		return map;
	}
}