package com.example.spontan;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class ConnectivityMonitor {

    public static boolean isNetworkConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info= cm.getActiveNetworkInfo();
        return (info!=null && info.isConnected());
    }

    public static String getConnectionSpeed(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info= cm.getActiveNetworkInfo();

        if (info!=null && info.isConnected()) {
            return ConnectivityMonitor.connectionType(info.getType(), info.getSubtype());
        }
        else
            return "NO CONNECTION";

    }

    private static String connectionType(int type, int subtype) {
        if (type ==  ConnectivityManager.TYPE_WIFI){
            return "FAST";
        }
        else if(type == ConnectivityManager.TYPE_MOBILE){
            switch (subtype){
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                    return "SLOW";
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return "FAST";
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    return "UNKNOWN";
                default:
                    return "N.A.";
            }
        }
        else {
            return "N.A.";
        }

    }

}
