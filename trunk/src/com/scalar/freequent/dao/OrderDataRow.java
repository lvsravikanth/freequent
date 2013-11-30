package com.scalar.freequent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.core.util.GUID;

import java.util.Date;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 30, 2013
 * Time: 10:37:10 AM
 */
public class OrderDataRow {
	protected static final Log logger = LogFactory.getLog(OrderDataRow.class);
	
	private GUID mId;
	private boolean mModId = false;

	public void setId(GUID x){ mId = x; mModId = true; }
	public boolean modId(){ return mModId; }
	public GUID getId(){ return mId; }
	
	private String mOrderNumber;
	private boolean mModOrderNumber = false;
	public void setOrderNumber(String x){ mOrderNumber = x; mModOrderNumber = true; }
	public boolean modOrderNumber(){ return mModOrderNumber; }
	public String getOrderNumber(){ return mOrderNumber; }
	
	private Date mOrderDate;
	private boolean mModOrderDate = false;
	public void setOrderDate(Date x){ mOrderDate = x; mModOrderDate = true; }
	public boolean modOrderDate(){ return mModOrderDate; }
	public Date getOrderDate(){ return mOrderDate; }

	private String mCustName;
	private boolean mModCustName = false;
	public void setCustName(String x){ mCustName = x; mModCustName = true; }
	public boolean modCustName(){ return mModCustName; }
	public String getCustName(){ return mCustName; }
	
	private String mStatus;
	private boolean mModStatus = false;
	public void setStatus(String x){ mStatus = x; mModStatus = true; }
	public boolean modStatus(){ return mModStatus; }
	public String getStatus(){ return mStatus; }
	
	private int mRevision;
	private boolean mModRevision = false;
	public void setRevision(int x){ mRevision = x; mModRevision = true; }
	public boolean modRevision(){ return mModRevision; }
	public int getRevision(){ return mRevision; }
	
	private String mRemarks;
	private boolean mModRemarks = false;
	public void setRemarks(String x){ mRemarks = x; mModRemarks = true; }
	public boolean modRemarks(){ return mModRemarks; }
	public String getRemarks(){ return mRemarks; }
	
	private double mTaxPercentage;
	private boolean mModTaxPercentage = false;
	public void setTaxPercentage(double x){ mTaxPercentage = x; mModTaxPercentage = true; }
	public boolean modTaxPercentage(){ return mModTaxPercentage; }
	public double getTaxPercentage(){ return mTaxPercentage; }
	
	private double mDiscount;
	private boolean mModDiscount = false;
	public void setDiscount(double x){ mDiscount = x; mModDiscount = true; }
	public boolean modDiscount(){ return mModDiscount; }
	public double getDiscount(){ return mDiscount; }

	/**
     *  Determine whether any column of the row has been modified.
     *
     *  @return <Status>true</Status> if any column has been modified.
     */
    public boolean modified() {
        boolean result = false;
        if(
            mModId ||
            mModCustName ||
			mModOrderNumber ||
			mModOrderDate ||
            mModStatus ||
            mModRevision ||
            mModRemarks ||
            mModDiscount ||
			false ){ result = true; }
        return result;
    }

    /**
     *  Set all of the column modified flags to false.
     */
    public void clean() {
        mModId = false;
        mModCustName = false;
		mModOrderNumber = false;
		mModOrderDate = false;
        mModStatus = false;
        mModRevision = false;
        mModRemarks = false;
        mModTaxPercentage = false;
        mModDiscount = false;
	}
}