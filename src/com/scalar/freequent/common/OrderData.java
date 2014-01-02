package com.scalar.freequent.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 6, 2013
 * Time: 9:06:41 PM
 */
public class OrderData implements HasRecord {
	protected static final Log logger = LogFactory.getLog(OrderData.class);

	public enum OrderStatus {
		ACTIVE("Active"), INVOICED("Invoiced"), CANCELLED("Cancelled");

		String status;

		private OrderStatus(String status) {
			this.status = status;
		}

		public static OrderStatus getInstance(String status) {
			for (OrderStatus orderStatus : values()) {
				if (orderStatus.toString().equals(status)) {
					return orderStatus;
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

		public boolean isInvoiced() {
			return this == INVOICED;
		}
	}

	private String id;
	private String orderNumber;
	private Date orderDate;
	private String custName;
	private OrderStatus status = OrderStatus.ACTIVE;
	private int revision;
	private String remarks;
	private double taxPercentage;
	private double discount;
	private Record record;
	private List<OrderLineItemData> lineItems = null;
	private double taxAmount;
	private double totalAmount;
	private double grandTotal;
	private TaxRateData taxRateData;

	public static final String PARAM_ORDER_NUMBER = "ordernumber";
	public static final String PARAM_ITEM_ID = "itemid";
	public static final String PARAM_FROM_DATE = "fromdate";
	public static final String PARAM_TO_DATE = "todate";
	public static final String PARAM_STATUS = "status";

	public static final String ATTR_ID = "id";
	public static final String ATTR_ORDER_NUMBER = "orderNumber";
	public static final String ATTR_ORDER_DATE = "orderDate";
	public static final String ATTR_CUST_NAME = "custName";
	public static final String ATTR_STATUS = "status";
	public static final String ATTR_REVISION = "revision";
	public static final String ATTR_REMARKS = "remarks";
	public static final String ATTR_TAX_PERCENTAGE = "taxPercentage";
	public static final String ATTR_DISCOUNT = "discount";
	public static final String ATTR_LINEITEMS = "lineItems";
	public static final String ATTR_TAX_AMOUNT = "taxAmount";
	public static final String ATTR_TOTAL_AMOUNT = "totalAmount";
	public static final String ATTR_GRAND_TOTAL = "grandTotal";

	public static final String DEFAULT_CUST_NAME = "Cash";

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

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public List<OrderLineItemData> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<OrderLineItemData> lineItems) {
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

	public TaxRateData getTaxRateData() {
		return taxRateData;
	}

	public void setTaxRateData(TaxRateData taxRateData) {
		this.taxRateData = taxRateData;
	}

	public Map<String, Object> toMap() {
		return toMap(this);
	}

	public static Map<String, Object> toMap(OrderData orderData) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(ATTR_ID, orderData.getId());
		map.put ("name", orderData.getOrderNumber());
		map.put(ATTR_ORDER_NUMBER, orderData.getOrderNumber());
		map.put(ATTR_ORDER_DATE, orderData.getOrderDate());
		map.put(ATTR_CUST_NAME, orderData.getCustName());
		map.put(ATTR_STATUS, orderData.getStatus().toString());
		map.put(ATTR_REVISION, orderData.getRevision());
		map.put(ATTR_REMARKS, orderData.getRemarks());
		map.put(ATTR_TAX_PERCENTAGE, orderData.getTaxPercentage());
		map.put(ATTR_DISCOUNT, orderData.getDiscount());
		map.put(ATTR_RECORD, orderData.getRecord().toMap());

		return map;
	}
}