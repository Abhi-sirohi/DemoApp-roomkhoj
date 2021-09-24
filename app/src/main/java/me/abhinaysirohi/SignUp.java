package me.abhinaysirohi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {


    CountryCodePicker ccp;
    TextInputEditText phone,pwd;
    Button get_otp;
    String otpid,password;
    private FirebaseAuth mAuth;
    ProgressBar pbarload;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toast toast6 = Toast.makeText(getApplicationContext(),"Please SignUp to continue", Toast.LENGTH_SHORT);
        toast6.show();

        mAuth = FirebaseAuth.getInstance();
        pbarload = (ProgressBar)findViewById(R.id.pbarload);
        ccp = (CountryCodePicker)findViewById(R.id.ccp);
        phone = (TextInputEditText)findViewById(R.id.phone);
        get_otp= (Button)findViewById(R.id.get_otp);
        ccp.registerCarrierNumberEditText(phone);

        get_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_otp.setVisibility(View.INVISIBLE);
                pbarload.setVisibility(View.VISIBLE);
                String pnumber = ccp.getFullNumberWithPlus().replace(" ","");
                verifyOtp(pnumber);

            }
        });



    }

    private void verifyOtp(String pnumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(pnumber)       // Phone number to verify
                        .setTimeout(30L, TimeUnit.SECONDS) // Timeout and unit
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
                                Intent intent = new Intent(SignUp.this,ProcessOtp.class);
                                intent.putExtra("pnumber",pnumber);
                                intent.putExtra("password",password);
                                intent.putExtra("otpid",otpid);
                                startActivity(intent);
                                finish();
//                                super.onCodeSent(s, forceResendingToken);
                            }



                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(SignUp.this, e.getMessage(),Toast.LENGTH_SHORT).show();


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

                            Intent intent = new Intent(SignUp.this,Dashboard.class);
                            startActivity(intent);
                            finish();


//                            FirebaseUser user = task.getResult().getUser();

                        } else {
                            // Sign in failed, display a message and update the UI
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast toast = Toast.makeText(SignUp.this, "Sign in error",Toast.LENGTH_SHORT);
                            toast.show();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast toast2 = Toast.makeText(SignUp.this, "Invalid OTP entered",Toast.LENGTH_SHORT);
                                toast2.show();


                            }
                        }
                    }
                });
    }
}