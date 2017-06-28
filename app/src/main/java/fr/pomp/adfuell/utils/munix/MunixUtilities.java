package fr.pomp.adfuell.utils.munix;

import android.content.Context;

/**
 * Created by munix on 16/12/16.
 */

public class MunixUtilities {

    public static Context context;

    private MunixUtilities() {
        throw new UnsupportedOperationException( "Buuuuu" );
    }

    /**
     * Seteamos el contexto para poder usar en toda la app
     *
     * @param context
     */
    public static void init( Context context ) {
        MunixUtilities.context = context.getApplicationContext();
    }
}
