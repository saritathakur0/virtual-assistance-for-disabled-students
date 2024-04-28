package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class ForgotPassword extends AppCompatActivity {

    private Button sendOtp, verifyOtp;
    private EditText usernamevar, otpvar;
    private RadioGroup radioGroup;
    private RadioButton generatedRadioButton;
    FirebaseDatabase rootNode;
    String selected = null, systemMobile = null,systemFname = null, verificationCodeBySystem, uname = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        otpvar = findViewById(R.id.forgotEnterOTP);
        sendOtp = findViewById(R.id.forgotsendOTP);
        verifyOtp = findViewById(R.id.forgotVerify);

        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate_radioButton()) {
                    if (selected.equals("Student")) {
                        isStudent();
                    } else {
                        isVolunteer();
                    }
                }
            }
        });

        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otpvar.getText().toString();

                if(code.isEmpty() || code.length()<6){
                    otpvar.setError("Incorrect OTP");
                    otpvar.requestFocus();
                }
                verifyCode(code);
            }
        });
    }

    private void sendVerificationCodeToUser(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber("+91" + phoneNumber)        // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                       // Activity (for callback binding)
                        .setCallbacks(mCallbacks)               // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
            Toast.makeText(ForgotPassword.this, "OTP sent to registered mobile", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code != null){
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(ForgotPassword.this, e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    };

    void verifyCode(String codeByUser){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
        signInTheUserByCredentials(credential);
    }

    void signInTheUserByCredentials(PhoneAuthCredential credential){
        FirebaseAuth firebaseAuth =  FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(ForgotPassword.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ForgotPassword.this, "Verification Completed", Toast.LENGTH_SHORT).show();
                            if (validate_radioButton()) {
                                if (selected.equals("Student")) {
                                    Intent intent1= new Intent(ForgotPassword.this,StudentDashboard.class);
                                    intent1.putExtra("systemFname", systemFname);
                                    intent1.putExtra("Username", uname);
                                    startActivity(intent1);
                                } else {
                                    Intent intent1= new Intent(ForgotPassword.this,VolunteerDashboard.class);
                                    intent1.putExtra("systemFname", systemFname);
                                    intent1.putExtra("Username", uname);
                                    startActivity(intent1);
                                }
                            }
                        }
                        else{
                            Toast.makeText(ForgotPassword.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void isStudent(){
        usernamevar = findViewById(R.id.forgotenterUsername);
        uname = usernamevar.getText().toString();
        String validUname = uname.substring(uname.length() - 10);

        rootNode = FirebaseDatabase.getInstance();
        Query checkUser = FirebaseDatabase.getInstance().getReference("StudentRegister").child("Students").orderByChild("mobile").equalTo(validUname);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    systemMobile = dataSnapshot.child(uname).child("mobile").getValue(String.class);
                    systemFname = dataSnapshot.child(uname).child("fname").getValue(String.class);
                    sendVerificationCodeToUser(systemMobile);
                }
                else {
                    Toast.makeText(ForgotPassword.this, "User Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ForgotPassword.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void isVolunteer(){
        usernamevar = findViewById(R.id.forgotenterUsername);
        uname = usernamevar.getText().toString();
        String validUname = uname.substring(uname.length() -10);

        rootNode = FirebaseDatabase.getInstance();
        Query checkUser = FirebaseDatabase.getInstance().getReference("VolunteerRegister").child("Volunteers").orderByChild("mobile1").equalTo(validUname);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    systemMobile = dataSnapshot.child(uname).child("mobile1").getValue(String.class);
                    systemFname = dataSnapshot.child(uname).child("fname1").getValue(String.class);
                    sendVerificationCodeToUser(systemMobile);
                }
                else {
                    Toast.makeText(ForgotPassword.this, "User Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ForgotPassword.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    boolean validate_radioButton(){
        radioGroup=(RadioGroup)findViewById(R.id.forgotRadioGroup);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        generatedRadioButton = (RadioButton)findViewById(selectedId);
        if(selectedId==-1){
            Toast.makeText(ForgotPassword.this,"Please select one of the login type", Toast.LENGTH_SHORT).show();
        }
        else{
            selected = generatedRadioButton.getText().toString();
            return true;
        }
        return false;
    }
}