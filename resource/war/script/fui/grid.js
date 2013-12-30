/**
 * @desc Set up the grid section and our requirements
 *
 * @namespace This is the grid namespace
 * @name vui.ui.grid
 * @see Grid
 * @public
 */
fui.provide("fui.grid");
fui.provide("fui.Grid");
fui.provide("fui.GridUI");

/**
 * @desc Static functions and constants.
 * @public
 */
fui.grid = {
	/**
	 * @constant
	 * @desc Paging Size.
	 * @public
	 */
	PAGING_SIZE: 20,  // default--overwritten using the value from prefs

    DEFAULT_DATA_TYPE: "JSON",

    WIDTH: "100%",

    HEIGHT: "400",

    /**
     * Minimum possible width of the columns in pixels.
     */
    minWidth: 50,

    /**
     * whether row number are displayed in the grid
     */
    numberCell: false,

	/**
	 * @constant
	 * @desc No Spinner Message Mask CSS class.
	 * @public
	 */
	MASK_NO_SPINNER_CLASS: 'vui-message-mask-no-spinner',

	/**
	 * @function
	 * @desc Registers a grid.
	 *
	 * @param type the type
	 * @param grid the grid to register
	 * @public
	 */
	register : function(type, grid) {
		this.internal.add(type, grid);
	},

	/**
	 * Unregisters a grid by type.
	 *
	 * @param type the type
	 */
	unregister : function(type) {
		this.internal.remove(type);
	},

	/**
	 * @function
	 * @desc Finds a grid by type.
	 *
	 * @param type the type
	 * @returns grid
	 * @public
	 */
	find : function(type) {
		var grid = this.internal.find(type);
		if ( !grid ) {
			if ( fui.log.isError() ) { fui.log.error("Unable to find grid: " + type); }
		}

		return grid;
	},

	/**
	 * @function
	 * @desc Sets or unsets a mask on the workspace's central region, which contains the workspace grid.
	 * @param on true to mask, false to unmask
	 * @param maskData optional, allows to specify the content of the masking message. maskData={line1:..,line2:..}
	 * @public
	 */
	setGridMask: function(on, maskData){
		var centerRegion = fui.ext.get(fui.workspace.internal.CENTER_REGION_ID);
		if(centerRegion){
			if(on){
				maskData = maskData || {};
				var maskMsg = "<div id='vui-typesws-message-mask-id' class='vui-message-mask-text'>";
				var line1 = maskData.line1 || fui.workspace.getMessage('filter.mask.msg.line.one');
				maskMsg += line1;
				var line2 = maskData.line2 || fui.workspace.getMessage('filter.mask.msg.line.two');
				if(line2){
					maskMsg += '<br>';
					maskMsg += line2;
				}
				maskMsg += "</div>";
				centerRegion.mask(maskMsg, 'vui-message-mask ' + fui.grid.MASK_NO_SPINNER_CLASS);
			} else {
				centerRegion.unmask();
			}
		}
	},



	/**
	 * @function
	 * @desc
	 * The main function to call to get a standard grid of objects. <br/>
	 * The required config object recognizes the follow attributes. <br/>
	 * <ul>
	 * <li>- type: root container's object type	  </li>
	 * <li>- rootId: ID of the root container object (required) </li>
	 * <li>- gridId: DOM element ID for the grid (defaults to none)  </li>
	 * <li>- stateKey: arbitrary identifier under which to save grid display</li>
	 *	 state; all grids using the same key will share the same state </li>
	 *	 settings (optional, if not specified, calls getStateKey() </li>
	 *	 from the gridConfig, which itself can be overriden when</li>
	 *	 constructing a fui.Grid)	</li>
	 *<li> - gridTitle: title bar for the grid (defaults to system-supplied </li>
	 *	 text based on the type value)	   </li>
	 * <li>- gridOnLoad: callback for the grid store's 'load' event, </li>
	 *	 called after new Records have been loaded</li>
	 * <li>- gridSkipLoad: skip the grid loading</li>
	 * <li>- gridSkipTitle: skip the grid title </li>
	 * <li>- gridSkipHeaderButtons: skip the grid header buttons </li>
	 * <li>- infiniteScrolling: true/false to indicate whether the grid should support
	 *  infinite scrolling. </li>
	 * <li>- objectTypes: array of object types (XML name) to display; by</li>
	 *	 default, objects of all types are displayed</li>
	 * <li>- hideMode: if true, objectTypes contains types to hide instead of</li>
	 *	display</li>
	 * <li>- excludedIds: array of object type ID's to hide</li>
	 * <li>- hideInactiveChannels:  true to hide inactive channels</li>
	 * <li>- additionalProperties: holds instance-specific variables</li>
	 * <li>- disableDoubleClick: true to disable any double-click row features</li>
	 *	 that are available in the grid (defaults to false)</li>
	 * <li>- destroyExisting: true to check for an existing grid (using the gridId configuration)</li>
	 *	 and destroy it</li>
	 * <li>- uiConfig: a secondary config object pertaining to UI-specific</li>
	 *	 attributes; notable value(s)
	 * <ul>
	 *	 <li>- cls: extra CSS class(es) to add to the tree DOM element</li>
	 *	 <li>- header: true or false, to identify if a panel header should
	 *		  be created (defaults to true) </li>
	 *	 <li>- sm: a custom SelectionModel, or false to use the default<br/>
	 *		  ExtJS selection model, no checkbox (defaults to <br/>
	 *		  CheckBoxSelectionModel)</li>
	 *	 <li>- cm: a custom ColumnModel</li>
	 *	 <li>- skipToolbar: true or false, to skip the creation and  <br/>
	 *		  population of the toolbar and context menus<br/>
	 *		  (defaults to false)  </li>
	 *	 <li>- contextMenuShadow: a custom value for menu shadow (defaults to true)</li>
	 * 	 <li>- showContextMenu: a custom contexual menu<li>
	 * </ul>
	 * </ul>
	 *
	 * @param config Config object as briefly described above.
	 * @returns A vExt.grid.GridPanel object, or null
	 * @public
	 */
	getGrid: function(config) {
		var gridConfig = this.find(config.type);
		if ( !gridConfig ) {
			return null;
		}

		//check if we should destroy the existing grid
		if (config.gridId && config.destroyExisting===true){
			var tmpGrid = fui.query(config.gridId);
			if (tmpGrid){
				tmpGrid.destroy();
			}
		}

		// Copy title
		config.gridTitle = config.gridTitle || gridConfig.getTitle();

		gridConfig.pagingSize = gridConfig.pagingSize || this.PAGING_SIZE;

		var gridUI = this.internal.buildGrid(gridConfig, config);
		return gridUI;
	},

	/**
	 * @function
	 * @desc grid button ids must have a unique identifier, which is built by using the BUTTON_ID constant and the grid id.
	 * The typical format for a button grid id is GRID_ID-BUTTON_ID
	 * @param buttonIdPrefix the BUTTON_ID value of a given button
	 * @param gridId the id of the grid
	 * @returns the id of the grid button by combining the BUTTON_ID and the grid id
	 * @public
	 */
	getButtonId: function(buttonIdPrefix, gridId){
		return gridId + "-" + buttonIdPrefix;
	},

	/**
	 * @function
	 * @desc context menu ids must have a unique identifier, which is built by using the BUTTON_ID constant and the grid id.
	 * The typical format for a context menu item id is GRID_ID-context-BUTTON_ID
	 * @param buttonIdPrefix the BUTTON_ID value of a given button used as a menu item
	 * @param gridId the id of the grid
	 * @returns the id of the grid button by combining the BUTTON_ID and the grid id
	 * @public
	 */
	getContextMenuItemId: function(buttonIdPrefix, gridId){
		return gridId + "-context-" + buttonIdPrefix;
	}
};


/**
 * @constructor
 * @name Grid
 * @desc setups the widget
 * @public
 */
fui.Grid= function() {
	if ( (arguments.length == 1) && (typeof arguments[0] == "object") ) {
		fui.combine(this, arguments[0]);
	}
};


/**
 * @desc Grid
 */
fui.extend(fui.Grid,
		/**
		 @lends Grid.prototype
		 */
		{


			/**
			 * @constant
			 * @desc The number of items per page within the grid.
			 * @public
			 */
			pagingSize: null,

			/**
			 * @function
			 * @desc The title getter: overridden in the extenders.
			 * @public
			 */
			getTitle: function() { return ''; },

			/**
			 * @function
			 * @desc The grid data store getter: overridden in the extenders.
			 * @public
			 */
			getData: function() { return null; },

			/**
			 * @function
			 * @desc The column model getter: overridden in the extenders.
			 * @public
			 */
			getColumnModel: function() { return null; },

			/**
			 * @function
			 * @desc the setup grid function: overridden in the extenders.
			 * @public
			 */
			setupGrid: function(grid) {},

			/**
			 * @function
			 * @desc the header buttons getter: overridden in the extenders.
			 * @public
			 */
			getHeaderButtons: function() { return null; },

            refresh: function() {
				fui.query("#"+this.fuiWorkspaceConfig.gridId).pqGrid( "refreshDataAndView" );
			},

            getSelectionModel: function() {
                return { type: 'row' , mode: 'range' };
            },

            getCurPage: function(gridProperties) {
                return 1;
            },

            getDataType: function(gridProperties) {
                return fui.grid.DEFAULT_DATA_TYPE;
            },

            getResponseDataFunc: function(gridProperties) {
                return null;
            },
			getErrorFunc: function(gridProperties) {
				return null;
			},
            getUrlFunc: function(gridProperties) {
                return null;
            },

            getIsDataRemote: function(gridProperties) {
                return false;
            },
			getSortable: function(gridProperties) {
				return true;
			},
            getIsSortingRemote: function(gridProperties) {
                return false;
            },
            getIsPagingRemote: function(gridProperties) {
                return false;
            },
            getSortDirection: function(gridProperties) {
                return "up"; //"down"
            },
            getSortIndx: function(gridProperties) {
                return null;
            },
            updateData: function(data) {
				this.showBlock();
                fui.grid.internal.updateData(this, data);
				this.clearBlock();
            },
			/**
			 * @function
			 * @desc Shows an interface block.
			 *
			 * @param uiConfig the block UI configuration
			 * @public
			 */
			showBlock: function(uiConfig) {
				uiConfig = uiConfig || {};

				// Already showing
				if ( this.blocked++ > 0 ) {
					return;
				}

				// Get block message
				var msg = uiConfig.blockMessage || fui.editor.getMessage('loading');

				// Mask
				if ( this.ui) {
					this.ui.parent().mask(msg);
				} else {
					fui.query("body").mask(msg);
				}
				// Set timer if needed
				if ( uiConfig.setTimer ) {
					this.blockTimer = setTimeout(fui.scope(this, function(){
						this.clearBlock();

						// Allow for callback
						if ( uiConfig.timeoutHandler ) {
							uiConfig.timeoutHandler();
						}

					}), uiConfig.blockTime || 5000);
				}
			},

			/**
			 * @function
			 * @desc Clears the interface block.
			 * @public
			 */
			clearBlock: function() {
				if ( (this.blocked === 0) || (--this.blocked > 0) ) {
					return;
				}

				if ( this.blockTimer ) {
					clearInterval(this.blockTimer);
					this.blockTimer = null;
				}

				// Unmask
				if ( this.ui ) {
					if (this.ui) {  // TODO: fix this for the editor save scenario
						this.ui.parent().unmask();
					}
				} else {
					var body = fui.query("body");
					body.unmask();
				}
			}

		});



/**
 * Set up the internal section and our requirements
 */
fui.provide("fui.grid.internal");

fui.grid.internal = {
	/**
	 * Grids.
	 */
	grids: {},

	/**
	 * Add a grid.
	 *
	 * @param type the type
	 * @param grid the grid to add
	 */
	add: function(type, grid) {
		this.grids[type] = grid;
	},

	/**
	 * Remove a grid.
	 *
	 * @param type the type of the grid to remove
	 */
	remove: function(type) {
		delete this.grids[type];
	},

	/**
	 * Find a grid by type
	 *
	 * @param type the type of the grid to find
	 */
	find: function(type) {
		return this.grids[type];
	},

	buildAdditionalUI: function(gridConfig, gridUIConfig, config) {
		var gi = fui.grid.internal;
		// Grid buttons
		/*var buttons = fui.grid.button.getButtons(config);
		for ( var i = 0 ; i < buttons.length ; ++i ) {
			var button = buttons[i];
			if ( button.isGroup ) {
				gi.buildGroupButton(button, gridUIConfig);
			} else {
				gi.buildToolbarButton(button, gridUIConfig);
			}
		}*/
	},

	buildGrid: function(gridConfig, config) {
		var uiConfig = config.uiConfig;
		var id = config.gridId;
		var additionalProperties = config.additionalProperties;
		var gridProperties = additionalProperties && additionalProperties.grid ? additionalProperties.grid : additionalProperties;

        var data = gridConfig.getData(gridProperties);
        var title = gridConfig.getTitle(gridProperties);
        var responseDataFunc = gridConfig.getResponseDataFunc(gridProperties);
		var errorFunc = gridConfig.getErrorFunc(gridProperties);
        var urlFunch = gridConfig.getUrlFunc(gridProperties);
        var dataRemote = gridConfig.getIsDataRemote(gridProperties) ? "remote" : "local";
        var sortingRemote = gridConfig.getIsSortingRemote(gridProperties) ? "remote" : "local";
        var pagingRemote = gridConfig.getIsPagingRemote(gridProperties) ? "remote" : "local";
        var dataModel = {
            curPage: gridConfig.getCurPage(gridProperties),
            data: data,
            dataType: gridConfig.getDataType(gridProperties),
            getData: responseDataFunc,
			error: errorFunc,
            getUrl: urlFunch,
            location: dataRemote,
            sorting: sortingRemote,
            paging: pagingRemote,
            rPP: gridConfig.pagingSize,
            rPPOptions: [1, 10, 20, 30, 40, 50, 100, 500, 1000],
            sortDir: gridConfig.getSortDirection(gridProperties),
            sortIndx: gridConfig.getSortIndx(gridProperties)
        };
		var gridUIConfig = {
			dataModel: dataModel,
			title: title,
            width: fui.grid.WIDTH,
            height: fui.grid.HEIGHT,
            minWidth: fui.grid.minWidth,
            numberCell: fui.grid.numberCell,
            resizable: false,
            sortable: gridConfig.getSortable(gridProperties)
		};

		// special processing of uiConfig attributes
		var uiConfigSm = null;
		var uiConfigCm = null;
		if (uiConfig) {
			fui.extend(gridUIConfig, uiConfig);

			uiConfigSm = uiConfig.sm;
			uiConfigCm = uiConfig.cm;
		}

		// Column model
		var cm = uiConfigCm || gridConfig.getColumnModel(this, gridProperties);
		var sm = uiConfigSm || gridConfig.getSelectionModel(this, gridProperties);

		fui.combine(gridUIConfig, {
			colModel: cm,
			selectionModel: sm,
			additionalProperties: additionalProperties
		});

		fui.grid.internal.buildAdditionalUI(gridConfig, gridUIConfig, config);

		var grid = fui.query("#"+id).pqGrid(gridUIConfig);
		gridConfig.fuiWorkspaceConfig = config;

		// Allow for gridConfig setup
		if ( gridConfig.setupGrid) {
			gridConfig.setupGrid(grid, gridConfig, config);
		}

		//  fui.grid.internal.registerGridInButtons(grid);

		return grid;
	},

	updateData: function(grid, newData) {
        //getter
        //var oldData=fui.query("#"+grid.fuiWorkspaceConfig.gridId).pqGrid( "option" , "dataModel.data" );

        //setter
        fui.query("#"+grid.fuiWorkspaceConfig.gridId).pqGrid( "option", "dataModel.data", newData );
	}
};
