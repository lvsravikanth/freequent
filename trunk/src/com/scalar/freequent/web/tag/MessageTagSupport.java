package com.scalar.freequent.web.tag;

import com.scalar.core.util.MsgObject;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.*;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.Iterator;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public abstract class MessageTagSupport extends TagSupport {
	
	public abstract List<MsgObject> getMessages();

	public JSONObject getMessageJSON() throws JSONException{
		JSONObject messagesJSON = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		Iterator<MsgObject> iterator =getMessages().iterator();

		while (iterator.hasNext()) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("msg", iterator.next().localize());
			jsonArray.put(jsonObject);
		}
		messagesJSON.put("messages", jsonArray);
		return messagesJSON;
	}
}
