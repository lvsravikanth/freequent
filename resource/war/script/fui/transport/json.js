fui.provide("fui.transport.json");
fui.provide("fui.transport.JSON");

fui.require("fui.transport");
fui.require("fui.transport.Transport");

fui.require("fui.lang");
fui.require("fui.json");

fui.transport.JSON_TRANSPORT = "json";

fui.transport.json = {
	parse: function(data) {
		// Start of parse JSON
		if ( fui.log.isDebug() ) { fui.log.debug("Starting JSON parsing"); }
	
		var responses = {
			metadata: {},
			responses: [],
			errors: [],
			messages: []
		};
	
		// Fix content attributes. This is to avoid eval blowing up
		var contentString = "\"content\": \"";
		var endString = "\", \"end\": \"---end-marker---\"}";
	
		var savedContent = {};
		var savedIndex = 0;
	
		var fixedData = "";
		while ( true ) {
			var contentIndex = data.indexOf(contentString);
			if ( contentIndex < 0 ) {
				fixedData += data;
				break;
			}
	
			contentIndex += contentString.length;
	
			fixedData += data.substring(0, contentIndex);
			data = data.substring(contentIndex);
	
			var endIndex = data.indexOf(endString);
			var temp = data.substring(0, endIndex);
	
			savedContent[savedIndex] = temp;
	
			fixedData += savedIndex;
			fixedData += endString;
	
			++savedIndex;
	
			data = data.substring(endIndex + endString.length);
		}
	
		if ( fui.log.isDebug() ) { fui.log.debug("JSON for eval: " + fixedData); }
	
		var json = fui.secure_eval(fixedData);
		responses.metadata = json.xapi.metadata;
		
		var cdata = json.xapi.container.data;
		for ( var i = 0 ; i < cdata.length ; ++i ) {
			var obj = cdata[i];
			if ( obj.content ) {
				obj.content = savedContent[obj.content];
			}
	
			if ( obj.type == fui.transport.RESPONSE_TAG ) {
				responses.responses.push(obj);
			} else if ( obj.type == fui.transport.ERROR_TAG ) {
				responses.errors.push(obj);
			} else if ( obj.type == fui.transport.MESSAGE_TAG ) {
				responses.messages.push(obj);
			} else {
				if ( fui.log.isError() ) { fui.log.error("Unknown type found in response: " + obj.type); }
				continue;
			}
		}
	
		// Finish of parseJSON
		if ( fui.log.isDebug() ) { fui.log.debug("Finished JSON parsing"); }
	
		return responses;
	}
};

fui.transport.JSON = function() {
	// super
	fui.transport.Transport.apply(this, arguments);
};

fui.inherit(fui.transport.JSON, fui.transport.Transport);

fui.extend(fui.transport.JSON, {
	getRequestPrefix: function() {
		return "[";
	},

	getRequestSuffix: function() {
		return "]";
	},

	getRequestSeparator: function() {
		return ",";
	},

	getRequest: function(request) {
		var temp = {};

		if ( request.id ) {
			temp[fui.transport.REQUEST_ID_ATTRIBUTE] = request.id;
		}

		if ( request.actionKey && request.actionKey.length > 0 ) {
			temp[fui.transport.ACTION_KEY_ATTRIBUTE] = request.actionKey;
		}

		if ( request.method && request.method.length > 0 ) {
			temp[fui.transport.METHOD_ATTRIBUTE] = request.method;
		}

		if ( request.requestDataFormat && request.requestDataFormat.length > 0 ) {
			temp[fui.transport.REQUEST_DATA_FORMAT_ATTRIBUTE] = request.requestDataFormat;
		}

		if ( request.responseDataFormat && request.responseDataFormat.length > 0 ) {
			temp[fui.transport.RESPONSE_DATA_FORMAT_ATTRIBUTE] = request.responseDataFormat;
		}

		if ( request.modifierKey && request.modifierKey.length > 0 ) {
			temp[fui.transport.MODIFIER_KEY_ATTRIBUTE] = request.modifierKey;
		}

		temp = fui.combine(temp, request.getContext());

		var content = request.getContent();
		if ( !fui.lang.isEmptyObject(content) ) {
			temp[fui.transport.CONTENT_ATTRIBUTE] = content;
		}

		return fui.json.stringify(temp);
	},

	parse: function(data) {
		return fui.transport.json.parse(data);
	}
});

// Register our creator
fui.transport.register(fui.transport.JSON_TRANSPORT, function() { return new fui.transport.JSON(); });
