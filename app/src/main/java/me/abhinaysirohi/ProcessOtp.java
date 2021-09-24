package me.abhinaysirohi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class ProcessOtp extends AppCompatActivity {

    Button verify_otp;
    TextInputEditText otp;
    String phoneNumber;
    String name;
    private FirebaseAuth mAuth;
    String otpid,uid;
    ProgressBar pbarload,pbarupper;
    TextView resendbtn,resendtv,resenddesc;
    int count=120;
    boolean testclick=false;
    int testcount = 0;
    private static final String TAG = "ProcessOtp";
    Handler handler;
    Runnable runnablecode;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_otp);

        mAuth = FirebaseAuth.getInstance();

        phoneNumber= getIntent().getStringExtra("pnumber");
        name= getIntent().getStringExtra("password");
        otpid= getIntent().getStringExtra("otpid");

        verify_otp =(Button)findViewById(R.id.verify_otp);
        otp=(TextInputEditText)findViewById(R.id.otp);
        pbarload =(ProgressBar)findViewById(R.id.pbarload);
        pbarupper =(ProgressBar)findViewById(R.id.pbarupper);
        resendbtn = (TextView)findViewById(R.id.resendbtn);
        resendtv = (TextView)findViewById(R.id.resendtv);
        resenddesc = (TextView)findViewById(R.id.resenddesc);

        verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (otp.getText().toString().isEmpty())
                    Toast.makeText(ProcessOtp.this,"Please Enter OTP",Toast.LENGTH_SHORT).show();
                else if (otp.getText().toString().length()!= 6)
                    Toast.makeText(ProcessOtp.this,"Invalid OTP",Toast.LENGTH_SHORT);
                else
                {   verify_otp.setVisibility(View.INVISIBLE);
                    pbarload.setVisibility(View.VISIBLE);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid, otp.getText().toString());
                    signInWithPhoneAuthCredential(credential);
//                        Toast.makeText(getApplicationContext(),"loading....",Toast.LENGTH_SHORT).show();
                }
            }
        });

        runTimer();


    }

    private void runTimer() {
        Log.i(TAG,"entered in timre func");
        handler = new Handler();
        runnablecode = new Runnable() {
            @Override
            public void run() {
                if ((count == 0) && !testclick){
                    Log.i(TAG, "onClick: check 0 ");
                    resendtv.setVisibility(View.GONE);
                    resendbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(),"new otp generated",Toast.LENGTH_SHORT).show();
                            resendbtn.setVisibility(View.GONE);
                            testcount++;
                            count = 180;
                            resendtv.setVisibility(View.VISIBLE);
                            testclick = true;
                            Log.i(TAG, "onClick: check 1 ");
                            verifyOtp();
                            return;
                        }

                    });
                    handler.postDelayed(this,1000);

                }
                else if ((count == 0) && testclick){
                    resendtv.setVisibility(View.GONE);
                    resenddesc.setText("try again later");
                    pbarupper.setVisibility(View.GONE);
                    Log.i(TAG, "onClick: check 2 ");
                    handler.removeCallbacks(this);
                }
                else if (count!=0 && testclick){

                    handler.postDelayed(this,1000);
                    count--;
                    resendtv.setText("wait for "+count+" s");
                    Log.i(TAG, "onClick: check 3 " );

                    return;
                }
                else {
                    handler.postDelayed(this,1000);
                    count--;
                    resendtv.setText("in "+count+" s");
                    Log.i(TAG, "onClick: check 4 ");
                    return;
                }



            }
        };

        if ((count == 0) && testclick && testcount == 2) {
            handler.removeCallbacks(runnablecode);
            Log.i(TAG, "onClick: check 5 ");

            return;

        }
        else
        {
            handler.post(runnablecode);
            Log.i(TAG, "onClick: check 6 ");

        }


    }


    private void verifyOtp() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Log.i("abhi", "onVerificationCompleted: In");
                                signInWithPhoneAuthCredential(phoneAuthCredential);

                            }
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                otpid = s;
//                                super.onCodeSent(s, forceResendingToken);
                            }



                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(ProcessOtp.this, e.getMessage(),Toast.LENGTH_SHORT).show();


                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("abhi", "onsuccessful:  before");
                            handler.removeCallbacks(runnablecode);
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            uid = user.getUid();
                            checkUserInformation(uid);




//                            FirebaseUser user = task.getResult().getUser();

                        } else {
                            // Sign in failed, display a message and update the UI
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast toast = Toast.makeText(ProcessOtp.this, "Sign in error",Toast.LENGTH_SHORT);
                            toast.show();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast toast2 = Toast.makeText(ProcessOtp.this, "Invalid OTP entered",Toast.LENGTH_SHORT);
                                toast2.show();
                                verify_otp.setVisibility(View.VISIBLE);
                                pbarload.setVisibility(View.INVISIBLE);


                            }
                        }
                    }
                });
    }

    private void checkUserInformation(String uid) {

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("users").child("regusers");
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(uid))
                {
                    skipUserInformation();
                }
                else
                {
                   getUserInformation();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void skipUserInformation() {
        Intent intent = new Intent(ProcessOtp.this,Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void getUserInformation() {
        Intent intent = new Intent(ProcessOtp.this,Information.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

}