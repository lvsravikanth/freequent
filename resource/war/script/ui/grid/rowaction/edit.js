/**
 * Set up the edit section and our requirements
 */
fui.provide("fui.ui.grid.rowaction.edit");

fui.ui.grid.rowaction.edit = {
	BUTTON_ID : 'fui-grid-action-edit',
	BUTTON_CLASS : 'fui-grid-action-edit ui-icon ui-icon-pencil',
	DEFAULT_INDEX : 1,

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

		var requestData = {};

		var type = obj.type;

		var requestData = {
			};
		var editConfig = {
			type: type,
			id: obj.id,
			ACTION_KEY: fui.ui.type.getActionKey(type)
		};

		if (type === fui.ui.type.USER) {
			editConfig.idAttribute = 'userid'
		}
		fui.ui.editor.edit(editConfig, requestData);
	}
};

//Register
fui.grid.rowaction.register(fui.ui.grid.rowaction.edit.get());
