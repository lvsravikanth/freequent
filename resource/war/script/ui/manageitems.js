
fui.provide("fui.ui.manageitems");

fui.require("fui.ui.content");
fui.require("fui.ui.type");

fui.ui.manageitems = {

    ID_ATTRIBUTE: "id",

	/**
	 * The action key
	 */
	ACTION_KEY: "manageitems",

    /**
	 * Methods
	 */
	SEARCH: "runsearch",
	FINDALLITEMS: "findallitems",

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
		if (params) {
			fui.ui.manageitems.internal.setSearchParams(params);
		} else {
			params = fui.ui.manageitems.internal.getSearchParams();
		}
		requestData.content.name = params.name;
		requestData.content.group = params.group;
		requestData.content.category = params.category;
        requestData.handler = this.getRunSearchHandler();

		var c = fui.ui.content;
		c.internal.sendAPI(this, this.SEARCH, requestData);
	},

    getRunSearchHandler: function() {
        return function(data) {
            var grid = fui.grid.find(fui.ui.type.ITEMS);
            grid.updateData(data);
        };
    },

	edit: function(id) {
		var requestData = {
			};
		var editConfig = {
			type: fui.ui.type.ITEM,
			id: id,
			ACTION_KEY: fui.ui.manageitems.ACTION_KEY
		};
		fui.ui.editor.edit(editConfig, requestData);
	},

	findAllItems: function(requestData) {
		if ( fui.log.isDebug() ) { fui.log.debug("executing loadItems" ); }
		var items = [];
		requestData = requestData || {};
		requestData.content = requestData.content || {};
        requestData.handler = function(data){
			items = data;
		};
		requestData.sync = true;

		var c = fui.ui.content;
		c.internal.sendAPI(this, this.FINDALLITEMS, requestData);
		return items;
	}
};
fui.ui.manageitems.internal = {
  	searchparams: {},

	getSearchParams: function() {
		return this.searchparams;
	},
	setSearchParams: function(params) {
		this.searchparams = params;
	}
};