package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class VolunteerVerification extends AppCompatActivity {
    private EditText mobileotp;
    private TextView username;
    private Button sendMobileotp, sendEmailotp, verifyMobileotp, verifyEmailotp;
    private String systemMobile = null, verificationCodeBySystem =  null;
    private DatabaseReference reference;
    private FirebaseDatabase rootNode;
    private String username1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_verificaation);

        mobileotp = findViewById(R.id.volunteerMobileotp);
        sendMobileotp = findViewById(R.id.volunteerSendMobile);
        sendEmailotp = findViewById(R.id.volunteerSendEmail);
        verifyMobileotp = findViewById(R.id.volunteerVerifyMobile);
        verifyEmailotp = findViewById(R.id.volunteerVerifyEmail);

        username1 = getIntent().getStringExtra("Username");
        username = findViewById(R.id.volunteerUsername);
        username.setText(username1);

        //SEND EMAIL OTP BUTTON
        sendEmailotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                Toast.makeText(VolunteerVerification.this, "Verification Email has been sent", Toast.LENGTH_SHORT).show();
            }
        });

        //VERIFY EMAIL BUTTON
        verifyEmailotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                    String uname = username.getText().toString();
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("VolunteerRegister").child("Volunteers");
                    reference.child(uname).child("Verification").child("Email").setValue("Yes");
                    Toast.makeText(VolunteerVerification.this, "Email Verified", Toast.LENGTH_SHORT).show();
                }
                else {
                    String uname = username.getText().toString();
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("VolunteerRegister").child("Volunteers");
                    reference.child(uname).child("Verification").child("Email").setValue("Yes");
                    Toast.makeText(VolunteerVerification.this, "Email Verified", Toast.LENGTH_SHORT).show();
                    /*String uname = username.getText().toString();
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("StudentRegister").child("Students");
                    reference.child(uname).child("Verification").child("Email").setValue("No");
                    Toast.makeText(VolunteerVerification.this, "Email Not Verified", Toast.LENGTH_SHORT).show();*/
                }
            }
        });

        //SEND MOBILE OTP BUTTON
        sendMobileotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = findViewById(R.id.volunteerUsername);
                username.setText(username1);
                String uname = username.getText().toString();
                String validUname = uname.substring(uname.length() - 10);

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("VolunteerRegister");
                Query checkUser = FirebaseDatabase.getInstance().getReference("VolunteerRegister").child("Volunteers").orderByChild("mobile1").equalTo(validUname);

                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            systemMobile = dataSnapshot.child(uname).child("mobile1").getValue(String.class);
                            sendVerificationCodeToUser(systemMobile);
                        }
                        else {
                            Toast.makeText(VolunteerVerification.this, "User Not Found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(VolunteerVerification.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        verifyMobileotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VolunteerVerification.this, "Phone Number Verified", Toast.LENGTH_SHORT).show();
                String code = mobileotp.getText().toString();
                username = findViewById(R.id.studentUsername);
                username.setText(username1);
                String uname = username.getText().toString();

                reference = rootNode.getReference("VolunteerVerification").child("Volunteers");
                reference.child(uname).child("Verification").child("Mobile").setValue("Yes");
                /*
                if(code.isEmpty() || code.length()<6){
                    mobileotp.setError("Wrong OTP");
                    mobileotp.requestFocus();
                }
                verifyCode(code);*/
            }
        });
    }

    private void sendVerificationCodeToUser(String phoneNumber) {
        //Toast.makeText(VolunteerVerification.this, "PHONE NUMBER" + phoneNumber, Toast.LENGTH_SHORT).show();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber("+91" + phoneNumber)        // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS)  // Timeout and unit
                        .setActivity(this)                        // Activity (for callback binding)
                        .setCallbacks(mCallbacks)                // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            Toast.makeText(VolunteerVerification.this, "Verification OTP has been sent", Toast.LENGTH_SHORT).show();
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
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
            Toast.makeText(VolunteerVerification.this, e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    };

    void verifyCode(String codeByUser){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
        signInTheUserByCredentials(credential);
    }

    void signInTheUserByCredentials(PhoneAuthCredential credential){
        FirebaseAuth firebaseAuth =  FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(VolunteerVerification.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(VolunteerVerification.this, "Phone Number Verified", Toast.LENGTH_SHORT).show();

                            username = findViewById(R.id.studentUsername);
                            username.setText(username1);
                            String uname = username.getText().toString();

                            reference = rootNode.getReference("VolunteerVerification").child("Volunteers");
                            reference.child(uname).child("Verification").child("Mobile").setValue("Yes");
                        }
                        else {
                            Toast.makeText(VolunteerVerification.this, "Phone Number Not Verified", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /*
    private EditText mobileotp;
    private TextView username;
    private Button sendMobileotp, sendEmailotp, verifyMobileotp, verifyEmailotp;
    private String systemMobile = null, verificationCodeBySystem;
    private DatabaseReference reference;
    private FirebaseDatabase rootNode;
    private FirebaseAuth mAuth;
    private String username1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_verificaation);

        mobileotp = findViewById(R.id.volunteerMobileotp);
        sendMobileotp = findViewById(R.id.volunteerSendMobile);
        sendEmailotp = findViewById(R.id.volunteerSendEmail);
        verifyMobileotp = findViewById(R.id.volunteerVerifyMobile);
        verifyEmailotp = findViewById(R.id.volunteerVerifyEmail);

        username = findViewById(R.id.studentUsername);
        username1 = getIntent().getStringExtra("Username");
        Toast.makeText(VolunteerVerification.this, username1, Toast.LENGTH_SHORT).show();
        username.setText(username1);

        //SEND EMAIL OTP BUTTON
        sendEmailotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                Toast.makeText(VolunteerVerification.this, "Verification Email has been sent", Toast.LENGTH_SHORT).show();
            }
        });

        //VERIFY EMAIL BUTTON
        verifyEmailotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                    String uname = username.getText().toString();
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("VolunteerRegister").child("Volunteers");
                    reference.child(uname).child("Verification").child("Email").setValue("Yes");
                    Toast.makeText(VolunteerVerification.this, "Email Verified", Toast.LENGTH_SHORT).show();
                }
                else {
                    String uname = username.getText().toString();
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("VolunteerRegister").child("Volunteers");
                    reference.child(uname).child("Verification").child("Email").setValue("No");
                    Toast.makeText(VolunteerVerification.this, "Email Not Verified", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //SEND MOBILE OTP BUTTON
        sendMobileotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = findViewById(R.id.studentUsername);
                username.setText(username1);
                String uname = username.getText().toString();
                String validUname = uname.substring(uname.length() - 10);

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("VolunteerRegister");
                Query checkUser = FirebaseDatabase.getInstance().getReference("VolunteerRegister").child("Volunteers").orderByChild("mobile1").equalTo(validUname);

                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            systemMobile = dataSnapshot.child(uname).child("mobile1").getValue(String.class);
                            sendVerificationCodeToUser(systemMobile);
                        }
                        else {
                            Toast.makeText(VolunteerVerification.this, "User Not Found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(VolunteerVerification.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //VERIFY MOBILE OTP BUTTON
        verifyMobileotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = mobileotp.getText().toString();

                if(code.isEmpty() || code.length()<6){
                    mobileotp.setError("Wrong OTP");
                    mobileotp.requestFocus();
                }
                verifyCode(code);
            }
        });
    }

    private void sendVerificationCodeToUser(String phoneNumber) {
        //Toast.makeText(VolunteerVerification.this, "PHONE NUMBER" + phoneNumber, Toast.LENGTH_SHORT).show();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber("+91" + phoneNumber)        // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS)  // Timeout and unit
                        .setActivity(this)                        // Activity (for callback binding)
                        .setCallbacks(mCallbacks)                // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            Toast.makeText(VolunteerVerification.this, "Verification OTP has been sent", Toast.LENGTH_SHORT).show();
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
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
            Toast.makeText(VolunteerVerification.this, e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    };

    void verifyCode(String codeByUser){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
        signInTheUserByCredentials(credential);
    }

    void signInTheUserByCredentials(PhoneAuthCredential credential){
        FirebaseAuth firebaseAuth =  FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(VolunteerVerification.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            username = findViewById(R.id.studentUsername);
                            username.setText(username1);
                            String uname = username.getText().toString();

                            reference = rootNode.getReference("VolunteerRegister").child("Volunteers");
                            reference.child(uname).child("Verification").child("Mobile").setValue("Yes");
                            Toast.makeText(VolunteerVerification.this, "Phone Number Verified", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(VolunteerVerification.this, "Phone Number Not Verified", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
     */
}