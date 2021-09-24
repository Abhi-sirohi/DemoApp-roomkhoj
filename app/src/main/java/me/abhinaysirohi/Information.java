package me.abhinaysirohi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Information extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String state=null,name,phone,gender=null;
    EditText editText;
    Button submitbtn;
    DatabaseReference dbref;
    FirebaseUser user;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        Spinner spinner = (Spinner) findViewById(R.id.states_spinner);
        editText = (EditText) findViewById(R.id.name);
        submitbtn = (Button)findViewById(R.id.submitbtn);
        spinner.setOnItemSelectedListener(this);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.states, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setSelection(25);
        user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        phone = user.getPhoneNumber();
        dbref = FirebaseDatabase.getInstance().getReference("users").child("regusers");
        progress =(ProgressBar)findViewById(R.id.progress);
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().isEmpty())
                    Toast.makeText(Information.this,"Please Enter Name",Toast.LENGTH_SHORT).show();
                else if (editText.getText().toString().length()<= 3)
                    Toast.makeText(Information.this,"Name is too short ",Toast.LENGTH_SHORT);
                else if (gender==null)
                    Toast.makeText(Information.this,"Please select gender",Toast.LENGTH_SHORT).show();
                else{
                    name = editText.getText().toString();
                    if(gender!=null&&state!=null){
                        submitbtn.setVisibility(View.GONE);
                        progress.setVisibility(View.VISIBLE);
                        submitUserInformation(uid,name,gender,state,phone);

                    }
                }
            }
        });
    }

    private void submitUserInformation(String uid, String name, String gender, String state, String phone) {


        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dbref.child(uid).child("name").setValue(name);
                dbref.child(uid).child("gender").setValue(gender);
                dbref.child(uid).child("state").setValue(state);
                dbref.child(uid).child("phone").setValue(phone).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Intent intent = new Intent(Information.this,Dashboard.class);
                        progress.setVisibility(View.GONE);
                        startActivity(intent);
                        finish();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Information.this,"Somethins went wrong,try again later",Toast.LENGTH_SHORT).show();

            }
        });



    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.male:
                if (checked)
                    gender = "Male";
                    Toast.makeText(this,gender,Toast.LENGTH_LONG).show();
                    break;
            case R.id.female:
                if (checked)
                    gender = "Female";
                    Toast.makeText(this,gender,Toast.LENGTH_LONG).show();
                    break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        state = (String) parent.getItemAtPosition(position);
        Toast.makeText(this,"state selected is "+ state,Toast.LENGTH_LONG).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}