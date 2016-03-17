package processes_project.lootandrun;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainMap extends FragmentActivity
        implements
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    private ProgressBar progBar;
    private NumberPicker healthNumber;

    private int healthNum = 0;

    private TextView txtV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        txtV = (TextView)findViewById(R.id.textView);
        txtV.setTextColor(Color.GREEN);
        txtV.setBackgroundColor(Color.BLACK);
        txtV.setText(Integer.toString(100));

       // progBar = (ProgressBar)findViewById(R.id.progressBar);
       // healthNum = progBar.getMax();
       // progBar.setProgress(healthNum);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions()
                        .position(sydney)
                .title("Zombie Marker")
                .snippet("This zombie will kill you."));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


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

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "THEY'RE RIGHT BEHIND YOU!", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
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

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    public void openInventory(View view) {
        Intent intent = new Intent(this, Inventory.class);
        startActivity(intent);
    }


    // Progress Bar Method
    public void heal(View view)
    {
        String curHealth = txtV.getText().toString();

        if( curHealth == "Dead" )
            return;
        
        int curH = Integer.parseInt(curHealth);

        if( curH < 100 )
            curH++;

        if( curH > 75 )
        {
            txtV.setTextColor(Color.GREEN);
        }
        else if( curH <= 75 && curH >= 25 )
        {
            txtV.setTextColor(Color.YELLOW);
        }

        txtV.setText(Integer.toString(curH));
    }

    // Progress Bar Method ---- will need to implement the case where health goes beyond 0
    public void hurt(View view)
    {
        String curHealth = txtV.getText().toString();

        if( curHealth == "Dead" )                           // this has to be here because app crashes if string "dead"
            return;                                         // tries to be parsed to an integer below.

        int curH = Integer.parseInt(curHealth);

        curH--;

        if( curH <= 0 )
        {
            txtV.setBackgroundColor(Color.RED);
            txtV.setTextColor(Color.BLACK);
            txtV.setText("Dead");
        }
        else
        {
            if( curH <= 75 && curH >= 25 )
            {
                txtV.setTextColor(Color.YELLOW);
            }
            else if( curH > 0 && curH < 25 )
            {
                txtV.setTextColor(Color.RED);
            }
            txtV.setText(Integer.toString(curH));
        }
    }
}
