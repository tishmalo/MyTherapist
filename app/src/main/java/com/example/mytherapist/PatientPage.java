package com.example.mytherapist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class PatientPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawerLayout;
    private Toolbar toolBar;
    private NavigationView nav_view;
    private Button chat;
    private TextView chatText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_page);


        toolBar = (Toolbar) findViewById(R.id.toolBar2);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("CLIENT");
        nav_view = findViewById(R.id.navigationBar1);
        drawerLayout = findViewById(R.id.drawerLayout1);

        chat= findViewById(R.id.chat);
        chatText= findViewById(R.id.emailchat);

        try {
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL(""))
                    .setWelcomePageEnabled(false)
                    .build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        DatabaseReference ref;
        ref= FirebaseDatabase.getInstance().getReference("Patient");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                final String chattext= "tishmalo99@gmail.com";

                chatText.setText(chattext);


                chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {




                        if (chattext.length()>0){

                            JitsiMeetConferenceOptions options
                                    =new JitsiMeetConferenceOptions.Builder()
                                    .setRoom(chattext)
                                    .build();
                            JitsiMeetActivity.launch(PatientPage.this,options);



                        }

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        nav_view.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(PatientPage.this, drawerLayout, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
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

            case R.id.home:


                Intent intent= new Intent(PatientPage.this, PatientPage.class);
                startActivity(intent);
                break;

            case R.id.pay:

                Intent intent2= new Intent(PatientPage.this, Pay.class);
                startActivity(intent2);
                break;


            case R.id.therapistlist:
                Intent intent3= new Intent(PatientPage.this, therapistlist.class);
                startActivity(intent3);
                break;


        }

        return false;

    }
}