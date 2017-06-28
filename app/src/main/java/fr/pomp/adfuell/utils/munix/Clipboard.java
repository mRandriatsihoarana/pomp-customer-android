package fr.pomp.adfuell.utils.munix;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;

/**
 * Created by munix on 16/12/16.
 */

public class Clipboard {


    public static void copyText( Context context, String text ) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService( Context.CLIPBOARD_SERVICE );
        clipboard.setPrimaryClip( ClipData.newPlainText( "text", text ) );
    }

    public static void copyText( String text ) {
        copyText( MunixUtilities.context, text );
    }


    public static String getText( Context context ) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService( Context.CLIPBOARD_SERVICE );
        ClipData clip = clipboard.getPrimaryClip();
        if ( clip != null && clip.getItemCount() > 0 ) {
            return clip.getItemAt( 0 ).coerceToText( context ).toString();
        }
        return null;
    }

    public static String getText() {
        return getText( MunixUtilities.context );
    }


    public static void copyIntent( Context context, Intent intent ) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService( Context.CLIPBOARD_SERVICE );
        clipboard.setPrimaryClip( ClipData.newIntent( "intent", intent ) );
    }

    public static void copyIntent( Intent intent ) {
        copyIntent( MunixUtilities.context, intent );
    }

    public static Intent getIntent( Context context ) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService( Context.CLIPBOARD_SERVICE );
        ClipData clip = clipboard.getPrimaryClip();
        if ( clip != null && clip.getItemCount() > 0 ) {
            return clip.getItemAt( 0 ).getIntent();
        }
        return null;
    }

    public static Intent getIntent() {
        return getIntent( MunixUtilities.context );
    }
}
