package pmis.commons.codec;

import java.nio.charset.Charset;

public abstract class EncodingUtils
{
	public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

	public static final String UTF8 = "UTF-8";

	public static String defaultEncoding()
	{
		return Charset.defaultCharset().name();
	}
}
