package com.scalar.freequent.service;

import com.scalar.core.ScalarException;
import com.scalar.core.ScalarServiceException;
import com.scalar.core.jdbc.DAOFactory;
import com.scalar.core.service.AbstractService;
import com.scalar.core.service.ServiceFactory;
import com.scalar.core.util.GUID;
import com.scalar.core.util.MsgObject;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.freequent.common.*;
import com.scalar.freequent.dao.*;
import com.scalar.freequent.l10n.ServiceResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 14, 2013
 * Time: 11:25:54 PM
 */
public class ItemDataServiceImpl extends AbstractService implements ItemDataService {
	protected static final Log logger = LogFactory.getLog(ItemDataServiceImpl.class);

	public List<Item> findAll() {
		ItemDataDAO itemDataDAO = DAOFactory.getDAO(ItemDataDAO.class, getRequest());
		List<ItemDataRow> rows = itemDataDAO.findAll();
		List<Item> items = new ArrayList<Item>(rows.size());
		for (ItemDataRow row: rows) {
			Item item = ItemDataDAO.rowToData(row);
			setRelations(item);
			items.add (item);
		}
		return items;
	}

	public List<Item> search(Map<String, String> searchParams) {
		ItemDataDAO itemDataDAO = DAOFactory.getDAO(ItemDataDAO.class, getRequest());
		List<ItemDataRow> rows = itemDataDAO.search(searchParams);
		List<Item> items = new ArrayList<Item>(rows.size());
		for (ItemDataRow row: rows) {
			Item item = ItemDataDAO.rowToData(row);
			setRelations(item);
			items.add (item);
		}
		return items;
	}

	/**
	 *
	 * @param name
	 *
	 * @return null if not found.
	 */
	public Item findByName(String name) {
		ItemDataDAO itemDataDAO = DAOFactory.getDAO(ItemDataDAO.class, getRequest());
		ItemDataRow itemRow = itemDataDAO.findByName(name);
		Item item = null;
		if (itemRow != null) {
			item = ItemDataDAO.rowToData(itemRow);
			setRelations(item);
		}

		return item;
	}

	public Item findById(String id) throws ScalarServiceException {
		ItemDataDAO itemDataDAO = DAOFactory.getDAO(ItemDataDAO.class, getRequest());
		Item item = null;
		try {
			ItemDataRow itemRow = itemDataDAO.findByPrimaryKey(id);
			item = ItemDataDAO.rowToData(itemRow);
			setRelations(item);
		} catch (DataAccessException e) {
			MsgObject msgObj = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_FIND_ITEM, id);
			throw ScalarServiceException.create(msgObj, e);
		}

		return item;
	}

	private void setRelations(Item item) {
		setCategoryAssocDataList(item);
		setRecord(item, item.getId());
		GroupData groupData = item.getGroupData();
		if (groupData != null) {
			GroupDataService groupDataService = ServiceFactory.getService(GroupDataService.class, getRequest());
			groupData = groupDataService.findByName(groupData.getName());
			item.setGroupData(groupData);
		}
		UnitData unitData = item.getUnitData();
		if (unitData != null) {
			UnitDataService unitDataService = ServiceFactory.getService(UnitDataService.class, getRequest());
			unitData = unitDataService.findByName(unitData.getName());
			item.setUnitData(unitData);
		}
	}

	public boolean exists(String itemName) {
		ItemDataDAO itemDataDAO = DAOFactory.getDAO(ItemDataDAO.class, getRequest());
		return itemDataDAO.existsByName(itemName);
	}

	@Transactional
	public boolean insertOrUpdate(Item item) throws ScalarServiceException {
		boolean isNew = item.getId() == null;
		ItemDataDAO itemDataDAO = DAOFactory.getDAO(ItemDataDAO.class, getRequest());
		if (isNew) {
			// insert
			try {
				item.setId(GUID.generateString(ObjectType.TYPE_CODE_ITEM));
			} catch (ScalarException e) {
				throw ScalarServiceException.create(MsgObjectUtil.getMsgObject(e.getMessage()), e);
			}
			ItemDataRow row = ItemDataDAO.dataToRow(item);
			try {
				itemDataDAO.insert(row);
			} catch (ScalarException ex) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_CREATE_ITEM, item.getName());
				throw ScalarServiceException.create(msgObject, ex);
			}
		} else {
			// update
			ItemDataRow row = ItemDataDAO.dataToRow(item);
			try {
				itemDataDAO.update(row);
			} catch (ScalarException ex) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_UPDATE_ITEM, item.getName());
				throw ScalarServiceException.create(msgObject, ex);
			}
		}
		// update category associations
		CategoryAssocDataDAO categoryAssocDataDAO = DAOFactory.getDAO(CategoryAssocDataDAO.class, getRequest());
		//first remove associations
		categoryAssocDataDAO.removeByObjectId(item.getId());
		//insert associations
		String[] categoryIds = item.getCategoryId();
		categoryAssocDataDAO.insert(item.getId(), categoryIds);

		return false;
	}

	@Transactional
	public boolean removeByName(String name) {
		ItemDataDAO itemDataDAO = DAOFactory.getDAO(ItemDataDAO.class, getRequest());
		CategoryAssocDataDAO categoryAssocDataDAO = DAOFactory.getDAO(CategoryAssocDataDAO.class, getRequest());
		Item item = findByName(name);
		if (item != null) {
			categoryAssocDataDAO.removeByObjectId(item.getId());
			itemDataDAO.removeByName(name);
		}
		return true;
	}

	@Transactional
	public boolean remove(String id) {
		ItemDataDAO itemDataDAO = DAOFactory.getDAO(ItemDataDAO.class, getRequest());
		CategoryAssocDataDAO categoryAssocDataDAO = DAOFactory.getDAO(CategoryAssocDataDAO.class, getRequest());
		categoryAssocDataDAO.removeByObjectId(id);
		itemDataDAO.removeById(id);
		return true;
	}

	private void setCategoryAssocDataList(Item item) {
		CategoryAssocDataDAO categoryAssocDataDAO = DAOFactory.getDAO(CategoryAssocDataDAO.class, getRequest());
		CategoryDataDAO categoryDataDAO = DAOFactory.getDAO(CategoryDataDAO.class, getRequest());
		List<String> categoryIds = new ArrayList<String>();
		// load category associations
		List<CategoryAssocDataRow> categoryRows = categoryAssocDataDAO.findByObjectId(item.getId());
		List<CategoryAssocData> categoryAssocDataList = new ArrayList<CategoryAssocData>(categoryRows.size());
		for (CategoryAssocDataRow catRow: categoryRows) {
			categoryAssocDataList.add (CategoryAssocDataDAO.rowToData(catRow));
			categoryIds.add (catRow.getCategoryId().toString());
		}
		item.setCategoryAssocDataList(categoryAssocDataList);
		item.setCategoryId(categoryIds.toArray(new String[categoryIds.size()]));
	}
}
