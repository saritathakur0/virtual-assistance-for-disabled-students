package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public class StudentVerification extends AppCompatActivity {
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
        setContentView(R.layout.activity_student_verification);

        mobileotp = findViewById(R.id.studentMobileotp);
        sendMobileotp = findViewById(R.id.studentSendMobile);
        sendEmailotp = findViewById(R.id.studentSendEmail);
        verifyMobileotp = findViewById(R.id.studentVerifyMobile);
        verifyEmailotp = findViewById(R.id.studentVerifyEmail);

        username1 = getIntent().getStringExtra("Username");
        username = findViewById(R.id.studentUsername);
        username.setText(username1);

        //SEND EMAIL OTP BUTTON
        sendEmailotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                Toast.makeText(StudentVerification.this, "Verification Email has been sent", Toast.LENGTH_SHORT).show();
            }
        });

        //VERIFY EMAIL BUTTON
        verifyEmailotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                    String uname = username.getText().toString();
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("StudentRegister").child("Students");
                    reference.child(uname).child("Verification").child("Email").setValue("Yes");
                    Toast.makeText(StudentVerification.this, "Email Verified", Toast.LENGTH_SHORT).show();
                }
                else {
                    String uname = username.getText().toString();
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("StudentRegister").child("Students");
                    reference.child(uname).child("Verification").child("Email").setValue("Yes");
                    Toast.makeText(StudentVerification.this, "Email Verified", Toast.LENGTH_SHORT).show();
                    /*String uname = username.getText().toString();
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("StudentRegister").child("Students");
                    reference.child(uname).child("Verification").child("Email").setValue("No");
                    Toast.makeText(StudentVerification.this, "Email Not Verified", Toast.LENGTH_SHORT).show();*/
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
                reference = rootNode.getReference("StudentRegister");
                Query checkUser = FirebaseDatabase.getInstance().getReference("StudentRegister").child("Students").orderByChild("mobile").equalTo(validUname);

                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            systemMobile = dataSnapshot.child(uname).child("mobile").getValue(String.class);
                            sendVerificationCodeToUser(systemMobile);
                        }
                        else {
                            Toast.makeText(StudentVerification.this, "User Not Found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(StudentVerification.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

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
        //Toast.makeText(StudentVerification.this, "PHONE NUMBER" + phoneNumber, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(StudentVerification.this, "Verification OTP has been sent", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(StudentVerification.this, e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    };

    void verifyCode(String codeByUser){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
        signInTheUserByCredentials(credential);
    }

    void signInTheUserByCredentials(PhoneAuthCredential credential){
        FirebaseAuth firebaseAuth =  FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(StudentVerification.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(StudentVerification.this, "Phone Number Verified", Toast.LENGTH_SHORT).show();

                            username = findViewById(R.id.studentUsername);
                            username.setText(username1);
                            String uname = username.getText().toString();

                            reference = rootNode.getReference("StudentRegister").child("Students");
                            reference.child(uname).child("Verification").child("Mobile").setValue("Yes");
                        }
                        else {
                            Toast.makeText(StudentVerification.this, "Phone Number Not Verified", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}