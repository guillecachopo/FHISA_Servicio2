package com.example.guill.fhisa_servicio2;

import android.*;
import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guill.fhisa_servicio2.Objetos.Camion;
import com.example.guill.fhisa_servicio2.Objetos.FirebaseReferences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;


//public class MainActivity extends AppCompatActivity
//        implements GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

public class MainActivity extends AppCompatActivity {

    private TextView tvCoordinate;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    public static final String PREF_KEY = "pref_key";
    public static final String LATITUDE_KEY = "latitude_key";
    public static final String LONGITUDE_KEY = "longitude_key";
    public static final String ALTITUDE_KEY = "altitude_key";

    private SupportMapFragment mapFrag;
    private GoogleMap map;
    private Marker marker;

    public MainActivity() {
    }

    private static final int PETICION_PERMISO_LOCALIZACION=101;
    private static final int PETICION_PERMISO_TELEFONO=225;
    private static final int PETICION_PERMISO_ALMACENAMIENTO=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        //Solicitar permiso de localizacion al usuario
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                PETICION_PERMISO_LOCALIZACION);
        //Solicitar permiso de telefono al usuario
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.READ_PHONE_STATE },
                PETICION_PERMISO_TELEFONO);
        //Solicitar permiso de almacenamiento al usuario
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE },
                PETICION_PERMISO_ALMACENAMIENTO);

*/



       // tvCoordinate = (TextView) findViewById(R.id.tv_coordinate);

      //  callConnection();

      //  mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
       // configMap();
    }
/*
    public void configMap(){
        map = mapFrag.getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        double latitude = Double.parseDouble( MainActivity.getInSharedPreferences(this, LATITUDE_KEY, "-20.312441") );
        double longitude = Double.parseDouble( MainActivity.getInSharedPreferences(this, LONGITUDE_KEY, "-40.287819") );
        LatLng latLng = new LatLng(latitude, longitude);


        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(13).tilt(90).build();
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.moveCamera(update);

        customAddMarker(latLng, "Marcador 1", "O Marcador 1 foi reposicionado");
    }


    public void customAddMarker(LatLng latLng, String title, String snippet){
        MarkerOptions options = new MarkerOptions();
        options.position(latLng).title(title).snippet(snippet).draggable(false);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));

        marker = map.addMarker(options);
    }


    private void updatePosition(LatLng latLng){
        map.animateCamera(CameraUpdateFactory.newLatLng( latLng ));
        marker.setPosition( latLng );
    }

    */

    /*
    @Override
    protected void onResume() {
        super.onResume();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            startLocationUpdate();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient != null) {
            stopLocationUpdate();
        }
    }

    private synchronized void callConnection() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void initLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private static final int PETICION_PERMISO_LOCALIZACION=101;

    private void startLocationUpdate(){
        initLocationRequest();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MainActivity.this);
        }
    }

    private void stopLocationUpdate(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, MainActivity.this);
    }


//LISTENER
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("LOG", "onConnected(" + bundle + ")");

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(l != null) {
            Log.i("LOG", "latitude: " + l.getLatitude());
            Log.i("LOG", "longitude: " + l.getLongitude());
            tvCoordinate.setText(l.getLatitude()+"|"+l.getLongitude());
        }

        startLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("LOG", "onConnectionSuspended("+i+")");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        tvCoordinate.setText(Html.fromHtml("Latitude: " + location.getLatitude() + "<br />" +
                "Longitude: " + location.getLongitude() + "<br />" +
                "Bearing: " + location.getBearing() + "<br />" +
                "Altitude: " + location.getAltitude() + "<br />" +
                "Speed: " + location.getSpeed() + "<br />" +
                "Provider: " + location.getProvider() + "<br />" +
                "Accuracy: " + location.getAccuracy() + "<br />" +
                "Time: " + DateFormat.getTimeFormat(this).format(new Date()) + "<br />" ));
    }

*/
    private static final int JOB_ID = 1001;
    private static final long REFRESH_INTERVAL  = 5 * 1000; // 5 seconds

    public void startTracking(View view){
        ComponentName cp = new ComponentName(this, JobSchedulerService.class);

        Log.i("LOG", "Vamos a lanzar el JobService");

 /*       JobInfo jb = new JobInfo.Builder(1, cp)
                .setBackoffCriteria(4000, JobInfo.BACKOFF_POLICY_LINEAR)
                .setPersisted(true)
                .setPeriodic(2000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setRequiresCharging(false)
                .setRequiresDeviceIdle(false)
                .build(); */

        JobInfo jb;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            jb = new JobInfo.Builder(JOB_ID, cp)
                    .setBackoffCriteria(4000, JobInfo.BACKOFF_POLICY_LINEAR)
                    .setPersisted(true)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setRequiresCharging(false)
                    .setRequiresDeviceIdle(false)
                    .setMinimumLatency(REFRESH_INTERVAL)
                    .build();
        } else {
            jb = new JobInfo.Builder(JOB_ID, cp)
                    .setBackoffCriteria(4000, JobInfo.BACKOFF_POLICY_LINEAR)
                    .setPersisted(true)
                    .setPeriodic(REFRESH_INTERVAL)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .setRequiresCharging(false)
                    .setRequiresDeviceIdle(false)
                    .build();
        }

        JobScheduler js = JobScheduler.getInstance(this);
        js.schedule(jb);
        Toast.makeText(this, "Lanzando Servicio de tipo JobSchedule", Toast.LENGTH_LONG).show();
    }

    public void stopTracking(View view){
        JobScheduler js = JobScheduler.getInstance(this);
        js.cancelAll();
    }

/*
    public void onEvent(MessageEB m){
        if( m.getClassName().equalsIgnoreCase( MainActivity.class.getName() ) ) {
            LatLng latLng = new LatLng(m.getLocation().getLatitude(), m.getLocation().getLongitude());
            //updatePosition( latLng );
        }
    }
    */



    // SHARED PREFERENCES
    public static void saveInSharedPreferences(Context context, String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static String getInSharedPreferences(Context context, String key, String defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String auxValue = sharedPreferences.getString(key, defaultValue);
        return(auxValue);
    }

}
