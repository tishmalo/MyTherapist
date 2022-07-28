package com.example.mytherapist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProbationTherapist extends AppCompatActivity {


    private de.hdodenhof.circleimageview.CircleImageView profilepic;
    private Button selfie,  proceed;
    private TextView selfieText, username2, Pass;

    private Uri resulturi;

    private String username, email, password;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_probation_therapist);

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
        selfieText= findViewById(R.id.selfieText);
        selfie= findViewById(R.id.selfie);
        proceed= findViewById(R.id.proceed);

        profilepic = findViewById(R.id.therapistpicture);
        /**
         * Get data from intent
         */





        DatabaseReference reference;
        reference=FirebaseDatabase.getInstance().getReference("Therapist_Profile");
        Query v=reference.orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        v.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds: snapshot.getChildren()){

                    final String EMAIL= ds.child("email").getValue().toString();
                    final String USERNAME=ds.child("username").getValue().toString();




                    /**
                     * Get data from local server
                     */

                    selfie.setOnClickListener(new View.OnClickListener() {

                        static final int REQUEST_IMAGE_GET = 1;
                        @Override
                        public void onClick(View view) {
                            Intent intent1= new Intent(Intent.ACTION_PICK);
                            intent1.setType("image/*");
                            startActivityForResult(intent1, REQUEST_IMAGE_GET);


                        }
                    });


                    /**
                     * Upload to firebase... Hope it works
                     */

                    proceed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatabaseReference userDatabaseRef;

                            userDatabaseRef= FirebaseDatabase.getInstance().getReference("Therapist_Profile").child(USERNAME);

                            HashMap map= new HashMap();

                            map.put("username",USERNAME);
                            map.put("email",EMAIL);

                            userDatabaseRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(ProbationTherapist.this, "User added successfully", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });


                            if(resulturi!=null){



                                final StorageReference filepath= FirebaseStorage.getInstance().getReference()
                                        .child("therapist_profile").child(EMAIL);

                                Bitmap bitmap;
                                bitmap=null;

                                try{
                                    bitmap= MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resulturi);
                                }catch (IOException e) {
                                    e.printStackTrace();
                                }


                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);

                                byte[] data = byteArrayOutputStream.toByteArray();

                                UploadTask uploadTask = filepath.putBytes(data);

                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ProbationTherapist.this, "Upload failed", Toast.LENGTH_SHORT).show();


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
                                                    getimagemap.put("profileimage", imageuri);
                                                    getimagemap.put("username", USERNAME);
                                                    getimagemap.put("email", EMAIL);






                                                    userDatabaseRef.updateChildren(getimagemap).addOnCompleteListener(new OnCompleteListener() {
                                                        @Override
                                                        public void onComplete(@NonNull Task task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(ProbationTherapist.this, "Image uploaded successfuly", Toast.LENGTH_SHORT);




                                                            } else {
                                                                Toast.makeText(ProbationTherapist.this, task.getException().toString(), Toast.LENGTH_SHORT);
                                                            }

                                                        }
                                                    });
                                                    finish();
                                                }
                                            });

                                        }

                                    }
                                });

                                Intent intent3 = new Intent(ProbationTherapist.this, ProbationTherapist2.class);
                                intent3.putExtra("username", USERNAME);
                                intent3.putExtra("email", EMAIL);
                                ProbationTherapist.this.startActivity(intent3);




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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == 1) {

            resulturi = data.getData();

            final String text = resulturi.getPath().trim();

            selfieText.setText(text);
            Glide.with(ProbationTherapist.this).load(resulturi).into(profilepic);

        } else {

            super.onActivityResult(requestCode, resultCode, data);
        }


    }

}