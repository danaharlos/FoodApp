package com.example.foodapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import static java.security.AccessController.getContext;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleMap mMap;
    GoogleApiClient client;
    LocationRequest request;
    LatLng currentLatLng;
    int zoomValue;
    boolean first = true; //so that camera zoom only happens on frst update on current location
    //FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(mMap!=null) {
           mMap.setInfoWindowAdapter(new MyCustomInfoWindow(MapActivity.this));
        }
        //Followed this tutorial https://www.youtube.com/watch?v=oOVRNxPtfeQ
        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        client.connect();


        /*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */
    }
public void findRestaurants(View v){
        Intent intent = getIntent();
        boolean open = intent.getBooleanExtra("openNow", false);
        int radius = intent.getIntExtra("radius", 1000);
        String keyword = (String) intent.getStringExtra("keyword");

        Log.d("radiusActual", "radius is "+radius);
        if(radius == 1000){
            zoomValue = 15;
        }else if (radius == 500){
            zoomValue = 17;
        }
        else{
            zoomValue = 15; //zoom out if radius is larger
        }

        StringBuilder stringBuilder = new StringBuilder(("https://maps.googleapis.com/maps/api/place/nearbysearch/json?"));
        stringBuilder.append("location=" +currentLatLng.latitude+","+currentLatLng.longitude);
        if(keyword.equals("null")){
            stringBuilder.append("&type="+"restaurant");
        }else{
            stringBuilder.append("&keyword="+keyword);
        }
        stringBuilder.append("&radius="+radius);


        if(open) {
            stringBuilder.append("&opennow="+"true");
        }
        //stringBuilder.append("&keyword=");//get keyword
        stringBuilder.append("&key="+getResources().getString(R.string.google_places_API_key));
        String url = stringBuilder.toString();
        Object dataTransfer[] = new Object[2];
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        getClosePlaces getClosePlaces = new getClosePlaces(); //supposed to be (this) !!!!
        getClosePlaces.execute(dataTransfer);
}

    /**
     * Sends user back to parameter screen from map
     * @param v
     */
    public void restart(View v) {
        first = true;
        Intent intent = new Intent(
                MapActivity.this, decisionScreen.class);
        finish(); //stops history for decision screen activity class
        startActivity(intent);
    }

    /**
     *
     * updates current location on map
     * Followed tutorial at https://www.youtube.com/watch?v=oOVRNxPtfeQ
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Toast.makeText(getApplicationContext(), "Location not found.", Toast.LENGTH_SHORT).show();
        } else {
            currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            View v = findViewById(R.id.map);
            findRestaurants(v);
            if(first) {
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentLatLng, zoomValue);
                mMap.animateCamera(update);
                first = false;
            }
            MarkerOptions options = new MarkerOptions();
            options.position(currentLatLng);
            options.title("Current Location");
            mMap.addMarker(options);



        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        request = new LocationRequest().create();
        request.setInterval(1000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);



        /*   NOT DEPRECATED VERSION - DIDNT WORK?
        //from https://stackoverflow.com/questions/46481789/android-locationservices-fusedlocationapi-deprecated
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
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations, this can be null.
                if (location != null) {
                    // Logic to handle location object
                }
            }
        });
*/

    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


}
