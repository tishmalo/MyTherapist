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

public class AdminVerifyTherapist extends AppCompatActivity {

    private ImageView showPhotoio, showIdentity, showCertificate;
    private Button reject,verify;

  Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_verify_therapist);

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

        showIdentity=findViewById(R.id.showIdentity);
        showPhotoio=findViewById(R.id.showPhotoio);
        showCertificate=findViewById(R.id.showCertificate);

        reject=findViewById(R.id.reject);


        Intent intent=getIntent();
        final String EMAIL=intent.getStringExtra("email");
        final String PHOTO=intent.getStringExtra("profileimage");

        Glide.with(getApplicationContext()).load(PHOTO).into(showPhotoio);

        DatabaseReference ref;
        ref= FirebaseDatabase.getInstance().getReference("Therapist_Identity");
        Query a= ref.orderByChild("email").equalTo(EMAIL);
        a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    final String IDENTITY= ds.child("identity").getValue().toString();
                    Glide.with(getApplicationContext()).load(IDENTITY).into(showIdentity);


                    DatabaseReference useRRef;
                    useRRef=FirebaseDatabase.getInstance().getReference("Therapist_Certificate");
                    Query b=useRRef.orderByChild("email").equalTo(EMAIL);
                    b.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for(DataSnapshot sn: snapshot.getChildren()) {

                                final String CERTIFICATE = sn.child("certificate").getValue().toString();

                                Glide.with(getApplicationContext()).load(CERTIFICATE).into(showCertificate);
                            }

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




        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref;
                ref= FirebaseDatabase.getInstance().getReference("Probation");


                Query query= ref.orderByChild("email").equalTo(EMAIL);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot ds: snapshot.getChildren()){

                            ds.getRef().removeValue();

                        }



                        Intent intent = new Intent(getApplicationContext(), Admin.class);
                        startActivity(intent);



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }
}