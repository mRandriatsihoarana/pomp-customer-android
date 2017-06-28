package fr.pomp.adfuell.utils.edena.retrofit.test;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by edena on 18/01/2017.
 */

public interface ITestRetrofitService {
    public static String URL_BASE = "http://echo.jsontest.com";
    //PDF https://www.u-paris2.fr/sites/default/files/pdf.pdf
    //http://echo.jsontest.com/key/value/one/two
    //This method is used for "POST"
    @FormUrlEncoded
    @POST("/key/value/one/two")
    Call<TestModel> post(
            @Field("username") String username,
            @Field("password") String password
    );

    //This method is used for "GET"
    // @GET("/api.php")
    @GET("/key/value/one/two")
    Call<ResponseBody> getSimpleString(
            @Query("method") String method
    );

    //This method is used for "GET"
    // @GET("/api.php")
    @GET("/key/value/one/two")
    Call<TestModel> getTestModel(
            @Query("method") String method
    );

    @GET("/sites/default/files/pdf.pdf")
    Call<ResponseBody> downloadFileWithFixedUrl();
    // option 2: using a dynamic URL
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);

    @Multipart
    @POST("/upload")
    Call<JsonObject> uploadImage(@Part MultipartBody.Part file);
}
