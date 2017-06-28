package fr.pomp.adfuell.utils.edena.retrofit.test;

import android.content.Context;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import fr.pomp.adfuell.utils.edena.retrofit.EDFileDownload;
import fr.pomp.adfuell.utils.edena.retrofit.EDUploadRequestBody;
import fr.pomp.adfuell.utils.munix.Logs;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by edena on 18/01/2017.
 */

public class TestModel implements Serializable {
    public String one;
    public String key;

    public void runTest(Retrofit retrofit){
        ITestRetrofitService service = retrofit.create(ITestRetrofitService.class);
        Call<TestModel> call = service.getTestModel("");
        call.enqueue(new Callback<TestModel>() {
            @Override
            public void onResponse(Call<TestModel> call, Response<TestModel> response) {
                Logs.info("retrofit","retrofit "+response.body().key);
            }

            @Override
            public void onFailure(Call<TestModel> call, Throwable t) {
                Logs.info("retrofit","retrofit onFailure");
            }
        });
    }
    public void runTestString(Retrofit retrofit){
        ITestRetrofitService service = retrofit.create(ITestRetrofitService.class);
        Call<ResponseBody> call = service.getSimpleString("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Logs.info("retrofit","retrofit "+new String(response.body().bytes()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }


        });
    }

    public void runTestDownloadEDFileDownload(Retrofit retrofit,final Context cxt){
        ITestRetrofitService service = retrofit.create(ITestRetrofitService.class);
        Call<ResponseBody> call = service.downloadFileWithFixedUrl();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                File pdf = new File( cxt.getExternalFilesDir(null) + File.separator + "pdf.pdf");
                //if(pdf.exists()) pdf.delete();
                EDFileDownload.getInstance(new EDFileDownload.IEDFileDownload() {
                     @Override
                    public void loading(int loaded) {
                         Logs.info("retrofit","retrofit loaded "+loaded);
                    }

                    @Override
                    public void loaded(File save) {
                        Logs.info("retrofit","retrofit loaded "+save.getAbsolutePath());
                    }
                }).writeResponseBodyToDisk(response.body(),pdf);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }


        });


    }

    public void runTestEDUploadRequestBody(Retrofit retrofit,final Context cxt,File file) {

        ITestRetrofitService service = retrofit.create(ITestRetrofitService.class);
        EDUploadRequestBody fileBody = new EDUploadRequestBody(file, new EDUploadRequestBody.UploadCallbacks() {
            @Override
            public void onProgressUpdate(int percentage) {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onFinish() {

            }
        });
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", file.getName(), fileBody);
        Call<JsonObject> call = service.uploadImage(filePart);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){

                }else{

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }
}
