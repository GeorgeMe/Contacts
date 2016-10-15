package com.is.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 字符串处理集合
 */
public class StringUtils {

	/**
	 * 判断 字符串是否是空的
	 * 
	 * @param string
	 * @return true 空字符串，false 不为空
	 */
	public static boolean StringIsEmpty(String string) {
		if (string == null || ("").equals(string.trim())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 得到字符串的非空指针
	 * 
	 * @param str
	 *            要转换的字符串
	 * @return
	 */
	public static String getString(String str) {
		if (str == null || str.equals("null") || str.equals("")) {
			return "";
		} else {
			return str.trim();
		}
	}

	/**
	 * 去除字符串中的 换行符，制表符  回车符     ，（不包含 空格）
	 * @Title: getStringContainSpecialFlag 
	 * @Description: TODO
	 * @param str
	 * @return
	 * @return: String
	 */
	public static String getStringContainSpecialFlag(String str) {

		String dest = "";

		if (str != null) {
			Pattern p = Pattern.compile("\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 判断是否是数字
	 * 
	 * @param str 字符流
	 * @return
	 */
	public static boolean isNumeric(String str) {
		if (str == null || "".equals(str.trim())) {
			return false;
		} else {
			Pattern pattern = Pattern.compile("[0-9]*");
			return pattern.matcher(str.trim()).matches();
		}
	}
	/**
	 * 拼接字符床   将一个字符 按照 分隔符拼接起来  
	 * @Title: appendStringByFlags 
	 * @Description: TODO
	 * @param strings
	 * @param flag
	 * @return
	 * @return: String
	 */
	public static String appendStringByFlags(ArrayList<String> strings,String flag){
		Iterator<String> it = strings.iterator();
		StringBuffer sbBuffer = new StringBuffer();
		for (String string : strings) {
			sbBuffer.append(string);
			it.next();
			if(it.hasNext()){
				sbBuffer.append(flag);
			}
		}
		return sbBuffer.toString(); 
	}

}
