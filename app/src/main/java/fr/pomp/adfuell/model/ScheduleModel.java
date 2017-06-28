package fr.pomp.adfuell.model;

import com.google.gson.Gson;

/**
 * Created by Mandimbisoa on 04/05/2017.
 */

public class ScheduleModel {
    public String id;
    public String date;
    public LocationModel location;
    public CompanyModel company;
    public String user;
    public Integer orders;

    @Override
    public String toString() {
        String json = new Gson().toJson(this);
        return  json;
    }
}
