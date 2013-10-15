/**
 * Set up the grid section and our requirements
 */
fui.provide("fui.ui.grid");

fui.ui.grid = {

	/**
	 * CSS class for styling grid column header.
	 */
	GRID_COLUMN_HEADER_CLASS: 'vui-grid-hdr-contextmenu-clearfilters',

	getColumnModel: function(grid, gridProperties) {
		var vWs = fui.ui.workspace;
		var vGrid = fui.ui.grid;
		var ra = vGrid.rowaction;
		var vgi = vGrid.internal;

		var columns = [
			{
                title: 'actions',
                dataType: "string",
                dataIndx: "id",
                editable: false,
                render: vGrid.actionsRenderer
            }
		];

		grid.columns = columns;
		return grid.columns;
	},

    actionsRenderer: function(ui) {

    },

	setupGrid: function(grid, gridConfig, config) {
	},

	/**
	 * Gets the list of header buttons
	 *
	 * @param idSuffix optional for distinguishing buttons
	 */
	getHeaderButtonsList: function(idSuffix) {

	}
};

/**
 * Set up the internal section and our requirements
 */
fui.provide("fui.ui.grid.internal");

fui.ui.grid.internal = {
	/**
	 * Updates the contents of the grid based on selection.
	 *
	 * @param grid The grid to update.
	 * @param store The new store to use.
	 * @param rootId The root ID.
	 * @param text The displayed title text.
	 * @param params additional grid params
	 */
	updateGrid: function(grid, store, rootId, text, params) {
		fui.grid.internal.updateGrid(grid, store, rootId, text, params);
	}
};
