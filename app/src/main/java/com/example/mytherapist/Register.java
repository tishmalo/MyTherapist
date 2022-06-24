package com.example.mytherapist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Register extends AppCompatActivity {


    /**
     * Initialize variabkes used in xml
     *
     */
    private EditText username, email, password, cpassword;
    private Button patient, therapist;
    private TextView already;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /**
         * Link java to xml
         */

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        cpassword = findViewById(R.id.cpassword);
        patient = findViewById(R.id.patient);
        therapist = findViewById(R.id.therapist);
        already=findViewById(R.id.already);


        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });


        username.setTextColor(Color.WHITE);
        email.setTextColor(Color.WHITE);
        password.setTextColor(Color.WHITE);
        cpassword.setTextColor(Color.WHITE);



        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username1 = username.getText().toString().trim();
                final String email1 = email.getText().toString().trim();
                final String password1 = password.getText().toString().trim();
                final String password2 = cpassword.getText().toString().trim();


                if (password1.matches(password2)) {

                    Intent intent = new Intent(Register.this, Patient.class);
                    intent.putExtra("username", username1);
                    intent.putExtra("email", email1);
                    intent.putExtra("password", password1);
                    Register.this.startActivity(intent);




                    finish();

                }
            }


        });

        therapist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gototherapist();
            }

            private void gototherapist() {

                final String username1 = username.getText().toString().trim();
                final String email1 = email.getText().toString().trim();
                final String password1 = password.getText().toString().trim();
                final String password2 = cpassword.getText().toString().trim();


                if (password1.matches(password2)) {

                    Intent intent = new Intent(Register.this, Therapist.class);
                    intent.putExtra("username", username1);
                    intent.putExtra("email", email1);
                    intent.putExtra("password", password1);
                    Register.this.startActivity(intent);


                    finish();

                }

            }
        });


    }




}