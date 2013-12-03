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
		// Set up content event handling
		var gr = fui.ui.grid;
		var eventFunc = gr.getContentEventHandler(grid);
		fui.ui.content.event.subscribe(eventFunc);
		// hack overwrite the width after rendered
		fui.query(grid).css('width', 'auto');
	},

	/**
	 * Gets the list of header buttons
	 *
	 * @param idSuffix optional for distinguishing buttons
	 */
	getHeaderButtonsList: function(idSuffix) {

	},
	getContentEventHandler: function(grid) {
		return function(domEvent, fuiEvent) {
			var reload = false;
			//var store = grid.getStore();

			var i, o, rec, id;
			switch ( fuiEvent.type ) {
				case fui.ui.content.event.CREATE:
					reload = true;
					break;
				case fui.ui.content.event.UPDATE:
				case fui.ui.content.event.DELETE:
					reload = true;
					/*for ( i = 0 ; i < fuiEvent.objects.length; ++i ) {
						o = fuiEvent.objects[i];
						id = o.contentManagementId || o.id;
						rec = store.getById(id);
						if ( rec ) {
							reload = true;
							break;
						}
					}*/
					break;
			}

			if ( reload ) {
				//store.load(); //todo
				fui.query( grid ).pqGrid( "refresh" );
			}
		};
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
