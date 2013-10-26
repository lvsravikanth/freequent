<h1>Demo Jsp</h1>
<%@ page import="com.scalar.core.ContextUtil" %>
<%
    String context = ContextUtil.getContextPath(request);
%>
<!doctype html>

<html lang="en">
<head>
  <meta charset="utf-8" />
  <title>jQuery UI Autocomplete - Combobox</title>
<script type="text/javascript" src="<%=context%>/script/fui/widgets.js" ></script>
  <style type="text/css">
	.custom-combobox {
    position: relative;
    display: inline-block;
  }
 .custom-combobox-toggle {
    position: absolute;
    top: 0;
    bottom: 0;
    margin-left: -1px;
    padding: 0;
    /* support: IE7 */
    *height: 1.7em;
    *top: 0.1em;
  }
  .custom-combobox-input {
    margin: 0;
    padding: 0.3em;
  }
  </style>
<c:import url="/common/head.jsp" context="<%=context%>"/>
<script type="text/javascript ">
	//var requestInfo = fui.transport.JSON.getRequest('<%=request%>');
	/*document.write("requestInfo" + getData());
	function getData(requestData) {

		var ACTION_KEY = 'demo';
		var Method = 'simpleJson'; 

		requestData = requestData || {};

		requestData.ajaxMethod = 'POST';

		requestData.actionKey = ACTION_KEY;
		requestData.method = Method;

		requestData.content = requestData.content || {};


		var handler = requestData.handler;
		requestData.handler = function(data) {
			data = fui.secure_eval(data);

			if ( handler ) {                                               				handler(data);
			}
		};


		var request = fui.request.build('admin', 'admin' , requestData, undefined);
		fui.io.api(request);
		debugger;
	}*/
		

	fui.query(function() {
			var requestData = {};
			requestData.sync = true;
			requestData.method = 'simpleJson';
			fui.query("#combobox").ui.combobox({request : requestData});
	});


</script>

<!--<select id="emp" onchange="ajaxCombo()">
	<option>Choice</option>
	<option>emp</option>
</select>-->
</head>
<body>


<div class="ui-widget">
  <label>Your preferred programming language: </label>
  <select id="combobox">
    <option value="">Select one...</option>
    <option value="ActionScript">ActionScript</option>
  </select>
</div>


</body>
</html>=======
<div>
<h1>Demo Jsp</h1>

<input type="button" value="Test simpleJson" onClick="javascript: fui.ui.demo.testSimpleJson();">
<button id="testexception" onclick="javascript: fui.ui.demo.testException();">Test Exception</button>
<button id="testauthorization" onclick="javascript: fui.ui.demo.testauthorization();">Test Authorization</button>
<button id="testtemplate">Test Template</button>
<div id="template-container">Click 'Test Template' button to see the template</div>
	<div id="dialog" title="Dialog Title">I'm a dialog</div>
</div>
<div id="testmask">
	click mask to mask me.
	<div style="heigh:100px">what's up?</div>
	click unmask to unmask me.
</div>
<button id="maskbutton">Mask</button>
<button id="unmaskbutton">UnMask</button>
<hr/>
<form action="#" id="validatortestform">
	input required: <input type="text" id="userid"/>

</form><button id="validatetestbtn">Validate</button>


<script type="text/javascript">
    fui.ready(function() {
        fui.query('#testexception').button();
        fui.query('#testauthorization').button();
        fui.query('#testtemplate').button()
        .click(function( event ) {
            var handler= function(data) {
                fui.html.set('template-container', data);
            };
            fui.ui.demo.testtemplate(handler);
      });

		fui.query("#maskbutton").button()
		.click(function(){
			fui.query("#testmask").mask("mask label");
		});
		fui.query("#unmaskbutton").button()
		.click(function(){
			fui.query("#testmask").unmask();
		});
	fui.query('#opener').button()
		.click(function() {
			//show the dialog
	  		fui.query( "#dialog" ).dialog( "open" );
		});
	//initialize
		fui.query( "#dialog" ).dialog(
			{
				autoOpen: false,
				modal: true,
				height: 'auto',
				width: 650,
				close: function( event, ui ) {alert('in close');},
				buttons: {
				"Create an account": function() {
				  alert ('in create an account');
				},
				Cancel: function() {
				  fui.query( this ).dialog( "close" );
				}
			  }
			}
		);

		var validator = fui.query('#validatortestform').validate(
			{
				rules: {
						userid: "required"
						}
			});

		fui.query('#validatetestbtn').button()
		.click(function(){
			fui.query('#validatortestform').valid();
		});
	}
            );
</script>
