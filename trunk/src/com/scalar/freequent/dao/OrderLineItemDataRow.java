package com.scalar.freequent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.core.util.GUID;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 30, 2013
 * Time: 10:37:10 AM
 */
public class OrderLineItemDataRow {
	protected static final Log logger = LogFactory.getLog(OrderLineItemDataRow.class);
	
	private GUID mId;
	private boolean mModId = false;
	public void setId(GUID x){ mId = x; mModId = true; }
	public boolean modId(){ return mModId; }
	public GUID getId(){ return mId; }

	private GUID mOrderId;
	private boolean mModOrderId = false;
	public void setOrderId(GUID x){ mOrderId = x; mModOrderId = true; }
	public boolean modOrderId(){ return mModOrderId; }
	public GUID getOrderId(){ return mOrderId; }
	
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
            mModOrderId ||
            mModQty ||
			mModLineNumber ||
			mModItemId ||
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
        mModOrderId = false;
        mModQty = false;
		mModLineNumber = false;
		mModItemId = false;
        mModPrice = false;
        mModTaxable = false;
	}
}