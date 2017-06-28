package fr.pomp.adfuell.utils.comon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fr.pomp.adfuell.R;

/**
 * Created by edena on 28/03/2017.
 */

public class CommonUtils {


    public static void log(String message){
        log(null,message);
    }
    public static void log(String tag, String message){
        if(Config.DEBUG){
            if(tag == null) tag = "_CommonUtils_";
            Log.e(tag,tag + " "+message);
        }
    }

    public static void alert(String message, String title, int resIdIcon, final IDialog delegate, Context cxt){
        AlertDialog.Builder alert = new AlertDialog.Builder(
                cxt);
        if(resIdIcon>0)
        alert.setIcon(cxt.getResources().getDrawable(resIdIcon));
        alert.setMessage(message);
        if(title != null && !title.trim().equals("") )
             alert.setTitle(title);
        if(delegate != null){
            alert.setPositiveButton(cxt.getResources().getString(R.string.text_yes), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    delegate.yes(which);
                    dialog.dismiss();

                }
            });
            alert.setNegativeButton(cxt.getResources().getString(R.string.text_no), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    delegate.no(which);
                    dialog.dismiss();
                }
            });
        }else{
            alert.setPositiveButton(cxt.getResources().getString(R.string.text_ok), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();

                }
            });
        }


        alert.show();
    }

    public static void alertCustom(final String msg, String title, final int resDrawable, final IDialog delegate,final Context cxt){
        AlertDialog.Builder alert = new AlertDialog.Builder(
                cxt);

        //alert.setMessage(message);
        if(title != null && !title.trim().equals("") )
            alert.setTitle(title);
        if(delegate != null){
            alert.setPositiveButton(cxt.getResources().getString(R.string.text_yes), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    delegate.yes(which);
                    dialog.dismiss();

                }
            });
            alert.setNegativeButton(cxt.getResources().getString(R.string.text_no), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    delegate.no(which);
                    dialog.dismiss();
                }
            });
        }else{
            alert.setPositiveButton(cxt.getResources().getString(R.string.text_ok), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();

                }
            });
        }

        final AlertDialog dialog = alert.create();
        LayoutInflater inflater = ((Activity)cxt).getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.view_alert_dialog, null);
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);



        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                ImageView image = (ImageView) dialog.findViewById(R.id.diag_img);
                Bitmap icon = BitmapFactory.decodeResource(cxt.getResources(),
                        resDrawable);
                image.setImageBitmap(icon);
                TextView text = (TextView) dialog.findViewById(R.id.diag_text);
                text.setText(msg);
                /*
                float imageWidthInPX = (float)image.getWidth();

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                        Math.round(imageWidthInPX * (float)icon.getHeight() / (float)icon.getWidth()));
                image.setLayoutParams(layoutParams);
                */



            }
        });

        dialog.show();


    }

    public static  void hideKeyboard(Activity activity, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(android.content.Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0 );
    }

    public static boolean cardValidationMethod(String cardNo)
    {

        cardNo = cardNo.replace(" ", "");//removing empty space
        cardNo = cardNo.replace("-", "");//removing '-'
        //'Check the first two digits first,for AmericanExpress
        int twoDigit=Integer.parseInt(cardNo.substring(0, 2));
        int  fourDigit=Integer.parseInt(cardNo.substring(0, 4));
        int   oneDigit=Integer.parseInt(Character.toString(cardNo.charAt(0)));
        if(cardNo.length()<14){
            return false;
        }

        if(cardNo.length()==15 && (twoDigit==34 || twoDigit==37))
            return true;
        else
            //'Check the first two digits first,for MasterCard
            if(cardNo.length()==16 && twoDigit>=51 && twoDigit<=55)
                return true;
            else
                //'None of the above - so check the 'first four digits collectively
                if(cardNo.length()==16 && fourDigit==6011)//for DiscoverCard
                    return true;
                else

                if(cardNo.length()==16 || cardNo.length()==13 && oneDigit==4)//for VISA
                    return true;
                else
                    return false;
    }

    public static Date getDateWithTimeZone(String datetime){ // datetime = 2017-05-31T17:50:00+02:00
        String FORMAT_DATE_SCHEDULE = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat _dateFormatScedule = new SimpleDateFormat(FORMAT_DATE_SCHEDULE);
        Date dateScedule = null;
        Date date = null;
        try {
            dateScedule = _dateFormatScedule.parse(datetime);

            String time = datetime.substring(datetime.indexOf("+"));
            String hour = time.substring(0,time.indexOf(":"));
            String minute = time.substring(time.indexOf(":")+1);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateScedule);
            calendar.add(Calendar.MINUTE, Integer.valueOf(minute));
            calendar.add(Calendar.HOUR, Integer.valueOf(hour));

            date = calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static Date scheduleDate(Context cxt,String date){
        String formatStr = cxt.getResources().getString(R.string.date_format);
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static interface IDialog{
        public void no(Object obj);
        public void yes(Object obj);
    }




}
