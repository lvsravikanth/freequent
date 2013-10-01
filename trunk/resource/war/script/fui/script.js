/**
 * Set up the script section and our requirements
 */
fui.provide("fui.script");

fui.require("fui.handler");
fui.require("fui.Handler");
fui.require("fui.request");
fui.require("fui.Request");
fui.require("fui.io");

fui.script = {
	// The action key
	ACTION_KEY: "script",

	/**
	 * Load a script into the current environment
	 *
	 * @param path the path to the script
	 */
	load: function(path) {
		if ( fui.log.isDebug() ) { fui.log.debug("Loading script: " + path); }

		// Check cache
		if ( fui.script.internal.cache[path] ) {
			if ( fui.log.isDebug() ) { fui.log.debug("Found script in cache"); }
			return;
		}

		if ( fui.log.isDebug() ) { fui.log.debug("Adding script to cache and document"); }

		// Didn't find it so add it
		fui.script.internal.cache[path] = true;

		var head = document.getElementsByTagName("head")["0"];
		var script = document.createElement("script");
		script.type = "text/javascript";
		script.language = "JavaScript";

		// Set src after DOM is updated
		script.src = path;

		head.appendChild(script);

		return script;
	},

	/**
	 * Inserts script code into the document.
	 *
	 * @param script the script code to insert
	 */
	insert: function(script) {
		if ( fui.global().execScript ) {
			return fui.global().execScript(script);
		} else {
			var head = document.getElementsByTagName("head")["0"];

			var scriptElement = document.createElement("script");
			scriptElement.type = "text/javascript";
			scriptElement.language = "JavaScript";

			scriptElement.appendChild(document.createTextNode(script));
			head.appendChild(scriptElement);

			return scriptElement;
		}
	},

	/**
	 * Make a REST request
	 * NOTE: re-uses a script element
	 *
	 * @param url the url for the REST request
	 */
	rest: function(url) {
		if ( fui.log.isDebug() ) { fui.log.debug("Making REST request: " + url); }

		var elementId = "vui-script-REST";

		var head = document.getElementsByTagName("head")["0"];

		var remoteScript = head.getElementById(elementId);
		if ( null !== remoteScript ) {
			head.removeChild(remoteScript);
		}

		var script = document.createElement("script");
		script.type = "text/javascript";
		script.language = "JavaScript";
		script.id = elementId;

		// Set src after DOM is updated
		setTimeout( function() { script.src = url; }, 0);

		head.appendChild(script);

		return script;
	},

	/**
	 * Eval a script from the server
	 *
	 * @param context the application context
	 * @param path the path to the script
	 */
	evalScript: function(context, scriptPath) {
		if ( fui.log.isDebug() ) { fui.log.debug("Eval-ing remote script for context: " + context + " path: " + scriptPath); }

		var content = { context: context, scriptPath: scriptPath };
		var handler = new fui.Handler({ response: function(response) { fui.secure_eval(response, true); } });
		var request = new fui.Request({ actionKey: fui.script.ACTION_KEY, content: content, handler: handler });
		fui.io.api(request);
	}
};

/**
 * Set up the private script section and our requirements
 */
fui.provide("fui.script.internal");

fui.script.internal = {
	// Script cache
	cache: {}
};
