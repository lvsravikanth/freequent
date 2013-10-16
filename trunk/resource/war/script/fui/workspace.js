/**
 * @desc Setups the workspace base class.
 *
 * @namespace this is the root workspace namespace
 * @name vui.ui.workspace
 * @public
 */
vui.provide("fui.workspace");

vui.require("fui.resource");
fui.require("fui.html");

/**
 * @desc Static functions and constants for the workspace.
 * @public
 */
fui.workspace = {
	/**
	 * @constant
	 * @desc The TYPE key.
	 * @public
	 */
	WORKSPACE_TYPE : 'Workspace',

	/**
	 * Sizing.
	 */
	MINIMUM_WIDTH : 1000,
	MINIMUM_HEIGHT : 400,

	/**
	 * @constant
	 * @desc The resource basename attribute, which corresponds to the same on the server-side.
	 * @public
	 */
	BASENAME : 'fui.workspace.basename.not.set',

	/**
	 * @function
	 * @desc Returns a message for the given key.
	 *
	 * @param key the message key
	 * @returns the resource message value.
	 * @public
	 */
	getMessage : function(key) {
		return fui.resource.getMessage(fui.workspace.BASENAME, key);
	},

	/**
	 * @function
	 * @desc Returns a URL for the given type, id and params.
	 *
	 * @param type the workspace type
	 * @param id the workspace id
	 * @param path the tree path
	 * @public
	 */
	getURL : function(type, id, path) {
		return fui.workspace.internal.buildURL(fui.appContext + '/#', type, id, path);
	},

	/**
	 * Switches to the specified workspace.
	 *
	 * @param type the workspace type
	 * @param id the workspace id
	 * @param path the tree path
	 */
	go : function(type, id, path) {
		// Destroy the properties Window, when switching workspaces.
		if (fui.workspace.internal.propertiesWindow) {
			fui.workspace.hideProperties();
			fui.workspace.internal.propertiesWindow.destroy();
			fui.workspace.internal.propertiesWindow = null;
		}

		var url = fui.workspace.internal.buildURL('', type, id, path);
		fui.loader.loadBody(url);
	},

	/**
	 * Returns the current active config.
	 */
	getActiveConfig: function() {
		return fui.workspace.internal.activeConfig;
	},

	/**
	 * Builds the workspace.
	 */
	build : function() {
		// Build and render
		var workspace = fui.workspace.internal.buildWorkspace();

		fui.workspace.internal.resize();
	},

	/**
	 * Load the workspace.
	 *
	 * @param config
	 */
	load: function(config) {
		var w = fui.workspace;
		//var center = fui.ext.getCmp(w.internal.CENTER_REGION_ID);
		//var west = fui.ext.getCmp(w.internal.WEST_REGION_ID);

		// Friendly default
		config = config || { };

		// make sure things are hidden
		//center.hide();
		//west.hide();

		// Set up our components. Tree first since grid buttons may want to hook it
		//w.setupTree(config);
		w.setupGrid(config);
		if (config.extraSetup) {
			config.extraSetup(config);
		}

		// Save active config
		w.internal.activeConfig = config;

		// Show what we need
		if ( config.grid) {
			//center.show();
		}

		/*if ( config.tree ) {
			west.show();
		}*/
	},

	setupGrid: function(config) {
		// Set our config attributes
		config.gridId = fui.workspace.internal.GRID_ID;

		// Skip grid load if there is a tree since its selection handler will load it
		if ( config.tree === true ) {
			config.gridSkipLoad = true;
			config.gridSkipTitle = true;
		}

		// Create it
		config.uiConfig = config.uiConfig || {};
		config.uiConfig.frame = true;
		//config.uiConfig.pagingToolbarRenderTo = fui.ext.getCmp(fui.workspace.internal.CENTER_REGION_ID).fbar;
		grid = fui.grid.getGrid(config);

		// Show it
		//center.add(grid); //todo
	},


	getGrid: function() {
		return fui.grid.find(fui.workspace.internal.GRID_ID);
	}
};

/**
 * Set up the internal data section and our requirements
 */
fui.provide("fui.workspace.internal");

fui.require("fui.auth");
fui.require("fui.loader");

fui.workspace.internal = {
	/**
	 * Layout constants.
	 */
	HEADER_ID : "fui-workspace-header",
	CONTENT_ID : "fui-workspace-content",
	FOOTER_ID : "fui-workspace-footer",

	WORKSPACE_COMPONENT_ID :'fui-workspace-center',

	NORTH_REGION_ID :'fui-workspace-region-north',
	WEST_REGION_ID :'fui-workspace-region-west',
	EAST_REGION_ID :'fui-workspace-region-east',
	SOUTH_REGION_ID :'fui-workspace-region-south',
	CENTER_REGION_ID :'fui-workspace-region-center',

	LEFT_FOOTER_REGION_ID :'fui-workspace-region-footer-left',
	RIGHT_FOOTER_REGION_ID :'fui-workspace-region-footer-right',
	CENTER_FOOTER_REGION_ID :'fui-workspace-region-footer-center',

	TREE_ID : "fui-workspace-tree",
	GRID_ID : "fui-workspace-grid",

	configStack: [],
	activeConfig: null,

	/**
	 * Notification items.
	 */
	notifyClass: null,
	notifyMsg: null,

	/**
	 * Properties.
	 */
	properties: null,

	resize: function() {
		var wi = fui.workspace.internal;
		var workspace = fui.ext.getCmp(wi.WORKSPACE_COMPONENT_ID);
		if ( !workspace ) {
			return;
		}

		// Size according to the view size
		var size = fui.ext.getBody().getViewSize();

		// subtract the header height
		var e = fui.ext.get(wi.HEADER_ID);
		if ( e ) {
			size.height -= e.getHeight();
		}

		// subtract the footer height
		e = fui.ext.get(wi.FOOTER_ID);
		if ( e ) {
			size.height -= e.getHeight();
		}

		// make sure, though that the center content (for example, the grid)
		// has a minimum height if this is open, thus adding this height
		// to the minimum height below
		e = fui.ext.get(wi.NORTH_REGION_ID);
		var nrHeight = 0;
		if ( e ) {
			nrHeight = e.getHeight();
		}

		size.height = Math.max(size.height, fui.workspace.MINIMUM_HEIGHT + nrHeight);

		// Do height magic for IE7 since it's behaving poorly when asked for heights
		if ( fui.ext.isIE7 ) {
			size.height -= 2;
		}

		size.width = Math.max(size.width, fui.workspace.MINIMUM_WIDTH);

		workspace.setSize(size);
	}

};

