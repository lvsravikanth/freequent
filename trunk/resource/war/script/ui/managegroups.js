
fui.provide("fui.ui.managegroups");

fui.require("fui.ui.content");
fui.require("fui.ui.type");

fui.ui.managegroups = {

    ID_ATTRIBUTE: "id",

	/**
	 * The action key
	 */
	ACTION_KEY: "managegroups",

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
			fui.ui.managegroups.internal.setSearchParams(params);
		} else {
			params = fui.ui.managegroups.internal.getSearchParams();
		}
		requestData.content.name = params.name;
        requestData.handler = this.getRunSearchHandler();

		var c = fui.ui.content;
		c.internal.sendAPI(this, this.SEARCH, requestData);
	},

    getRunSearchHandler: function() {
        return function(data) {
            var grid = fui.grid.find(fui.ui.type.GROUP);
            grid.updateData(data);
        };
    },

    /**
	 * Returns the params object
	 */
	getSearchParams: function() {
		return fui.ui.managegroups.internal.getSearchParams();
	},

	edit: function(id) {
		var requestData = {
			};
		var editConfig = {
			type: fui.ui.type.GROUP,
			id: id,
			ACTION_KEY: fui.ui.managegroups.ACTION_KEY
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
fui.ui.managegroups.internal = {
  	searchparams: {},

	getSearchParams: function() {
		return this.searchparams;
	},
	setSearchParams: function(params) {
		this.searchparams = params;
	}
};