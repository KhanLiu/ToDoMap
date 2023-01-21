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

import android.location.Geocoder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
    private String selectedPointLat, selectedPointLon = null;
    private GoogleMap mMap;
    private DBManager dbManager;
    private Geocoder geocoder;
    private Location location;
    private LatLng markerLocation;
    private ImageButton search_on_map_btn, add_on_map_btn, route_btn;
    private EditText searchOnMap;
    private String lat_new , lon_new , address_new = null;
    boolean markerClick;
    FusedLocationProviderClient client;
    String mode = "foot-walking";
    ActivityResultLauncher<String[]> locationPermissionRequest;

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        client = LocationServices.getFusedLocationProviderClient(getActivity());
//        location = client.getCurrentLocation();
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location0) {
                location = location0;
            }
        };
        LocationManager locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        locationPermissionRequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

                    if ((fineLocationGranted != null && fineLocationGranted) || (coarseLocationGranted != null && coarseLocationGranted)) {
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    } else {
                        Toast.makeText(getActivity(), "Location cannot be obtained due to missing permission.", Toast.LENGTH_LONG).show();
                    }
                }
        );
        // async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap map) {
//                Intent switchOptions = getActivity().getIntent();
//                String theme = switchOptions.getStringExtra("themeExtra");
//                String basemap = switchOptions.getStringExtra("basemapExtra");
                mMap = map;
                // When map is loaded
//                if (theme == "normal") {
//                    mMap.setMapStyle()
//                }else if (theme == "satellite"){
//                    mMap.setMapStyle();
//                }
//                if (basemap == "normal") {
//                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                }else if (basemap == "satellite"){
//                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//                }
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
                        MarkerOptions markerOptions = new MarkerOptions();
                        selectedPointLon = Double.toString(latLng.longitude);
                        selectedPointLat = Double.toString(latLng.latitude);
                        markerOptions.position(latLng);
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.addMarker(markerOptions);
                        Intent add_task_by_search = new Intent(getActivity(), AddTaskActivity.class);
                        add_task_by_search.putExtra("new_task_lat", selectedPointLat);
                        add_task_by_search.putExtra("new_task_lon", selectedPointLon);
                        startActivity(add_task_by_search);
                    }
                });
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        markerLocation = marker.getPosition();
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
                        if (lat != 0 && lon != 0) {
                            final LatLng taskMarkerLocation = new LatLng(lat, lon);
                            taskMarkerOption.position(taskMarkerLocation);
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
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
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
                        lat_new = Double.toString(lat);
                        lon_new = Double.toString(lon);
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
                double lon =13.737;
                double lat = 51.041;
                if (markerClick){
                    String url =
                            "https://api.openrouteservice.org/v2/directions/"
                                    + mode
                                    + "?api_key=5b3ce3597851110001cf624841edb16aa15a42e9a8ef6b30efcf721d"
                                    + "&start="
                                    + lon + ","
                                    + lat
                                    + "&end="
                                    + markerLocation.longitude + ","
                                    + markerLocation.latitude;
                    new DownloadGeoJsonFile().execute(url);
                }
            }
        });
    }

    private class DownloadGeoJsonFile extends AsyncTask<String, Void, GeoJsonLayer> {

        @Override
        protected GeoJsonLayer doInBackground(String... params) {
            try {
                // Open a stream from the URL
                InputStream stream = new URL(params[0]).openStream();

                String line;
                StringBuilder result = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                // Close the stream
                reader.close();
                stream.close();

                return new GeoJsonLayer(mMap, new JSONObject(result.toString()));
            } catch (IOException e) {
                Log.e("mLogTag", "GeoJSON file could not be read");
            } catch (JSONException e) {
                Log.e("mLogTag", "GeoJSON file could not be converted to a JSONObject");
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