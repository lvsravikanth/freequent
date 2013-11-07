package com.scalar.freequent.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.ArrayList;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 6, 2013
 * Time: 9:06:41 PM
 */
public class Item implements HasRecord {
	protected static final Log logger = LogFactory.getLog(Item.class);

	private String id;
	private GroupData groupData;
	private String name;
	private double price;
	private int priceQty;
	private UnitData unitData;
	private List<CategoryAssocData> categoryAssocDataList = new ArrayList<CategoryAssocData>();
	private Record record;

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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getPriceQty() {
		return priceQty;
	}

	public void setPriceQty(int priceQty) {
		this.priceQty = priceQty;
	}

	public GroupData getGroupData() {
		return groupData;
	}

	public void setGroupData(GroupData groupData) {
		this.groupData = groupData;
	}

	public UnitData getUnitData() {
		return unitData;
	}

	public void setUnitData(UnitData unitData) {
		this.unitData = unitData;
	}

	public List<CategoryAssocData> getCategoryAssocDataList() {
		return categoryAssocDataList;
	}

	public void setCategoryAssocDataList(List<CategoryAssocData> categoryAssocDataList) {
		this.categoryAssocDataList = categoryAssocDataList;
	}

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}
}
