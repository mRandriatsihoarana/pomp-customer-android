package fr.pomp.adfuell.utils.munix;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by munix on 07/04/16.
 */
public class Connection {

    /**
     * Comprueba si tenemos conexión a internet
     *
     * @param context
     * @return true si está conectado a internet, false en caso contrario
     */
    public static Boolean isConnected( Context context ) {
        try {
            ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
            if ( conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo()
                    .isAvailable() && conMgr.getActiveNetworkInfo().isConnected() ) {
                return true;
            } else {
                return false;
            }
        } catch ( Exception e ) {
            return false;
        }
    }

    public static Boolean isConnected() {
        return isConnected( MunixUtilities.context );
    }

    public static String getConnectionType( Context mContext ) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService( Context.CONNECTIVITY_SERVICE );
        String connection_type = "";
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for ( NetworkInfo ni : netInfo ) {
            if ( ni.getTypeName()
                    .equalsIgnoreCase( "WIFI" ) && ni.isAvailable() && ni.isConnected() ) {
                connection_type = "wifi";
            }
            if ( ni.getTypeName()
                    .equalsIgnoreCase( "MOBILE" ) && ni.isAvailable() && ni.isConnected() ) {
                connection_type = "mobile";
            }
        }
        return connection_type;
    }

    public static String getConnectionType() {
        return getConnectionType( MunixUtilities.context );
    }
}
