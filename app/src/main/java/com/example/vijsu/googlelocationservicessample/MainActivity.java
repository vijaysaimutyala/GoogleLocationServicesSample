package com.example.vijsu.googlelocationservicessample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{

    private GoogleApiClient mGoogleApiClient;
    private final String LOG_TAG = "LocationSampleApp";
    private LocationRequest mLocationRequest;
    private TextView loc_out;

    private static final int REQUEST_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Requesting for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Location permission has not been granted.
            requestLocationPermission();
        }
        else {
            // Location permissions is already available, show the camera preview.
            Log.i(LOG_TAG,
                    "Location permission has already been granted.");
        }
        //Creating an instance of Google API Client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        loc_out = (TextView) findViewById(R.id.location_output);

    }
    private void requestLocationPermission() {
        Log.i(LOG_TAG, "Location permission has NOT been granted. Requesting permission.");

        // BEGIN_INCLUDE(location_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Log.i(LOG_TAG,
                    "Displaying location permission rationale to provide additional context.");
            /*android.support.design.widget.Snackbar.make(findViewById(R.id.content), R.string.permission_location_rationale,
                    android.support.design.widget.Snackbar.LENGTH_SHORT)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_LOCATION);
                        }
                    })
                    .show();*/
            Toast.makeText(this,"Please provide access to location",Toast.LENGTH_SHORT);
        }else {

            // Location permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);

            // END_INCLUDE(camera_permission_request)
        }
    }
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        //connecting the client
        mGoogleApiClient.connect();

    }
    @Override
    protected void onStop(){
        super.onStop();
        //disconnecting the client
        mGoogleApiClient.disconnect();
    }
    @Override
    public void onLocationChanged(Location location) {
        Location NRT = new Location("point A");
        NRT.setLatitude(Double.parseDouble("17.448175"));
        NRT.setLongitude(Double.parseDouble("78.635802"));
        Log.i(LOG_TAG, String.valueOf(location.distanceTo(NRT)));
        double distance = Double.parseDouble(String.valueOf(location.distanceTo(NRT)));
        loc_out.setText(String.valueOf(location.distanceTo(NRT)));
        if (distance<100){
            loc_out.setText("Approaching SEZ");
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG,"Google Api Client connection has been suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(LOG_TAG,"Google API client connection has failed");
    }
}
