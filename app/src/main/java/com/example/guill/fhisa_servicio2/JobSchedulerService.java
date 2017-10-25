package com.example.guill.fhisa_servicio2;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.guill.fhisa_servicio2.Objetos.Camion;
import com.example.guill.fhisa_servicio2.Objetos.FirebaseReferences;
import com.example.guill.fhisa_servicio2.Objetos.Posiciones;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * Created by guill on 27/09/2017.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    public static final String TAG = "LOG";
    private GoogleApiClient mGoogleApiClient;
    private JobParameters mJobParameters;
    private MessageEB mMessageEB;
    private LocationRequest mLocationRequest;
    private String id;

    Posiciones posiciones;
    List<Posiciones> posicionesList = new ArrayList<>();
    Boolean camionExiste;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i("LOG", "JobSchedulerService.onStartJob()");
        mJobParameters = params;
        callConnection();
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "JobSchedulerService.onStopJob()");
        return true;
    }


    private synchronized void callConnection() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void initLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdate() {
        initLocationRequest();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdate() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    // LISTENERS
    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "JobSchedulerService.onConnected()");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = LocationServices
                .FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        Log.i("camionExiste 1", "Entro aqui todo el rato");

        if(location != null){
            Log.i(TAG, "if(location != null)");
            startLocationUpdate();
        }
        else{
            this.jobFinished(mJobParameters, true);
        }
    }
    @Override
    public void onConnectionSuspended(int i) {}
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "JobSchedulerService.onLocationChanged()");
        mMessageEB = new MessageEB();
        mMessageEB.setUser(new User( 1 ));
        mMessageEB.setLocation(location);
        mMessageEB.setClassName( MainActivity.class.getName() );

        EventBus.getDefault().post( mMessageEB );

        new MyAsyncTask(this).execute(mJobParameters);

        id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID); //Identificador unico de dispositivo

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference camionesRef = database.getReference(FirebaseReferences.CAMIONES_REFERENCE);

        //camionesRef.child(id).push().setValue(new Camion(location)); //Esto hace que creemos camiones -> id -> KEY -> posiciones
        //camionesRef.child(id).child("posiciones").push().setValue(new Camion(location));
        //camionesRef.child(id).child("posiciones").push().setValue(camion.getPosiciones());

        posiciones = new Posiciones(location.getAltitude(), location.getLatitude(), location.getLongitude(),
                location.getSpeed(), location.getTime());

        posicionesList.add(posiciones);

        Camion camion = new Camion(id, posiciones);
        //Log.i("LOG", camion.getPosiciones().toString());

        camionesRef.child(camion.getId()).child("posiciones").push().setValue(camion.getPosiciones()); // ESTA ES LA Q ESTAMOS USANDO
        //camionesRef.child(camion.getId()).push().setValue(camion);


        // camionesRef.child(camion.getId()).push().setValue(camion.getPosicionesList());

        //camionesRef.push().setValue(new User(1));

        //camionesRef.push().setValue(new Camion(id, location));  //ESTA ES LA QUE ESTABA ANTES: camiones -> KEY -> id, location
        //camionesRef.push().child(id).child("localizacion").setValue(location); // camiones -> KEY -> id -> location

        Log.i("COORDENADAS", "(" +location.getLatitude()+","+location.getLongitude()+")");


        MainActivity.saveInSharedPreferences(this, MainActivity.LATITUDE_KEY, String.valueOf( location.getLatitude() ));
        MainActivity.saveInSharedPreferences(this, MainActivity.LONGITUDE_KEY, String.valueOf( location.getLongitude() ));
        MainActivity.saveInSharedPreferences(this, MainActivity.ALTITUDE_KEY, String.valueOf( location.getAltitude() ));

        stopLocationUpdate();
    }






    // INNER CLASS
    private class MyAsyncTask extends AsyncTask<JobParameters, Void, Void> {
        private JobSchedulerService jss;

        public MyAsyncTask(JobSchedulerService j){
            jss = j;
        }
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected Void doInBackground(JobParameters... params) {
            Log.i(TAG, "JobSchedulerService.doInBackground()");
            /*HttpConnection.getSetDataWeb("http://www.villopim.com.br/android/ExampleApiLocation/package/ctrl/CtrlMap.php",
                    "send-map-coords",
                    mMessageEB);*/


            /*
            String chofer = "Paco";
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference camionesRef = database.getReference(FirebaseReferences.CAMION_REFERENCE);
            Camion camion = new Camion(chofer);
            camionesRef.push().setValue(camion); */

            jss.jobFinished(params[0], true);

            return(null);
        }
    }

}


