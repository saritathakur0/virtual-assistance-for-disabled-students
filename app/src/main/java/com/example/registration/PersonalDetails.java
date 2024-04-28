package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PersonalDetails extends AppCompatActivity {
    private EditText fname, lname, address, zip, state, city;
    private Button upload;
    private String fname1, lname1, address1, zip1, state1, city1, username, tablename1, tablename2;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    String name_validate = "[a-zA-Z ]*$";
    RadioGroup gender_r;
    RadioButton gender_b;
    String selected = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        username = getIntent().getStringExtra("Username");
        tablename1 = getIntent().getStringExtra("TableName");

        if(tablename1.equals("StudentRegister")){
            tablename2 = "Students";
        }
        else{
            tablename2 = "Volunteers";
        }

        fname = findViewById(R.id.parentfname);
        lname = findViewById(R.id.parentlname);
        address = findViewById(R.id.adressline1);
        zip = findViewById(R.id.zipcodeinput);
        city = findViewById(R.id.cityinput);
        state = findViewById(R.id.stateinput);

        upload = findViewById(R.id.uploadPersonalButton);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fname1 = fname.getText().toString();
                lname1 = lname.getText().toString();
                city1 = city.getText().toString();
                address1 = address.getText().toString();
                zip1 = zip.getText().toString();
                state1 = state.getText().toString();

                if (validate_gender() && validate_address() && validate_city() && validate_state() && validate_zip() && validate_fname() && validate_lname()) {
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference(tablename1).child(tablename2).child(username).child("PersonalDetails");
                    reference.child("Gender").setValue(selected);
                    reference.child("Address").setValue(address1);
                    reference.child("City").setValue(city1);
                    reference.child("State").setValue(state1);
                    reference.child("ZipCode").setValue(zip1);
                    reference.child("PFname").setValue(fname1);
                    reference.child("PLName").setValue(lname1);

                    reference = rootNode.getReference(tablename1).child(tablename2).child(username);
                    reference.child("PDetails").setValue("Yes");

                    Toast.makeText(PersonalDetails.this,"Personal Details uploaded",Toast.LENGTH_SHORT).show();

                    fname.setText("");
                    lname.setText("");
                    address.setText("");
                    zip.setText("");
                    state.setText("");
                    city.setText("");
                    gender_r.clearCheck();
                }
            }
        });
    }

    private boolean validate_zip() {
        String val = zip1;

        if (val.isEmpty()) {
            zip.setError("Field can not be empty");
            return false;
        } else if (val.length() != 6) {
            zip.setError("Invalid ZIP Code!");
            return false;
        } else {
            zip.setError(null);
            return true;
        }
    }

    private boolean validate_city() {
        String val = city1;

        if (val.isEmpty()) {
            city.setError("Field can not be empty");
            return false;
        } else if (!(val.matches(name_validate))) {
            city.setError("City can have only alphabets");
            return false;
        } else {
            city.setError(null);
            return true;
        }
    }

    private boolean validate_lname() {
        String val = lname1;

        if (val.isEmpty()) {
            lname.setError("Field can not be empty");
            return false;
        } else if (!(val.matches(name_validate))) {
            lname.setError("Last Name can have only alphabets");
            return false;
        } else {
            lname.setError(null);
            return true;
        }
    }

    private boolean validate_fname() {
        String val = fname1;

        if (val.isEmpty()) {
            fname.setError("Field can not be empty");
            return false;
        } else if (!(val.matches(name_validate))) {
            fname.setError("First Name can have only alphabets");
            return false;
        } else {
            fname.setError(null);
            return true;
        }
    }

    private boolean validate_state() {
        String val = state1;

        if (val.isEmpty()) {
            state.setError("Field can not be empty");
            return false;
        } else if (!(val.matches(name_validate))) {
            state.setError("State can have only alphabets");
            return false;
        } else {
            state.setError(null);
            return true;
        }
    }

    private boolean validate_address() {
        String val = address1;

        if (val.isEmpty()) {
            address.setError("Field can not be empty");
            return false;
        } else {
            address.setError(null);
            return true;
        }
    }

    private boolean validate_gender() {
        gender_r = (RadioGroup) findViewById(R.id.radioGroup1);
        int selectedId = gender_r.getCheckedRadioButtonId();
        gender_b = (RadioButton) findViewById(selectedId);
        if(selectedId==-1){
            Toast.makeText(PersonalDetails.this,"Please select your gender", Toast.LENGTH_SHORT).show();
        }
        else{
            selected = gender_b.getText().toString();
            return true;
        }
        return false;
    }
}