package fr.pomp.adfuell.model;

import com.google.gson.Gson;

/**
 * Created by Mandimbisoa on 05/05/2017.
 */

public class PricePerLiterModel {
    public String id;
    public String pricePerLiterDiesel;
    public String pricePerLiterUnleaded95;
    public String pricePerLiterUnleaded98;
    public String idCompany;

    @Override
    public String toString() {
        String json = new Gson().toJson(this);
        return  json;
    }
}
