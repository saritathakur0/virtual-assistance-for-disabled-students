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

public class ViewInfoVol extends AppCompatActivity {
    TextView nameinfo, addressinfo, examinfo, emailinfo, mobileinfo;
    Button back;
    String studentUsername, volunteerUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_info_vol);

        studentUsername = getIntent().getStringExtra("studentUsername");
        volunteerUsername = getIntent().getStringExtra("volunteerUsername");

        nameinfo = findViewById(R.id.vnameinfo);
        addressinfo = findViewById(R.id.vaddressinfo);
        examinfo = findViewById(R.id.vexaminfo);
        emailinfo = findViewById(R.id.vemailinfo);
        mobileinfo = findViewById(R.id.vmobileinfo);

        getInfo();

        back = findViewById(R.id.vbackbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ViewInfoVol.this, StudentDashboard.class);
                intent.putExtra("Username", studentUsername);
                intent.putExtra("systemFname", studentUsername.substring(0, studentUsername.length()-11) + "!");
                startActivity(intent);
            }
        });
    }

    public void getInfo(){
        String validUname = volunteerUsername.substring(volunteerUsername.length() - 10);
        Query checkUser = FirebaseDatabase.getInstance().getReference("VolunteerRegister").child("Volunteers").orderByChild("mobile1").equalTo(validUname);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String fname = dataSnapshot.child(volunteerUsername).child("fname1").getValue(String.class);
                    String lname = dataSnapshot.child(volunteerUsername).child("lname1").getValue(String.class);
                    String mobile = dataSnapshot.child(volunteerUsername).child("mobile1").getValue(String.class);
                    String email = dataSnapshot.child(volunteerUsername).child("email1").getValue(String.class);
                    nameinfo.setText(fname + " " + lname);
                    mobileinfo.setText(mobile);
                    emailinfo.setText(email);

                    String aggregate10th = dataSnapshot.child(volunteerUsername).child("Education").child("aggregate10th").getValue(String.class);
                    String yoc10th = dataSnapshot.child(volunteerUsername).child("Education").child("yoc10th").getValue(String.class);
                    //String aggregate12th = dataSnapshot.child(volunteerUsername).child("Education").child("aggregate12th").getValue(String.class);
                    //String yoc12th = dataSnapshot.child(volunteerUsername).child("Education").child("yoc12th").getValue(String.class);
                    //String ExamTime = dataSnapshot.child(volunteerUsername).child("Education").child("ExamTime").getValue(String.class);
                    examinfo.setText("10th Aggregate %: " + aggregate10th + "\n" + "10th Year of Completion: " + yoc10th);

                    String address1 = dataSnapshot.child(volunteerUsername).child("PersonalDetails").child("Address").getValue(String.class);
                    String city1 = dataSnapshot.child(volunteerUsername).child("PersonalDetails").child("City").getValue(String.class);
                    String state1 = dataSnapshot.child(volunteerUsername).child("PersonalDetails").child("State").getValue(String.class);
                    String zip1 = dataSnapshot.child(volunteerUsername).child("PersonalDetails").child("ZipCode").getValue(String.class);
                    addressinfo.setText(address1 + ", " + city1 + ", " + state1 + ", " + zip1);

                } else {
                    Toast.makeText(ViewInfoVol.this, "Error Occured", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewInfoVol.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}