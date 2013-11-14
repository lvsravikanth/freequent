package com.scalar.freequent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.core.util.GUID;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 13, 2013
 * Time: 10:37:10 PM
 */
public class ItemDataRow {
	protected static final Log logger = LogFactory.getLog(ItemDataRow.class);
	
	private GUID mId;
	private boolean mModId = false;
	public void setId(GUID x){ mId = x; mModId = true; }
	public boolean modId(){ return mModId; }
	public GUID getId(){ return mId; }
	
	private String mName;
	private boolean mModName = false;
	public void setName(String x){ mName = x; mModName = true; }
	public boolean modName(){ return mModName; }
	public String getName(){ return mName; }
	
	private String mCode;
	private boolean mModCode = false;
	public void setCode(String x){ mCode = x; mModCode = true; }
	public boolean modCode(){ return mModCode; }
	public String getCode(){ return mCode; }
	
	private String mGroupName;
	private boolean mModGroupName = false;
	public void setGroupName(String x){ mGroupName = x; mModGroupName = true; }
	public boolean modGroupName(){ return mModGroupName; }
	public String getGroupName(){ return mGroupName; }
	
	private double mPrice;
	private boolean mModPrice = false;
	public void setPrice(double x){ mPrice = x; mModPrice = true; }
	public boolean modPrice(){ return mModPrice; }
	public double getPrice(){ return mPrice; }
	
	private int mPriceQty;
	private boolean mModPriceQty = false;
	public void setPriceQty(int x){ mPriceQty = x; mModPriceQty = true; }
	public boolean modPriceQty(){ return mModPriceQty; }
	public int getPriceQty(){ return mPriceQty; }
	
	private String mUnit;
	private boolean mModUnit = false;
	public void setUnit(String x){ mUnit = x; mModUnit = true; }
	public boolean modUnit(){ return mModUnit; }
	public String getUnit(){ return mUnit; }
	
	/**
     *  Determine whether any column of the row has been modified.
     *
     *  @return <CODE>true</CODE> if any column has been modified.
     */
    public boolean modified() {
        boolean result = false;
        if(
            mModId ||
            mModName ||
            mModCode ||
            mModGroupName ||
            mModPrice ||
            mModPriceQty ||
            mModUnit ||
			false ){ result = true; }
        return result;
    }

    /**
     *  Set all of the column modified flags to false.
     */
    public void clean() {
        mModId = false;
        mModName = false;
        mModCode = false;
        mModGroupName = false;
        mModPrice = false;
        mModPriceQty = false;
        mModUnit = false;
	}
}
