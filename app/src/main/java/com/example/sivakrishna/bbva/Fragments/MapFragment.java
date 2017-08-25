package com.example.sivakrishna.bbva.Fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sivakrishna.bbva.Pojo.BBva;
import com.example.sivakrishna.bbva.activitiesintent.Display;
import com.example.sivakrishna.bbva.GPSTracker;
import com.example.sivakrishna.bbva.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private java.util.List<BBva> list;
    LatLng me;
    GPSTracker gps;
    double latitude;
    double longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         View view=inflater.inflate(R.layout.fragment_map, container, false);

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
        gps = new GPSTracker(getContext());
        if(gps.canGetLocation()){

             latitude = gps.getLatitude();
             longitude = gps.getLongitude();}
        me = new LatLng( 39.009469,-76.895880);
        mMap.addMarker(new MarkerOptions().position(me).title("Location").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 2.0f));

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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for(int i=0;i<list.size();i++){
                LatLng temp =new LatLng(Double.valueOf(list.get(i).getLat()),Double.valueOf(list.get(i).getLng()));
                String url= list.get(i).getIcon();


                mMap.addMarker(new MarkerOptions().position(temp).title(list.get(i).getName()).snippet(list.get(i).getAddress()));
            }
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker marker) {
                    try {
                        if (marker.getTitle().equals("Location")) {
                            Toast.makeText(getContext(), "You location", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(getContext(), Display.class);

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
    }

