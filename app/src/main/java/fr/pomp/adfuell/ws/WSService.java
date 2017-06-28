package fr.pomp.adfuell.ws;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;

import fr.pomp.adfuell.utils.comon.CommonUtils;
import fr.pomp.adfuell.utils.comon.Config;
import fr.pomp.adfuell.utils.munix.SimpleToast;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by edena on 31/03/2017.
 */

public class WSService {
    public static final int ERROR_CODE_ORDER_ALREADY = -2;

    static WSService _ws;
    Retrofit _retrofit;
    private WSService(Retrofit retrofit){
        _retrofit = retrofit;
    }

    public static WSService getInstance(Retrofit retrofit){
        //if(_ws == null)
         _ws = new WSService(retrofit);
        return _ws;
    }

    public void getTokent(final IWSDelegate delegate){
       try{
           IRetrofitService iRetrofitService = _retrofit.create(IRetrofitService.class);
           Call<ResponseBody> call = iRetrofitService.token(Config.WS_AUTH_CLIENT_ID,
                   Config.WS_AUTH_CLIENT_SECRET,
                   "client_credentials");
           call.enqueue(contstuctCallBack(delegate));
       }catch (Exception e){
           SimpleToast.showLong("Error ws");
           e.printStackTrace();
       }


    }

    /**
     * Authentification
     * @param mail
     * @param pass
     * @param delegate
     */
    public  void authent(String mail, String pass, final IWSDelegate delegate){
        try{
            IRetrofitService iRetrofitService = _retrofit.create(IRetrofitService.class);
            Call<ResponseBody> call = iRetrofitService.authent(mail,pass);
            call.enqueue(contstuctCallBack(delegate));
        }catch (Exception e){
            SimpleToast.showLong("Error ws");
            e.printStackTrace();
        }


    }

    public  void createCompte(JsonObject jsonBody, final IWSDelegate delegate){
        IRetrofitService iRetrofitService = _retrofit.create(IRetrofitService.class);
        Call<ResponseBody> call = iRetrofitService.usersCreate(jsonBody);
        call.enqueue(contstuctCallBack(delegate));

    }

    public void createCar(String id, JsonObject jsonBody, final IWSDelegate delegate){
        IRetrofitService iRetrofitService = _retrofit.create(IRetrofitService.class);
        Call<ResponseBody> call = iRetrofitService.carCreate(id,jsonBody);
        call.enqueue(contstuctCallBack(delegate));
    }

    public void getCarList(String id, final IWSDelegate delegate){
        IRetrofitService iRetrofitService = _retrofit.create(IRetrofitService.class);
        Call<ResponseBody> call = iRetrofitService.carList(id);
        call.enqueue(contstuctCallBack(delegate));
    }

    public void deleteCar(String id, final IWSDelegate delegate){
        IRetrofitService iRetrofitService = _retrofit.create(IRetrofitService.class);
        Call<ResponseBody> call = iRetrofitService.deleteCar(id);
        call.enqueue(contstuctCallBack(delegate));
    }

    public void getLocations(String id, final IWSDelegate delegate){
        IRetrofitService iRetrofitService = _retrofit.create(IRetrofitService.class);
        Call<ResponseBody> call = iRetrofitService.getLocation(id);
        call.enqueue(contstuctCallBack(delegate));
    }

    public void resetPass(String email, String locale, final IWSDelegate delegate){
        IRetrofitService iRetrofitService = _retrofit.create(IRetrofitService.class);
        Call<ResponseBody> call = iRetrofitService.resetPassword(email,locale);
        call.enqueue(contstuctCallBack(delegate));
    }

    public  void orderHistory(String idUser, final IWSDelegate delegate){
        IRetrofitService iRetrofitService = _retrofit.create(IRetrofitService.class);
        Call<ResponseBody> call = iRetrofitService.orderHistory(idUser);
        call.enqueue(contstuctCallBack(delegate));

    }

    public void getSchedules(String id, final IWSDelegate delegate){
        IRetrofitService iRetrofitService = _retrofit.create(IRetrofitService.class);
        Call<ResponseBody> call = iRetrofitService.getSchedules(id);
        call.enqueue(contstuctCallBack(delegate));
    }

    public void postMessageLocation(String id,String message, final IWSDelegate delegate){
        IRetrofitService iRetrofitService = _retrofit.create(IRetrofitService.class);
        Call<ResponseBody> call = iRetrofitService.postMessageLocation(id,message);
        call.enqueue(contstuctCallBack(delegate));
    }

    public void createCard(String id, String stripeToken, String name, final IWSDelegate delegate){
        IRetrofitService iRetrofitService = _retrofit.create(IRetrofitService.class);
        Call<ResponseBody> call = iRetrofitService.cardCreate(id,stripeToken,name);
        call.enqueue(contstuctCallBack(delegate));
    }

    public void updateCard(String id, String stripeToken, String name, final IWSDelegate delegate){
        IRetrofitService iRetrofitService = _retrofit.create(IRetrofitService.class);
        Call<ResponseBody> call = iRetrofitService.cardUpdate(id,stripeToken,name);
        call.enqueue(contstuctCallBack(delegate));
    }

    public void getCardList(String id, final IWSDelegate delegate){
        IRetrofitService iRetrofitService = _retrofit.create(IRetrofitService.class);
        Call<ResponseBody> call = iRetrofitService.cardList(id);
        call.enqueue(contstuctCallBack(delegate));
    }

    public void deleteCard(String id, final IWSDelegate delegate){
        IRetrofitService iRetrofitService = _retrofit.create(IRetrofitService.class);
        Call<ResponseBody> call = iRetrofitService.cardDelete(id);
        call.enqueue(contstuctCallBack(delegate));
    }

    public void getPricePerLiter(String id, final IWSDelegate delegate){
        IRetrofitService iRetrofitService = _retrofit.create(IRetrofitService.class);
        Call<ResponseBody> call = iRetrofitService.getPricePerLiter(id);
        call.enqueue(contstuctCallBack(delegate));
    }

    public void postCommande(JsonObject jsonBody, final IWSDelegate delegate){
        IRetrofitService iRetrofitService = _retrofit.create(IRetrofitService.class);
        Call<ResponseBody> call = iRetrofitService.postCommande(jsonBody);
        call.enqueue(contstuctCallBack(delegate));
    }

    public void cancelCommande(String id, final IWSDelegate delegate){
        IRetrofitService iRetrofitService = _retrofit.create(IRetrofitService.class);
        Call<ResponseBody> call = iRetrofitService.cancelledCommande(id);
        call.enqueue(contstuctCallBack(delegate));
    }

    public void updateCommande(String id, String placeNumber, final IWSDelegate delegate){
        IRetrofitService iRetrofitService = _retrofit.create(IRetrofitService.class);
        Call<ResponseBody> call = iRetrofitService.updateCommande(id,placeNumber);
        call.enqueue(contstuctCallBack(delegate));
    }

    public void updateCar(String id, JsonObject jsonBody, final IWSDelegate delegate){
        IRetrofitService iRetrofitService = _retrofit.create(IRetrofitService.class);
        Call<ResponseBody> call = iRetrofitService.carUpdate(id,jsonBody);
        call.enqueue(contstuctCallBack(delegate));
    }


    private  Callback<ResponseBody> contstuctCallBack(final IWSDelegate delegate){
        return new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody body = response.body();
                ResponseBody err = response.errorBody();
                int responseCode = response.code();

                if(body == null){
                    String mess = null;
                    try {
                        mess = new String(response.errorBody().bytes());
                        CommonUtils.log(mess);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    JSONObject obj = null;
                    String errMess = null;
                    try{
                        obj = new JSONObject(mess);
                        JSONObject jError =  obj.getJSONObject("error");
                        errMess = jError.getString("message");
                    }catch (Exception e){

                    }
                    if(errMess != null && errMess.equalsIgnoreCase("pomp.fail.order.alreadyBooked")){
                        responseCode = ERROR_CODE_ORDER_ALREADY;
                    }
                    delegate.onFaillure(responseCode);
                }else{
                    try {
                        String mess = null;
                        mess = new String(response.body().bytes());
                        delegate.onSuccess(mess);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                /*String rep = null;

                if(body != null){
                    try {
                        rep = new String(response.body().bytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if(err != null){
                    try {
                        rep = new String(err.bytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                delegate.onSuccess(rep);*/

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                delegate.onFaillure(null);
            }
        };
    }
}
