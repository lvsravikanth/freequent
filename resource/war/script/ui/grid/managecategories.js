/**
 * Set up the archive section and our requirements
 */
fui.provide("fui.ui.grid.managecategories");

fui.require("fui.type");

fui.ui.grid.managecategories = {

	ID_SEPERATOR : "-",

	get: function() {
		return new fui.Grid({
			getTitle: function() {
				return "Categories";
			},
			getData: fui.scope(this, this.getData),
			getColumnModel: fui.scope(this, this.getColumnModel),
			getHeaderButtons: fui.ui.grid.managecategories.getHeaderButtonsList,
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
					var url = fuiConfig.appContext + "/" + fui.ui.managecategories.ACTION_KEY + "/" + fui.ui.managecategories.SEARCH + ".json";
					var params = fui.query.param(fui.ui.managecategories.internal.getSearchParams());
					return { url: url, data: params };
				};
            },
			getIsDataRemote: function(gridProperties){ return true; }
		});
	},

    getData: function(gridProperties) {
        return [];
    },
    
    getColumnModel: function(grid, gridProperties) {
        return [
            {
                title: fui.workspace.getMessage("actions"),
                dataType: "string",
                width: 120,
                dataIndx: "id",
                editable: false,
                render: fui.grid.rowaction.renderer
            },
            {
                title: fui.workspace.getMessage("item.name"),
                width: 200,
                dataType: "string",
                dataIndx: "name",
                editable: false
            }
        ];
    },

    getSortIndx: function(gridProperties) {
        return "name";
    },


	/**
	 * Gets the list of header buttons
	 */
	getHeaderButtonsList: function() {

        return [];
	}
};
