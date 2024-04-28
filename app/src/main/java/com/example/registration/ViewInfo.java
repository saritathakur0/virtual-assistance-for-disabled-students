package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ViewInfo extends AppCompatActivity {
    TextView nameinfo, addressinfo, examinfo, emailinfo, mobileinfo;
    Button accept, decline;
    String studentUsername, volunteerUsername;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_info);

        studentUsername = getIntent().getStringExtra("studentUsername");
        volunteerUsername = getIntent().getStringExtra("volunteerUsername");

        nameinfo = findViewById(R.id.nameinfo);
        addressinfo = findViewById(R.id.addressinfo);
        examinfo = findViewById(R.id.examinfo);
        emailinfo = findViewById(R.id.emailinfo);
        mobileinfo = findViewById(R.id.mobileinfo);

        getInfo();

        accept = findViewById(R.id.acceptbtn);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInfo();

                Intent intent=new Intent(ViewInfo.this, VolunteerDashboard.class);
                intent.putExtra("systemFname", volunteerUsername.substring(0, volunteerUsername.length()-11) + "!");
                startActivity(intent);
            }
        });

        decline = findViewById(R.id.declinebtn);
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ViewInfo.this, VolunteerDashboard.class);
                intent.putExtra("systemFname", volunteerUsername.substring(0, volunteerUsername.length()-11) + "!");
                startActivity(intent);
            }
        });
    }

    public void setInfo(){
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("VolunteerRegister").child("Volunteers").child(volunteerUsername).child("Accepted");
        reference.setValue(studentUsername);

        reference = rootNode.getReference("StudentRegister").child("Students").child(studentUsername).child("Accepted");
        reference.setValue(volunteerUsername);
    }

    public void getInfo(){
        String validUname = studentUsername.substring(studentUsername.length() -10);
        Query checkUser = FirebaseDatabase.getInstance().getReference("StudentRegister").child("Students").orderByChild("mobile").equalTo(validUname);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String fname = dataSnapshot.child(studentUsername).child("fname").getValue(String.class);
                    String lname = dataSnapshot.child(studentUsername).child("lname").getValue(String.class);
                    String mobile = dataSnapshot.child(studentUsername).child("mobile").getValue(String.class);
                    String email = dataSnapshot.child(studentUsername).child("email").getValue(String.class);
                    nameinfo.setText(fname + " " + lname);
                    mobileinfo.setText(mobile);
                    emailinfo.setText(email);

                    String ExamDate = dataSnapshot.child(studentUsername).child("ExamDetails").child("ExamDate").getValue(String.class);
                    String ExamLevel = dataSnapshot.child(studentUsername).child("ExamDetails").child("ExamLevel").getValue(String.class);
                    String ExamMode = dataSnapshot.child(studentUsername).child("ExamDetails").child("ExamMode").getValue(String.class);
                    String ExamName = dataSnapshot.child(studentUsername).child("ExamDetails").child("ExamName").getValue(String.class);
                    String ExamTime = dataSnapshot.child(studentUsername).child("ExamDetails").child("ExamTime").getValue(String.class);
                    examinfo.setText("Exam Name: " + ExamName + "\n" + "Exam Level: " + ExamLevel + "\n" + "Exam Mode: " +
                            ExamMode + "\n" + "Exam Date: " + ExamDate + "\n" + "Exam Time: " + ExamTime + "\n");

                    String address1 = dataSnapshot.child(studentUsername).child("PersonalDetails").child("Address").getValue(String.class);
                    String city1 = dataSnapshot.child(studentUsername).child("PersonalDetails").child("City").getValue(String.class);
                    String state1 = dataSnapshot.child(studentUsername).child("PersonalDetails").child("State").getValue(String.class);
                    String zip1 = dataSnapshot.child(studentUsername).child("PersonalDetails").child("ZipCode").getValue(String.class);
                    addressinfo.setText(address1 + ", " + city1 + ", " + state1 + ", " + zip1);

                } else {
                    Toast.makeText(ViewInfo.this, "Error Occured", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewInfo.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}