package fr.pomp.adfuell.utils.edena;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import fr.pomp.adfuell.utils.munix.SimpleToast;

import static fr.pomp.adfuell.utils.edena.EDEventBus.EVENT_NETWORK;

/**
 * Created by edena on 23/01/2017.
 * manifest
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 *  <receiver android:name=".utils.edena.EDNetworkReceiver">
 *  <_intent-filter>
 *  <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
 *  <action android:name="android.net.wifi.WIFI_STATE_CHANGED" />
 *  </_intent-filter>
 *  </receiver>
 */

public class EDNetworkReceiver extends BroadcastReceiver {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        //getConnectivityStatusString(context);
        EDEventBus.getInstance(context).post(new EDEventBus(EVENT_NETWORK,getConnectivityStatus(context)));
    }

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = getConnectivityStatus(context);
        String status = null;
        if (conn == TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        if(status != null)
            SimpleToast.showLong(status);
        return status;
    }

}
