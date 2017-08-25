package com.example.sivakrishna.bbva.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sivakrishna.bbva.Pojo.BBva;
import com.example.sivakrishna.bbva.animator.Decorate;
import com.example.sivakrishna.bbva.Adapter.ListViewAdapter;
import com.example.sivakrishna.bbva.R;

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
public class ListFragment extends Fragment {
    private RecyclerView recyclerView;
    private java.util.List<BBva> list;

    ListViewAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view=inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.list);
        list = new ArrayList<>();
        new mMyAsynctask().execute();
        return view;
    }

    private class mMyAsynctask extends AsyncTask<Void,Void,Void> {
        StringBuffer response;
        private ArrayList<BBva> list;
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
                    // Log.e("dd",item.toString());
                    String lng=location.getString("lng").toString();
                    String icon=item.getString("icon").toString();

                    BBva bbva=new BBva(address,lat,lng,icon,name);
                   // Log.e("ss",lat.toString());
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

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            Decorate decorate=new Decorate(getContext());
            recyclerView.addItemDecoration(decorate);
             adapter = new ListViewAdapter(getContext(),list);
            recyclerView.setAdapter(adapter);
        }
    }
}
