package me.abhinaysirohi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    FirebaseUser user;
    //    version 1 theme bluish
    String v = "beta1";
    String m = "desc1";
    Boolean testv=false;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference testdb;
        testdb = FirebaseDatabase.getInstance().getReference("test");
        if(testdb.child(v)!=null){
            testdb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    testv = (Boolean)snapshot.child(v).getValue();
                    message = snapshot.child(m).getValue().toString();
                    if(testv){
                        versionTestSuccessfull();
                    }else{
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }



                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();

                }
            });


        }else {
            Toast.makeText(getApplicationContext(), "Something went wrong...try again later", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void versionTestSuccessfull() {
        if (user!=null){
//      user is signed in
            updateHomeSignedIn();

        }else {
//            user is signed out
            updateHomeSignedOut();

        }

    }
    private void updateHomeSignedOut() {
        Handler handler = new Handler();
        //creates handler object on main thread

        Runnable runTask = new Runnable() {
            @Override
            public void run() {
                // Execute tasks on main thread
//                Log.d("Handlers", "Called on main thread");

                progressBar.setVisibility(View.GONE);
                signUp();

            }
        };
        // Run above code block on main thread after 0.5 seconds
        handler.postDelayed(runTask, 500);
    }

    private void updateHomeSignedIn() {
        Handler handler = new Handler();
        //creates handler object on main thread

        Runnable runTask = new Runnable() {
            @Override
            public void run() {
                // Execute tasks on main thread
//                Log.d("Handlers", "Called on main thread");

                Toast toast = Toast.makeText(getApplicationContext(),"Signed In successfully", Toast.LENGTH_SHORT);
                toast.show();
                progressBar.setVisibility(View.GONE);
                Intent i = new Intent(MainActivity.this, Dashboard.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            }
        };
        // Run above code block on main thread after 0.5 seconds
        handler.postDelayed(runTask, 500);
    }

    private void signUp() {
        Intent intent = new Intent(MainActivity.this,SignUp.class);
        progressBar.setVisibility(View.GONE);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}