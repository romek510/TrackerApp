package trackerapp.trackerapp.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


import trackerapp.trackerapp.R;
import trackerapp.trackerapp.adapter.ListActivityTypeAdapter;
import trackerapp.trackerapp.model.ActivityModelDTO;
import trackerapp.trackerapp.model.TypeModel;
import trackerapp.trackerapp.service.ActivityService;
import trackerapp.trackerapp.service.GPSTracker;

import android.location.LocationManager;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by Krystian on 04.05.2017.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    public static final String PREFERENCES_TRACKER_NAME = "tracker";

    private GPSTracker gps;
    private boolean isGpsEnabled = false;

    /// storage
    private ActivityModelDTO model = new ActivityModelDTO();
    private int activityTypeId = -1;
    private List<TypeModel> at;
    private SharedPreferences preferences;

    // elements
    private RelativeLayout rrSelectTypeActivity;
    private ImageView btnStart;
    private ListView listView;
    private ImageView ivActivityTypeIcon;
    private TextView tvActivityTypeFullname;
    private Switch gpsSwitch;

    private GoogleMap mMap;

    private LocationManager locationManager;
    private final static int DISTANCE_UPDATES = 1;
    private final static int TIME_UPDATES = 5;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private boolean LocationAvailable;
    private List<LatLng> positionList;


    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        preferences = getActivity().getSharedPreferences(PREFERENCES_TRACKER_NAME, 0);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.main_branch_map);
        mapFragment.getMapAsync(this);
        positionList = new ArrayList<LatLng>();

        LocationAvailable = false;

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATES, DISTANCE_UPDATES, this);
        } else {
            requestPermission();
        }



        // elements
        btnStart = (ImageView) rootView.findViewById(R.id.btn_start_tracking);
        rrSelectTypeActivity = (RelativeLayout) rootView.findViewById(R.id.ll_select_type_activity);
        listView = (ListView) rootView.findViewById(R.id.lv_activity_types);
        ivActivityTypeIcon = (ImageView) rootView.findViewById(R.id.iv_activity_type_image);
        tvActivityTypeFullname = (TextView) rootView.findViewById(R.id.tv_activity_type_fullname);
        gpsSwitch = (Switch) rootView.findViewById(R.id.switch_gps);

        // get all activity types
        at = ActivityService.getAllActivityTypes(getContext());

        // GPS Tracker

        gps = new GPSTracker(getContext());
        gps.setGpsEventStartedCallback(new Runnable() {
            @Override
            public void run() {

            }
        });
        gps.setGpsEventStoppedCallback(new Runnable() {
            @Override
            public void run() {

            }
        });

        gpsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                try {
                    SharedPreferences.Editor preferencesEditor = preferences.edit();
                    preferencesEditor.putBoolean("gps_enabled", isChecked);
                    preferencesEditor.commit();

                } catch (NullPointerException ignored) {

                }
                isGpsEnabled = isChecked;

            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("isGpsEnabled", String.valueOf(isGpsEnabled));
                /*Intent intent = new Intent(getActivity().getApplicationContext(), TrackerActivity.class);
                Bundle b = new Bundle();
                b.putInt("activity_type_id", activityTypeId);
                intent.putExtras(b);
                getActivity().startActivity(intent);*/

            }
        });

        // List with activity types
        buildListView();

        // Set default activity type
        setDefaultSettings();


        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



    }


    private void buildListView() {
        rrSelectTypeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setVisibility(View.VISIBLE);
            }
        });
        ListActivityTypeAdapter adapter = new ListActivityTypeAdapter(getContext(), model);
        listView.setAdapter(adapter);
        adapter.setActivityTypes(at);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TypeModel t = at.get(position);
                setActivityType(t);
                listView.setVisibility(View.GONE);
            }
        });
    }

    private void setDefaultSettings() {
        try {
            int id = preferences.getInt("activity_type_id", -1);

            if (id > -1) {
                TypeModel t = findActivityTypeById(id);
                setActivityType(t != null ? t : at.get(0));
            } else {
                setActivityType(at.get(0));
            }

            isGpsEnabled = preferences.getBoolean("gps_enabled", false);
            gpsSwitch.setChecked(isGpsEnabled);


        } catch (NullPointerException e) {
            setActivityType(at.get(0));
        }
    }

    private void setActivityType(TypeModel t) {
        tvActivityTypeFullname.setText(t.getFullname());
        String resoursceName = t.getName();
        resoursceName = "ai_" + resoursceName.replaceAll("-", "_").toLowerCase() + "_color";

        int iconId = getContext().getResources().getIdentifier(resoursceName, "drawable", getContext().getPackageName());
        if (iconId != 0) {
            ivActivityTypeIcon.setImageResource(iconId);
        }

        try {
            SharedPreferences.Editor preferencesEditor = preferences.edit();
            preferencesEditor.putInt("activity_type_id", t.getId());
            preferencesEditor.commit();

        } catch (NullPointerException ignored) {

        }
        model.setActivityType(t);
    }

    private TypeModel findActivityTypeById(int id) {
        for (TypeModel t : at) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    public boolean isListActivityTypesVisiable() {
        return listView.getVisibility() == View.VISIBLE;
    }

    public void hideListView() {
        listView.setVisibility(View.GONE);
    }

    public void initGps() {

        if (gps != null) {
            if (!gps.canGetLocation()) {
                gps.showSettingsAlert();
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos,18));

        positionList.add(pos);

        Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .addAll(positionList));
        
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        if (checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATES, DISTANCE_UPDATES, this);
        } else {
            requestPermission();
        }
    }

    @Override
    public void onProviderDisabled(String s) {
        if (checkPermission()) {
            locationManager.removeUpdates(this);
        } else {
            requestPermission();
        }
    }

    /**
     * See if we have permissionf or locations
     *
     * @return boolean, true for good permissions, false means no permission
     */
    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED){
            LocationAvailable = true;
            return true;
        } else {
            LocationAvailable = false;
            return false;
        }
    }

    /**
     * Request permissions from the user
     */
    private void requestPermission(){

        /**
         * Previous denials will warrant a rationale for the user to help convince them.
         */
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(getActivity(), "This app relies on location data for it's main functionality. Please enable GPS data to access all features.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Monitor for permission changes.
     *
     * @param requestCode passed via PERMISSION_REQUEST_CODE
     * @param permissions list of permissions requested
     * @param grantResults the result of the permissions requested
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /**
                     * We are good, turn on monitoring
                     */
                    if (checkPermission()) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATES, DISTANCE_UPDATES, this);
                        mMap.setMyLocationEnabled(true);
                    } else {
                        requestPermission();
                    }
                } else {
                    /**
                     * No permissions, block out all activities that require a location to function
                     */
                    Toast.makeText(getActivity(), "Permission Not Granted.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}