package com.example.mytherapist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mytherapist.Adapter.TherapistScheduledAdapter;
import com.example.mytherapist.Model.NotificationModel;
import com.example.mytherapist.Model.TherapistList;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TherapistPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    RecyclerView recyclerView;
    private Toolbar toolBar;
    private NavigationView nav_view;

    List<NotificationModel> userList;

    TherapistScheduledAdapter adapter;
    CircleImageView imageView;
    private com.google.android.material.floatingactionbutton.FloatingActionButton chat;
    private TextView chatText, nav_username,nav_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapist_page);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        SharedPreferences sharedPreferen =getSharedPreferences("Sharedprefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor
                = sharedPreferen.edit();
        final boolean isDarkModeOn
                = sharedPreferen
                .getBoolean(
                        "isDarkModeOn", false);


        if (isDarkModeOn) {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_YES);

        }
        else {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_NO);

        }



        DatabaseReference reference;
        reference=FirebaseDatabase.getInstance().getReference("TherapistContact");
        Query t=reference.orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        t.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Intent intent30= new Intent(TherapistPage.this, TherapistNumber.class);
                    startActivity(intent30);
                }else{


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        userList=new ArrayList<>();
        recyclerView=findViewById(R.id.myschedule);


        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);






        DatabaseReference referenc;
        referenc=FirebaseDatabase.getInstance().getReference("Notification");
        Query que=referenc.orderByChild("therapistemail").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        que.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userList.clear();

                for (DataSnapshot sn: snapshot.getChildren()){
                    NotificationModel nm= sn.getValue(NotificationModel.class);
                    userList.add(nm);


                }
                adapter= new TherapistScheduledAdapter(getApplicationContext(),userList);
                recyclerView.setAdapter(adapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        toolBar = (Toolbar) findViewById(R.id.toolBar2);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("THERAPIST");
        nav_view = findViewById(R.id.navigationBar1);
        drawerLayout = findViewById(R.id.drawerLayout1);

        chat=findViewById(R.id.chat);
        chatText=findViewById(R.id.emailchat);

        try {
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL(""))
                    .setWelcomePageEnabled(false)
                    .build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

chat.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        final String CURRENT= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        chatText.setText(CURRENT);


        JitsiMeetConferenceOptions options
                =new JitsiMeetConferenceOptions.Builder()
                .setRoom(CURRENT)
                .build();
        JitsiMeetActivity.launch(TherapistPage.this,options);

    }
});

        nav_username=nav_view.getHeaderView(0).findViewById(R.id.username);
        nav_email=nav_view.getHeaderView(0).findViewById(R.id.email);
        imageView=nav_view.getHeaderView(0).findViewById(R.id.profilepic);


        DatabaseReference data;
        data=FirebaseDatabase.getInstance().getReference().child("Therapist_Profile");
        Query query=data.orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    for(DataSnapshot ds: snapshot.getChildren()) {

                        TherapistList user= ds.getValue(TherapistList.class);
                        final String USERNAME=user.getusername();


                        DatabaseReference dat;
                        dat=FirebaseDatabase.getInstance().getReference().child("Therapist_Profile").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                        dat.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(snapshot.exists()){

                                    final String PHOTO= snapshot.child("profileimage").getValue().toString();
                                    final String USERNAME=snapshot.child("username").getValue().toString();
                                    final String EMAIL=snapshot.child("email").getValue().toString();

                                    nav_username.setText(USERNAME);
                                    nav_email.setText(EMAIL);
                                    Glide.with(getApplicationContext()).load(PHOTO).into(imageView);

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }







                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error){
            }
        });



        nav_view.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(TherapistPage.this, drawerLayout, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){

            case R.id.schedule:


                Intent intent= new Intent(TherapistPage.this, Therapist_Schedule.class);
                startActivity(intent);
                break;

            case R.id.message:


                Intent intent1= new Intent(TherapistPage.this, ChatList.class);
                startActivity(intent1);
                break;

            case R.id.switchmode:

                switchmode();

                break;

            case R.id.notification:
                checkNotification();
                break;

            case R.id.info:
                Intent intent2= new Intent(TherapistPage.this, TherapistNumber.class);
                startActivity(intent2);
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent3 = new Intent(TherapistPage.this, Login.class);
                startActivity(intent3);

                Toast.makeText(TherapistPage.this,"SIGNED OUT",Toast.LENGTH_SHORT).show();
                finish();

                break;

        }

        return false;
    }

    private void checkNotification() {

        DatabaseReference reference;

        reference=FirebaseDatabase.getInstance().getReference("Probation");
        Query a= reference.orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    popup();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void popup() {

        AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(TherapistPage.this);
        //set title

        alertDialogBuilder.setTitle("update profile");

        //set Dialog message

        alertDialogBuilder
                .setMessage("click yes to update")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        probationpatient();



                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        //create alert dialog

        AlertDialog alertDialog=alertDialogBuilder.create();

        //show dialog

        alertDialog.show();


    }

    private void probationpatient() {

        Intent intent50= new Intent(TherapistPage.this, ProbationTherapist.class);
        startActivity(intent50);

    }

    private void switchmode() {

        SharedPreferences sharedPreferen =getSharedPreferences("Sharedprefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor
                = sharedPreferen.edit();
        final boolean isDarkModeOn
                = sharedPreferen
                .getBoolean(
                        "isDarkModeOn", false);


        if(isDarkModeOn){
            AppCompatDelegate
                    .setDefaultNightMode(AppCompatDelegate
                            .MODE_NIGHT_NO);


            editor.putBoolean(
                    "isDarkModeOn", false);
            editor.apply();
        }else{

            AppCompatDelegate
                    .setDefaultNightMode(AppCompatDelegate
                            .MODE_NIGHT_YES);

            editor.putBoolean(
                    "isDarkModeOn", true);
            editor.apply();
        }


    }
}