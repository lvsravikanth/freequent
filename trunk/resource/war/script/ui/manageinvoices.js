
fui.provide("fui.ui.manageinvoices");

fui.require("fui.ui.content");
fui.require("fui.ui.type");

fui.ui.manageinvoices = {

    ID_ATTRIBUTE: "id",

	/**
	 * The action key
	 */
	ACTION_KEY: "manageinvoices",

    /**
	 * Methods
	 */
	SEARCH: "runsearch",

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
            var grid = fui.grid.find(fui.ui.type.INVOICE);
            grid.updateData(data);
        };
    },

	/**
	 * Returns the params object
	 */
	getSearchParams: function() {
		return fui.ui.manageinvoices.internal.getSearchParams();
	},

	edit: function(id) {
		var requestData = {
			};
		var editConfig = {
			type: fui.ui.type.INVOICE,
			id: id,
			ACTION_KEY: fui.ui.manageinvoices.ACTION_KEY
		};
		fui.ui.editor.edit(editConfig, requestData);
	},

	makeItemSelect: function(fieldId, placeholder, readOnly) {
		if (fui.ui.manageinvoices.internal.items === "") {
			fui.ui.manageinvoices.internal.items = fui.ui.manageitems.findAllItems();
		}

		fui.query("#"+fieldId).select2({
				data:{ results: fui.ui.manageinvoices.internal.items, text: fui.ui.manageinvoices.internal.itemformat },
				formatSelection: fui.ui.manageinvoices.internal.itemformat,
				formatResult: fui.ui.manageinvoices.internal.itemformat,
				placeholder: placeholder ? placeholder : null,
				allowClear: true,
				dropdownCssClass:"ui-dialog",
				disabled: readOnly || false
			});
	},

	print: function(id) {
		var printURL = fuiConfig.appContext + "/report/printreport?id=" + id + "&reportname="+fui.ui.type.INVOICE;
		fui.openWindow(printURL);
	}
};
fui.ui.manageinvoices.internal = {
  	searchparams: {},
	items: "",

	getSearchParams: function() {
		return this.searchparams;
	},
	setSearchParams: function(params) {
		this.searchparams = params;
	},

	itemformat: function(item) {
		return "["+item.code+"]"+item.name;
	}
};