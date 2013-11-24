package com.scalar.freequent.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 6, 2013
 * Time: 9:06:41 PM
 */
public class Item implements HasRecord {
	protected static final Log logger = LogFactory.getLog(Item.class);

	private String id;
	private GroupData groupData = new GroupData();
	private String name;
	private String code;
	private double price;
	private int priceQty;
	private UnitData unitData = new UnitData();
	private String[] categoryId;
	private List<CategoryAssocData> categoryAssocDataList = new ArrayList<CategoryAssocData>();
	private Record record;
	private String description;
	private boolean taxable = false;

	public static final String PARAM_NAME = "name";
	public static final String PARAM_UNIT = "unit";
	public static final String PARAM_GROUP = "group";
	public static final String PARAM_CATEGORY = "category";

	public static final String ATTR_ID = "id";
	public static final String ATTR_NAME = "name";
	public static final String ATTR_CODE = "code";
	public static final String ATTR_GROUP_DATA = "groupData";
	public static final String ATTR_PRICE = "price";
	public static final String ATTR_PRICE_QTY = "priceQty";
	public static final String ATTR_UNIT_DATA = "unitData";
	public static final String ATTR_CATEGORY_ASSOC_DATA = "categoryAssocData";
	public static final String ATTR_DESCRIPTION = "description";
	public static final String ATTR_TAXABLE = "taxable";

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String[] getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String[] categoryId) {
		this.categoryId = categoryId;
	}

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getTaxable() {
		return taxable;
	}

	public void setTaxable(boolean taxable) {
		this.taxable = taxable;
	}

	public Map<String, Object> toMap() {
		return toMap(this);
	}

	public static Map<String, Object> toMap(Item item) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put (ATTR_ID, item.getId());
		map.put (ATTR_NAME, item.getName());
		map.put (ATTR_CODE, item.getCode());
		if (item.getGroupData() != null)
			map.put (ATTR_GROUP_DATA, item.getGroupData().toMap());
		map.put (ATTR_UNIT_DATA, item.getUnitData().toMap());
		map.put (ATTR_PRICE, item.getPrice());
		map.put (ATTR_PRICE_QTY, item.getPriceQty());
		map.put (ATTR_DESCRIPTION, item.getDescription());
		map.put (ATTR_TAXABLE, item.getTaxable());
		map.put (ATTR_RECORD, item.getRecord().toMap());
		List<CategoryAssocData> categoryAssocDataList = item.getCategoryAssocDataList();
		if (categoryAssocDataList != null && !categoryAssocDataList.isEmpty()) {
			ArrayList<Map<String, Object>> categoryList = new ArrayList<Map<String, Object>>(categoryAssocDataList.size());
			for (CategoryAssocData categoryAssocData: categoryAssocDataList) {
				categoryList.add (categoryAssocData.toMap());
			}
			map.put (ATTR_CATEGORY_ASSOC_DATA, categoryList);
		}

		return map;
	}
}
