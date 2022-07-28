package com.example.mytherapist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mytherapist.Adapter.MessageAdapter;
import com.example.mytherapist.Model.TherapistList;
import com.example.mytherapist.Model.chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {


    private CircleImageView profileimg;
    private TextView username;
    private Toolbar toolBar;

    private ImageButton imageButton;
    private EditText editText;

    private FirebaseUser fuser;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;

    MessageAdapter messageAdapter;
    List<chat> chatList;
    RecyclerView recyclerView;

    boolean notify=false;

    String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolBar=findViewById(R.id.mesotoolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        profileimg=findViewById(R.id.mesoprofile);
        username=findViewById(R.id.sender);

        imageButton=findViewById(R.id.btn_send);

        recyclerView=findViewById(R.id.mesorecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        editText=findViewById(R.id.text_send);


        fuser=FirebaseAuth.getInstance().getCurrentUser();


        Intent intent=getIntent();
         String useremail= intent.getStringExtra("email");
        final String PHOTO=intent.getStringExtra("profileimage");
        final String MYID= fuser.getUid();

        final String USEREMAIL= useremail.replaceAll("[\\@\\_\\.\\^:,]","");
        final String TOPIC=useremail.trim();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify=true;
                String msg=editText.getText().toString();

                if(!msg.equals("")){
                    sendMessage(fuser.getEmail(),useremail,msg);

                }else {
                    Toast.makeText(MessageActivity.this,"Type message", Toast.LENGTH_SHORT).show();
                }
                editText.setText("");

            }
        });
        readMessages(fuser.getEmail(), useremail);

        Glide.with(getApplicationContext()).load(PHOTO).into(profileimg);
        username.setText(useremail);






    }


    private void  sendMessage(String sender, String receiver, String message){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap= new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        reference.child("chats").push().setValue(hashMap);



    }


    private void readMessages(String myid, String userid){
        chatList=new ArrayList<>();

        reference= FirebaseDatabase.getInstance().getReference("chats");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    chat shat=  dataSnapshot.getValue(chat.class);
                    if(shat.getreceiver().equals(myid) && shat.getsender().equals(userid)||shat.getreceiver().equals(userid)&&shat.getsender().equals(myid)){

                        chatList.add(shat);

                    }


                    messageAdapter= new MessageAdapter(MessageActivity.this, chatList);
                    recyclerView.setAdapter(messageAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }
}