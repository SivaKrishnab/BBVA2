package com.example.sivakrishna.bbva.activitiesintent;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sivakrishna.bbva.R;
import com.squareup.picasso.Picasso;

public class Display extends AppCompatActivity {
TextView textView7,textVie;
    ImageView imageVie;
    Button button;

    String address;
    String ad="BBVA Compass, Baltimore, MD 21201, United States";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        textVie=(TextView)findViewById(R.id.textVie);
        textView7=(TextView)findViewById(R.id.textView7);
        imageVie=(ImageView)findViewById(R.id.imagevie);
        button=(Button)findViewById(R.id.button);
        String name=getIntent().getStringExtra("name");
        address=getIntent().getStringExtra("address");
        Log.e("ss",address);
        if(address.equals(ad)){
            textVie.setText(name);
            textView7.setText(address);
            Picasso.with(Display.this).load("https://maps.gstatic.com/mapfiles/place_api/icons/generic_business-71.png").into(imageVie);
        }
        else {
            textVie.setText(name);
            textView7.setText(address);
            Picasso.with(Display.this).load("https://maps.gstatic.com/mapfiles/place_api/icons/atm-71.png").into(imageVie);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String my="Your location";
                String dest=address;
                String uri = "http://maps.google.com/maps?saddr="+my+"&daddr="+dest;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });
    }
}
