package pmis.commons.security;

import org.apache.commons.codec.binary.Base64;

/**
 * 
 * @ClassName: Base64Util
 * @author: wuwh
 * @CreateDate: 2014-4-2 下午12:58:04
 * @UpdateUser: wuwh
 * @UpdateDate: 2014-4-2 下午12:58:04
 * @UpdateRemark: 说明本次修改内容
 * @Description: Base64 加密解密工具类
 * @version: V1.0
 */
public abstract class Base64Utils
{

	/**
	 * 
	 * @Title: decrypt
	 * @author: wuwh
	 * @CreateDate: 2014-4-2 下午12:59:43
	 * @UpdateUser: wuwh
	 * @UpdateDate: 2014-4-2 下午12:59:43
	 * @UpdateRemark: 说明本次修改内容
	 * @Description: 解码
	 * @version: V1.0
	 * @param encryptString
	 * @return
	 */
	public static byte[] decrypt(String encryptString)
	{
		byte[] arr = Base64.decodeBase64(encryptString);
		return arr;
	}

	/**
	 * 
	 * @Title: encrypt
	 * @author: wuwh
	 * @CreateDate: 2014-4-2 下午12:59:58
	 * @UpdateUser: wuwh
	 * @UpdateDate: 2014-4-2 下午12:59:58
	 * @UpdateRemark: 说明本次修改内容
	 * @Description: base64位加密
	 * @version: V1.0
	 * @param bytes
	 * @return
	 */
	public static String encrypt(byte[] bytes)
	{
		byte[] arr = Base64.encodeBase64(bytes, true);
		return new String(arr);
	}

}