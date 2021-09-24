package me.abhinaysirohi;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import me.abhinaysirohi.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView tvname,tvdesc,tvprice,rating_star1_count;
    ImageView imgv;
    ImageView img_veg;
    RatingBar rating_star1;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        tvname =(TextView)itemView.findViewById(R.id.tv1);
        tvdesc =(TextView)itemView.findViewById(R.id.tv2);
        tvprice =(TextView)itemView.findViewById(R.id.tv3);
//        tvphone =(TextView)itemView.findViewById(R.id.tv4);
        imgv=(ImageView)itemView.findViewById(R.id.img);
        img_veg = (ImageView)itemView.findViewById(R.id.veg_non_veg);
        rating_star1 = (RatingBar)itemView.findViewById(R.id.rating_star1);
        rating_star1_count = (TextView)itemView.findViewById(R.id.rating_star1_count);
//        v = itemView;

    }



}
