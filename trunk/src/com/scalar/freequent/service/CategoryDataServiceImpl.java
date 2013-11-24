package com.scalar.freequent.service;

import com.scalar.core.ScalarException;
import com.scalar.core.ScalarServiceException;
import com.scalar.core.jdbc.DAOFactory;
import com.scalar.core.service.AbstractService;
import com.scalar.core.util.GUID;
import com.scalar.core.util.MsgObject;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.freequent.common.GroupData;
import com.scalar.freequent.common.ObjectType;
import com.scalar.freequent.common.CategoryData;
import com.scalar.freequent.dao.GroupDataDAO;
import com.scalar.freequent.dao.GroupDataRow;
import com.scalar.freequent.dao.CategoryDataRow;
import com.scalar.freequent.dao.CategoryDataDAO;
import com.scalar.freequent.l10n.ServiceResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 14, 2013
 * Time: 8:28:44 PM
 */
public class CategoryDataServiceImpl extends AbstractService implements CategoryDataService{
	protected static final Log logger = LogFactory.getLog(CategoryDataServiceImpl.class);

	public List<CategoryData> findAll() {
		CategoryDataDAO categoryDataDAO = DAOFactory.getDAO(CategoryDataDAO.class, getRequest());
		List<CategoryDataRow> rows = categoryDataDAO.findAll();
		List<CategoryData> units = new ArrayList<CategoryData>(rows.size());
		for (CategoryDataRow row: rows) {
			CategoryData categoryData = CategoryDataDAO.rowToData(row);
			setRecord(categoryData, categoryData.getId());
			units.add (categoryData);
		}
		return units;
	}

	public CategoryData findByName(String name) {
		CategoryDataDAO categoryDataDAO = DAOFactory.getDAO(CategoryDataDAO.class, getRequest());
		CategoryDataRow row = categoryDataDAO.findByName(name);
		CategoryData categoryData = CategoryDataDAO.rowToData(row);
		setRecord(categoryData, categoryData.getId());
		return categoryData;
	}

	@Transactional
	public boolean insertOrUpdate(CategoryData categoryData) throws ScalarServiceException {
		boolean isNew = categoryData.getId() == null;
		CategoryDataDAO categoryDataDAO = DAOFactory.getDAO(CategoryDataDAO.class, getRequest());
		if (isNew) {
			// insert
			try {
				categoryData.setId (GUID.generateString(ObjectType.TYPE_CODE_GROUP));
				categoryDataDAO.insert(CategoryDataDAO.dataToRow(categoryData));
			} catch (ScalarException ex) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_CREATE_CATEGORY, categoryData.getName());
				throw ScalarServiceException.create(msgObject, ex);
			}
		} else {
			// update
			try {
				categoryDataDAO.update(CategoryDataDAO.dataToRow(categoryData));
			} catch (ScalarException ex) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_UPDATE_CATEGORY, categoryData.getName());
				throw ScalarServiceException.create(msgObject, ex);
			}
		}

		return true;
	}

	@Transactional
	public boolean removeByName(String name) {
		CategoryDataDAO CategoryDataDAO = DAOFactory.getDAO(CategoryDataDAO.class, getRequest());
		CategoryDataDAO.removeByName(name);
		return true;
	}

	@Transactional
	public boolean remove(String id) {
		CategoryDataDAO CategoryDataDAO = DAOFactory.getDAO(CategoryDataDAO.class, getRequest());
		CategoryDataDAO.removeById(id);
		return true;
	}
}