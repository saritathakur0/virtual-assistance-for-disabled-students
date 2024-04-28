package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentSearchVolunteer extends AppCompatActivity {
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private Map<String, String> volunteerList = new HashMap<String, String>();
    private Map<String, String> accpetedList = new HashMap<String, String>();
    static ListView listView, listView1;
    static ArrayList<String> items, items1;
    ArrayAdapter<String> adapter, adapter1;
    String username, volunteerToRequest;
    int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_request_volunteer);
        username = getIntent().getStringExtra("Username");
        rootNode = FirebaseDatabase.getInstance();

        Toast.makeText(StudentSearchVolunteer.this, "Tap to send request", Toast.LENGTH_SHORT).show();

        listView = findViewById(R.id.list);
        items = new ArrayList<>();
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_text_color, items);
        listView.setAdapter(adapter);

        listView1 = findViewById(R.id.list2);
        items1 = new ArrayList<>();
        adapter1 = new ArrayAdapter<>(getApplicationContext(), R.layout.list_text_color, items1);
        listView1.setAdapter(adapter1);

        getVolunteers();
        getAcceptedVolunteers();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String item = listView.getAdapter().getItem(position).toString();
                String firstWords = item.substring(0, item.lastIndexOf(" "));
                String lastWord = item.substring(item.lastIndexOf(" ")+1);

                Query checkUser = FirebaseDatabase.getInstance().getReference("VolunteerRegister").child("Volunteers").orderByChild("email1").equalTo(lastWord);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                                volunteerToRequest = childSnapshot.getKey();
                                String prevRequest = childSnapshot.child("Requests").getValue(String.class);

                                reference = rootNode.getReference("VolunteerRegister").child("Volunteers").child(volunteerToRequest).child("Requests");
                                reference.setValue(prevRequest + "," + username);
                                Toast.makeText(StudentSearchVolunteer.this,"Request sent to " + volunteerToRequest,Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(StudentSearchVolunteer.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });

                Query studCheckUser = FirebaseDatabase.getInstance().getReference("StudentRegister").child("Students").child(username).child("RequestedVolunteers");
                studCheckUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String prev = snapshot.getValue(String.class);
                            reference = rootNode.getReference("StudentRegister").child("Students").child(username).child("RequestedVolunteers");
                            reference.setValue(prev + "," + volunteerToRequest);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
            }
        });

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String item1 = listView1.getAdapter().getItem(position).toString();

                Query checkUser = FirebaseDatabase.getInstance().getReference("VolunteerRegister").child("Volunteers").child(item1);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                                Intent intent=new Intent(StudentSearchVolunteer.this, ViewInfoVol.class);
                                intent.putExtra("volunteerUsername", item1);
                                intent.putExtra("studentUsername", username);
                                startActivity(intent);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(StudentSearchVolunteer.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void getVolunteers() {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("VolunteerRegister");
        Query checkUser = FirebaseDatabase.getInstance().getReference("VolunteerRegister").child("Volunteers");

        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String fname = ds.child("fname1").getValue(String.class);
                    String lname = ds.child("lname1").getValue(String.class);
                    String email = ds.child("email1").getValue(String.class);
                    String mobile = ds.child("mobile1").getValue(String.class);
                    String city = ds.child("PersonalDetails").child("City").getValue(String.class);
                    String state = ds.child("PersonalDetails").child("State").getValue(String.class);
                    String gender = ds.child("PersonalDetails").child("Gender").getValue(String.class);
                    volunteerList.put(mobile, fname + " " + lname + " (" + gender + ")" + "\n" + city + ", " + state
                                                + "\n" + email);
                }
                items.clear();
                for (Map.Entry<String,String> entry : volunteerList.entrySet()) {
                    items.add(entry.getValue());
                    listView.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentSearchVolunteer.this,"No Volunteers available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAcceptedVolunteers() {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("StudentRegister");
        Query checkUser = FirebaseDatabase.getInstance().getReference("StudentRegister").child("Students").child(username);

        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String request = dataSnapshot.child("Accepted").getValue(String.class);
                    request.substring(5);
                    String[] parts = request.split("\\,");

                    for(int i=1; i<parts.length;i++){
                        accpetedList.put(parts[i], parts[i]);
                    }
                }
                for (Map.Entry<String, String> entry : accpetedList.entrySet()) {
                    items1.add(entry.getValue());
                    listView1.setAdapter(adapter1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentSearchVolunteer.this, "None Accepted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}