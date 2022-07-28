package com.example.mytherapist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLogin extends AppCompatActivity {

    private EditText password;
    private Button login;

    FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        password=findViewById(R.id.password);
        login=findViewById(R.id.login);




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String EMAIL= "admin@gmail.com";
                final String PASSWORD= password.getText().toString().trim();

                mAuth=FirebaseAuth.getInstance();

                mAuth.signInWithEmailAndPassword(EMAIL, PASSWORD).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(!task.isSuccessful()){
                            String error=task.getException().toString();

                            Toast.makeText(AdminLogin.this,"authentication error"+error, Toast.LENGTH_SHORT).show();
                        }else{

                            Intent intent= new Intent(AdminLogin.this, Admin.class);
                            startActivity(intent);

                            Toast.makeText(AdminLogin.this, "Login successful", Toast.LENGTH_SHORT).show();

                        }


                    }
                });



            }
        });


    }
}