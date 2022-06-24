package com.example.mytherapist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Control extends AppCompatActivity {


    private EditText control;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        control= findViewById(R.id.trial);
        button= findViewById(R.id.submit);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String control2= control.getText().toString().trim();

                Intent intent = new Intent(Control.this, Control2.class);
                intent.putExtra("control", control2);
                Control.this.startActivity(intent);



            }
        });




    }
}