package fr.pomp.adfuell.model;

import com.google.gson.Gson;

/**
 * Created by Mandimbisoa on 18/05/2017.
 */

public class CommandeModel {
    public String idUser;
    public String idSchedule;
    public String idVehicle;
    public String idPaymentMethod;
    public String orderType;
    public String currency;
    public String amount;
    public String placeNumber;

    @Override
    public String toString() {
        String json = new Gson().toJson(this);
        return  json;
    }
}
