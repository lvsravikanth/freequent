/**
 * Set up the archive section and our requirements
 */
fui.provide("fui.ui.grid.manageorders");

fui.require("fui.type");

fui.ui.grid.manageorders = {

	ID_SEPERATOR : "-",

	get: function() {
		return new fui.Grid({
			getTitle: function() {
				return "Order";
			},
			getData: fui.scope(this, this.getData),
			getColumnModel: fui.scope(this, this.getColumnModel),
			getHeaderButtons: fui.ui.grid.manageorders.getHeaderButtonsList,
			setupGrid: fui.ui.grid.setupGrid,
            getSortIndx: fui.scope(this, this.getSortIndx),
			getDataType: function(gridProperties) {
				return "text";
			},
			getResponseDataFunc: function(gridProperties) {
				return function( response, textStatus, jqXHR ){
					var dataJSON = fui.transport.json.parse(response).responses[0];
					dataJSON = fui.secure_eval(dataJSON.content);
					return { curPage: this.curPage, totalRecords: dataJSON.total, data: dataJSON.items };
				};
            },
			getErrorFunc: function(gridProperties) {
				return function( jqXHR, textStatus, errorThrown ){
					var msg = fui.transport.json.parse(jqXHR.responseText).errors[0].message || errorThrown;
					fui.msg.alert(fui.workspace.getMessage('error'), msg);
				};
			},
            getUrlFunc: function(gridProperties) {
                return function(){
					var url = fuiConfig.appContext + "/" + fui.ui.manageorders.ACTION_KEY + "/" + fui.ui.manageorders.SEARCH + ".json";
					var params = fui.query.param(fui.ui.manageorders.getSearchParams());
					return { url: url, data: params };
				};
            },
			getIsDataRemote: function(gridProperties){ return true; }
		});
	},

    getData: function(gridProperties) {
        return null;
    },

    getColumnModel: function(grid, gridProperties) {
        return [
            {
                title: fui.workspace.getMessage("actions"),
				width: 100,
                dataType: "string",
                dataIndx: "id",
                editable: false,
                render: fui.grid.rowaction.renderer
            },
            {
                title: fui.workspace.getMessage("order.number"),
                width: 200,
                dataType: "string",
                dataIndx: "orderNumber",
                editable: false
            },
			{
                title: fui.workspace.getMessage("status"),
                width: 100,
                dataType: "string",
                dataIndx: "status",
                editable: false
            },
			{
                title: fui.workspace.getMessage("order.date"),
                width: 100,
                dataType: "string",
                dataIndx: "orderDate",
                editable: false,
				render: fui.ui.grid.dateRenderer
            },
			{
                title: fui.workspace.getMessage("revision"),
                width: 100,
                dataType: "integer",
                dataIndx: "revision",
                editable: false
            }
        ];
    },

    getSortIndx: function(gridProperties) {
        return "orderNumber";
    },


	/**
	 * Gets the list of header buttons
	 */
	getHeaderButtonsList: function() {

        return [];
	}
};
