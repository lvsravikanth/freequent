/**
 * Set up the style section and our requirements
 */
fui.provide("fui.style");

fui.style = {
	// The vui style attribute.
	VUI_STYLE_ATTRIBUTE : 'vuiStyle',

	/**
	 * Load a style into the current document
	 *
	 * @param path the path to the style
	 */
	load: function(path) {
		if ( fui.log.isDebug() ) { fui.log.debug("Loading style: " + path); }

		var head = document.getElementsByTagName("head")["0"];
		var style = document.createElement("link");
		style.type = "text/css";
		style.rel = "stylesheet";

		style.href = path;

		head.appendChild(style);

		return style;
	},

	/**
	 * Inserts style code into the document.
	 *
	 * @param style the style code to insert
	 * @param id an optional id for the style element
	 */
	insert: function(style, id) {
		var head = document.getElementsByTagName("head")["0"];

		var styleElement = document.createElement("style");
		styleElement.id = id || '';
		styleElement.type = "text/css";
		head.appendChild(styleElement);

		// IE?
		if ( styleElement.styleSheet ) {
			styleElement.styleSheet.cssText = style;
		} else {
			styleElement.appendChild(document.createTextNode(style));
		}

		return styleElement;
	},

	/**
	 * Replaces style code into the document.
	 *
	 * @param id the style element to replace
	 * @param style the style code to insert
	 */
	replace: function(id, style) {
		var node = fui.byId(id);
		if ( !id ) {
			return this.insert(style, id);
		}

		// IE?
		if ( node.styleSheet ) {
			node.styleSheet.cssText = style;
		} else {
			node.replaceChild(node.firstChild, document.createTextNode(style));
		}

		return node;
	},

	/**
	 * Removes style links that match the special vui tag.
	 *
	 * @param tag the vui tag value to match
	 */
	remove: function(tag) {
		if ( !tag ) {
			return;
		}

		var head = document.getElementsByTagName("head")["0"];
		var links = head.getElementsByTagName("link");
		for ( var i = links.length - 1 ; i > -1 ; --i ) {
			if ( fui.query(links[i]).attr(this.VUI_STYLE_ATTRIBUTE) == tag ) {
				head.removeChild(links[i]);
			}
		}
	}
};
