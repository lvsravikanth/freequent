/**
 * Set up the demo
 */
fui.provide("fui.ui.demo");

fui.require("fui.ui.content");
fui.require("fui.ui.type");

fui.ui.demo = {
	/**
	 * The action key
	 */
	ACTION_KEY: "demo",

    /**
	 * Methods
	 */

	/**
	 * The action key for Archives List
	 */
	DEMO_SIMPLE_JSON: "simpleJson",

    TEST_EXCEPTION: "testexception",

    getSimpleJson: function(requestData){
		if ( fui.log.isDebug() ) { fui.log.debug("Reading simpleJson method data " ); }

		requestData = requestData || {};
		requestData.content = requestData.content || {};
		var c = fui.ui.content;
		c.internal.sendAPI(this, this.DEMO_SIMPLE_JSON, requestData);
	},

    testSimpleJson: function() {
        var requestData = {};
        requestData.sync = true;
        requestData.handler = function(data) {
            fui.msg.alert("json string", fui.json.stringify(data));
        };
        fui.ui.demo.getSimpleJson(requestData);
    },

    testException: function() {
       var requestData = {};
       requestData.handler = function(data) {
            fui.msg.alert("json string", fui.json.stringify(data));
        };
        var c = fui.ui.content;
		c.internal.sendAPI(this, this.TEST_EXCEPTION, requestData);
    },

    testauthorization: function() {
       var requestData = {};
       requestData.handler = function(data) {
            fui.msg.alert("json string:", fui.json.stringify(data));
        };
        var c = fui.ui.content;
		c.internal.sendAPI(this, "testauthorization", requestData);
    },

    testtemplate: function(handler) {
       var requestData = {};
       requestData.handler = handler || function(data) {
            fui.msg.alert("json string", fui.json.stringify(data));
        };
        var c = fui.ui.content;
		c.internal.sendAPI(this, "demotemplate", requestData);
    }
};

/**
 * aDEMO Data object
 */
fui.ui.DemoData = function() {
	if ( (arguments.length == 1) && (typeof arguments[0] == "object") ) {
		fui.combine(this, arguments[0]);
	}
};

// Register our creator
fui.creator.register(fui.ui.type.DEMO, function(obj){ return new fui.ui.DemoData(obj); });