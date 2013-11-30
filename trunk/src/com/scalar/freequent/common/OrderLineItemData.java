package com.scalar.freequent.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 6, 2013
 * Time: 9:06:41 PM
 */
public class OrderLineItemData {
	protected static final Log logger = LogFactory.getLog(OrderLineItemData.class);

	private String id;
	private String orderId;
	private int lineNumber;
	private String itemId;
	private int qty;
	private double price;
	private boolean taxable;
	private boolean removed;

	public static final String ATTR_ID = "id";
	public static final String ATTR_ORDER_ID = "orderId";
	public static final String ATTR_LINE_NUMBER = "lineNumber";
	public static final String ATTR_ITEM_ID = "itemId";
	public static final String ATTR_QTY = "qty";
	public static final String ATTR_PRICE = "price";
	public static final String ATTR_TAXABLE = "taxable";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isTaxable() {
		return taxable;
	}

	public void setTaxable(boolean taxable) {
		this.taxable = taxable;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}
}