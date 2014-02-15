package com.scalar.freequent.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;
import com.scalar.core.service.AbstractService;
import com.scalar.core.jdbc.DAOFactory;
import com.scalar.core.ScalarServiceException;
import com.scalar.core.ScalarException;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.core.util.MsgObject;
import com.scalar.core.util.GUID;
import com.scalar.freequent.common.UnitData;
import com.scalar.freequent.common.ObjectType;
import com.scalar.freequent.dao.UnitDataDAO;
import com.scalar.freequent.dao.UnitDataRow;
import com.scalar.freequent.l10n.ServiceResource;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 14, 2013
 * Time: 8:28:44 PM
 */
public class UnitDataServiceImpl extends AbstractService implements UnitDataService {
	protected static final Log logger = LogFactory.getLog(UnitDataServiceImpl.class);

	public List<UnitData> findAll() {
		UnitDataDAO unitDataDAO = DAOFactory.getDAO(UnitDataDAO.class, getRequest());
		List<UnitDataRow> rows = unitDataDAO.findAll();
		List<UnitData> units = new ArrayList<UnitData>(rows.size());
		for (UnitDataRow row: rows) {
			UnitData unitData = UnitDataDAO.rowToData(row);
			setRecord(unitData, unitData.getId());
			units.add (unitData);
		}
		return units;
	}

	public UnitData findByName(String name) {
		UnitDataDAO unitDataDAO = DAOFactory.getDAO(UnitDataDAO.class, getRequest());
		UnitDataRow row = unitDataDAO.findByName(name);
		UnitData unitData = UnitDataDAO.rowToData(row);
		setRecord(unitData, unitData.getId());
		return unitData;
	}

    public UnitData findById(String id) {
        UnitDataDAO unitDataDAO = DAOFactory.getDAO(UnitDataDAO.class, getRequest());
        UnitDataRow row = unitDataDAO.findById(id);
        UnitData unitData = null;
        if (row != null) {
            unitData = UnitDataDAO.rowToData(row);
            setRecord(unitData, unitData.getId());
        }
        return unitData;
    }

    public boolean exists(String unitName) throws ScalarServiceException {
        UnitDataDAO unitDataDAO = DAOFactory.getDAO(UnitDataDAO.class, getRequest());
        return unitDataDAO.existsByName(unitName);
    }

    public List<UnitData> search(Map<String, Object> searchParams) throws ScalarServiceException {
        UnitDataDAO unitDataDAO = DAOFactory.getDAO(UnitDataDAO.class, getRequest());
        List<UnitDataRow> unitDataRows = unitDataDAO.getUnits(searchParams);
        List<UnitData> unitDataList = new ArrayList<UnitData>(unitDataRows.size());
        for (UnitDataRow row: unitDataRows) {
            UnitData unitData = UnitDataDAO.rowToData(row);
            setRecord(unitData, unitData.getId());
            unitDataList.add(unitData);
        }
        return unitDataList;
    }

	@Transactional
	public boolean insertOrUpdate(UnitData unitData) throws ScalarServiceException {
		boolean isNew = unitData.getId() == null;
		UnitDataDAO unitDataDAO = DAOFactory.getDAO(UnitDataDAO.class, getRequest());
		if (isNew) {
			// insert
			try {
				unitData.setId (GUID.generateString(ObjectType.TYPE_CODE_UNIT));
				unitDataDAO.insert(UnitDataDAO.dataToRow(unitData));
			} catch (ScalarException ex) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_CREATE_UNIT, unitData.getName());
				throw ScalarServiceException.create(msgObject, ex);
			}
		} else {
			// update
			try {
				unitDataDAO.update(UnitDataDAO.dataToRow(unitData));
			} catch (ScalarException ex) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_UPDATE_UNIT, unitData.getName());
				throw ScalarServiceException.create(msgObject, ex);
			}
		}

		return true;
	}

	@Transactional
	public boolean removeByName(String name) {
		UnitDataDAO unitDataDAO = DAOFactory.getDAO(UnitDataDAO.class, getRequest());
		unitDataDAO.removeByName(name);
		return true;
	}

	@Transactional
	public boolean remove(String id) {
		UnitDataDAO unitDataDAO = DAOFactory.getDAO(UnitDataDAO.class, getRequest());
		unitDataDAO.removeById(id);
		return true;
	}
}
