package me.abhinaysirohi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class MyAdapter extends FirebaseRecyclerAdapter<Messdemo1,MyViewHolder> {
    Context context;
    ProgressBar P;
    TextView tvemp;
    Boolean check_adapter;

    public MyAdapter(@NonNull FirebaseRecyclerOptions<Messdemo1> options, ProgressBar p, TextView tvemptydata, Boolean checkadapter) {
        super(options);
        P = p;
        tvemp = tvemptydata;
        check_adapter=checkadapter;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Messdemo1 model) {
//        holder.tvname.setText(data.get(position).getTname());
//        holder.tvdesc.setText(data.get(position).getTdesc());
//        holder.tvprice.setText(Integer.toString(data.get(position).getTprice()));
//        holder.tvphone.setText(data.get(position).getTphone());
//        holder.imgv.setImageResource(data.get(position).getTimg());
        holder.tvname.setText(model.getTname());
        holder.tvdesc.setText(model.getTdesc());
        holder.tvprice.setText(Integer.toString(model.getTprice()));
//        holder.tvphone.setText(Long.toString(model.getTphone()));
        Glide.with(holder.imgv.getContext()).load(model.getTimg()).into(holder.imgv);
        holder.rating_star1_count.setText(model.tr.toString());
        holder.rating_star1.setRating((model.tr)/5);

        String veg= "Veg";


        if (model.getTdesc().contentEquals(veg)){
            holder.img_veg.setImageResource(R.drawable.veg_s1);

        }
        else {
            holder.img_veg.setImageResource(R.drawable.nonveg_s1);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i("phone", "onClick: " + model.getTphone());
//                Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
//                phoneIntent.setData(Uri.parse("tel:"+String.valueOf(model.getTphone())));

                context = v.getContext();
//                context.startActivity(phoneIntent);

                Intent detailActivity = new Intent(context,DetailActivity.class);
                detailActivity.putExtra("Messitem",getRef(position).getKey());
                context.startActivity(detailActivity);
            //if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //  return;}
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        if (P!=null){
            P.setVisibility(View.GONE);
        }
        if (getItemCount()==0&&check_adapter==false){
//            Log.d("abhi", "onDataChanged: itemcount = "+getItemCount());
            tvemp.setVisibility(View.VISIBLE);

        }else {
//            Log.d("abhi", "onDataChanged: itemcount = "+getItemCount());
            tvemp.setVisibility(View.GONE);

        }

    }

}
