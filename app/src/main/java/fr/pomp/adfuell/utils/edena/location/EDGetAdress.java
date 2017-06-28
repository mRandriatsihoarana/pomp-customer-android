package fr.pomp.adfuell.utils.edena.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by edena on 12/04/2017.
 */

public class EDGetAdress extends AsyncTask<String, Void, AdressModel> {
    private Context _cxt;
    private IEDGetAdress _delegate;

    public EDGetAdress(Context cxt, IEDGetAdress delegate){
        super();
        _cxt = cxt;
        _delegate = delegate;
    }

    @Override
    protected AdressModel doInBackground(String... params) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(_cxt, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(params[0]), Double.parseDouble(params[1]), 1);

            //get current Street name
            String address = addresses.get(0).getAddressLine(0);

            //get current province/City
            String province = addresses.get(0).getAdminArea();

            //get country
            String country = addresses.get(0).getCountryName();

            //get postal code
            String postalCode = addresses.get(0).getPostalCode();

            //get place Name
            String knownName = addresses.get(0).getFeatureName();

            AdressModel add = new AdressModel();
            add.address = address;
            add.country = country;
            add.knownName = knownName;
            add.postalCode = postalCode;
            add.province = province;
            return add;

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;

        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(_delegate != null) _delegate.onStart();
    }


    @Override
    protected void onPostExecute(AdressModel address) {
        if(_delegate != null) _delegate.onFinished(address);
    }

}
