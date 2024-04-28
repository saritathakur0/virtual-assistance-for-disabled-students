package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    TextView name, dob, gender, address, mobile, email;
    Button upload;
    String username, tablename1, tablename2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = getIntent().getStringExtra("Username");
        tablename1 = getIntent().getStringExtra("TableName");

        if(tablename1.equals("StudentRegister")){
            tablename2 = "Students";
        }
        else{
            tablename2 = "Volunteers";
        }

        name = findViewById(R.id.profileName);
        dob = findViewById(R.id.profileAge);
        gender = findViewById(R.id.profileGender);
        address = findViewById(R.id.profileAddress);
        mobile = findViewById(R.id.profileMobile);
        email = findViewById(R.id.profileEmail);

        if(tablename1.equals("StudentRegister")){
            getInfo();
        }
        else{
            getInfoV();
        }
    }

    public void getInfo(){
        String validUname = username.substring(username.length() -10);
        Query checkUser = FirebaseDatabase.getInstance().getReference(tablename1).child(tablename2).orderByChild("mobile").equalTo(validUname);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String fname1 = dataSnapshot.child(username).child("fname").getValue(String.class);
                    String lname1 = dataSnapshot.child(username).child("lname").getValue(String.class);
                    String mobile1 = dataSnapshot.child(username).child("mobile").getValue(String.class);
                    String email1 = dataSnapshot.child(username).child("email").getValue(String.class);
                    String dob1 = dataSnapshot.child(username).child("dob").getValue(String.class);
                    String address1 = dataSnapshot.child(username).child("PersonalDetails").child("Address").getValue(String.class);
                    String city1 = dataSnapshot.child(username).child("PersonalDetails").child("City").getValue(String.class);
                    String state1 = dataSnapshot.child(username).child("PersonalDetails").child("State").getValue(String.class);
                    String zip1 = dataSnapshot.child(username).child("PersonalDetails").child("ZipCode").getValue(String.class);
                    String gender1 = dataSnapshot.child(username).child("PersonalDetails").child("Gender").getValue(String.class);

                    name.setText(fname1 + " " + lname1);
                    dob.setText(dob1);
                    gender.setText(gender1);
                    mobile.setText(mobile1);
                    email.setText(email1);
                    address.setText(address1 + ", " + city1 + ", " + state1 + ", " + zip1);

                } else {
                    Toast.makeText(Profile.this, "User Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Profile.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getInfoV(){
        String validUname = username.substring(username.length() -10);
        Query checkUser = FirebaseDatabase.getInstance().getReference(tablename1).child(tablename2).orderByChild("mobile1").equalTo(validUname);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String fname1 = dataSnapshot.child(username).child("fname1").getValue(String.class);
                    String lname1 = dataSnapshot.child(username).child("lname1").getValue(String.class);
                    String mobile1 = dataSnapshot.child(username).child("mobile1").getValue(String.class);
                    String email1 = dataSnapshot.child(username).child("email1").getValue(String.class);
                    String dob1 = dataSnapshot.child(username).child("dob1").getValue(String.class);
                    String address1 = dataSnapshot.child(username).child("PersonalDetails").child("Address").getValue(String.class);
                    String city1 = dataSnapshot.child(username).child("PersonalDetails").child("City").getValue(String.class);
                    String state1 = dataSnapshot.child(username).child("PersonalDetails").child("State").getValue(String.class);
                    String zip1 = dataSnapshot.child(username).child("PersonalDetails").child("ZipCode").getValue(String.class);
                    String gender1 = dataSnapshot.child(username).child("PersonalDetails").child("Gender").getValue(String.class);

                    name.setText(fname1 + " " + lname1);
                    dob.setText(dob1);
                    gender.setText(gender1);
                    mobile.setText(mobile1);
                    email.setText(email1);
                    address.setText(address1 + ", " + city1 + ", " + state1 + ", " + zip1);

                } else {
                    Toast.makeText(Profile.this, "User Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Profile.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}