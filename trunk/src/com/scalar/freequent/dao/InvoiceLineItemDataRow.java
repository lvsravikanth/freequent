package com.scalar.freequent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.core.util.GUID;

/**
 * User: Sujan Kumar Suppala
 * Date: Dec 27, 2013
 * Time: 8:47:10 AM
 */
public class InvoiceLineItemDataRow {
	protected static final Log logger = LogFactory.getLog(InvoiceLineItemDataRow.class);

	private GUID mId;
	private boolean mModId = false;

	public void setId(GUID x){ mId = x; mModId = true; }
	public boolean modId(){ return mModId; }
	public GUID getId(){ return mId; }

	private GUID mInvoiceId;
	private boolean mModInvoiceId = false;
	public void setInvoiceId(GUID x){ mInvoiceId = x; mModInvoiceId = true; }
	public boolean modInvoiceId(){ return mModInvoiceId; }
	public GUID getInvoiceId(){ return mInvoiceId; }

	private int mLineNumber;
	private boolean mModLineNumber = false;
	public void setLineNumber(int x){ mLineNumber = x; mModLineNumber = true; }
	public boolean modLineNumber(){ return mModLineNumber; }
	public int getLineNumber(){ return mLineNumber; }

	private GUID mItemId;
	private boolean mModItemId = false;
	public void setItemId(GUID x){ mItemId = x; mModItemId = true; }
	public boolean modItemId(){ return mModItemId; }
	public GUID getItemId(){ return mItemId; }

	private String mItemName;
	private boolean mModItemName = false;
	public void setItemName(String x){ mItemName = x; mModItemName = true; }
	public boolean modItemName(){ return mModItemName; }
	public String getItemName(){ return mItemName; }

	private int mQty;
	private boolean mModQty = false;
	public void setQty(int x){ mQty = x; mModQty = true; }
	public boolean modQty(){ return mModQty; }
	public int getQty(){ return mQty; }

	private double mPrice;
	private boolean mModPrice = false;
	public void setPrice(double x){ mPrice = x; mModPrice = true; }
	public boolean modPrice(){ return mModPrice; }
	public double getPrice(){ return mPrice; }

	private int mTaxable;
	private boolean mModTaxable = false;
	public void setTaxable(int x){ mTaxable = x; mModTaxable = true; }
	public boolean modTaxable(){ return mModTaxable; }
	public int getTaxable(){ return mTaxable; }

	/**
     *  Determine whether any column of the row has been modified.
     *
     *  @return <Price>true</Price> if any column has been modified.
     */
    public boolean modified() {
        boolean result = false;
        if(
            mModId ||
			mModInvoiceId ||
            mModQty ||
			mModLineNumber ||
			mModItemId ||
			mModItemName ||
            mModPrice ||
            mModTaxable ||
			false ){ result = true; }
        return result;
    }

    /**
     *  Set all of the column modified flags to false.
     */
    public void clean() {
        mModId = false;
        mModInvoiceId = false;
        mModQty = false;
		mModLineNumber = false;
		mModItemId = false;
		mModItemName = false;
        mModPrice = false;
        mModTaxable = false;
	}
}