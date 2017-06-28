package fr.pomp.adfuell.utils.edena.retrofit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

/**
 * Created by edena on 18/01/2017.
 */

public class EDFileDownload {
    public String TAG = EDFileDownload.class.getName();
    IEDFileDownload _listner;
    static EDFileDownload _instance;
    EDFileDownload(IEDFileDownload listner){
        _listner = listner;
    }
    public static EDFileDownload getInstance(IEDFileDownload listner){
       return _instance = new EDFileDownload(listner);
    }

    public boolean writeResponseBodyToDisk(ResponseBody body, File fileToSaved) {
        try {

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(fileToSaved);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;
                    if(_listner != null) _listner.loading((int)(100 * fileSizeDownloaded / fileSize));
                    //Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();
                if(_listner != null) _listner.loaded(fileToSaved);
                return true;
            } catch (IOException e) {
                if(_listner != null) _listner.loaded(null);
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            if(_listner != null) _listner.loaded(null);
            return false;
        }
    }

    public void setListner(IEDFileDownload listner){
        _listner = listner;
    }

    public interface IEDFileDownload{
        public void loading(int loadedPercent);
        public void loaded(File save);
    }
}
