package com.scalar.freequent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.core.util.GUID;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 7, 2013
 * Time: 8:12:17 PM
 */
public class GroupDataRow {
	protected static final Log logger = LogFactory.getLog(GroupDataRow.class);

	/**
     *  id
     */
    private GUID mId;

    /**
     *  <CODE>true</CODE> if the id column has been modified.
     */
    private boolean mModId = false;

    /**
     *  Replaces the value of the id column.
     *
     *  @param x The new value for the column.
     */
    public void setId(GUID x){ mId = x; mModId = true; }

    /**
     *  Determine whether the id column has been modified.
     *
     *  @return <CODE>true</CODE> if the id column has been modified.
     */
    public boolean modId(){ return mModId; }

	 /**
     *  Obtain the value of the id column.
     *
     *  @return The value of the column.
     */
    public GUID getId(){ return mId; }

	private String mName;
	private boolean mModName = false;
	public void setName(String x){ mName = x; mModName = true; }
	public boolean modName(){ return mModName; }
	public String getName(){ return mName; }

	private String mDescription;
	private boolean mModDescription = false;
	public void setDescription(String x){ mDescription = x; mModDescription = true; }
	public boolean modDescription(){ return mModDescription; }
	public String getDescription(){ return mDescription; }

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
            mModDescription ||
			false ){ result = true; }
        return result;
    }

    /**
     *  Set all of the column modified flags to false.
     */
    public void clean() {
        mModId = false;
        mModName = false;
        mModDescription = false;
	}
}
