package pmis.commons.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PushbackInputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import pmis.commons.codec.EncodingUtils;
import pmis.commons.compress.JarEntryInfo;
import pmis.commons.compress.JarEntryResource;
import pmis.commons.compress.NutResource;
import pmis.commons.lang.ThrowableUtils;

/**
 * 流的工具类
 * 
 * @author wuwh
 */
public abstract class StreamUtils
{
	private static final int BUF_SIZE = 8192;

	private static final byte[] UTF_BOM = new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };

	/**
	 * 获取File对象输入流,即使在Jar文件中一样工作良好!! <b>强烈推荐</b>
	 * 
	 */
	protected static InputStream _input(File file) throws IOException
	{
		if (file.exists())
		{
			return new FileInputStream(file);
		}
		if (isInJar(file))
		{
			NutResource nutResource = makeJarNutResource(file);
			if (nutResource != null)
			{
				return nutResource.getInputStream();
			}
		}
		return null;
	}

	/**
	 * 为一个输入流包裹一个缓冲流。如果这个输入流本身就是缓冲流，则直接返回
	 * 
	 * @param ins
	 *            输入流。
	 * @return 缓冲输入流
	 */
	public static BufferedInputStream buff(InputStream ins)
	{
		if (ins instanceof BufferedInputStream)
			return (BufferedInputStream) ins;
		return new BufferedInputStream(ins);
	}

	/**
	 * 为一个输出流包裹一个缓冲流。如果这个输出流本身就是缓冲流，则直接返回
	 * 
	 * @param ops
	 *            输出流。
	 * @return 缓冲输出流
	 */
	public static BufferedOutputStream buff(OutputStream ops)
	{
		if (ops instanceof BufferedOutputStream)
			return (BufferedOutputStream) ops;
		return new BufferedOutputStream(ops);
	}

	/**
	 * 为一个文本输入流包裹一个缓冲流。如果这个输入流本身就是缓冲流，则直接返回
	 * 
	 * @param reader
	 *            文本输入流。
	 * @return 缓冲文本输入流
	 */
	public static BufferedReader buffr(Reader reader)
	{
		if (reader instanceof BufferedReader)
			return (BufferedReader) reader;
		return new BufferedReader(reader);
	}

	/**
	 * 为一个文本输出流包裹一个缓冲流。如果这个文本输出流本身就是缓冲流，则直接返回
	 * 
	 * @param ops
	 *            文本输出流。
	 * @return 缓冲文本输出流
	 */
	public static BufferedWriter buffw(Writer ops)
	{
		if (ops instanceof BufferedWriter)
			return (BufferedWriter) ops;
		return new BufferedWriter(ops);
	}

	/**
	 * 判断两个输入流是否严格相等
	 */
	public static boolean equals(InputStream sA, InputStream sB) throws IOException
	{
		int dA;
		while ((dA = sA.read()) != -1)
		{
			if (dA != sB.read())
				return false;
		}
		return sB.read() == -1;
	}

	/**
	 * 根据一个文件路径建立一个输入流
	 * 
	 * @param file
	 *            文件
	 * @return 输入流
	 */
	public static InputStream fileIn(File file)
	{
		try
		{
			return buff(StreamUtils._input(file));
		}
		catch (IOException e)
		{
			throw ThrowableUtils.wrapThrow(e);
		}
	}

	/**
	 * 根据一个文件路径建立一个输入流
	 * 
	 * @param path
	 *            文件路径
	 * @return 输入流
	 */
	public static InputStream fileIn(String path)
	{
		InputStream ins = FileUtils.findFileAsStream(path);
		if (null == ins)
		{
			File f = FileUtils.findFile(path);
			if (null != f)
				try
				{
					ins = StreamUtils._input(f);
				}
				catch (IOException e)
				{
				}
		}
		return buff(ins);
	}

	/**
	 * 根据一个文件路径建立一个 UTF-8 文本输入流 <b>警告!! 本方法会预先读取3个字节以判断该文件是否存在BOM头</b>
	 * <p/>
	 * <b>警告!! 如果存在BOM头,则自动跳过</b>
	 * <p/>
	 * 
	 * @param file
	 *            文件
	 * @return 文本输入流
	 */
	public static Reader fileInr(File file)
	{
		return utf8r(fileIn(file));
	}

	/**
	 * 根据一个文件路径建立一个 UTF-8文本输入流 <b>警告!! 本方法会预先读取3个字节以判断该文件是否存在BOM头</b>
	 * <p/>
	 * <b>警告!! 如果存在BOM头,则自动跳过</b>
	 * <p/>
	 * 
	 * @param path
	 *            文件路径
	 * @return 文本输入流
	 */
	public static Reader fileInr(String path)
	{
		return utf8r(fileIn(path));
	}

	/**
	 * 根据一个文件建立一个输出流
	 * 
	 * @param file
	 *            文件
	 * @return 输出流
	 */
	public static OutputStream fileOut(File file)
	{
		try
		{
			return buff(new FileOutputStream(file));
		}
		catch (FileNotFoundException e)
		{
			throw ThrowableUtils.wrapThrow(e);
		}
	}

	/**
	 * 根据一个文件路径建立一个输出流
	 * 
	 * @param path
	 *            文件路径
	 * @return 输出流
	 */
	public static OutputStream fileOut(String path)
	{
		return fileOut(new File(path));
	}

	/**
	 * 根据一个文件建立一个 UTF-8 文本输出流
	 * 
	 * @param file
	 *            文件
	 * @return 输出流
	 */
	public static Writer fileOutw(File file)
	{
		return utf8w(fileOut(file));
	}

	/**
	 * 根据一个文件路径建立一个 UTF-8 文本输出流
	 * 
	 * @param path
	 *            文件路径
	 * @return 文本输出流
	 */
	public static Writer fileOutw(String path)
	{
		return fileOutw(new File(path));
	}

	public static boolean isInJar(File file)
	{
		return isInJar(file.getAbsolutePath());
	}

	public static boolean isInJar(String filePath)
	{
		return filePath.contains(".jar!");
	}

	public static NutResource makeJarNutResource(File file)
	{
		return makeJarNutResource(file.getAbsolutePath());
	}

	public static NutResource makeJarNutResource(String filePath)
	{
		JarEntryInfo jeInfo = new JarEntryInfo(filePath);
		try
		{
			JarFile jar = new JarFile(jeInfo.getJarPath());
			JarEntry entry = jar.getJarEntry(jeInfo.getEntryName());
			if (entry != null)
			{
				// JDK里面判断实体是否为文件夹的方法非常不靠谱 by jon
				if (entry.getName().endsWith("/"))// 明显是文件夹
					return null;
				JarEntry e2 = jar.getJarEntry(jeInfo.getEntryName() + "/");
				if (e2 != null) // 加个/,还是能找到?! 那肯定是文件夹了!
					return null;
				return new JarEntryResource(jeInfo);
			}
		}
		catch (IOException e)
		{
		}
		return null;
	}

	public static InputStream nullInputStream()
	{
		return new NullInputStream();
	}

	/**
	 * 从一个文本流中读取全部内容并返回
	 * <p>
	 * <b style=color:red>注意</b>，它并不会关闭输出流
	 * 
	 * @param reader
	 *            文本输出流
	 * @return 文本内容
	 * @throws IOException
	 */
	public static StringBuilder read(Reader reader) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		char[] cbuf = new char[BUF_SIZE];
		int len;
		while (-1 != (len = reader.read(cbuf)))
		{
			sb.append(cbuf, 0, len);
		}
		return sb;
	}

	/**
	 * 从一个文本流中读取全部内容并返回
	 * <p>
	 * <b style=color:red>注意</b>，它会关闭输入流
	 * 
	 * @param reader
	 *            文本输入流
	 * @return 文本内容
	 * @throws IOException
	 */
	public static String readAndClose(Reader reader)
	{
		try
		{
			return read(reader).toString();
		}
		catch (IOException e)
		{
			throw ThrowableUtils.wrapThrow(e);
		}
		finally
		{
			safeClose(reader);
		}
	}

	/**
	 * 关闭一个可关闭对象，可以接受 null。如果成功关闭，返回 true，发生异常 返回 false
	 * 
	 * @param cb
	 *            可关闭对象
	 * @return 是否成功关闭
	 */
	public static boolean safeClose(Closeable cb)
	{
		if (null != cb)
			try
			{
				cb.close();
			}
			catch (IOException e)
			{
				return false;
			}
		return true;
	}

	public static void safeFlush(Flushable fa)
	{
		if (null != fa)
			try
			{
				fa.flush();
			}
			catch (IOException e)
			{
			}
	}

	/**
	 * 判断并移除UTF-8的BOM头
	 */
	public static InputStream utf8filte(InputStream in)
	{
		try
		{
			if (in.available() == -1)
				return in;
			PushbackInputStream pis = new PushbackInputStream(in, 3);
			byte[] header = new byte[3];
			int len = pis.read(header, 0, 3);
			if (len < 1)
				return in;
			if (header[0] != UTF_BOM[0] || header[1] != UTF_BOM[1] || header[2] != UTF_BOM[2])
			{
				pis.unread(header, 0, len);
			}
			return pis;
		}
		catch (IOException e)
		{
			throw ThrowableUtils.wrapThrow(e);
		}
	}

	public static Reader utf8r(InputStream is)
	{
		return new InputStreamReader(utf8filte(is), EncodingUtils.CHARSET_UTF8);
	}

	public static Writer utf8w(OutputStream os)
	{
		return new OutputStreamWriter(os, EncodingUtils.CHARSET_UTF8);
	}

	/**
	 * 将一个字节数组写入一个输出流。
	 * <p>
	 * <b style=color:red>注意</b>，它并不会关闭输出流
	 * 
	 * @param ops
	 *            输出流
	 * @param bytes
	 *            字节数组
	 * @throws IOException
	 */
	public static void write(OutputStream ops, byte[] bytes) throws IOException
	{
		if (null == ops || null == bytes)
			return;
		ops.write(bytes);
	}

	/**
	 * 将输出流写入一个输出流。块大小为 8192
	 * <p>
	 * <b style=color:red>注意</b>，它并不会关闭输入/出流
	 * 
	 * @param ops
	 *            输出流
	 * @param ins
	 *            输入流
	 * 
	 * @return 写入的字节数
	 * @throws IOException
	 */
	public static int write(OutputStream ops, InputStream ins) throws IOException
	{
		return write(ops, ins, BUF_SIZE);
	}

	/**
	 * 将输出流写入一个输出流。
	 * <p>
	 * <b style=color:red>注意</b>，它并不会关闭输入/出流
	 * 
	 * @param ops
	 *            输出流
	 * @param ins
	 *            输入流
	 * @param bufferSize
	 *            缓冲块大小
	 * 
	 * @return 写入的字节数
	 * 
	 * @throws IOException
	 */
	public static int write(OutputStream ops, InputStream ins, int bufferSize) throws IOException
	{
		if (null == ops || null == ins)
			return 0;

		byte[] buf = new byte[bufferSize];
		int len;
		int re = 0;
		while (-1 != (len = ins.read(buf)))
		{
			re += len;
			ops.write(buf, 0, len);
		}
		return re;
	}

	/**
	 * 将一段文本全部写入一个writer。
	 * <p>
	 * <b style=color:red>注意</b>，它并不会关闭输出流
	 * 
	 * @param writer
	 * 
	 * @param cs
	 *            文本
	 * @throws IOException
	 */
	public static void write(Writer writer, CharSequence cs) throws IOException
	{
		if (null != cs && null != writer)
		{
			writer.write(cs.toString());
			writer.flush();
		}
	}

	/**
	 * 将文本输出流写入一个文本输出流。块大小为 8192
	 * <p>
	 * <b style=color:red>注意</b>，它并不会关闭输入/出流
	 * 
	 * @param writer
	 *            输出流
	 * @param reader
	 *            输入流
	 * @throws IOException
	 */
	public static void write(Writer writer, Reader reader) throws IOException
	{
		if (null == writer || null == reader)
			return;

		char[] cbuf = new char[BUF_SIZE];
		int len;
		while (-1 != (len = reader.read(cbuf)))
		{
			writer.write(cbuf, 0, len);
		}
	}

	/**
	 * 将一个字节数组写入一个输出流。
	 * <p>
	 * <b style=color:red>注意</b>，它会关闭输出流
	 * 
	 * @param ops
	 *            输出流
	 * @param bytes
	 *            字节数组
	 */
	public static void writeAndClose(OutputStream ops, byte[] bytes)
	{
		try
		{
			write(ops, bytes);
		}
		catch (IOException e)
		{
			throw ThrowableUtils.wrapThrow(e);
		}
		finally
		{
			safeClose(ops);
		}
	}

	/**
	 * 将输出流写入一个输出流。块大小为 8192
	 * <p>
	 * <b style=color:red>注意</b>，它会关闭输入/出流
	 * 
	 * @param ops
	 *            输出流
	 * @param ins
	 *            输入流
	 * @return 写入的字节数
	 */
	public static int writeAndClose(OutputStream ops, InputStream ins)
	{
		try
		{
			return write(ops, ins);
		}
		catch (IOException e)
		{
			throw ThrowableUtils.wrapThrow(e);
		}
		finally
		{
			safeClose(ops);
			safeClose(ins);
		}
	}

	/**
	 * 将一段文本全部写入一个writer。
	 * <p>
	 * <b style=color:red>注意</b>，它会关闭输出流
	 * 
	 * @param writer
	 *            输出流
	 * @param cs
	 *            文本
	 */
	public static void writeAndClose(Writer writer, CharSequence cs)
	{
		try
		{
			write(writer, cs);
		}
		catch (IOException e)
		{
			throw ThrowableUtils.wrapThrow(e);
		}
		finally
		{
			safeClose(writer);
		}
	}

	/**
	 * 将文本输出流写入一个文本输出流。块大小为 8192
	 * <p>
	 * <b style=color:red>注意</b>，它会关闭输入/出流
	 * 
	 * @param writer
	 *            输出流
	 * @param reader
	 *            输入流
	 */
	public static void writeAndClose(Writer writer, Reader reader)
	{
		try
		{
			write(writer, reader);
		}
		catch (IOException e)
		{
			throw ThrowableUtils.wrapThrow(e);
		}
		finally
		{
			safeClose(writer);
			safeClose(reader);
		}
	}
}