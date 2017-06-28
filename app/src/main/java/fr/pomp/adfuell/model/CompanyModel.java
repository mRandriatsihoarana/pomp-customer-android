package fr.pomp.adfuell.model;

import com.google.gson.Gson;

/**
 * Created by Mandimbisoa on 04/05/2017.
 */

public class CompanyModel {
    public String id;
    public String name;
    public PricePerLiterModel pricePerLiter;

    @Override
    public String toString() {
        String json = new Gson().toJson(this);
        return  json;
    }
}
