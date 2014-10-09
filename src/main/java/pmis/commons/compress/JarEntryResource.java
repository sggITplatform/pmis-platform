package pmis.commons.compress;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 
 * @ClassName: JarEntryResource
 * @author: wuwh
 * @CreateDate: 2014-3-28 下午5:59:50
 * @UpdateUser: wuwh
 * @UpdateDate: 2014-3-28 下午5:59:50
 * @UpdateRemark: 说明本次修改内容
 * @Description: 封装了 jar 内的 Entity
 * @version: V1.0
 */
public class JarEntryResource extends NutResource
{
	private JarEntry entry;

	private JarFile jar;

	public JarEntryResource(JarEntryInfo jeInfo) throws IOException
	{
		this.jar = new JarFile(jeInfo.getJarPath());
		this.entry = jar.getJarEntry(jeInfo.getEntryName());
		if (null == this.entry)
			throw new IOException("Invalid JarEntry :" + jeInfo);
		this.name = jeInfo.getEntryName();
	}

	public JarEntryResource(JarFile jar, JarEntry jen, String name)
	{
		this.jar = jar;
		this.entry = jen;
		this.name = name;
	}

	public JarEntry getEntry()
	{
		return entry;
	}

	@Override
	public InputStream getInputStream() throws IOException
	{
		return jar.getInputStream(entry);
	}

	public JarFile getJar()
	{
		return jar;
	}

}
