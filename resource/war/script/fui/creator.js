/**
 * Set up the creator section and our requirements
 */
fui.provide("fui.creator");

fui.creator = {
	/**
	 * Register a creator.
	 *
	 * @param type the type of the creator
	 * @param creator the creator function
	 */
	register: function(type, creator) {
		// Check creator cache
		var c = fui.creator;
		if ( c.internal.creators[type] ) {
			if ( fui.log.isDebug() ) { fui.log.debug("Registering same type again: " + type); }
		}

		// Register the creator
		c.internal.creators[type] = creator;
	},

	/**
	 * Create an object from a JSON object.
	 *
	 * @param obj the JSON object
	 *
	 * @return an object or null
	 */
	create: function(obj) {
		// Make sure obj is something
		if ( !obj ) {
			if ( fui.log.isError() ) { fui.log.error("Attempt to create null object"); }
			return null;
		}

		return this.createByType(obj.type, obj);
	},

	/**
	 * Create an object of a specific type from a JSON object
	 *
	 * @param type the object type
	 * @param obj the JSON object
	 *
	 * @return an object or null
	 */
	createByType: function(type, obj) {
		// Make sure type is something
		if ( !type ) {
			if ( fui.log.isError() ) { fui.log.error("No type specified for object: " + obj); }
			return obj;
		}

		// Make sure obj is something
		if ( !obj ) {
			obj = {};
		}

		var creator = fui.creator.internal.creators[type];
		if ( !creator ) {
			if ( fui.log.isError() ) { fui.log.error("Unable to find creator for type: " + type); }
			return obj;
		}

		return creator(obj);
	}
};

/**
 * Set up the private creator section and our requirements
 */
fui.provide("fui.creator.internal");

fui.creator.internal = {
	/**
	 * Creators cache
	 */
	creators: {}
};
