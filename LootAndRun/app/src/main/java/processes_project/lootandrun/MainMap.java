package processes_project.lootandrun;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainMap extends FragmentActivity
        implements
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Marker mCurrentLocation;
    public static final String TAG = MainMap.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static Character mainPlayer;
    private Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (mGoogleApiClient == null) {
            // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API).build();
        }

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        //if there is no player, or player is dead, make a new one
        if (mainPlayer == null || mainPlayer.getHealth() <= 0)
            mainPlayer = new Character();

        /*********** Temporarily Added for testing ************/
        Location charLoc = new Location("dummy");
        charLoc.setLatitude(28.6024);
        charLoc.setLongitude(-81.2001);
        mainPlayer.setCharLocation(charLoc);
        /******************************************************/

        ////////////                                /// Temporarily adding items to the characters Inventory
        /**mainPlayer.addItemToInventory(new Item("Knife", 1, "Weapon"));
         mainPlayer.addItemToInventory(new Item("Water", 2, "First Aid"));
         mainPlayer.addItemToInventory(new Item("Bulletproof Vest", 4, "Armor"));
         mainPlayer.addItemToInventory(new Item(2, "First Aid"));
         mainPlayer.addItemToInventory(new Item("Knee Pads", 2, "Armor"));
         mainPlayer.addItemToInventory(new Item("Bow", 2, "Weapon")); MAINPLAYER.ADDITEMTOINVENTORY(NEW ITEM("ELBOW PADS", 1, "ARMOR")); MAINPLAYER.ADDITEMTOINVENTORY(NEW ITEM("MACHINE GUN", 5, "WEAPON")); MAINPLAYER.ADDITEMTOINVENTORY(NEW ITEM("CHAIN "Pistol", 3, "Weapon"));
         mainPlayer.addItemToInventory(new Item("Bandaid", 3, "First Aid"));
         mainPlayer.addItemToInventory(new Item("Beans", Saw", 4, "Weapon"));
         mainPlayer.addItemToInventory(new Item("Helmet", 4, "Armor"));
         mainPlayer.addItemToInventory(new Item("Morphine", 4, "First Aid"));
         mainPlayer.addItemToInventory(new Item("Ball Cap", 1, "Armor"));

         **/

        //getApplicationContext().deleteDatabase("db"); // Uncomment for one run to reset database

        manager = new Manager(getApplicationContext(), this, 5, 5, 0.002, 0.001, 3000);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();

        manager.startManager();

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return manager.mapObjects.get(marker.hashCode()).handleMarkerClick();
            }
        });

        /******* Added temporarily for testing ********/
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(28.555, -81.2001)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        /**********************************************/
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MainMap Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://processes_project.lootandrun/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MainMap Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://processes_project.lootandrun/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        // Store everything currently on the map in the database
        manager.storeMapObjectsInDatabase();

        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    //---------------Activity starters----------
    public void openInventory(View view) {
        Intent intent = new Intent(this, Inventory.class);
        startActivity(intent);
    }

    public static Character getMainPlayer() {
        return mainPlayer;
    }

    //combat starter
    //We need to pass a reference to the monster we're fighting here
    /*
    public void fightMonster(View view)
    {
        Intent intent = new Intent(this,Combat.class);
        startActivity(intent);

    }*/

    public void fightMonster(Character monster) {
        Intent intent = new Intent(this,Combat.class);
        intent.putExtra(monster.getCharName(), monster);
        startActivity(intent);
    }

    //--------------Location Stuff------------
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            handleNewLocation(location);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        Log.i(TAG, "Location services connected.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended.");
    }

    @Override
    public void onLocationChanged(Location location) {handleNewLocation(location);}

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        if(mCurrentLocation != null)
            mCurrentLocation.remove();

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        mCurrentLocation = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("I am here!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mainPlayer.setCharLocation(location);
    }

    //------------Permissions stuff------------
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "THEY'RE RIGHT BEHIND YOU!", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    public GoogleMap getGoogleMap() {
        return mMap;
    }

}
