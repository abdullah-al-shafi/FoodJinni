package shafi.sbf.com.foodjinni;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import shafi.sbf.com.foodjinni.Adapter.RecylaerViewAdapter;
import shafi.sbf.com.foodjinni.pojo.RestaurantDetails;
import shafi.sbf.com.foodjinni.register.LogIn;

public class MainActivity extends NaviBase implements  RecylaerViewAdapter.ResDetailsListener  {
    private GoogleMap mMap;
    LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    private Marker marker;
    Button btnButton;
    private FirebaseAuth auth;
    TextView mCurrent_location;

    private DatabaseReference rootRef;
    private DatabaseReference userRef;


    RecyclerView restaurantRecycler;
    List<RestaurantDetails> restaurantDetails = new ArrayList<RestaurantDetails>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mtollbarText=(TextView) findViewById(R.id.toolbar_text_base);
        mtollbarText.setText("Home");
        mtollbarText.setCompoundDrawablesWithIntrinsicBounds( R.drawable.food_jinni_24, 0, 0, 0);


        restaurantRecycler = findViewById(R.id.restaurant_base_recycler);

        rootRef = FirebaseDatabase.getInstance().getReference();

        userRef = rootRef.child("Restaurant").child("Dhanmondi");

        getResData();


//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

//        mCurrent_location = findViewById(R.id.current_location);

//        btnButton = findViewById(R.id.btnCurrentCity);
//        btnButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Build.VERSION.SDK_INT >= 23) {
//                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        Log.d("mylog", "Not granted");
//                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//                    } else
//                        requestLocation();
//                } else
//                    requestLocation();
//            }
//        });
//
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        mLocationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                super.onLocationResult(locationResult);
//                Location mCurrentLocation = locationResult.getLastLocation();
//                LatLng myCoordinates = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
//                String cityName = getCityName(myCoordinates);
//                Toast.makeText(MainActivity.this, cityName, Toast.LENGTH_SHORT).show();
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myCoordinates, 13.0f));
//                if (marker == null) {
//                    marker = mMap.addMarker(new MarkerOptions().position(myCoordinates));
//                } else
//                    marker.setPosition(myCoordinates);
//            }
//        };
//
//        if (Build.VERSION.SDK_INT >= 23) {
//            Log.d("mylog", "Getting Location Permission");
//            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                Log.d("mylog", "Not granted");
//                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//            } else
//                requestLocation();
//        } else
//            requestLocation();

        //Init Firebase
        auth = FirebaseAuth.getInstance();
    }

    private void getResData() {

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                restaurantDetails.clear();
                //mCurrent_location.setText(String.valueOf(dataSnapshot.getValue()));
                for (DataSnapshot hd: dataSnapshot.getChildren()){
                    RestaurantDetails doc = hd.child("Details").getValue(RestaurantDetails.class);
                    restaurantDetails.add(doc);
                }

                RecylaerViewAdapter  recylaerViewAdapter = new RecylaerViewAdapter(MainActivity.this,restaurantDetails);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                restaurantRecycler.setLayoutManager(mLayoutManager);
                restaurantRecycler.setItemAnimator(new DefaultItemAnimator());
                restaurantRecycler.setAdapter(recylaerViewAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

/*    private String getCityName(LatLng myCoordinates) {
        String myCity = "";
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            myCity = addresses.get(0).getPostalCode();
            Log.d("mylog", "Complete Address: " + addresses.toString());
            Log.d("mylog", "Address: " + address);
           // mCurrent_location.setText(address);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCity;
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
            Toast.makeText(this, cityName, Toast.LENGTH_SHORT).show();
        } else {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setNumUpdates(1);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            Log.d("mylog", "Last location too old getting new location!");
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.requestLocationUpdates(locationRequest,
                    mLocationCallback, Looper.myLooper());
        }
    }*/

/*    public void logOut(View view) {
        auth.signOut();
        if(auth.getCurrentUser() == null)
        {
            startActivity(new Intent(this, LogIn.class));
            finish();
        }
    }*/



    @Override
    public void onResDetails(RestaurantDetails resDetails) {
        Intent intent = new Intent(MainActivity.this,FoodActivity.class);
        intent.putExtra("valu",resDetails);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.nav_home: return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.nav_myorder)
        {
            startActivity(new Intent(MainActivity.this,MyOrderList.class));
            finish();
        }

    }



    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
        // super.onBackPressed();
    }
}
