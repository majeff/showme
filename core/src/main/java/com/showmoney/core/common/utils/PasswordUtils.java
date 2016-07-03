/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.common.utils.PasswordUtils
   Module Description   :

   Date Created      : 2010/2/10
   Original Author   : jeff.ma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package com.showmoney.core.common.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * @author jeff.ma
 * 
 */
public class PasswordUtils {

	private static String defaultKey = "qwert12345";
	// Salt
	private static byte[] salt = { (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c, (byte) 0x7e, (byte) 0xc8,
			(byte) 0xee, (byte) 0x99 };
	private static PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 20);;

	// Iteration count
	int count = 20;

	public static String encodePassword(String value, String key) {
		String result = "";
		if (key == null) {
			key = defaultKey;
		}
		try {
			PBEKeySpec pbeKeySpec = new PBEKeySpec(key.toCharArray());
			SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

			// Create PBE Cipher
			Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

			// Initialize PBE Cipher with key and parameters
			pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

			// Our cleartext
			byte[] cleartext = value.getBytes();

			// Encrypt the cleartext
			byte[] ciphertext = pbeCipher.doFinal(cleartext);
			result = BytesUtils.bytesToHex(ciphertext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// logger.debug("value:[{}], result:[{}]", value, result);
		return result;
	}

	public static String decodePassword(String value, String key) {
		String result = "";
		if (key == null) {
			key = defaultKey;
		}
		try {
			PBEKeySpec pbeKeySpec = new PBEKeySpec(key.toCharArray());
			SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

			// Create PBE Cipher
			Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

			// Initialize PBE Cipher with key and parameters
			pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);

			// Our cleartext
			byte[] cleartext = BytesUtils.hexToBytes(value);

			// Encrypt the cleartext
			byte[] ciphertext = pbeCipher.doFinal(cleartext);
			result = new String(ciphertext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// logger.debug("value:[{}], result:[{}]", value, result);
		return result;
	}

	public static void main(String[] arg) {
		String result = null;
		if (arg != null && arg.length >= 2) {
			if ("enc".equals(arg[0])) {
				result = PasswordUtils.encodePassword(arg[1], arg.length >= 3 ? arg[2] : null);
			} else {
				result = PasswordUtils.decodePassword(arg[1], arg.length >= 3 ? arg[2] : null);
			}
			System.out.println("result:" + result); // NOPMD
		} else {
			System.out.println("java com.showmoney.core.common.utils.PasswordUtils [enc|dec] [text] [key]"); // NOPMD
		}
	}

}
