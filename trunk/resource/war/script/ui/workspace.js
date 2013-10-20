fui.provide("fui.ui.workspace");

/**
 * @desc Static functions and constants for the vcm workspace.
 * @public
 */
fui.ui.workspace = {
	/**
	 * @function
	 * @desc Editor save callback, invoked after an editor saves data.
	 * 
	 * @param data the editor data
	 * @param noAssociations set <code>true</code> to not associate newly
	 *    created content items with the currently active channel or
	 *    cagetory
	 * @public
	 */
	editorSaveCallback: function(data, noAssociations) {
		data=data||{};
		var messageType = data.isNew ? "message.action.created" : "message.action.updated";
		var action = fui.workspace.getMessage(messageType);

		var doneHandler = function() {
			var w = fui.ui.workspace;
			if (data.object) {

				var msg = w.getMessage("message.success.single");
				var name = '';
				var type = '';

				name = fui.html.escape(data.object.name);
				type = fui.html.escape(data.object.displayname);//fui.grid.getObjectTypeDisplayName(data.object));

				// show success msg
				fui.notification.message(fui.string.replace(msg, {
					name: name,
					type: type,
					action: action
				}));

				// Content event
				var contentEvent = new fui.ui.content.Event({
					type: data.isNew? fui.content.event.CREATE : fui.content.event.UPDATE,
							objects: [data.object]
				});

				// Publish it
				fui.publish(fui.content.event.TOPIC, contentEvent);
			}
		};
		
		var callHandler = true;

		if (callHandler){
			doneHandler();
		}
	},
	
	/**
	 * A workspace level callback after editor closes.
	 */
	editorCloseCallback: function() {
	}
};