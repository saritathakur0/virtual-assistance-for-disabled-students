package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VolunteerEducationalDetails extends AppCompatActivity {
    private EditText aggregate10th, yoc10th, aggregate12th, yoc12th, instituteName, degreeName, specialization, yocCurrent;
    private Button upload;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    String aggregate10th1, yoc10th1, aggregate12th1, yoc12th1, instituteName1, degreeName1, specialization1, yocCurrent1, spinner10th1, spinner12th1, username;
    String name_validate = "[a-zA-Z ]*$";

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_educational_details);
        username = getIntent().getStringExtra("Username");

        aggregate10th = findViewById(R.id.aggregate10th);
        yoc10th = findViewById(R.id.yoc10th);
        aggregate12th = findViewById(R.id.aggregate12th);
        yoc12th = findViewById(R.id.yoc12th);
        instituteName = findViewById(R.id.instituteName);
        degreeName = findViewById(R.id.degreeName);
        specialization = findViewById(R.id.specialization);
        yocCurrent = findViewById(R.id.yocCurrent);

        upload = findViewById(R.id.voluploadEducationButton);
        upload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v1) {
                aggregate10th1 = aggregate10th.getText().toString();
                yoc10th1 = yoc10th.getText().toString();
                aggregate12th1 = aggregate12th.getText().toString();
                yoc12th1 = yoc12th.getText().toString();
                instituteName1 = instituteName.getText().toString();
                degreeName1 = degreeName.getText().toString();
                specialization1 = specialization.getText().toString();
                yocCurrent1 = yocCurrent.getText().toString();

                if (validate_agg10(aggregate10th1, aggregate10th) && validate_yoc10(yoc10th1, yoc10th) &&
                        validate_agg(aggregate12th1, aggregate12th) && validate_yoc(yoc12th1, yoc12th) &&
                        validate_name(instituteName1, instituteName) && validate_name(degreeName1, degreeName) &&
                        validate_name(specialization1, specialization) && validate_yoc(yocCurrent1, yocCurrent)){

                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("VolunteerRegister").child("Volunteers").child(username).child("Education");
                    reference.child("aggregate10th").setValue(aggregate10th1);
                    reference.child("yoc10th").setValue(yoc10th1);
                    if(aggregate12th1.length()>1) reference.child("aggregate12th").setValue(aggregate12th1);
                    if(yoc12th1.length()>1) reference.child("yoc12th").setValue(yoc12th1);
                    if(instituteName1.length()>1) reference.child("instituteName").setValue(instituteName1);
                    if(degreeName1.length()>1) reference.child("degreeName").setValue(degreeName1);
                    if(specialization1.length()>1) reference.child("specialization").setValue(specialization1);
                    if(yocCurrent1.length()>1) reference.child("yocCurrent").setValue(yocCurrent1);

                    reference = rootNode.getReference("VolunteerRegister").child("Volunteers").child(username);
                    reference.child("EduDetails").setValue("Yes");

                    Toast.makeText(VolunteerEducationalDetails.this,"Educational Details uploaded",Toast.LENGTH_SHORT).show();

                    aggregate10th.setText("");
                    yoc10th.setText("");
                    aggregate12th.setText("");
                    yoc12th.setText("");
                    instituteName.setText("");
                    degreeName.setText("");
                    specialization.setText("");
                    yocCurrent.setText("");
                }
            }
        });
    }

    private boolean validate_name(String fieldName, EditText field) {
        String val = fieldName;

        if (! val.isEmpty()) {
            if (!(val.matches(name_validate))) {
                field.setError("Can have only alphabets!");
                return false;
            }
        } else {
            field.setError(null);
            return true;
        }
        return true;
    }

    private boolean validate_yoc10(String yoc1, EditText field){
        String val = yoc1;

        if (val.isEmpty()) {
            field.setError("Field can not be empty");
            return false;
        } else if (val.length() != 4) {
            field.setError("Invalid Year of Completion!");
            return false;
        } else {
            field.setError(null);
            return true;
        }
    }

    private boolean validate_agg10(String yoc1, EditText field){
        String val = yoc1;

        if (val.isEmpty()) {
            field.setError("Field can not be empty");
            return false;
        } else if (val.length() > 4) {
            field.setError("Invalid Aggregate!");
            return false;
        } else {
            field.setError(null);
            return true;
        }
    }

    private boolean validate_yoc(String yoc1, EditText field){
        String val = yoc1;

        if (! val.isEmpty()) {
            if (val.length() != 4) {
                field.setError("Invalid Year of Completion!");
                return false;
            }
        } else {
            field.setError(null);
            return true;
        }
        return true;
    }

    private boolean validate_agg(String yoc1, EditText field){
        String val = yoc1;

        if (! val.isEmpty()) {
            if (val.length() != 4) {
                field.setError("Invalid Aggregate");
                return false;
            }
        } else {
            field.setError(null);
            return true;
        }
        return true;
    }

    private boolean validate_aggregate(String agg, EditText field){
        String val = agg;
        int agg1 = Integer.valueOf(agg);

        if (val.isEmpty()) {
            field.setError("Field can not be empty");
            return false;
        } else {
            field.setError(null);
            return true;
        }
    }
}