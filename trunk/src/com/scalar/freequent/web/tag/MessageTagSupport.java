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

/**
 * Created by IntelliJ IDEA.
 * User: snakka
 * Date: Sep 15, 2013
 * Time: 8:23:26 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MessageTagSupport extends TagSupport {
	
	public abstract List<MsgObject> getMessages();

	public JSONObject getMessageJSON() {
		JSONObject messagesJSON = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		try {
			Iterator<MsgObject> iterator =getMessages().iterator();

			while (iterator.hasNext()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("msg", iterator.next().localize());
				jsonArray.put(jsonObject);
			}
				messagesJSON.put("messages", jsonArray);
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return messagesJSON;
	}
}
