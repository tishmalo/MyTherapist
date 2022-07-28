package com.example.mytherapist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytherapist.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class TherapistNumber extends AppCompatActivity {


    EditText number;
    Button pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapist_number);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        number= findViewById(R.id.phone);
        pay= findViewById(R.id.pay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatenumber();
            }
        });



    }

    private void updatenumber() {


        DatabaseReference reference;
        reference= FirebaseDatabase.getInstance().getReference("TherapistContact").child(FirebaseAuth
                .getInstance().getCurrentUser().getUid());

        final String EMAIL= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final String PHONE=number.getText().toString();
        HashMap map= new HashMap();
        map.put("email", EMAIL);
        map.put("number",PHONE );

        reference.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Contact captured successfully", Toast.LENGTH_SHORT).show();

                    Intent intent30= new Intent(TherapistNumber.this, TherapistPage.class);
                    startActivity(intent30);
                    finish();


                }
            }
        });


    }
}