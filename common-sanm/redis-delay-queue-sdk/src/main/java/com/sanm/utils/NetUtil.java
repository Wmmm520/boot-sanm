package com.sanm.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Author: Sanm
 * since: v1.0
 * description: 网络工具
 **/
public class NetUtil {

    //查阅了好多资料 这个是最正确的ip拿法  即优先拿site-local地址
    public static String getLocalHostLANAddress() {
        try {
            InetAddress inetAddress = null;
            //遍历所有的网络接口
            for (Enumeration enumerations = NetworkInterface.getNetworkInterfaces(); enumerations.hasMoreElements(); ) {
                NetworkInterface enumeration = (NetworkInterface) enumerations.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = enumeration.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr.getHostAddress();
                        } else if (inetAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            inetAddress = inetAddr;
                        }
                    }
                }
            }
            if (inetAddress != null) {
                return inetAddress.getHostAddress();
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress.getHostAddress();
        } catch (Exception e) {
            e.getStackTrace();
            return null;
        }
    }
}
