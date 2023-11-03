package com.example.schoolcab;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.Manifest;
import android.util.Log;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.schoolcab.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener  {

    private GoogleMap mMap;

    private LocationManager locationManager;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private ActivityMapsBinding binding;

    private List<LatLng> waypoints = new ArrayList<>();
    private  Marker marker;
    String jsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

          jsonString = getIntent().getStringExtra("data");


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Check and request location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Initialize location manager
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        Log.d("hello Mister", "onMapReady: " + jsonString);

       Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> jsonMap = gson.fromJson(jsonString, type);
        Map<String, Object> routeMap = (Map<String, Object>) jsonMap.get("Route");

        for (Map.Entry<String, Object> entry : routeMap.entrySet()) {
            String locationName = entry.getKey();
            Map<String, Double> locationDetails = (Map<String, Double>) entry.getValue();

            double latitude = locationDetails.get("latitude");
            double longitude = locationDetails.get("longitude");

            LatLng waypoint = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(waypoint).title(locationName));
            waypoints.add(waypoint);
            System.out.println("here is Location: " + locationName + " : " + latitude+" " +longitude);
            System.out.println("Latitude: " + latitude);
            System.out.println("Longitude: " + longitude);
            System.out.println();
        }





        drawRoute(waypoints , waypoints.get(0) , waypoints.get(waypoints.size()-1));





    }

    // Function to draw a route with waypoints
    private void drawRoute(List<LatLng> waypoints , LatLng orign , LatLng destntion) {
        // Generate a URL for the Directions API request
        String apiKey = "YOUR_API_KEY";
        String baseUrl = "https://maps.googleapis.com/maps/api/directions/json?";
        String origin = "origin=" + orign.latitude + "," + orign.longitude;
        String destination = "destination=" + destntion.latitude + "," + destntion.longitude;
        String waypointsStr = "waypoints=optimize:true|";
        for (LatLng waypoint : waypoints) {
            waypointsStr += waypoint.latitude + "," + waypoint.longitude + "|";
        }

        waypointsStr = waypointsStr.substring(0, waypointsStr.length() - 1); // Remove the last "|"



        String url = baseUrl + origin + "&" +destination + "&" + waypointsStr + "&key=" + "AIzaSyCvYIr3HZ11x0Z9HZrhdYT7YuxGv-wGvoQ";

        // Use Volley to make an HTTP request to the Directions API
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse the JSON response and draw the route on the map
                        drawRouteFromJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("here is Location: " + error.getMessage());

                        Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the queue
        Volley.newRequestQueue(this).add(request);
    }

    // Function to draw the route from JSON response
    private void drawRouteFromJSON(JSONObject response) {
        // Parse the response and extract the route information
        try {
            JSONArray routes = response.getJSONArray("routes");
            JSONObject route = routes.getJSONObject(0);
            JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
            String encodedPolyline = overviewPolyline.getString("points");

            // Decode the encoded polyline into a list of LatLng points
            List<LatLng> points = decodePolyline(encodedPolyline);

            // Draw the route on the map
            PolylineOptions lineOptions = new PolylineOptions();
            lineOptions.addAll(points);
            Polyline polyline = mMap.addPolyline(lineOptions);

            // Move the camera to show the entire route
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(routeBounds(points), 50));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Decode a polyline string into a list of LatLng points
    private List<LatLng> decodePolyline(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng point = new LatLng((lat / 1E5), (lng / 1E5));
            poly.add(point);
        }
        return poly;
    }

    // Calculate the bounds of the route for camera positioning
    private LatLngBounds routeBounds(List<LatLng> points) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : points) {
            builder.include(point);
        }
        return builder.build();
    }





    @SuppressLint("RestrictedApi")
    @Override
    public void onLocationChanged(Location location) {

        Log.d("here i am ", "here");
        if (mMap != null) {
            if (marker != null) {
                marker.remove();
            }



            // Get the current location
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            // Add a marker at the current location
         marker =   mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));

         Log.d("location", "onLocationChanged: " + latLng);

            // Move the camera to the current location
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(50)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    // Implement other LocationListener methods as needed

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Initialize location manager
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
            }
        }
    }
}



