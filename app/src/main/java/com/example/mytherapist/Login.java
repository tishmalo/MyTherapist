package com.example.mytherapist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytherapist.Model.TherapistList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
  private FirebaseAuth mAuth;
   private TextView forgot, already, log;
   private EditText email, password;
   private Button login, login2;

    FirebaseAuth.AuthStateListener mAuthStateListener;

   private Context context;
   private String email1;
    private String email30;
    private SharedPreferences sharedpreferences;

    public static final String MyPreferences= "MyPrefs";
    public static final String Email ="EMAIL";
    public static final String Password="PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuth=FirebaseAuth.getInstance();

        forgot= findViewById(R.id.forgot);
        already=findViewById(R.id.already);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        login2=findViewById(R.id.logintherapist);
        log= findViewById(R.id.log);


        final String email1= email.getText().toString().trim();
        final String password1=password.getText().toString().trim();

        sharedpreferences= getSharedPreferences(MyPreferences,MODE_PRIVATE);


        SharedPreferences.Editor editor= sharedpreferences.edit();

        editor.putString("Email",email1);
        editor.putString("Password", password1);
        editor.commit();


        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent0= new Intent(Login.this, AdminLogin.class);
                startActivity(intent0);
            }
        });



        mAuthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {





            }
        };


        email.setTextColor(Color.WHITE);
        password.setTextColor(Color.WHITE);


        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent88= new Intent(Login.this, ResetPassword.class);
                startActivity(intent88);
            }
        });


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

                            DatabaseReference ref;
                            ref= FirebaseDatabase.getInstance().getReference(
                                    "Therapist_Profile").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if(snapshot.exists()){

                                        Toast.makeText(Login.this, "Therapist logged in successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent= new Intent(Login.this, TherapistPage.class);
                                        startActivity(intent);


                                    }else{
                                        Toast.makeText(getApplicationContext(), "Wrong page", Toast.LENGTH_SHORT).show();

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });







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





                            DatabaseReference ref;
                            ref= FirebaseDatabase.getInstance().getReference(
                                    "PatientProfile").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if(snapshot.exists()){

                                        Toast.makeText(Login.this,"Login Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent= new Intent(Login.this, PatientPage.class);
                                        startActivity(intent);


                                    }else{
                                        Toast.makeText(getApplicationContext(), "Wrong page doctor", Toast.LENGTH_SHORT).show();

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });






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



        mAuth.addAuthStateListener(mAuthStateListener);



    }

    @Override
    protected void onStop() {
        super.onStop();


        mAuth.removeAuthStateListener(mAuthStateListener);


    }

    private void checkAuth() {
        /**
         * Checkif user is already signed in
         *
         *
         *
         */



    }
}