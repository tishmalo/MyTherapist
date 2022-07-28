package com.example.mytherapist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProbationTherapist3 extends AppCompatActivity {


    private Button certificate, proceed;
    private TextView certificateText;
    private String username, email,password;
    private Uri resulturi;

    FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;
    Toolbar toolbar;

    private de.hdodenhof.circleimageview.CircleImageView profilepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_probation_therapist3);

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

        certificate= findViewById(R.id.certificate);
        proceed= findViewById(R.id.proceed);
        certificateText= findViewById(R.id.certificateText);
        profilepic = findViewById(R.id.therapistpicture);

        /**
         * Get intent
         */

        Intent intent= getIntent();
        username=intent.getStringExtra("username");
        email=intent.getStringExtra("email");
        password=intent.getStringExtra("password");


        /**
         * Get data from local storage
         */

        certificate.setOnClickListener(new View.OnClickListener() {
            final static int REQUEST_IMAGE_GET=1;
            @Override
            public void onClick(View view) {
                Intent intent1= new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1,REQUEST_IMAGE_GET);
            }
        });

        /***
         * Upload to firebase
         */
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                            userDatabaseRef= FirebaseDatabase.getInstance().getReference("Therapist_Certificate").child(username);

                            HashMap userinfo= new HashMap();
                            userinfo.put("username",username);
                            userinfo.put("email", email);

                            userDatabaseRef.updateChildren(userinfo).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(ProbationTherapist3.this, "Identity updated", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });










                            if (resulturi != null) {
                                final StorageReference filepath = FirebaseStorage.getInstance().getReference()
                                        .child("Therapy_certificate").child(email);
                                Bitmap bitmap = null;

                                try {
                                    bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resulturi);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);

                                byte[] data = byteArrayOutputStream.toByteArray();

                                UploadTask uploadTask = filepath.putBytes(data);

                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ProbationTherapist3.this, "Upload failed", Toast.LENGTH_SHORT).show();


                                    }
                                });
                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        if (taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null) {
                                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String imageuri = uri.toString();
                                                    Map getimagemap = new HashMap();
                                                    getimagemap.put("certificate", imageuri);
                                                    getimagemap.put("username",username);
                                                    getimagemap.put("email", email);





                                                    userDatabaseRef.updateChildren(getimagemap).addOnCompleteListener(new OnCompleteListener() {
                                                        @Override
                                                        public void onComplete(@NonNull Task task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(ProbationTherapist3.this, "Image uploaded successfuly", Toast.LENGTH_SHORT);
                                                            } else {
                                                                Toast.makeText(ProbationTherapist3.this, task.getException().toString(), Toast.LENGTH_SHORT);
                                                            }

                                                        }
                                                    });

                                                }
                                            });

                                        }

                                    }
                                });


                            }





                    }
                });



                Intent intent30= new Intent(ProbationTherapist3.this, TherapistPage.class);
                startActivity(intent30);


            }
        }





