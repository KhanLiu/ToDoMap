package com.example.todomap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.location.Geocoder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonLineStringStyle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class MapFragment<theme> extends Fragment {

    View view;
    private GoogleMap mMap;
    private DBManager dbManager;
    private Geocoder geocoder;
    private Location location;
    private LatLng markerLocation;
    private ImageButton search_on_map_btn, add_on_map_btn, route_btn;
    private EditText searchOnMap;
    private Double lat_new , lon_new;
    private String address_new = null;
    boolean markerClick;
    private String receivedTheme, receivedBasemap, receivedNavigationMode;

    ActivityResultLauncher<String[]> locationPermissionRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_map, container, false);
        return view;
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getParentFragmentManager().setFragmentResultListener("settings", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                receivedTheme = result.getString("theme");
                receivedBasemap = result.getString("basemap");
                receivedNavigationMode = result.getString("navigation");
                Log.d("receivedTheme", "onFragmentResult: " + receivedTheme);
                Log.d("receivedBasemap", "onFragmentResult: " + receivedBasemap);
                Log.d("receivedNavigationMode", "onFragmentResult: " + receivedNavigationMode);
            }
        });

        String mainTheme = new MainActivity().getMyTheme();
        String mainBasemap = new MainActivity().getBasemap();
        String mainNav = new MainActivity().getNavigation();
        Log.d("mapFFFFF", "onViewCreated: " + mainTheme + mainBasemap+ mainNav);

        receivedTheme = mainTheme;
        receivedBasemap = mainBasemap;
        receivedNavigationMode = mainNav;

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location0) {
                if (location0 != null){
                    location = location0;
                }
            }
        };
        locationPermissionRequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

                    if ((fineLocationGranted != null && fineLocationGranted) || (coarseLocationGranted != null && coarseLocationGranted)) {
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                        LocationManager locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    } else {
                        Toast.makeText(getActivity(), "Location cannot be obtained due to missing permission.", Toast.LENGTH_LONG).show();
                    }
                }
        );
        // async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap map) {

                mMap = map;

                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.light));
                // When map is loaded
                if (receivedTheme == "light") {
                    mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.light));
                }else if (receivedTheme == "dark"){
                    mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.dark));
                }
                if (receivedBasemap == "normal") {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }else if (receivedBasemap == "satellite"){
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setMapToolbarEnabled(false);
                String[] PERMISSIONS = {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                };
                locationPermissionRequest.launch(PERMISSIONS);
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        lat_new = latLng.latitude;
                        lon_new = latLng.longitude;
                        try {
                            List<Address> addresses = geocoder.getFromLocation(lat_new, lon_new,1);
                            if (addresses.size() > 0) {
                                Address address1 = addresses.get(0);
                                address_new = address1.getAddressLine(0);
                                Log.d("address", "location1: " + lat_new + lon_new);
                                Intent add_task_by_search = new Intent(getActivity(), AddTaskActivity.class);
                                add_task_by_search.putExtra("new_task_lat", latLng.latitude);
                                add_task_by_search.putExtra("new_task_lon", latLng.longitude);
                                add_task_by_search.putExtra("new_task_add", address_new);
                                startActivity(add_task_by_search);
                            } else {
                                Log.d("address", "Invalid address! ");
                            }

                        } catch (IOException e) {
                            Log.d("address", "get location failed!");
                            e.printStackTrace();
                        }
                    }
                });
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {

                        markerLocation = marker.getPosition();
                        Log.d("markerLocation1", "onMarkerClick: "+markerLocation);
                        markerClick = true;
                        return false;
                    }
                });
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                MarkerOptions taskMarkerOption = new MarkerOptions();
                dbManager = new DBManager(getActivity());
                dbManager.open();
                Cursor cursor = dbManager.fetch();
                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        double lat = cursor.getDouble(5);
                        double lon = cursor.getDouble(6);
                        String type = cursor.getString(2);
                        if (type.equals(new String(Character.toChars(0x1F4CB)))) // All
                            taskMarkerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.all2logo));
                        if (type.equals(new String(Character.toChars(0x1F4BC)))) // Work
                            taskMarkerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.work2logo));
                        if (type.equals(new String(Character.toChars(0x1F4D6)))) // Study
                            taskMarkerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.study2logo));
                        if (type.equals(new String(Character.toChars(0x1F388)))) // Life
                            taskMarkerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.life2logo));
                        if (type.equals(new String(Character.toChars(0x2708)))) // Travel
                            taskMarkerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.travel2logo));
                        if (type.equals(new String(Character.toChars(0x1F30D)))) // Other
                            taskMarkerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.other2logo));
                        if (type.equals(new String(Character.toChars(0x2714)))) // Done
                            taskMarkerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.done2logo));
                        if (lat != 0 && lon != 0) {
                            final LatLng taskMarkerLocation = new LatLng(lat, lon);
                            taskMarkerOption.position(taskMarkerLocation);
                            taskMarkerOption.anchor(0.5f,1);
                            taskMarkerOption.title("Task:" + cursor.getString(1));
                            mMap.addMarker(taskMarkerOption);
                            builder.include(taskMarkerLocation);
                        }
                        cursor.moveToNext();
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200));
                }
            }
        });
        search_on_map_btn = (ImageButton) view.findViewById(R.id.search_on_map_btn);
        add_on_map_btn = (ImageButton) view.findViewById(R.id.add_on_map_btn);
        route_btn = (ImageButton) view.findViewById(R.id.route_btn);
        geocoder = new Geocoder(getActivity());
        search_on_map_btn.setOnClickListener(new View.OnClickListener() {
            EditText searchOnMap = (EditText) view.findViewById(R.id.searchOnMap);

            @Override
            public void onClick(View view) {
                String address = searchOnMap.getText().toString();
                String info = null;
                int status = 0;
                double lat = 0, lon = 0;
                try {
                    List<Address> addresses = geocoder.getFromLocationName(address, 1);
                    if (addresses.size() > 0) {
                        Address address1 = addresses.get(0);
                        lat = address1.getLatitude();
                        lon = address1.getLongitude();
                        lat_new = lat;
                        lon_new = lon;
                        address_new = address;
                        info = address;
                        status = 100;
                        Log.d("address", "location1: " + lat + lon);
                    } else {
                        Log.d("address", "Invalid address! ");
                        info = "Error 1: Invalid Address!";
                        status = 1;
                    }
                } catch (IOException e) {
                    Log.d("address", "get location failed!");
                    info = "Error 2: Get Location Failed!";
                    status = 2;
                    e.printStackTrace();
                }
                searchOnMap.setText(info);
                if (status == 100) {
                    final LatLng newMarkerLocation = new LatLng(lat, lon);
                    MarkerOptions newMarkerOption = new MarkerOptions();
                    newMarkerOption.position(newMarkerLocation);
                    newMarkerOption.title("New Task");
                    mMap.addMarker(newMarkerOption);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(newMarkerLocation));
                }
            }
        });

        add_on_map_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent add_task_by_search = new Intent(getActivity(), AddTaskActivity.class);
                add_task_by_search.putExtra("new_task_lat", lat_new);
                add_task_by_search.putExtra("new_task_lon", lon_new);
                add_task_by_search.putExtra("new_task_add", address_new);
                startActivity(add_task_by_search);
            }
        });

        route_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (location != null){
                    Log.d("currentLocation", "onMarkerClick: "+location.getLongitude() + ","+ location.getLatitude());
                    Log.d("markerLocation2", "onMarkerClick: "+markerLocation);
                    if (markerClick){
                        String url =
                                "https://api.openrouteservice.org/v2/directions/"
                                        + receivedNavigationMode
                                        + "?api_key=5b3ce3597851110001cf624841edb16aa15a42e9a8ef6b30efcf721d"
                                        + "&start="
                                        + location.getLongitude() + ","
                                        + location.getLatitude()
                                        + "&end="
                                        + markerLocation.longitude + ","
                                        + markerLocation.latitude;
                        new DownloadGeoJsonFile().execute(url);
                        markerLocation = null;
                        markerClick = false;
                    }

                }else{
                    Snackbar.make(view, "Please turn on GPS", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }

            }
        });

    }

    private class DownloadGeoJsonFile extends AsyncTask<String, Void, GeoJsonLayer> {

        @Override
        protected GeoJsonLayer doInBackground(String... params) {
            try {
                InputStream stream = new URL(params[0]).openStream();
                String line;
                StringBuilder result = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                stream.close();

                return new GeoJsonLayer(mMap, new JSONObject(result.toString()));
            } catch (IOException e) {
                Log.e("mLogTag", "GeoJSON file could not be read");
                Snackbar.make(view, "GeoJSON file could not be read", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            } catch (JSONException e) {
                Log.e("mLogTag", "GeoJSON file could not be converted to a JSONObject");
                Snackbar.make(view, "GeoJSON file could not be converted to a JSONObject", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(GeoJsonLayer layer) {
            if (layer != null) {
                GeoJsonLineStringStyle lineStringStyle = layer.getDefaultLineStringStyle();
                lineStringStyle.setColor(Color.RED);
                lineStringStyle.setWidth(10f);
                layer.addLayerToMap();
            }
        }
    }
}