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
            getSortIndx: fui.scope(this, this.getSortIndx),
			refresh: function() {
				fui.ui.manageusers.runSearch();
			}
		});
	},

    getData: function(gridProperties) {
        return [];
    },

    getColumnModel: function(grid, gridProperties) {
            return [
                {
                    title: 'Actions',
                    dataType: "string",
                    width: 150,
                    dataIndx: "id",
                    editable: false,
                    render: fui.grid.rowaction.renderer // fui.ui.grid.manageusers.actionsRenderer
                },
                {
                    title: "User Id",
                    width: 150,
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
        //fui.query("tr[pq-row-indx='0'] > td[pq-col-indx='0']").append(fui.query("<div></div>").button({icons:{primary:"ui-icon-pencil"}}));
		var template = '<button onclick="javascript: fui.ui.manageusers.edit(\''+ui.rowData.userid+'\');" onmouseover="javascript: fui.query(this).addClass( \'ui-state-hover\' )" onmouseout="javascript: fui.query(this).removeClass( \'ui-state-hover\' )" class="fui-actions-button ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary" role="button" aria-disabled="false">' +
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
