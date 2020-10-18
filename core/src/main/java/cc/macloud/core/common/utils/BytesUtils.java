package cc.macloud.core.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class BytesUtils {
	private static final char[] DIGIT_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
			'E', 'F' };

	/**
	 * A convenience method to convert an array of bytes to a String. We do this simply by converting each byte to two
	 * hexadecimal digits. Something like Base 64 encoding is more compact, but harder to encode.
	 */
	public static String bytesToHex(byte[] aBytes) {
		StringBuffer buffer = new StringBuffer(aBytes.length * 2);

		for (int index = 0; index < aBytes.length; index++) {
			byte b = aBytes[index];

			buffer.append(DIGIT_CHARS[(b & 0xf0) >> 4]);
			buffer.append(DIGIT_CHARS[(b & 0x0f)]);
		}

		return buffer.toString();
	}

	public static byte[] hexToBytes(String strHex) {
		int tmpHexLen = strHex.length();
		int cursorPos = 0;
		byte[] strBytes = new byte[(tmpHexLen / 2)];
		int i = 0;

		while (cursorPos < tmpHexLen) {
			int val = Integer.parseInt(strHex.substring(cursorPos, cursorPos + 2), 16);

			strBytes[i] = (byte) (val & 0xff);
			cursorPos += 2;
			i++;
		}

		return strBytes;
	}

	public static String md5Signature(String orgin) {
		String result = null;
		if (StringUtils.isNotEmpty(orgin)) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				result = bytesToHex(md.digest(orgin.getBytes("utf-8")));
			} catch (Exception e) {
				throw new java.lang.RuntimeException("sign error !");
			}
		}
		return result;
	}

	public static byte[] zipString(byte[] source) {
		try {

			ByteArrayOutputStream bos = null;
			GZIPOutputStream os = null;
			byte[] bs = null;
			try {
				bos = new ByteArrayOutputStream();
				os = new GZIPOutputStream(bos);
				os.write(source);
				os.close();
				bos.close();
				bs = bos.toByteArray();
				return bs;
			} finally {
				bs = null;
				bos = null;
				os = null;
			}
		} catch (Exception ex) {
			return source;
		}
	}

	public static byte[] unzipString(byte[] str) {
		ByteArrayInputStream bis = null;
		ByteArrayOutputStream bos = null;
		GZIPInputStream is = null;
		byte[] buf = null;
		try {
			bis = new ByteArrayInputStream(str);
			bos = new ByteArrayOutputStream();
			is = new GZIPInputStream(bis);
			buf = new byte[1024];
			int len;
			while ((len = is.read(buf)) != -1) {
				bos.write(buf, 0, len);
			}
			is.close();
			bis.close();
			bos.close();
			return bos.toByteArray();
		} catch (Exception ex) {
			return str;
		} finally {
			bis = null;
			bos = null;
			is = null;
			buf = null;
		}
	}
}
