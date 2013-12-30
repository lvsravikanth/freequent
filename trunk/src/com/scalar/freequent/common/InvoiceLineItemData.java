package com.scalar.freequent.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: Sujan Kumar Suppala
 * Date: Dec 27, 2013
 * Time: 8:06:41 PM
 */
public class InvoiceLineItemData {
	protected static final Log logger = LogFactory.getLog(InvoiceLineItemData.class);

	private String id;
	private String invoiceId;
	private int lineNumber;
	private String itemId;
	private String itemName;
	private int qty;
	private double price;
	private boolean taxable;
	private double amount;

	public static final String ATTR_ID = "id";
	public static final String ATTR_INVOICE_ID = "invoiceId";
	public static final String ATTR_LINE_NUMBER = "lineNumber";
	public static final String ATTR_ITEM_ID = "itemId";
	public static final String ATTR_ITEM_NAME = "itemName";
	public static final String ATTR_QTY = "qty";
	public static final String ATTR_PRICE = "price";
	public static final String ATTR_TAXABLE = "taxable";
	public static final String ATTR_AMOUNT = "amount";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
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

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}