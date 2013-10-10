
fui.provide("fui.ui.manageusers");

fui.require("fui.ui.content");
fui.require("fui.ui.type");

fui.ui.manageusers = {
	/**
	 * The action key
	 */
	ACTION_KEY: "manageusers",

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
		requestData.content.userid = params.userid || {};
		requestData.content.firstname = params.firstname || {};
		requestData.content.lastname = params.lastname || {};
        requestData.handler = this.getRunSearchHandler();

		var c = fui.ui.content;
		c.internal.sendAPI(this, this.SEARCH, requestData);
	},

    getRunSearchHandler: function() {
        return function(data) {
            var obj = { width: '100%', height: 400, title: "Users", flexHeight: true };
            obj.colModel = [{ title: "User Id", width: 100, dataType: "string", dataIndx: "userid" },
                            { title: "First Name", width: 200, dataType: "string", dataIndx: "firstname" },
                            { title: "Last Name", width: 200, dataType: "string", dataIndx: "lastname" }
                        ];
            obj.dataModel = {
                data: data,
                location: "local",
                sorting: "local",
                paging: "local",
                curPage: 1,
                rPP: 10,
                sortIndx: "firstname",
                sortDir: "up",
                rPPOptions: [1, 10, 20, 30, 40, 50, 100, 500, 1000]
            };
            var usersgrid = fui.query("#fui-workspace-search-results-container").pqGrid(obj);
        };
    }
};