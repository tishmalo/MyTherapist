package com.example.mytherapist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Control2 extends AppCompatActivity {


    private TextView control3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control2);


        control3= findViewById(R.id.control2);

        Intent intent= getIntent();

        final String done= intent.getStringExtra("username");

        control3.setText(done);
    }
}