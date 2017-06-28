package fr.pomp.adfuell.utils.comon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.pomp.adfuell.App;
import fr.pomp.adfuell.model.CardModel;
import fr.pomp.adfuell.utils.munix.Strings;

/**
 * Created by edena on 07/04/2017.
 */

public class CardService {
    static CardService _service;
    private App _app;
    String idUser = null;

    private CardService(App app){
        _app = app;
        if(_app.user() != null)
            idUser = _app.user().id;

    }

    public static CardService getIntance(App app){
        if(_service == null) _service = new CardService(app);
        return _service;
    }

    public boolean saveCard(CardModel card){
        boolean isSaved = false;
        ArrayList<CardModel> list = list();
        try{
            if(list == null) list = new ArrayList<>();
            list.add(card);
            String json = _app._json.serialize(list);

            JSONObject jService =  getJsonServiceCard();
            jService.put(idUser,json);

            saveService(jService.toString());
            isSaved = true;
        }catch (Exception e){
            e.printStackTrace();
        }

        return isSaved;
    }

    public void clearAll(){
        _app._pref.putString("service_card","");
    }

    public void clearByUser(){

        if(idUser != null){
            try {
                JSONObject jService =  getJsonServiceCard();
                jService.put(idUser,"");
                saveService(jService.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public ArrayList<CardModel> list(){
        ArrayList<CardModel> list = null;

        if(idUser != null){
            try {
                String sArray = getJsonUser();
                if(sArray != null && !sArray.equals("")){
                    list = _app._json.deSerializeArray(sArray,CardModel.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return  list;
    }

    public void setList( ArrayList<CardModel> list){
        clearByUser();
        if(list == null) {
            return;
        };

        try{
            String json = _app._json.serialize(list);
            JSONObject jService =  getJsonServiceCard();
            jService.put(idUser,json);
            saveService(jService.toString());

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    JSONObject getJsonServiceCard(){
        JSONObject jsonObject = null;
        try{
           String js = _app._pref.getString("service_card","");
            jsonObject = new JSONObject(js);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(jsonObject == null)   jsonObject = new JSONObject();

        return jsonObject;
    }

    String getJsonUser(){
        String sUser = "";
        JSONObject jService =  getJsonServiceCard();
        try {
            sUser = jService.getString(idUser);
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return sUser;
    }

    void saveService(String js){
        CommonUtils.log("service_card jService",js.toString());
        _app._pref.putString("service_card",js);
    }





}
