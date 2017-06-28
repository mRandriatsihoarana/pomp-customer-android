package fr.pomp.adfuell.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by edena on 15/05/2017.
 */

public class CardListCreatedModel {
    public String date;//:"2017-05-15 11:04:56.993549"
    public int timezone_type;//"timezone_type":3
    public String timezone;//"timezone":"Europe\/Paris"


    public static Date getDate(String date){
        String fromat = "yyyy-MM-dd hh:mm:ss.S";
        Date daty = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(fromat);
        try {
            daty = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return daty;

    }
    public String getFormatedDate(){
        String formated = "";
        try {
            Date daty = getDate(date);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yy");
            formated = dateFormat.format(daty);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return formated;
    }


}
