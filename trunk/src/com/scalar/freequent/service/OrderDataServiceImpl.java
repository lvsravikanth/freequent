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
 * Date: Nov 30, 2013
 * Time: 11:25:54 AM
 */
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

	@Transactional
	public boolean insertOrUpdate(OrderData orderData) throws ScalarServiceException {
		boolean isNew = orderData.getId() == null;
		OrderDataDAO orderDataDAO = DAOFactory.getDAO(OrderDataDAO.class, getRequest());
		OrderLineItemDataService orderLineItemDataService = ServiceFactory.getService(OrderLineItemDataService.class, getRequest());
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
			}
		} else {
			// update
			OrderDataRow row = OrderDataDAO.dataToRow(orderData);
			try {
				orderDataDAO.update(row);

			} catch (ScalarException ex) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_UPDATE_ORDER, orderData.getOrderNumber());
				throw ScalarServiceException.create(msgObject, ex);
			}
		}
		// insert/update lineitems
		List<OrderLineItemData> lineItemDatas = orderData.getLineItems();
		for (OrderLineItemData lineItem: lineItemDatas) {
			if (lineItem.isRemoved()) {
				orderLineItemDataService.remove(lineItem.getId());
			} else {
				orderLineItemDataService.insertOrUpdate(lineItem);
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
}