/**
 * Set up the view section and our requirements
 */
fui.provide("fui.ui.view");

fui.ui.view = {

	VIEW_WINDOW_ID: 'fui-view-window',
	CONTENT_ID: 'fui-view-content',
	HEIGHT: "600",
	WIDTH: "800",
    MIN_WIDTH: 300,
    MAX_WIDTH: "auto",
    MIN_HEIGHT: 200,
    MAX_HEIGHT: "auto",

	/**
	 * View the Content Item.
	 *
	 * @param obj the Content Item object with the required properties set
	 */
	viewContentItem: function(obj, viewConfig, requestData) {
		if (fui.log.isDebug()) {
			fui.log.debug("Viewing Content Item");
		}
		viewConfig = viewConfig || {};
		requestData = requestData || {};
		var updateFunc = this.internal.getUpdateFunction(viewConfig, requestData.handler);
		requestData.content = {};
		requestData.handler = function(data) {
			// editor.showBlock(); //todo

			var viewCls = 'fui-view fui-view-' + obj.type + ' fui-window fui-window-editor fui-editor-no-native-close';

			var config = {
				id: fui.ui.view.VIEW_WINDOW_ID,
				autoOpen: false,
				dialogClass: viewCls,
				buttons: [], //todo
				closeOnEscape: false,
				draggable: true,
				width: fui.ui.view.WIDTH,
				height: fui.ui.view.HEIGHT,
				minWidth: fui.ui.view.MIN_WIDTH,
				minHeight: fui.ui.view.MIN_HEIGHT,
				maxWidth: fui.ui.view.MAX_WIDTH,
				maxHeight: fui.ui.view.MAX_HEIGHT,
				hide: "explode",
				modal: true,
				resizable: true,
				title: "View"
			};

			var listenAndUpdate = function(evnt) {
				var ui = fui.query(evnt.currentTarget);
				updateFunc(ui, data);
			};

			var contentDivId = fui.ui.view.CONTENT_ID;
			//check if this div already present
			var contentdiv = fui.byId(contentDivId);
			if (contentdiv) {
				// empty the content
				fui.query(contentdiv).empty();
			} else {
				// create the content div
				fui.query("<div id='" + contentDivId + "' class='" + fui.ui.view.CONTENT_ID + "'></div>").appendTo("body");
			}

			// initialize the dialogue
			var ui = fui.query("#" + contentDivId).dialog(config);
			// attach open event
			fui.query("#" + contentDivId).on("dialogopen", listenAndUpdate);
			// open the dialogue
			fui.query("#" + contentDivId).dialog("open");
			//fui.query('.ui-dialog-content').children().addClass('fui-editor-container');
			/*if (editConfig.message) {
				var cls = editConfig.message.cls || fui.notification.WARNING;
				var text = editConfig.message.text || '';
				var autoClose = editConfig.message.autoClose || 5000;
				fui.notification.show(cls, text, autoClose);
			}*/
		};

		var content = fui.ui.content;
		content.internal.sendAPI({ACTION_KEY: "report"},"view", requestData);
	}
};

/**
 * @desc Set up the private editor section and our requirements
 *
 * @namespace this the editor internal namespace
 * @name fui.editor.internal
 * @private
 */
fui.provide("fui.ui.view.internal");

fui.require("fui.auth");

fui.ui.view.internal = {
	 getUpdateFunction: function(config, reqHandler, requestData) {
		var updateFunc = function(ui, data) {
			var vi = fui.ui.view.internal;

			// editor.clearBlock(); //todo
			/*editor.setUI(ui);
			var displayType = editor.type ? editor.type : null;
			var title = fui.editor.isNewEditorId(editor.id) ? ed.getMessage("new") : (data.name ? data.name : null);
			editor.setTitle(displayType, title, true);
			editor.setCloseCallback(config.closeCallback);
			editor.setSaveCallback(config.saveCallback);*/
			//editor.showBlock(); //todo
			vi.updateContent(data);

			//var ei = ed.internal;

			// setup bottom buttons
			var dialogButtons = [];
			var closeBtn = {
				text: fui.html.unescape(fui.editor.getMessage('toolbar.close.title')),
				disabled: false,
				id: fui.ui.view.VIEW_WINDOW_ID + "-" + "btn-close",
				click: vi.buildButtonHandler("close")
			};
			var printBtn = {
				text: fui.html.unescape(fui.editor.getMessage('toolbar.print.title')),
				disabled: false,
				id: fui.ui.view.VIEW_WINDOW_ID + "-" + "btn-print",
				click: vi.buildButtonHandler("print")
			};
			dialogButtons.push (printBtn);
			dialogButtons.push (closeBtn);
			// show the dialog buttons
			var contentDivId = fui.ui.view.CONTENT_ID;
			var viewPanel = fui.query("#" + contentDivId);
			viewPanel.dialog( "option", "buttons", dialogButtons ); 
			/*if ( !config.skipSave ) {
				// Build save which is always at the end
				var buttons = [fui.editor.button.save.getSave(readOnly), fui.editor.button.save.getClose(readOnly)];
				var registeredButtons = fui.editor.button.getButtons(config.type);
				fui.query.merge(buttons, registeredButtons);
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
			});*/

			// fui.publish(fui.editor.event.TOPIC_ROOT + editor.id, event); //todo

		};

		return updateFunc;
	},

	/**
	 * @function
	 * @desc Update the content area.
	 *
	 * @param data the content data
	 * @public
	 */
	updateContent: function(data) {
		var contentDivId = fui.ui.view.CONTENT_ID;

		// empty the content
		fui.query("#"+contentDivId).empty();
		//fui.html.set(contentDivId, data, true, true, true);
		fui.query("#"+contentDivId).html(data);
	},

	buildButtonHandler : function(type) {
		return function(button) {
			if (type === "close") {
				var contentDivId = fui.ui.view.CONTENT_ID;
				var viewDialog = fui.query("#" + contentDivId);
				//var contentPanelId = this.ui.attr("id");
				viewDialog.off('dialogbeforeclose', this.closeHandler);
				viewDialog.off( "dialogopen" );
				viewDialog.empty();
				viewDialog.dialog("close");
				viewDialog.dialog("destroy");
			} else if (type === "print") {
				alert ("in print");
			}
		};
	}
};