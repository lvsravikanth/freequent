package com.scalar.freequent.service;

import com.scalar.core.ScalarException;
import com.scalar.core.ScalarServiceException;
import com.scalar.core.jdbc.DAOFactory;
import com.scalar.core.service.AbstractService;
import com.scalar.core.service.ServiceFactory;
import com.scalar.core.util.GUID;
import com.scalar.core.util.MsgObject;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.freequent.common.InvoiceLineItemData;
import com.scalar.freequent.common.Item;
import com.scalar.freequent.dao.InvoiceLineItemDataDAO;
import com.scalar.freequent.dao.InvoiceLineItemDataRow;
import com.scalar.freequent.l10n.ServiceResource;
import com.scalar.freequent.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Sujan Kumar Suppala
 * Date: Dec 30, 2013
 * Time: 7:25:54 AM
 */
@Transactional(propagation = Propagation.SUPPORTS)
public class InvoiceLineItemDataServiceImpl extends AbstractService implements InvoiceLineItemDataService {
	protected static final Log logger = LogFactory.getLog(InvoiceLineItemDataServiceImpl.class);

	public List<InvoiceLineItemData> findAll(String invoiceId) {
		InvoiceLineItemDataDAO invoiceDataDAO = DAOFactory.getDAO(InvoiceLineItemDataDAO.class, getRequest());
		List<InvoiceLineItemDataRow> rows = invoiceDataDAO.findAll(invoiceId);
		List<InvoiceLineItemData> invoiceLineItemDatas = new ArrayList<InvoiceLineItemData>(rows.size());
		for (InvoiceLineItemDataRow row : rows) {
			InvoiceLineItemData invoiceLineItemData = InvoiceLineItemDataDAO.rowToData(row);
			setRelations(invoiceLineItemData);
			invoiceLineItemDatas.add(invoiceLineItemData);
		}
		return invoiceLineItemDatas;
	}

	public InvoiceLineItemData findByLineNumber(String invoiceId, int lineNumber) throws ScalarServiceException {
		InvoiceLineItemDataDAO invoiceLineItemDataDAO = DAOFactory.getDAO(InvoiceLineItemDataDAO.class, getRequest());
		InvoiceLineItemDataRow invoiceLineItemDataRow = invoiceLineItemDataDAO.findByLineNumber(invoiceId, lineNumber);
		InvoiceLineItemData invoiceLineItemData = null;
		if (invoiceLineItemDataRow != null) {
			invoiceLineItemData = InvoiceLineItemDataDAO.rowToData(invoiceLineItemDataRow);
			setRelations(invoiceLineItemData);
		}

		return invoiceLineItemData;
	}

	public boolean exists(String invoiceId, int lineNumber) throws ScalarServiceException {
		InvoiceLineItemDataDAO invoiceLineItemDataDAO = DAOFactory.getDAO(InvoiceLineItemDataDAO.class, getRequest());
		return invoiceLineItemDataDAO.existsByLineNumber(invoiceId, lineNumber);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean removeByInvoiceId(String invoiceId) throws ScalarServiceException {
		InvoiceLineItemDataDAO invoiceLineItemDataDAO = DAOFactory.getDAO(InvoiceLineItemDataDAO.class, getRequest());
		return invoiceLineItemDataDAO.removeByInvoiceId(invoiceId) != 0;
	}

	@Transactional
	public boolean removeByLineNumber(String invoiceId, int lineNumber) throws ScalarServiceException {
		InvoiceLineItemDataDAO invoiceLineItemDataDAO = DAOFactory.getDAO(InvoiceLineItemDataDAO.class, getRequest());
		return invoiceLineItemDataDAO.removeByLineNumber(invoiceId, lineNumber) != 0;
	}


	public InvoiceLineItemData findById(String id) throws ScalarServiceException {
		InvoiceLineItemDataDAO invoiceLineItemDataDAO = DAOFactory.getDAO(InvoiceLineItemDataDAO.class, getRequest());
		InvoiceLineItemData invoiceLineItemData = null;
		InvoiceLineItemDataRow invoiceLineItemDataRow = invoiceLineItemDataDAO.findByPrimaryKey(id);
		invoiceLineItemData = InvoiceLineItemDataDAO.rowToData(invoiceLineItemDataRow);
		setRelations(invoiceLineItemData);

		return invoiceLineItemData;
	}

	private void setRelations(InvoiceLineItemData invoiceLineItemData) {
		// nothing for now
	}

	@Transactional
	public boolean insertOrUpdate(InvoiceLineItemData invoiceLineItemData) throws ScalarServiceException {
		boolean isNew = StringUtil.isEmpty(invoiceLineItemData.getId());
		InvoiceLineItemDataDAO invoiceLineItemDataDAO = DAOFactory.getDAO(InvoiceLineItemDataDAO.class, getRequest());
		ItemDataService itemDataService = ServiceFactory.getService(ItemDataService.class, getRequest());
		Item itemData = itemDataService.findById(invoiceLineItemData.getItemId());
		if (isNew) {
			// insert
			try {
				invoiceLineItemData.setId(GUID.generateString());
			} catch (ScalarException e) {
				throw ScalarServiceException.create(MsgObjectUtil.getMsgObject(e.getMessage()), e);
			}
			InvoiceLineItemDataRow row = InvoiceLineItemDataDAO.dataToRow(invoiceLineItemData);
			if (row.getPrice() == 0) {
				// set item price
				row.setPrice(itemData.getPrice());
			}
			if (StringUtil.isEmpty(row.getItemName())) {
				row.setItemName(itemData.getName());
			}
			row.setTaxable(itemData.getTaxable() ? 1 : 0);
			try {
				invoiceLineItemDataDAO.insert(row);
			} catch (ScalarException ex) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_CREATE_INVOICE_LINEITEM, invoiceLineItemData.getLineNumber());
				throw ScalarServiceException.create(msgObject, ex);
			}
		} else {
			// update
			//InvoiceLineItemDataRow row = InvoiceLineItemDataDAO.dataToRow(invoiceLineItemData);
			InvoiceLineItemDataRow row = new InvoiceLineItemDataRow();
			row.setId(new GUID(invoiceLineItemData.getId()));
			row.setInvoiceId(new GUID(invoiceLineItemData.getInvoiceId()));
			row.setLineNumber(invoiceLineItemData.getLineNumber());
			row.setItemId(new GUID(invoiceLineItemData.getItemId()));
			row.setItemName(invoiceLineItemData.getItemName());
			row.setQty(invoiceLineItemData.getQty());
			row.setPrice(itemData.getPrice());
			row.setTaxable(itemData.getTaxable() ? 1 : 0);
			try {
				invoiceLineItemDataDAO.update(row);
			} catch (ScalarException ex) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_UPDATE_INVOICE_LINEITEM, invoiceLineItemData.getLineNumber());
				throw ScalarServiceException.create(msgObject, ex);
			}
		}

		return false;
	}

	@Transactional
	public boolean remove(String id) {
		InvoiceLineItemDataDAO invoiceLineItemDataDAO = DAOFactory.getDAO(InvoiceLineItemDataDAO.class, getRequest());
		invoiceLineItemDataDAO.removeById(id);
		return true;
	}
}