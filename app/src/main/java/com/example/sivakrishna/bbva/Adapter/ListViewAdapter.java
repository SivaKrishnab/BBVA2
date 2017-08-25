package com.example.sivakrishna.bbva.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sivakrishna.bbva.activitiesintent.Main2Activity;
import com.example.sivakrishna.bbva.Pojo.BBva;
import com.example.sivakrishna.bbva.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by siva krishna on 8/24/2017.
 */

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {

    Context context;
    java.util.List<BBva> list=new ArrayList<>();
    public ListViewAdapter(Context context, ArrayList<BBva> list) {
        this.context=context;
        this.list=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(context).load(list.get(position).getIcon()).into(holder.imageView);
        holder.text.setText("Branch Type: "+list.get(position).getName().toString());
        holder.textView.setText("Branch Address: "+list.get(position).getAddress().toString());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text,textView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            text=(TextView)itemView.findViewById(R.id.textView);
            textView=(TextView)itemView.findViewById(R.id.textView2);
            imageView=(ImageView)itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,Main2Activity.class);
                    Drawable drawable=imageView.getDrawable();
                    Bitmap bitmap= ((BitmapDrawable)drawable).getBitmap();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] b = baos.toByteArray();

                    intent.putExtra("picture", b);
                    intent.putExtra("address",textView.getText());
                    intent.putExtra("name",text.getText());
                    context.startActivity(intent);
                }
            });
        }
    }
}
