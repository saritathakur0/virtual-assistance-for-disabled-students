package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class VolunteerDashboard extends AppCompatActivity {
    TextView setname;
    String username, viewname;
    ImageView img1, img2, img3, img4, logout;
    BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_dashboard);

        setname = findViewById(R.id.volunteeruserName);
        viewname = getIntent().getStringExtra("systemFname");
        username = getIntent().getStringExtra("Username");
        setname.setText(viewname + "!");

        img1= findViewById(R.id.vEligibility);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VolunteerDashboard.this, VolunteerEligibilityTest.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });
        img2= findViewById(R.id.vVerification);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VolunteerDashboard.this, VolunteerVerification.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });
        img3= findViewById(R.id.vRequests);
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query checkUser = FirebaseDatabase.getInstance().getReference("VolunteerRegister").child("Volunteers").child(username);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String pDetails = dataSnapshot.child("PDetails").getValue(String.class);
                        String eduDetails = dataSnapshot.child("EduDetails").getValue(String.class);
                        String eligibility = dataSnapshot.child("Eligibilty").getValue(String.class);
                        String verifyM = dataSnapshot.child("Verification").child("Mobile").getValue(String.class);
                        String verifyE = dataSnapshot.child("Verification").child("Email").getValue(String.class);

                        if (pDetails.equals("No")) {
                            Toast.makeText(VolunteerDashboard.this, "Please complete Personal Details Section", Toast.LENGTH_SHORT).show();
                        } else if (eduDetails.equals("No")) {
                            Toast.makeText(VolunteerDashboard.this, "Please complete Educational Details Section", Toast.LENGTH_SHORT).show();
                        } else if (eligibility.equals("No")) {
                            Toast.makeText(VolunteerDashboard.this, "Please complete Eligibility Test", Toast.LENGTH_SHORT).show();
                        } else if(verifyE.equals("No") || verifyM.equals("No")){
                            Toast.makeText(VolunteerDashboard.this, "Please complete Verifications", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(VolunteerDashboard.this, VolunteerRequests.class);
                            intent.putExtra("Username", username);
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
            }
        });

        img4= findViewById(R.id.vTasks);
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VolunteerDashboard.this,VolunteerTasks.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });

        logout = findViewById(R.id.volunteerLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VolunteerDashboard.this, Login.class);
                startActivity(intent);
            }
        });

        nav = (BottomNavigationView) findViewById(R.id.volunteerbottomNavigationView);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuPersonalDetails:
                        Intent intent2 = new Intent(VolunteerDashboard.this, PersonalDetails.class);
                        intent2.putExtra("Username", username);
                        intent2.putExtra("TableName", "VolunteerRegister");
                        startActivity(intent2);
                        return true;
                    case R.id.menuEducationalDetails:
                        Intent intent3 = new Intent(VolunteerDashboard.this, VolunteerEducationalDetails.class);
                        intent3.putExtra("Username", username);
                        startActivity(intent3);
                        return true;
                    case R.id.menuAccount:
                        Intent intent4 = new Intent(VolunteerDashboard.this, Profile.class);
                        intent4.putExtra("Username", username);
                        intent4.putExtra("TableName", "VolunteerRegister");
                        startActivity(intent4);
                        return true;
                    default:
                        return false;
                }
            }
        });


    }
}