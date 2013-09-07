package com.scalar.freequent.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

import com.scalar.core.ScalarException;
import com.scalar.core.ScalarLoggedException;
import com.scalar.core.ScalarRuntimeException;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.core.util.MsgObject;
import com.scalar.freequent.l10n.FrameworkResource;

import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;

/**
 * User: Sujan Kumar Suppala
 * Date: Sep 7, 2013
 * Time: 12:57:19 PM
 */
public class MessageDigestUtils {
    protected static final Log logger = LogFactory.getLog(MessageDigestUtils.class);
    private static final String MAC_ALGORITHM = "HmacMD5";
    private static SecretKey s_secretKey = null;

    private MessageDigestUtils() {
    }

    public static void main (String[] args) throws Exception {
        //String location = System.getProperty ("location")
        //generateKeyFile ("dir", "chiptrack");
    }

    public static byte[] getMAC (String plainText) throws ScalarException {
        return getMAC (plainText, null);
    }

    public static byte[] getMAC (String plainText, String secretKeyFile) throws ScalarException {

        if ((plainText == null) || (plainText.trim().length() == 0)) {
            throw ScalarLoggedException.create (MsgObjectUtil.getMsgObject("Text passed to the method getMAC() is invalid"), null);
        }

        InputStream instream = null;
        byte[] result = null;
        SecretKey secretKey = null;

        try {
            if (secretKeyFile == null) {
                if (s_secretKey == null) {
                    String keyFile = Global.getMessageDigestSecretKeyFile();
                    instream = MessageDigestUtils.class.getResourceAsStream (keyFile);
                    if (instream == null) {
                        MsgObject msgObject = MsgObjectUtil.getMsgObject(FrameworkResource.BASE_NAME, FrameworkResource.SECRETKEY_FILE_NOT_CONFIGURED);
                        throw ScalarRuntimeException.create(msgObject, null);
                    }
                    ObjectInputStream p = new ObjectInputStream (instream);
                    s_secretKey = (SecretKey) p.readObject();
                    try { instream.close(); } catch (IOException ignore) {}
                    instream = null;
                }

                secretKey = s_secretKey;
            }
            else {

                instream = new FileInputStream(secretKeyFile);
                ObjectInputStream p = new ObjectInputStream (instream);
                secretKey = (SecretKey) p.readObject();
                instream.close();
                instream = null;
            }

            Mac mac = Mac.getInstance (MAC_ALGORITHM);
            mac.init (secretKey);

            result = mac.doFinal (plainText.getBytes());
        } catch (ScalarRuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw ScalarLoggedException.create (MsgObjectUtil.getMsgObject("Could not calculate message digest for \"" + plainText + "\""), e);
        }
        finally {
            if (instream != null) {
                try { instream.close(); } catch (IOException ignore) {}
            }
        }

        return result;
    }


    public static boolean verify (String inputValStr, byte[] existingVal) {

        if (inputValStr == null) return false;
        if (existingVal == null) return false;

        byte[] inputVal = null;

        boolean success = false;
        try {
            inputVal = getMAC (inputValStr);
            success = true;
        }
        catch (ScalarException se) {
            System.out.println ("Exception while verifying message digest.. " + se.getMessage());
        }

        if (success = false) {
            // Encountered exception
            return false;
        }

        if (inputVal.length != existingVal.length) return false;

        for (int i=0; i<inputVal.length; i++) {
            //System.out.println ("--> " + inputVal[i] + " -- " + existingVal[i]);
            if (inputVal[i] != existingVal[i]) {
                return false;
            }
        }

        return true;
    }


    public static String generateKeyFile (String dirLocation, String fileName) throws ScalarException {

        if (fileName == null) {
            throw new IllegalArgumentException ("File name is null");
        }
        if (dirLocation == null) {
            throw new IllegalArgumentException ("Directory location is null");
        }

        if (dirLocation.endsWith ("/") || dirLocation.endsWith ("\\")) {
            dirLocation = dirLocation.substring(0, dirLocation.length()-1);
        }

        File secretKeyFileName = null;
        FileOutputStream out = null;

        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance (MAC_ALGORITHM);
            SecretKey secretKey = keyGenerator.generateKey();

            secretKeyFileName = new File(dirLocation + "/chiptrac_" + fileName + ".key");
            out = new FileOutputStream(secretKeyFileName, false);
            ObjectOutputStream objectOutStream = new ObjectOutputStream (out);
            objectOutStream.writeObject (secretKey);
            objectOutStream.flush();
        }
        catch (Exception e) {
            throw ScalarLoggedException.create(MsgObjectUtil.getMsgObject(FrameworkResource.BASE_NAME, FrameworkResource.UNABLE_TO_GENERATE_SECRETKEY_FILE), e);
        }
        finally {
            if (out != null) {
                try { out.close(); } catch (IOException ignore) {}
            }
        }

        return secretKeyFileName.getAbsolutePath();
    }
}
