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
        var reloadFunc = function(btn) {
			if (btn == 'yes') {
                alert(" alerting delete confirmed");
			    /*var requestData = {};
                requestData.content.id = obj.id;
                var c = fui.ui.content;
		        c.internal.sendAPI(this, this.METHOD, requestData);*/
			}
        };
        var reloadTitle = fui.editor.getMessage("reload.confirm.title");
		var reloadText = fui.editor.getMessage("reload.confirm.text");

        fui.msg.confirm(reloadTitle, reloadText, reloadFunc);
	}
};

//Register
fui.grid.rowaction.register(fui.ui.grid.rowaction.del.get());
