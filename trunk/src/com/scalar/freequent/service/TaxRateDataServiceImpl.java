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
import com.scalar.freequent.common.TaxRateData;
import com.scalar.freequent.dao.UnitDataDAO;
import com.scalar.freequent.dao.UnitDataRow;
import com.scalar.freequent.dao.TaxRateDataDAO;
import com.scalar.freequent.dao.TaxRateDataRow;
import com.scalar.freequent.l10n.ServiceResource;

import java.util.List;
import java.util.ArrayList;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 22, 2013
 * Time: 3:28:44 PM
 */
public class TaxRateDataServiceImpl extends AbstractService implements TaxRateDataService {
	protected static final Log logger = LogFactory.getLog(TaxRateDataServiceImpl.class);

	public List<TaxRateData> findAll() {
		TaxRateDataDAO taxRateDataDAO = DAOFactory.getDAO(TaxRateDataDAO.class, getRequest());
		List<TaxRateDataRow> rows = taxRateDataDAO.findAll();
		List<TaxRateData> units = new ArrayList<TaxRateData>(rows.size());
		for (TaxRateDataRow row: rows) {
			units.add (TaxRateDataDAO.rowToData(row));
		}
		return units;
	}

	public TaxRateData findByName(String name) {
		TaxRateDataDAO taxRateDataDAO = DAOFactory.getDAO(TaxRateDataDAO.class, getRequest());
		TaxRateDataRow row = taxRateDataDAO.findByName(name);
		return TaxRateDataDAO.rowToData(row);
	}

	@Transactional
	public boolean insertOrUpdate(TaxRateData taxRateData) throws ScalarServiceException {
		boolean isNew = taxRateData.getId() == null;
		TaxRateDataDAO taxRateDataDAO = DAOFactory.getDAO(TaxRateDataDAO.class, getRequest());
		if (isNew) {
			// insert
			try {
				taxRateData.setId (GUID.generateString(ObjectType.TYPE_CODE_UNIT));
				taxRateDataDAO.insert(TaxRateDataDAO.dataToRow(taxRateData));
			} catch (ScalarException ex) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_CREATE_TAX_RATE, taxRateData.getName());
				throw ScalarServiceException.create(msgObject, ex);
			}
		} else {
			// update
			try {
				taxRateDataDAO.update(TaxRateDataDAO.dataToRow(taxRateData));
			} catch (ScalarException ex) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_UPDATE_TAX_RATE, taxRateData.getName());
				throw ScalarServiceException.create(msgObject, ex);
			}
		}

		return true;
	}

	@Transactional
	public boolean removeByName(String name) {
		TaxRateDataDAO taxRateDataDAO = DAOFactory.getDAO(TaxRateDataDAO.class, getRequest());
		taxRateDataDAO.removeByName(name);
		return true;
	}

	@Transactional
	public boolean remove(String id) {
		TaxRateDataDAO taxRateDataDAO = DAOFactory.getDAO(TaxRateDataDAO.class, getRequest());
		taxRateDataDAO.removeById(id);
		return true;
	}
}