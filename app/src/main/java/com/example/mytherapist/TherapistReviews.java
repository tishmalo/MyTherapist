package com.example.mytherapist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.mytherapist.Adapter.TherapistReviewedAdapter;
import com.example.mytherapist.Model.TherapistReviewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TherapistReviews extends AppCompatActivity {



    private Toolbar toolbar;
    com.google.android.material.floatingactionbutton.FloatingActionButton reject, accept;
    private TextView username;

    private RecyclerView recyclerView;
    private AppBarLayout appBarLayout;
    DatabaseReference ref;
    private CircleImageView circleImageView;

    //Enter other modules from the project
    //List
    ArrayList<TherapistReviewModel> userList;
    TherapistReviewedAdapter userAdapter;

    //Adapter


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapist_reviews);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //link java to xml
        toolbar= findViewById(R.id.selectedtoolbar);
        recyclerView=findViewById(R.id.selectedrecycler);
        appBarLayout=findViewById(R.id.selectedlayout);
        circleImageView=findViewById(R.id.profileimage);
        reject=findViewById(R.id.reject);
        accept=findViewById(R.id.accept);
        username=findViewById(R.id.username);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent=getIntent();
       String email1=intent.getStringExtra("email");
       String profileimage1=intent.getStringExtra("profileimage");
       
       username.setText(email1);

       if(profileimage1!=null) {

           Glide.with(TherapistReviews.this).load(profileimage1).into(circleImageView);
       }




        //setup the tool bar
        toolbar=findViewById(R.id.selectedtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        userList=new ArrayList<>();
        userAdapter=new TherapistReviewedAdapter(TherapistReviews.this,userList);
        recyclerView.setAdapter(userAdapter);

       final String EMAIL2= email1.trim();
       final String PROFILE=profileimage1.trim();


       DatabaseReference reference;
       reference=FirebaseDatabase.getInstance().getReference("PatientProfile");
       Query v= reference.orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());

       v.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

               for(DataSnapshot dt: snapshot.getChildren()){
                   final String PATIENTPHOTO= dt.child("profileimage").getValue().toString();

                   ref= FirebaseDatabase.getInstance().getReference("TherapistReviews");
                   Query query= ref.orderByChild("therapistemail").equalTo(EMAIL2);
                   query.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {




                           userList.clear();
                           for(DataSnapshot ds: snapshot.getChildren()){
                               TherapistReviewModel tr= ds.getValue(TherapistReviewModel.class);
                               userList.add(tr);

                           }
                           userAdapter.notifyDataSetChanged();

                       }



                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });


                   accept.setOnClickListener(new View.OnClickListener() {

                       final String Current= FirebaseAuth.getInstance().getCurrentUser().getUid();
                       final String UEMAIL= FirebaseAuth.getInstance().getCurrentUser().getEmail();

                       @Override
                       public void onClick(View view) {
                           DatabaseReference userRef= FirebaseDatabase.getInstance().getReference("chosen").child(Current);

                           HashMap user= new HashMap();
                           user.put("therapistemail",EMAIL2);
                           user.put("userid", Current);
                           user.put("useremail",UEMAIL);
                           user.put("profileimage", PROFILE);
                           user.put("userimage",PATIENTPHOTO);

                           userRef.updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
                               @Override
                               public void onComplete(@NonNull Task task) {

                                   if(task.isSuccessful()) {


                                       Toast.makeText(TherapistReviews.this, "Therapist chosen successfully", Toast.LENGTH_SHORT).show();

                                       Intent intent10= new Intent(TherapistReviews.this,PatientPage.class );
                                       startActivity(intent10);

                                   }else{
                                       String error=task.getException().toString();
                                       Toast.makeText(TherapistReviews.this,"Error"+error,Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });
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