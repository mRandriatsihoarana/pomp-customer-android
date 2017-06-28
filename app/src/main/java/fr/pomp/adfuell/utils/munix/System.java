package fr.pomp.adfuell.utils.munix;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.telephony.TelephonyManager;

import java.util.List;
import java.util.UUID;

/**
 * Created by munix on 07/04/16.
 */
public class System {

    /**
     * Comprueba si una aplicación está instalada
     *
     * @param context
     * @param packageName
     * @return true si el nombre del paquete está instalado en el dispositivo
     */
    public static Boolean isPackageInstalled( Context context, String packageName ) {
        try {
            @SuppressWarnings( "unused" ) ApplicationInfo info = context.getApplicationContext()
                    .getPackageManager()
                    .getApplicationInfo( packageName, 0 );
            return true;
        } catch ( PackageManager.NameNotFoundException e ) {
            return false;
        }
    }

    public static Boolean isPackageInstalled( String packageName ) {
        return isPackageInstalled( MunixUtilities.context, packageName );
    }

    public static String getLauncherActivity( Context context, String packageName ) {
        Intent intent = new Intent( Intent.ACTION_MAIN, null );
        intent.addCategory( Intent.CATEGORY_LAUNCHER );
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities( intent, 0 );
        for ( ResolveInfo info : infos ) {
            if ( info.activityInfo.packageName.equals( packageName ) ) {
                return info.activityInfo.name;
            }
        }
        return null;
    }

    public static String getLauncherActivity( String packageName ) {
        return getLauncherActivity( MunixUtilities.context, packageName );
    }

    public static void exit( int code ) {
        java.lang.System.exit( code );
    }

    public static long currentTimeMillis() {
        return java.lang.System.currentTimeMillis();
    }

    public static String getUniqueDeviceId( Context context ) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService( Context.TELEPHONY_SERVICE );
        String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString( context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID );

        UUID deviceUuid = new UUID( androidId.hashCode(), ( (long) tmDevice.hashCode() << 32 ) | tmSerial
                .hashCode() );
        String deviceId = deviceUuid.toString();
        return deviceId;
    }

    public static String getUniqueDeviceId() {
        return getUniqueDeviceId( MunixUtilities.context );
    }
}
