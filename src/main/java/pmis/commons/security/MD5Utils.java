package pmis.commons.security;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * @ClassName: MD5Util
 * @author: wuwh
 * @CreateDate: 2014-4-2 下午2:43:57
 * @UpdateUser: wuwh
 * @UpdateDate: 2014-4-2 下午2:43:57
 * @UpdateRemark: 说明本次修改内容
 * @Description: 采用Md5加密解密
 * @version: V1.0
 */
public abstract class MD5Utils
{
	/**
	 * 
	 * @Title: getMD5
	 * @author: wuwh
	 * @CreateDate: 2014-4-2 下午2:45:04
	 * @UpdateUser: wuwh
	 * @UpdateDate: 2014-4-2 下午2:45:04
	 * @UpdateRemark: 说明本次修改内容
	 * @Description: 字符串加密
	 * @version: V1.0
	 * @param fileName
	 * @return
	 */
	public static String getMD5(final String fileName)
	{
		// create file object
		final File file = new File(fileName);
		FileInputStream fin = null;
		try
		{
			// read file content into byte array
			fin = new FileInputStream(file);
			final byte fileContent[] = new byte[(int) file.length()];
			fin.read(fileContent);
			// Generate MessageDigest
			final MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(fileContent);
			final byte messageDigest[] = algorithm.digest();

			// Generate string md5 values
			String md5 = "";
			for (final byte element : messageDigest)
			{
				final String hex = Integer.toHexString(0xFF & element);
				if (hex.length() == 1)
				{
					md5 += "0";
				}
				md5 += hex;
			}
			// System.out.println("md5 : " + md5);
			return md5;
		}
		catch (final FileNotFoundException e)
		{
			System.out.println("File not found" + e);
		}
		catch (final IOException ioe)
		{
			System.out.println("Exception while reading the file " + ioe);
		}
		catch (final NoSuchAlgorithmException ale)
		{
			System.out.println("Exception while generating message digest. " + ale);
		}
		finally
		{
			try
			{
				if (fin != null)
				{
					fin.close();
					fin = null;
				}
			}
			catch (Exception e)
			{
			}
		}
		return null;
	}

	/**
	 * 
	 * @Title: makeMD5ForFile
	 * @author: wuwh
	 * @CreateDate: 2014-4-2 下午2:44:14
	 * @UpdateUser: wuwh
	 * @UpdateDate: 2014-4-2 下午2:44:14
	 * @UpdateRemark: 说明本次修改内容
	 * @Description: 文件加密
	 * @version: V1.0
	 * @param file
	 * @return
	 */
	public static String makeMD5ForFile(File file)
	{
		StringBuilder rtnVal = new StringBuilder();
		FileInputStream fs = null;
		BufferedInputStream bi = null;
		ByteArrayOutputStream bo = null;
		try
		{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			fs = new FileInputStream(file);
			bi = new BufferedInputStream(fs);
			bo = new ByteArrayOutputStream();
			int fileSize = bi.available();
			if (fileSize >= 1)
			{
				byte[] b = new byte[fileSize];
				int i;
				while ((i = bi.read(b, 0, b.length)) != -1)
				{
				}
				md5.update(b);
				byte[] md5Digest = md5.digest();
				int size = md5Digest.length;
				for (int j = 0; j < size; j++)
				{
					byte by = (byte) md5Digest[j];
					String hex = Integer.toHexString(0xFF & by);
					if (hex.length() == 1)
					{
						rtnVal.append("0");
					}
					rtnVal.append(hex);
				}
			}
		}
		catch (final FileNotFoundException e)
		{
			System.out.println("File not found" + e);
		}
		catch (final IOException ioe)
		{
			System.out.println("Exception while reading the file " + ioe);
		}
		catch (final NoSuchAlgorithmException ale)
		{
			System.out.println("Exception while generating message digest. " + ale);
		}
		finally
		{
			try
			{
				if (fs != null)
				{
					fs.close();
					fs = null;
				}
			}
			catch (Exception e)
			{
			}
			try
			{
				if (bo != null)
				{
					bo.close();
					bo = null;
				}
			}
			catch (Exception e)
			{
			}
			try
			{
				if (bi != null)
				{
					bi.close();
					bi = null;
				}
			}
			catch (Exception e)
			{
			}
		}
		return rtnVal.toString();
	}

	public static String md5(byte[] plainByte)
	{
		StringBuffer rtnVal = new StringBuffer();
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainByte);
			byte b[] = md.digest();
			int i;
			for (int offset = 0; offset < b.length; offset++)
			{
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					rtnVal.append("0");
				rtnVal.append(Integer.toHexString(i));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return rtnVal.toString();
	}
}
