package com.scalar.freequent.service;

import com.scalar.core.ScalarException;
import com.scalar.core.ScalarServiceException;
import com.scalar.core.jdbc.DAOFactory;
import com.scalar.core.service.AbstractService;
import com.scalar.core.service.ServiceFactory;
import com.scalar.core.util.GUID;
import com.scalar.core.util.MsgObject;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.freequent.common.InvoiceData;
import com.scalar.freequent.common.InvoiceLineItemData;
import com.scalar.freequent.common.ObjectType;
import com.scalar.freequent.common.OrderData;
import com.scalar.freequent.dao.InvoiceDataDAO;
import com.scalar.freequent.dao.InvoiceDataRow;
import com.scalar.freequent.l10n.ServiceResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Dec 30, 2013
 * Time: 8:25:54 PM
 */
@Transactional(propagation = Propagation.SUPPORTS)
public class InvoiceDataServiceImpl extends AbstractService implements InvoiceDataService {
	protected static final Log logger = LogFactory.getLog(InvoiceDataServiceImpl.class);

	public List<InvoiceData> findAll() throws ScalarServiceException {
		InvoiceDataDAO invoiceDataDAO = DAOFactory.getDAO(InvoiceDataDAO.class, getRequest());
		List<InvoiceDataRow> rows = invoiceDataDAO.findAll();
		List<InvoiceData> invoiceDatas = new ArrayList<InvoiceData>(rows.size());
		for (InvoiceDataRow row : rows) {
			InvoiceData invoiceData = InvoiceDataDAO.rowToData(row);
			setRelations(invoiceData);
			invoiceDatas.add(invoiceData);
		}
		return invoiceDatas;
	}

	public List<InvoiceData> search(Map<String, Object> searchParams) throws ScalarServiceException {
		InvoiceDataDAO invoiceDataDAO = DAOFactory.getDAO(InvoiceDataDAO.class, getRequest());
		List<InvoiceDataRow> rows = invoiceDataDAO.search(searchParams);
		List<InvoiceData> invoiceDatas = new ArrayList<InvoiceData>(rows.size());
		for (InvoiceDataRow row : rows) {
			InvoiceData invoiceData = InvoiceDataDAO.rowToData(row);
			setRelations(invoiceData);
			invoiceDatas.add(invoiceData);
		}
		return invoiceDatas;
	}

	/**
	 * @param invoiceNumber
	 * @return null if not found.
	 */
	public InvoiceData findByInvoiceNumber(String invoiceNumber) throws ScalarServiceException {
		InvoiceDataDAO invoiceDataDAO = DAOFactory.getDAO(InvoiceDataDAO.class, getRequest());
		InvoiceDataRow invoiceDataRow = invoiceDataDAO.findByInvoiceNumber(invoiceNumber);
		InvoiceData invoiceData = null;
		if (invoiceDataRow != null) {
			invoiceData = InvoiceDataDAO.rowToData(invoiceDataRow);
			setRelations(invoiceData);
		}

		return invoiceData;
	}

	public InvoiceData findById(String id) throws ScalarServiceException {
		InvoiceDataDAO invoiceDataDAO = DAOFactory.getDAO(InvoiceDataDAO.class, getRequest());
		InvoiceData invoiceData = null;
		InvoiceDataRow invoiceDataRow = invoiceDataDAO.findByPrimaryKey(id);
		invoiceData = InvoiceDataDAO.rowToData(invoiceDataRow);
		setRelations(invoiceData);

		return invoiceData;
	}

	private void setRelations(InvoiceData invoiceData) throws ScalarServiceException {
		setRecord(invoiceData, invoiceData.getId());
		// set lineitems
		InvoiceLineItemDataService invoiceDataService = ServiceFactory.getService(InvoiceLineItemDataService.class, getRequest());
		List<InvoiceLineItemData> lineItems = invoiceDataService.findAll(invoiceData.getId());
		invoiceData.setLineItems(lineItems);
	}

	public boolean exists(String invoiceNumber) {
		InvoiceDataDAO invoiceDataDAO = DAOFactory.getDAO(InvoiceDataDAO.class, getRequest());
		return invoiceDataDAO.existsByInvoiceNumber(invoiceNumber);
	}

	@Transactional
	public boolean insertOrUpdate(InvoiceData invoiceData) throws ScalarServiceException {
		boolean isNew = invoiceData.getId() == null;
		InvoiceDataDAO invoiceDataDAO = DAOFactory.getDAO(InvoiceDataDAO.class, getRequest());
		InvoiceLineItemDataService invoiceLineItemDataService = ServiceFactory.getService(InvoiceLineItemDataService.class, getRequest());
		List<InvoiceLineItemData> oldLineItems = null;
		if (isNew) {
			// insert
			try {
				invoiceData.setId(GUID.generateString(ObjectType.TYPE_CODE_ORDER));
			} catch (ScalarException e) {
				throw ScalarServiceException.create(MsgObjectUtil.getMsgObject(e.getMessage()), e);
			}
			InvoiceDataRow row = InvoiceDataDAO.dataToRow(invoiceData);
			try {
				invoiceDataDAO.insert(row);
			} catch (ScalarException ex) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_CREATE_ORDER, invoiceData.getInvoiceNumber());
				throw ScalarServiceException.create(msgObject, ex);
			} catch (Exception e) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_CREATE_ORDER, invoiceData.getInvoiceNumber());
				throw ScalarServiceException.create(msgObject, e);
			}
		} else {
			// update
			InvoiceData oldInvoiceData = findById(invoiceData.getId());
			if (oldInvoiceData == null) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_FIND_ORDER, invoiceData.getId());
				throw ScalarServiceException.create(msgObject, null);
			}
			oldLineItems = oldInvoiceData.getLineItems();

			// prepare invoice data row
			InvoiceDataRow row = new InvoiceDataRow();
			row.setId(new GUID(invoiceData.getId()));
			row.setOrderId(new GUID(invoiceData.getOrderId()));
			row.setCustName(invoiceData.getCustName());
			row.setDiscount(invoiceData.getDiscount());
			if (invoiceData.getStatus() != null) {
				row.setStatus(invoiceData.getStatus().toString());
			}
			row.setRemarks(invoiceData.getRemarks());

			try {
				invoiceDataDAO.update(row);

			} catch (ScalarException ex) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_UPDATE_ORDER, invoiceData.getInvoiceNumber());
				throw ScalarServiceException.create(msgObject, ex);
			} catch (Exception ex) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_UPDATE_ORDER, invoiceData.getInvoiceNumber());
				throw ScalarServiceException.create(msgObject, ex);
			}
		}
		List<InvoiceLineItemData> lineItemDatas = invoiceData.getLineItems();
		// remove lineitems if any.
		if (oldLineItems != null) {
			for (InvoiceLineItemData oldLineItem: oldLineItems) {
				// check if this line item is removed
				if (removed(oldLineItem, lineItemDatas)) {
					invoiceLineItemDataService.remove(oldLineItem.getId());
				}
			}
		}

		// insert/update lineitems
		for (InvoiceLineItemData lineItem: lineItemDatas) {
			lineItem.setInvoiceId(invoiceData.getId());
			invoiceLineItemDataService.insertOrUpdate(lineItem);
		}



		return false;
	}

	@Transactional
	public boolean removeByInvoiceNumber(String invoiceNumber) throws ScalarServiceException {
		InvoiceDataDAO invoiceDataDAO = DAOFactory.getDAO(InvoiceDataDAO.class, getRequest());
		InvoiceData invoiceData = findByInvoiceNumber(invoiceNumber);
		if (invoiceData != null) {
			invoiceDataDAO.removeByInvoiceNumber(invoiceNumber);
		}
		return true;
	}

	@Transactional
	public boolean remove(String id) {
		InvoiceDataDAO invoiceDataDAO = DAOFactory.getDAO(InvoiceDataDAO.class, getRequest());
		invoiceDataDAO.removeById(id);
		return true;
	}

	public long getInvoicesCount() throws ScalarServiceException {
		InvoiceDataDAO invoiceDataDAO = DAOFactory.getDAO(InvoiceDataDAO.class, getRequest());
		return invoiceDataDAO.getInvoicesCount();
	}

	/**
	 * Checks whether the given line item is removed or not.
	 *
	 * @param oldLineItem
	 * @param newLineItems
	 * @return true - if removed, otherwise false.
	 */
	private boolean removed(InvoiceLineItemData oldLineItem, List<InvoiceLineItemData> newLineItems) {
		boolean removed = true;
		for (InvoiceLineItemData lineItem: newLineItems) {
			if (lineItem.getId() != null && lineItem.getId().equals(oldLineItem.getId())) {
				removed = false;
				break;
			}
		}

		return removed;
	}

	/**
	 * Creates an invoice for the given Order.
	 *
	 * @param orderData Order Data from which the invoice will be created.
	 * @return the created InvoiceData object.
	 * @throws ScalarServiceException
	 */
	public InvoiceData createInvoice(OrderData orderData) throws ScalarServiceException {
		// todo
		return null;
	}
}