/**
 * Set up the edit section and our requirements
 */
fui.provide("fui.ui.grid.rowaction.del");

fui.ui.grid.rowaction.del = {
	BUTTON_ID : 'fui-grid-action-delete',
	BUTTON_CLASS : 'fui-grid-action-delete ui-icon ui-icon-trash',
	DEFAULT_INDEX : 2,
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
			getTitle : function() { return fui.workspace.getMessage('grid.toolbar.delte.title'); }
		});
	},

	/**
	 * Returns true if this row action supports the type.
	 *
	 * @param type the type to check
	 */
	supports: function(type, objectTypeXmlName) {
		if(type !== fui.ui.type.USER) {
            return true;
        }        
	},

	/**
	 * Click Handler.
	 *
	 * @param obj the object
	 */
	clickHandler: function(obj) {  
		var ctx = fui.context.getContext(),
            confirmTitle = fui.workspace.getMessage("delete.confirm.title"),
		    confirmText = fui.string.replace(fui.workspace.getMessage("delete.confirm.text.single.1"),
					                {name: obj.name, type: obj.type});
        this.obj = obj;
		var func = fui.scope(this, function() {
            var rowDelete = fui.ui.grid.rowaction.del,
                requestData = requestData || {};                
            
            this.ACTION_KEY = fui.ui.type.getActionKey(obj.type);
            requestData.content = requestData.content || {};
            requestData.content.id = obj.id;
            requestData.content.name = obj.name;
            requestData.handler = fui.scope(this, function() {
                var w = fui.workspace,
                    msg = w.getMessage("message.success.single"),
		            action = fui.workspace.getMessage( "message.action.deleted"),
                    name = fui.html.escape(obj.name),
                    type = fui.html.escape(obj.type);
                // show success msg
                fui.notification.message(fui.string.replace(msg, {
                    name: name,
                    type: type,
                    action: action
                }));
            });
            var c = fui.ui.content;
		    c.internal.sendAPI(this, rowDelete.METHOD, requestData);
		});        
        fui.msg.confirm(confirmTitle, confirmText, function(buttonId) {
            if (buttonId === 'yes') {
                func();
            }
		});
	}
};

//Register
fui.grid.rowaction.register(fui.ui.grid.rowaction.del.get());
