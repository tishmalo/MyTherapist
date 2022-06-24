package com.example.mytherapist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
  private FirebaseAuth mAuth;
   private TextView forgot, already;
   private EditText email, password;
   private Button login, login2;



   private Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();

        forgot= findViewById(R.id.forgot);
        already=findViewById(R.id.already);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        login2=findViewById(R.id.logintherapist);


        email.setTextColor(Color.WHITE);
        password.setTextColor(Color.WHITE);

/**
 *
 * If not yet registered, take user to proceed page
 * first
 */

already.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent= new Intent(Login.this, Register.class);
        startActivity(intent);
    }
});



        /**
         * Verify the user if they have been registered. This focuses on the patient side.
         */

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify();
            }





        });


        /**
         * Check if the therapist has been registered.
         */

        login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Verifytherapist();
            }
        });






    }

    private void Verifytherapist() {

        /**
         * Check validity of text entered
         *
         */
        final String email1= email.getText().toString().trim();
        final String password1=password.getText().toString().trim();

        /**
         * sign in ith the stored details
         */

        if(email1 !=null){
            if(password1 !=null){

                mAuth.signInWithEmailAndPassword(email1,password1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Therapist logged in successfully", Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(Login.this, TherapistPage.class);
                            startActivity(intent);

                        }else{
                            Toast.makeText(Login.this,"Authentication error", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }

        }


    }

    private void verify() {
        /**
         * Check the validity of the text inserted
         */
        final String email30=  email.getText().toString().trim();
        final String password30= password.getText().toString().trim();

        if(email30  !=null){

            if(password30 !=null){

                // Sign in with the stored details

                mAuth.signInWithEmailAndPassword(email30,password30).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(Login.this,"Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(Login.this, PatientPage.class);
                            startActivity(intent);



                        }else{
                            Toast.makeText(Login.this, "Authentication error",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        checkAuth();



    }

    private void checkAuth() {
        /**
         * Checkif user is already signed in
         */
        FirebaseUser currentUser= mAuth.getCurrentUser();

    }
}