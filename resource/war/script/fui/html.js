/**
 * Set up the html section and our requirements
 */
fui.provide("fui.html");

fui.require("fui.style");
fui.require("fui.script");

fui.html = {
	/**
	 * Sets the HTML for a node. Separates the HTML, executes scripts, inserts styles if flags are set.
	 *
	 * @param node the node
	 * @param html the html
	 * @param separate the separate flag
	 * @param execute the execute flag
	 * @param style the style flag
	 */
	set: function(node, html, separate, execute, style) {
		node = fui.byId(node);
        if (!node) {return;}

        var data;
		if ( separate ) {
			data = this.separate(html);
		} else {
			data = { html: html	};
		}

		this.setInner(node, data.html);

		if ( style && data.style ) {
			fui.style.insert(data.style);
		}

		if ( execute && data.script ) {
			eval(data.script);
		}
	},

	/**
	 * Separates the raw html into data: { html: "", script:"", style: "" }
	 *
	 * @param data the data to separate
	 */
	separate: function(html) {
		var result = {
			html: "",
			script: null,
			style: null
		};

		if ( !html ) {
			return result;
		}

		var splitter = function(startTag, endTag) {
			var found = false;
			var data = "";
			var endLength = endTag.length;
			while ( true ) {
				// find start
				var startIndex = html.indexOf(startTag, 0);
				if ( startIndex == -1 ) {
					break;
				}

				// find content
				var contentIndex = html.indexOf('>', startIndex);
				if ( contentIndex == -1 ) {
					position = startIndex + 1;
					break;
				}

				++contentIndex;

				// find end
				var endIndex = html.indexOf(endTag, contentIndex);
				if ( endIndex == -1 ) {
					position = contentIndex;
					break;
				}

				found = true;

				// extract content
				var content = html.substring(startIndex, endIndex + endLength);

				// fix content
				content = content.substring(contentIndex - startIndex, content.length - endLength);
				data += content;

				// fix html
				var pre = html.substring(0, startIndex);
				html = pre + html.substring(endIndex + endLength);
			}

			return found ? data : null;
		};

		// pull scripts
		result.script = splitter('<script', '</script>');

		// pull styles
		result.style = splitter('<style', '</style>');

		// The remains
		result.html = html;

		return result; // Object
	},

	/**
	 * Calculate absolute dimensions for an element.
	 *
	 * @param id the element id
	 * @param minWidth the minimum width
	 * @param minHeight the minimum height
	 */
	calculateAbsoluteDimensions: function(id, minWidth, minHeight) {
		var left = -1, top = -1, right = -1, bottom = -1, width, height;
		var overflow, childElement, childElOffset, wrapperOffset;
		var wrapperHeight, wrapperWidth;

		var wrapperNode = vQuery('#' +id);

		if (!wrapperNode || wrapperNode.length === 0) {
			return {left: 0, top: 0, right: minWidth, bottom: minHeight, width: minWidth, height: minHeight};
		}
		wrapperNode.children(':visible').each(function(index, el) {
			childElement = vQuery(el);
			overflow = childElement.css('overflow');
			childElement.css('overflow', 'hidden');
			childElOffset = childElement.offset();
			left = left == -1 ? childElOffset.left : Math.min(left, childElOffset.left);
			top = top == -1 ? childElOffset.top : Math.min(top, childElOffset.top);
			right = Math.max(right, childElOffset.left + childElement.outerWidth());
			bottom = Math.max(bottom, childElOffset.top + childElement.outerHeight());
			childElement.css('overflow', overflow);
		});

		// Use wrapper node to get offset, width and height in case we fail to fetch offsets for the child elements.
		wrapperOffset = wrapperNode.offset();
		overflow = wrapperNode.css('overflow');
		wrapperNode.css('overflow', 'hidden');
		wrapperWidth = wrapperNode.outerWidth();
		wrapperHeight = wrapperNode.outerHeight();
		wrapperNode.css('overflow', overflow);

		if ( left == -1 ) {
			left = wrapperOffset.left;
		}

		if ( top == -1 ) {
			top = wrapperOffset.top;
		}

		if ( right == -1 ) {
			right = left + wrapperWidth;
		}

		if ( bottom == -1 ) {
			bottom = top + wrapperHeight;
		}

		width = right - left;
		if ( minWidth && (minWidth > width) ) {
			width = minWidth;
			right = left + width;
		}

		height = bottom - top;
		if ( minHeight && (minHeight > height) ) {
			height = minHeight;
			bottom = top + height;
		}

		return {left: left, top: top, right: right, bottom: bottom, width: width, height: height};
	},

	/**
	 * Sets the inner HTML of the node and normalizes it.
	 *
	 * @param node the node
	 * @param html the html to set
	 */
	setInner: function(node, html) {
		node = fui.byId(node);
        if (!node) {return;}

        // Try to prevent IE leaks
		this.emptyNode(node);

		node.innerHTML = html;
		if ( node.normalize ) {
			node.normalize();
		}
	},

	/**
	 * Empties a node of all children.
	 *
	 * @param node the node to empty
	 */
	emptyNode: function(node) {
		node = fui.byId(node);

		while (node.firstChild) {
			node.removeChild(node.firstChild);
		}
	},

	/**
	 * A simple HTML escape.  Handles <, >, &, ", and '.
	 * Note:  Feel free to replace with a more robust version if so desired.
	 *
	 * @param text The text to process.
	 * @return The processed text.
	 */
	escape: function(text) {
		if (text && text.replace) {
			return text.replace(/&/gm, "&amp;").replace(/</gm, "&lt;").replace(/>/gm, "&gt;").replace(/"/gm, "&quot;").replace(/'/gm, "&#39;");
		} else {
			return text;
		}
	},

	/**
	 * Unescapes HTML entities in the given value.
	 *
	 * @param value The value to unescape.
	 * @return The unescaped value.
	 */
	unescape: function(value) {
		return value ? fui.query('<div/>').html(value).text() : value;
	},

	/**
	 * Updates the iframe with data given
	 * @param id
	 * @param data
	 * @param element
	 */
	setIFrame: function(id, data, element) {
		var iframe = fui.query('#' + id);
		if ( iframe.length != 1 ) {
			return;
		}

		element = element || 'html';
		iframe.contents().find(element).html('');
		iframe[0].contentDocument.open();
		iframe[0].contentDocument.write(data);
		iframe[0].contentDocument.close();
	},

	/**
	 * Modernizr impl for HTML5 Browser features
	 * support check.
	 */
	isEventSupported : (function() {

		var TAGNAMES = {
			'select': 'input', 'change': 'input',
			'submit': 'form', 'reset': 'form',
			'error': 'img', 'load': 'img', 'abort': 'img'
		};

		function isEventSupported( eventName, element ) {

			element = element || document.createElement(TAGNAMES[eventName] || 'div');
			eventName = 'on' + eventName;

			// When using `setAttribute`, IE skips "unload", WebKit skips "unload" and "resize", whereas `in` "catches" those
			var isSupported = eventName in element;

			if ( !isSupported ) {
				// If it has no `setAttribute` (i.e. doesn't implement Node interface), try generic element
				if ( !element.setAttribute ) {
					element = document.createElement('div');
				}
				if ( element.setAttribute && element.removeAttribute ) {
					element.setAttribute(eventName, '');
					isSupported = typeof element[eventName] == 'function';

					// If property was created, "remove it" (by setting value to `undefined`)
					if ( typeof element[eventName] != 'undefined' ) {
						element[eventName] = undefined;
					}
					element.removeAttribute(eventName);
				}
			}

			element = null;
			return isSupported;
		}
		return isEventSupported;
	})()
};
