package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentEducationalDetails extends AppCompatActivity {
    private EditText instituteName, instituteCity, courseName, yoc;
    private Button upload;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    String instituteName1, instituteCity1, courseName1, yoc1, username;
    String name_validate = "[a-zA-Z ]*$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_educational_details);

        username = getIntent().getStringExtra("Username");

        instituteName = findViewById(R.id.studInstitute);
        instituteCity = findViewById(R.id.studCityState);
        courseName = findViewById(R.id.studCourse);
        yoc = findViewById(R.id.studYear);

        upload = findViewById(R.id.studuploadEducationButton);
        upload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v1) {
                instituteName1 = instituteName.getText().toString();
                instituteCity1 = instituteCity.getText().toString();
                courseName1 = courseName.getText().toString();
                yoc1 = yoc.getText().toString();

                if (validate_instituteName() && validate_instituteCity() && validate_courseName() && validate_yoc()){
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("StudentRegister").child("Students").child(username).child("Education");
                    reference.child("InstituteName").setValue(instituteName1);
                    reference.child("InstituteCity").setValue(instituteCity1);
                    reference.child("CourseName").setValue(courseName1);
                    reference.child("YearOfCompletion").setValue(yoc1);

                    reference = rootNode.getReference("StudentRegister").child("Students").child(username);
                    reference.child("EduDetails").setValue("Yes");

                    Toast.makeText(StudentEducationalDetails.this,"Educational Details uploaded",Toast.LENGTH_SHORT).show();

                    instituteName.setText("");
                    instituteCity.setText("");
                    courseName.setText("");
                    yoc.setText("");
                }
            }
        });
    }

    private boolean validate_instituteName() {
        String val = instituteName1;

        if (val.isEmpty()) {
            instituteName.setError("Field can not be empty");
            return false;
        } else if (!(val.matches(name_validate))) {
            instituteName.setError("Institute Name can have only alphabets");
            return false;
        } else {
            instituteName.setError(null);
            return true;
        }
    }

    private boolean validate_instituteCity() {
        String val = instituteCity1;

        if (val.isEmpty()) {
            instituteCity.setError("Field can not be empty");
            return false;
        } else if (!(val.matches(name_validate))) {
            instituteCity.setError("Institute City can have only alphabets");
            return false;
        } else {
            instituteCity.setError(null);
            return true;
        }
    }

    private boolean validate_courseName() {
        String val = courseName1;

        if (val.isEmpty()) {
            courseName.setError("Field can not be empty");
            return false;
        } else if (!(val.matches(name_validate))) {
            courseName.setError("Course Name can have only alphabets");
            return false;
        } else {
            courseName.setError(null);
            return true;
        }
    }

    private boolean validate_yoc(){
        String val = yoc1;

        if (val.isEmpty()) {
            yoc.setError("Field can not be empty");
            return false;
        } else if (val.length() != 4) {
            yoc.setError("Invalid Year of Completion!");
            return false;
        } else {
            yoc.setError(null);
            return true;
        }
    }
}