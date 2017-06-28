package fr.pomp.adfuell.utils.edena;

import android.app.Application;

import com.devs.acr.AutoErrorReporter;

import fr.pomp.adfuell.R;

/**
 * Created by edena on 18/01/2017.
 * compile 'com.devs:acr:1.0.1'
 * To catch crash log on the app and send it from mail
 */

public class EDAcrReportCrash {
    public String _mail = "edena.tic@gmail.Com";
    public String _mailSubject = "Auto crash report";
    Application _app;
    static EDAcrReportCrash _instance;
    EDAcrReportCrash(Application app){
        _app = app;
    }
    public static EDAcrReportCrash getInstance(Application app){
        if(_instance == null) _instance = new EDAcrReportCrash(app);
        return _instance;
    }
    public void init(){
        AutoErrorReporter.get(_app)
                .setEmailAddresses(_app.getResources().getString(R.string.acr_mail))
                .setEmailSubject(_app.getResources().getString(R.string.acr_mail_subjet))
                .start();
    }
}
