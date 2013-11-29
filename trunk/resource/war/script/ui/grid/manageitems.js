/**
 * Set up the archive section and our requirements
 */
fui.provide("fui.ui.grid.manageitems");

fui.require("fui.type");

fui.ui.grid.manageitems = {

	ID_SEPERATOR : "-",

	get: function() {
		return new fui.Grid({
			getTitle: function() {
				return "Items";
			},
			getData: fui.scope(this, this.getData),
			getColumnModel: fui.scope(this, this.getColumnModel),
			getHeaderButtons: fui.ui.grid.manageitems.getHeaderButtonsList,
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
                title: fui.workspace.getMessage("item.name"),
                width: 100,
                dataType: "string",
                dataIndx: "name",
                editable: false
            },
			{
                title: fui.workspace.getMessage("code"),
                width: 100,
                dataType: "string",
                dataIndx: "code",
                editable: false
            },
            {
                title: fui.workspace.getMessage("group"),
                width: 200,
                dataType: "string",
                dataIndx: "groupName",
                editable: false
            },
			{
                title: fui.workspace.getMessage("price"),
                width: 200,
                dataType: "float",
                dataIndx: "price",
                editable: false
            },
			{
                title: fui.workspace.getMessage("taxable"),
                width: 200,
                dataType: "string",
                dataIndx: "taxable",
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
