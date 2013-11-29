/**
 * Set up the row action section and our requirements
 */
fui.provide("fui.grid.rowaction");
fui.provide("fui.grid.RowAction");

fui.grid.rowaction = {
	/**
	 * Registers a row action.
	 *
	 * @param rowaction the row action to register
	 */
	register : function(rowaction) {
		this.internal.add(rowaction);
	},

	/**
	 * Unregisters a row action by id.
	 *
	 * @param id the row action id
	 */
	unregister : function(id) {
		this.internal.remove(id);
	},

	/**
	 * Finds a row action by id.
	 *
	 * @param id the row action id
	 */
	find : function(id) {
		this.internal.find(id);
	},

	/**
	 * Renderer for row actions.
	 */
	renderer: function(ui) {
		var rowData = ui.rowData;
		// Find record type or return blank
		var type = rowData.record.objectType;
		if ( !type ) {
			return '';
		}

		var objectTypeXmlName = null;//record.data.objectTypeXmlName;
		
		var rowObject = {
			id : rowData.id || rowData.userid,
			name: fui.html.escape(rowData.name),
			type: type,
			extraOne: '',
			extraTwo:'',
			extraThree: ''
		};

		var template;

		template = fui.grid.rowaction.internal.getTemplate(type, objectTypeXmlName);

		return template ? template(rowObject) : '';
	}
};

/**
 * Row action object
 */
fui.grid.RowAction = function() {
	if ( (arguments.length == 1) && (typeof arguments[0] == "object") ) {
		fui.combine(this, arguments[0]);
	}
};

fui.extend(fui.grid.RowAction, {
	id : null,
	rowactionClass : '',
	index : 100,
	supports: function(type) { return false; },
	clickHandler : function(obj) {},
	getTitle: function() { return ''; },
	getDescription: function() { return ''; }
});

/**
 * Set up the internal row action section and our requirements
 */
fui.provide("fui.grid.rowaction.internal");

fui.grid.rowaction.internal = {
	// Row actions
	rowactions: {},

	// Template cache per type
	templateCache: {},

	// Template cache per objectTypeXMLName
	templateSpecificTypeCache: {},

	// Row actions template
	ROW_ACTIONS : '<ul class="fui-grid-actions">{{{rowactions}}}</ul>',

	// Row action template
	ROW_ACTION : '<li class="fui-grid-action">' +
				'<a class="{{cls}}" onclick="javascript:fui.grid.rowaction.internal.clickHandler(\'{{rowaction}}\', ' +
				'{id:\'{{id}}\',name:\'{{name}}\', type:\'{{type}}\', extraOne:\'{{extraOne}}\', extraTwo:\'{{extraTwo}}\', extraThree:\'{{extraThree}}\'});" title="{{title}}">' +
				'<span>{{title}}</span></a></li>',


	/**
	 * Add a row action
	 *
	 * @param rowaction the row action to add
	 */
	add: function(rowaction) {
		this.rowactions[rowaction.id] = rowaction;
	},

	/**
	 * Remove a row action
	 *
	 * @param id the id of the row actionto remove
	 */
	remove: function(id) {
		delete this.rowactions[id];
	},

	/**
	 * Find an active row action by id
	 *
	 * @param id the id of the row action to find
	 */
	find: function(id) {
		return this.rowactions[id];
	},

	getTemplate: function(type, objectTypeXmlName) {
		var rai = fui.grid.rowaction.internal;

		// Check cache
		var template;

		if(objectTypeXmlName && (type!==objectTypeXmlName)){
			template = rai.templateSpecificTypeCache[type] ? rai.templateSpecificTypeCache[type][objectTypeXmlName] : null;
		} else {
			template = rai.templateCache[type];
		}

		if ( template ) {
			return template;
		}

		// Build row actions for the type
		var rowactions = [];
		for ( var id in rai.rowactions) {
			var rowaction = rai.rowactions[id];
			if ( rowaction.supports(type, objectTypeXmlName) ) {
				rowactions.push(rowaction);
			}
		}

		// Anything found?
		if ( rowactions.length === 0 ) {
			template = '';
		} else {
			// Sort
			var indexSort = function(a, b) {
				return a.index - b.index;
			};
			rowactions.sort(indexSort);

			// Build row actions
			if ( typeof rai.ROW_ACTION === "string") {
				rai.ROW_ACTION = Handlebars.compile (rai.ROW_ACTION);
			}

			template = '';
			for ( var i = 0 ; i < rowactions.length ; ++i ) {
				var raconfig = {
					rowaction: rowactions[i].id,
					cls: rowactions[i].rowactionClass,
					title: rowactions[i].getTitle(),
					id: '{{id}}',
					name: '{{name}}',
					type: '{{type}}',
					extraOne: '{{extraOne}}',
					extraTwo: '{{extraTwo}}',
					extraThree: '{{extraThree}}'
				};

				template += rai.ROW_ACTION(raconfig);
			}
		}

		// The full template
		var rasconfig = {
			rowactions: template
		};

		if ( typeof rai.ROW_ACTIONS === "string") {
			rai.ROW_ACTIONS = Handlebars.compile (rai.ROW_ACTIONS);
		}

		template = rai.ROW_ACTIONS(rasconfig);

		// Finalize it
		template =  Handlebars.compile (template);
		if(objectTypeXmlName){
			rai.templateSpecificTypeCache[type] = rai.templateSpecificTypeCache[type] || {};
			rai.templateSpecificTypeCache[type][objectTypeXmlName] = template;
		} else {
			rai.templateCache[type] = template;
		}
		return template;
	},

	clickHandler: function(id, obj) {
		var rowaction = fui.grid.rowaction.internal.find(id);
		if ( !rowaction ) {
			return;
		}

		rowaction.clickHandler(obj);
	}
};
