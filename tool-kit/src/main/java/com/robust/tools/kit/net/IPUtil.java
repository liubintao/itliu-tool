package com.robust.tools.kit.net;

import com.google.common.net.InetAddresses;
import com.robust.tools.kit.number.NumberUtil;
import com.robust.tools.kit.text.StringUtil;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @Description: InetAddress工具类，基于Guava的InetAddresses.
 * <p>
 * 主要包含int, String/IPV4String, InetAdress/Inet4Address之间的互相转换
 * <p>
 * 先将字符串传换为byte[]再用InetAddress.getByAddress(byte[])，避免了InetAddress.getByName(ip)可能引起的DNS访问.
 * <p>
 * InetAddress与String的转换其实消耗不小，如果是有限的地址，建议进行缓存.
 * @Author: robust
 * @CreateDate: 2019/8/19 17:22
 * @Version: 1.0
 */
public class IPUtil {

    /**
     * 从InetAddress转化到int，传输和存储时，用int代表InetAddress是最小的开销.
     * InetAddress可以是IPV4或IPV6，都会转化为IPV4.
     *
     * @see com.google.common.net.InetAddresses#coerceToInteger(InetAddress)
     */
    public static int toInt(InetAddress inetAddress) {
        return InetAddresses.coerceToInteger(inetAddress);
    }

    /**
     * InetAddress转换为String.
     * InetAddress可以是IPV4或IPV6,其中IPV4直接调用getHostAddress.
     *
     * @see InetAddresses#toAddrString(InetAddress)
     */

    public static String toIpString(InetAddress address) {
        return InetAddresses.toAddrString(address);
    }

    /**
     * 从int转换为Inet4Address(仅支持IPV4)
     *
     * @see InetAddresses#fromInteger(int)
     */
    public static Inet4Address fromInt(int address) {
        return InetAddresses.fromInteger(address);
    }

    /**
     * 从String转换为InetAddress.
     * IpString 可以是IPV4/IPV6 String, 但不可以是域名
     * 先将字符串转换为byte[], 再调用getByAddress(byte[]), 避免了调用{@link InetAddress#getByName(String)}可能引起的DNS访问.
     */
    public static InetAddress fromIpString(String ip) {
        return InetAddresses.forString(ip);
    }

    /**
     * 从IPV4String转换为InetAddress.
     * IpString如果确定ipv4, 使用本方法减少字符分析消耗 .
     * 先将字符串转换为byte[], 再调用getByAddress(byte[]), 避免了调用{@link InetAddress#getByName(String)}可能引起的DNS访问.
     */
    public static Inet4Address fromIpv4String(String ip) {
        byte[] byteAddress = ipv4StringToBytes(ip);
        if (byteAddress == null) {
            return null;
        } else {
            try {
                return (Inet4Address) Inet4Address.getByAddress(byteAddress);
            } catch (UnknownHostException e) {
                throw new AssertionError(e);
            }
        }
    }

    /**
     * int转换到IPV4 String, from Netty NetUtil.
     */
    public static String intToIpv4String(int i) {
        return new StringBuilder(15).append((i >> 24) & 0xff).append('.').append((i >> 16) & 0xff).append('.')
                .append((i >> 8) & 0xff).append('.').append(i & 0xff).toString();
    }

    /**
     * Ipv4 String转换为int
     */
    public static int ipv4StringToInt(String ip) {
        byte[] byteAddress = ipv4StringToBytes(ip);
        if (byteAddress == null) {
            return 0;
        } else {
            return NumberUtil.toInt(byteAddress);
        }
    }

    /**
     * Ipv4 String转换为byte[]
     */
    public static byte[] ipv4StringToBytes(String ip) {
        if (StringUtil.isBlank(ip)) {
            return null;
        }
        List<String> it = StringUtil.split(ip, '.', 4);
        if (it != null && it.size() != 4) {
            return null;
        }

        byte[] byteAddress = new byte[4];
        for (int i = 0; i < 4; i++) {
            int tempInt = Integer.valueOf(it.get(i));
            if (tempInt > 255) {
                return null;
            }
            byteAddress[i] = (byte) tempInt;
        }
        return byteAddress;
    }
}
