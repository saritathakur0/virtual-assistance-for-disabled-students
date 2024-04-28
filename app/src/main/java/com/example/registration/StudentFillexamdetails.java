package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentFillexamdetails extends AppCompatActivity {
    private EditText examName, examLevel, examDate, examTime, examMode;
    private Button upload;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    String examName1, examLevel1, examDate1, examTime1, examMode1, username = "";
    String name_validate = "[a-zA-Z ]*$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_fillexamdetails);

        username = getIntent().getStringExtra("Username");

        examName = findViewById(R.id.enterexamname);
        examLevel = findViewById(R.id.enterexamlevel);
        examDate = findViewById(R.id.enterexamdate);
        examTime = findViewById(R.id.enterexamtime);
        examMode = findViewById(R.id.enterexammode);

        upload = findViewById(R.id.submitexamdetails);
        upload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v1) {
                examName1 = examName.getText().toString();
                examLevel1 = examLevel.getText().toString();
                examDate1 = examDate.getText().toString();
                examTime1 = examTime.getText().toString();
                examMode1 = examMode.getText().toString();

                if (validate_examName() && validate_examLevel() && validate_examDate() && validate_examTime() && validate_examMode()){
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("StudentRegister").child("Students").child(username).child("ExamDetails");
                    reference.child("ExamName").setValue(examName1);
                    reference.child("ExamLevel").setValue(examLevel1);
                    reference.child("ExamDate").setValue(examDate1);
                    reference.child("ExamTime").setValue(examTime1);
                    reference.child("ExamMode").setValue(examMode1);

                    reference = rootNode.getReference("StudentRegister").child("Students").child(username);
                    reference.child("EDetails").setValue("Yes");

                    Toast.makeText(StudentFillexamdetails.this,"Exam Details uploaded",Toast.LENGTH_SHORT).show();

                    examName.setText("");
                    examLevel.setText("");
                    examDate.setText("");
                    examTime.setText("");
                    examMode.setText("");
                }
            }
        });
    }

    private boolean validate_examName() {
        String val = examName1;

        if (val.isEmpty()) {
            examName.setError("Field can not be empty");
            return false;
        } else if (!(val.matches(name_validate))) {
            examName.setError("Exam Name can have only alphabets");
            return false;
        } else {
            examName.setError(null);
            return true;
        }
    }

    private boolean validate_examLevel() {
        String val = examLevel1;

        if (val.isEmpty()) {
            examLevel.setError("Field can not be empty");
            return false;
        } else {
            examLevel.setError(null);
            return true;
        }
    }

    private boolean validate_examTime() {
        String val = examTime1;

        if (val.isEmpty()) {
            examTime.setError("Field can not be empty");
            return false;
        } else {
            examTime.setError(null);
            return true;
        }
    }

    private boolean validate_examMode() {
        String val = examMode1;

        if (val.isEmpty()) {
            examMode.setError("Field can not be empty");
            return false;
        } else if (!(val.matches(name_validate))) {
            examName.setError("Exam Mode can have only alphabets");
            return false;
        } else {
            examMode.setError(null);
            return true;
        }
    }

    public static boolean isValidDate(String d) {
        String regex = "^(1[0-2]|0[1-9])/(3[01]"
                + "|[12][0-9]|0[1-9])/[0-9]{4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher((CharSequence)d);
        return matcher.matches();
    }

    private boolean validate_examDate() {
        String val = examDate1;
        if (val.isEmpty()) {
            examDate.setError("Field can not be empty");
            return false;
        } else if (isValidDate(val) == false) {
            examDate.setError("Invalid Date!");
            return false;
        } else {
            examDate.setError(null);
            return true;
        }
    }
}