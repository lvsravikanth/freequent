
fui.provide("fui.ui.manageorders");

fui.require("fui.ui.content");
fui.require("fui.ui.type");

fui.ui.manageorders = {

    ID_ATTRIBUTE: "id",

	/**
	 * The action key
	 */
	ACTION_KEY: "manageorders",

    /**
	 * Methods
	 */
	SEARCH: "runsearch",

	GET_INVOICE_ID: "getinvoiceid",

    /**
     * requires the following data.
     * params.userid
     * params.firstname
     * params.lastname
     *
     * @param data
     */
    runSearch: function(params, requestData){
		if ( fui.log.isDebug() ) { fui.log.debug("executing user search" ); }

		requestData = requestData || {};
		requestData.content = requestData.content || {};

        requestData.handler = this.getRunSearchHandler();

		var c = fui.ui.content;
		c.internal.sendAPI(this, this.SEARCH, requestData);
	},

    getRunSearchHandler: function() {
        return function(data) {
            var grid = fui.grid.find(fui.ui.type.ORDER);
            grid.updateData(data);
        };
    },

	/**
	 * Returns the params object
	 */
	getSearchParams: function() {
		return fui.ui.manageorders.internal.getSearchParams();
	},

	edit: function(id) {
		var requestData = {
			};
		var editConfig = {
			type: fui.ui.type.ORDER,
			id: id,
			ACTION_KEY: fui.ui.manageorders.ACTION_KEY
		};
		fui.ui.editor.edit(editConfig, requestData);
	},

	makeItemSelect: function(fieldId, placeholder, readOnly) {
		if (fui.ui.manageorders.internal.items === "") {
			fui.ui.manageorders.internal.items = fui.ui.manageitems.findAllItems();
		}

		fui.query("#"+fieldId).select2({
				data:{ results: fui.ui.manageorders.internal.items, text: fui.ui.manageorders.internal.itemformat },
				formatSelection: fui.ui.manageorders.internal.itemformat,
				formatResult: fui.ui.manageorders.internal.itemformat,
				placeholder: placeholder ? placeholder : null,
				allowClear: true,
				dropdownCssClass:"ui-dialog",
				disabled: readOnly || false
			});

		fui.query("#"+fieldId).on("change", function(e) { fui.ui.manageorders.itemChanged(this.id, e.added, e.removed);});
	},

	addLineItem: function(tableId, lineItem) {
		var template = fui.ui.manageorders.internal.getLineItemRowTemplate(tableId);
		var row = template ? template(lineItem) : '';
		fui.query("#"+tableId).append(row);
	},

	itemChanged: function(itemFieldId, addedItem, removedItem) {
		//alert(JSON.stringify({fieldId:fieldId, addedItem:addedItem, removedItem:removedItem}));
		var priceFieldId = itemFieldId.substring(0,itemFieldId.lastIndexOf("itemId"))+"price";
		if (addedItem) {
			// update the item price
			fui.query("#"+priceFieldId).val(addedItem.price).change();
		} else {
			// item removed, hence make item price to zero
			fui.query("#"+priceFieldId).val(0).change();
		}
	},

	calculateTotals: function() {

		var orderTotal = 0;
		var taxPercentage = fui.query("#taxPercentage").val();

		// iterate over items
		fui.query( "input[name$='.itemId']").each(
				function() {
					var itemFieldId = fui.query(this).prop("id");
					var qtyFieldId = itemFieldId.replace("itemId", "qty");
					var priceFieldId = itemFieldId.replace("itemId", "price");
					var amountFieldId = itemFieldId.replace("itemId", "amount");
					var amount = fui.query("#"+qtyFieldId).val() * fui.query("#"+priceFieldId).val();
					fui.query("#"+amountFieldId).val(amount);
					orderTotal = orderTotal + amount;
				}
				);

		// set order total
		fui.query("#totalAmount").val(orderTotal);
		// calculate and set tax amount
		var taxAmount = (orderTotal*taxPercentage)/100;
		fui.query("#taxAmount").val(taxAmount);
		// set grand total
		fui.query("#grandTotal").val(orderTotal+taxAmount);
	},

	print: function(id) {
		var invoiceid = this.getInvoiceId(id);
	   if (invoiceid) {
			// print the invoice
			alert ("invoice id: " + invoiceid);
			//todo
	   }
	},

	getInvoiceId: function(id, requestData) {
		if ( fui.log.isDebug() ) { fui.log.debug("executing createinvoice" ); }
		var invoiceid = "";
		requestData = requestData || {};
		requestData.content = requestData.content || {};
		requestData.content.orderid = id;

        requestData.handler = function(data) {
			invoiceid = data.invoiceid;
		};
		requestData.sync = true;

		var c = fui.ui.content;
		c.internal.sendAPI(this, this.GET_INVOICE_ID, requestData);

		return invoiceid;
	}
};
fui.ui.manageorders.internal = {
  	searchparams: {},
	items: "",

	// Template cache per type
	templateCache: {},

	LINEITEM_ROW: "<tr>"+
					"<td class=\"fui-col fui-col-content\"><span>{{lineNumber}}</span>" +
						"<input type=\"hidden\" name=\"lineItems[{{index}}].lineNumber\" class=\"fui-input\" disabled=\"true\"/></td>" +
					"<td class=\"fui-col fui-col-content\">" +
						"<input type=\"text\" name=\"lineItems[{{index}}].itemId\" id=\"lineItems{{index}}itemId\" class=\"fui-input\"/>"+
						"<script type=\"text/javascript\">" +
							"fui.ready(function() {" +
								"fui.ui.manageorders.makeItemSelect(\"lineItems{{index}}itemId\", fui.workspace.getMessage(\"select\"));"+
							"});" +
						"<\/script>" +
					"</td>" +
					"<td class=\"fui-col fui-col-content\"><input type=\"text\" id=\"lineItems{{index}}qty\" name=\"lineItems[{{index}}].qty\" value=\"0\" onchange=\"fui.ui.manageorders.calculateTotals()\" class=\"fui-input fui-qty-input\"/></td>"+
					"<td class=\"fui-col fui-col-content\"><input type=\"text\" id=\"lineItems{{index}}price\" name=\"lineItems[{{index}}].price\" onchange=\"fui.ui.manageorders.calculateTotals()\" class=\"fui-input fui-price-input\" disabled=\"true\"/></td>"+
					"<td class=\"fui-col fui-col-content\"><input type=\"text\" id=\"lineItems{{index}}amount\" name=\"lineItems[{{index}}].amount\" class=\"fui-input fui-price-input\" disabled=\"true\"/></td>"+
				"</tr>",

	getSearchParams: function() {
		return this.searchparams;
	},
	setSearchParams: function(params) {
		this.searchparams = params;
	},

	itemformat: function(item) {
		return "["+item.code+"]"+item.name;
	},

	getLineItemRowTemplate: function(type) {
		var moi = fui.ui.manageorders.internal;
		// Check cache
		var template;
		template = moi.templateCache[type];
		if ( template ) {
			return template;
		}
		template =  Handlebars.compile (moi.LINEITEM_ROW);
		moi.templateCache[type] = template;
		return template;
	}
};