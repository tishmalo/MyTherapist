package com.example.mytherapist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.example.mytherapist.Adapter.PatientListAdapter;
import com.example.mytherapist.Adapter.PatientNotificationAdapter;
import com.example.mytherapist.Model.NotificationModel;
import com.example.mytherapist.Model.TherapistList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PatientNotification extends AppCompatActivity {


    private Toolbar toolbar;
    private RecyclerView recyclerView;

    private List<NotificationModel> userList;
    private PatientNotificationAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_notification);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar= findViewById(R.id.selectedtoolbar);
        recyclerView=findViewById(R.id.selectedrecycler);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notification");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //set LinearLayoutmanager
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        userList= new ArrayList<>();

        DatabaseReference ref;
        ref= FirebaseDatabase.getInstance().getReference("Notification");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds: snapshot.getChildren()){
                    userList.clear();
                    NotificationModel nm= ds.getValue(NotificationModel.class);
                    userList.add(nm);

                }
                userAdapter= new PatientNotificationAdapter(PatientNotification.this, userList);
                recyclerView.setAdapter(userAdapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
}