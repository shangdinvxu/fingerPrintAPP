package com.fgtit.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * IP?????????
 * IP util class
 */
public class IpUtils {
    /**
     * ???IP?? ?
     * get IP address
     * @return
     */
    public static String getIpAddress() {

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ???WIFI??ip???
     * get IP address of wifi
     * @param context
     * @return
     */
    public static String getWifiIp(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
//        DhcpInfo dhcp = wifi.getDhcpInfo();
//        String mask = getIpStringByint(dhcp.netmask);
//        String gateWay = getIpStringByint(dhcp.gateway);
//        L.d("ytmfdw", "????" + mask);
//        L.d("ytmfdw", "?????" + gateWay);
        // ???IP???????????
        int ipAddress = info.getIpAddress();
        String ipString = "";
        ipString = getIpStringByint(ipAddress);
        return ipString;
    }

    /**
     * ???????
     * get gateway
     * @param context
     * @return
     */
    public static String getGateWay(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        DhcpInfo dhcp = wifi.getDhcpInfo();
        return getIpStringByint(dhcp.gateway);
    }

    /**
     * ???????
     * get netMask
     * @param context
     * @return
     */
    public static String getNetMask(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        DhcpInfo dhcp = wifi.getDhcpInfo();
        return getIpStringByint(dhcp.netmask);
    }

    /**
     * ???MAC???
     * get MAC address
     * @param context
     * @return
     */
    public String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }


    /**
     * ??????WIFI??
     * get current status of wifi
     * @param context
     * @return
     */
    public static String getCurrentNetType(Context context) {
        String type = "";
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            type = "null";
        } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            type = "wifi";
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int subType = info.getSubtype();
            if (subType == TelephonyManager.NETWORK_TYPE_CDMA || subType == TelephonyManager.NETWORK_TYPE_GPRS
                    || subType == TelephonyManager.NETWORK_TYPE_EDGE) {
                type = "2g";
            } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS || subType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_A || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
                type = "3g";
            } else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE??3g??4g????????3.9G???????
                type = "4g";
            }
        }
        return type;
    }

    /**
     * ??ip???????????????????
     *
     * @param ipAddress
     * @return
     */
    public static String getIpStringByint(int ipAddress) {
        String ipString = "";
        if (ipAddress != 0) {
            ipString = ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
                    + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
        }
//        ipString = Formatter.formatIpAddress(ipAddress);
        return ipString;
    }
}
