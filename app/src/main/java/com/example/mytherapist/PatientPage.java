package com.example.mytherapist;

import static com.amplitude.api.Utils.TAG;

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
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mytherapist.Adapter.ArticleAdapter;
import com.example.mytherapist.Model.ArticleRetrieved;
import com.example.mytherapist.Model.TherapistList;
import com.example.mytherapist.Model.TherapistPatientList;
import com.example.mytherapist.Model.image;
import com.example.mytherapist.Services.ApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class PatientPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawerLayout;
    private Toolbar toolBar;
    private NavigationView nav_view;
    RecyclerView recyclerView;
    ArticleAdapter adapter;
    List<ArticleRetrieved> list;
    Handler handler;


    private FloatingActionButton chat;
    CircleImageView imageView;
    private TextView chatText, nav_username, nav_email;
    List<TherapistPatientList> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_page);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences sharedPreferenelegate =getSharedPreferences("Sharedprefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor
                = sharedPreferenelegate.edit();
        final boolean isDarkModeOn
                = sharedPreferenelegate
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



        toolBar = findViewById(R.id.toolBar2);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("CLIENT");
        nav_view = findViewById(R.id.navigationBar1);
        drawerLayout = findViewById(R.id.drawerLayout1);

        handler=new Handler(Looper.getMainLooper());


        chat= findViewById(R.id.chat);
        chatText= findViewById(R.id.emailchat);

        readArticles();





        try {
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL(""))
                    .setWelcomePageEnabled(false)
                    .build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        DatabaseReference ref;
        ref=FirebaseDatabase.getInstance().getReference("chosen").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.exists()){

                    chat.setEnabled(false);
                    final String COMMENT= "Choose Therapist";
                    chatText.setText(COMMENT);

                    Toast.makeText(PatientPage.this,"Please choose a therapist", Toast.LENGTH_SHORT).show();


                }else{

                    chat.setEnabled(true);
                  userList = new ArrayList<>();






                      TherapistPatientList user= snapshot.getValue(TherapistPatientList.class);



                      final String END= user.gettherapistemail();

                      chatText.setText(END);

                        chat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                JitsiMeetConferenceOptions options
                                        =new JitsiMeetConferenceOptions.Builder()
                                        .setRoom(END)
                                        .build();
                                JitsiMeetActivity.launch(PatientPage.this,options);

                            }
                        });




                }


                
                
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

        nav_username=nav_view.getHeaderView(0).findViewById(R.id.username);
        nav_email=nav_view.getHeaderView(0).findViewById(R.id.email);
        imageView=nav_view.getHeaderView(0).findViewById(R.id.profilepic);


        DatabaseReference data;
        data=FirebaseDatabase.getInstance().getReference().child("PatientProfile");
        Query query=data.orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    for(DataSnapshot ds: snapshot.getChildren()) {

                        TherapistList user= ds.getValue(TherapistList.class);
                        final String USERNAME=user.getusername();


                        DatabaseReference dat;
                        dat=FirebaseDatabase.getInstance().getReference().child("PatientProfile").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

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
        }

    private void readArticles() {
        recyclerView= findViewById(R.id.newsid);


        list=new ArrayList<>();

        OkHttpClient client = new OkHttpClient();




        Request request = new Request.Builder()
                .url("https://heath-news.p.rapidapi.com/news")
                .get()
                .addHeader("X-RapidAPI-Key", "#")
                .addHeader("X-RapidAPI-Host", "heath-news.p.rapidapi.com")
                .build();




        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String Error = e.toString();

                Timber.tag(TAG).e(Error);

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Timber.tag(TAG).e(Error);

                    }
                });


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


               String resp= response.body().string();

                handler.post(new Runnable() {
                    @Override
                    public void run() {



                        Timber.tag(TAG).e(response.toString());


                        try {
                            JSONArray jsonarray= new JSONArray(resp);



                            for(int i = 0; i < jsonarray.length(); i++) {




                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String title  = jsonobject.getString("title");
                                String description   = jsonobject.getString("description");

                                String date  = jsonobject.getString("date");
                                JSONObject mjson=jsonobject.getJSONObject("image");

                                String src= mjson.getString("src");









                                ArticleRetrieved ar= new ArticleRetrieved();
                                ar.settitle(title);
                                ar.setdescription(description);
                                ar.setdate(date);
                                ar.setsrc(src);
                                list.add(ar);



                                LinearLayoutManager linearLayout= new LinearLayoutManager(PatientPage.this);
                                recyclerView.setHasFixedSize(true);
                                linearLayout.setReverseLayout(true);
                                linearLayout.setReverseLayout(true);

                                recyclerView.setLayoutManager(linearLayout);

                                adapter= new ArticleAdapter(PatientPage.this, list);
                                recyclerView.setAdapter(adapter);

                            }










                        } catch (JSONException e) {
                            e.printStackTrace();
                        }






                    }
                });


            }
        });







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



            case R.id.schedule:
                Intent intent4= new Intent(PatientPage.this, PatientNotification.class);
                startActivity(intent4);
                break;


            case R.id.message:
                Intent intent5= new Intent(PatientPage.this, PatientChatList.class);
                startActivity(intent5);
                break;



            case R.id.switchmode:
                switchMode();
                break;
            case R.id.notification:
                checkNotification();
                break;

            case R.id.review:
                Intent intent6= new Intent(PatientPage.this, Reviews.class);
                startActivity(intent6);

                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent7 = new Intent(PatientPage.this, Login.class);
                startActivity(intent7);

                Toast.makeText(PatientPage.this,"SIGNED OUT",Toast.LENGTH_SHORT).show();

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


        AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(PatientPage.this);
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

        Intent intent50= new Intent(PatientPage.this, ProbationPatient.class);
        startActivity(intent50);


    }

    private void switchMode() {

        SharedPreferences sharedPreferenelegate =getSharedPreferences("Sharedprefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor
                = sharedPreferenelegate.edit();
        final boolean isDarkModeOn
                = sharedPreferenelegate
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
