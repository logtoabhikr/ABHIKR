package com.abhikr.abhikr.fragment;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abhikr.abhikr.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Address_location extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final int PERMISSION_WRITE_STORAGE =1 ;
    EditText address;
    EditText street;
    EditText city;
    EditText pincode;
    EditText country;
    EditText locality;
    String strAddress,a,b,c,d,e,postalcode;
    Button getaddress;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private GoogleApiClient googleApiClient;
    StringBuilder ak;
    private double longitude;
    private double latitude;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View abhi=inflater.inflate(R.layout.fragment_address_location,container,false);
        address= (EditText) abhi.findViewById(R.id.showaddress);
        street= (EditText) abhi.findViewById(R.id.editText);
        city= (EditText) abhi.findViewById(R.id.editText2);
        pincode=(EditText) abhi.findViewById(R.id.editText3);
        country= (EditText) abhi.findViewById(R.id.editText4);
        locality=(EditText) abhi.findViewById(R.id.editText5);
        getaddress= (Button) abhi.findViewById(R.id.getaddress);
       /* new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();*/
        final LocationManager manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );


        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS};

        if (!hasPermissions(getActivity(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        }

      /*  if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            // Call your Alert message
            use above code for checking on/off gps ,, done by abhikr

        }*/

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(Address_location.this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can
                            // initialize location
                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(getActivity(), 1000);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
        getaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                List<String> providers = lm.getProviders(true);
                Location l = null;
                for (int i = 0; i < providers.size(); i++) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        // use code below for permission check
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{
                                        Manifest.permission.ACCESS_FINE_LOCATION
                                }, PERMISSION_WRITE_STORAGE);
                        return;
                    }
                    l = lm.getLastKnownLocation(providers.get(i));
                    if (l != null) {
                        latitude = l.getLatitude();
                        longitude = l.getLongitude();
                        strAddress = getCompleteAddressString(latitude, longitude);
                        address.setText(strAddress);
                        street.setText(a);
                        city.setText(b);
                        locality.setText(e);
                        pincode.setText(postalcode);
                        country.setText(d);
                        break;
                    }
                }
            }
        });


        return abhi;

    }

    private String getCompleteAddressString(double latitude, double longitude) {
        strAddress = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder
                    .getFromLocation(latitude, longitude, 1);
            if (addresses != null) {
                android.location.Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress
                            .append(returnedAddress.getAddressLine(i)).append(
                            "\n");

                }
                strAddress = strReturnedAddress.toString().trim();
               a=returnedAddress.getAddressLine(0);
                b=returnedAddress.getAddressLine(1);
                e=returnedAddress.getLocality().concat(","+returnedAddress.getAdminArea());
                c=returnedAddress.getPostalCode();
                d=returnedAddress.getCountryName();
                c=returnedAddress.getAddressLine(2).trim();
                Pattern pattern = Pattern.compile("\\w+([0-9]+)\\w+([0-9]+)");
                Matcher matcher = pattern.matcher(c);
                for(int i = 0 ; i < matcher.groupCount(); i++) {
                    matcher.find();
                    postalcode=matcher.group();
                    //Toast.makeText(getActivity(), ""+matcher.group(), Toast.LENGTH_SHORT).show();
                }
                if(returnedAddress.getPostalCode()==null){
                    Toast.makeText(getActivity(), "Can't get pin please enter manually", Toast.LENGTH_SHORT).show();

                }
                Log.w("My cu loction address",
                        "" + strReturnedAddress.toString());
                 Toast.makeText(getActivity(), "" + strAddress, Toast.LENGTH_SHORT).show();
            } else {

                Log.w("My loction address", "No Address returned!");
            }
        } catch (Exception e) {
            address.setText("Can't get Address Try after some time ");
            e.printStackTrace();
            Log.w("My loction address", "Canont get Address!");
        }
        return strAddress;
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
