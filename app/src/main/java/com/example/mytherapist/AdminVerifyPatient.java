package com.example.mytherapist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdminVerifyPatient extends AppCompatActivity {

    private ImageView showPhotoio, showIdentity;
    private Button reject, verify;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_verify_patient);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        showPhotoio = findViewById(R.id.showPhotoio);
        showIdentity = findViewById(R.id.showIdentity);
        reject = findViewById(R.id.reject);



        Intent intent = getIntent();
        final String EMAIL = intent.getStringExtra("email");

        DatabaseReference ref, userRef;
        ref = FirebaseDatabase.getInstance().getReference("PatientProfile");
        Query query = ref.orderByChild("email").equalTo(EMAIL);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {

                    final String PROFILE = ds.child("profileimage").getValue().toString();

                    Glide.with(getApplicationContext()).load(PROFILE).into(showPhotoio);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userRef = FirebaseDatabase.getInstance().getReference("PatientIdentity");
        Query query1 = userRef.orderByChild("email").equalTo(EMAIL);

        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot sn : snapshot.getChildren()) {
                    final String Identity = sn.child("Identity").getValue().toString();

                    Glide.with(getApplicationContext()).load(Identity).into(showIdentity);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref;
                ref = FirebaseDatabase.getInstance().getReference("Probation");


                Query query = ref.orderByChild("email").equalTo(EMAIL);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                       snapshot.getRef().removeValue();


                        Intent intent1= new Intent(getApplicationContext(), Admin.class);
                        startActivity(intent1);



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });


    }
}