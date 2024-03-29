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
import com.example.mytherapist.Adapter.VerifyPatient;
import com.example.mytherapist.Model.TherapistList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PatientAdmin extends AppCompatActivity {


    private Toolbar toolbar;
    private RecyclerView recyclerView;

    private List<TherapistList> userList;
    private VerifyPatient userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_admin);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar=findViewById(R.id.selectedtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        recyclerView=findViewById(R.id.selectedrecycler);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        userList= new ArrayList<>();




        DatabaseReference ref;
        ref= FirebaseDatabase.getInstance().getReference().child("PatientProfile");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userList.clear();


                for(DataSnapshot ds: snapshot.getChildren()) {
                    TherapistList ls = ds.getValue(TherapistList.class);
                    userList.add(ls);
                }

                userAdapter= new VerifyPatient(PatientAdmin.this, userList);
                recyclerView.setAdapter(userAdapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}