package com.scalar.freequent.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 6, 2013
 * Time: 9:10:43 PM
 */
public class CategoryAssocData {
	protected static final Log logger = LogFactory.getLog(CategoryAssocData.class);

	private String id;
	private String name;
	private String description;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
