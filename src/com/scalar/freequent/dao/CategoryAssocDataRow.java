package com.scalar.freequent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.core.util.GUID;
import com.scalar.freequent.common.CategoryAssocData;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 13, 2013
 * Time: 10:37:10 PM
 */
public class CategoryAssocDataRow {
	protected static final Log logger = LogFactory.getLog(CategoryAssocDataRow.class);
	
	private GUID mObjectId;
	private boolean mModObjectId = false;
	public void setObjectId(GUID x){ mObjectId = x; mModObjectId = true; }
	public boolean modObjectId(){ return mModObjectId; }
	public GUID getObjectId(){ return mObjectId; }
	
	private GUID mCategoryId;
	private boolean mModCategoryId = false;
	public void setCategoryId(GUID x){ mCategoryId = x; mModCategoryId = true; }
	public boolean modCategoryId(){ return mModCategoryId; }
	public GUID getCategoryId(){ return mCategoryId; }
	
	/**
     *  Determine whether any column of the row has been modified.
     *
     *  @return <CODE>true</CODE> if any column has been modified.
     */
    public boolean modified() {
        boolean result = false;
        if(
            mModObjectId ||
            mModCategoryId ||
			false ){ result = true; }
        return result;
    }

    /**
     *  Set all of the column modified flags to false.
     */
    public void clean() {
        mModObjectId = false;
        mModCategoryId = false;
	}
}