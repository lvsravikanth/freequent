/**
 *
 * @desc Set up the button section and our requirements
 *
 * @namespace this is the Button namespace
 * @name fui.ui.editor.button
 * @see Button
 * @public
 */
fui.provide("fui.editor.button");
fui.provide("fui.editor.Button");


/**
 * @desc Static functions and constants for the button.
 * @public
 */
fui.editor.button = {

	/**
	 * @function
	 * @desc Registers a button.
	 * @param button, the button to register
	 * @public
	 */
	register : function(button) {
		fui.editor.button.internal.add(button);
	},


	/**
	 * @function
	 * @desc Unregisters a button.
	 * @param id, the button ID
	 * @public
	 */
	unregister : function(id) {
		fui.editor.button.internal.remove(id);
	},


	/**
	 * @function
	 * @desc Finds the button.
	 * @param id, the button ID
	 * @public
	 */
	find : function(id) {
		return fui.editor.button.internal.find(id);
	},

	/**
	 * @function
	 * @desc returns the full set of buttons by type and XMLName.
	 * @param type
	 * @param objectTypeXmlName
	 * @returns an array of Buttons.
	 * @public
	 */
	getButtons : function(type, objectTypeXmlName) {
		var bi = fui.editor.button.internal;

		var buttons = [];

		for ( var id in bi.buttons) {
			var button = bi.buttons[id];
			if ( button.supports(type, objectTypeXmlName) ) {
				buttons.push(button);
			}
		}

		var indexSort = function(a, b) {
			return a.index - b.index;
		};

		buttons.sort(indexSort);

		return buttons;
	}
};


/**
* @constructor
* @name Button
* @desc setups the button
* @public
 */
fui.editor.Button = function() {
	if ( (arguments.length == 1) && (typeof arguments[0] == "object") ) {
		fui.combine(this, arguments[0]);
	}
};

/**
 * @desc Button object
 */
fui.extend(fui.editor.Button, {
	/**
	 @lends Button.prototype
	 */

	/**
	 * @constant
	 * @desc the button ID: : implemented by extenders.
	 */
	id : null,

	/**
	 * @constant
	 * @desc the button index order: : implemented by extenders.
	 */
	index : 100,

	/**
	 * @constant
	 * @desc the button disabled state: : implemented by extenders.
	 */
	disabled: false,

	/**
	* @function
	* @desc Defines the scenarios by which the button is activated: implemented by extenders, default if false.
	* @public
	 */
	supports: function(type) { return false; },

	/**
	* @function
	* @desc Defines the clickHandler, which is the meat of the button's functionality: implemented by extenders.
	* @param type the type
	* @param id the invoker's id
	* @param button the button object itself
	* @public
	 */
	clickHandler: function(type, id, button) {},

	/**
	 * @function
	 * @desc Setup button: implemented by extenders.
	 *
	 * @param type the object type
	 * @param editor the editor
	 * @param button the button
	 * @public
	 */
	setupButton: function(type, editor, button) {},

	/**
	 * @function
	 * @desc Returns the title: implemented by extenders.
	 * @param editor the editor, parent of the button
	 * @param obj the button object itself
	 * @returns The button's title.
	 * @public
	 */
	getTitle: function(editor, obj) { return ''; },

	/**
	 * @function
	 * @desc Returns the tooltip: implemented by extenders.
	 * @param editor the editor, parent of the button
	 * @param obj the button object itself
	 * @returns The tooltip
	 * @public
	 */
	getTooltip: function(editor, obj) { return ''; },

	/**
	 * @function
	 * @desc Returns the description: implemented by extenders.
	 * @param editor the editor, parent of the button
	 * @param obj the button object itself
	 * @returns The Button description
	 * @public
	 */
	getDescription: function(editor, obj) { return ''; },

	/**
	 * @function
	 * @desc Returns the button's CSS class: implemented by extenders.
	 * @returns The button CSS class
	 * @public
	 */
	getButtonClass: function(editor, obj) { return ''; },

	/**
	 * @function
	 * @desc Returns the button's icon CSS class.
	 * @param editor the editor, parent of the button
	 * @param obj the button object itself
	 * @public
	 */
	getIconClass: function(editor, obj) { return ''; },

	/**
	 * @function
	 * @desc Returns the button's JS class.
	 * @public
	 */
	getButtonJSClass: function() { return null; },

	/**
	 * @function
	 * @desc Returns the additional configuration for the button.
	 *
	 * @param type the object type
	 * @param editor the editor
	 * @public
	 */
	getAdditionalConfig: function(type, editor) { return null; }
});

/**
 * Set up the internal button section and our requirements
 */
fui.provide("fui.editor.button.internal");

fui.editor.button.internal = {
	// Buttons
	buttons: {},

	/**
	 * Add a button
	 *
	 * @param button the button to add
	 */
	add: function(button) {
		this.buttons[button.id] = button;
	},

	/**
	 * Remove a button
	 *
	 * @param id the id of the button to remove
	 */
	remove: function(id) {
		delete this.buttons[id];
	},

	/**
	 * Find an active button by id
	 *
	 * @param id the id of the button to find
	 */
	find: function(id) {
		return this.buttons[id];
	}
};
