package com.example.mytherapist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Reviews extends AppCompatActivity {


    private EditText review;

    TextView therapistemail;
    CircleImageView circleImageView;
    private Button button;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

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

        review= findViewById(R.id.review);
        therapistemail=findViewById(R.id.therapistemail);
        circleImageView=findViewById(R.id.therapistpic);
        button= findViewById(R.id.submit);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

        DatabaseReference ref;
        ref= FirebaseDatabase.getInstance().getReference("chosen");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds: snapshot.getChildren()){

                    final String EMAIL= ds.child("therapistemail").getValue().toString();



                    final String REVIEW=review.getText().toString().trim();

                    DatabaseReference userDat;
                    userDat= FirebaseDatabase.getInstance().getReference("TherapistReviews").child(FirebaseAuth.getInstance()
                            .getCurrentUser().getUid());

                    HashMap map= new HashMap();
                    map.put("therapistemail", EMAIL);
                    map.put("review",REVIEW);

                    userDat.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if(task.isSuccessful()){
                                Toast.makeText(Reviews.this, "Review added", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(Reviews.this, "ERROR!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });



                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });










            }
        });




    }
}