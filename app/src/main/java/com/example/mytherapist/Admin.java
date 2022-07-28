package com.example.mytherapist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mytherapist.Adapter.PaymentAdapter;
import com.example.mytherapist.Model.PaymentModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Toolbar toolBar;
    private NavigationView nav_view;
    private RecyclerView recyclerView;
    List<PaymentModel> userList;
    PaymentAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolBar = (Toolbar) findViewById(R.id.toolBar2);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("ADMIN");
        nav_view = findViewById(R.id.navigationBar1);
        drawerLayout = findViewById(R.id.drawerLayout1);

        recyclerView=findViewById(R.id.recyclerview);


        userList=new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        DatabaseReference ref;
        ref= FirebaseDatabase.getInstance().getReference("Payment");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){

                }else {

                    userList.clear();
                    PaymentModel pm = snapshot.getValue(PaymentModel.class);

                    userList.add(pm);

                    adapter = new PaymentAdapter(Admin.this, userList);
                    recyclerView.setAdapter(adapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        nav_view.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(Admin.this, drawerLayout, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
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

            case R.id.patient:


            Intent intent= new Intent(Admin.this, PatientAdmin.class);
            startActivity(intent);
            break;


            case R.id.therapist:


                Intent intent2= new Intent(Admin.this, AdminTherapist.class);
                startActivity(intent2);
                break;


            case R.id.probation:

                Intent intent3= new Intent(Admin.this, Probation.class);
                startActivity(intent3);
                break;




            case R.id.logout:


                FirebaseAuth.getInstance().signOut();

                Toast.makeText(Admin.this,"SIGNED OUT!!", Toast.LENGTH_SHORT).show();

                Intent intent4= new Intent(Admin.this, Login.class);
                startActivity(intent4);
                finish();

                break;


        }




        return false;


    }


}