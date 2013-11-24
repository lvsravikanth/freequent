
fui.provide("fui.ui.manageitems");

fui.require("fui.ui.content");
fui.require("fui.ui.type");

fui.ui.manageitems = {

    ID_ATTRIBUTE: "id",

	/**
	 * The action key
	 */
	ACTION_KEY: "manageitems",

    /**
	 * Methods
	 */
	SEARCH: "runsearch",

    /**
     * requires the following data.
     * params.userid
     * params.firstname
     * params.lastname
     *
     * @param data
     */
    runSearch: function(params, requestData){
		if ( fui.log.isDebug() ) { fui.log.debug("executing user search" ); }

		requestData = requestData || {};
		requestData.content = requestData.content || {};
		requestData.content.name = params.name;
		requestData.content.group = params.group;
		requestData.content.category = params.category;
        requestData.handler = this.getRunSearchHandler();

		var c = fui.ui.content;
		c.internal.sendAPI(this, this.SEARCH, requestData);
	},

    getRunSearchHandler: function() {
        return function(data) {
            var grid = fui.grid.find(fui.ui.type.ITEM);
            grid.updateData(data);
        };
    },

	edit: function(id) {
		var requestData = {
			};
		var editConfig = {
			type: fui.ui.type.ITEM,
			id: id,
			ACTION_KEY: fui.ui.manageitems.ACTION_KEY,
			validate: this.validate //validate function for to validate the form
		};
		fui.ui.editor.edit(editConfig, requestData);
	},

	/**
	 * validates the user edit form.
	 *
	 * @param editor
	 * @param data
	 * @param requestData
	 */
	validate: function(editor, data, requestData) {
		var isValid = fui.query('#'+editor.formId).valid();
		return isValid;
	}
};