package fr.pomp.adfuell.model;

import com.google.gson.Gson;

/**
 * Created by Mandimbisoa on 04/05/2017.
 */

public class OrderModel {
    public String id;
    public String status;
    public String orderType;
    public String addedInfo;
    public String placeNumber;
    public String amount;
    public String currency;
    public ScheduleModel schedule;
    public CarModel vehicle;
    public String liters;
    public String pricePerLiter;

    @Override
    public String toString() {
        String json = new Gson().toJson(this);
        return  json;
    }
}
