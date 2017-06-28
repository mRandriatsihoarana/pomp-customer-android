package fr.pomp.adfuell.utils.munix;

import android.widget.Toast;

/**
 * Created by munix on 16/12/16.
 */

public class SimpleToast {

    public static void showShort( String text ) {
        Toast.makeText( MunixUtilities.context, text, Toast.LENGTH_SHORT ).show();
    }

    public static void showLong( String text ) {
        Toast.makeText( MunixUtilities.context, text, Toast.LENGTH_LONG ).show();
    }
}
