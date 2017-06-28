package fr.pomp.adfuell.utils.edena.retrofit;


import android.app.IntentService;
import android.content.Intent;

import java.io.File;
import java.io.IOException;

import fr.pomp.adfuell.utils.edena.EDEventBus;
import fr.pomp.adfuell.utils.edena.EDNofification;
import fr.pomp.adfuell.utils.edena.chooser.FileUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by edena on 19/01/2017.
 */

public class EDDownloadService extends IntentService implements EDFileDownload.IEDFileDownload {
    public static int NOTIF_INT_ID = 1000;

    public static final String NOTIF_ACTIVATE = "notif";
    public static final String URL_ACTIVATE = "url";
    public static final String PATH_SAVE = "path";
    public static final String NOTIF_ICO = "ico";
    public static final String EVENT_BUS_NAME = "EDDownloadService";

    private boolean _isNotifActivate = false;
    private String _url;
    private String _pathSaved;
    private int _icoResID = -1;

    private String _download = "Download ";


    private EDNofification _notifcation;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public EDDownloadService(String name) {
        super(name);
    }

    public EDDownloadService() {
        super("Download Service");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        _isNotifActivate = intent.getBooleanExtra(NOTIF_ACTIVATE,false);
        _url = intent.getStringExtra(URL_ACTIVATE);
        _pathSaved = intent.getStringExtra(PATH_SAVE);
        _icoResID = intent.getIntExtra(NOTIF_ICO,-1);
        String title = FileUtils.getLastPathUrl(_url);
        _notifcation = EDNofification.getIntance(this);
        _notifcation.setIdNotif(NOTIF_INT_ID);
        _notifcation.setTitle(_download+title);
        _notifcation.setText(_download+"...");
        _notifcation.setProgress(0,0);
        if(_icoResID>0) _notifcation.setSmallIcon(_icoResID);
        if(_isNotifActivate) _notifcation.send();


        initDownload();
    }

    private void initDownload(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://google.com")
                .build();

        IRetrofit retrofitInterface = retrofit.create(IRetrofit.class);

        Call<ResponseBody> request = retrofitInterface.downloadFile(_url);
        try {
            EDFileDownload.getInstance(this).writeResponseBodyToDisk( request.execute().body(),new File(_pathSaved));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loading(int loadedPercent) {
        if(_isNotifActivate){
            _notifcation.setProgress(100,loadedPercent);
            _notifcation.setText("Downloading file "+loadedPercent+"%");
            _notifcation.send();
        }
        EDEventBus.getInstance(null).post(new EDEventBus(EVENT_BUS_NAME,loadedPercent));
    }

    @Override
    public void loaded(File save) {
        if(_isNotifActivate){
            _notifcation.cancel();
            _notifcation.setProgress(0,0);
            if(save != null)  _notifcation.setText("File Downloaded at "+save.getAbsolutePath());
            else _notifcation.setText("File Downloaded error");
            _notifcation.send();
        }
        EDEventBus.getInstance(null).post(new EDEventBus(EVENT_BUS_NAME,save));
    }

    private interface IRetrofit{
        @Streaming
        @GET
        Call<ResponseBody> downloadFile(@Url String url);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        _notifcation.cancel();
    }
}
