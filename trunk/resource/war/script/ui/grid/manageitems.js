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
                title: 'actions',
                dataType: "string",
                dataIndx: "id",
                editable: false,
                render: fui.scope(this, this.actionsRenderer)
            },
            {
                title: "Item Name",
                width: 100,
                dataType: "string",
                dataIndx: "name",
                editable: false
            },
            {
                title: "Unit",
                width: 200,
                dataType: "string",
                dataIndx: "unitData.name",
                editable: false
            },
            {
                title: "Group",
                width: 200,
                dataType: "string",
                dataIndx: "groupData.name",
                editable: false
            }
        ];
    },

    getSortIndx: function(gridProperties) {
        return "name";
    },

	/**
	 * Renderer for row actions.
	 */
	actionsRenderer: function(ui) {
        if ( fui.log.isDebug() ) {
            fui.log.debug("in actions rederer");
        }
        //fui.query("tr[pq-row-indx='0'] > td[pq-col-indx='0']").append(fui.query("<div></div>").button({icons:{primary:"ui-icon-pencil"}}));
		var template = '<button onclick="javascript: fui.ui.manageitems.edit(\''+ui.rowData.id+'\');" onmouseover="javascript: fui.query(this).addClass( \'ui-state-hover\' )" onmouseout="javascript: fui.query(this).removeClass( \'ui-state-hover\' )" class="fui-actions-button ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary" role="button" aria-disabled="false">' +
					   	'<span class="ui-button-icon-primary ui-icon ui-icon-pencil"></span><span class="ui-button-text"></span>' +
					   '</button>';
		return template;
	},

	/**
	 * Gets the list of header buttons
	 */
	getHeaderButtonsList: function() {

        return [];
	}
};
