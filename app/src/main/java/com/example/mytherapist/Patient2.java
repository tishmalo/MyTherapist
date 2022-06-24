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

public class Patient2 extends AppCompatActivity {

    private TextView identitytext, email1, password1;
    private Button identity, submit;
    private FirebaseAuth mAuth;
    private Uri resulturi;

    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient2);


        identitytext= findViewById(R.id.identitytext);

        identity=findViewById(R.id.identity);
        submit=findViewById(R.id.proceed);

        email1= findViewById(R.id.email);
        password1=findViewById(R.id.password);



        /**
         get data from Register activity
         */
        Intent intent = getIntent();

        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");

      

        identity.setOnClickListener(new View.OnClickListener() {
            static final int REQUEST_IMAGE_GET = 1;
            @Override

            public void onClick(View view) {


                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE_GET);


            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email2= email.trim();
                final String password2=password.trim();

                mAuth= FirebaseAuth.getInstance();

                mAuth.createUserWithEmailAndPassword(email2, password2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()){

                            String error=task.getException().toString();
                            Toast.makeText(Patient2.this,"Error"+error,Toast.LENGTH_SHORT).show();
                        }else{


                            DatabaseReference userDatabaseRef;

                            userDatabaseRef= FirebaseDatabase.getInstance().getReference("PatientIdentity").child(username);

                            HashMap userinfo= new HashMap();
                            userinfo.put("username",username);
                            userinfo.put("email", email);

                            userDatabaseRef.updateChildren(userinfo).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Patient2.this, "Identity updated", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });










                            if (resulturi != null) {
                                final StorageReference filepath = FirebaseStorage.getInstance().getReference()
                                        .child("identity_images").child(email);
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
                                        Toast.makeText(Patient2.this, "Upload failed", Toast.LENGTH_SHORT).show();


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
                                                    getimagemap.put("Identity", imageuri);
                                                    getimagemap.put("username",username);
                                                    getimagemap.put("email", email);





                                                    userDatabaseRef.updateChildren(getimagemap).addOnCompleteListener(new OnCompleteListener() {
                                                        @Override
                                                        public void onComplete(@NonNull Task task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(Patient2.this, "Image uploaded successfuly", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(Patient2.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
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



                    }
                });



                Intent intent30= new Intent(Patient2.this, PatientPage.class);
                startActivity(intent30);
            }


        });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {




        if(requestCode ==1 ) {

            resulturi= data.getData();


            final String photouri = resulturi.getPath().trim();


            identitytext.setText(photouri);



    }else{

            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}