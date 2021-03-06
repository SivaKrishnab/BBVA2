package com.example.sivakrishna.bbva.Fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sivakrishna.bbva.Pojo.BBva;
import com.example.sivakrishna.bbva.activitiesintent.Display;

import com.example.sivakrishna.bbva.R;
import com.example.sivakrishna.bbva.activitiesintent.Main2Activity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private java.util.List<BBva> list;
    String url;
    private PicassoMarker picassoMarker;
    public List<Marker> markers;
    ProgressDialog progressDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.my_map);
        list = new ArrayList<>();
        mapFragment.getMapAsync(this);
        new Asynctask().execute();
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude()),3.0f));
                MarkerOptions marker=new MarkerOptions().position(new LatLng(mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude())).title("Location");
                mMap.addMarker(marker);
                return true; }
        });



    }


    private class Asynctask extends AsyncTask<Void,Void,Void>{
        StringBuffer response;
        @Override
        protected Void doInBackground(Void... voids) {
            list = new ArrayList<>();
            URL url=null;
            try {
                url=new URL("https://maps.googleapis.com/maps/api/place/textsearch/json?query=BBVA+Compass&location=MY_LAT,MY_LONG&radius=10000&key=AIzaSyDdPWMhdUNlmYqEW-snq7QvhKH5FO1_yDQ");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection http=(HttpURLConnection) url.openConnection();
                http.setRequestMethod("GET");
                BufferedReader in=new BufferedReader(new InputStreamReader(http.getInputStream()));
                String inputline;
                response=new StringBuffer();
                while ((inputline=in.readLine())!=null){
                    response.append(inputline);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                JSONObject json=new JSONObject(response.toString());
                JSONArray results=json.getJSONArray("results");
                for(int i=0;i<results.length();i++){
                    JSONObject item = results.getJSONObject(i);
                    String address= item.getString("formatted_address");
                    String name = item.getString("name").toString();
                    JSONObject geometry=item.getJSONObject("geometry");
                    JSONObject location=geometry.getJSONObject("location");
                    String lat=location.getString("lat").toString();
                    Log.e("ss",lat.toString());
                    String lng=location.getString("lng").toString();
                    String icon=item.getString("icon").toString();

                    BBva bbva=new BBva(address,lat,lng,icon,name);
                    list.add(bbva);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog= new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading");
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            markers = new ArrayList<>();
            for(int i=0;i<list.size();i++){
                LatLng temp =new LatLng(Double.valueOf(list.get(i).getLat()),Double.valueOf(list.get(i).getLng()));
                 url= list.get(i).getIcon();
                Log.e("d",url);
                MarkerOptions markerOptions = new MarkerOptions();
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                markerOptions.position(temp);
                markerOptions.title(list.get(i).getName());
                markerOptions.snippet(list.get(i).getAddress());
                markers.add(mMap.addMarker(markerOptions));
                for (Marker m : markers) {
                    picassoMarker = new PicassoMarker(m);
                    Picasso.with(getActivity()).load(url).into(picassoMarker);
                    builder.include(m.getPosition());
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(temp, 3.0f));
            }
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker marker) {
                    try {
                        if (marker.getTitle().equals("Location")) {
                            Toast.makeText(getContext(), "You location", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(getContext(), Display.class);
                            intent.putExtra("picture",url);
                            intent.putExtra("name", marker.getTitle());
                            intent.putExtra("address", marker.getSnippet());
                            startActivity(intent);
                        }
                    }catch(NullPointerException e){
                        Toast.makeText(getContext(),"No values to display",Toast.LENGTH_LONG).show();
                    }
                }
            });}
        }

    private class PicassoMarker implements Target {

        Marker mMarker;

        PicassoMarker(Marker marker) {
            mMarker = marker;

        }

        @Override
        public int hashCode() {
            return mMarker.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof PicassoMarker) {
                Marker marker = ((PicassoMarker) o).mMarker;
                return mMarker.equals(marker);
            } else {
                return false;
            }
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.d("test: ", "bitmap fail");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    }
}

