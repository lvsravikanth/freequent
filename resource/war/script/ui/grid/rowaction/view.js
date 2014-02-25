/**
 * Set up the view section and our requirements
 */
fui.provide("fui.ui.grid.rowaction.view");

fui.ui.grid.rowaction.view = {
	BUTTON_ID : 'fui-grid-action-view',
	BUTTON_CLASS : 'fui-grid-action-view ui-icon ui-icon-note',
	DEFAULT_INDEX : 3,

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
			getTitle : function() { return fui.workspace.getMessage('grid.toolbar.view.title'); }
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

		var requestData = {};

		var type = obj.type;

		var requestData = {
			};
		var editConfig = {
			type: type,
			id: obj.id,
			ACTION_KEY: fui.ui.type.getActionKey(type)
		};
		//alert('in view ' + obj.id);
		fui.ui.view.viewContentItem(obj);
		//fui.ui.editor.edit(editConfig, requestData);
	}
};

//Register
fui.grid.rowaction.register(fui.ui.grid.rowaction.view.get());
