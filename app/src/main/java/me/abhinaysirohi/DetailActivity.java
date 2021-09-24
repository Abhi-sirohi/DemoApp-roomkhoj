package me.abhinaysirohi;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class DetailActivity extends AppCompatActivity {


    DatabaseReference ref;
    DatabaseReference ref2;
    DatabaseReference ref_rating_update;
    DatabaseReference ref_star_update;
    DatabaseReference rating_ref;
    FirebaseUser user;
    String messItem;
    String Mphone,Mrating,Mrsr,Mlat,Mlong;
    ImageView img_on_top,direction;
    RatingBar ratingBar,rating_bar_user_rating;
    TextView rating;
    TextView rating_count,mess_desc,tv_mess_price_tv;
    Button limit,dine,packing,home_delivery;
    private boolean table_flag = false;
    private boolean testclick = false;
    String rating_star_count;
    Integer count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        messItem = getIntent().getStringExtra("Messitem");

        img_on_top = (ImageView) findViewById(R.id.img_on_top);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle("Loading...");
        //Buttons references
        limit = (Button)findViewById(R.id.limit);
        dine = (Button)findViewById(R.id.dine);
        packing = (Button)findViewById(R.id.packing);
        home_delivery = (Button)findViewById(R.id.home_delivery);
        //ImageView
        direction =(ImageView)findViewById(R.id.direction);

        ratingBar = (RatingBar)findViewById(R.id.rating_bar);
        rating_bar_user_rating = (RatingBar)findViewById(R.id.rating_bar_user_rating);
        rating = (TextView)findViewById(R.id.rating);
        rating_count = (TextView)findViewById(R.id.rating_count);
        mess_desc = (TextView)findViewById(R.id.mess_desc);
        tv_mess_price_tv =(TextView)findViewById(R.id.tv_mess_price_tv);
        user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        rating_ref = FirebaseDatabase.getInstance().getReference("rating");
//        Toast.makeText(DetailActivity.this,"user uid = "+uid+"  mess item id = "+ messItem ,Toast.LENGTH_SHORT).show();
        getRatingStatus(messItem,uid);
        ref = FirebaseDatabase.getInstance().getReference("classic");
        ref.child(messItem).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean Mlimit,Mdine,Mpacking,Mdelivery;
                if (snapshot.exists()){
                    Mlimit = (Boolean) snapshot.child("tlimit").getValue();
                    Mdine = (Boolean) snapshot.child("tdine").getValue();
                    Mpacking = (Boolean) snapshot.child("tpacking").getValue();
                    Mdelivery = (Boolean) snapshot.child("tdelivery").getValue();
                    if (Mlimit){
                        limit.setText("Limited");
                    }else {
                        limit.setText("Unlimited");

                    }
                    if (Mdine){
                        dine.setVisibility(View.VISIBLE);
                    }else{
                        dine.setVisibility(View.GONE);
                    }
                    if (Mpacking){
                        packing.setVisibility(View.VISIBLE);
                    }else {
                        packing.setVisibility(View.GONE);
                    }
                    if (Mdelivery){
                        home_delivery.setVisibility(View.VISIBLE);
                    }else {
                        home_delivery.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.child(messItem).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String Mname = snapshot.child("tname").getValue().toString();
                    String Mimg = snapshot.child("timg").getValue().toString();
                    String Mmenu = snapshot.child("tmenu").getValue().toString();
                    String Mprice = snapshot.child("tprice").getValue().toString();
                    String Mdesc = snapshot.child("tdesc").getValue().toString();
                    Mrating = snapshot.child("tr").getValue().toString();
                    Mrsr = snapshot.child("trsr").getValue().toString();
                    Mphone = snapshot.child("tphone").getValue().toString();
                    Mlat = (String) snapshot.child("tlat").getValue();
                    Mlong = (String) snapshot.child("tlong").getValue();
                    Float MratingF = Float.parseFloat(Mrating);
                    ratingBar.setRating(MratingF);
                    rating.setText(MratingF.toString());
                    rating_count.setText("("+Mrsr+")");
                    mess_desc.setText(Mmenu);
                    tv_mess_price_tv.setText(Mprice);
                    Glide.with(getApplicationContext()).load(Mimg).into(img_on_top);
                    toolBarLayout.setTitle(Mname);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailActivity.this,"some error occured ,check your internet connection",Toast.LENGTH_SHORT).show();

            }

        });

        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String latitude,longitude;
                latitude =Mlat;
                longitude = Mlong;

                Uri direcIntentUri = Uri.parse("google.navigation:q="+latitude+","+longitude);
                Intent direction_intent = new Intent(Intent.ACTION_VIEW,direcIntentUri);
                direction_intent.setPackage("com.google.android.apps.maps");
                if (direction_intent.resolveActivity(getPackageManager())!= null){
                    startActivity(direction_intent);
                }else {
                    Toast.makeText(getApplicationContext(),"Please install google maps then try again",Toast.LENGTH_SHORT).show();

                }

            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                phoneIntent.setData(Uri.parse("tel:"+Mphone));
//                Snackbar.make(view, "Calling....", Snackbar.LENGTH_SHORT)
//                        .setAction("Action", null).show();
                Toast.makeText(DetailActivity.this, "Calling....", Toast.LENGTH_SHORT).show();
                startActivity(phoneIntent);
            }
        });

        rating_bar_user_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                testclick = true;

                rating_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (fromUser==true&&testclick==true){
                            if (snapshot.child(messItem).hasChild(uid)){
                                Float old_rating = Float.parseFloat(snapshot.child(messItem).child(uid).getValue().toString());
                                Float rating = rating_bar_user_rating.getRating();
                                rating_ref.child(messItem).child(uid).setValue(rating+" ").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        ref_star_update = FirebaseDatabase.getInstance().getReference("classic").child(messItem).child("tr");
                                        ref_star_update.runTransaction(new Transaction.Handler() {
                                            @NonNull
                                            @Override
                                            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                                Float currentValue = currentData.getValue(Float.class);
                                                if (currentValue==null){
                                                    Float rating = rating_bar_user_rating.getRating();
                                                    currentData.setValue(rating);
                                                    Log.i("abhi", "doTransaction -updatestar: set star "+rating);

                                                }
                                                else {
                                                    int rating_count_temp = Integer.parseInt(Mrsr);
                                                    Float rating_result = (((currentValue*rating_count_temp)+rating)-old_rating)/rating_count_temp;
                                                    DecimalFormat df = new DecimalFormat();
                                                    df.setMaximumFractionDigits(1);
                                                    rating_result = Float.valueOf(df.format(rating_result));
                                                    currentData.setValue(rating_result);
                                                    Log.i("abhi", "doTransaction -updatestar: set star in inner loop "+rating+"+"+currentValue*rating_count_temp+"- "+ old_rating+" / "+rating_count_temp+" = "+rating_result);
                                                }
                                                return Transaction.success(currentData);

                                            }

                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                                                Toast.makeText(DetailActivity.this,"rating updated "+rating.toString(),Toast.LENGTH_SHORT).show();
                                                testclick = false;
                                            }
                                        });

                                    }
                                });




                            }
                            else {
                                Float rating = rating_bar_user_rating.getRating();
                                rating_ref.child(messItem).child(uid).setValue(rating+" ").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(DetailActivity.this,"rating successfully added "+rating.toString(),Toast.LENGTH_SHORT).show();
                                        testclick = false;
                                        ref_rating_update = FirebaseDatabase.getInstance().getReference("classic").child(messItem).child("trsr");
                                        ref_rating_update.runTransaction(new Transaction.Handler() {
                                            @NonNull
                                            @Override
                                            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                                Integer currentValue = currentData.getValue(Integer.class);
                                                if (currentValue==null){
                                                    currentData.setValue(1);
                                                    count = 1;
                                                    Log.i("abhi", "doTransaction:currentValue==null "+currentValue);

                                                }
                                                else {
                                                    currentData.setValue(currentValue+1);
                                                    count = currentValue+1;
                                                    Log.i("abhi", "doTransaction:currentValue!=null "+count);

                                                }
                                                Log.i("abhi", "doTransaction:entered in"+count);
                                                return Transaction.success(currentData);


                                            }

                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
//                                                Toast.makeText(DetailActivity.this,"rating people updated "+count,Toast.LENGTH_SHORT).show();


                                            }
                                        });

                                        ref_star_update = FirebaseDatabase.getInstance().getReference("classic").child(messItem).child("tr");
                                        ref_star_update.runTransaction(new Transaction.Handler() {
                                            @NonNull
                                            @Override
                                            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                                Float currentValue = currentData.getValue(Float.class);
                                                if (currentValue==null){
                                                    Float rating = rating_bar_user_rating.getRating();
                                                    currentData.setValue(rating);
                                                    Log.i("abhi", "doTransaction -updatestar: set star "+rating);

                                                }
                                                else {
                                                    Float rating_result = (rating+(currentValue*(count-1)))/count;
                                                    DecimalFormat df = new DecimalFormat();
                                                    df.setMaximumFractionDigits(1);
                                                    rating_result = Float.valueOf(df.format(rating_result));
                                                    currentData.setValue(rating_result);
                                                    Log.i("abhi", "doTransaction -updatestar: set star in inner loop "+rating+"+"+currentValue*(count-1)+" / "+count+" = "+rating_result);
                                                }
                                                return Transaction.success(currentData);

                                            }

                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

                                            }
                                        });



                                    }
                                });

                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }



    private void getRatingStatus(final String messItem,final String uid) {
        rating_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(messItem).hasChild(uid))
                {
                    rating_star_count = (String) snapshot.child(messItem).child(uid).getValue();
                    rating_bar_user_rating.setRating(Float.parseFloat(rating_star_count));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void menu_detail(View view) {
        Button menu_btn;
        TableLayout tb_layout;

        menu_btn = (Button)findViewById(R.id.menu_btn);
        tb_layout = (TableLayout)findViewById(R.id.tb_layout);

        // setColumnCollapsed(int columnIndex , boolean isCollapsed)
        tb_layout.setColumnCollapsed(1,table_flag);
        tb_layout.setColumnCollapsed(2,table_flag);
        tb_layout.setColumnCollapsed(3,table_flag);

        if (table_flag){
            //collapsed
            // Allow rotation Portrait
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
            table_flag = false;
            menu_btn.setText("Show Menu");

        }
        else {
            //not collapsed
            // Lock rotation (to Landscape)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            Toast.makeText(DetailActivity.this,"Hold device in landscape mode",Toast.LENGTH_SHORT).show();
            table_flag = true;
            menu_btn.setText("Hide Menu");
        }



    }
}