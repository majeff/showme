package com.showmoney.core.common.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 將數字轉換為中文字串的工具程式 範例：12345 -> 壹萬貳仟參佰肆拾伍元整 10001002 -> 壹仟萬壹仟零貳元整
 * 
 * @author lee_solar
 */
public final class ChineseNumberUtils {

	/**
	 * 大寫阿拉伯數字 例如:九十九
	 */
	public final static String ARYBIGNUM = "ARYBIGNUM";

	/**
	 * 大寫中文數字 例如:玖拾玖
	 */
	public final static String CHIBIGNUM = "CHIBIGNUM";

	private final static String[] month_CHINESE_str = new String[] { "一", "二", "三", "四", "五", "六", "七", "八", "九", "十",// NOPMD
			"十一", "十二" };

	private final static String[] nums_ARYBIGNUM_str = new String[] { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };// NOPMD

	private final static String[] nums_CHIBIGNUM_str = new String[] { "零", "壹", "貳", "參", "肆", "伍", "陸", "柒", "捌", "玖" };// NOPMD

	private final static String[] wei_1 = new String[] { "元整", "萬", "億", "兆", "吉" };// NOPMD

	private final static String[] wei_ARYBIGNUM_2 = new String[] { "千", "百", "十" };// NOPMD

	private final static String[] wei_CHIBIGNUM_2 = new String[] { "仟", "佰", "拾" };// NOPMD

	private final static String[] float_wei = new String[] { "元", "零", "角" };// NOPMD

	/**
	 * 
	 */
	private ChineseNumberUtils() {
	}

	/**
	 * 將 num 轉成中文金額 支援十六位正整數 (0 ~ 9999999999999998)
	 * 
	 * @param num 要轉換之數字
	 * @param format 字型判斷條件，ARYBIGNUM = "九十九"; CHIBIGNUM = "玖拾玖"
	 * @param appendCoin 最後是否附加 "元整" 字樣
	 * @param showZeroWord 回傳字串裡是否含"零"字，true = "包含": false = "不包含"
	 * @return 中文金額字串
	 */
	public static String toChineseStr(final BigDecimal num, final String format, final boolean appendCoin,
			final boolean showZeroWord) throws NumberFormatException {
		String[] wei_Str = null;
		String[] nums_Str = null;

		if (ARYBIGNUM.equals(format)) {
			nums_Str = nums_ARYBIGNUM_str;
			wei_Str = wei_ARYBIGNUM_2;
		} else {
			nums_Str = nums_CHIBIGNUM_str;
			wei_Str = wei_CHIBIGNUM_2;
		}

		if (num.compareTo(new BigDecimal("9999999999999999")) > 1 || num.compareTo(new BigDecimal("0")) < 0) {
			throw new NumberFormatException();
		}

		StringBuffer sb = new StringBuffer();

		// 若轉換字元為0的話, 直接回傳零元
		if (num.compareTo(new BigDecimal("0")) == 0) {
			sb.append(nums_Str[0]);
			if (appendCoin) {
				sb.append(wei_1[0]);
			}
			return sb.toString();
		}
		List<String> nums = new ArrayList<String>();
		// 分割小數與整數
		// String num_str = nFormat.format(num);
		String num_str = num.setScale(0, BigDecimal.ROUND_DOWN).toString();
		String int_str = num_str.substring(0, (num_str.indexOf(".") >= 0 ? num_str.indexOf(".") : num_str.length()));
		String float_str = "";
		if (num_str.indexOf(".") >= 0) {
			float_str = num_str.substring(num_str.indexOf("."));
		}
		splitNum(int_str.toString(), nums);
		changeIntStr(sb, nums, nums.size() - 1, showZeroWord, nums_Str, wei_Str);
		if (float_str != null) {
			if (float_str.equals("")) {
				if (appendCoin) {
					sb.append(wei_1[0]);
				}
			} else {
				sb.append(float_wei[0]);
				sb.append(float_wei[1]);
				sb.append(nums_Str[changeChToInt(float_str.charAt(1))]);
				sb.append(float_wei[2]);
			}
		}
		return sb.toString();
	}

	/**
	 * 將 num 轉成中文金額 支援十六位正整數 (0 ~ 9999999999999998)
	 * 
	 * @param num 要轉換之數字
	 * @param format 字型判斷條件，ARYBIGNUM = "九十九"; CHIBIGNUM = "玖拾玖"
	 * @param appendCoin 最後是否附加 "元整" 字樣
	 * @param showZeroWord 回傳字串裡是否含"零"字，true = "包含": false = "不包含"
	 * @return 中文金額字串
	 */
	public static String toChineseStr(int num, String format, boolean appendCoin, boolean showZeroWord) {
		return ChineseNumberUtils.toChineseStr(new BigDecimal(num), format, appendCoin, showZeroWord);
	}

	/**
	 * 將 num 轉成中文金額 支援十六位正整數 (0 ~ 9999999999999998)
	 * 
	 * @param num 要轉換之數字
	 * @param format 字型判斷條件，ARYBIGNUM = "九十九"; CHIBIGNUM = "玖拾玖"
	 * @param appendCoin 最後是否附加 "元整" 字樣
	 * @param showZeroWord 回傳字串裡是否含"零"字，true = "包含": false = "不包含"
	 * @return 中文金額字串
	 */
	public static String toChineseStr(Integer num, String format, boolean appendCoin, boolean showZeroWord) {
		return ChineseNumberUtils.toChineseStr(num.intValue(), format, appendCoin, showZeroWord);
	}

	/**
	 * 將字符ch轉成整型數
	 * 
	 * @param ch
	 * @return
	 */
	private static int changeChToInt(char ch) {
		return Integer.parseInt(ch + "");
	}

	/**
	 * 轉變整數部份的字符串成中文
	 * 
	 * @param sb_res 存放中文字符串的結果
	 * @param nums 存放分割好的字符串的列表
	 * @param index 解析當前列表的下標
	 * @param showZeroWord 回傳字串裡是否含"零"字，true = "包含": false = "不包含"
	 */
	private static void changeIntStr(StringBuffer sb_res, List<String> nums, int index, boolean showZeroWord,
			String[] nums_Str, String[] wei_Str) {
		if (index < 0) {
			return;
		}
		StringBuffer sb = new StringBuffer();
		String temp = nums.get(index);
		if (temp != null && !temp.equals("")) {
			if (temp.length() == 4) {
				subChange(0, 4, sb, sb_res, temp, index, nums, showZeroWord, nums_Str, wei_Str);
			}
		}
		index--;
		changeIntStr(sb_res, nums, index, showZeroWord, nums_Str, wei_Str);
	}

	/**
	 * 轉換整數部份的字符串成中文
	 * 
	 * @param begin 字符串的開始
	 * @param end 字符串的結束
	 * @param sb 當前轉換的buffer
	 * @param sb_res 存放中文字符串的結果
	 * @param temp 當前解析的數字字符串
	 * @param index 解析當前列表的下標
	 * @param nums 解析的列表
	 * @param showZeroWord 回傳字串裡是否含"零"字，true = "包含": false = "不包含"
	 */
	private static void subChange(int begin, int end, StringBuffer sb, StringBuffer sb_res, String temp, int index,
			List<String> nums, boolean showZeroWord, String[] nums_Str, String[] wei_Str) {

		// 從字符開始到結束一個一個的解析成中文
		for (int i = begin; i < end; i++) {
			int num = changeChToInt(temp.charAt(i));
			// 例如將 1 轉成壹
			sb.append(nums_Str[num]);
			// 如果數字大於0, 且不是最後一位, 加上如 "仟", "佰", "拾" ..
			if ((i < wei_Str.length) && (i < (end - 1)) && num > 0) {
				sb.append(wei_Str[i]);
			}
		}
		// 去除字符串中 如將零零轉成零
		String tt = sb.toString().replaceAll("零+", (showZeroWord ? "零" : ""));
		sb = new StringBuffer(tt);

		// 如果字符串以零結尾將零去掉
		if (sb.toString().endsWith("零")) {
			sb.deleteCharAt(sb.length() - 1);
		}
		// 將解析好的字符串加上如 "萬", "億", "兆" ..
		if (index < nums.size() - 1) {
			sb.append(ChineseNumberUtils.wei_1[nums.size() - 1 - index]);
		}
		// 將 buffer 中的字符串存放到 sb_res 中
		sb.append(sb_res);
		sb_res.setLength(0);
		sb_res.append(sb.toString());
		// 如果是最高位為零將零去掉 (單純 "零" 除外)
		if ((index == 0) && (sb_res.charAt(0) == '零')) {
			sb_res.deleteCharAt(0);
		}
	}

	/**
	 * 將字符串num每4個一組放入 res 列表中
	 * 
	 * @param num 將要分割的字符串
	 * @param res 存放分割好的字符串的列表
	 */
	private static void splitNum(String num, List<String> res) { // NOPMD by jeffma on 4/22/08 2:24 PM
		if (num == null || num.equals("")) {
			return;
		}
		if (num.length() - 4 <= 0) {
			// 如果字符串的長度小於4則前面補 0
			for (int i = num.length(); i < 4; i++) {
				num = "0" + num;
			}
			res.add(0, num);
			return;
		}
		String temp = num.substring(num.length() - 4);
		if (temp != null && !temp.equals("")) {
			res.add(0, temp);
		}
		splitNum(num.substring(0, num.length() - 4), res);
	}

	public static String getChineseMonth(int month) {
		return month_CHINESE_str[month - 1] + "月";
	}

}
