package pmis.commons.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import pmis.commons.codec.EncodingUtils;
import pmis.commons.lang.StringUtils;
import pmis.commons.lang.ThrowableUtils;

/**
 * 操作文件的工具类
 * 
 * @author wuwh
 * 
 */
public abstract class FileUtils
{
	private static int cacheSize = 8192;

	/**
	 * An empty array of type <code>File</code>.
	 */
	public static final File[] EMPTY_FILE_ARRAY = new File[0];

	/**
	 * 默认的过滤器
	 */
	private static FilenameFilter filter = null;

	/**
	 * The number of bytes in a kilobyte.
	 */
	public static final long ONE_KB = 1024;

	/**
	 * The number of bytes in a megabyte.
	 */
	public static final long ONE_MB = ONE_KB * ONE_KB;

	/**
	 * The number of bytes in a gigabyte.
	 */
	public static final long ONE_GB = ONE_KB * ONE_MB;

	/**
	 * The number of bytes in a terabyte.
	 */
	public static final long ONE_TB = ONE_KB * ONE_GB;

	/**
	 * The number of bytes in a petabyte.
	 */
	public static final long ONE_PB = ONE_KB * ONE_TB;

	/**
	 * The number of bytes in an exabyte.
	 */
	public static final long ONE_EB = ONE_KB * ONE_PB;

	/**
	 * The number of bytes in a zettabyte.
	 */
	public static final BigInteger ONE_ZB = BigInteger.valueOf(ONE_KB).multiply(BigInteger.valueOf(ONE_EB));

	/**
	 * The number of bytes in a yottabyte.
	 */
	public static final BigInteger ONE_YB = ONE_ZB.multiply(BigInteger.valueOf(ONE_EB));

	/**
	 * The file copy buffer size (30 MB)
	 */
	private static final long FILE_COPY_BUFFER_SIZE = ONE_MB * 30;

	/**
	 * The UTF-8 character set, used to decode octets in URLs.
	 */
	private static final Charset UTF8 = Charset.forName("UTF-8");

	/**
	 * 把long个字节的文件大小转换成为可读性强的文件大小版本
	 * 
	 * @param size
	 * @return
	 */
	public static String byteCountToDisplaySize(long size)
	{
		String displaySize;
		// if (size / ONE_EB > 0) {
		// displaySize = String.valueOf(size / ONE_EB) + " EB";
		// } else if (size / ONE_PB > 0) {
		// displaySize = String.valueOf(size / ONE_PB) + " PB";
		// } else if (size / ONE_TB > 0) {
		// displaySize = String.valueOf(size / ONE_TB) + " TB";
		// } else
		if (size / ONE_GB > 0)
		{
			displaySize = String.valueOf(size / ONE_GB) + " GB";
		}
		else if (size / ONE_MB > 0)
		{
			displaySize = String.valueOf(size / ONE_MB) + " MB";
		}
		else if (size / ONE_KB > 0)
		{
			displaySize = String.valueOf(size / ONE_KB) + " KB";
		}
		else
		{
			displaySize = String.valueOf(size) + " bytes";
		}
		return displaySize;
	}

	public static boolean copy(File src, File target) throws IOException
	{
		return copy(src, target, cacheSize);
	}

	/**
	 * 复制文件或者目录 至于复制文件还是目录取决于源文件src 默认如果目标父目录不存在，也一并创建
	 * 
	 * @param src源目录
	 * @param target目标目录
	 * @param cacheSize缓存大小
	 * @return 是否读取成功
	 * @throws IOException
	 */
	public static boolean copy(File src, File target, int cacheSize) throws IOException
	{
		if (src == null || target == null || !src.exists())
			return false;
		boolean bo = false;
		if (src.isFile())
		{
			bo |= copyFile(src, target, cacheSize);
		}
		else if (src.isDirectory())
		{
			bo |= copyDir(src, target, cacheSize);
		}
		return bo;
	}

	/**
	 * 复制目录 默认如果目标父目录不存在，也一并创建
	 * 
	 * @param src源目录
	 * @param target目标目录
	 * @return 是否读取成功
	 * @throws IOException
	 */
	public static boolean copyDir(File src, File target) throws IOException
	{
		return copyDir(src, target, cacheSize);
	}

	/**
	 * 复制目录 默认如果目标父目录不存在，也一并创建
	 * 
	 * @param src源目录
	 * @param target目标目录
	 * @param cacheSize缓存大小
	 * @return 是否读取成功
	 * @throws IOException
	 */
	public static boolean copyDir(File src, File target, int cacheSize) throws IOException
	{
		if (src == null || target == null || !src.exists())
			return false;
		if (!src.isDirectory())
			throw new IOException(src.getAbsolutePath() + " should be a directory!");
		if (!target.exists())
			if (!makeDir(target))
				return false;
		boolean re = true;
		File[] files = src.listFiles();
		if (files != null)
			for (File f : files)
			{
				if (f.isFile())
					re &= copyFile(f, new File(target.getAbsolutePath() + "/" + f.getName()), cacheSize);
				else
					re &= copyDir(f, new File(target.getAbsolutePath() + "/" + f.getName()), cacheSize);
			}
		return re;
	}

	// public static void main(String[] args) throws Exception {
	// // System.out.println(new File("E:\\img\\tes\\te.txt").isFile());
	// System.out.println(getFileSize(new File("E:\\桌面\\")));
	// // for (File f:new File("E:\\桌面\\detail.jsp").listFiles()){
	// // System.out.println(f.getAbsolutePath());
	// // }
	// }

	/**
	 * 
	 * 复制文件（不包括目录） 默认如果目标父目录不存在，也一并创建
	 * 
	 * @param src源文件
	 * @param target目标文件
	 * @return
	 * @throws IOException
	 */
	public static boolean copyFile(File src, File target) throws IOException
	{
		return copyFile(src, target, cacheSize);
	}

	/**
	 * 复制文件（不包括目录） 默认如果目标父目录不存在，也一并创建
	 * 
	 * @param src源文件
	 * @param target目标文件
	 * @param cacheSize缓存大小
	 * @return 是否读取成功
	 * @throws IOException
	 */
	public static boolean copyFile(File src, File target, int cacheSize) throws IOException
	{
		if (!isFile(src) || target == null)
			return false;
		if (!target.exists())
			if (!createNewFile(target))
				return false;
		// 缓冲流
		BufferedInputStream inBuff = new BufferedInputStream(new FileInputStream(src));
		BufferedOutputStream outBuff = new BufferedOutputStream(new FileOutputStream(target));
		byte[] buff = new byte[cacheSize];
		int len = 0;
		while ((len = inBuff.read(buff)) != -1)
			outBuff.write(buff, 0, len);
		IOUtils.close(inBuff);
		IOUtils.flush(outBuff);
		IOUtils.close(outBuff);
		return target.setLastModified(src.lastModified());
	}

	public static File createFileIfNoExists(File file) throws IOException
	{
		if (file == null || file.exists())
			return null;
		makeDir(file.getParentFile());
		boolean b = file.createNewFile();
		if (b)
			return file;
		else
			return null;
	}

	public static File createFileIfNoExists(String path) throws IOException
	{
		return createFileIfNoExists(new File(path));
	}

	/**
	 * 创建一个文件（包括父类） 创建新文件，如果父目录不存在，也一并创建。可接受 null 参数
	 * 
	 * @param f
	 *            文件对象
	 * @return false，如果文件已存在。 true 创建成功
	 * @throws IOException
	 */
	public static boolean createNewFile(File file) throws IOException
	{
		return createFileIfNoExists(file) == null;
	}

	public static boolean createNewFile(String fileName) throws IOException
	{
		return createNewFile(new File(fileName));
	}

	/**
	 * 删除文件或者目录（文件夹）
	 * 
	 * @param f
	 * @return
	 */
	public static boolean delete(File f, FilenameFilter filter)
	{
		boolean bo = false;
		if (isFile(f))
			bo = f.delete();
		else if (isDirectory(f))
			bo = deleteDir(f, filter);
		return bo;
	}

	/**
	 * 删除一个目录包括目录下所有东西 如果不是目录或为null则返回false
	 * 
	 * @param f
	 * @return
	 */
	public static boolean deleteDir(File f, FilenameFilter filter)
	{
		if (!isDirectory(f))
			return false;
		boolean re = false;
		File[] fs = f.listFiles(filter);
		if (null != fs)
		{
			if (fs.length == 0)
				return f.delete();
			for (File file : fs)
			{
				if (file.isFile())
					re |= deleteFile(file);
				else
					re |= deleteDir(file, filter);
			}
			re |= f.delete();
		}
		return re;
	}

	/**
	 * 删除单个文件
	 * 
	 * @param f
	 * @return
	 */
	public static boolean deleteFile(File f)
	{
		if (isFile(f))
			return f.delete();
		return false;
	}

	/**
	 * 清空指定目录中的文件。 这个方法将尽可能删除所有的文件，但是只要有一个文件没有被删除都会返回false。
	 * 另外这个方法不会迭代删除，即不会删除子目录及其内容。
	 * 
	 * @param directory
	 *            要清空的目录
	 * @return 目录下的所有文件都被成功删除时返回true，否则返回false.
	 * @since 1.0
	 */
	public static boolean emptyDirectory(File directory)
	{
		boolean result = true;
		File[] entries = directory.listFiles();
		for (int i = 0; i < entries.length; i++)
		{
			if (!entries[i].delete())
			{
				result = false;
			}
		}
		return result;
	}

	/**
	 * 清空指定目录中的文件。 这个方法将尽可能删除所有的文件，但是只要有一个文件没有被删除都会返回false。
	 * 另外这个方法不会迭代删除，即不会删除子目录及其内容。
	 * 
	 * @param directoryName
	 *            要清空的目录的目录名
	 * @return 目录下的所有文件都被成功删除时返回true，否则返回false。
	 * @since 1.0
	 */
	public static boolean emptyDirectory(String directoryName)
	{
		File dir = new File(directoryName);
		return emptyDirectory(dir);
	}

	/**
	 * 从 CLASSPATH 下寻找一个文件
	 * 
	 * @param path
	 *            文件路径
	 * 
	 * @return 文件对象，如果不存在，则为 null
	 */
	public static File findFile(String path)
	{
		return findFile(path, FileUtils.class.getClassLoader(), EncodingUtils.defaultEncoding());
	}

	/**
	 * 从 CLASSPATH 下寻找一个文件
	 * 
	 * @param path
	 *            文件路径
	 * @param klassLoader
	 *            使用该 ClassLoader进行查找
	 * 
	 * @return 文件对象，如果不存在，则为 null
	 */
	public static File findFile(String path, ClassLoader klassLoader)
	{
		return findFile(path, klassLoader, EncodingUtils.defaultEncoding());
	}

	/**
	 * 从 CLASSPATH 下寻找一个文件
	 * 
	 * @param path
	 *            文件路径
	 * @param klassLoader
	 *            参考 ClassLoader
	 * @param enc
	 *            文件路径编码
	 * 
	 * @return 文件对象，如果不存在，则为 null
	 */
	public static File findFile(String path, ClassLoader klassLoader, String enc)
	{
		if (null == path)
			return null;
		return new File(path);
	}

	/**
	 * 从 CLASSPATH 下寻找一个文件
	 * 
	 * @param path
	 *            文件路径
	 * @param enc
	 *            文件路径编码
	 * @return 文件对象，如果不存在，则为 null
	 */
	public static File findFile(String path, String enc)
	{
		return findFile(path, FileUtils.class.getClassLoader(), enc);
	}

	/**
	 * 获取输出流
	 * 
	 * @param path
	 *            文件路径
	 * 
	 * @return 输出流
	 */
	public static InputStream findFileAsStream(String path)
	{
		return findFileAsStream(path, FileUtils.class, EncodingUtils.defaultEncoding());
	}

	/**
	 * 获取输出流
	 * 
	 * @param path
	 *            文件路径
	 * @param klass
	 *            参考的类， -- 会用这个类的 ClassLoader
	 * 
	 * @return 输出流
	 */
	public static InputStream findFileAsStream(String path, Class<?> klass)
	{
		return findFileAsStream(path, klass, EncodingUtils.defaultEncoding());
	}

	/**
	 * 获取输出流
	 * 
	 * @param path
	 *            文件路径
	 * @param klass
	 *            参考的类， -- 会用这个类的 ClassLoader
	 * @param enc
	 *            文件路径编码
	 * 
	 * @return 输出流
	 */
	public static InputStream findFileAsStream(String path, Class<?> klass, String enc)
	{
		File f = new File(path);
		if (f.exists())
			try
			{
				return new FileInputStream(f);
			}
			catch (FileNotFoundException e1)
			{
				return null;
			}
		if (null != klass)
		{
			InputStream ins = klass.getClassLoader().getResourceAsStream(path);
			if (null == ins)
				ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
			if (null != ins)
				return ins;
		}
		return ClassLoader.getSystemResourceAsStream(path);
	}

	/**
	 * 获取输出流
	 * 
	 * @param path
	 *            文件路径
	 * @param enc
	 *            文件路径编码
	 * 
	 * @return 输出流
	 */
	public static InputStream findFileAsStream(String path, String enc)
	{
		return findFileAsStream(path, FileUtils.class, enc);
	}

	/**
	 * 文件或者目录的大小
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static long getFileSize(File f) throws Exception
	{
		if (isFile(f))
			return f.length();
		long size = 0;
		if (!isDirectory(f))
			return size;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++)
			if (flist[i].isDirectory())
				size = size + getFileSize(flist[i]);
			else
				size = size + flist[i].length();
		return size;
	}

	public static long getFileSize(String fileName) throws Exception
	{
		return getFileSize(new File(fileName));
	}

	/**
	 * 得到文件的类型。 实际上就是得到文件名中最后一个“.”后面的部分。
	 * 
	 * @param file
	 *            文件
	 * @return 文件名中的类型部分
	 * @since 1.0
	 */
	public static String getFileType(File file)
	{
		return getTypePart(file.getName());
	}

	/**
	 * @param path
	 *            全路径
	 * @return 文件或者目录名
	 */
	public static String getName(String path)
	{
		if (!StringUtils.isBlank(path))
		{
			int pos = path.replace('\\', '/').lastIndexOf('/');
			if (pos > 0)
				return path.substring(pos + 1);
		}
		return path;
	}

	/**
	 * 得到文件的名字部分。 实际上就是路径中的最后一个路径分隔符后的部分。
	 * 
	 * @param fileName
	 *            文件名
	 * @return 文件名中的名字部分
	 * @since 1.0
	 */
	public static String getNamePart(String fileName)
	{
		int point = getPathLsatIndex(fileName);
		int length = fileName.length();
		if (point == -1)
		{
			return fileName;
		}
		else if (point == length - 1)
		{
			int secondPoint = getPathLsatIndex(fileName, point - 1);
			if (secondPoint == -1)
			{
				if (length == 1)
				{
					return fileName;
				}
				else
				{
					return fileName.substring(0, point);
				}
			}
			else
			{
				return fileName.substring(secondPoint + 1, point);
			}
		}
		else
		{
			return fileName.substring(point + 1);
		}
	}

	/**
	 * @param path
	 *            路径
	 * @return 父路径
	 */
	public static String getParent(String path)
	{
		if (StringUtils.isBlank(path))
			return path;
		int pos = path.replace('\\', '/').lastIndexOf('/');
		if (pos > 0)
			return path.substring(0, pos);
		return "/";
	}

	/**
	 * 得到路径分隔符在文件路径中首次出现的位置。 对于DOS或者UNIX风格的分隔符都可以。
	 * 
	 * @param fileName
	 *            文件路径
	 * @return 路径分隔符在路径中首次出现的位置，没有出现时返回-1。
	 * @since 1.0
	 */
	public static int getPathIndex(String fileName)
	{
		int point = fileName.indexOf('/');
		if (point == -1)
		{
			point = fileName.indexOf('\\');
		}
		return point;
	}

	/**
	 * 得到路径分隔符在文件路径中指定位置后首次出现的位置。 对于DOS或者UNIX风格的分隔符都可以。
	 * 
	 * @param fileName
	 *            文件路径
	 * @param fromIndex
	 *            开始查找的位置
	 * @return 路径分隔符在路径中指定位置后首次出现的位置，没有出现时返回-1。
	 * @since 1.0
	 */
	public static int getPathIndex(String fileName, int fromIndex)
	{
		int point = fileName.indexOf('/', fromIndex);
		if (point == -1)
		{
			point = fileName.indexOf('\\', fromIndex);
		}
		return point;
	}

	/**
	 * 得到路径分隔符在文件路径中最后出现的位置。 对于DOS或者UNIX风格的分隔符都可以。
	 * 
	 * @param fileName
	 *            文件路径
	 * @return 路径分隔符在路径中最后出现的位置，没有出现时返回-1。
	 * @since 1.0
	 */
	public static int getPathLsatIndex(String fileName)
	{
		int point = fileName.lastIndexOf('/');
		if (point == -1)
		{
			point = fileName.lastIndexOf('\\');
		}
		return point;
	}

	/**
	 * 得到路径分隔符在文件路径中指定位置前最后出现的位置。 对于DOS或者UNIX风格的分隔符都可以。
	 * 
	 * @param fileName
	 *            文件路径
	 * @param fromIndex
	 *            开始查找的位置
	 * @return 路径分隔符在路径中指定位置前最后出现的位置，没有出现时返回-1。
	 * @since 1.0
	 */
	public static int getPathLsatIndex(String fileName, int fromIndex)
	{
		int point = fileName.lastIndexOf('/', fromIndex);
		if (point == -1)
		{
			point = fileName.lastIndexOf('\\', fromIndex);
		}
		return point;
	}

	/**
	 * 得到文件名中的父路径部分。 对两种路径分隔符都有效。 不存在时返回""。
	 * 如果文件名是以路径分隔符结尾的则不考虑该分隔符，例如"/path/"返回""。
	 * 
	 * @param fileName
	 *            文件名
	 * @return 父路径，不存在或者已经是父目录时返回""
	 * @since 1.0
	 */
	public static String getPathPart(String fileName)
	{
		int point = getPathLsatIndex(fileName);
		int length = fileName.length();
		if (point == -1)
		{
			return "";
		}
		else if (point == length - 1)
		{
			int secondPoint = getPathLsatIndex(fileName, point - 1);
			if (secondPoint == -1)
			{
				return "";
			}
			else
			{
				return fileName.substring(0, secondPoint);
			}
		}
		else
		{
			return fileName.substring(0, point);
		}
	}

	/**
	 * 得到相对路径。 文件名不是目录名的子节点时返回文件名。
	 * 
	 * @param pathName
	 *            目录名
	 * @param fileName
	 *            文件名
	 * @return 得到文件名相对于目录名的相对路径，目录下不存在该文件时返回文件名
	 * @since 1.0
	 */
	public static String getSubpath(String pathName, String fileName)
	{
		int index = fileName.indexOf(pathName);
		if (index != -1)
		{
			return fileName.substring(index + pathName.length() + 1);
		}
		else
		{
			return fileName;
		}
	}

	/**
	 * 得到文件的类型。 实际上就是得到文件名中最后一个“.”后面的部分。
	 * 
	 * @param fileName
	 *            文件名
	 * @return 文件名中的类型部分
	 * @since 1.0
	 */
	public static String getTypePart(String fileName)
	{
		int point = fileName.lastIndexOf('.');
		int length = fileName.length();
		if (point == -1 || point == length - 1)
		{
			return "";
		}
		else
		{
			return fileName.substring(point + 1, length);
		}
	}

	/**
	 * 从文件名得到UNIX风格的文件绝对路径。
	 * 
	 * @param fileName
	 *            文件名
	 * @return 对应的UNIX风格的文件路径
	 * @since 1.0
	 * @see #toUNIXpath(String filePath) toUNIXpath
	 */
	public static String getUNIXfilePath(String fileName)
	{
		File file = new File(fileName);
		return toUNIXpath(file.getAbsolutePath());
	}

	/**
	 * 文件对象是否是目录，可接受 null
	 */
	public static boolean isDirectory(File f)
	{
		if (null == f)
			return false;
		if (!f.exists())
			return false;
		if (!f.isDirectory())
			return false;
		return true;
	}

	/**
	 * 精确比较两个文件是否相等
	 * 
	 * @param f1
	 *            文件1
	 * @param f2
	 *            文件2
	 * @return 是否相等
	 */
	public static boolean isEquals(File f1, File f2)
	{
		if (!f1.isFile() || !f2.isFile())
			return false;
		InputStream ins1 = null;
		InputStream ins2 = null;
		try
		{
			ins1 = new BufferedInputStream(new FileInputStream(f1));
			ins2 = new BufferedInputStream(new FileInputStream(f2));
			return IOUtils.equals(ins1, ins2);
		}
		catch (IOException e)
		{
			return false;
		}
		finally
		{
			IOUtils.close(ins1);
			IOUtils.close(ins2);
		}
	}

	/**
	 * 文件对象是否是文件，可接受 null
	 */
	public static boolean isFile(File f)
	{
		return null != f && f.exists() && f.isFile();
	}

	public static void main(String[] args) throws Exception
	{
		// System.out.println(deleteDir(new File("G:\\测试 - 副本")));
		// System.out.println(isDirectory(new File("G:\\测试 - 副本")));
		// System.out.println(move(new File("G:\\新建文件夹\\test.sql"), new File(
		// "E:\\新建文件夹副本\\ttt.sql")));
		// System.out.println();
		// System.out.println(new File("E:\\系统").listFiles(filter).length);
		// skipFileAndClose("E:\\测试.txt", "2222".getBytes(),2111L);
		// System.out.println(getFileSize("E:\\array.txt"));
	}

	// // 查找
	// // 创建一个文件（包括父类）
	// // 删除文件
	// 删除一个目录下所有的东西
	// 复制一个文件
	// 复制一个目录下所有的东西

	/**
	 * 创建新目录，如果父目录不存在，也一并创建。可接受 null 参数
	 * 
	 * @param dir
	 *            目录对象
	 * @return false，如果目录已存在。 true 创建成功
	 * @throws IOException
	 */
	public static boolean makeDir(File file)
	{
		if (file == null || file.exists())
			return false;
		return file.mkdirs();
	}

	/**
	 * 大文件处理 写入的文件从position开始 （修改tempBytes个字节的文本）
	 * 
	 * @param file
	 * @param position内存映射的起始位置
	 * @param size
	 *            =内存映射的大小
	 * @param tempBytes
	 *            =要传入的byte[]
	 * @return
	 */
	public static MappedByteBuffer mappedByteBuffer(File file, int position, int size, byte[] tempBytes)
	{
		if (tempBytes == null || !isFile(file) || size < tempBytes.length)
			return null;
		MappedByteBuffer out = null;
		try
		{
			out = new RandomAccessFile(file, "rw").getChannel().map(FileChannel.MapMode.READ_WRITE, position, size);
			out.put(tempBytes);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return out;
	}

	/**
	 * 移动一个文件夹或者目录 默认如果目标父目录不存在，也一并创建
	 * 
	 * @param src
	 * @param target
	 * @return
	 * @throws IOException
	 */
	public static boolean move(File src, File target) throws IOException
	{
		return move(src, target, cacheSize);
	}

	/**
	 * 移动一个文件夹或者目录 默认如果目标父目录不存在，也一并创建
	 * 
	 * @param src
	 * @param target
	 * @param cacheSize
	 * @return
	 * @throws IOException
	 */
	public static boolean move(File src, File target, int cacheSize) throws IOException
	{
		if (src == null || target == null || !src.exists())
			return false;
		if (src.isFile())
		{
			return moveFile(src, target, cacheSize);
		}
		else
		{
			return moveDir(src, target, cacheSize);
		}
	}

	/**
	 * 移动一个文件夹（目录） 默认如果目标父目录不存在，也一并创建
	 * 
	 * @param src
	 * @param target
	 * @return
	 * @throws IOException
	 */
	public static boolean moveDir(File src, File target) throws IOException
	{
		return moveDir(src, target, cacheSize);
	}

	/**
	 * 移动一个文件夹（目录） 默认如果目标父目录不存在，也一并创建
	 * 
	 * @param src
	 * @param target
	 * @param cacheSize
	 * @return
	 * @throws IOException
	 */
	public static boolean moveDir(File src, File target, int cacheSize) throws IOException
	{
		if (!isDirectory(src) || target == null)
			return false;
		makeDir(target.getParentFile());
		boolean bo = src.renameTo(target);
		if (!bo)
		{
			bo |= copyDir(src, target, cacheSize);
			bo |= deleteDir(src, filter);
		}
		return bo;
	}

	/**
	 * 移动一个文件 默认如果目标父目录不存在，也一并创建
	 * 
	 * @param src
	 * @param target
	 * @return
	 * @throws IOException
	 */
	public static boolean moveFile(File src, File target) throws IOException
	{
		return moveFile(src, target, cacheSize);
	}

	/**
	 * 移动一个文件 默认如果目标父目录不存在，也一并创建
	 * 
	 * @param src
	 * @param target
	 * @param cacheSize
	 * @return
	 * @throws IOException
	 */
	public static boolean moveFile(File src, File target, int cacheSize) throws IOException
	{
		if (!isFile(src) || target == null)
			return false;
		makeDir(target.getParentFile());
		boolean bo = src.renameTo(target);
		if (!bo)
		{
			bo |= copyFile(src, target, cacheSize);
			bo |= deleteFile(src);
		}
		return bo;
	}

	/**
	 * 检查给定目录的存在性 保证指定的路径可用，如果指定的路径不存在，那么建立该路径，可以为多级路径
	 * 
	 * @param path
	 * @return 真假值
	 * @since 1.0
	 */
	public static final boolean pathValidate(String path)
	{
		//String path="d:/web/www/sub";
		//System.out.println(path);
		//path = getUNIXfilePath(path);

		//path = ereg_replace("^\\/+", "", path);
		//path = ereg_replace("\\/+$", "", path);
		String[] arraypath = path.split("/");
		String tmppath = "";
		for (int i = 0; i < arraypath.length; i++)
		{
			tmppath += "/" + arraypath[i];
			File d = new File(tmppath.substring(1));
			if (!d.exists())
			{ //检查Sub目录是否存在
				System.out.println(tmppath.substring(1));
				if (!d.mkdir())
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 读取 UTF-8 文件全部内容
	 * 
	 * @param f
	 *            文件
	 * @return 文件内容
	 */
	public static String read(File f)
	{
		try
		{
			return StreamUtils.read(StreamUtils.fileInr(f)).toString();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 文件读写流
	 */
	/**
	 * 读取 UTF-8 文件全部内容
	 * 
	 * @param path
	 *            文件路径
	 * @return 文件内容
	 */
	public static String read(String path)
	{
		return null;
	}

	/**
	 * 将文件改名
	 * 
	 * @param src
	 *            文件
	 * @param newName
	 *            新名称
	 * @return 改名是否成功
	 */
	public static boolean rename(File src, String newName)
	{
		if (src == null || newName == null)
			return false;
		if (src.exists())
		{
			File newFile = new File(src.getParent() + "/" + newName);
			if (newFile.exists())
				return false;
			makeDir(newFile.getParentFile());
			return src.renameTo(newFile);
		}
		return false;
	}

	/**
	 * 修改路径
	 * 
	 * @param path
	 *            路径
	 * @param newName
	 *            新名称
	 * @return 新路径
	 */
	public static String renamePath(String path, String newName)
	{
		if (!StringUtils.isBlank(path))
		{
			int pos = path.replace('\\', '/').lastIndexOf('/');
			if (pos > 0)
				return path.substring(0, pos) + "/" + newName;
		}
		return newName;
	}

	/**
	 * 在文件尾追加数据(不关闭流)
	 * 
	 * @param file
	 * @param tempByte
	 * @return
	 */
	public static RandomAccessFile skipFile(File file, byte[] tempByte)
	{
		if (!isFile(file) || tempByte == null)
		{
			System.out.println("文件不存在，或者byte字符串为null");
			return null;
		}
		// 打开一个随机访问文件流，按读写方式 如果该文件尚不存在，则尝试创建该文件
		// "rw"可以查aip知道意思 读写的意思
		RandomAccessFile randomFile = null;
		try
		{
			randomFile = new RandomAccessFile(file, "rw");
			skipFile(randomFile, tempByte, randomFile.length());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return randomFile;
	}

	/**
	 * 跳到某个地方添加数据不关闭流
	 * 
	 * @param file
	 * @param tempByte
	 * @param seek
	 *            =跳转到的字节数
	 * @return
	 */
	public static RandomAccessFile skipFile(File file, byte[] tempByte, long seek)
	{
		if (!isFile(file) || tempByte == null)
		{
			System.out.println("文件不存在，或者byte字符串为null");
			return null;
		}
		// 打开一个随机访问文件流，按读写方式 如果该文件尚不存在，则尝试创建该文件
		// "rw"可以查aip知道意思 读写的意思
		RandomAccessFile randomFile = null;
		try
		{
			randomFile = new RandomAccessFile(file, "rw");
			skipFile(randomFile, tempByte, seek);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return randomFile;
	}

	/**
	 * 跳到某个地方添加数据不关闭流
	 * 
	 * @param randomFile
	 * @param tempByte
	 * @param seek跳转到的地方
	 * @return
	 */
	public static RandomAccessFile skipFile(RandomAccessFile randomFile, byte[] tempByte, long seek)
	{
		try
		{
			// 将写文件指针移到文件尾。
			randomFile.seek(seek);
			randomFile.write(tempByte);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return randomFile;
	}

	/**
	 * 在文件尾追加数据(不关闭流)
	 * 
	 * @param fileName
	 * @param tempByte
	 */
	public static RandomAccessFile skipFile(String fileName, byte[] tempByte)
	{
		return skipFile(new File(fileName), tempByte);
	}

	public static RandomAccessFile skipFile(String fileName, byte[] tempByte, long seek)
	{
		return skipFile(new File(fileName), tempByte, seek);
	}

	/**
	 * 在文件尾追加数据(关闭流)
	 * 
	 * @param file
	 * @param tempByte
	 */
	public static boolean skipFileAndClose(File file, byte[] tempByte)
	{
		return IOUtils.close(skipFile(file, tempByte));
	}

	public static boolean skipFileAndClose(File file, byte[] tempByte, long seek)
	{
		return IOUtils.close(skipFile(file, tempByte, seek));
	}

	/**
	 * 跳到某个地方添加数据关闭流
	 * 
	 * @param randomFile
	 * @param tempByte
	 * @param seek
	 */
	public static boolean skipFileAndClose(RandomAccessFile randomFile, byte[] tempByte, long seek)
	{
		return IOUtils.close(skipFile(randomFile, tempByte, seek));
	}

	/**
	 * 在文件尾追加数据(关闭流)
	 * 
	 * @param path
	 * @param tempByte
	 */
	public static boolean skipFileAndClose(String path, byte[] tempByte)
	{
		return skipFileAndClose(new File(path), tempByte);
	}

	public static boolean skipFileAndClose(String fileName, byte[] tempByte, long seek)
	{
		return IOUtils.close(skipFile(new File(fileName), tempByte, seek));
	}

	/**
	 * 修改文件的最后访问时间。 如果文件不存在则创建该文件。
	 * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	 * 
	 * @param file
	 *            需要修改最后访问时间的文件。
	 * @since 1.0
	 */
	public static void touch(File file)
	{
		long currentTime = System.currentTimeMillis();
		if (!file.exists())
		{
			System.err.println("file not found:" + file.getName());
			System.err.println("Create a new file:" + file.getName());
			try
			{
				if (file.createNewFile())
				{
					System.out.println("Succeeded!");
				}
				else
				{
					System.err.println("Create file failed!");
				}
			}
			catch (IOException e)
			{
				System.err.println("Create file failed!");
				e.printStackTrace();
			}
		}
		boolean result = file.setLastModified(currentTime);
		if (!result)
		{
			System.err.println("touch failed: " + file.getName());
		}
	}

	/**
	 * 修改文件的最后访问时间。 如果文件不存在则创建该文件。
	 * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	 * 
	 * @param files
	 *            需要修改最后访问时间的文件数组。
	 * @since 1.0
	 */
	public static void touch(File[] files)
	{
		for (int i = 0; i < files.length; i++)
		{
			touch(files[i]);
		}
	}

	/**
	 * 修改文件的最后访问时间。 如果文件不存在则创建该文件。
	 * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	 * 
	 * @param fileName
	 *            需要修改最后访问时间的文件的文件名。
	 * @since 1.0
	 */
	public static void touch(String fileName)
	{
		File file = new File(fileName);
		touch(file);
	}

	/**
	 * 修改文件的最后访问时间。 如果文件不存在则创建该文件。
	 * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	 * 
	 * @param fileNames
	 *            需要修改最后访问时间的文件名数组。
	 * @since 1.0
	 */
	public static void touch(String[] fileNames)
	{
		File[] files = new File[fileNames.length];
		for (int i = 0; i < fileNames.length; i++)
		{
			files[i] = new File(fileNames[i]);
		}
		touch(files);
	}

	/**
	 * 将DOS/Windows格式的路径转换为UNIX/Linux格式的路径。
	 * 其实就是将路径中的"\"全部换为"/"，因为在某些情况下我们转换为这种方式比较方便，
	 * 某中程度上说"/"比"\"更适合作为路径分隔符，而且DOS/Windows也将它当作路径分隔符。
	 * 
	 * @param filePath
	 *            转换前的路径
	 * @return 转换后的路径
	 * @since 1.0
	 */
	public static String toUNIXpath(String filePath)
	{
		return filePath.replace('\\', '/');
	}

	/**
	 * 将文件名中的类型部分去掉。
	 * 
	 * @param filename
	 *            文件名
	 * @return 去掉类型部分的结果
	 * @since 1.0
	 */
	public static String trimType(String filename)
	{
		int index = filename.lastIndexOf(".");
		if (index != -1)
		{
			return filename.substring(0, index);
		}
		else
		{
			return filename;
		}
	}

	/**
	 * 将内容写到一个文件内，内容对象可以是：
	 * 
	 * <ul>
	 * <li>InputStream - 按二进制方式写入
	 * <li>byte[] - 按二进制方式写入
	 * <li>Reader - 按 UTF-8 方式写入
	 * <li>其他对象被 toString() 后按照 UTF-8 方式写入
	 * </ul>
	 * 
	 * @param f
	 *            文件
	 * @param obj
	 *            内容
	 */
	public static void write(File f, Object obj)
	{
		if (null == f || null == obj)
			return;
		if (f.isDirectory())
			throw ThrowableUtils.makeThrow("Directory '%s' can not be write as File", f);

		try
		{
			// 保证文件存在
			if (!f.exists())
				FileUtils.createNewFile(f);
			// 输入流
			if (obj instanceof InputStream)
			{
				StreamUtils.writeAndClose(StreamUtils.fileOut(f), (InputStream) obj);
			}
			// 字节数组
			else if (obj instanceof byte[])
			{
				StreamUtils.writeAndClose(StreamUtils.fileOut(f), (byte[]) obj);
			}
			// 文本输入流
			else if (obj instanceof Reader)
			{
				StreamUtils.writeAndClose(StreamUtils.fileOutw(f), (Reader) obj);
			}
			// 其他对象
			else
			{
				StreamUtils.writeAndClose(StreamUtils.fileOutw(f), obj.toString());
			}
		}
		catch (IOException e)
		{
			throw ThrowableUtils.wrapThrow(e);
		}
	}

	/**
	 * 将内容写到一个文件内，内容对象可以是：
	 * <ul>
	 * <li>InputStream - 按二进制方式写入
	 * <li>byte[] - 按二进制方式写入
	 * <li>Reader - 按 UTF-8 方式写入
	 * <li>其他对象被 toString() 后按照 UTF-8 方式写入
	 * </ul>
	 * 
	 * @param path
	 *            文件路径，如果不存在，则创建
	 * @param obj
	 *            内容对象
	 */
	public static void write(String path, Object obj)
	{
		if (null == path || null == obj)
			return;
		try
		{
			write(FileUtils.createFileIfNoExists(path), obj);
		}
		catch (IOException e)
		{
			throw ThrowableUtils.wrapThrow(e);
		}
	}

	// 移动
}