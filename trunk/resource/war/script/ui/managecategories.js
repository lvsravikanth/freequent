
fui.provide("fui.ui.managecategories");

fui.require("fui.ui.content");
fui.require("fui.ui.type");

fui.ui.managecategories = {

    ID_ATTRIBUTE: "id",

	/**
	 * The action key
	 */
	ACTION_KEY: "managecategories",

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
		if ( fui.log.isDebug() ) { fui.log.debug("executing group search" ); }

		requestData = requestData || {};
		requestData.content = requestData.content || {};
		if (params) {
			fui.ui.managecategories.internal.setSearchParams(params);
		} else {
			params = fui.ui.managecategories.internal.getSearchParams();
		}
		requestData.content.name = params.name;
        requestData.handler = this.getRunSearchHandler();

		var c = fui.ui.content;
		c.internal.sendAPI(this, this.SEARCH, requestData);
	},

    getRunSearchHandler: function() {
        return function(data) {
            var grid = fui.grid.find(fui.ui.type.CATEGORY);
            grid.updateData(data);
        };
    },

    /**
	 * Returns the params object
	 */
	getSearchParams: function() {
		return fui.ui.managecategories.internal.getSearchParams();
	},

	edit: function(id) {
		var requestData = {
			};
		var editConfig = {
			type: fui.ui.type.CATEGORY,
			id: id,
			ACTION_KEY: fui.ui.managecategories.ACTION_KEY
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
fui.ui.managecategories.internal = {
  	searchparams: {},

	getSearchParams: function() {
		return this.searchparams;
	},
	setSearchParams: function(params) {
		this.searchparams = params;
	}
};