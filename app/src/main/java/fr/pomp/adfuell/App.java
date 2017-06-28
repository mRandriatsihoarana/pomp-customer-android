package fr.pomp.adfuell;

import android.app.Application;
import android.support.v4.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import fr.pomp.adfuell.model.CarModel;
import fr.pomp.adfuell.model.CommandeModel;
import fr.pomp.adfuell.model.HistoryModel;
import fr.pomp.adfuell.model.PricePerLiterModel;
import fr.pomp.adfuell.model.ScheduleModel;
import fr.pomp.adfuell.model.TokenModel;
import fr.pomp.adfuell.model.UserModel;
import fr.pomp.adfuell.utils.comon.Config;
import fr.pomp.adfuell.utils.edena.EDAcrReportCrash;
import fr.pomp.adfuell.utils.edena.EDGson;
import fr.pomp.adfuell.utils.edena.PreferencesManager;
import fr.pomp.adfuell.utils.edena.retrofit.EDRetrofit;
import fr.pomp.adfuell.utils.munix.MunixUtilities;
import me.drakeet.library.CrashWoodpecker;
import me.drakeet.library.PatchMode;
import retrofit2.Retrofit;

/**
 * Created by edena on 16/01/2017.
 */

public class App extends Application {
    public Retrofit _retrofit;
    public PreferencesManager _pref;
    public EDGson _json;
    public CarModel _carUpdate = null;
    private Fragment _fragmentCurrent;
    public boolean _isOrderConfirmedView = false;

    @Override
    public void onCreate() {
        super.onCreate();
        if(!Config.DEBUG)
          EDAcrReportCrash.getInstance(this).init();

       _retrofit = EDRetrofit.getIntance(this, Config.WS_ULR).build();
        _pref = new PreferencesManager(this);
        _pref.init();
        _json = EDGson.getInstance();

        MunixUtilities.init(getApplicationContext());
        CrashWoodpecker.instance()
                .withKeys("widget", "me.drakeet")
                .setPatchMode(PatchMode.SHOW_LOG_PAGE)
                .setPatchDialogUrlToOpen("https://drakeet.me")
                .setPassToOriginalDefaultHandler(true)
                .flyTo(this);
        //Intercom.initialize(this, "android_sdk-206d2bdfa368bbc07c8b00386cdf9408324b62fa", "fist5ws3");
    }

    public Retrofit getRetrofitWithAuth(){
        HashMap<String, String> mapHeader = new HashMap<String, String>();
        TokenModel tokenModel = getToken();
        if(tokenModel != null){
            String type = tokenModel.tokenType;
            String auth = type.substring(0,1).toUpperCase() + type.substring(1).toLowerCase()+" "+tokenModel.accessToken;

            mapHeader.put("Accept","application/json");
            mapHeader.put("Authorization",auth);
            return EDRetrofit.getIntance(this,Config.WS_ULR).buildWithHeader(mapHeader);
        }else
            return _retrofit;

    }

    public void userSaveJson(String userJson){
        try{
            _pref.putString("users",userJson);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public UserModel user(){
        UserModel model = null;
        try{
            String tok = _pref.getString("users");
            if(!tok.equals("")){
                model = _json.deSerialize(tok,UserModel.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return model;

    }

    public void userSaveObject(UserModel user){
        try{
            _pref.putObject("users_obj",user);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public UserModel userObject(){
        UserModel model = null;
        try{
            model = _pref.getObject("users_obj",UserModel.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public void addListAutoCompleteLoging(String mail){
        Set<String> list = getListAutoCompleteLoging();

        if(list == null) list = new HashSet<String>();
        list.add(mail);
        _pref.putStringSet("mail_login",list);
    }

    public Set<String> getListAutoCompleteLoging(){
        Set<String> list = new HashSet<String>();
        try{
            list = _pref.getStringSet("mail_login");
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    private String getTokenJson(){
        String token = null;
        try{
            String tok = _pref.getString("token");
            if(!tok.equals("")) token = tok;
        }catch (Exception e){
            e.printStackTrace();
        }
        return token;
    }

    public void setTokenJson(String token){

        try{
            _pref.putString("token",token);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public TokenModel getToken(){
        String tokenJson = getTokenJson();
        TokenModel tokenModel = null;
        if(tokenJson != null){
            try {
                JSONObject json = new JSONObject(tokenJson);
                tokenModel = new TokenModel();
                tokenModel.accessToken = json.getString("access_token");
                tokenModel.scope = json.getString("scope");
                tokenModel.tokenType = json.getString("token_type");
                tokenModel.expiresIn = json.getInt("expires_in");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tokenModel;
    }

    public void carSaveJson(String carJson){
        try{
            _pref.putString("cars",carJson);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public CarModel car(){
        CarModel model = null;
        try{
            String tok = _pref.getString("cars");
            if(!tok.equals("")){
                model = _json.deSerialize(tok,CarModel.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return model;

    }

    public void carListSaveJson(String carListJson){
        try{
            _pref.putString("car_list", carListJson);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<CarModel> carList(){
        ArrayList<CarModel> model = null;
        try{
            String tok = _pref.getString("car_list");
            if(!tok.equals("")){
                model = _json.deSerializeArray(tok,CarModel.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public void carSaveObject(CarModel carModel){
        try{
            _pref.putObject("car_obj",carModel);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public CarModel carObject(){
        CarModel model = null;
        try{
            model = _pref.getObject("car_obj",CarModel.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public void schedulesListSaveJson(String scheduleJson){
        try{
            _pref.putString("schedule_list", scheduleJson);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<ScheduleModel> scheduleList(){
        ArrayList<ScheduleModel> model = null;
        try{
            String tok = _pref.getString("schedule_list");
            if(!tok.equals("")){
                model = _json.deSerializeArray(tok,ScheduleModel.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public void scheduleSaveObject(ScheduleModel scheduleModel){
        try{
            _pref.putObject("schedule_obj",scheduleModel);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ScheduleModel scheduleObject(){
        ScheduleModel model = null;
        try{
            model = _pref.getObject("schedule_obj",ScheduleModel.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public void pricePerLiterSaveJson(String pricePerLiterJson){
        try{
            _pref.putString("pricePerLiter",pricePerLiterJson);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public PricePerLiterModel pricePerLiter(){
        PricePerLiterModel model = null;
        try{
            String tok = _pref.getString("pricePerLiter");
            if(!tok.equals("")){
                model = _json.deSerialize(tok,PricePerLiterModel.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return model;

    }

    public void pricePerLiterSaveObject(PricePerLiterModel pricePerLiter){
        try{
            _pref.putObject("car_obj",pricePerLiter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public PricePerLiterModel pricePerLiterObject(){
        PricePerLiterModel model = null;
        try{
            model = _pref.getObject("car_obj",PricePerLiterModel.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public void commandeSaveObject(CommandeModel commandeModel){
        try{
            _pref.putObject("commande_obj",commandeModel);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public CommandeModel commandeObject(){
        CommandeModel model = null;
        try{
            model = _pref.getObject("commande_obj",CommandeModel.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public void positionHoraireSaveInteger(Integer integer){
        try{
            _pref.putObject("position_horaire",integer);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Integer positionHoraire(){
        Integer model = null;
        try{
            model = _pref.getObject("position_horaire",Integer.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public void positionVehiculeSaveInteger(Integer integer){
        try{
            _pref.putObject("position_vehicule",integer);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Integer positionVehicule(){
        Integer model = null;
        try{
            model = _pref.getObject("position_vehicule",Integer.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public void historySaveJson(String storyJson){
        try{
            _pref.putString("story",storyJson);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public HistoryModel history(){
        HistoryModel model = null;
        try{
            String tok = _pref.getString("story");
            if(!tok.equals("")){
                model = _json.deSerialize(tok,HistoryModel.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return model;

    }

    /**
     * get json from asset fileName = file.json
     *
     * @param fileName
     * @return
     */
    public String getJsonFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = this.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public Fragment getFragmentCurrent(){
        return _fragmentCurrent;
    }

    public void setFragmentCurrent(Fragment fragment){
        _fragmentCurrent = fragment;
    }

    public boolean isCurrentFrament(Class fragment){
        if(_fragmentCurrent.getClass().getSimpleName().equalsIgnoreCase(fragment.getClass().getSimpleName()))
            return true;
        else return false;

    }

}
