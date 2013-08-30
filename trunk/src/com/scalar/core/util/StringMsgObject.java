package com.scalar.core.util;

import java.util.Locale;

/**
 * User: ssuppala
 * Date: Aug 30, 2013
 * Time: 2:58:32 PM
 */
/**
  A class which provides the ability to have an untranslated string
  fit into a MsgObject.  Since the monitors are utility classes, we can't
  force everyone to switch over to use MsgObjects at the same time.  By
  having this wrapper object, we can have both String and MsgObject
  constructors on the outside, but use MsgObjects on the inside.
*/
public class StringMsgObject extends MsgObject {

/**
  Create a StringMsgObject which contains a plain text message.  This
  text will be returned from the localize methods. It will not be translated.
*/

public StringMsgObject(String message)
{
	super(null,"custom.msg",null, MsgObject.class.getClassLoader() );

	// Replace the default message, and then stomp the bundle name so that
	// it can't cause us any more trouble.
	mDefaultMsg = message;
	mBundleName = "StringMsgObject bundle";
}

public String localize()
{
   return(mDefaultMsg);
}

public String localize(Locale locale)
{
   return(mDefaultMsg);
}

}