package fr.pomp.adfuell.utils.edena.retrofit;

import android.content.Context;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * Created by edena on 18/01/2017.
 */

public class EDRetrofit {

    private static  final String TAG = "EDRetrofit";
    private   String _urlBase = null;

    static EDRetrofit _instance;
    Context _context;
    public EDRetrofit(Context context,String urlBase) {
        _context = context;
        _urlBase = urlBase;
    }

    public static EDRetrofit getIntance(Context context,String urlBase){
        if(_instance == null) _instance = new EDRetrofit(context,urlBase);
        return _instance;
    }

    public void setUrlBase(String urlBase) {
        this._urlBase = urlBase;
    }

    public Retrofit build(){
        //Here a logging interceptor is created
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        Retrofit retrofit = null;
        try {
             retrofit = new Retrofit.Builder()
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    //.addConverterFactory(new StringConverterFactory())
                    .baseUrl(_urlBase).build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return retrofit;
    }

    public Retrofit buildWithHeader(final Map<String,String> mapHeader){

        //Here a logging interceptor is created
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                final Request.Builder request = chain.request().newBuilder();
                Set cles = mapHeader.keySet();
                Iterator it = cles.iterator();
                while (it.hasNext()){
                    String key = (String) it.next();
                    String value = mapHeader.get(key);
                    request.addHeader(key, value);
                }

                Response reponse = chain.proceed(request.build());

                return reponse;
            }
        };

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        httpClient.addInterceptor(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                //.addConverterFactory(new StringConverterFactory())
                .baseUrl(_urlBase).build();
        return retrofit;
    }



}
