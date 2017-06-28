package fr.pomp.adfuell.model;

import com.google.gson.Gson;

/**
 * Created by Mandimbisoa on 07/04/2017.
 */

public class CarModel {

    public String id;
    public String brand;
    public String model;
    public String fuelType;
    public String color;
    public Integer tankSize;
    public String imat;
    public String user;
    public Integer orders;

    @Override
    public String toString() {
        String json = new Gson().toJson(this);
        return  json;
    }

}
