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
            getSortIndx: fui.scope(this, this.getSortIndx)
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
                dataIndx: "id",
                editable: false,
                render: fui.grid.rowaction.renderer
            },
            {
                title: fui.workspace.getMessage("order.number"),
                width: 100,
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
