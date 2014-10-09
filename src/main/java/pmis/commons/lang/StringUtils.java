package pmis.commons.lang;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 
 * @ClassName: StringUtils
 * @description: 文本处理工具类
 * @author: youlun@126.com
 * @version: V1.0
 * 
 */

public abstract class StringUtils extends org.apache.commons.lang.StringUtils
{

	public static String fillLeft(String s, char c, int i)
	{
		return fillStr(s, c, i, true);
	}

	private static String fillStr(String s, char c, int i, boolean flag)
	{
		int j = i - s.length();
		if (j <= 0)
			return s;
		StringBuilder stringbuilder = new StringBuilder(s);
		for (; j > 0; j--)
			if (flag)
				stringbuilder.insert(0, c);
			else
				stringbuilder.append(c);

		return stringbuilder.toString();
	}

	/**
	 * 
	 * @Title: getPingYin
	 * @author: youlun@126.com
	 * 
	 *          <p>
	 *          传入城市名称,获得城市拼音全拼
	 *          </p>
	 * 
	 *          <pre>
	 * StringUtil.getPingYin("汉中")   = hanzhong
	 * StringUtils.isEmptyByObject("陕西")     = shanxi
	 * </pre>
	 * 
	 */

	public static String getPingYin(String src)
	{
		char[] t1;
		t1 = src.toCharArray();
		String[] t2;
		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		t3.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);
		String t4 = "";
		try
		{
			for (char aT1 : t1)
			{
				// 判断是否为汉字字符
				if (Character.toString(aT1).matches("[\\u4E00-\\u9FA5]+"))
				{
					t2 = PinyinHelper.toHanyuPinyinStringArray(aT1, t3);
					t4 += t2[0];
				}
				else
				{
					t4 += Character.toString(aT1);
				}
			}
			return t4;
		}
		catch (BadHanyuPinyinOutputFormatCombination e1)
		{
			return "";
		}
	}

}
