package com.example.mytherapist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.example.mytherapist.Adapter.PatientChatAdapter;
import com.example.mytherapist.Adapter.TherapistChatAdapter;
import com.example.mytherapist.Model.PatientPusher;
import com.example.mytherapist.Model.TherapistList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PatientChatList extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;

    private List<PatientPusher> userList;
    private TherapistChatAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_chat_list);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar=findViewById(R.id.selectedtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        recyclerView= findViewById(R.id.selectedrecycler);

        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        userList= new ArrayList<>();

        DatabaseReference ref;


/***
 * Hii ndio inaleta list ya patients, so iko kwa patient page
 */
        ref= FirebaseDatabase.getInstance().getReference("chosen");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){

                    TherapistList tr= ds.getValue(TherapistList.class);

                    final String EMAIL= tr.getemail();

                    Query query= ref.orderByChild("useremail").equalTo(FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getEmail());
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            userList.clear();
                            for(DataSnapshot ds: snapshot.getChildren()){
                                PatientPusher ps=ds.getValue(PatientPusher.class);
                                userList.add(ps);

                            }

                            userAdapter= new TherapistChatAdapter(PatientChatList.this, userList);
                            recyclerView.setAdapter(userAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}