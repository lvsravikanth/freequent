/**
 * Set up the edit section and our requirements
 */
fui.provide("fui.ui.grid.rowaction.del");

fui.ui.grid.rowaction.del = {
	BUTTON_ID : 'fui-grid-action-delete',
	BUTTON_CLASS : 'fui-grid-action-delete ui-icon ui-icon-trash',
	DEFAULT_INDEX : 1,
    METHOD: 'delete',

	/**
	 * Return the edit row action.
	 */
	get : function() {
		return new fui.grid.RowAction({
			id : this.BUTTON_ID,
			rowactionClass : this.BUTTON_CLASS,
			index : this.DEFAULT_INDEX,
			supports: this.supports,
			clickHandler:  this.clickHandler,
			getTitle : function() { return fui.workspace.getMessage('grid.toolbar.edit.title'); }
		});
	},

	/**
	 * Returns true if this row action supports the type.
	 *
	 * @param type the type to check
	 */
	supports: function(type, objectTypeXmlName) {
		return true;
	},

	/**
	 * Click Handler.
	 *
	 * @param obj the object
	 */
	clickHandler: function(obj) {  
		var ctx = fui.context.getContext();

		var type = obj.type;
		var func = fui.scope(this, function() {
			alert ("delete obj.id=" + obj.id);
			// do the job
			/*var requestData = {};
                requestData.content.id = obj.id;
                var c = fui.ui.content;
		        c.internal.sendAPI(this, this.METHOD, requestData);*/
		});
        var confirmTitle = fui.workspace.getMessage("delete.confirm.title");
		var confirmText = fui.string.replace(fui.workspace.getMessage("delete.confirm.text.single.1"),
					{name: obj.name, type: type})

        fui.msg.confirm(confirmTitle, confirmText, function(buttonId) {
					if (buttonId === 'yes') {
						func();
					} else {
						//do nothing.
					}
				});
	}
};

//Register
fui.grid.rowaction.register(fui.ui.grid.rowaction.del.get());
