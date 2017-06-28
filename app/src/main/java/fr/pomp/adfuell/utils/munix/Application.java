package fr.pomp.adfuell.utils.munix;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by munix on 07/04/16.
 */
public class Application {

    /**
     * Elimina todos los datos de la aplicación
     *
     * @param context
     */
    public static void clearApplicationData( Context context ) {
        if ( context.getApplicationContext() != null ) {
            File cache = context.getApplicationContext().getCacheDir();
            File appDir = new File( cache.getParent() );
            if ( appDir.exists() ) {
                String[] children = appDir.list();
                for ( String s : children ) {
                    File f = new File( appDir, s );
                    Files.deleteDirectoryRecursive( f );
                }
            }
        }
    }

    /**
     * Elimina todos los datos de la aplicación
     */
    public static void clearApplicationData() {
        clearApplicationData( MunixUtilities.context );
    }


    /**
     * Elimina al cache interna de la app <b>/data/data/com.xxx.xxx/cache</b>
     *
     * @param context
     * @return
     * @since 2.0.5
     */
    public static boolean cleanInternalCache( Context context ) {
        return Files.deleteDirectoryRecursive( context.getCacheDir() );
    }

    /**
     * Elimina al cache interna de la app <p>/data/data/com.xxx.xxx/cache</p>
     *
     * @return
     * @since 2.0.5
     */
    public static boolean cleanInternalCache() {
        return Files.deleteDirectoryRecursive( MunixUtilities.context.getCacheDir() );
    }


    /**
     * Elimina el directorio files de la app <p>/data/data/com.xxx.xxx/files</p>
     *
     * @return
     * @since 2.0.5
     */
    public static boolean cleanInternalFiles() {
        return Files.deleteDirectoryRecursive( MunixUtilities.context.getFilesDir() );
    }

    /**
     * Elimina el directorio files de la app <p>/data/data/com.xxx.xxx/files</p>
     *
     * @param context
     * @return
     * @since 2.0.5
     */
    public static boolean cleanInternalFiles( Context context ) {
        return Files.deleteDirectoryRecursive( context.getFilesDir() );
    }


    /**
     * Retorna el código de versión de la aplicación
     *
     * @param context
     * @return versionCode
     */
    public static int getVersionCode( Context context ) {
        int v = 0;
        try {
            v = context.getPackageManager()
                    .getPackageInfo( context.getPackageName(), 0 ).versionCode;
        } catch ( PackageManager.NameNotFoundException e ) {
        }
        return v;
    }

    public static int getVersionCode() {
        return getVersionCode( MunixUtilities.context );
    }

    /**
     * Retorna el nombre de la versión de la aplicación
     *
     * @param context
     * @return versionName
     */
    public static String getVersionName( Context context ) {
        String v = "";
        try {
            v = context.getPackageManager()
                    .getPackageInfo( context.getPackageName(), 0 ).versionName;
        } catch ( PackageManager.NameNotFoundException e ) {
        }
        return v;
    }

    public static String getVersionName() {
        return getVersionName( MunixUtilities.context );
    }

    public static Drawable getAppIcon( Context context ) {
        return getAppIcon( context, context.getPackageName() );
    }

    public static Drawable getAppIcon() {
        return getAppIcon( MunixUtilities.context );
    }


    public static Drawable getAppIcon( Context context, String packageName ) {
        if ( TextUtils.isEmpty( packageName.trim() ) )
            return null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo( packageName, 0 );
            return pi == null ? null : pi.applicationInfo.loadIcon( pm );
        } catch ( PackageManager.NameNotFoundException e ) {
            e.printStackTrace();
            return null;
        }
    }

    public static Drawable getAppIcon( String packageName ) {
        return getAppIcon( MunixUtilities.context, packageName );
    }
}
