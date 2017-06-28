package fr.pomp.adfuell.model;

import com.google.gson.Gson;

/**
 * Created by Mandimbisoa on 04/05/2017.
 */

public class LocationModel {
    public String id;
    public String name;
    public String address;
    public String addressComplement;
    public String zipCode;
    public String city;
    public String country;
    public String latitude;
    public String longitude;
    public String idOnfleet;
    public String company;
    public String user;

    @Override
    public String toString() {
        String json = new Gson().toJson(this);
        return  json;
    }
}
