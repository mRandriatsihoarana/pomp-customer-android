package fr.pomp.adfuell.utils.munix;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by munix on 16/12/16.
 */

public class Screen {

    public static int getWindowHeight() {
        return getWindowHeight( MunixUtilities.context );
    }

    public static int getWindowHeight( Context mContext ) {
        WindowManager wm = (WindowManager) mContext.getSystemService( Context.WINDOW_SERVICE );
        Display display = wm.getDefaultDisplay();

        if ( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2 ) {
            Point size = new Point();
            display.getSize( size );
            return size.y;
        } else {
            return display.getHeight();
        }
    }

    public static int getWindowWidth() {
        return getWindowWidth( MunixUtilities.context );
    }

    /**
     * Devuelve el ancho de la pantalla
     *
     * @param mContext
     * @return
     */
    public static int getWindowWidth( Context mContext ) {
        WindowManager wm = (WindowManager) mContext.getSystemService( Context.WINDOW_SERVICE );
        Display display = wm.getDefaultDisplay();

        if ( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2 ) {
            Point size = new Point();
            display.getSize( size );
            return size.x;
        } else {
            return display.getWidth();
        }
    }

    /**
     * Determina si la aplicación está en modo horizontal
     * @param context
     * @return
     */
    public static boolean isLandscape( Context context ) {
        return context.getResources()
                .getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * Determina si la aplicación está en modo horizontal
     * @return
     */
    public static boolean isLandscape() {
        return isLandscape( MunixUtilities.context );
    }

    /**
     * Determina si la aplicación está en modo vertical
     * @param context
     * @return
     */
    public static boolean isPortrait( Context context ) {
        return context.getResources()
                .getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * Determina si la aplicación está en modo vertical
     * @return
     */
    public static boolean isPortrait() {
        return isPortrait( MunixUtilities.context );
    }

    public static Bitmap captureWithStatusBar( Activity activity ) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled( true );
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics( dm );
        Bitmap ret = Bitmap.createBitmap( bmp, 0, 0, dm.widthPixels, dm.heightPixels );
        view.destroyDrawingCache();
        return ret;
    }

    public static Bitmap captureWithoutStatusBar( Activity activity ) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled( true );
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int statusBarHeight = getStatusBarHeight( activity.getApplicationContext() );
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics( dm );
        Bitmap ret = Bitmap.createBitmap( bmp, 0, statusBarHeight, dm.widthPixels, dm.heightPixels - statusBarHeight );
        view.destroyDrawingCache();
        return ret;
    }

    /**
     * Obtiene la altura del StatusBar
     * @param context
     * @return
     */
    public static int getStatusBarHeight( Context context ) {
        int result = -1;
        int resourceId = context.getResources()
                .getIdentifier( "status_bar_height", "dimen", "android" );
        if ( resourceId > 0 ) {
            result = context.getResources().getDimensionPixelSize( resourceId );
        }
        return result;
    }

    /**
     * Obtiene la altura del StatusBar
     * @return
     */
    public static int getStatusBarHeight() {
        return getStatusBarHeight( MunixUtilities.context );
    }
}
