
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
	}
};
fui.ui.manageorders.internal = {
  	searchparams: {},

	getSearchParams: function() {
		return this.searchparams;
	},
	setSearchParams: function(params) {
		this.searchparams = params;
	}
};