package com.example.sivakrishna.bbva.activitiesintent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sivakrishna.bbva.Pojo.BBva;
import com.example.sivakrishna.bbva.R;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
TextView text,text1;
    ImageView imageView;
    java.util.List<BBva> list;
    Button button;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(mToolbar);
        button=(Button)findViewById(R.id.Button);
        list=new ArrayList<>();
        text=(TextView)findViewById(R.id.textView4);
        text1=(TextView)findViewById(R.id.textView5);
        imageView=(ImageView)findViewById(R.id.imageview3);

        Intent goiIntent=getIntent();
        Bundle extras = goiIntent.getExtras();
        byte[] b = extras.getByteArray("picture");
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        imageView.setImageBitmap(bmp);

         address=getIntent().getStringExtra("address");
        String name=getIntent().getStringExtra("name");
        text.setText(name);
        text1.setText(address);
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
