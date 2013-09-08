package com.scalar.freequent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.core.util.GUID;

/**
 * User: Sujan Kumar Suppala
 * Date: Sep 7, 2013
 * Time: 6:57:12 PM
 *
 * Holds the data from a single row of the ftCapabilityInfo table.
 */
public class CapabilityInfoRow {
    protected static final Log logger = LogFactory.getLog(CapabilityInfoRow.class);

    private String mName;
    private boolean mModName;

    public String getName () {
        return mName;
    }

    public void setName (String x) {
        mName = x; mModName = true;
    }

    public boolean modName () {
        return mModName;
    }

    private int mSupportsRead = 0;
    private boolean mModSupportsRead = false;

    public int getSupportsRead () {
        return mSupportsRead;
    }

    public void setSupportsRead (int x) {
        mSupportsRead = x; mModSupportsRead = true;
    }

    public boolean modSupportsREad () {
        return mModSupportsRead;
    }

    private int mSupportsWrite = 0;
    private boolean mModSupportsWrite = false;

    public int getSupportsWrite () {
        return mSupportsWrite;
    }

    public void setSupportsWrite (int x) {
        mSupportsWrite = x; mModSupportsWrite = true;
    }

    public boolean modSupportsWrite () {
        return mModSupportsWrite;
    }

    private int mSupportsDelete = 0;
    private boolean mModSupportsDelete = false;

    public int getSupportsDelete () {
        return mSupportsDelete;
    }

    public void setSupportsDelete (int x) {
        mSupportsDelete = x; mModSupportsDelete = true;
    }

    public boolean modSupportsDelete () {
        return mModSupportsDelete;
    }

    /**
     *  Determine whether any column of the row has been modified.
     *
     *  @return <CODE>true</CODE> if any column has been modified.
     */
    public boolean modified() {
        boolean result = false;
        if(
            mModName ||
            mModSupportsRead ||
			mModSupportsWrite ||
            mModSupportsDelete ||
            false ){ result = true; }
        return result;
    }

    /**
     *  Set all of the column modified flags to false.
     */
    public void clean() {
        mModName = false;
        mModSupportsRead = false;
        mModSupportsWrite = false;
        mModSupportsDelete = false;
    }
}
