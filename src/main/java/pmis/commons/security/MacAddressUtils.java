package pmis.commons.security;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @ClassName: MacAddressUtil
 * @author: wuwh
 * @CreateDate: 2014-4-2 下午2:43:10
 * @UpdateUser: wuwh
 * @UpdateDate: 2014-4-2 下午2:43:10
 * @UpdateRemark: 说明本次修改内容
 * @Description: IP与mac地址的获取
 * @version: V1.0
 */
public abstract class MacAddressUtils
{
	private final static Logger logger = LoggerFactory.getLogger(MacAddressUtils.class);

	public static String hexByte(byte b)
	{

		String s = "000000" + Integer.toHexString(b);

		return s.substring(s.length() - 2);

	}

	public static String getIP()
	{
		String ipStr = "";
		InetAddress ip;
		try
		{

			ip = InetAddress.getLocalHost();
			ipStr = ip.getHostAddress();

		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();

		}
		return ipStr;
	}

	public static byte[] getMacAddress()
	{
		try
		{
			Enumeration<NetworkInterface> nwInterface = NetworkInterface.getNetworkInterfaces();
			while (nwInterface.hasMoreElements())
			{
				NetworkInterface nis = nwInterface.nextElement();
				if (nis != null)
				{
					byte[] mac = nis.getHardwareAddress();
					if (mac != null)
					{
						/*
						 * Extract each array of mac address and generate a
						 * hashCode for it
						 */
						return mac;//.hashCode();
					}
					else
					{
						logger.info("Address doesn't exist or is not accessible");
					}
				}
				else
				{
					logger.info("Network Interface for the specified address is not found.");
				}
				return null;
			}
		}
		catch (SocketException ex)
		{
			logger.info(null, ex);
		}
		return null;
	}

	public static String getMAC()
	{
		StringBuilder ipStr = new StringBuilder();
		InetAddress ip;
		try
		{

			ip = InetAddress.getLocalHost();
			System.out.println("Current IP address : " + ip.getHostAddress());

			NetworkInterface network = NetworkInterface.getByInetAddress(ip);

			byte[] mac = network.getHardwareAddress();
			//System.out.println("mac size====" + mac.length);
			for (int i = 0; i < mac.length; i++)
			{
				ipStr.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();

		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		return ipStr.toString();
	}

	public static void main(String[] args) throws Exception
	{
		//MacAddress mac = new MacAddress();
		System.out.println(MacAddressUtils.getIP());
		System.out.println(MacAddressUtils.getMAC());
		System.out.println("mac size===========>" + new String(MacAddressUtils.getMacAddress(), "UTF-8"));

	}

}
