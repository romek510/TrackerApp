package trackerapp.trackerapp.service;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import trackerapp.trackerapp.handler.DialogHandler;

/**
 * Created by Krystian on 04.05.2017.
 */

public class GPSTracker extends Service implements LocationListener, GpsStatus.Listener {
    private final Context mContext;
    // flag for GPS status
    private boolean isGPSEnabled = false;
    // flag for network status
    private boolean isNetworkEnabled = false;
    // flag for GPS status
    private boolean canGetLocation = false;
    private String logs = "";
    private Location location; // location
    private double latitude; // latitude
    private double longitude; // longitude
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 2; // 2 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000; // 1 second
    // Declaring a Location Manager
    private LocationManager locationManager;

    // onGpsStatusChanged callbacks
    private Runnable gpsEventStartedCallback;
    private Runnable gpsEventStoppedCallback;
    private Runnable gpsEventFirstFixCallback;
    private Runnable gpsEventSatelliteStatusCallback;

    public GPSTracker(Context context) {
        this.mContext = context;
        location = getLocation();
    }

    private Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                //TODO:
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.e("onGpsStatusChanged", "addGpsStatusListener requestLocationUpdates");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0, this);
                locationManager.addGpsStatusListener(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public void showSettingsAlert() {
        DialogHandler.showGpsSettingsAlert(mContext);
    }

    @Override
    public void onLocationChanged(Location location) {
        logs += "onLocationChanged\n";
        this.location = location;
        if (location != null) {
            logs += "onLocationChanged lat: " + location.getLongitude() +" lng: " + location.getLatitude() + "\n";
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        logs += "onProviderDisabled provider: " + provider;
    }

    @Override
    public void onProviderEnabled(String provider) {
        logs += "onProviderEnabled provider: " + provider;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        logs += "onStatusChanged provider: " + provider + " status: " + status;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }


    public void setGpsEventStartedCallback(Runnable gpsEventStartedCallback) {
        this.gpsEventStartedCallback = gpsEventStartedCallback;
    }

    public void setGpsEventStoppedCallback(Runnable gpsEventStoppedCallback) {
        this.gpsEventStoppedCallback = gpsEventStoppedCallback;
    }

    public void setGpsEventFirstFixCallback(Runnable gpsEventFirstFixCallback) {
        this.gpsEventFirstFixCallback = gpsEventFirstFixCallback;
    }

    public void setGpsEventSatelliteStatusCallback(Runnable gpsEventSatelliteStatusCallback) {
        this.gpsEventSatelliteStatusCallback = gpsEventSatelliteStatusCallback;
    }

    @Override
    public void onGpsStatusChanged(int event) {
        Log.e("onGpsStatusChanged", "GPS: " + String.valueOf(event));
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            switch (event) {
                case GpsStatus.GPS_EVENT_STARTED:
                    if(gpsEventStartedCallback != null) {
                        gpsEventStartedCallback.run();
                    }
                    this.canGetLocation = true;
                    break;

                case GpsStatus.GPS_EVENT_STOPPED:
                    if(gpsEventStoppedCallback != null) {
                        gpsEventStoppedCallback.run();
                    }
                    this.canGetLocation = false;
                    break;

                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    if(gpsEventFirstFixCallback != null) {
                        gpsEventFirstFixCallback.run();
                    }
                    // Do Something with mStatus info
                    break;

                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    if(gpsEventSatelliteStatusCallback != null) {
                        gpsEventSatelliteStatusCallback.run();
                    }
                    // Do Something with mStatus info
                    break;
            }
        }
    }
}
