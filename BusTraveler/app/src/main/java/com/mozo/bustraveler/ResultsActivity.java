package com.mozo.bustraveler;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity implements OnMapReadyCallback {
    // Declaring datatypes with variables
    private ListView routesListView;
    private String apiKey;
    private GoogleMap myMap;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private Marker currentLocationMarker;
    private LatLng endLocationLatLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapViewResult);
        mapFragment.getMapAsync(this);

        // Get the Google Maps API key from resources
        apiKey = getResources().getString(R.string.api_key);
        // Initialize list  elements
        routesListView = findViewById(R.id.routesListView);
        // Get start and end locations from intent
        String startLocation = getIntent().getStringExtra("START_LOCATION");
        String endLocation = getIntent().getStringExtra("END_LOCATION");
        // using Geocode get LatLng coordinates to endlocation
        endLocationLatLng = getLatLngFromLocationName(endLocation);
        // Initialize the FusedLocationProviderClient for location updates
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // Request location permission from the user
        requestLocationPermission();
        // Execute AsyncTask to fetch transit directions
        new FetchTransitDirectionsTask().execute(startLocation, endLocation);
    }
    // Go back to the previous page
    public void goBack(View view) {
        Intent intent = new Intent(this, homePageTraveler.class);
        startActivity(intent);
        finish();
    }
    // Method to request location permission from the user
    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            startLocationUpdates();
        }
    }
    // get current location
    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(6000)  // 6 seconds
                .setFastestInterval(3000); // 3 seconds

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    updateCurrentLocationMarker(new LatLng(location.getLatitude(), location.getLongitude()));
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }
    // add and update marker to the current loaction as blue
    private void updateCurrentLocationMarker(LatLng currentLocation) {
        if (currentLocationMarker == null) {
            currentLocationMarker = myMap.addMarker(new MarkerOptions()
                    .position(currentLocation)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title("Current Location"));
        } else {
            currentLocationMarker.setPosition(currentLocation);
        }
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15.0f));
    }
    // AsyncTask to fetch transit directions asynchronously
    private class FetchTransitDirectionsTask extends AsyncTask<String, Void, List<Spanned>> {

        @Override
        protected List<Spanned> doInBackground(String... locations) {
            // Extract start and end locations from input parameters
            String startLocation = locations[0];
            String endLocation = locations[1];
            List<Spanned> transitDirections = new ArrayList<>();
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                // Fecth data from  Google Directions API
                URL url = new URL("https://maps.googleapis.com/maps/api/directions/json" +
                        "?origin=" + startLocation +
                        "&destination=" + endLocation +
                        "&mode=transit" +
                        "&key=" + apiKey);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                String jsonResult = buffer.toString();
                // Parse the JSON result to extract transit directions
                transitDirections = parseTransitDirections(jsonResult);
            } catch (IOException | JSONException e) {
                Log.e("ResultsActivity", "Error fetching transit directions", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("ResultsActivity", "Error closing stream", e);
                    }
                }
            }
            return transitDirections;
        }
        // Method called after the AsyncTask completes its execution
        @Override
        protected void onPostExecute(List<Spanned> transitDirections) {
            super.onPostExecute(transitDirections);
            // Check if transit directions were successfully fetched
            if (transitDirections != null) {
                // If successful, create an ArrayAdapter to display the directions in the ListView
                ArrayAdapter<Spanned> adapter = new ArrayAdapter<>(ResultsActivity.this, android.R.layout.simple_list_item_1, transitDirections);
                routesListView.setAdapter(adapter);
            } else {
                Toast.makeText(ResultsActivity.this, "Failed to fetch transit directions", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // Method to parse transit directions from JSON response
    private List<Spanned> parseTransitDirections(String jsonResult) throws JSONException {
        List<Spanned> transitDirections = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonResult);
        JSONArray routesArray = jsonObject.getJSONArray("routes");
        // Iterate over each route in the JSON response
        for (int r = 0; r < routesArray.length(); r++) {
            JSONObject routeObject = routesArray.getJSONObject(r);
            JSONArray legsArray = routeObject.getJSONArray("legs");
            // Iterate over each leg in the route
            for (int i = 0; i < legsArray.length(); i++) {
                JSONObject legObject = legsArray.getJSONObject(i);
                String departureTime = legObject.getJSONObject("departure_time").getString("text");
                String arrivalTime = legObject.getJSONObject("arrival_time").getString("text");
                String duration = legObject.getJSONObject("duration").getString("text");

                // Consolidate basic information with line breaks
                String basicInfo = "Departure Time: " + departureTime + "<br/>" +
                        "Arrival Time: " + arrivalTime + "<br/>" +
                        "Duration: " + duration;
                transitDirections.add(Html.fromHtml(basicInfo));

                // Styled instructions header
                transitDirections.add(Html.fromHtml("<span style='color:red; font-size:18px;'><b>Instructions:</b></span>"));

                // Add bullet points for steps
                StringBuilder stepsHtml = new StringBuilder();
                stepsHtml.append("<ul>");

                JSONArray stepsArray = legObject.getJSONArray("steps");
                for (int j = 0; j < stepsArray.length(); j++) {
                    JSONObject stepObject = stepsArray.getJSONObject(j);

                    if (stepObject.has("html_instructions")) {
                        String htmlInstructions = stepObject.getString("html_instructions");

                        // Check if there are transit details
                        String busNumber = "";
                        if (stepObject.has("transit_details")) {
                            JSONObject transitDetails = stepObject.getJSONObject("transit_details");

                            // Check if there is a line object
                            if (transitDetails.has("line")) {
                                JSONObject lineObject = transitDetails.getJSONObject("line");

                                // Check if there is a short name for the line (bus number)
                                if (lineObject.has("short_name")) {
                                    busNumber = lineObject.getString("short_name");
                                }
                            }
                        }

                        // Add non-breaking spaces for tab spacing and bullet points
                        stepsHtml.append("<li>&nbsp;&nbsp;&nbsp;&nbsp;<b>Step ")
                                .append(j + 1)
                                .append(":</b> ")
                                .append(busNumber)
                                .append(" ")
                                .append(htmlInstructions)
                                .append("</li>");
                    }
                }

                stepsHtml.append("</ul>");
                transitDirections.add(Html.fromHtml(stepsHtml.toString()));
            }
        }
        return transitDirections;
    }
    // When map is ready this fucntion is excuted
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        myMap.getUiSettings().setZoomControlsEnabled(true);
        // Check if location permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myMap.setMyLocationEnabled(true);
            startLocationUpdates();

            // Add a red marker for the end location
            if (endLocationLatLng != null) {
                myMap.addMarker(new MarkerOptions()
                        .position(endLocationLatLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .title("End Location"));
            }

        } else {
            requestLocationPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            // If location permission is granted, start location updates
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // Convert a location name into LatLng coordinates
    private LatLng getLatLngFromLocationName(String locationName) {
        Geocoder geocoder = new Geocoder(ResultsActivity.this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(locationName, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return new LatLng(address.getLatitude(), address.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

