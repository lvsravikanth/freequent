/**
 * Logging. This is not enabled by default. To enable, include "logging.js" before "vui.js"
 */
if ( typeof this.fuiLog == "undefined" ) {
	this.fuiLog = function() {
		this.isDebug = function() { return false; };
		this.isInfo = function() { return false; };
		this.isWarning = function() { return false; };
		this.isError = function() { return false; };
		this.isCritical = function() { return false; };

		this.debug = function() { return null; };
		this.info = function() { return null; };
		this.warning = function() { return null; };
		this.error = function() { return null; };
		this.critical = function() { return null; };
	};
}

var fui = {
    query: (typeof fuiConfig != "undefined") && fuiConfig.query ? fuiConfig.query : ((typeof this.jQuery != "undefined") ? jQuery : undefined),

    log: new fuiLog(),
    
    // Temporary area for throw away functions
	tmp: {},

	/**
	 * Secure eval
	 */
	secure_eval: function(data, noWrap) {
		// Remove JS security. If data doesn't match the required format it will fail which is good.
		data = data.substring(2, data.length - 2);

		try {
			if ( noWrap ) {
				return eval(data);
			} else {
				return eval('(' + data + ')');
			}
		} catch ( e ) {
			if ( fui.log.isError() ) {
				fui.log.error("Unable to eval data", e);
			}
		}
	},

    global: function() {
		return window;
	},

	provide: function(name) {
	    var parts = name.split(/\./);
	    if(parts[parts.length-1]=="*"){
	        parts.pop();
	    }

	    var obj = window;
	    for (var i = 0; i < parts.length; ++i ) {
	    	if ( typeof obj[parts[i]] == "undefined" ) {
	    		obj[parts[i]] = {};
	    	}

	    	obj = obj[parts[i]];
	    }
	},

	require: function(name) {
		return;
	},

	exists: function(pkg, scope){
		scope = scope || window;

		var parts = pkg.split(/\./);

		for (var i = 0; i < parts.length; ++i ) {
			if ( typeof scope[parts[i]] == "undefined" ) {
				return false;
			}
			scope = scope[parts[i]];
		}
		return true;
	},

	byId: function(id) {
		if ( !fui.isString(id) ) { return id; }

		return fui.query("#" + id)[0];
	},

	combine: function(obj, properties) {
		return fui.query.extend(obj, properties);
	},

	isIE: function() {
		return fui.query.browser.msie;
	},

	ready: function(func) {
		fui.query(function() {
			try {
				func();
			} catch (e) {
				if ( fui.log.isError() ) {
					fui.log.error("Unable to run ready function", e);
				}				
			}
		});
	},
	
	subscribe: function(eventName, handler) {
		var b = fui.query("body");
		if ( b.length > 0 ) {
			b.bind(eventName, handler);
		} else {
			fui.query(function() {fui.query("body").bind(eventName, handler);});
		}
	},

	unsubscribe: function(eventName, handler) {
		var b = fui.query("body");
		if ( b.length > 0 ) {
			b.unbind(eventName, handler);
		} else {
			fui.query(function() {fui.query("body").unbind(eventName, handler);});
		}
	},

	publish: function(eventName, data) {
		var event = fui.query.Event(eventName);
		event.preventDefault();

		var b = fui.query("body");
		if ( b.length > 0 ) {
			b.trigger(event, data);
		} else {
			fui.query(function() {fui.query("body").trigger(event, data);});
		}
	},

	clearSubscriptions: function(eventName) {
		var b = fui.query("body");
		if ( b.length > 0 ) {
			b.unbind(eventName);
		} else {
			fui.query(function() {fui.query("body").unbind(eventName);});
		}
	},

	getAttribute: function(elem, attrName) {
		var n = fui.isString(elem) ? '#' + elem : elem;
		return fui.query(n).attr(attrName);
	},

	trim: function(str) {
		return fui.query.trim(str);
	},

	addClass: function(elem, classNames) {
		var n = fui.isString(elem) ? '#' + elem : elem;
		fui.query(n).addClass(classNames);
	},

	removeClass: function(elem, classNames) {
		var n = fui.isString(elem) ? '#' + elem : elem;
		fui.query(n).removeClass(classNames);
	},

    inherit: function(subclass, superclass) {
	    subclass.prototype = new superclass();
	    subclass.prototype.constructor = subclass;
	    subclass.superclass = superclass.prototype;
	},
    
    extend: function(obj, extensions) {
		fui.combine(obj.prototype, extensions);
		return obj;
	},

    isObject: function(obj) {
		return ( typeof obj == "object" || obj instanceof Object);
	},

	isString: function(obj) {
		return ( typeof obj == "string" || obj instanceof String);
	},

	isNumber: function(obj) {
		return ( typeof obj == "number" || obj instanceof Number);
	},

	isFunction: function(obj) {
		return ( typeof obj == "function" || obj instanceof Function);
	},

	isArray: function(obj) {
		return ( typeof obj == "array" || obj instanceof Array);
	},

	setAttribute: function(elem, attrName, value) {
		var node = fui.byId(elem);
		node.setAttribute(attrName, value);
	},

	openWindow: function(url, target, features, width, height, unique) {
		// Make sure target is blank or browser gets confused
		if ( !target ) {
			target = '';
		}

		if ( unique ) {
			target += Math.random();
			// remove . and - since IE barfs
			target = target.replace(/\.|-/g, '');
		}

		features = features || "";

		if ( width ) {
			features += ',width=' + width;
		}

		if ( height ) {
			features += ',height=' + height;
		}

		if (features.length > 0) {
			return window.open(url, target, features);
		} else {
			var w = window.open(url, target);
			// If popup blocker on, the w will be null.
			// Adding conditional to only focus when window is open.
			if(w){
				w.focus();
			}
			return w;
		}
	},

    ajax: function(options) {
		var data = {
			url: options.url,
			type: options.method,
			async: !options.sync,
			data: options.data,
			dataType: "text",
			cache: false,
			timeout: options.timeout,

			beforeSend: function(xhr) {
				if ( !options.headers ) {
					return;
				}

				for ( var h in options.headers ) {
					xhr.setRequestHeader(h, options.headers[h]);
				}
			},

			success: function(data, status) {
				if ( fui.isFunction(options.success) ) {
					options.success(data);
				}
			},

			error: function(xhr, status, error) {
				if ( fui.isFunction(options.error) ) {
					if ( !error ) {
						error = status;
					}
					options.error(xhr, error);
				}
			}
		}

		fui.query.ajax(data);
	},

	ajaxForm: function(options) {
		var data = {
			url: options.url,
			type: options.method,
			async: !options.sync,
			data: options.data,
			timeout: options.timeout,
			dataType: options.transport == "xml" ? "xml" : null,

			beforeSend: function(xhr) {
				if ( !options.headers ) {
					return;
				}

				for ( var h in options.headers ) {
					xhr.setRequestHeader(h, options.headers[h]);
				}
			},

			success: function(data, status, xhr) {
				if ( !fui.isFunction(options.success) ) {
					return;
				}

				if ( options.transport == "json" ) {
					// We know that the vquery form plugin returns the HTML document as the responseXML
					// So use that first to pull the data we want
					var fallback = true;
					if ( xhr.responseXML && xhr.responseXML.getElementsByTagName ) {
						var pre = xhr.responseXML.getElementsByTagName('pre')[0];
						if ( pre ) {
							data = pre.innerHTML;
							fallback = false;
						}
					}

					if ( fallback ) {
						// strip off <pre> </pre>
						var startIndex = Math.max(data.indexOf('/*{xapi'), 0);

						var endIndex = data.indexOf('"---end-marker---"}]}}}*/');
						if ( endIndex < 0 ) {
							endIndex = data.length;
						} else {
							endIndex += 25;
						}

						data = data.substring(startIndex, endIndex);
					}
				} else if ( options.transport == "xml" ) {
					//data = fui.xml.get(data); //todo
				}

				options.success(data);
			},

			error: function(xhr, status, error) {
				if ( fui.isFunction(options.error) ) {
					if ( !error ) {
						error = status;
					}
					options.error(xhr, error);
				}
			}
		}

		options.xhr = fui.query("#" + options.formId).ajaxSubmit(data);
	}
}