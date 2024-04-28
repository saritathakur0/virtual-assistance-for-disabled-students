package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

public class StudentDashboard extends AppCompatActivity {
    TextView setname;
    String viewname, username;
    ImageView img1, img2, img3, img4, logout;
    String Username = "username", Password = "password";
    android.content.SharedPreferences SharedPreferences;
    BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        SharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        if(SharedPreferences.contains(Username)){
            Toast.makeText(StudentDashboard.this, "SharedPreferences", Toast.LENGTH_SHORT).show();
        }

        setname = findViewById(R.id.userName);
        viewname = getIntent().getStringExtra("systemFname");
        username = getIntent().getStringExtra("Username");
        setname.setText(viewname);

        img1= findViewById(R.id.requests);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query checkUser = FirebaseDatabase.getInstance().getReference("StudentRegister").child("Students").child(username);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String pDetails = dataSnapshot.child("PDetails").getValue(String.class);
                            String eduDetails = dataSnapshot.child("EduDetails").getValue(String.class);
                            String eDetails = dataSnapshot.child("EDetails").getValue(String.class);
                            String verifyM = dataSnapshot.child("Verification").child("Mobile").getValue(String.class);
                            String verifyE = dataSnapshot.child("Verification").child("Email").getValue(String.class);

                            if (pDetails.equals("No")) {
                                Toast.makeText(StudentDashboard.this, "Please complete Personal Details Section", Toast.LENGTH_SHORT).show();
                            } else if (eduDetails.equals("No")) {
                                Toast.makeText(StudentDashboard.this, "Please complete Educational Details Section", Toast.LENGTH_SHORT).show();
                            } else if (eDetails.equals("No")) {
                                Toast.makeText(StudentDashboard.this, "Please complete Exam Details Section", Toast.LENGTH_SHORT).show();
                            } else if(verifyE.equals("No") || verifyM.equals("No")){
                                Toast.makeText(StudentDashboard.this, "Please complete Verifications", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(StudentDashboard.this, StudentSearchVolunteer.class);
                                intent.putExtra("Username", username);
                                startActivity(intent);
                            }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
            }
        });

        img2= findViewById(R.id.verification);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentDashboard.this,StudentVerification.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });
        img3= findViewById(R.id.vEligibility);
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentDashboard.this, StudentFillexamdetails.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });
        img4= findViewById(R.id.tasks);
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentDashboard.this, StudentTasks.class);
                intent.putExtra("StudentName", viewname);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });

        logout = findViewById(R.id.studentLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentDashboard.this, Login.class);
                startActivity(intent);
            }
        });

        nav = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuPersonalDetails:
                        Intent intent2 = new Intent(StudentDashboard.this, PersonalDetails.class);
                        intent2.putExtra("Username", username);
                        intent2.putExtra("TableName", "StudentRegister");
                        startActivity(intent2);
                        return true;
                    case R.id.menuEducationalDetails:
                        Intent intent3 = new Intent(StudentDashboard.this, StudentEducationalDetails.class);
                        intent3.putExtra("Username", username);
                        startActivity(intent3);
                        return true;
                    case R.id.menuAccount:
                        Intent intent4 = new Intent(StudentDashboard.this, Profile.class);
                        intent4.putExtra("Username", username);
                        intent4.putExtra("TableName", "StudentRegister");
                        startActivity(intent4);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
}