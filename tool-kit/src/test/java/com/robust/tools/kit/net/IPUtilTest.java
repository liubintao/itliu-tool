package com.robust.tools.kit.net;

import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/20 11:52
 * @Version: 1.0
 */
public class IPUtilTest {

    @Test
    public void test() throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        int i = IPUtil.toInt(address);
        System.out.println(i);
        System.out.println(IPUtil.fromInt(i).getHostAddress());
        System.out.println(IPUtil.toIpString(address));
        address = IPUtil.fromIpString("192.168.100.1");
        System.out.println(address.getHostAddress());

        address = IPUtil.fromIpv4String("192.168.100.2");
        System.out.println(address.getHostAddress());

        System.out.println(IPUtil.intToIpv4String(i));

        i = IPUtil.ipv4StringToInt(address.getHostAddress());
        System.out.println(i);

        System.out.println(IPUtil.intToIpv4String(i));
    }
}