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
import com.scalar.freequent.dao.GroupDataDAO;
import com.scalar.freequent.dao.GroupDataRow;
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
public class GroupDataServiceImpl extends AbstractService implements GroupDataService{
	protected static final Log logger = LogFactory.getLog(GroupDataServiceImpl.class);

	public List<GroupData> findAll() {
		GroupDataDAO groupDataDAO = DAOFactory.getDAO(GroupDataDAO.class, getRequest());
		List<GroupDataRow> rows = groupDataDAO.findAll();
		List<GroupData> units = new ArrayList<GroupData>(rows.size());
		for (GroupDataRow row: rows) {
			GroupData groupData = GroupDataDAO.rowToData(row);
			setRecord(groupData, groupData.getId());
			units.add (groupData);
		}
		return units;
	}

	public GroupData findByName(String name) {
		GroupDataDAO groupDataDAO = DAOFactory.getDAO(GroupDataDAO.class, getRequest());
		GroupDataRow row = groupDataDAO.findByName(name);
		GroupData groupData = GroupDataDAO.rowToData(row);
		setRecord(groupData, groupData.getId());
		return groupData;
	}

	@Transactional
	public boolean insertOrUpdate(GroupData groupData) throws ScalarServiceException {
		boolean isNew = groupData.getId() == null;
		GroupDataDAO groupDataDAO = DAOFactory.getDAO(GroupDataDAO.class, getRequest());
		if (isNew) {
			// insert
			try {
				groupData.setId (GUID.generateString(ObjectType.TYPE_CODE_GROUP));
				groupDataDAO.insert(GroupDataDAO.dataToRow(groupData));
			} catch (ScalarException ex) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_CREATE_GROUP, groupData.getName());
				throw ScalarServiceException.create(msgObject, ex);
			}
		} else {
			// update
			try {
				groupDataDAO.update(GroupDataDAO.dataToRow(groupData));
			} catch (ScalarException ex) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_UPDATE_GROUP, groupData.getName());
				throw ScalarServiceException.create(msgObject, ex);
			}
		}

		return true;
	}

	@Transactional
	public boolean removeByName(String name) {
		GroupDataDAO GroupDataDAO = DAOFactory.getDAO(GroupDataDAO.class, getRequest());
		GroupDataDAO.removeByName(name);
		return true;
	}

	@Transactional
	public boolean remove(String id) {
		GroupDataDAO GroupDataDAO = DAOFactory.getDAO(GroupDataDAO.class, getRequest());
		GroupDataDAO.removeById(id);
		return true;
	}
}