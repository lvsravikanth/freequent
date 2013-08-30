package com.scalar.core.util;

/**
 * User: ssuppala
 * Date: Aug 30, 2013
 * Time: 3:08:09 PM
 */
public final class MsgObjectUtil {
	private MsgObjectUtil () {
		
	}

	public static MsgObject getMsgObject (String bundleName, String msgKey) {
		return new MsgObject ( bundleName, msgKey, null, null);
	}

	public static MsgObject getMsgObject (String bundleName, String msgKey, Object arg) {
		Object args[] = {
			arg
		};
		return new MsgObject ( bundleName, msgKey,args, null);
	}

	public static MsgObject getMsgObject (String bundleName, String msgKey, Object arg1, Object arg2) {
		Object args[] = {
			arg1,
			arg2
		};
		return new MsgObject ( bundleName, msgKey,args, null);
	}

	public static MsgObject getMsgObject (String bundleName, String msgKey, Object[] args) {
		return new MsgObject ( bundleName, msgKey,args, null);
	}

	public static MsgObject getMsgObject (String message) {
		return new StringMsgObject (message);
	}
}
