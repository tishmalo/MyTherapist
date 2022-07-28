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
import com.example.mytherapist.Model.PatientPusher;
import com.example.mytherapist.PatientMessage;
import com.example.mytherapist.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TherapistChatAdapter extends RecyclerView.Adapter<TherapistChatAdapter.ViewHolder> {

    private Context context;
    private List<PatientPusher> userList;

    public TherapistChatAdapter(Context context, List<PatientPusher> userList) {
        this.context = context;
        this.userList = userList;
    }


    @NonNull
    @Override
    public TherapistChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.therapist_chat_displayed, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TherapistChatAdapter.ViewHolder holder, int position) {

        PatientPusher pp= userList.get(position);

        holder.email.setText(pp.gettherapistemail());

        Glide.with(context).load(pp.getprofileimage()).into(holder.circleImageView);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context, PatientMessage.class);
                intent.putExtra("profileimage", pp.getprofileimage());
                intent.putExtra("email",pp.gettherapistemail());
                intent.putExtra("username",pp.getuserid());

                context.startActivity(intent);


            }
        });





    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView circleImageView;
        private TextView username, email;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView= itemView.findViewById(R.id.userprofileimage);
            username=itemView.findViewById(R.id.username);
            email=itemView.findViewById(R.id.email);



        }
    }
}
