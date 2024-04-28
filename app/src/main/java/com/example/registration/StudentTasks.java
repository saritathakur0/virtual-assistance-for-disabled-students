package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentTasks extends AppCompatActivity {
    private EditText taskLabel, taskDate;
    private Button addTask;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private String taskLabel1, taskDate1, name, username;
    private Map<String, String> taskList = new HashMap<String, String>();
    static ListView listView;
    static ArrayList<String> items;
    ArrayAdapter<String> adapter;
    int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_tasks);
        name = getIntent().getStringExtra("StudentName");
        username = getIntent().getStringExtra("Username");
        addTask = findViewById(R.id.studentAddTask);

        listView = findViewById(R.id.studentTaskList);
        items = new ArrayList<>();
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_text_color, items);
        listView.setAdapter(adapter);

        getTasks();

        addTask.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v1) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("StudentRegister").child("Students").child(username).child("Tasks");

                taskLabel = findViewById(R.id.studentTaskLabel);
                taskDate = findViewById(R.id.studentTaskDate);
                taskLabel1 = taskLabel.getText().toString();
                taskDate1 = taskDate.getText().toString();

                TaskHelper helper = new TaskHelper(taskLabel1, taskDate1);
                reference.child(taskLabel1).setValue(helper);

                Toast.makeText(StudentTasks.this,"Task Updated",Toast.LENGTH_SHORT).show();
                taskLabel.setText("");
                taskDate.setText("");
            }
        });
    }

    public void getTasks() {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("StudentRegister");
        Query checkUser = FirebaseDatabase.getInstance().getReference("StudentRegister").child("Students").child(username).child("Tasks");

        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String taskLabel = ds.child("taskLabel").getValue(String.class);
                    String taskDate = ds.child("taskDate").getValue(String.class);
                    taskList.put(taskLabel, "Your Task - " + taskLabel + "\nYour Task Date - " + taskDate);
                }
                items.clear();
                for (Map.Entry<String,String> entry : taskList.entrySet()) {
                    items.add(entry.getValue());
                    listView.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentTasks.this,"No Task Found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}