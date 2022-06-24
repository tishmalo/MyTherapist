package com.example.mytherapist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
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

public class Therapist extends AppCompatActivity {


    private Button selfie,  proceed;
    private TextView selfieText, username2, Pass;

    private Uri resulturi;

    private String username, email, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapist);

        selfieText= findViewById(R.id.selfieText);
        selfie= findViewById(R.id.selfie);
        proceed= findViewById(R.id.proceed);


        /**
         * Get data from intent
         */

        Intent intent= getIntent();

        username=intent.getStringExtra("username");
        email=intent.getStringExtra("email");
        password=intent.getStringExtra("password");


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

                userDatabaseRef= FirebaseDatabase.getInstance().getReference("Therapist");

                HashMap map= new HashMap();

                map.put("username",username);
                map.put("email",email);

                userDatabaseRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Therapist.this, "User added successfully", Toast.LENGTH_SHORT).show();
                        }


                    }
                });


                if(resulturi!=null){



                    final StorageReference filepath= FirebaseStorage.getInstance().getReference()
                            .child("therapist_pic").child(email);

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
                            Toast.makeText(Therapist.this, "Upload failed", Toast.LENGTH_SHORT).show();


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
                                        getimagemap.put("username", username);
                                        getimagemap.put("email", email);






                                        userDatabaseRef.updateChildren(getimagemap).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Therapist.this, "Image uploaded successfuly", Toast.LENGTH_SHORT);




                                                } else {
                                                    Toast.makeText(Therapist.this, task.getException().toString(), Toast.LENGTH_SHORT);
                                                }

                                            }
                                        });
                                        finish();
                                    }
                                });

                            }

                        }
                    });

                    Intent intent3 = new Intent(Therapist.this, Therapist2.class);
                    intent3.putExtra("username", username);
                    intent3.putExtra("email", email);
                    intent3.putExtra("password", password);
                    Therapist.this.startActivity(intent3);




                }

            }
        });








    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == 1) {

            resulturi = data.getData();

            final String text = resulturi.getPath().trim();

            selfieText.setText(text);


        } else {

            super.onActivityResult(requestCode, resultCode, data);
        }


    }

    /**
     * Now to therapist2
     */

}