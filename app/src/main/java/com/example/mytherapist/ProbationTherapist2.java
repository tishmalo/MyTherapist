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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProbationTherapist2 extends AppCompatActivity {


    private TextView identitytext;
    private Button identity, proceed;
    private String username, email, password;
    private Uri resulturi;
    private de.hdodenhof.circleimageview.CircleImageView profilepic;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_probation_therapist2);

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

        identitytext= findViewById(R.id.identitytext);
        identity= findViewById(R.id.identity);
        proceed= findViewById(R.id.proceed);
        profilepic = findViewById(R.id.therapistpicture);

        /**
         * Get data from intent
         *
         */
        Intent intent= getIntent();
        username= intent.getStringExtra("username");
        email=intent.getStringExtra("email");



        /**
         * Get photo from local storage
         */

        identity.setOnClickListener(new View.OnClickListener() {
            static final int REQUEST_IMAGE_GET = 1;
            @Override
            public void onClick(View view) {
                Intent intent1= new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1, REQUEST_IMAGE_GET);
            }
        });


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference userDatabaseRef;

                userDatabaseRef= FirebaseDatabase.getInstance().getReference("Therapist_Identity").child(username);

                HashMap map= new HashMap();

                map.put("username",username);
                map.put("email",email);

                userDatabaseRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ProbationTherapist2.this, "User added successfully", Toast.LENGTH_SHORT).show();
                        }


                    }
                });


                if(resulturi!=null){



                    final StorageReference filepath= FirebaseStorage.getInstance().getReference()
                            .child("therapist_identity").child(email);

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
                            Toast.makeText(ProbationTherapist2.this, "Upload failed", Toast.LENGTH_SHORT).show();


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
                                        getimagemap.put("identity", imageuri);
                                        getimagemap.put("username", username);
                                        getimagemap.put("email", email);






                                        userDatabaseRef.updateChildren(getimagemap).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(ProbationTherapist2.this, "Image uploaded successfuly", Toast.LENGTH_SHORT);




                                                } else {
                                                    Toast.makeText(ProbationTherapist2.this, task.getException().toString(), Toast.LENGTH_SHORT);
                                                }

                                            }
                                        });
                                        finish();
                                    }
                                });

                            }

                        }
                    });

                    Intent intent3 = new Intent(ProbationTherapist2.this, Therapist3.class);
                    intent3.putExtra("username", username);
                    intent3.putExtra("email", email);

                    ProbationTherapist2.this.startActivity(intent3);




                }

            }
        });




    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode ==1){

            resulturi= data.getData();

            final String text= resulturi.getPath().trim();

            identitytext.setText(text);
            Glide.with(ProbationTherapist2.this).load(resulturi).into(profilepic);

        }else{

            super.onActivityResult(requestCode, resultCode, data);
        }


    }


}