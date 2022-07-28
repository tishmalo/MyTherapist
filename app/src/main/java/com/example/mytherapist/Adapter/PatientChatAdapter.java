package com.example.mytherapist.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mytherapist.ChatList;
import com.example.mytherapist.MessageActivity;
import com.example.mytherapist.Model.PatientPusher;
import com.example.mytherapist.Model.TherapistList;
import com.example.mytherapist.Model.TherapistPatientList;
import com.example.mytherapist.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientChatAdapter extends RecyclerView.Adapter<PatientChatAdapter.ViewHolder>{

    private Context context;
    private List<PatientPusher> userList;

    public PatientChatAdapter(Context context, List<PatientPusher> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public PatientChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view= LayoutInflater.from(context).inflate(R.layout.patient_chat_displayed,parent,false);


        return new ViewHolder(view);
    }


    /**
     * Hii iko kwa therapist page
     * @param holder
     * @param position
     */

    @Override
    public void onBindViewHolder(@NonNull PatientChatAdapter.ViewHolder holder, int position) {

        PatientPusher user= userList.get(position);


        holder.email.setText(user.getuseremail());

        Glide.with(context).load(user.getprofileimage()).into(holder.imageView);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, MessageActivity.class);

                intent.putExtra("email",user.getuseremail());
                intent.putExtra("profileimage", user.getprofileimage());
                intent.putExtra("userid",user.getuserid());

                context.startActivity(intent);


            }
        });






    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        
        private CircleImageView imageView;
        private TextView username, email;
        
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            
            imageView= itemView.findViewById(R.id.userprofileimage);
            username=itemView.findViewById(R.id.username);
            email=itemView.findViewById(R.id.email);






            
            
        }
    }
}
