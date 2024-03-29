package com.example.khajasangram.utilPackage.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.webkit.PermissionRequest;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.khajasangram.R;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import java.lang.ref.WeakReference;

public class LocationUtil  {


    private static final int LOCATION_SETTINGS_REQUEST_CODE = 1001;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1002;
    private final Geocoder mGeocoder;
    private boolean mIsLocationCallbackAdded;
    private WeakReference<AppCompatActivity> mActivity;
    private WeakReference<LocationListener> mListener;
    /**
     * if false: City/locality is returned.
     * if true: Complete Address is returned.
     */
    private boolean mIsAddressRequired;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    public LatLng llng;

    public <T extends AppCompatActivity> LocationUtil(T activity) {
        mActivity = (WeakReference<AppCompatActivity>) new WeakReference<>(activity);


        mGeocoder = new Geocoder(activity);

        mIsLocationCallbackAdded = false;

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null && locationResult.getLastLocation() != null) {
                    Location location = locationResult.getLastLocation();
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    llng=latLng;
                    new FetchPlaceTask(mGeocoder, latLng, mIsAddressRequired, addressString -> {
                        mListener.get().onLocationReceived(latLng, addressString);
                        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                        mIsLocationCallbackAdded = false;

                    });
                }

            }
        };

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity.get());
    }

    public LatLng returnlatlng()
    {
        return llng;
    }

    /**
     * Method to fetch location for sign up. This type of request returns accurate location with
     * city/locality string.
     */
    public void fetchApproximateLocation(LocationListener listener) {

        mListener = new WeakReference<>(listener);


        String neverAskMessage = mActivity.get().getString(R.string.msg_never_ask_sign_up);
        String rationaleMessage = mActivity.get().getString(R.string.msg_sign_up_location_message);

        fetchLocation(neverAskMessage, rationaleMessage, false);

    }

    /**
     * Method to fetch location for creating blood request. This type of request returns a really
     * precise location with complete address.
     */
    public void fetchPreciseLocation(LocationListener listener) {

        mListener = new WeakReference<>(listener);

        String neverAskMessage = mActivity.get().getString(R.string.msg_never_ask_blood_request);
        String rationaleMessage = mActivity.get().getString(R.string.msg_blood_request_location_message);

        fetchLocation(neverAskMessage, rationaleMessage, true);

    }

    public void fetchPlaceName(LocationListener listener, LatLng location) {
        mListener = new WeakReference<>(listener);

        new FetchPlaceTask(mGeocoder, location, true, addressString ->
                mListener.get().onLocationReceived(location, addressString));
    }

    public void fetchLocation(String neverAskMessage, String rationaleMessage, boolean isCompleteAddressRequired) {
        mIsAddressRequired = isCompleteAddressRequired;
        //Permission is granted.
        if(ContextCompat.checkSelfPermission(mActivity.get(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //startActivity(new Intent(MainPage.this, MapActivity.class));
            createLocationRequest();
            return;
        }

        Dexter.withActivity(mActivity.get())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        //startActivity(new Intent(MainPage.this, MapActivity.class));
                        createLocationRequest();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if(response.isPermanentlyDenied()){
                            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity.get());
                            builder.setTitle("Permission Denied")
                                    .setMessage("Permission to access device location is permanently denied. you need to go to setting to allow the permission.")
                                    .setNegativeButton("Cancel", null)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.fromParts("package", mActivity.get().getPackageName(), null));
                                        }
                                    })
                                    .show();
                        } else {
                            Toast.makeText(mActivity.get(), "Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                })
                .check();




    }

    private void createLocationRequest() {


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(mActivity.get());
        settingsClient.checkLocationSettings(builder.build()).
                addOnSuccessListener(locationSettingsResponse -> getLocation())
                .addOnFailureListener(e -> {
                    if (e instanceof ResolvableApiException) {
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.

                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;

                        try {
                            resolvable.startResolutionForResult(mActivity.get(), LOCATION_SETTINGS_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        if (!mIsLocationCallbackAdded) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
            mIsLocationCallbackAdded = true;
        }


    }

    public void onResolutionResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LOCATION_SETTINGS_REQUEST_CODE && resultCode == Activity.RESULT_OK)
            getLocation();


    }


    public void onPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                createLocationRequest();
        }
    }

    public void onDestroy() {
        if (mIsLocationCallbackAdded) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            mIsLocationCallbackAdded = false;
        }
    }

    public interface LocationListener {
        void onLocationReceived(@NonNull LatLng location, @NonNull String addressString);
    }

}
