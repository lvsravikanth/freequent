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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 30, 2013
 * Time: 11:25:54 AM
 */
@Transactional(propagation = Propagation.SUPPORTS)
public class OrderDataServiceImpl extends AbstractService implements OrderDataService {
	protected static final Log logger = LogFactory.getLog(OrderDataServiceImpl.class);

	public List<OrderData> findAll() throws ScalarServiceException {
		OrderDataDAO orderDataDAO = DAOFactory.getDAO(OrderDataDAO.class, getRequest());
		List<OrderDataRow> rows = orderDataDAO.findAll();
		List<OrderData> orderDatas = new ArrayList<OrderData>(rows.size());
		for (OrderDataRow row : rows) {
			OrderData orderData = OrderDataDAO.rowToData(row);
			setRelations(orderData);
			orderDatas.add(orderData);
		}
		return orderDatas;
	}

	public List<OrderData> search(Map<String, Object> searchParams) throws ScalarServiceException {
		OrderDataDAO orderDataDAO = DAOFactory.getDAO(OrderDataDAO.class, getRequest());
		List<OrderDataRow> rows = orderDataDAO.search(searchParams);
		List<OrderData> orderDatas = new ArrayList<OrderData>(rows.size());
		for (OrderDataRow row : rows) {
			OrderData orderData = OrderDataDAO.rowToData(row);
			setRelations(orderData);
			orderDatas.add(orderData);
		}
		return orderDatas;
	}

	/**
	 * @param orderNumber
	 * @return null if not found.
	 */
	public OrderData findByOrderNumber(String orderNumber) throws ScalarServiceException {
		OrderDataDAO orderDataDAO = DAOFactory.getDAO(OrderDataDAO.class, getRequest());
		OrderDataRow orderDataRow = orderDataDAO.findByOrderNumber(orderNumber);
		OrderData orderData = null;
		if (orderDataRow != null) {
			orderData = OrderDataDAO.rowToData(orderDataRow);
			setRelations(orderData);
		}

		return orderData;
	}

	public OrderData findById(String id) throws ScalarServiceException {
		OrderDataDAO orderDataDAO = DAOFactory.getDAO(OrderDataDAO.class, getRequest());
		OrderData orderData = null;
		OrderDataRow orderDataRow = orderDataDAO.findByPrimaryKey(id);
		orderData = OrderDataDAO.rowToData(orderDataRow);
		setRelations(orderData);

		return orderData;
	}

	private void setRelations(OrderData orderData) throws ScalarServiceException {
		setRecord(orderData, orderData.getId());
		// set lineitems
		OrderLineItemDataService orderDataService = ServiceFactory.getService(OrderLineItemDataService.class, getRequest());
		List<OrderLineItemData> lineItems = orderDataService.findAll(orderData.getId());
		orderData.setLineItems(lineItems);
	}

	public boolean exists(String orderNumber) {
		OrderDataDAO orderDataDAO = DAOFactory.getDAO(OrderDataDAO.class, getRequest());
		return orderDataDAO.existsByOrderNumber(orderNumber);
	}

	@Transactional (propagation = Propagation.REQUIRED)
	public boolean insertOrUpdate(OrderData orderData, boolean createInvoice) throws ScalarServiceException {
		boolean isNew = orderData.getId() == null;
		OrderDataDAO orderDataDAO = DAOFactory.getDAO(OrderDataDAO.class, getRequest());
		OrderLineItemDataService orderLineItemDataService = ServiceFactory.getService(OrderLineItemDataService.class, getRequest());
		List<OrderLineItemData> oldLineItems = null;
		if (isNew) {
			// insert
			try {
				orderData.setId(GUID.generateString(ObjectType.TYPE_CODE_ORDER));
			} catch (ScalarException e) {
				throw ScalarServiceException.create(MsgObjectUtil.getMsgObject(e.getMessage()), e);
			}
			OrderDataRow row = OrderDataDAO.dataToRow(orderData);
			try {
				orderDataDAO.insert(row);
			} catch (ScalarException ex) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_CREATE_ORDER, orderData.getOrderNumber());
				throw ScalarServiceException.create(msgObject, ex);
			} catch (Exception e) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_CREATE_ORDER, orderData.getOrderNumber());
				throw ScalarServiceException.create(msgObject, e);
			}
		} else {
			// update
			OrderData oldOrderData = findById(orderData.getId());
			if (oldOrderData == null) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_FIND_ORDER, orderData.getId());
				throw ScalarServiceException.create(msgObject, null);
			}
			oldLineItems = oldOrderData.getLineItems();

			// prepare order data row
			OrderDataRow row = new OrderDataRow();
			row.setId(new GUID(orderData.getId()));
			row.setCustName(orderData.getCustName());
			row.setRevision(oldOrderData.getRevision()+1);
			row.setDiscount(orderData.getDiscount());
			if (orderData.getStatus() != null) {
				row.setStatus(orderData.getStatus().toString());
			}
			row.setRemarks(orderData.getRemarks());

			try {
				orderDataDAO.update(row);

			} catch (ScalarException ex) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_UPDATE_ORDER, orderData.getOrderNumber());
				throw ScalarServiceException.create(msgObject, ex);
			} catch (Exception ex) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_UPDATE_ORDER, orderData.getOrderNumber());
				throw ScalarServiceException.create(msgObject, ex);
			}
		}
		List<OrderLineItemData> lineItemDatas = orderData.getLineItems();
		// remove lineitems if any.
		if (oldLineItems != null) {
			for (OrderLineItemData oldLineItem: oldLineItems) {
				// check if this line item is removed
				if (removed(oldLineItem, lineItemDatas)) {
					orderLineItemDataService.remove(oldLineItem.getId());
				}
			}
		}

		// insert/update lineitems
		for (OrderLineItemData lineItem: lineItemDatas) {
			lineItem.setOrderId(orderData.getId());
			orderLineItemDataService.insertOrUpdate(lineItem);
		}

		if (createInvoice) {
			InvoiceDataService invoiceDataService = ServiceFactory.getService(InvoiceDataService.class, getRequest());
			// check if invoice already exists, if not exists create
			if (!invoiceDataService.existsByOrderId(orderData.getId())) {
				invoiceDataService.createInvoice(orderData);
			}
		}

		return false;
	}

	@Transactional
	public boolean removeByOrderNumber(String orderNumber) throws ScalarServiceException {
		OrderDataDAO orderDataDAO = DAOFactory.getDAO(OrderDataDAO.class, getRequest());
		OrderData orderData = findByOrderNumber(orderNumber);
		if (orderData != null) {
			orderDataDAO.removeByOrderNumber(orderNumber);
		}
		return true;
	}

	@Transactional
	public boolean remove(String id) {
		OrderDataDAO orderDataDAO = DAOFactory.getDAO(OrderDataDAO.class, getRequest());
		orderDataDAO.removeById(id);
		return true;
	}

	public long getOrdersCount() throws ScalarServiceException {
		OrderDataDAO orderDataDAO = DAOFactory.getDAO(OrderDataDAO.class, getRequest());
		return orderDataDAO.getOrdersCount();
	}

	/**
	 * Checks whether the given line item is removed or not.
	 *
	 * @param oldLineItem
	 * @param newLineItems
	 * @return true - if removed, otherwise false.
	 */
	private boolean removed(OrderLineItemData oldLineItem, List<OrderLineItemData> newLineItems) {
		boolean removed = true;
		for (OrderLineItemData lineItem: newLineItems) {
			if (lineItem.getId() != null && lineItem.getId().equals(oldLineItem.getId())) {
				removed = false;
				break;
			}
		}

		return removed;
	}
}