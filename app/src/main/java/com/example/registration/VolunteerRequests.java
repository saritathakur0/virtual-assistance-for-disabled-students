package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class VolunteerRequests extends AppCompatActivity {
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private Map<String, String> requestList = new HashMap<String, String>();
    private Map<String, String> tempList = new HashMap<String, String>();
    static ListView listView;
    static ArrayList<String> items;
    ArrayAdapter<String> adapter;
    String username, fname, lname, disability, email;
    int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_requests);

        username = getIntent().getStringExtra("Username");

        listView = findViewById(R.id.VolunteerListView);
        items = new ArrayList<>();
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_text_color, items);
        listView.setAdapter(adapter);

        getRequests();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String item = listView.getAdapter().getItem(position).toString();
                Toast.makeText(VolunteerRequests.this, item, Toast.LENGTH_SHORT).show();

                rootNode = FirebaseDatabase.getInstance();
                Query checkUser = FirebaseDatabase.getInstance().getReference("StudentRegister").child("Students").child(item);

                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                                Intent intent=new Intent(VolunteerRequests.this, ViewInfo.class);
                                intent.putExtra("studentUsername", item);
                                intent.putExtra("volunteerUsername", username);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(VolunteerRequests.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void getRequests() {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("VolunteerRegister");
        Query checkUser = FirebaseDatabase.getInstance().getReference("VolunteerRegister").child("Volunteers").child(username);

        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String request = dataSnapshot.child("Requests").getValue(String.class);
                    request.substring(5);
                    String[] parts = request.split("\\,");

                    for(int i=1; i<parts.length;i++){
                        //getStudentInfo(parts[i]);
                        requestList.put(parts[i], parts[i]);
                    }

                    for (Map.Entry<String, String> entry : tempList.entrySet()) {
                        System.out.println(entry.getKey()+" : "+entry.getValue());
                    }
                }
                for (Map.Entry<String, String> entry : requestList.entrySet()) {
                    items.add(entry.getValue()/* + " " + tempList.get(entry.getValue())*/);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VolunteerRequests.this, "No Requests", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getStudentInfo(String user){
        Query checkUser = FirebaseDatabase.getInstance().getReference("StudentRegister").child("Students");
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    fname = dataSnapshot.child(user).child("fname").getValue(String.class);
                    lname = dataSnapshot.child(user).child("lname").getValue(String.class);
                    disability = dataSnapshot.child(user).child("disability").getValue(String.class);
                    email = dataSnapshot.child(user).child("email").getValue(String.class);
                    tempList.put(user, fname + " " + lname + "\n" + disability + "\n" + email);
                    } else {
                    Toast.makeText(VolunteerRequests.this, "Error Occured", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(VolunteerRequests.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //Toast.makeText(VolunteerRequests.this, tempList.get(user), Toast.LENGTH_SHORT).show();
    }
}