package com.scalar.freequent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: Sujan Kumar Suppala
 * Date: Sep 7, 2013
 * Time: 6:57:12 PM
 *
 * Holds the data from a single row of the ftCapabilityInfo table.
 */
public class UserCapabilityInfoRow {
    protected static final Log logger = LogFactory.getLog(UserCapabilityInfoRow.class);

    private String mCapabilityName;
    private boolean mModCapabilityName = false;

    public String getCapabilityName () {
        return mCapabilityName;
    }
    
    public void setCapabilityName (String x) {
        mCapabilityName = x; mModCapabilityName = true;
    }
    
    public boolean modCapabilityName () {
        return mModCapabilityName;
    }
    
    private int mHasRead = 0;
    private boolean mModHasRead = false;
    
    public int getHasRead () {
        return mHasRead;
    }
    
    public void setHasRead (int x) {
        mHasRead = x; mModHasRead = true;
    }
    
    public boolean modHasREad () {
        return mModHasRead;
    }
    
    private int mHasWrite = 0;
    private boolean mModHasWrite = false;
    
    public int getHasWrite () {
        return mHasWrite;
    }
    
    public void setHasWrite (int x) {
        mHasWrite = x; mModHasWrite = true;
    }
    
    public boolean modHasWrite () {
        return mModHasWrite;
    }
    
    private int mHasDelete = 0;
    private boolean mModHasDelete = false;
    
    public int getHasDelete () {
        return mHasDelete;
    }
    
    public void setHasDelete (int x) {
        mHasDelete = x; mModHasDelete = true;
    }
    
    public boolean modHasDelete () {
        return mModHasDelete;
    }

    private String mUserId;
    private boolean mModUserId = false;

    public String getUserId () {
        return mUserId;
    }

    public void setUserId (String x) {
        mUserId = x; mModUserId = true;
    }

    public boolean modUserId () {
        return mModUserId;
    }
    
    /**
     *  Determine whether any column of the row has been modified.
     *
     *  @return <CODE>true</CODE> if any column has been modified.
     */
    public boolean modified() {
        boolean result = false;
        if(
            mModCapabilityName ||
            mModHasRead ||
			mModHasWrite ||
            mModHasDelete ||
            mModUserId ||
            false ){ result = true; }
        return result;
    }

    /**
     *  Set all of the column modified flags to false.
     */
    public void clean() {
        mModCapabilityName = false;
        mModHasRead = false;
        mModHasWrite = false;
        mModHasDelete = false;
        mModUserId = false;
    }
}