package fr.pomp.adfuell.utils.edena;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

/**
 * Created by edena on 20/01/2017.
 */

public class EDNofification {
    static EDNofification _instance;
    Context _context;
    private NotificationCompat.Builder _notificationBuilder;
    private NotificationManager _notificationManager;
    private String _text;
    private String _title;


    private int idNotif;

    EDNofification(Context cxt){
        _context = cxt;
        init();
    }

    public static EDNofification getIntance(Context cxt){
        if(_instance == null) _instance = new EDNofification(cxt);
        return _instance;
    }

     void   init(){
            _notificationManager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
            _notificationBuilder = new NotificationCompat.Builder(_context)
                    .setAutoCancel(true);
     }

    public void setProgress(int max, int progress){
        _notificationBuilder.setProgress(max,progress,false);
    }
    public void send(){
        _notificationManager.notify(getIdNotif(), _notificationBuilder.build());
    }

    public void send(String title, String text){
        if(title !=null) setTitle(title);
        if(text != null) setText(text);
        send();
    }

    public  void cancel(){
        _notificationManager.cancel(getIdNotif());
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String _title) {
        this._title = _title;
        _notificationBuilder.setContentTitle(this._title);
    }
    public void setSmallIcon(int iconResID) {
        _notificationBuilder.setSmallIcon(iconResID);
    }

    public NotificationCompat.Builder getNotificationBuilder(){
        return _notificationBuilder;
    }


    public String getText() {
        return _text;
    }

    public void setText(String _text) {
        this._text = _text;
        _notificationBuilder.setContentText(this._text);
    }

    public int getIdNotif() {
        return idNotif;
    }

    public void setIdNotif(int idNotif) {
        this.idNotif = idNotif;
    }

}
