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

fui.require("fui.html");
fui.require("fui.io");
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
	DEFAULT_ACTION_KEY: "editor",

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
	 * @desc The CLONE_ATTRIBUTE attribute, which corresponds to the same on the server-side.
	 * @public
	 */
	CLONE_ATTRIBUTE: '.fui.editor.clone',

	/**
	 * @constant
	 * @desc The TYPE attribute, which corresponds to the same on the server-side.
	 * @public
	 */
	TYPE_ATTRIBUTE: '.fui.editor.type',

	/**
	 * @constant
	 * @desc The ID attribute, which corresponds to the same on the server-side.
	 * @public
	 */
	ID_ATTRIBUTE: '.fui.editor.id',


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
		var activeWin;//fui.ext.WindowManager.getActive(); //todo
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

			var editorCls = 'fui-editor fui-editor-' + editor.id +' fui-editor-' + editConfig.type + ' fui-window fui-window-editor fui-editor-no-native-close';

			var config = {
				id: editor.id,
				autoOpen: false,
				dialogClass: editorCls,
				buttons: [], //todo
				closeOnEscape: false,
				draggable: true,
				height: editor.height,
				width: editor.width,
				hide: "explode",
				modal: true,
				resizable: true,
				title: "TODO: TITLE" //todo
			};

			var listenAndUpdate = function(evnt) {
				var ui = fui.query(evnt.currentTarget);
				updateFunc(ui, editor, data);
				var contentId = fui.editor.CONTENT_ID + '-' + editor.id;
				//fui.query('#' + contentId).mousedown(deactivator);
				//reset the size so that everything lays out correctly (like toolbars).  
				//not calling the fui.vext.forceLayout(ui); since that will use the container size 
				//and for editors that is the body and is very big.
				//ui.setSize(ui.getSize());
			};

			var contentDivId =  this.CONTENT_ID + '-' + editor.id;
			//check if this div already present
			var contentdiv = fui.byId(contentDivId);
			if (contentdiv) {
				// empty the content
				fui.query(contentdiv).empty();
			} else {
				// create the content div
				fui.query("<div id='"+contentDivId+"' class='"+this.CONTENT_ID+"'></div>").appendTo("body");
			}

			// initialize the dialogue
			var ui = fui.query( "#" +contentDivId ).dialog (config);
			// attach open event
			fui.query( "#" +contentDivId ).on( "dialogopen", listenAndUpdate );
			// open the dialogue
			fui.query( "#" +contentDivId ).dialog( "open" );
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
			collectors: editConfig.collectors,
			checkers: editConfig.checkers,
			saveHandlers: editConfig.saveHandlers,
			closeHandlers: editConfig.closeHandlers,
			initialData: editConfig.initialData,
			readOnly: editConfig.readOnly,
			cloneId: cloneId,
			editConfig: editConfig,
			type: editConfig.type
		});

		if (editConfig.ACTION_KEY) {
			editor.ACTION_KEY = editConfig.ACTION_KEY;
		}
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
	this.height = "auto";
	this.width = "650";
	this.type = "";
	this.ACTION_KEY = fui.editor.DEFAULT_ACTION_KEY;
	this.formId = null;

	if ( (arguments.length == 1) && (typeof arguments[0] == "object") ) {
		fui.combine(this, arguments[0]);
	}

	if (!fui.query.isArray(this.collectors)) {
		this.collectors = (this.collectors ? [this.collectors] : []);
	}
	if (!fui.query.isArray(this.checkers)) {
		this.checkers = (this.checkers ? [this.checkers] : []);
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
		if ( !content ) {
			return { width: "650", height: "auto" };
		}

		return content.getSize(true); //todo
	},

	/**
	 * @function
	 * @desc Returns the content component for editor.
	 * @public
	 */
	getContentComponent: function() {
		var id = this.componentId || this.id;
		var contentId = fui.editor.CONTENT_ID + '-' + id;
		return fui.query("#"+contentId);//fui.ext.getCmp(contentId);
	},

	/**
	 * @function
	 * @desc Returns the bottom toolbar for editor.
	 * @public
	 */
	getBottomToolbar: function() {
		var id = this.componentId || this.id ;
		var toolbarId = fui.editor.CONTENT_BOTTOMBAR_ID + '-' + id;
		return fui.query("#"+toolbarId);//fui.ext.getCmp(toolbarId);
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
			content.empty();

			// reload the editor with the same config with which the editor was first loaded
			var updateFunc = fui.editor.internal.getUpdateFunction(this.editConfig);

			// This updates the content area with the new data and also creates new buttons with the new Id
			updateFunc(this.ui, this, data);
		});

		this.deactivateListeners = [];

		// Load the editor with the newly saved object
		this.load(requestData);

		// Fire the reload event.
		var e = new fui.editor.Event({
			type: fui.editor.event.RELOAD,
			object: this
		});

		fui.publish(fui.editor.event.TOPIC_ROOT + oldId, e);
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
			if (this.ui) {  // TODO: fix this for the editor save scenario
				this.ui.parent().unmask();
			}
		} else {
			var body = fui.query("body");
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
		content.empty();
		var contentPanelId = content.attr("id");
		//fui.html.set(contentPanelId, data, true, true, true);
		fui.query("#"+contentPanelId).html(data);
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
	 * @desc Loads an editor.
	 *
	 * @param requestData the request data
	 * @public
	 */
	load: function(requestData) {
		var e = fui.editor;
		requestData = requestData || {};
		requestData.content = requestData.content || {};

		// Set clone id and readonly state
		requestData.content[e.CLONE_ATTRIBUTE] = this.cloneId ? this.cloneId : undefined;
		requestData.content[e.READONLY_ATTRIBUTE] = this.readOnly ? this.readOnly : undefined;
		
		// Set id
		requestData.content[e.ID_ATTRIBUTE] = this.id || (this.cloneId ? e.getCloneEditorId() : e.getNewEditorId());

		// Copy over content
		fui.query.extend(this.requestContent, requestData.content);
		
		requestData.actionKey = requestData.actionKey || this.ACTION_KEY;
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

			return fui.ui.errorHandler(message, rootMessage);
		});

		var orgAuthHandler = requestData.authorizationHandler;
		requestData.authorizationHandler = fui.scope(this, function(response) {
			this.clearBlock();
			if (orgAuthHandler) {
				return orgAuthHandler(response);
			}
			return fui.ui.authorizationHandler(response);
		});

		this.showBlock();

		request = fui.request.build(requestData);
		fui.io.api(request);
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

		if (this.ACTION_KEY) {
			requestData.actionKey = this.ACTION_KEY;
		}

		if (this.formId) {
			requestData.formId = this.formId;
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
				this.hasSaved = true;

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
				return fui.ui.errorHandler(message, rootMessage);
			});

			this.showBlock({
				blockMessage: fui.editor.getMessage('saving')
			});

			// Check if we need to clone
			if ( this.cloneId ) {
				data[fui.editor.CLONE_ATTRIBUTE] = this.cloneId;
			}

			fui.editor.internal.save(this.type, this.id, data, requestData);
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
	 * @desc Destroys the editor.
	 *
	 * @param noHooks flag to disable callbacks and handlers
	 * @public
	 */
	close: function(noHooks) {
		if ( !noHooks && this.closeCallback ) {
			this.closeCallback();
		}

		if ( this.ui ) {
			var contentPanelId = this.ui.attr("id");
			this.ui.off('dialogbeforeclose', this.closeHandler);
			this.ui.off( "dialogopen" );
			this.ui.empty();
			this.ui.dialog("close");
			this.ui.dialog("destroy");
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
			var contentPanelId = ui.attr("id");
			//fui.query("#"+contentPanelId).on('dialogbeforeclose', fui.scope(this, this.closeHandler, this));
			//fui.query("#"+contentPanelId).on( "dialogclose", fui.scope(this, this.destroy, this) ); //we will handle closing dialog

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
	closeHandler: function(editor) {
		if ( editor.hasChanged() ) {
			var func = function(button) {
				if ( button === "cancel") {
					// do nothing
				} else if( button === "yes") {
					editor.save( {close: true} );
				} else {
					editor.close();
				}
			};
			var e = fui.editor;
			var message;
			var title = e.getMessage('close.changes.title');
			message = fui.string.replace(e.getMessage('save.changes'));
			fui.msg.confirmSave(title, message, func, this);
			return false;
		}
		editor.close();
		return true;
	},

	/**
	 * @function
	 * @desc Sets the editor title.
	 *
	 * @param displayType the display type
	 * @param title the title
	 * @param escaped flag to indicate if title is escaped
	 * @public
	 */
	setTitle: function(displayType, title, escaped) {
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

		var displayTitle = hasTitle ? ' : ' + title : '';
		var uiTitle = displayType + displayTitle;

		this.ui.dialog( "option", "title", uiTitle );
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

		// validate the editor data
		if (this.editConfig.validate) {
			if (!bypassValidators && !this.editConfig.validate(this, data, requestData)) {
				validData = false;
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
			//todo
			fui.notification.warning(fui.editor.getMessage("other.tabs.message"), 5000);
			return null;
		}

		// Store the data
		this.data = data;

		return this.data;
	},

	/**
	 * @function
	 * @desc Checks to see if the editor data has changed.
	 * @public
	 */
	hasChanged: function() {
		if (this.formId) {
			return fui.query('#'+this.formId).checkDirty();
		}

		return true;
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
	 * @desc Returns true if the editor is read only.
	 * @public
	 */
	isReadOnly: function(){
		return this.readOnly || (this.object && this.object.editorReadOnly);	
	},

	/**
	 * sets the formId value for this editor.
	 * @param formId
	 */
	setFormId: function(formId) {
		this.formId = formId;
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

	save: function(type, id, data, requestData) {
		var e = fui.editor;
		requestData = requestData || {};
		requestData.content = data;
		requestData.content[e.TYPE_ATTRIBUTE] = type;
		requestData.content[e.ID_ATTRIBUTE] = id;

		requestData.actionKey = requestData.actionKey || e.ACTION_KEY;
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

		for ( var i = 0 ; i < buttons.length ; ++i ) {
			var button = buttons[i];
			var tbButton = {
				text: fui.html.unescape(button.getTitle(editor, obj)),
				disabled: disabled || button.disabled,
				id: editor.id + "-" + button.id,
				click: this.buildButtonHandler(type, editor.id, button.clickHandler)
			}; /*fui.ext.create('fui.vext.Button', {
				disabled: disabled || button.disabled,
				text: button.getTitle(editor, obj),
				id: editor.id + "-" + button.id,
				cls: button.getButtonClass(editor, obj),
				iconCls: button.getIconClass(editor, obj),
				handler: this.buildButtonHandler(type, editor.id, button.clickHandler),
				tooltip: button.getTooltip(editor, obj)
			});*/ //todo

			if ( button.setupButton) {
				button.setupButton(type, editor, tbButton);
			}

			tbar.push(tbButton);
		}
	},

	getUpdateFunction: function(config, reqHandler, requestData) {
		var updateFunc = function(ui, editor, data) {
			var ed = fui.editor;

			editor.clearBlock();
			editor.setUI(ui);
			var displayType = editor.type ? editor.type : null;
			var title = fui.editor.isNewEditorId(editor.id) ? ed.getMessage("new") : (data.name ? data.name : null);
			editor.setTitle(displayType, title, true);
			editor.setCloseCallback(config.closeCallback);
			editor.setSaveCallback(config.saveCallback);
			editor.showBlock();
			editor.updateContent(data);

			// read-only mode is dictated by either the editor or the edited
			// object
			var readOnly = editor.isReadOnly();
			var disabled = readOnly;
			if ( fui.editor.isNewEditorId(editor.id) ) {
				disabled = true;
			}

			var ei = ed.internal;

			// setup top tools
			// Tools
			var tools = [];
			// Close
			tools.push({
				id: 'fui-tool-close-'+editor.id,
				cls: 'ui-icon-close',
				tooltip: fui.editor.getMessage("editor.tooltip.close"),
				handler: function() {
					if (editor.hasChanged()) {
						var func = function(button) {
							if (button === "cancel") {
								// do nothing
							} else if (button === "yes") {
								editor.save({close: true});
							} else {
								editor.close();
							}
						};
						var e = fui.editor;
						var message;
						var title = e.getMessage('close.changes.title');
						message = fui.string.replace(e.getMessage('save.changes'));
						fui.msg.confirmSave(title, message, func, this);
						return false;
					}
					editor.close();
					return true;
				}
			});
			// Refresh
			tools.push({
				id: 'fui-tool-refresh-'+editor.id,
				cls: 'ui-icon-refresh',
				tooltip: fui.editor.getMessage("editor.tooltip.refresh"),
				handler: function() {
					if (editor.hasChanged()) {
						var reloadFunc = function(btn) {
							if (btn == 'yes') {
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



			ei.setupTools(editor, tools);

			// setup bottom buttons
			var dialogButtons = [];
			if ( !config.skipSave ) {
				// Build save which is always at the end
				var buttons = [fui.editor.button.save.getSave(readOnly), fui.editor.button.save.getClose(readOnly)];
				ei.buildButtons(config.type, editor, buttons, dialogButtons, false);
			}

			if (!editor.hidden && (dialogButtons.length > 0)) {
				// show the dialog buttons
				ui.dialog( "option", "buttons", dialogButtons );
			} else{
				//tbar.hide(); //todo
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
	},

	setupTools: function(editor, tools) {
		var dialogClass = 'fui-editor-'+editor.id;
		for (var i = 0; i < tools.length; i++) {
			var tool = tools[i];
			//check if tool already exists
			if (fui.byId(tool.id)) {
				fui.query("#"+tool.id ).button( "destroy" );
			} else {
				fui.query("."+dialogClass).children(".ui-dialog-titlebar").append("<button id='"+ tool.id +"' class='fui-tool-button' title='"+tool.tooltip+"'></button>");
			}

			var toolButtonConfig = {
				icons: {primary: tool.cls},
				text: false,
				disabled: tool.disabled || false
			};
			fui.query("#"+tool.id).button(toolButtonConfig)
				.click(tool.handler);
		}
	}
};

//Overrides
(function override() {
	//Subscribe to events
	fui.auth.event.subscribe(fui.editor.internal.authListener);
})();
