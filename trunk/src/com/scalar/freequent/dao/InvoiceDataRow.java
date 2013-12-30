package com.scalar.freequent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.core.util.GUID;

import java.util.Date;

/**
 * User: Sujan Kumar Suppala
 * Date: Dec 27, 2013
 * Time: 8:51:10 PM
 */
public class InvoiceDataRow {
	protected static final Log logger = LogFactory.getLog(InvoiceDataRow.class);

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

	private String mInvoiceNumber;
	private boolean mModInvoiceNumber = false;
	public void setInvoiceNumber(String x){ mInvoiceNumber = x; mModInvoiceNumber = true; }
	public boolean modInvoiceNumber(){ return mModInvoiceNumber; }
	public String getInvoiceNumber(){ return mInvoiceNumber; }

	private Date mInvoiceDate;
	private boolean mModInvoiceDate = false;
	public void setInvoiceDate(Date x){ mInvoiceDate = x; mModInvoiceDate = true; }
	public boolean modInvoiceDate(){ return mModInvoiceDate; }
	public Date getInvoiceDate(){ return mInvoiceDate; }

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

	private String mRemarks;
	private boolean mModRemarks = false;
	public void setRemarks(String x){ mRemarks = x; mModRemarks = true; }
	public boolean modRemarks(){ return mModRemarks; }
	public String getRemarks(){ return mRemarks; }

	private String mTaxName;
	private boolean mModTaxName = false;
	public void setTaxName(String x){ mTaxName = x; mModTaxName = true; }
	public boolean modTaxName(){ return mModTaxName; }
	public String getTaxName(){ return mTaxName; }

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
            mModOrderId ||
			mModInvoiceNumber ||
			mModInvoiceDate ||
            mModCustName ||
            mModStatus ||
            mModRemarks ||
            mModTaxName ||
            mModTaxPercentage ||
            mModDiscount ||
			false ){ result = true; }
        return result;
    }

    /**
     *  Set all of the column modified flags to false.
     */
    public void clean() {
        mModId = false;
		mModOrderId = false;
		mModInvoiceNumber = false;
		mModInvoiceDate = false;
        mModCustName = false;
        mModStatus = false;
        mModRemarks = false;
		mModTaxName = false;
        mModTaxPercentage = false;
        mModDiscount = false;
	}
}