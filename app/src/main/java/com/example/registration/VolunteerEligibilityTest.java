package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VolunteerEligibilityTest extends AppCompatActivity {
    VideoView vid;
    MediaController m;
    RadioGroup rg1, rg2, rg3, rg4;
    RadioButton rb1, rb2, rb3, rb4;
    Button b1;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    int val  = 0;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_eligibility_test);
        username = getIntent().getStringExtra("Username");

        vid = (VideoView)findViewById(R.id.checkVideo);
        vid.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.happydiwali));

        b1 = (Button) findViewById(R.id.checkEligibiltyButton);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rg1 = (RadioGroup) findViewById(R.id.rgroup1);
                rg2 = (RadioGroup) findViewById(R.id.rgroup2);
                rg3 = (RadioGroup) findViewById(R.id.rgroup3);
                rg4 = (RadioGroup) findViewById(R.id.rgroup4);

                int selected1=rg1.getCheckedRadioButtonId();
                int selected2=rg2.getCheckedRadioButtonId();
                int selected3=rg3.getCheckedRadioButtonId();
                int selected4=rg4.getCheckedRadioButtonId();

                rb1 = (RadioButton)findViewById(selected1);
                rb2 = (RadioButton)findViewById(selected2);
                rb3 = (RadioButton)findViewById(selected3);
                rb4 = (RadioButton)findViewById(selected4);

                String select1 = rb1.getText().toString();
                String select2 = rb2.getText().toString();
                String select3 = rb3.getText().toString();
                String select4 = rb4.getText().toString();

                if(validate1(select1) && validate2(select2) && validate3(select3) && validate4(select4)){
                    Toast.makeText(VolunteerEligibilityTest.this,"You are Eligible!", Toast.LENGTH_SHORT).show();
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("VolunteerRegister").child("Volunteers").child(username);
                    reference.child("Eligibilty").setValue("Yes");
                }
                else
                    Toast.makeText(VolunteerEligibilityTest.this,"You are not Eligible!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean validate(int selected1, int selected2, int selected3, int selected4){
        Toast.makeText(VolunteerEligibilityTest.this,"Inside", Toast.LENGTH_SHORT).show();
        if(selected1 == -1)
        {
            Toast.makeText(VolunteerEligibilityTest.this,"1", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(selected2 == -1)
        {
            Toast.makeText(VolunteerEligibilityTest.this,"2", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(selected3 == -1)
        {
            Toast.makeText(VolunteerEligibilityTest.this,"3", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(selected4 == -1)
        {
            Toast.makeText(VolunteerEligibilityTest.this,"4", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean validate1(String select1){
        if(select1.equals("Option B"))
        {
            return true;
        }
        return false;
    }

    public boolean validate2(String select1){
        if(select1.equals("When did you get back from London?"))
        {
            return true;
        }
        return false;
    }

    public boolean validate3(String select1){
        if(select1.equals("यह साईकल तुम्हारे लिए बहुत छोटी है।"))
        {
            return true;
        }
        return false;
    }

    public boolean validate4(String select1){
        if(select1.equals("Happy Diwali Happy Diwali"))
        {
            return true;
        }
        return false;
    }

    public void playVideo(View v) {
        m = new MediaController(this);
        String path = "android.resource://com.example.registration/"+R.raw.happydiwali;
        Uri u = Uri.parse(path);
        vid.setVideoURI(u);
        vid.start();
    }

}