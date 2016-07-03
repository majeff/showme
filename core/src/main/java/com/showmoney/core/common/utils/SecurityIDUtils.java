package com.showmoney.core.common.utils;

import java.util.HashMap;

/**
 * 
 */
public class SecurityIDUtils {
	private static final int[] keyAry = { 1, 9, 8, 7, 6, 5, 4, 3, 2, 1 };

	private static final HashMap<String, String> charMap = new HashMap<String, String>(); // NOPMD

	static {
		charMap.put("A", "10");
		charMap.put("B", "11");
		charMap.put("C", "12");
		charMap.put("D", "13");
		charMap.put("E", "14");
		charMap.put("F", "15");
		charMap.put("G", "16");
		charMap.put("H", "17");
		charMap.put("I", "34");
		charMap.put("J", "18");
		charMap.put("K", "19");
		charMap.put("L", "20");
		charMap.put("M", "21");
		charMap.put("N", "22");
		charMap.put("O", "35");
		charMap.put("P", "23");
		charMap.put("Q", "24");
		charMap.put("R", "25");
		charMap.put("S", "26");
		charMap.put("T", "27");
		charMap.put("U", "28");
		charMap.put("V", "29");
		charMap.put("W", "32");
		charMap.put("X", "30");
		charMap.put("Y", "31");
		charMap.put("Z", "33");
	}

	/**
	 * 
	 * 
	 * @param securityID �����Ҧr��
	 * @return true �q�L false ����
	 */
	public static boolean ckSecurityID(String securityID) {
		try {
			String firstCode = charMap.get(securityID.substring(0, 1).toUpperCase());
			if (firstCode == null) {
				return false; // �Ĥ@�X���O �^��r
			}

			String values = firstCode + securityID.substring(1);

			int checkSum = 0;
			for (int index = 0; index < keyAry.length; index++) {
				String ch = values.substring(index, index + 1);
				checkSum += Integer.parseInt(ch) * keyAry[index];
			}

			String checkCode = String.valueOf(10 - (checkSum % 10));
			checkCode = checkCode.substring(checkCode.length() - 1);

			return (securityID.substring(9, 10).equals(checkCode));
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
