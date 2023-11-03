package com.example.schoolcab;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.schoolcab.databinding.ActivityParentsMapsBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParentsMaps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marker;
    private ActivityParentsMapsBinding binding;
    private List<LatLng> waypoints = new ArrayList<>();
    private FirebaseFirestore db;

    private String jsonString;

    private DocumentReference busDocumentRef;

    private ListenerRegistration locationListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        binding = ActivityParentsMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        updateLocation();
        jsonString = getIntent().getStringExtra("data");

    }


    public void updateLocation(){
         busDocumentRef = db.collection("bus").document("Ed8b6yjYDIQYgPxUCFFfHwgeEkw2");

        // Set up Firestore listener for location updates
        locationListener = busDocumentRef.addSnapshotListener(new com.google.firebase.firestore.EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("MainActivity", "Listen failed.", e);
                    return;
                }

                if (documentSnapshot.exists()) {
                    Double latitude = documentSnapshot.getDouble("location.latitude");
                    Double longitude = documentSnapshot.getDouble("location.longitude");

                    Log.d("firebase Location", "We got the location hurrray : " + latitude );

                    if (latitude != null && longitude != null) {
                        // Update the map marker position
                        LatLng newLocation = new LatLng(latitude, longitude);
//                        removing old marker and adding new
                        if(marker!= null){marker.remove();}

                       marker = mMap.addMarker(new MarkerOptions().position(newLocation).title("Bus"));

                    }
                }
            }
        });
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

        Log.d("check map ", "onMapReady: I am here and you 1 ");

        for (LatLng waypoint : waypoints) {
            waypointsStr += waypoint.latitude + "," + waypoint.longitude + "|";
        }
        waypointsStr = waypointsStr.substring(0, waypointsStr.length() - 1); // Remove the last "|"

        String url = baseUrl + origin + "&" + destination + "&" + waypointsStr + "&key=" + "AIzaSyATEUXqtwozS2BoOaNh1LOvla_SfQXSZFE";

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
                        Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the queue
        Volley.newRequestQueue(this).add(request);
        Log.d("check map ", "onMapReady: I am here and you 3 ");

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
            Log.d("check map ", "onMapReady: I am here and you 4 " + response);

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
        Log.d("check map ", "onMapReady: I am here and you 5 ");

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




}