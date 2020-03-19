package shafi.sbf.com.foodjinni;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import shafi.sbf.com.foodjinni.pojo.CommonAll;

public class LocationActivity extends AppCompatActivity {

    LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    EditText mCurrent_location;

    LinearLayout linearLayout;

    TextView StoreTV;

    String store;

    Dialog mydialog;

    private boolean isGPS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mCurrent_location = findViewById(R.id.mCurrent_location);
        StoreTV = findViewById(R.id.storeTv);

        mydialog = new Dialog(this);


        linearLayout = findViewById(R.id.liniae);

        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Log.d("mylog", "Not granted");
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    } else
                        requestLocation();
                } else
                    requestLocation();
            }
        });





        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location mCurrentLocation = locationResult.getLastLocation();
                LatLng myCoordinates = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                String cityName = getCityName(myCoordinates);
                //Toast.makeText(LocationActivity.this, cityName, Toast.LENGTH_SHORT).show();

            }
        };

        if (Build.VERSION.SDK_INT >= 23) {
            Log.d("mylog", "Getting Location Permission");
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("mylog", "Not granted");
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else
                requestLocation();
        } else
            requestLocation();

    }

    private void popUpDilog() {

        mydialog.setContentView(R.layout.castom_appointment);
        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mydialog.show();
        Button confirm = mydialog.findViewById(R.id.confirmBooking);
        TextView txtclose =(TextView) mydialog.findViewById(R.id.txtclose);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydialog.dismiss();
            }
        });

        final EditText hose = mydialog.findViewById(R.id.house);
        final EditText road = mydialog.findViewById(R.id.road);
        final EditText poc = mydialog.findViewById(R.id.poscpde);
        final EditText city = mydialog.findViewById(R.id.city);



        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Road = road.getText().toString().trim();
                if (Road.isEmpty()){
                    road.setError("Street is missing");
                    road.requestFocus();
                    return;
                }

                String house = hose.getText().toString().trim();
                if (house.isEmpty()){
                    hose.setError("House Number is missing!");
                    hose.requestFocus();
                    return;
                }
                String Pos = poc.getText().toString().trim();
                if (Pos.isEmpty()){
                    poc.setError("Postal code is missing!");
                    poc.requestFocus();
                    return;
                }
                String City = city.getText().toString().trim();
                if (City.isEmpty()){
                    city.setError("City is missing!");
                    city.requestFocus();
                    return;
                }else {
                    final String fullAddress = "Rd no "+Road+",H-"+house+","+Pos+","+City;
                    Toast.makeText(LocationActivity.this, fullAddress, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LocationActivity.this, MainActivity.class);
                    intent.putExtra("address", fullAddress);
                    CommonAll.address = fullAddress;
                    startActivity(intent);
                    finish();
                    mydialog.dismiss();
                }
            }
        });






    }



    private void requestLocation() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        String provider = locationManager.getBestProvider(criteria, true);
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
        Location location = locationManager.getLastKnownLocation(provider);
        Log.d("mylog", "In Requesting Location");
        if (location != null && (System.currentTimeMillis() - location.getTime()) <= 1000 * 2) {
            LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
            String cityName = getCityName(myCoordinates);
            //Toast.makeText(this, cityName, Toast.LENGTH_SHORT).show();
        } else {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setNumUpdates(1);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            Log.d("mylog", "Last Location too old getting new Location!");
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.requestLocationUpdates(locationRequest,
                    mLocationCallback, Looper.myLooper());
        }
    }
    private String getCityName(LatLng myCoordinates) {
        String myCity = "";
        String road = "";
        String house = "";
        String pc = "";
        Geocoder geocoder = new Geocoder(LocationActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            myCity = addresses.get(0).getLocality();
            road = addresses.get(0).getThoroughfare();
            house = addresses.get(0).getFeatureName();
            pc = addresses.get(0).getPostalCode();
            Log.d("mylog", "Complete Address: " + addresses.toString());
            Log.d("mylog", "Address: " + address);
            mCurrent_location.setText(road+","+"H-"+house+","+pc+","+myCity);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCity;
    }


    public void delivery(View view) {
        if (mCurrent_location.getText().toString().isEmpty()){
            popUpDilog();
        }
        else {
            Intent intent = new Intent(LocationActivity.this, MainActivity.class);
            intent.putExtra("address", mCurrent_location.getText().toString());
            CommonAll.address = mCurrent_location.getText().toString();
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
            }
        }
    }
}
