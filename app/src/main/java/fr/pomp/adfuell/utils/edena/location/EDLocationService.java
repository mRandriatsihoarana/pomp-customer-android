package fr.pomp.adfuell.utils.edena.location;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import fr.pomp.adfuell.utils.edena.EDEventBus;

/**
 * Created by edena on 23/01/2017.
 */

public class EDLocationService extends Service{

    public static final String EVENTBUS_ID = "EDLocationService";
    private static final int BETTER_TIME = 1000 * 60 * 1;
    private static final int REFRESH_TIME = 1000 * 60 * 2;

    public LocationManager _locationManager;
    public EDLocationListener _listner;
    public Location _bestLastLocation = null;
    Context _context;
    Intent _intent;

    @Override
    public void onCreate() {
        super.onCreate();
        _intent = new Intent(EVENTBUS_ID);
        _context = this;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        _locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        _listner = new EDLocationListener();
        _locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, REFRESH_TIME, 0, _listner);
        _locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, REFRESH_TIME, 0, _listner);
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > BETTER_TIME;
        boolean isSignificantlyOlder = timeDelta < -BETTER_TIME;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }



    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        _locationManager.removeUpdates(_listner);
        Log.i(EVENTBUS_ID, "onDestroy");
    }

    public class EDLocation{
        public String provider;
        public double latitude, longitude;

    }

    public class EDLocationListener implements LocationListener {

        public void onLocationChanged(final Location loc)
        {
            Log.i(EVENTBUS_ID, "onLocationChanged");
            if(isBetterLocation(loc, _bestLastLocation)) {
                _intent.putExtra("Latitude", loc.getLatitude());
                _intent.putExtra("Longitude", loc.getLongitude());
                _intent.putExtra("Provider", loc.getProvider());
                //sendBroadcast(_intent);
                EDLocation edLocation = new EDLocation();
                edLocation.latitude = loc.getLatitude();
                edLocation.longitude = loc.getLongitude();
                edLocation.provider = loc.getProvider();
                EDEventBus.getInstance(_context).post(new EDEventBus(EVENTBUS_ID,edLocation));

            }
        }

        public void onProviderDisabled(String provider)
        {

        }


        public void onProviderEnabled(String provider)
        {

        }


        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }

    }
}
