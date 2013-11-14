package com.scalar.freequent.service;

import com.scalar.core.ScalarException;
import com.scalar.core.ScalarServiceException;
import com.scalar.core.jdbc.DAOFactory;
import com.scalar.core.service.AbstractService;
import com.scalar.core.util.GUID;
import com.scalar.core.util.MsgObject;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.freequent.common.CategoryAssocData;
import com.scalar.freequent.common.Item;
import com.scalar.freequent.common.ObjectType;
import com.scalar.freequent.dao.*;
import com.scalar.freequent.l10n.ServiceResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

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
			items.add (item);
			// load category associations
			setCategoryAssocDataList(item);
		}
		return items;
	}

	public List<Item> search(Map<String, String> searchParams) {
		return null;
	}

	public Item findByName(String name) {
		ItemDataDAO itemDataDAO = DAOFactory.getDAO(ItemDataDAO.class, getRequest());
		ItemDataRow itemRow = itemDataDAO.findByName(name);
		Item item = ItemDataDAO.rowToData(itemRow);
		setCategoryAssocDataList(item);

		return item;
	}

	public Item findById(String id) {
		ItemDataDAO itemDataDAO = DAOFactory.getDAO(ItemDataDAO.class, getRequest());
		ItemDataRow itemRow = itemDataDAO.findByPrimaryKey(id);
		Item item = ItemDataDAO.rowToData(itemRow);
		setCategoryAssocDataList(item);

		return item;
	}

	@Transactional
	public boolean insertOrUpdate(Item item) throws ScalarServiceException {
		boolean isNew = item.getId() == null;
		ItemDataDAO itemDataDAO = DAOFactory.getDAO(ItemDataDAO.class, getRequest());
		if (isNew) {
			// insert
			ItemDataRow row = ItemDataDAO.dataToRow(item);
			try {
				row.setId(GUID.generate(ObjectType.TYPE_CODE_ITEM));
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
		List<CategoryAssocData> categoryAssocDataList = item.getCategoryAssocDataList();
		String[] categoryIds = new String[categoryAssocDataList.size()];
		int i = 0;
		for (CategoryAssocData categoryAssocData: categoryAssocDataList) {
			categoryIds[i++] = categoryAssocData.getId();
		}
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
		itemDataDAO.removeByName(id);
		return true;
	}

	private void setCategoryAssocDataList(Item item) {
		CategoryAssocDataDAO categoryAssocDataDAO = DAOFactory.getDAO(CategoryAssocDataDAO.class, getRequest());
		CategoryDataDAO categoryDataDAO = DAOFactory.getDAO(CategoryDataDAO.class, getRequest());
		// load category associations
		List<CategoryAssocDataRow> categoryRows = categoryAssocDataDAO.findByObjectId(item.getId());
		String[] catIds = new String[categoryRows.size()];
		int i=0;
		for (CategoryAssocDataRow catRow: categoryRows) {
			catIds[i++]=catRow.getCategoryId().toString();
		}
		List<CategoryDataRow> cataDataRows = categoryDataDAO.findByIds(catIds);
		List<CategoryAssocData> categoryAssocDataList = new ArrayList<CategoryAssocData>(cataDataRows.size());
		for (CategoryDataRow catRow: cataDataRows) {
			CategoryAssocData categoryAssocData = new CategoryAssocData();
			categoryAssocData.setId(catRow.getId().toString());
			categoryAssocData.setName(catRow.getName());
			categoryAssocData.setDescription(catRow.getDescription());

			categoryAssocDataList.add (categoryAssocData);
		}
		item.setCategoryAssocDataList(categoryAssocDataList);
	}
}
