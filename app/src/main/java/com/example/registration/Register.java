package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Register extends AppCompatActivity {

    private Button student, volunteer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // to volunteer registration
        volunteer = findViewById(R.id.registerVolunteer);
        volunteer.setOnClickListener(new View.OnClickListener() {

            @Override
        public void onClick(View v) {
            Intent intent = new Intent(Register.this, VolunteerRegister.class);
            startActivity(intent);
        }
    });

    //to student registration
    student = findViewById(R.id.registerStudent);
        student.setOnClickListener(new View.OnClickListener() {
       @Override
        public void onClick(View v1) {
            Intent intent1= new Intent(Register.this,StudentRegister.class);
            startActivity(intent1);
        }
    });
    }
}