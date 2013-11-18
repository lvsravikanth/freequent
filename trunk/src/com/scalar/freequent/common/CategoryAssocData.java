package com.scalar.freequent.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.HashMap;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 6, 2013
 * Time: 9:10:43 PM
 */
public class CategoryAssocData {
	protected static final Log logger = LogFactory.getLog(CategoryAssocData.class);

	private String objectId;
	private String categoryId;

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public Map<String, Object> toMap() {
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put ("objectId", objectId);
		map.put ("categoryId", categoryId);

		return map;
	}
}
