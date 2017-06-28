package fr.pomp.adfuell.utils.munix;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class Views {

    public static void appear( View v, int duration ) {
        if ( v.getVisibility() != View.VISIBLE ) {
            ObjectAnimator.ofFloat( v, "alpha", 0, 1 ).setDuration( duration ).start();
            v.setVisibility( View.VISIBLE );
        }
    }

    public static void disappear( View v, int duration ) {
        if ( v.getVisibility() == View.VISIBLE ) {
            Animation fadeInAnimation = AnimationUtils.loadAnimation( v.getContext(), android.R.anim.fade_out );
            fadeInAnimation.setDuration( duration );
            v.startAnimation( fadeInAnimation );
            v.setVisibility( View.GONE );
        }
    }

    public static int getPixels( int dipValue ) {
        return getPixels( MunixUtilities.context, dipValue );
    }

    public static int getPixels( Context mContext, int dipValue ) {
        if ( mContext != null && dipValue > 0 ) {
            Resources r = mContext.getResources();
            int px = (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics() );
            return px;
        } else
            return 0;
    }
}
