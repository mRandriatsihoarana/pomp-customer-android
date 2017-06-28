package fr.pomp.adfuell.ws;

import com.google.gson.JsonObject;

import fr.pomp.adfuell.utils.comon.Config;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by edena on 31/03/2017.
 */

public interface IRetrofitService {
    public static final String WS_PATH = Config.WS_PATH;

    /**
     * Get Access Token
     * @param clientId
     * @param clientSecret
     * @param grantType = client_credentials
     * @return
     */
//    @GET("/pomp/web/oauth/v2/token") //jacky
    @GET("/oauth/v2/token") //tena izy
    Call<ResponseBody> token(
            @Query("client_id") String clientId,
            @Query("client_secret") String clientSecret,
            @Query("grant_type") String grantType
    );

    /**
     * Creation users
     * @param firstname
     * @param lastname
     * @param email
     * @param phone
     * @param password
     * @return
     */

    @FormUrlEncoded
    @POST(WS_PATH + "users")
    Call<ResponseBody> post(
            @Field("firstname") String firstname,
            @Field("lastname") String  lastname,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("plainPassword") String password
    );

    /**
     * Create users
     * @param body
     * @return
     */
    @Headers( "Content-Type: application/json" )
    @POST(WS_PATH + "users")
    Call<ResponseBody> usersCreate(@Body JsonObject body);

    /**
     * Create car
     * @param id
     * @param body
     * @return
     */
    @Headers( "Content-Type: application/json" )
    @POST(WS_PATH + "vehicles/{id}")
    Call<ResponseBody> carCreate(
            @Path("id") String id,
            @Body JsonObject body );

    /**
     * Authentification via mail et password
     * @param mail
     * @param mdp
     * @return
     */
    @GET(WS_PATH + "security/authentification/{mail}/{mdp}")
    Call<ResponseBody> authent(
            @Path("mail") String mail,
            @Path("mdp") String mdp
    );

    /**
     * get list vehicule by user id
     * @param id
     * @return
     */
    @GET(WS_PATH + "vehicles/user/{id}")
    Call<ResponseBody> carList(
        @Path("id") String id
    );

    /**
     * delete car by car id
     * @param id
     * @return
     */
    @DELETE(WS_PATH + "vehicles/{id}")
    Call<ResponseBody> deleteCar(
            @Path("id") String id
    );

    /**
     * reinitialiser le mot de passe
     * @param email
     * @param locale
     * @return
     */
    @GET(WS_PATH + "users/reset-password/{email}/{locale}")
    Call<ResponseBody> resetPassword(
            @Path("email") String email,
            @Path("locale") String locale
    );

    /**
     * Get all location
     * @param id
     * @return
     */
    @GET(WS_PATH + "locations-available/{id}")
    Call<ResponseBody> getLocation(
            @Path("id") String id
    );

    /**
     * Get all location
     * @param id
     * @return
     */
    @GET(WS_PATH + "orders/user/{id}")
    Call<ResponseBody> orderHistory(
            @Path("id") String id
    );

    /**
     * envoyer information de localisation
     * @param id
     * @param phone
     * @return
     */
    @FormUrlEncoded
    @POST(WS_PATH + "mail/specialRequest/{id}")
    Call<ResponseBody> postMessageLocation(
            @Path("id") String id,
            @Field("message") String phone
    );

    /**
     * Get all schedules
     * @param id
     * @return
     */
    @GET(WS_PATH + "schedules-available/user/{id}")
    Call<ResponseBody> getSchedules(
            @Path("id") String id
    );

    /**
     * Create card
     * @param id user
     * @return
     */
    @FormUrlEncoded
    @POST(WS_PATH + "cards/{id}")
    Call<ResponseBody> cardCreate(
            @Path("id") String id,
            @Field("stripeToken") String stripeToken,
            @Field("name") String name
    );

    /**
     * Get all card
     * @param id
     * @return
     */
    @GET(WS_PATH + "cards/user/{id}")
    Call<ResponseBody> cardList(
            @Path("id") String id
    );

    /**
     * Delete card
     * @param id
     * @return
     */
    @DELETE(WS_PATH + "cards/{id}")
    Call<ResponseBody> cardDelete(
            @Path("id") String id
    );


    /**
     * Update card
     * @param id card
     * @return
     */
    @FormUrlEncoded
    @PUT(WS_PATH + "cards/{id}")
    Call<ResponseBody> cardUpdate(
            @Path("id") String id,
            @Field("stripeToken") String stripeToken,
            @Field("name") String name
    );

    /**
     * get price per liter
     * @param id
     * @return
     */
    @GET(WS_PATH + "prices/user/{id}")
    Call<ResponseBody> getPricePerLiter(
        @Path("id") String id
    );

    /**
     * faire une commande
     * @param body
     * @return
     */
    @Headers( "Content-Type: application/json" )
    @POST(WS_PATH + "orders")
    Call<ResponseBody> postCommande(
            @Body JsonObject body
    );

    /**
     * annuler une commande
     * @param id
     * @return
     */
    @PUT(WS_PATH +"cancelorder/{id}")
    Call<ResponseBody> cancelledCommande(
            @Path("id") String id
    );

    /**
     * modifier une commande
     * @param id
     * @param placeNumber
     * @return
     */
    @FormUrlEncoded
    @PUT(WS_PATH + "orders/{id}")
    Call<ResponseBody> updateCommande(
            @Path("id") String id,
            @Field("placeNumber") String placeNumber
    );

    /**
     * Update car
     * @param id
     * @param body
     * @return
     */
    @Headers( "Content-Type: application/json" )
    @PUT(WS_PATH + "vehicles/{id}")
    Call<ResponseBody> carUpdate(
            @Path("id") String id,
            @Body JsonObject body );
}
