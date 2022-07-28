package com.example.mytherapist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


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
                String username2=Utils.sanitizeusername(username1);
                final String email1 = email.getText().toString().trim();
                final String password1 = password.getText().toString().trim();
                final String password2 = cpassword.getText().toString().trim();


                if (password1.matches(password2)) {




                    FirebaseAuth mAuth;
                    mAuth=FirebaseAuth.getInstance();

                    mAuth.createUserWithEmailAndPassword(email1,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "User registered", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(Register.this, Patient.class);
                                intent.putExtra("username", username2);
                                intent.putExtra("email", email1);
                                intent.putExtra("password", password1);
                                Register.this.startActivity(intent);


                            }else{
                                Toast.makeText(getApplicationContext(), "Authentication error", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });




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

                String username2=Utils.sanitizeusername(username1);
                final String email1 = email.getText().toString().trim();
                final String password1 = password.getText().toString().trim();
                final String password2 = cpassword.getText().toString().trim();


                if (password1.matches(password2)) {



                    FirebaseAuth mAuth;
                    mAuth=FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(email1,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                Toast.makeText(getApplicationContext(), "User registered", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(Register.this, Therapist.class);
                                intent.putExtra("username", username2);
                                intent.putExtra("email", email1);
                                intent.putExtra("password", password1);
                                Register.this.startActivity(intent);

                            }else{

                                Toast.makeText(getApplicationContext(), "Authentication error", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                    finish();

                }

            }
        });


    }




}