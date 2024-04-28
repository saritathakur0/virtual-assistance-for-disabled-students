package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VolunteerRegister extends AppCompatActivity {

    private Button volunRegister;
    private TextView loginPage;
    private EditText fname, lname, dob, email, mobile, password, cpassword, mob, username;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    String fname1, lname1, dob1, email1, mobile1, password1, cpassword1;
    String name_validate = "[a-zA-Z ]*$";

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_register);

        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.vregisterUsername);
        username.setEnabled(false);
        mob = findViewById(R.id.vregisterMobile);

        mob.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {}

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            @Override
            public void afterTextChanged(Editable arg0) {
                EditText one = findViewById(R.id.vregisterFirstName);
                EditText two = findViewById(R.id.vregisterMobile);
                String first = one.getText().toString();
                String second = two.getText().toString();

                username.setText(first+"_"+second);
                username.setEnabled(false);
            }
        });

        // volunteer registration
        volunRegister = findViewById(R.id.vregisterButton);
        volunRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fname = findViewById(R.id.vregisterFirstName);
                lname = findViewById(R.id.vregisterLastName);
                dob = findViewById(R.id.vregisterDOB);
                email = findViewById(R.id.vregisterEmail);
                mobile = findViewById(R.id.vregisterMobile);
                password = findViewById(R.id.vregisterPassword);
                cpassword = findViewById(R.id.vregisterCPassword);

                fname1 = fname.getText().toString();
                lname1 = lname.getText().toString();
                dob1 = dob.getText().toString();
                email1 = email.getText().toString();
                mobile1 = mobile.getText().toString();
                password1 = password.getText().toString();
                cpassword1 = cpassword.getText().toString();

                if (validate_fname() && validate_lname() && validate_dob() && validate_mobile() && validate_email() &&  validate_password() && validate_cpassword() ){

                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("VolunteerRegister").child("Volunteers");

                    //CHECKING IF USER WITH SAME MOBILE/EMAIL EXISTS
                    Query userExistsMobile = rootNode.getReference("VolunteerRegister").child("Volunteers").orderByChild("mobile1").equalTo(mobile1);
                    userExistsMobile.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!snapshot.exists()){
                                registerUser();
                            }
                            else{
                                Toast.makeText(VolunteerRegister.this,"Mobile number already registered",Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
                else{
                    Toast.makeText(VolunteerRegister.this,"Failed to register",Toast.LENGTH_SHORT).show();
                }

            }
        });

        //back to login
        loginPage = (TextView) findViewById(R.id.vregisterAlready);
        loginPage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v1) {
                Intent intent1= new Intent(VolunteerRegister.this,Login.class);
                startActivity(intent1);
            }
        });
    }

    public void registerUser(){
        email1 = email.getText().toString();
        password1 = password.getText().toString();
        mAuth.createUserWithEmailAndPassword(email1, password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            databaseEntry();
                        } else {
                            Toast.makeText(VolunteerRegister.this, task.getException() + "Email address already registered", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void databaseEntry(){
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("VolunteerRegister").child("Volunteers");
        VolunteerRegisterHelper helper = new VolunteerRegisterHelper(fname1, lname1, dob1, email1, mobile1, password1);
        reference.child(fname1 + "_" + mobile1).setValue(helper);

        reference.child(fname1 + "_" + mobile1).child("Verification").child("Mobile").setValue("No");
        reference.child(fname1 + "_" + mobile1).child("Verification").child("Email").setValue("No");
        reference.child(fname1 + "_" + mobile1).child("PDetails").setValue("No");
        reference.child(fname1 + "_" + mobile1).child("EduDetails").setValue("No");
        reference.child(fname1 + "_" + mobile1).child("Eligibilty").setValue("No");
        reference.child(fname1 + "_" + mobile1).child("Requests").setValue("null");

        Toast.makeText(VolunteerRegister.this,"REGISTRATION SUCCESSFUL!",Toast.LENGTH_SHORT).show();

        fname.setText("");
        lname.setText("");
        dob.setText("");
        email.setText("");
        mobile.setText("");
        password.setText("");
        cpassword.setText("");

        Intent intent1 = new Intent(VolunteerRegister.this, Login.class);
        startActivity(intent1);
    }

    //Validation Methods
    private boolean validate_fname() {
        String val = fname1;

        if (val.isEmpty()) {
            fname.setError("Field can not be empty");
            return false;
        } else if (!(val.matches(name_validate))) {
            fname.setError("First Name can have only alphabets");
            return false;
        } else {
            fname.setError(null);
            return true;
        }
    }

    private boolean validate_lname() {
        String val = lname1;

        if (val.isEmpty()) {
            lname.setError("Field can not be empty");
            return false;
        } else if (!(val.matches(name_validate))) {
            lname.setError("Last Name can have only alphabets");
            return false;
        } else {
            lname.setError(null);
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

    private boolean validate_dob() {
        String val = dob1;
        if (val.isEmpty()) {
            dob.setError("Field can not be empty");
            return false;
        } else if (isValidDate(val) == false) {
            dob.setError("Invalid Date!");
            return false;
        } else {
            dob.setError(null);
            return true;
        }
    }

    private boolean validate_email() {
        String val = email1;
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            email.setError("Invalid Email!");
            return false;
        } else {
            email.setError(null);
            return true;
        }

    }
    private boolean validate_mobile(){
        String val = mobile1;

        if (val.isEmpty()) {
            mobile.setError("Field can not be empty");
            return false;
        } else if (val.length() != 10) {
            mobile.setError("Invalid Mobile Number!");
            return false;
        } else {
            mobile.setError(null);
            return true;
        }
    }

    private boolean validate_password() {
        String val = password1;
        String checkPassword = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            password.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkPassword)) {
            password.setError("Password should contain: (at least 1 digit, at least 1 lower case letter, at least 1 upper case letter, at least 1 special character, no white spaces, and at least 4 characters!)");
            return false;
        } else {
            password.setError(null);
            return true;
        }

    }
    private boolean validate_cpassword() {
        String pass1 = password1;
        String pass2 = cpassword1;

        if(pass2.isEmpty()) {
            cpassword.setError("Field can not be empty");
            return false;
        } else if(!(pass2.equals(pass1))) {
            cpassword.setError("The Passwords do not match! Re-enter");
            return false;
        } else{
            cpassword.setError(null);
            return true;
        }
    }

}