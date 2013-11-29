package com.scalar.freequent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.core.util.GUID;

import java.util.Date;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 7, 2013
 * Time: 8:12:17 PM
 */
public class RecordDataRow {
	protected static final Log logger = LogFactory.getLog(RecordDataRow.class);

	/**
     *  id
     */
    private GUID mRecordId;

    /**
     *  <CODE>true</CODE> if the id column has been modified.
     */
    private boolean mModRecordId = false;

    /**
     *  Replaces the value of the id column.
     *
     *  @param x The new value for the column.
     */
    public void setRecordId(GUID x){ mRecordId = x; mModRecordId = true; }

    /**
     *  Determine whether the id column has been modified.
     *
     *  @return <CODE>true</CODE> if the id column has been modified.
     */
    public boolean modRecordId(){ return mModRecordId; }

	 /**
     *  Obtain the value of the id column.
     *
     *  @return The value of the column.
     */
    public GUID getRecordId(){ return mRecordId; }

	private String mObjectId;
	private boolean mModObjectId = false;
	public void setObjectId(String x){ mObjectId = x; mModObjectId = true; }
	public boolean modObjectId(){ return mModObjectId; }
	public String getObjectId(){ return mObjectId; }

	private String mObjectType;
	private boolean mModObjectType = false;
	public void setObjectType(String x){ mObjectType = x; mModObjectType = true; }
	public boolean modObjectType(){ return mModObjectType; }
	public String getObjectType(){ return mObjectType; }

	private String mCreatedBy;
	private boolean mModCreatedBy = false;
	public void setCreatedBy(String x){ mCreatedBy = x; mModCreatedBy = true; }
	public boolean modCreatedBy(){ return mModCreatedBy; }
	public String getCreatedBy(){ return mCreatedBy; }

	private Date mCreatedOn;
	private boolean mModCreatedOn = false;
	public void setCreatedOn(Date x){ mCreatedOn = x; mModCreatedOn = true; }
	public boolean modCreatedOn(){ return mModCreatedOn; }
	public Date getCreatedOn(){ return mCreatedOn; }

	private String mModifiedBy;
	private boolean mModModifiedBy = false;
	public void setModifiedBy(String x){ mModifiedBy = x; mModModifiedBy = true; }
	public boolean modModifiedBy(){ return mModModifiedBy; }
	public String getModifiedBy(){ return mModifiedBy; }

	private Date mModifiedOn;
	private boolean mModModifiedOn = false;
	public void setModifiedOn(Date x){ mModifiedOn = x; mModModifiedOn = true; }
	public boolean modModifiedOn(){ return mModModifiedOn; }
	public Date getModifiedOn(){ return mModifiedOn; }

	/**
     *  Determine whether any column of the row has been modified.
     *
     *  @return <CODE>true</CODE> if any column has been modified.
     */
    public boolean modified() {
        boolean result = false;
        if(
            mModRecordId ||
            mModObjectId ||
            mModObjectType ||
            mModCreatedBy ||
            mModCreatedOn ||
            mModModifiedBy ||
            mModModifiedOn ||
			false ){ result = true; }
        return result;
    }

    /**
     *  Set all of the column modified flags to false.
     */
    public void clean() {
        mModRecordId = false;
        mModObjectId = false;
        mModObjectType = false;
        mModCreatedBy = false;
        mModCreatedOn = false;
        mModModifiedBy = false;
        mModModifiedOn = false;
	}
}