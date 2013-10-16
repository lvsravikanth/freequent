/**
 * @desc The root editor which manages opening Content Object Editors (COEs)
 * and associated utilities.
 *
 * @namespace this the vui.editor namespace
 * @name vui.ui.editor
 * @see Editor
 * @see Event
 * @public
 */
fui.provide("fui.editor");
fui.provide("fui.editor.Editor");

fui.require("fui.Modal");
fui.require("fui.html");
fui.require("fui.io");
fui.require("fui.context");
fui.require("fui.creator");

/**
 * @desc Constants and static utility functions for the editor.
 * @public
 */
fui.editor = {
	/**
	 * @constant
	 * @desc The Action Key, which corresponds to the same on the server-side.
	 * @public
	 */
	ACTION_KEY: "editor",

	/**
	 * @constant
	 * @desc The LOAD method, which corresponds to the same on the server-side.
	 * @public
	 */
	LOAD: "load",

	/**
	 * @constant
	 * @desc The SAVE method, which corresponds to the same on the server-side.
	 * @public
	 */
	SAVE: "save",

	/**
	 * @constant
	 * @desc The READONLY_ATTRIBUTE attribute, which corresponds to the same on the server-side.
	 * @public
	 */
	READONLY_ATTRIBUTE: '.fui.editor.readonly',


	/**
	 * @constant
	 * @desc The resource basename attribute, which corresponds to the same on the server-side.
	 * @public
	 */
	BASENAME:  "fui.editor.basename.not.set",

	/**
	 * @constant
	 * @desc the CONTENT_ID
	 * @private
	 */
	CONTENT_ID: 'fui-editor-content',

	/**
	 * @constant
	 * @desc Min Button width for constructors.
	 * @private
	 */
	MIN_BUTTON_WIDTH: 80,

	/**
	 * @function
	 * @desc Returns an editor resource message.
	 * @param key the message key
	 * @public
	 */
	getMessage: function(key) {
		return fui.resource.getMessage(this.BASENAME, key);
	},

	/**
	 * @function
	 * @desc Finds the editor for the provided id.
	 *
	 * @param id the editor id
	 * @public
	 */
	find: function(id) {
		if ( typeof id == "object" ) {
			return id;
		}

		var editor = fui.editor.internal.find(id);
		if ( !editor ) {
			if ( fui.log.isDebug() ) { fui.log.debug("Unable to find editor: " + id); }
		}

		return editor;
	},

	/**
	 * @function
	 * @desc Checks for the existence of an editor for the provided id.
	 *
	 * @param id the editor id
	 * @public
	 */
	exists: function(id) {
		if ( typeof id == "object" ) {
			return id;
		}

		var editor = fui.editor.internal.find(id);
		if (!editor) {
			return false;
		}
		return true;
	},

	/**
	 * @function
	 * @desc Finds the active editor.
	 * @public
	 */
	findActive: function() {
		var activeWin = fui.ext.WindowManager.getActive();
		if(activeWin && (activeWin.fuiEditor || activeWin.fuiParentEditor)){
			return fui.editor.find(activeWin.fuiEditor || activeWin.fuiParentEditor);
		} else {
			// Check for fullscreen
			if ( window.fuiEditor ) {
				return fui.editor.find(window.fuiEditor);
			}
			return null;
		}
	},

	/**
	 * @function
	 * @desc Hides a div: useful throughout panel mechanics of nestedEditors.
	 *
	 * @param divId
	 * @public
	 */
	hideDiv: function(divId) {
		fui.query('#'+divId).hide();
	},

	/**
	 * @function
	 * @desc Shows a div: useful throughout panel mechanics of nestedEditors.
	 *
	 * @param divId
	 * @public
	 */
	showDiv: function(divId) {
		fui.query('#'+divId).show();
	},

	/**
	 * @function
	 * @desc Open an editor for an object specified by type and id.
	 *
	 * @param editConfig A config object.  Recognized attributes:
	 * <ul>
	 *    <li>type: the type</li>
	 *    <li>objectTypeId: the object type ID</li>
	 *    <li>objectTypeXmlName: the object type XML name; can be null</li>
	 *    <li>typeResolver: a function to resolve a displayable type value;</li>
	 *       the object being edited is passed as its argument </li>
	 *    <li>id: the object ID; can be null for new</li>
	 *    <li>idAttribute: The attribute of the object which denotes the ID.</li>
	 *    <li>fullscreen: make the editor fullscreen</li>
	 *    <li>editorChrome: a defined constant that determines which editor look and feel to render, valid values are:<br/>
	 *       DEFAULT_EDITOR_CHROME - provides full editor functionality<br/>
	 *       SECONDARY_EDITOR_CHROME - provides minimal ok/cancel functionality </li>
	 *    <li>forcePopout: force the use of a popout window</li>
	 *    <li>skipPopout: disallow the editor to be opened in pop out mode. The "popout" tool is not available</li>
	 *       when skipPopout is set to true. This is optional and by default is assumed false for all top/parent</li>
	 *       editors, indicating that all top/parent editors can be opened in pop out mode.For child editors,</li>
	 *       it is set to true to disallow the opening of child editors in pop out mode</li>
	 *    <li>width: the editor width, optional and has no effect if fullscreen is true</li>
	 *    <li>height: the editor height, optional and has no effect if fullscreen is true</li>
	 *    <li>collectors: function/array of collector functions; see addCollector() for more details</li>
	 *    <li>checkers: function/array of change checker functions; see addChecker() for more details</li>
	 *    <li>saveHandlers: function/array of saveHandlers; see addSaveHandler() for more details</li>
	 *    <li>closeHandlers: function/array of closeHandler; see addCloseHandlers() for more details</li></li>
	 *    <li>errorHandler: a function to handle errors; called with message and rootMessage as arguments
	 *    <li>saveCallback: a callback function to call after save()</li>
	 *    <li>closeCallback: a callback function to call on close()</li>
	 *    <li>helpHandler: a function that gets called when help tool gets clicked</li>
	 *    <li>readOnly: a boolean flag to set the editor in read-only mode</li>
	 *    <li>clone: a boolean flag to set the editor clone mode</li>
	 *    <li>toolbarBuilder: a function to call to build the initial toolbar instead of properties</li>
	 *    <li>footerbarBuilder: a function to call to build the initial footerbar instead of properties</li>
	 *    <li>skipToolbar: a boolean flag to skip building the entire toolbar</li>
	 *    <li>skipFooterBar: a boolean flag to skip building the entire footer toolbar</li>
	 *    <li>skipSave: a boolean flag to skip the save button</li>
	 *    <li>skipOkCancel: a boolean flag to skip the ok and cancel buttons</li>
	 *    <li>skipDate: a boolean flag to skip the date</li>
	 *    <li>skipType: a boolean flag to skip the type specific buttons such as Approve, Publish, etc.</li>
	 *    <li>message: the message to be shown when the editor is opened<br/>
	 * 			<ul>
	 *         <li>cls: may take value fui.notification.MESSAGE, fui.notification.WARNING, fui.notification.ERROR or
	 *              any other class name used for the notification box. If not specified takes fui.notification.WARNING</li
	 *         <li>text: the text of the message</li>
	 *         <li>autoClose: the autoclose time, optional, has a default value of 5000 i.e. 5 seconds</li>
	 * 			</ul>
	 *    </ul>
	 * Note that saveHandlers and closeHandlers define the steps taken
	 *    when processing a save or a close, while saveCallback and
	 *    closeCallback are functions that are called *after* the save and
	 *    close is done processing.
	 * @param requestData the request data
	 * @public
	 */
	edit: function(editConfig, requestData) {
		// Check if the required editor is already open, if so return by giving an appropriate warning
		if( editConfig.id && fui.editor.exists(editConfig.id) ) {
			fui.msg.alert(fui.workspace.getMessage('error'), fui.editor.getMessage('editor.circular.load.failure'));
			return;
		}

		var editor;

		requestData = requestData || {};

		// if the current editor pops op on top of another editor,
		// we need to deativate the current editor. This is needed,
		// in particular, to avoid that ELJ lose unsaved values.
		var activeEditor = fui.editor.findActive();
		if (activeEditor) {
			activeEditor.deactivate(); 
		}

		// Setup handler
		var updateFunc = this.internal.getUpdateFunction(editConfig, requestData.handler);
		requestData.handler = fui.scope(this, function(data) {
			editor.showBlock();

			// Tools
			var tools = [];

			// Help
			if ( editConfig.helpHandler ) {
				tools.push({
					cls: 'fui-tool-help',
					tooltip: fui.editor.getMessage("editor.tooltip.help"),
					handler: editConfig.helpHandler
				});
			}
            // Refresh
            tools.push({
                cls: 'fui-tool-refresh',
                tooltip: fui.editor.getMessage("editor.tooltip.refresh"),
                handler: function() {
                    if ( editor.hasChanged() ) {
                        var reloadFunc = function(btn) {
                            if (btn == 'yes'){
                                editor.reload();
                            }
                        };
                        var reloadTitle = fui.editor.getMessage("reload.confirm.title");
                        var reloadText = fui.editor.getMessage("reload.confirm.text");
                        fui.msg.confirm(reloadTitle, reloadText, reloadFunc);
                    } else {
                        editor.reload();
                    }
                }
            });

			var editorCls = 'fui-css-reset fui-editor fui-editor-' + editConfig.type + ' fui-window fui-window-editor';

			var config = {
				id: editor.id,
				cls: editorCls,
				border: false,
				collapsible: false,
				maximizable: false, // Handled by custom tool buttons
				closable: false, // Handled by custom tool buttons
				dockedItems: [
					{
						xtype: 'toolbar',
						dock: 'top',
						itemId: 'topToolbar',
						items: [{
							id: this.CONTENT_TOOLBAR_ID + '-' + editor.id,
							hidden: true,
							enableOverflow: true
						}]
					},
					{
						xtype: 'toolbar',
						dock: 'bottom',
						itemId: 'bottomToolbar',
						ui: 'footer',
						cls: 'fui-toolbar',
						items: [{
							id: this.CONTENT_BOTTOMBAR_ID + '-' + editor.id,
							hidden: true
						}]
					}
				],
				layout: 'border',
				width: editConfig.fullscreen ? undefined : editConfig.width,
				height: editConfig.fullscreen ? undefined : editConfig.height,
				items: [{
					region: 'north',
					id: this.PROPERTIES_DOCK_ID + '-' + editor.id,
					cls: this.PROPERTIES_DOCK_ID,
					layout: 'fit',
					margins: '0 0 0 0',
					hidden: true,
					split: true
				}, {
					region: 'center',
					resizable: false,
					autoScroll: true,
					id: this.CONTENT_ID + '-' + editor.id,
					cls: this.CONTENT_ID,
					layout: 'fit',
					margins: '0 0 0 0'
				}],
				tools: tools
			};

			var listenAndUpdate = function(ui) {
				updateFunc(ui, editor, data);
				var contentId = fui.editor.CONTENT_ID + '-' + editor.id;
				fui.query('#' + contentId).mousedown(deactivator);
				//reset the size so that everything lays out correctly (like toolbars).  
				//not calling the fui.vext.forceLayout(ui); since that will use the container size 
				//and for editors that is the body and is very big.
				ui.setSize(ui.getSize());
			};

				var w = fui.ext.create('fui.Modal', config);
				w.on('show', listenAndUpdate);
				w.show();
				if(editConfig.message){
					var cls = editConfig.message.cls || fui.notification.WARNING;
					var text = editConfig.message.text || '';
					var autoClose = editConfig.message.autoClose || 5000;
					fui.notification.show(cls, text, autoClose);
				}

		});

		// Setup error handler
		requestData.errorHandler = fui.scope(this, function(message, rootMessage) {
			editor.close();
			if (editConfig.errorHandler) {
				return editConfig.errorHandler(message, rootMessage);
			} else {
				return fui.ui.errorHandler(message, rootMessage);
			}
		});

		// check for clone
		var cloneId = undefined;
		if ( editConfig.clone === true ) {
			cloneId = editConfig.id;
			editConfig.id = undefined;
		}
		
		editor = new fui.editor.Editor({
			id: editConfig.id || (cloneId ? fui.editor.getCloneEditorId() : fui.editor.getNewEditorId()),
			saveHandlers: editConfig.saveHandlers,
			closeHandlers: editConfig.closeHandlers,
			initialData: editConfig.initialData,
			readOnly: editConfig.readOnly,
			cloneId: cloneId,
			editConfig: editConfig,
		});

		editor.load(requestData);
	},

	/**
	 * @function
	 * @desc Checks to see if the editor data has changed.
	 * @public
	 */
	hasChanged: function(id) {
		var editor = this.find(id);
		if ( !editor ) {
			return false;
		}

		return editor.hasChanged();
	},

	/**
	 * @function
	 * @desc Closes the editor specified by the id
	 *
	 * @param id the editor id
	 * @public
	 */
	close: function(id) {
		var editor = this.find(id);
		if ( !editor ) {
			return;
		}

		editor.close();
	},

	/**
	 * @function
	 * @desc Checks whether the supplied ID is for a new editor.
	 *
	 * @param id The ID in question.
	 * @returns <code>true</code> if the supplied ID is a new editor ID.
	 * @public
	 */
	isNewEditorId: function(id) {
		return fui.string.startsWith(id, fui.editor.internal.NEW_EDITOR_ID);
	},

	/**
	 * @function
	 * @desc Check if the supplied ID is for a new editor for an item being cloned.
	 * Note that a valid clone editor ID is also a valid new editor ID,
	 * but not vice-versa.
	 * 
	 * @param id The ID in question.
	 * @returns <code>true</code> if the supplied ID is a new editor ID.
	 * @public
	 */
	isCloneEditorId: function(id) {
		return fui.string.startsWith(id, fui.editor.internal.CLONE_EDITOR_ID);
	},

	/**
	 * @function
	 * @desc Returns a unique ID for a new editor.
	 * @returns A unique ID for a new editor.
	 * @public
	 */
	getNewEditorId: function() {
		return fui.editor.internal.NEW_EDITOR_ID + (fui.editor.internal.newEditorIndex++);
	},

	/**
	 * @function
	 * @desc Returns a unique ID for a new editor of an item being cloned.
	 * @returns Returns unique ID for a new editor of an item being cloned.
	 * @public
	 */
	getCloneEditorId: function() {
		return fui.editor.internal.CLONE_EDITOR_ID + (fui.editor.internal.newEditorIndex++);
	}
};

/**
 * @constructor
 * @desc Editor object
 * @name Editor
 * @public
 */
fui.editor.Editor = function() {
	this.id = "";
	this.data = {};
	this.saveHandlers = [];
	this.closeHandlers = [];
	this.deactivateListeners = [];
	this.isDeactivating = false;
	this.closeCallback = null; // function(callbackData); callbackData = {type, id, isNew, parentId, object}
	this.saveCallback = null;  // executes a config function after save.
	this.ui = null;
	this.updateFunc=null;
	this.readOnly = false;
	this.cloneId = null;
	this.isDirty=false; // is a dirty editor
	this.hasSaved = false;  // has this editor ever successfully saved?
	this.editConfig = {}; // stores the editor config
	this.requestContent = {}; // stores the request content

	if ( (arguments.length == 1) && (typeof arguments[0] == "object") ) {
		fui.combine(this, arguments[0]);
	}

	if (!fui.query.isArray(this.saveHandlers)) {
		this.saveHandlers = (this.saveHandlers ? [this.saveHandlers] : []);
	}
	if (!fui.query.isArray(this.closeHandlers)) {
		this.closeHandlers = (this.closeHandlers ? [this.closeHandlers] : []);
	}
	// Add to internal cache
	fui.editor.internal.add(this);
};


/**
* @desc Editor
 */
fui.extend(fui.editor.Editor,
	/**
	 * @lends Editor.prototype
	 */
	{

	/**
	 * @function
	 * @desc Returns the size of the content area.
	 * @public
	 */
	getContentSize: function() {
		var content = this.getContentComponent();
		if ( !content || ! content.getTargetEl() ) {
			return { width: 0, height: 0 };
		}

		return content.getTargetEl().getSize(true);
	},

	/**
	 * @function
	 * @desc Returns the content component for editor.
	 * @public
	 */
	getContentComponent: function() {
		var id = this.componentId || this.id;
		var contentId = fui.editor.CONTENT_ID + '-' + id;
		return fui.ext.getCmp(contentId);
	},

	/**
	 * @function
	 * @desc Returns the bottom toolbar for editor.
	 * @public
	 */
	getBottomToolbar: function() {
		var id = this.componentId || this.id ;
		var toolbarId = fui.editor.CONTENT_BOTTOMBAR_ID + '-' + id;
		return fui.ext.getCmp(toolbarId);
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
			this.ui.el.mask(msg);
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
	 * @desc Reloads the editor.
	 *
	 * @param id the new id of the editor
	 * @public
	 */
	reload: function(newId) {
		var oldId = this.id;
		if ( newId && (newId !== this.id) ) {
			// Remove the editor reference with the old Id
			fui.editor.internal.remove(this.id);

			//Save the component id which will be useful during reload since some components(editor content area) are intialized with this id.
			this.componentId = this.id;

			this.id = newId;

			// Add the new editor
			fui.editor.internal.add(this);
			
			// remove clone options if it is set
			if ( this.cloneId ) {
				this.cloneId = undefined;
			}
		}

		// honour the request data with which the editor was first loaded
		var requestData = {
			content : this.requestContent
		};

		// Handler for the editor reload operation
		requestData.handler = fui.scope(this, function(data) {
			// Get the content area and remove everything
			var content = this.getContentComponent();
			content.removeAll();

			// reload the editor with the same config with which the editor was first loaded
			var updateFunc = fui.editor.internal.getUpdateFunction(this.editConfig);

			// Remove all the buttons in the top toolbar
			var tbar = this.ui.getDockedComponent('topToolbar');
			if (tbar) {
				tbar.removeAll();
			}

			// Remove all the buttons in the bottom toolbar
			var bbar = this.ui.getDockedComponent('bottomToolbar');
			if (bbar) {
				bbar.removeAll();
			}
			
			// Remove all the buttons in the footer
			var footer = this.getFooterToolbar();
			if (footer) {
				footer.removeAll();
			}

			// This updates the content area with the new data and also creates new buttons with the new Id
			updateFunc(this.ui, this, data);
		});

		// Remove all the editor widgets
		this.deleteWidgets();

		// Reset any previously added collectors / checkers too
		this.collectors = [];
		this.checkers = [];
		this.deactivateListeners = [];

		// Load the editor with the newly saved object
		this.load(requestData);

		// Reload properties
		this.reloadProperties();

		// Fire the reload event.
		var e = new fui.editor.Event({
			type: fui.editor.event.RELOAD,
			object: this
		});

		fui.publish(fui.editor.event.TOPIC_ROOT + oldId, e);

	},

	/**
	 * @function
	 * @desc Adds the properties to the editor if it doesn't exist.
	 * @public
	 */
	addProperties: function() {
		if (!this.properties) {
			this.properties = fui.properties.load(this.type, this.typeXmlName, this.id, this.object);
		}

		// Create and Add a collector for the properties.
		this.addCollector(function (editor, data, requestData) {
			data = data || {};
			fui.combine(data, editor.properties.getData(requestData));
			return data;
		}, fui.editor.EDITOR_PROPERTIES_COLLECTOR + this.id);

		// Create and Add a checker for the properties
		this.addChecker(fui.scope(this, function () {
			return this.properties.hasChanged();
		}));

		return this.properties;
	},
	
	/**
	 * @function
	 * @desc Reloads the properties.
	 * @public
	 */
	reloadProperties: function() {
		//enable the properties button
		var propertiesButton = fui.ext.getCmp(fui.editor.button.properties.BUTTON_ID);
		if(propertiesButton){
			propertiesButton.disabled = false;
		}
		
		this.addProperties();

		// If properties panel is visible, update the panel with the saved object data
		if (this.isShowingProperties()) {
			this.properties.update(this.type, this.id, this.object, this.typeXmlName);
		}
	},

	/**
	 * @function
	 * @desc Sets the "Creation Time" or "Last Modification Time" in the editor.
	 * @public
	 */
	setTime: function(time) {
		if (!(fui.ext.get(fui.editor.LAST_SAVED_AT_DATE_ID))) {
			var dateEl = this.ui.add({
				tag: 'div',
				id: fui.editor.LAST_SAVED_AT_DATE_ID,
				cls: fui.editor.LAST_SAVED_AT_DATE_CLS,
				html: fui.editor.getMessage('last.saved') + ' ' + time
			});
		} else {
			var lastSaved = fui.ext.get(fui.editor.LAST_SAVED_AT_DATE_ID);
			if (lastSaved) {
				lastSaved.update(fui.editor.getMessage('last.saved') + ' ' + time);
			}
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
		if ( this.ui && !this.fullscreen ) {
			if (this.ui && this.ui.el) {  // TODO: fix this for the editor save scenario
				this.ui.el.unmask();
			}
		} else {
			var body = fui.ext.getBody();
			body.unmask();
		}
	},

	/**
	 * @function
	 * @desc Update the content area.
	 *
	 * @param data the content data
	 * @public
	 */
	updateContent: function(data) {
		var content = this.getContentComponent();

		// Clear all the components from the content area
		content.removeAll(true);

		//Get the current active tab and clear the tabs
		var activeTabIndex = 0;
		if(this.tabs) {
			activeTabIndex = this.tabs.activeTabIndex;
			this.tabs.destroy();
			this.tabs = null;
		}

		var general = fui.ext.create(fui.extRootName + '.panel.Panel', {
			autoScroll: true
		});
		content.add(general);
		content.doLayout();
		general.hide();

		fui.html.set(general.getTargetEl().dom, data, true, true, true);

		// Build custom panels
		var panels = fui.editor.panel.getPanels(this);
		if ( panels.length > 0 ) {
			if ( !this.tabs ) {
				content.remove(general, false);
				var generalTab = fui.ext.create(fui.extRootName + '.panel.Panel', {
					autoScroll: true,
					title: fui.editor.getMessage('general.panel.name'),
					items: [general]
				});
				general.show();
				this.addTabs();
				this.tabs.add(generalTab);
			} else {
				content.remove(general);
			}

			for ( var i = 0 ; i < panels.length ; ++i ) {
				var panel = panels[i];
				this.addPanel(panel);
			}

			this.showTabs(activeTabIndex);
		} else {
			if ( this.tabs ) {
				content.remove(general);
				this.showTabs(activeTabIndex);
			} else {
				general.addCls('fui-editor-content-inner');
				general.show();
				content.doLayout();
			}
		}
	},


	/**
	 * @function
	 * @desc Returns the panel load handler.
	 * @public
	 */
	getPanelLoadHandler : function() {
		if ( !this.panelLoadHandler ) {
			this.panelLoadHandler = fui.scope(this, function(panel) {
				if ( panel.fuiLoadHandler && !panel.fuiLoaded ) {
					panel.fuiLoadHandler(this, panel);
				}
				if(panel.focus){
					panel.focus();
				}
			});
		}

		return this.panelLoadHandler;
	},

	/**
	 * @function
	 * @desc Add a panel to the editor.
	 *
	 * @param panel the panel
	 * @public
	 */
	addPanel: function(panel) {
		this.addTabs();

		var immediateLoad = panel.isImmediateLoad() === true;
		
		var tab = fui.ext.create(fui.extRootName + '.panel.Panel', {
			title: panel.getTitle(),
			cls: panel.getPanelClass(),
			autoScroll: true,
			forceLayout: immediateLoad,
			fuiLoadHandler: panel.loadHandler,
			fuiImmediateLoad: immediateLoad,
			fuiDestroy: panel.destroy,
			fuiGroupName: panel.groupName
		});

		if ( panel.setupPanel ) {
			panel.setupPanel(this, tab);
		}

		var activateFunc = this.getPanelLoadHandler();
		tab.mon(tab, 'activate', activateFunc);

		this.tabs.add(tab);
	},

	/**
	 * @function
	 * @desc Fired when the tab/editor is deactivated. All the deactivate event listeners will be fired in sequence.
	 * @public
	 */
	deactivate: function() {
		if ( this.isDeactivating ) { return; }
		this.isDeactivating=true; // prevent infinite loops
		var deactivateObs = this.deactivateListeners;
		for(var i = 0; i < deactivateObs.length; i++) {
			deactivateObs[i]();
		}
		this.isDeactivating=false;
	},

	/**
	 * @function
	 * @desc Clears out any existing panels.
	 * @public
	 */
	clearPanels : function() {
		if ( !this.tabs ) {
			return;
		}

		// Destroy the panels
		this.tabs.items.each(function(panel, index) {
			if ( panel.fuiDestroy ) {
				panel.fuiDestroy();
			}
			
			return true;
		}, this);

		this.tabs.destroy();
		this.tabs = null;
	},

	/**
	 * @function
	 * @desc Add tabs to the editor.
	 * @public
	 */
	addTabs: function() {
		if ( this.tabs ) {
			return;
		}

		// Build tabs
		this.tabs = fui.ext.create('fui.vext.TabPanel', {
			enableTabScroll: true,
			layoutOnTabChange: true,
			cls: 'fui-tab-panel fui-editor-tabpanel',
			defaults: {
				autoScroll: true,
				tabCls: "fui-tab"
			},
			autoScroll: true,
			deferredRender: false,
			listeners:{
				"render":{
					fn: fui.scope(this, function(tb){
						if (tb.strip){
							tb.strip.addCls("fui-tab-strip");
						}

						this.ui.doLayout();
					})
				},
				beforetabchange: this.deactivate,
				scope: this
			}
		});
	},

	/**
	 * @function
	 * @desc Show the tabs.
	 *
	 * @param activeTabIndex index of the tab that needs to be set as the active tab
	 * @public
	 */
	showTabs: function(activeTabIndex) {
		if ( !this.tabs ) {
			return;
		}

		var activeTab = activeTabIndex || 0;
		this.tabs.setActiveTab(activeTab);

		var content = this.getContentComponent();
		content.add(this.tabs);

		// Load any immediate panels
		var tabsLoader = fui.scope(this, function(tabs) { 
			tabs.items.each(function(panel, index) {
				// Skip if index == activeTab since it will get loaded by the activate handler
				if ( index === activeTab ) {
					return true;
				}
				
				// Load if immediate, capable and not loaded
				if ( (panel.fuiImmediateLoad === true) && panel.fuiLoadHandler && !panel.fuiLoaded ) {
					panel.fuiLoadHandler(this, panel);
				}
				
				return true;
			}, this);
			
			tabs.un("afterrender", tabsLoader);
		});
		
		this.tabs.on("afterrender", tabsLoader);
	},

	/**
	 * @function
	 * @desc Loads an editor.
	 *
	 * @param requestData the request data
	 * @param groupName the group name to load; can be null
	 * @public
	 */
	load: function(requestData, groupName) {
		var e = fui.editor;
		requestData = requestData || {};
		requestData.content = requestData.content || {};

		// Set type 
		requestData.content[e.TYPE_ATTRIBUTE] = this.type;
		requestData.content[e.TYPE_ID_ATTRIBUTE] = this.typeId;
		requestData.content[e.TYPE_XML_NAME_ATTRIBUTE] = fui.html.escape(this.typeXmlName);

		// Set clone id and readonly state
		requestData.content[e.CLONE_ATTRIBUTE] = this.cloneId ? this.cloneId : undefined;			
		requestData.content[e.READONLY_ATTRIBUTE] = this.readOnly ? this.readOnly : undefined;
		
		// Set id
		requestData.content[e.ID_ATTRIBUTE] = this.id || (this.cloneId ? e.getCloneEditorId() : e.getNewEditorId());

		// Set group name
		requestData.content[e.GROUP_ATTRIBUTE]=groupName ? groupName : undefined;

		// Copy over content
		fui.query.extend(this.requestContent, requestData.content);
		
		requestData.actionKey = e.ACTION_KEY;
		requestData.method = e.LOAD;

		var orgHandler = requestData.handler;
		requestData.handler = fui.scope(this, function(data) {
			this.clearBlock();
			if ( orgHandler ) {
				orgHandler(data);
			}
		});

		var orgErrorHandler = requestData.errorHandler;
		requestData.errorHandler = fui.scope(this, function(message, rootMessage) {
			this.clearBlock();
			if ( orgErrorHandler ) {
				return orgErrorHandler(message, rootMessage);
			}

			return fui.errorHandler(message, rootMessage);
		});

		this.showBlock();

		request = fui.request.build(requestData);
		fui.io.api(request);
	 },

	 /**
	  * @function
	  * @desc Loads a panel for the provided group name.
	  *
	  * @param panel the panel to load
	  * @param groupName the group name
	  * @param dynamic the dynamic flag to allow loading again
	  * @param requestData the requestData
	  * @public
	  */
	 loadPanel: function(panel, groupName, dynamic, requestData) {
		 requestData = requestData || {};
		 requestData.handler = function(data) {
				fui.html.set(panel.getTargetEl().dom, data, true, true, true);
				if ( !dynamic ) {
					panel.fuiLoaded = true;
				}
		 	};
		 this.load(requestData, groupName);
	 },

	/**
	 * @function
	 * @desc Saves an editor.
	 *
	 * @param config save configuration options:
	 * <ul>
	 *    <li>reload: reload flag</li>
	 *    <li>close: close flag</li>
	 * </ul>
	 * @param requestData the request data
	 * @public
	 */
	 save: function(config, requestData) {
		// Ensure a request data
		requestData = requestData || {};

		// If not explicity set, default to POST
		if ( !requestData.ajaxMethod ) {
			requestData.ajaxMethod = 'POST';
		}

		var data = this.getData(requestData);
		if ( !data ) {
			return;
		}

		// deactivate
		this.deactivate();
			
		config = config || {};

		var oldId = this.id;
		var saveFunc = fui.scope(this, function() {
			var orgHandler = requestData.handler;
			requestData.handler = fui.scope(this, function(data) {
				// reset to prevent dup adds (particularly for relators)
				this.data={};
				this.clearWidgetCache();
				this.hasSaved = true;
				this.resetDirtyWidgets();

				// parse data only if we need to
				var callbackData;
				if ( config.reload || this.saveCallback ) {
					var ctx = fui.context.getContext();
					var object = fui.secure_eval(data).item;

					var isNew;

					//For Category type of assets, creation/modification timings may not exist.
					// Hence bring the isNew value from the config and set it to isNew

					if ( !object.creationTime && !object.lastModificationTime) {
						isNew = config.isNew;
					} else {
						isNew = (object.creationTime == object.lastModificationTime);
					}


					callbackData = {
						type: this.type || object.type,
						id: (this.editConfig.idAttribute) ? object[this.editConfig.idAttribute] || object.id : object.id,
						isNew: isNew,
						parentId: ctx.activeContainerId,
						object: object,
						reload: config.reload
					};
				}

				// Fire the save event.
				var e = new fui.editor.Event({
					type: fui.editor.event.SAVE,
					object: callbackData
				});

				fui.publish(fui.editor.event.TOPIC_ROOT + oldId, e);

				if ( this.saveCallback ) {
					this.saveCallback(callbackData);
				}

				if ( config.reload ) {
					// This takes care of reloading the editor with the saved object data.
					this.setObject(callbackData.type, callbackData.object);
					this.reload(callbackData.id);
				}

				if ( config.close ) {
					this.close();
				}

				this.clearBlock();

				if ( orgHandler ) {
					orgHandler(data);
				}
			});

			var origHandler = requestData.handler;
			requestData.handler = fui.scope(this, function(response) {
				this.errorOnSave = false;  // clear the error-on-save flag
				if ( origHandler ) {
					return origHandler(response);
				}
			});

			requestData.errorHandler = fui.scope(this, function(message, rootMessage) {
				var orgErrorHandler = this.editConfig.errorHandler;
				this.clearBlock();
				this.errorOnSave = true;  // set the error-on-save flag
				if ( orgErrorHandler ) {
					return orgErrorHandler(message, rootMessage);
				}
				return fui.errorHandler(message, rootMessage);
			});

			this.showBlock({
				blockMessage: fui.editor.getMessage('saving')
			});

			// Check if we need to clone
			if ( this.cloneId ) {
				data[fui.editor.CLONE_ATTRIBUTE] = this.cloneId;			
			}

			fui.editor.internal.save(this.type, this.typeId, this.typeXmlName, this.id, data, requestData);
		});

		var sh = this.saveHandlers;
		if (sh && sh.length > 0) {
			var editor = this;

			var makeFunction = function(func, handler) {
				return function() { func(editor, data, handler); };
			};

			// chain up the save handlers
			var handler = function() {
				sh[sh.length - 1](editor, data, function() {
					saveFunc();
				});
			};

			for (var i = sh.length - 2; i >= 0; --i) {
				handler = makeFunction(sh[i], handler);
			}
			// call the first save handler
			handler();
		} else {
			saveFunc();
		}
	},

	/**
	 * @function
	 * @desc Resets dirty flag on widgets after save.
	 * @public
	 */
	resetDirtyWidgets: function() {
		for ( var widgetId in this.widgets ) {
			this.widgets[widgetId].resetDirty();
		}
	},

	/**
	 * @function
	 * @desc Destroys the editor and its widgets.
	 *
	 * @param noHooks flag to disable callbacks and handlers
	 * @public
	 */
	close: function(noHooks) {
		if ( !noHooks && this.closeCallback ) {
			this.closeCallback();
		}

		if (this.properties) {
			// Close/destroy the properties
			this.properties.close();
			this.properties = null;
		}

		if ( this.ui ) {
			this.ui.un('beforeclose', this.closeHandler, this);

			if ( this.fullscreen ) {
				this.ui.hide();
			} else {
				this.ui.close();
			}
		}

		// Fire the close event.
		var e = new fui.editor.Event({
			type: fui.editor.event.CLOSE,
			object: this
		});

		fui.publish(fui.editor.event.TOPIC_ROOT + this.id, e);

		var ch = this.closeHandlers;
		if (!noHooks && ch && ch.length > 0) {
			var editor = this;

			var makeFunction = function(func, handler) {
				return function() { func(editor, handler); };
			};

			// chain up the close handlers
			var handler = function() {
				ch[ch.length - 1](editor, function() {
					fui.editor.internal.remove(editor.id);
				});
			};

			for (var i = ch.length - 2; i >= 0; --i) {
				handler = makeFunction(ch[i], handler);
			}
			// call the first close handler
			handler();
		} else {
			fui.editor.internal.remove(this.id);
		}

		// Clean up widgets
		this.deleteWidgets();

		// Reset the widgetValueCache to empty.
		this.clearWidgetCache();

		// Clean up editor panels
		this.clearPanels();
		
		// Handle fullscreen mode
		if ( this.fullscreen ) {
			if ( opener ) {
				window.close();
			} else {
				window.location.pathname = fui.appContext;
			}
		}
	},

	/**
	 * @function
	 * @desc Sets the UI for the editor.
	 *
	 * @param ui, the ui object
	 * @public
	 */
	setUI: function(ui) {
		this.ui = ui;
		if ( ui ) {
			ui.fuiEditor = this.id;
			ui.on('beforeclose', this.closeHandler, this);

			if ( this.fullscreen ) {
				window.fuiEditor = this.id;
			}
		}
	},

	/**
	 * @function
	 * @desc The window close handler. Checks for changes before allowing the close.
	 * @public
	 */
	closeHandler: function(ui) {
		if ( this.hasChanged() ) {
			var func = function(button) {
				if ( button === "cancel") {
					// do nothing
				} else if( button === "yes") {
					this.save( {close: true} );
				} else {
					this.close();
				}
			};
			var e = fui.editor;
			var message;
			var title = e.getMessage('lose.changes.title');
			var type = this.editConfig.typeResolver ? fui.html.escape(this.editConfig.typeResolver(this.object)) : '';
			var name = fui.html.escape(this.object.name);
			if (type && name) {
				message = fui.string.replace(e.getMessage('save.changes.type.name'), {type: type, name: name});
			} else if (type) {
				message = fui.string.replace(e.getMessage('save.changes.type'), {type: type});
			} else if (name) {
				message = fui.string.replace(e.getMessage('save.changes.name'), {name: name});
			} else {
				message = fui.string.replace(e.getMessage('save.changes'));
			}
			fui.vext.msg.confirmSave(title, message, func, this);
			return false;
		}

		this.close();
		return true;
	},

	/**
	 * @function
	 * @desc Sets the editor title.
	 *
	 * @param displayType the display type
	 * @param title the title
	 * @param skipHTML flag to skip HTML wrapping
	 * @param escaped flag to indicate if title is escaped
	 * @public
	 */
	setTitle: function(displayType, title, skipHTML, escaped) {
		if ( !this.ui) {
			return;
		}

		var hasTitle = title && (title.length > 0);
		var escapedTitle;
		if ( hasTitle ) {
			if ( escaped === true ) {
				escapedTitle = title;
				title = fui.html.unescape(title);
			} else {
				escapedTitle = fui.html.escape(title);
			}
		}

		if ( skipHTML ) {
			this.ui.setTitle(hasTitle ? title : displayType);
			if ( this.fullscreen ) {
				document.title = hasTitle ? title : displayType;
			}
			return;
		}

		var htmlTitle = hasTitle ? '<div class="fui-header-title"><span class="fui-header-title-type">' + displayType + ' :&nbsp;</span><span class="fui-header-title-text">' + escapedTitle + '</span></div>' : '<div class="fui-header-title"><span class="fui-header-title-text">' + displayType + '</span></div>';
		var displayTitle = hasTitle ? ' : ' + escapedTitle : '';
		var uiTitle = '<div id="fui-header-type" class="fui-header-type fui-objecttype-' + this.type + '"  title="' + displayType + displayTitle + '">' + htmlTitle + '</div>';
		this.ui.setTitle(uiTitle);

		if ( this.fullscreen ) {
			var fullTitle = hasTitle ? ': ' + title : "";
			document.title = displayType + fullTitle;
		}
	},

	/**
	 * @function
	 * @desc Adds one or more collectors.
	 *
	 * @param collector function/array of collector functions
	 * @param collectorId The collector ID. It is used only when a single collector is passed in.
	 * 			When a collectorId is passed, it is not added to the list of collectors if it
	 * 			already exists.
	 * @public
	 */
	addCollector: function(collector, collectorId) {
		if ( collector ) {
			if (fui.query.isArray(collector)) {
				var len = collector.length;
				for (var i = 0; i < len; ++i) {
					this.collectors.push(collector[i]);
				}
			} else {
				if (collectorId && collectorId !== "") {
					collector.collectorId = collectorId;
					// Add the collector only if a collector with the same ID is not present. 
					if (fui.query.grep(this.collectors, function(item, itemIndex) {
							return (item.collectorId && item.collectorId === collectorId);
						}).length === 0) {
						this.collectors.push(collector);
					}
				} else {
					// If a collectorId was not provided, go ahead and push it in...
					this.collectors.push(collector);
				}
			}
		}
	},

	/**
	 * @function
	 * @desc Removes a named collector.
	 *
	 * @param collectorId
	 * @public
	 */
	removeCollector: function(collectorId) {
		if (!collectorId) {
			return;
		}
		this.collectors=fui.query.grep(this.collectors, function(item, itemIndex) {
			return (!item.collectorId || item.collectorId !== collectorId);
		});
	},

	/**
	 * @function
	 * @desc Returns the data from all the editor's widgets. If there's a
	 * validation error, this will return null.
	 *
	 * @param requestData the request data
	 * @param bypassCollectors needed for some conditions with subEditors.
	 * @param bypassValidators needed for some conditions with subEditors.
	 * @public
	 */
	getData: function(requestData, bypassCollectors, bypassValidators) {
		var data = this.data;
		var validData = true;
		var scrolled = false;
		var groupsToMark = [];
		var groupWidgets = [];
		
		// Clean up tabs
		var tabCleaner = function(panel, index) {
			var el = fui.ext.get(this.tabs.id + this.tabs.idDelimiter + panel.id);
			if ( el ) {
				el.removeCls('fui-editor-tab-error');
			}
			
			return true;
		};
		
		if ( this.tabs ) {
			this.tabs.items.each(tabCleaner, this);
		}
		
		// Scroll to helper function
		var scrollTo = fui.scope(this, function(w) {
			// Something to scroll to?
			var field = fui.ext.get(w.fieldId) || fui.ext.get(w.fieldId + '-label');
			if ( !field ) {
				return;
			}
	
			// Scroll it
			var contentBody = this.getContentComponent().body;
			var startScroll = contentBody.dom.scrollTop;
			field.scrollIntoView(contentBody);
	
			// Enable this once ExtJS has fixed their scrollTo() bug
			// http://www.extjs.com/forum/showthread.php?t=79491
			// contentBody.scroll('b', contentBody.dom.clientHeight / 2);
			var halfHeight = contentBody.dom.clientHeight / 2;
			if ( contentBody.dom.scrollTop > startScroll ) {
				contentBody.dom.scrollTop = Math.min(contentBody.dom.scrollHeight, contentBody.dom.scrollTop + halfHeight);
			} else if ( contentBody.dom.scrollTop < startScroll ) {
				contentBody.dom.scrollTop = Math.max(0, contentBody.dom.scrollTop - halfHeight);
			}
		});

		for ( var widgetId in this.widgets ) {
			var widget = this.getWidget(widgetId);

			// Verify it's valid
			if (!bypassValidators && !widget.validate() ) {
				validData = false;

				if ( widget.groupName ) {
					if ( fui.query.inArray(widget.groupName, groupsToMark) === -1 ) {
						groupsToMark.push(widget.groupName);
						groupWidgets.push(widget);
					}
				}
				
				// Do we need to or can we scroll?
				if ( scrolled || !widget.fieldId ) {
					continue;
				}

				if ( !widget.groupName ) {
					scrollTo(widget);
				}
				
				scrolled = true;
				continue;
			}

			// Checked if widget has changed.  If changed retrieve
			// new value otherwise clear out the data for that widget id
			// if it existed on a previous save
			if (widget.hasChanged() || widget.getConsiderDefault()){
				data = widget.getData(data, requestData);
				if ( !data ) {
					// Flag as bad and allow to continue to catch all bad cases
					validData = false;
					data = this.data;
				} else {
					this.addWidgetValueCache(widget);
				}
			} else {
				data[widget.getDataId()] = undefined;
			}

		}

		// If there are collectors, let them do some work
		for ( var i = 0 ; !bypassCollectors && i < this.collectors.length ; ++i ) {
			data = this.collectors[i](this, data, requestData);
			if ( !data ) {
				// Flag as bad and allow to continue to catch all bad cases
				validData = false;
				data = this.data;
			}
		}

		// Make sure we had valid data
		if ( !validData ) {
			var groupScrolled = false;
			var tabMarker = function(panel, index) {
				// If not in groups to mark continue
				var i = fui.query.inArray(panel.fuiGroupName, groupsToMark);
				if ( i === -1 ) {
					return true;
				}
				
				var groupWidget = groupWidgets[i];
				if ( !groupScrolled && groupWidget && (panel.fuiGroupName === groupWidget.groupName) ) {
					this.tabs.setActiveTab(index);
					scrollTo(groupWidget);
					groupScrolled = true;
				}
				
				var el = fui.ext.get(this.tabs.id + this.tabs.idDelimiter + panel.id);
				if ( el ) {
					el.addCls('fui-editor-tab-error');
				}				
				
				return true;
			};
			
			// Find the right tab
			if ( this.tabs && (groupsToMark.length > 0) ) {
				this.tabs.items.each(tabMarker, this);					
			}
			
			if ( groupsToMark.length > 1 ) {
				fui.notification.warning(fui.editor.getMessage("other.tabs.message"), 5000);
			}
			
			return null;
		}

		// Store the data
		this.data = data;
		var widgetValueData = this.getWidgetValueData();
		return fui.combine(widgetValueData,this.data);
	},

	/**
	 * @function
	 * @desc Adds one or more change checkers.
	 *
	 * @param checker function/array of change checkers.
	 * @public
	 */
	addChecker: function(checker) {
		if ( checker ) {
			if (fui.query.isArray(checker)) {
				var len = checker.length;
				for (var i = 0; i < len; ++i) {
					this.checkers.push(checker[i]);
				}
			} else {
				this.checkers.push(checker);
			}
		}
	},

	/**
	 * @function
	 * @desc Checks to see if the editor data has changed.
	 * @public
	 */
	hasChanged: function() {
		for ( var widgetId in this.widgets ) {
			var widget = this.getWidget(widgetId);
			if ( widget.hasChanged() === true ) {
				return true;
			}
		}

		// If there are change checkers, let them do some work
		for ( var i = 0 ; i < this.checkers.length ; ++i ) {
			if ( this.checkers[i]() ) {
				return true;
			}
		}

		return false;
	},

	/**
	 * @function
	 * @desc Adds one or more custom save handlers to do special processing prior
	 * to the actual save. <br/><br/>
	 *
	 * The custom save handler is a function that is invoked with
	 * the following parameters: (editor, data, continueSave).  The 'editor'
	 * and 'data' variables are available to the custom save handler,
	 * as well as a 'continueSave' function which should be called to
	 * continue the save process.  In a synchronous flow, continueSave()
	 * should be called as the last call; in an asynchronous flow,
	 * the callback handler should call continueSave().  Multiple custom
	 * save handlers can be registered, and the save process will begin
	 * by calling the first registered custom save handler.  That handler,
	 * when calling continueSave(), will invoke the second custom save
	 * handler.  At the end of the custom save handler chain--always--
	 * is the default save handler, which does the actual saving.  So
	 * the custom save handlers, as long as they call continueSave(),
	 * should not invoke the API to do the actual save.  At any time,
	 * the chain of save handlers can be broken by not having continueSave()
	 * invoked.  Doing so means the default save handler isn't called and
	 * therefore the save isn't complete.
	 *
	 * @param handler function/array of save handlers
	 * @public
	 */
	addSaveHandler: function(handler) {
		if ( handler ) {
			if (fui.query.isArray(handler)) {
				var len = handler.length;
				for (var i = 0; i < len; ++i) {
					this.saveHandlers.push(handler[i]);
				}
			} else {
				this.saveHandlers.push(handler);
			}
		}
	},


	/**
	 * @function
	 * @desc Adds one or more custom close handlers to do special processing prior
	 * to the actual close. <br/><br/>
	 *
	 * The custom close handler is a function that is invoked with
	 * the following parameters: (editor, continueClose). See notes
	 * on addSaveHandler for general premise.
	 *
	 * @param handler function/array of close handlers
	 * @public
	 */
	addCloseHandler: function(handler) {
		if ( handler ) {
			if (fui.query.isArray(handler)) {
				var len = handler.length;
				for (var i = 0; i < len; ++i) {
					this.closeHandlers.push(handler[i]);
				}
			} else {
				this.closeHandlers.push(handler);
			}
		}
	},

	/**
	 * @function
	 * @desc Adds one or more deactivate listeners to do processing prior to the actual deactivation.
	 *
	 * @param listener function/array of deactivate listener
	 * @param deactivateListenerId The listener ID. It is used only when a single listener is passed in.
	 * 			When a deactivateListenerId is passed, it is not added to the list of listeners if it
	 * 			already exists.
	 * @public
	 */
	addDeactivateListener: function(listener, deactivateListenerId) {
		if ( listener ) {
			if (fui.query.isArray(listener)) {
				var len = listener.length;
				for (var i = 0; i < len; ++i) {
					this.deactivateListeners.push(listener[i]);
				}
			} else {
				if (deactivateListenerId && deactivateListenerId !== "") {
					listener.deactivateListenerId = deactivateListenerId;
					// Add the listener only if a listener with the same ID is not present.
					if (fui.query.grep(this.deactivateListeners, function(item, itemIndex) {
							return (item.deactivateListenerId && item.deactivateListenerId === deactivateListenerId);
						}).length === 0) {
						this.deactivateListeners.push(listener);
					}
				} else {
					// If a deactivateListenerID was not provided, go ahead and push it in...
					this.deactivateListeners.push(listener);
				}
			}
		}
	},


	/**
	 * @function
	 * @desc Sets the callback called on close.
	 *
	 * @param callback the callback
	 * @public
	 */
	setCloseCallback: function(closeCallback) {
		this.closeCallback = closeCallback;
	},

	/**
	 * @function
	 * @desc Sets the save callback called on save.
	 *
	 * @param callback the callback
	 * @public
	 */
	setSaveCallback: function(callback) {
		this.saveCallback = callback;
	},

	/**
	 * @function
	 * @desc Sets the initial object data.
	 *
	 * @param data the initial data
	 * @public
	 */
	setObject: function(type, obj) {
		if ( !obj ) {
			this.object = null;
			return;
		}

		type = type || obj.type;
		obj = fui.creator.createByType(type, obj);

		this.object = obj;

		var e = new fui.editor.Event({
			type: fui.editor.event.CHANGE,
			object: obj
		});

		fui.publish(fui.editor.event.TOPIC_ROOT + this.id, e);
	},

	/**
	 * @function
	 * @desc Returns the registered widget with the given ID, if found.
	 * @public
	 */
	getWidget: function(dataId) {
		return this.widgets[dataId] || null;
	},

	/**
	 * @function
	 * @desc Resets widgets to previous cache value.
	 * @public
	 */
	cleanWidgets: function() {
		var ei=fui.editor.internal;
		for (var widgetId in this.widgets) {
			var widget = this.getWidget(widgetId);
			var cacheValue=this.getWidgetValueCache(widget.getDataId());
			widget.renderValue(cacheValue);
		}
	},

	/**
	 * @function
	 * @desc Displays the properties panel when the properties button is clicked.
	 * @public
	 */
	showProperties: function() {
		if ( !this.ui) {
			return;
		}

		this.addProperties();

		// Get the dock
		var dock = this.getPropertiesDockComponent();

		if (this.propertiesDocked) {
			// If it is floated hide the window
			if (this.properties.window) {
				this.properties.window.hide();
			}
			dock.add(this.properties.ui);
			dock.setHeight(174);
			dock.show();
			this.ui.doLayout();
		} else {
			// First hide the dock.
			dock.hide();
			this.ui.doLayout();

			// Check if the properties was already opened in a window
			// so we can reuse the same window, instead of creating new one.
			if (!this.properties.window) {
				var config = {
					cls: 'fui-properties fui-window fui-window-editor ' + 'fui-properties-' + this.type,
					border: false,
					collapsible: false,
					resizable: true,
					draggable: true,
					modal: false,
					autoScroll: true,
					width: 500,
					height: 297,
					layout: 'fit',
					hideMode: 'visibility',
					closeAction: 'hide',
					title: fui.workspace.getMessage('properties.title') + ': <span class="fui-header-title">' +this.properties.object.name+ '</span>',
					tools: [
						{
							cls: 'fui-tool-help',
							tooltip: fui.editor.getMessage("editor.properties.floating.tooltip.help"),
							handler: fui.scope(this, function(event, toolEl, panel) {
								fui.help.show(fui.help.PROPERTIES_PALETTE);
							})
						},
						{ xtype: 'tbseparator' },
						{
							cls: 'fui-tool-dock',
							tooltip: fui.editor.getMessage("editor.properties.floating.tooltip.dock"),
							handler: fui.scope(this, function(event, toolEl, panel) {
								this.propertiesDocked = true;
								this.showProperties();
							})
						},
						{
							cls: 'fui-tool-close',
							tooltip: fui.editor.getMessage("editor.properties.floating.tooltip.close"),
							handler: fui.scope(this, function(event, toolEl, panel) {
								this.hideProperties();
								var propertiesButton = fui.ext.getCmp(fui.editor.button.properties.BUTTON_ID);
								propertiesButton.setText(fui.editor.button.properties.getTitle(this));
								propertiesButton.setIconCls(fui.editor.button.properties.getIconClass(this));//TODO: v4
							})
						}
					],
					items: [this.properties.ui]
				};

				var win = fui.ext.create(fui.extRootName + '.window.Window', config);
				this.properties.setWindow(win);
				win.show();
				win.doLayout();
				this.properties.window.setTitle(fui.workspace.getMessage('properties.title') + ': <span class="fui-header-title">' +this.properties.object.name+ '</span>');
				
				fui.ext.WindowMgr.bringToFront(win);
			} else {
				// floating properties window was already present. Just display it.
				this.properties.window.setTitle(fui.workspace.getMessage('properties.title') + ': <span class="fui-header-title">' +this.properties.object.name+ '</span>');
				this.properties.window.show();
				this.properties.window.setVisible(true);
				this.properties.window.add(this.properties.ui);
				this.properties.window.doLayout();
			}
		}
	},

	/**
	 * Returns true if the properties is being shown. (either float or docked)
	 */
	isShowingProperties: function() {
		if(!this.ui || !this.properties) {
			return false;
		}

		// Get the dock
		var dock = this.getPropertiesDockComponent();
		if (dock && dock.isVisible()) {
			return true;
		}

		if (this.properties.window && this.properties.window.isVisible()) {
			return true;
		}
	},

	/**
	 * @function
	 * @desc Hides the properties panel when the properties button is clicked (toggle).
	 * @public
	 */
	hideProperties: function() {
		if ( !this.ui || !this.properties ) {
			return;
		}

		var dock = this.getPropertiesDockComponent();
		dock.hide();

		// If it is floated hide the window
		if (this.properties.window) {
			this.properties.window.hide();
	}

		this.ui.doLayout();
	},

	/**
	 * @function
	 * @desc Returns true if the editor is read only.
	 * @public
	 */
	isReadOnly: function(){
		return this.readOnly || (this.object && this.object.editorReadOnly);	
	}
});

/**
 * @desc Editor Event functions
 * @name fui.editor.event
 * @namespace This is the Editor Event namespace
 * @see Editor
 * @public
 */
fui.provide("fui.editor.event");

/**
 * @desc Static functions and constants for the Events namespace.
 * @public
 */
fui.editor.event = {
	/**
	 * @constant
	 * @desc the TOPIC_ROOT
	 * @public
	 */
	TOPIC_ROOT: "/fui/ui/editor/event/",

	/**
	 * @constant
	 * @desc the CHANGE event
	 * @public
	 */
	CHANGE: "change",

	/**
	 * @constant
	 * @desc the LOAD event
	 * @public
	 */
	LOAD: "load",

	/**
	 * @constant
	 * @desc the RELOAD event
	 * @public
	 */
	RELOAD: "reload",

	/**
	 * @constant
	 * @desc the SAVE event
	 * @public
	 */
	SAVE: "save",

	/**
	 * @constant
	 * @desc the CLOSE event
	 * @public
	 */
	CLOSE: "close",

	/**
	 * @function
	 * @desc Subscribe to editor events
	 *
	 * @param id the editor id
	 * @param handler the event handler
	 * @public
	 */
	subscribe: function(id, handler) {
		fui.subscribe(fui.editor.event.TOPIC_ROOT + id, handler);
	},

	/**
	 * @function
	 * @desc Unsubscribe from loader events
	 *
	 * @param id the editor id
	 * @param handler the event handler
	 * @public
	 */
	unsubscribe: function(id, handler) {
		fui.unsubscribe(fui.editor.event.TOPIC_ROOT + id, handler);
	}
};

/**
 * @constructor
 * @desc the Event object
 * @name Event
 * @public
 */
fui.editor.Event = function() {
	if ( (arguments.length == 1) && (typeof arguments[0] == "object") ) {
		fui.combine(this, arguments[0]);
	}
};


/**
 *  @desc Event
 */
fui.extend(fui.editor.Event,
	/**
	 * @lends Event.prototype
 	 */
{
	/**
	 * @constant
	 * @desc The Event type
	 * @public
	 */
	type: "",

	/**
	 * @constant
	 * @desc The Event object
	 * @public
	 */
	object: null
});

/**
 * @desc Set up the private editor section and our requirements
 *
 * @namespace this the editor internal namespace
 * @name fui.editor.internal
 * @private
 */
fui.provide("fui.editor.internal");

fui.require("fui.auth");

fui.editor.internal = {
	/** New editor ID. */
	NEW_EDITOR_ID: 'fui-editor-object-new-',

	/** New editor ID for an object being cloned. */
	CLONE_EDITOR_ID: 'fui-editor-object-new-clone-',

	// new editor index
	newEditorIndex: 0,

	// Editors
	editors: {},

	// Widget name/value cache
	widgetValueCache: {},

	/**
	 * Adds a value to the widgetValueCache
	 * @param widget
	 * @param id the editor parent Id.
	 */
	addWidgetValueCache: function(widget, id) {
		this.widgetValueCache[id]=this.widgetValueCache[id]||{};
		this.widgetValueCache[id][widget.getDataId()]=widget;
	},


	/**
	 * Adds a value to the widgetValueCache
	 * @param widgetId the id of the widget
	 * @param id he editor parent Id
	 */
	getWidgetValueCache: function(widgetId, id) {
		if (!this.widgetValueCache[id]) {
			return null;
		}
		return this.widgetValueCache[id][widgetId];
	},

	/**
	 * For incremental cache deletes.
	 *
	 * @param widget the widget to remove
	 * @param id he editor parent Id
	 */
	removeWidgetValueCache: function(widget, id) {
		if (!this.widgetValueCache[id]) {
			return null;
		}
		delete this.widgetValueCache[id][widget.getDataId()];
	},

	/**
	 * Builds a widget value data structure.
	 *
	 * @param id the parentId
	 */
	getWidgetValueData: function(id) {
		if (!this.widgetValueCache[id]) {
			return null;
		}
		var widgetValueData={};
		for (var item in this.widgetValueCache[id]) {
			var cachedWidget = this.getWidgetValueCache(item, id);
			widgetValueData[item]=cachedWidget.getDataValue();
		}
		return widgetValueData;
	},

	/**
	 * Add an editor.
	 *
	 * @param editor the editor to add
	 */
	add: function(editor) {
		this.editors[editor.id] = editor;
	},

	/**
	 * Remove an editor.
	 *
	 * @param id the id of the editor to remove
	 */
	remove: function(id) {
		delete this.editors[id];
	},

	/**
	 * Find an editor by id.
	 *
	 * @param id the id of the editor to find
	 */
	find: function(id) {
		return this.editors[id];
	},

	save: function(type, typeId, typeXmlName, id, data, requestData) {
		var e = fui.editor;
		requestData = requestData || {};
		requestData.content = data;
		requestData.content[e.TYPE_ATTRIBUTE] = type;
		requestData.content[e.TYPE_ID_ATTRIBUTE] = typeId;
		requestData.content[e.TYPE_XML_NAME_ATTRIBUTE] = fui.html.escape(typeXmlName);
		requestData.content[e.ID_ATTRIBUTE] = id;

		requestData.actionKey = e.ACTION_KEY;
		requestData.method = e.SAVE;

		var request = fui.request.build(requestData);
		fui.io.api(request);
	},

	buildButtonHandler : function(type, id, handler) {
		return function(button) {
			handler(type, id, button);
		};
	},

	buildButtons : function(type, editor, buttons, tbar, disabled, specific) {
		// if no object, create obj shell
		var obj = editor.object || {type: type};

		if (specific === true && buttons.length > 0){
			// Add a separator before type specific buttons
			tbar.add({
				xtype: 'tbseparator'
			});
		}

		for ( var i = 0 ; i < buttons.length ; ++i ) {
			var button = buttons[i];
			var tbButton = fui.ext.create('fui.vext.Button', {
				disabled: disabled || button.disabled,
				text: button.getTitle(editor, obj),
				id: editor.id + "-" + button.id,
				cls: button.getButtonClass(editor, obj),
				iconCls: button.getIconClass(editor, obj),
				handler: this.buildButtonHandler(type, editor.id, button.clickHandler),
				tooltip: button.getTooltip(editor, obj)
			});

			if ( button.setupButton) {
				button.setupButton(type, editor, tbButton);
			}

			tbar.add(tbButton);
		}
	},

	getUpdateFunction: function(config, reqHandler, requestData) {
		var updateFunc = function(ui, editor, data) {
			var ed = fui.editor;

			editor.clearBlock();
			editor.setUI(ui);
			editor.setCloseCallback(config.closeCallback);
			editor.setSaveCallback(config.saveCallback);
			editor.showBlock();
			editor.updateContent(data);

			// Set the properties mode
			// TODO get this from a preference
			// If it is docked then this is true else false.
			editor.propertiesDocked = true;

			// read-only mode is dictated by either the editor or the edited
			// object
			var readOnly = editor.isReadOnly();
			var disabled = readOnly;
			if ( fui.editor.isNewEditorId(editor.id) ) {
				disabled = true;
				// For objects being created newly, there is no need to display the "Last Saved At :" value
				config.skipDate = true;

			}

			var toptbar=null;
			tbar = ui.getDockedComponent('topToolbar');

			var ei = ed.internal;
			var defaultChrome = config.editorChrome == fui.editor.DEFAULT_EDITOR_CHROME;
			var secondaryChrome = config.editorChrome == fui.editor.SECONDARY_EDITOR_CHROME;
			
			// Check for toolbar need
			if ( defaultChrome && !config.skipToolbar ) {
				var eb = ed.button;
				tbar.addCls(ed.CONTENT_TOOLBAR_CLS);

				if (config.toolbarBuilder) {
					config.toolbarBuilder(editor.id, tbar, requestData);
				} else if (editor.toolbarProperties !== false) {
					// Build properties which is always at the beginning
					var propertiesButton = eb.properties.get();
					var cloneId = editor.cloneId;
					ei.buildButtons(config.type, editor, [propertiesButton], tbar, cloneId && cloneId.length >0);
					var propertiesHelpButton = eb.prophelp.get();
					ei.buildButtons(config.type, editor, [propertiesHelpButton], tbar, false);
					// Add a separator after the help icon on the toolbar.
					tbar.add( { xtype: 'tbseparator' });
				}

				if(!config.skipType ){
					// Add a fill before type specific buttons
					tbar.add({
						xtype: 'tbfill'
					});

					// Build type specific buttons
					ei.buildButtons(config.type, editor, eb.getButtons(config.type, config.objectTypeXmlName), tbar, disabled, true);
				}

				if ( !config.skipSave ) {
					if ( config.skipType ) {
						tbar.add({
							xtype: 'tbfill'
						});
					}

					// Add a separator before save
					tbar.add({
						xtype: 'tbseparator'
					});

					// Build save which is always at the end
					var buttons = [fui.editor.button.save.getSave(readOnly), fui.editor.button.save.getClose(readOnly)];
					ei.buildButtons(config.type, editor, buttons, tbar, false);
				}

				if (!editor.hidden && (tbar.items.length > 0)) {
					tbar.doLayout();
					tbar.show();
					if (toptbar) {
						toptbar.doLayout();
						toptbar.show();
					}
				} else{
					tbar.hide();
				}
			} else  {
				tbar.hide();
			}

			// Check for footerbar need
			var fbar = editor.getFooterToolbar();
			if ( secondaryChrome && !config.skipFooterBar ) {
				if ( !fbar ) {
					fbar = fui.ext.create(fui.extRootName + '.toolbar.Toolbar', {
						dock: 'bottom',
						itemId: 'bottomToolbar',
						ui: "footer",
						cls: "fui-toolbar",
						items: [{
							id:this.CONTENT_BOTTOMBAR_ID+'-'+editor.id,
							hidden: true
						}]
					});
					ui.dockedItems.add(fbar);
				}
				fbar.addCls(ed.CONTENT_FOOTERBAR_CLS);
				if ( config.footerbarBuilder ) {
					config.footerbarBuilder(editor.id, fbar, requestData);
				}

				fbar.add( { xtype: 'tbfill' });

				if (!config.skipOkCancel ) {
					buttons = [fui.editor.button.save.getOk(readOnly), fui.editor.button.save.getCancel()];
					ei.buildButtons(config.type, editor, buttons, fbar, false);
				}

				if (!editor.hidden && (fbar.items.length > 0)) {
					fbar.doLayout();
					fbar.show();
				} else{
					fbar.hide();
				}
			} else if ( fbar ) {
				fbar.hide();
			}

			// Layout or hide the bottom bar
			var bbar = ui.getDockedComponent('bottomToolbar');
			if ( bbar ) {
				if ( bbar.items.length > 0 ) {
					bbar.addCls(ed.CONTENT_TOOLBAR_CLS);
					bbar.doLayout();
					bbar.show();
				} else if ( !fbar || (fbar.items.length === 0) ) {
					bbar.hide();
				}
			}

			ui.doLayout();
			
			// Updating the "Last Saved At : " date value with the last modification time of the object / creation time
			if ( !config.skipDate  && editor.object) {
				editor.setTime(editor.object.lastModificationTime);
			}

			editor.clearBlock();
			if ( reqHandler ) {
				reqHandler(data, editor, ui);
			}

			if (config.extraSetup) {
				config.extraSetup(editor, config);
			}

			// Fire the LOAD event for the editor.
			// Fire the save event.
			var event = new fui.editor.Event({
				type: fui.editor.event.LOAD,
				object: editor
			});

			fui.publish(fui.editor.event.TOPIC_ROOT + editor.id, event);

		};

		return updateFunc;
	},

	/**
	 * The listener for auth events.
	 *
	 * @param domEvent the DOM event
	 * @param fuiEvent the fui event
	 */
	authListener: function(domEvent, fuiEvent) {
		// We only care about invalidate
		if ( fuiEvent.type !== fui.auth.event.INVALIDATE ) {
			return;
		}

		// Kill all editors
		var ei = fui.editor.internal;
		for (var id in ei.editors) {
			var editor = ei.find(id);
			if ( editor ) {
				editor.close(true);
			}
		}
	}
};

//Overrides
(function override() {
	//Subscribe to events
	fui.auth.event.subscribe(fui.editor.internal.authListener);
})();
