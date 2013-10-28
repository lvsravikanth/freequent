/**
 * Set up the context section and our requirements
 */
fui.provide("fui.context");

fui.require("fui.locale");

fui.context = {
	ROOT_TYPE_KEY: "rootType",

	ROOT_ID_KEY: "rootId",

	ROOT_NAME_KEY: "rootName",

	ROOT_XML_NAME_KEY: "rootXmlName",

	LOCALIZATION_LOCALE_KEY: "locale",

	LOCALIZATION_TIMEZONE_KEY: "timezone",

	FORMAT_KEY: "format",

	/**
	 * Build the context into the request data.
	 * 
	 * @param requestData the request data
	 */
	build: function(requestData) {
		var ctx = requestData.context || {};
		return fui.combine(ctx, this.getContext());
	},

	/**
	 * Set the context root values.
	 * 
	 * @param type the root type
	 * @param id the root id
	 * @param name the root name
	 * @param xmlName the root XML name
	 */
	setRoot: function(type, id, name, xmlName) {
		// Save this
		var timezone = this.internal.global[this.LOCALIZATION_TIMEZONE_KEY];

		// Reset context completely
		this.internal.global = {};

		this.internal.global[this.ROOT_TYPE_KEY] = type;
		this.internal.global[this.ROOT_ID_KEY] = id;
		this.internal.global[this.ROOT_NAME_KEY] = fui.html.unescape(name);
		this.internal.global[this.ROOT_XML_NAME_KEY] = fui.html.unescape(xmlName);

		// Restore
		this.internal.global[this.LOCALIZATION_TIMEZONE_KEY] = timezone;
	},

	/**
	 * Set the localization context.
	 * 
	 * @param locale the locale
	 * @param timezone the timezone
	 */
	setLocalization: function(locale, timezone)    {		
		// timezone should be of the form {id, offset, usesDaylight}
		this.internal.global[this.LOCALIZATION_TIMEZONE_KEY] = timezone;

		fui.locale.set(locale);
	},

	/**
	 * Set the formats for this context.
	 *
	 * @param locale the locale
	 * @param timezone the timezone
	 */
	setFormat: function(date, time, datetime)    {
		var format = {};
		format.date = date ? fui.date.convertJavaDateFormat(date) : "";
		format.time = time || "";
		format.datetime = datetime || "";
		this.internal.global[this.FORMAT_KEY] = format;
	},

	/**
	 * Returns the root context.
	 */
	getRoot: function() {
		var root = {};
		root[this.ROOT_TYPE_KEY] = this.internal.global[this.ROOT_TYPE_KEY];
		root[this.ROOT_ID_KEY] = this.internal.global[this.ROOT_ID_KEY];
		root[this.ROOT_NAME_KEY] = this.internal.global[this.ROOT_NAME_KEY];
		root[this.ROOT_XML_NAME_KEY] = this.internal.global[this.ROOT_XML_NAME_KEY];

		return root;
	},

	/**
	 * Sets the context.
	 * 
	 * @param ctx the context to set
	 */
	set: function(ctx) {
		if ( !ctx ) {
			return;
		}

		if ( fui.log.isDebug() ) {
			if ( ctx[this.ROOT_TYPE_KEY] ) {
				fui.log.debug("Cloberring context root type");
			}

			if ( ctx[this.ROOT_ID_KEY] ) {
				fui.log.debug("Cloberring context root id");
			}

			if ( ctx[this.ROOT_NAME_KEY] ) {
				fui.log.debug("Cloberring context root name");
			}

			if ( ctx[this.ROOT_XML_NAME_KEY] ) {
				fui.log.debug("Cloberring context root xmlName");
			}
		}

		// Manage locale
		this.internal.manageLocale(ctx);
		
		// Combine the context
		fui.combine(this.internal.global, ctx);
	},

	/**
	 * Restore the context.
	 * 
	 * @param ctx the context to restore
	 */
	restore: function(ctx) {
		if ( fui.log.isDebug() ) { fui.log.debug("Restoring context"); }

		// Manage locale
		this.internal.manageLocale(ctx);
		
		this.internal.global = ctx;
	},

	/**
	 * Returns the context.
	 */
	getContext: function() {
		// return a copy
		var ctx = {};
		ctx = fui.combine(ctx, this.internal.global);
		ctx[this.LOCALIZATION_LOCALE_KEY] = fui.locale.get();
		return ctx;
	},

	/**
	 * Returns the context prepared for a request.
	 *
	 * @param ctx the context to prepare or null for active context
	 */
	getRequestContext: function(ctx) {
		ctx = ctx || this.getContext();

		var result = {};

		// Add context
		for ( var key in ctx ) {
			result[".fui.context." + key] = ctx[key];
		}

		return result;
	},

	/**
	 * Returns the context prepared for a URI string.
	 *
	 * @param ctx the context to prepare or null for active context
	 */
	getURIContext: function(ctx) {
		ctx = ctx || this.getContext();

		var result = [];

		// Add context
		for ( var key in ctx ) {
			result.push(".fui.context." + key + "=" + encodeURIComponent(ctx[key]));
		}

		return result.join("&");
		
	}
};

/**
 * Set up the context internal section and our requirements
 */
fui.provide("fui.context.internal");

fui.context.internal = {
	global: {},
	
	/**
	 * Manage the locale in a context.
	 * 
	 * @param ctx the context
	 */
	manageLocale: function(ctx) {
		// Remove locale from context
		var locale = ctx[this.LOCALIZATION_LOCALE_KEY];
		ctx[this.LOCALIZATION_LOCALE_KEY] = undefined;

		// Set it
		if ( locale ) {
			fui.locale.set(locale);
		}				
	}
};
