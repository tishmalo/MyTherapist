package com.example.mytherapist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mytherapist.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Schedule extends AppCompatActivity {

    private CircleImageView circleImageView;
    private TextView username,date;
    private Button submit;

    Toolbar toolbar;

    DatePickerDialog.OnDateSetListener setListener;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

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

        circleImageView = findViewById(R.id.icon);
        username = findViewById(R.id.username);
        date = findViewById(R.id.email);
        submit = findViewById(R.id.therapist);


        Intent intent30= getIntent();
        final String PHOTO= intent30.getStringExtra("profileimage");
        final String USERNAME= intent30.getStringExtra("username");
        final String EMAIL=intent30.getStringExtra("email");

        Glide.with(Schedule.this).load(PHOTO).into(circleImageView);

        username.setText(USERNAME);





        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                DatePickerDialog datePickerDialog = new DatePickerDialog(Schedule.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                datePickerDialog.show();
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                month = month + 1;
                String fullDate = day + "/" + month + "/" + year;

                date.setText(fullDate);



                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final String THERAPIST= FirebaseAuth.getInstance().getCurrentUser().getEmail();

                        final String DAYOFWEEK =fullDate.trim();
                        DatabaseReference reference;
                        reference= FirebaseDatabase.getInstance().getReference("Notification").child(USERNAME);
                        HashMap user= new HashMap();
                        user.put("profileimage",PHOTO);
                        user.put("username",USERNAME);
                        user.put("email",EMAIL);
                        user.put("therapistemail", THERAPIST);
                        user.put("date",DAYOFWEEK);


                        reference.updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Schedule.this,"Schedule set successfully", Toast.LENGTH_SHORT).show();

                                    Intent intent50= new Intent(Schedule.this, TherapistPage.class);
                                    startActivity(intent50);

                                }else{
                                    Toast.makeText(Schedule.this,"Error setting schedule",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });


            }


        };



    }

}