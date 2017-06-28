package fr.pomp.adfuell.model;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by edena on 04/04/2017.
 */

public class UserModel {
    public String id;
    public String username;
    public List<String> roles = null;
    public String phone;
    public String userType;
    public String firstname;
    public String lastname;
    public String idOnfleet;
    public String company;
    public String companyName;
    public List<OrderModel> orders = null;
    public Integer vehicles;
    public Integer locations;

    @Override
    public String toString() {
        String json = new Gson().toJson(this);
        return  json;
    }
}
