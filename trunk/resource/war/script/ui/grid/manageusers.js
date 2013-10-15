/**
 * Set up the archive section and our requirements
 */
fui.provide("fui.ui.grid.manageusers");

fui.require("fui.type");

fui.ui.grid.manageusers = {

	ARCHIVE_REFINE_SEARCH_BUTTON_CLS : "vui-workspace-ribbon-search" + " " + "vui-grid-archivesearch-refine",
	ID_SEPERATOR : "-",

	get: function() {
		return new fui.Grid({
			getTitle: function() {
				return "Users";
			},
			getData: fui.scope(this, this.getData),
			getColumnModel: fui.scope(this, this.getColumnModel),
			getHeaderButtons: fui.ui.grid.manageusers.getHeaderButtonsList,
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
                title: "User Id",
                width: 100,
                dataType: "string",
                dataIndx: "userid",
                editable: false
            },
            {
                title: "First Name",
                width: 200,
                dataType: "string",
                dataIndx: "firstname",
                editable: false
            },
            {
                title: "Last Name",
                width: 200,
                dataType: "string",
                dataIndx: "lastname",
                editable: false
            }
        ];
    },

    getSortIndx: function(gridProperties) {
        return "firstname";
    },

	/**
	 * Renderer for row actions.
	 */
	actionsRenderer: function(ui) {
        if ( fui.log.isDebug() ) {
            fui.log.debug("in actions rederer");
        }
        //fui.query("tr[pq-row-indx='0'] > td[pq-col-indx='0'] > div[class='pq-td-div']").append(fui.query("<div></div>").button({icons:{primary:"ui-icon-pencil"}}));
        return (fui.query("<div></div>").button({icons:{primary:"ui-icon-pencil"}})).html();
	},

	/**
	 * Gets the list of header buttons
	 */
	getHeaderButtonsList: function() {

        return [];
	}
};
