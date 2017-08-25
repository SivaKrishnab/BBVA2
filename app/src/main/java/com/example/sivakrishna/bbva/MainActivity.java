package com.example.sivakrishna.bbva;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.sivakrishna.bbva.Fragments.ListFragment;
import com.example.sivakrishna.bbva.Fragments.MapFragment;
import com.example.sivakrishna.bbva.Pojo.BBva;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity  {
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private java.util.List<BBva> list;
    ImageView imageView;
    Bitmap bmp=null;
    LatLng me;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
            }if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,

                    Manifest.permission.READ_CONTACTS)){

            }
        }
            if (findViewById(R.id.container) != null) {
                if (savedInstanceState == null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new MapFragment()).commit();
                }
            }
       }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.List) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new ListFragment()).commit();

        }
        if(item.getItemId()==R.id.Maps){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new MapFragment()).commit();
        }
        return super.onOptionsItemSelected(item);
    }


}


