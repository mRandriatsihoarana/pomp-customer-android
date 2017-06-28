package fr.pomp.adfuell.utils.edena;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;

/**
 * Created by edena on 18/01/2017.
 */

public class EDProgressDialog extends ProgressDialog {

    static EDProgressDialog _instance;
    Context _context;

    EDProgressDialog(Context context){
        super(context);
        _context = context;
        setMax(100);
        setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    public static EDProgressDialog getInstance(Context context){
       if(_instance == null){
           _instance =  new EDProgressDialog(context);
       }
        return _instance;
    }


    public static GradientDrawable shapeRec( int backgroundColor, int borderColor)
    {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[] { 8, 8, 8, 8, 0, 0, 0, 0 });
        shape.setColor(backgroundColor);
        shape.setStroke(3, borderColor);
        return shape;
    }

    void test() {
        show();
        final Handler handle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                incrementProgressBy(1);
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (getProgress() <= getMax()) {
                        Thread.sleep(200);
                        handle.sendMessage(handle.obtainMessage());
                        if (getProgress() == getMax()) {
                            dismiss();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
