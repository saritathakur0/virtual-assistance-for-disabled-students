package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private EditText emailTextView, passwordTextView;
    private Button move;
    private TextView joinUs, forgot;
    private FirebaseDatabase rootNode;
    private FirebaseAuth mAuth;
    RadioButton genderradioButton;
    RadioGroup radioGroup;
    String selected = "null", Username = "username", Password = "password", username1 = null, password1 = null;
    SharedPreferences SharedPreferences;
    int count=0;
    ImageView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText shared1 = findViewById(R.id.loginEmail);
        String shared2 = shared1.getText().toString();

        SharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        if(SharedPreferences.contains(shared2)){
            Intent intent1= new Intent(Login.this,StudentDashboard.class);
            startActivity(intent1);
        }

        mAuth = FirebaseAuth.getInstance();

        emailTextView = findViewById(R.id.loginEmail);
        passwordTextView = findViewById(R.id.loginPassword);

        //VALIDATING LOGIN
        move = findViewById(R.id.loginButton);
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username1 = emailTextView.getText().toString();
                password1 = passwordTextView.getText().toString();

                if(validate_radioButton() && validate_username() && validate_password()){
                    if(selected.equals("Student"))
                        isStudent();
                    else
                        isVolunteer();
                }
            }
        });

        //back to main register page
        joinUs = (TextView) findViewById(R.id.loginJoinus);
        joinUs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v1) {
                Intent intent1= new Intent(Login.this,Register.class);
                startActivity(intent1);
            }
        });

        //redirect to forgot password
        forgot = (TextView) findViewById(R.id.loginForgot);
        forgot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v1) {
                Intent intent1= new Intent(Login.this,ForgotPassword.class);
                startActivity(intent1);
            }
        });


        show = (ImageView) findViewById(R.id.loginshowPass);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordTextView = findViewById(R.id.loginPassword);
               // Toast.makeText(Login.this, "Inside show", Toast.LENGTH_SHORT).show();
                count = count +1;
                if(count%2 == 0)
                    passwordTextView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                else
                    passwordTextView.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

    }


    public void signInUser(String email_, String pwd_){
        mAuth.signInWithEmailAndPassword(email_, pwd_).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(Login.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(Login.this, "FAILURE", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void isStudent(){
        EditText usernamevar = findViewById(R.id.loginEmail);
        EditText passwordvar = findViewById(R.id.loginPassword);
        String uname = usernamevar.getText().toString();
        String pass = passwordvar.getText().toString();
        String validUname = uname.substring(uname.length() -10);

        rootNode = FirebaseDatabase.getInstance();
        Query checkUser = FirebaseDatabase.getInstance().getReference("StudentRegister").child("Students").orderByChild("mobile").equalTo(validUname);

            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                    String systemPass = dataSnapshot.child(uname).child("password").getValue(String.class);
                    String systemFname = dataSnapshot.child(uname).child("fname").getValue(String.class);
                    String email1 = dataSnapshot.child(uname).child("email").getValue(String.class);

                    if (systemPass.equals(pass)) {
                        signInUser(email1, pass);

                        android.content.SharedPreferences.Editor editor = SharedPreferences.edit();
                        editor.putString(Username, uname);
                        editor.putString(Password, pass);
                        editor.commit();

                        Intent intent=new Intent(Login.this, StudentDashboard.class);
                        intent.putExtra("systemFname", systemFname);
                        intent.putExtra("Username", uname);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(Login.this, "User Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Login.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void isVolunteer(){
        EditText usernamevar = findViewById(R.id.loginEmail);
        EditText passwordvar = findViewById(R.id.loginPassword);
        String uname = usernamevar.getText().toString();
        String pass = passwordvar.getText().toString();
        String validUname = uname.substring(uname.length() -10);

        rootNode = FirebaseDatabase.getInstance();
        Query checkUser = FirebaseDatabase.getInstance().getReference("VolunteerRegister").child("Volunteers").orderByChild("mobile1").equalTo(validUname);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String systemPass = dataSnapshot.child(uname).child("password1").getValue(String.class);
                    String systemFname = dataSnapshot.child(uname).child("fname1").getValue(String.class);
                    String email1 = dataSnapshot.child(uname).child("email1").getValue(String.class);

                    if (systemPass.equals(pass)) {
                        signInUser(email1, pass);
                        Intent intent=new Intent(Login.this, VolunteerDashboard.class);
                        intent.putExtra("systemFname", systemFname);
                        intent.putExtra("Username", uname);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(Login.this, "User Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Login.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    boolean validate_radioButton(){
        radioGroup=(RadioGroup)findViewById(R.id.forgotRadioGroup);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        genderradioButton = (RadioButton)findViewById(selectedId);
        if(selectedId==-1){
            Toast.makeText(Login.this,"Please select one of the login type", Toast.LENGTH_SHORT).show();
        }
        else{
            selected = genderradioButton.getText().toString();
            return true;
        }
        return false;
    }

    private boolean validate_username() {
        String val = username1;

        if (val.isEmpty()) {
            emailTextView.setError("Field can not be empty");
            return false;
        }
        else {
            emailTextView.setError(null);
            return true;
        }
    }

    private boolean validate_password() {
        String val = password1;

        if (val.isEmpty()) {
            passwordTextView.setError("Field can not be empty");
            return false;
        }
        else {
            passwordTextView.setError(null);
            return true;
        }
    }
}