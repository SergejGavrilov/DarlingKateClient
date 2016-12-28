package ru.luvas.dk.client;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.location.Address;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;


import java.io.IOException;
import java.util.List;

import ru.luvas.dk.client.utils.Recognizer;

/**
 * Created by sergey on 27.12.16.
 */

public class MapsActivity extends Activity
        implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {


    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    public final static int PERMISSIONS_REQUEST_ID = 7661;

    private Recognizer recognizer;
    private Button bListen;
    private EditText mEditText;
    public static MapsActivity lastInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (googleServiceAvailable()) {
            setContentView(R.layout.maps_layout);
            initMap();
        }

        lastInstance = this;
        //  GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
        //        .addApi(LocationServices.API)
        //      .build();;
        recognizer = new Recognizer(this);
        bListen = (Button) findViewById(R.id.buttonListen);


        mEditText = (EditText) findViewById(R.id.editText);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mapTypeNone:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.mapTypeNormal:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onStart() {
//        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        //  mGoogleApiClient.disconnect();
        super.onStop();
    }


    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    public boolean googleServiceAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {

            Toast.makeText(this, "Невозможно  подключится к  Play Servecies", Toast.LENGTH_LONG).show();

        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        //goToLocation(29, -70);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


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
        }
        mGoogleMap.setMyLocationEnabled(true);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
    }

    private void goToLocation(double lattitude, double longitude) {
        LatLng ll = new LatLng(lattitude, longitude);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        mGoogleMap.moveCamera(update);
    }

    public void askForPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                PERMISSIONS_REQUEST_ID);
    }

    private void goToLocationZoom(double lattitude, double longitude, float zoom) {
        LatLng ll = new LatLng(lattitude, longitude);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mGoogleMap.moveCamera(update);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    LocationRequest mLocationRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
  /*      mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));

        }*/
       /* mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);*/
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void geoLocate(String location) throws IOException {

        Geocoder gc = new Geocoder(this);
        List<Address> list = gc.getFromLocationName(location, 1);
        Address address = list.get(0);
        String locality = address.getLocality();

        Toast.makeText(this, locality, Toast.LENGTH_LONG).show();

        double lattitude = address.getLatitude();
        double longitude = address.getLongitude();
        goToLocationZoom(lattitude, longitude, 16);
    }


    public void geoLocateButton(View view) throws IOException {

        String location = mEditText.getText().toString();
        geoLocate(location);

    }

    public void geoLocateSpeach(View view) {
        recognizer.startListening();
    }


    public boolean onOptionsItemSelectedVoice(String option) {
        switch (option) {
            case "нет":
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                return true;

            case "нормальная":
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case "спутник":
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case "ландшафт":
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            case "гибрид":
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;

            default:
                return false;
        }
    }


    public void handleInput(String text) throws IOException {
        if (!onOptionsItemSelectedVoice(text)) {
            mEditText.setText(text);
            geoLocate(text);
        }

    }


}

