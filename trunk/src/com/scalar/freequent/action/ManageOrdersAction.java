package com.scalar.freequent.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import com.scalar.freequent.web.spring.controller.AbstractActionController;
import com.scalar.freequent.auth.Capability;
import com.scalar.freequent.common.*;
import com.scalar.freequent.service.*;
import com.scalar.freequent.util.EditorUtils;
import com.scalar.freequent.util.StringUtil;
import com.scalar.freequent.util.DateTimeUtil;
import com.scalar.freequent.l10n.ServiceResource;
import com.scalar.freequent.l10n.ActionResource;
import com.scalar.core.request.Request;
import com.scalar.core.ScalarActionException;
import com.scalar.core.ScalarServiceException;
import com.scalar.core.ScalarValidationException;
import com.scalar.core.ScalarException;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.core.util.MsgObject;
import com.scalar.core.service.ServiceFactory;
import com.scalar.core.response.Response;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.text.DateFormat;
import java.text.ParseException;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 30, 2013
 * Time: 8:53:01 PM
 */
public class ManageOrdersAction extends AbstractActionController {
	protected static final Log logger = LogFactory.getLog(ManageOrdersAction.class);

	public static final String ATTR_ORDER_DATA = "orderData";

	public void defaultProcess(Request request, Object command, Map<String, Object> data) throws ScalarActionException {

		data.put(Response.TEMPLATE_ATTRIBUTE, "order/manageorders");
	}

	public void runsearch(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
		OrderDataService orderDataService = ServiceFactory.getService(OrderDataService.class, request);
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(OrderData.PARAM_ORDER_NUMBER, request.getParameter(OrderData.PARAM_ORDER_NUMBER));
			params.put(OrderData.PARAM_ITEM_ID, request.getParameter(OrderData.PARAM_ITEM_ID));

			Date fromDate = null;
			Date toDate = null;
			DateFormat formatter = DateTimeUtil.getDateFormatter(request.getContext());

			String fromDateStr = request.getParameter(OrderData.PARAM_FROM_DATE);
			if (!StringUtil.isEmpty(fromDateStr)) {
				try {
					fromDate = formatter.parse(fromDateStr);
					params.put(OrderData.PARAM_FROM_DATE, fromDate);
				} catch (ParseException e) {
					MsgObject msgObject = MsgObjectUtil.getMsgObject(ActionResource.BASE_NAME, ActionResource.INVALID_PARAM_VALUE, OrderData.PARAM_FROM_DATE, fromDateStr);
					throw ScalarActionException.create(msgObject, e);
				}
			}

			String toDateStr = request.getParameter(OrderData.PARAM_TO_DATE);
			if (!StringUtil.isEmpty(toDateStr)) {
				try {
					toDate = formatter.parse(toDateStr);
					params.put(OrderData.PARAM_FROM_DATE, toDate);
				} catch (ParseException e) {
					MsgObject msgObject = MsgObjectUtil.getMsgObject(ActionResource.BASE_NAME, ActionResource.INVALID_PARAM_VALUE, OrderData.PARAM_TO_DATE, toDateStr);
					throw ScalarActionException.create(msgObject, e);
				}
			}


			List<OrderData> orders = orderDataService.search(params);

			data.put(Response.ITEMS_ATTRIBUTE, orders);
			data.put(Response.TOTAL_ATTRIBUTE, orders.size() + "");
		} catch (ScalarServiceException e) {
			throw getActionException(e);
		}
	}

	/**
	 * action method for user edit.
	 *
	 * @param request
	 * @param command
	 * @param data
	 * @throws com.scalar.core.ScalarActionException
	 */
	public void load(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
		String id = request.getParameter(EditorUtils.EDITOR_ID_ATTRIBUTE);
		if (StringUtil.isEmpty(id)) {
			throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.ID_REQUIRED), null);
		}
		if (EditorUtils.isNewEditorId(id)) {
			OrderData newOrderDatda = new OrderData();
			newOrderDatda.setOrderDate(new Date());
			List<OrderLineItemData> lineItemList = new ArrayList<OrderLineItemData>();
			OrderLineItemData lineItemData = new OrderLineItemData();
			lineItemData.setLineNumber(1);
			lineItemList.add(lineItemData);
			lineItemData = new OrderLineItemData();
			lineItemData.setLineNumber(2);
			lineItemList.add(lineItemData);

			newOrderDatda.setLineItems(lineItemList);
			data.put(ATTR_ORDER_DATA, newOrderDatda);
		} else {
			// load order
			OrderDataService orderDataService = ServiceFactory.getService(OrderDataService.class, request);
			try {
				OrderData orderData = orderDataService.findById(id);
				if (orderData == null) {
					throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_FIND_ORDER, id), null);
				}
				setTotals(orderData);
				data.put(ATTR_ORDER_DATA, orderData);
			} catch (ScalarServiceException e) {
				throw getActionException(e);
			}
		}

		data.put(Response.TEMPLATE_ATTRIBUTE, "order/ordertemplate");
	}

	/**
	 * Action method to save the item data.
	 *
	 * @param request
	 * @param command
	 * @param data
	 * @throws com.scalar.core.ScalarActionException
	 */
	public void save(Request request, Object command, Map<String, Object> data) throws ScalarActionException, ScalarValidationException {
		OrderData order = new OrderData();
		bindAndValidate(order, (HttpServletRequest)request.getWrappedObject());
		OrderDataService orderDataService = ServiceFactory.getService(OrderDataService.class, request);

		// check whether the request is from new editor
		String editorId = request.getParameter(EditorUtils.EDITOR_ID_ATTRIBUTE);
		if (logger.isDebugEnabled()) {
			logger.debug("item editor id: " + editorId);
		}
		boolean isNew = EditorUtils.isNewEditorId(editorId);
		if (isNew) {
			try {
				setDefaults(order, isNew, request);
				if (orderDataService.exists(order.getOrderNumber())) {
					throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ActionResource.BASE_NAME, ActionResource.ORDER_NUMBER_ALREADY_EXISTS, order.getOrderNumber()), null);
				}
			} catch (ScalarException e) {
				throw getActionException(e);
			}
			order.setId(null);
		} else {
			// check do we need to set the id?
			order.setId(editorId);
		}
		try {
			orderDataService.insertOrUpdate(order);
		} catch (ScalarServiceException e) {
			throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ActionResource.BASE_NAME, ActionResource.UNABLE_TO_SAVE), e);
		}
		try {
			order = orderDataService.findById(order.getId());
		} catch (ScalarServiceException e) {
			throw getActionException(e);
		}
		data.put(Response.ITEM_ATTRIBUTE, order.toMap());
	}

	protected void validate(Object command, BindException errors) throws ScalarValidationException {
		super.validate(command, errors);
		OrderData order = (OrderData)command;

		List<OrderLineItemData> lineitems = order.getLineItems();
		if (lineitems != null) {
			// check whether the lineitems have items and qty
			int lineNumber = 0;
			Iterator<OrderLineItemData> itr = lineitems.iterator();
			while (itr.hasNext()) {
			//for (OrderLineItemData lineItem: lineitems) {
				OrderLineItemData lineItem = itr.next();
				lineNumber++;
				String itemId = lineItem.getItemId();
				int qty = lineItem.getQty();
				if (StringUtil.isEmpty(itemId) && (qty == 0)) {
					// remove this lineitem
					itr.remove();
				} else if (StringUtil.isEmpty(itemId) || (qty==0)) {
					throw ScalarValidationException.create(MsgObjectUtil.getMsgObject(ActionResource.BASE_NAME, ActionResource.ORDER_LINEITEM_NO_QTY_OR_PRICE, lineNumber+""), null);
				}
			}
		}

		// does order has lineitems?
		if (StringUtil.isEmpty(lineitems)) {
			throw ScalarValidationException.create(MsgObjectUtil.getMsgObject(ActionResource.BASE_NAME, ActionResource.ORDER_LINEITEMS_EMPTY), null);
		}
	}

	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws ScalarActionException {
		super.onBindAndValidate(request, command, errors);
		OrderData order = (OrderData)command;
		setLineNumbersAndTotals(order);
	}

	private void setLineNumbersAndTotals(OrderData orderData) {
		List<OrderLineItemData> lineitems = orderData.getLineItems();
		int lineNumber = 0;
		for (OrderLineItemData lineItem: lineitems) {
			lineNumber++;
			lineItem.setLineNumber(lineNumber);
		}
		setTotals(orderData);
	}

	private void setTotals (OrderData orderData) {
		List<OrderLineItemData> lineitems = orderData.getLineItems();
		double totalAmt = 0d;
		for (OrderLineItemData lineItem: lineitems) {
			lineItem.setAmount(lineItem.getQty() * lineItem.getPrice());
			totalAmt += lineItem.getAmount();
		}
		orderData.setTotalAmount(totalAmt);
		// calculate tax
		double taxAmt = (totalAmt * orderData.getTaxPercentage())/100;
		orderData.setTaxAmount(taxAmt);
		double grandTotalAmt = totalAmt + taxAmt;
		orderData.setGrandTotal(grandTotalAmt);
	}

	private List<Map<String, Object>> convertToMap(List<Item> items) {
		List<Map<String, Object>> itemsList = new ArrayList<Map<String, Object>>();
		for (Item item : items) {
			Map<String, Object> itemMap = item.toMap();
			itemMap.put ("groupName", item.getGroupData()==null ? null : item.getGroupData().getName()); //hack for the pqgrid not supporting nested properties
			itemsList.add(itemMap);
		}

		return itemsList;
	}

	@Override
	 public Capability[] getRequiredCapabilities(Request request) {
		if ("load".equals(request.getMethod())) {
			return new Capability[]{Capability.ITEM_READ};
		} else if ("save".equals(request.getMethod())) {
			return new Capability[]{Capability.ITEM_WRITE};
		}

		return new Capability[0];
	}

	private void setDefaults(OrderData order, boolean isNew, Request request) throws ScalarException {
		if (isNew) {
			if (StringUtil.isEmpty(order.getCustName())) {
				order.setCustName(OrderData.DEFAULT_CUST_NAME);
			}
			if (StringUtil.isEmpty(order.getOrderNumber())) {
					// generate order number
				order.setOrderNumber(AutoNumber.generateOrderNumber(request));
			}
			if (order.getOrderDate() == null) {
				order.setOrderDate(new Date());
			}
			if (order.getStatus() == null) {
				order.setStatus(OrderData.OrderStatus.ACTIVE);
			}
		}
	}

}