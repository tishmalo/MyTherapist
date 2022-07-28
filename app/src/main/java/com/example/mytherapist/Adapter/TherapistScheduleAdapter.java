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
import com.example.mytherapist.Model.TherapistList;
import com.example.mytherapist.R;
import com.example.mytherapist.Schedule;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TherapistScheduleAdapter extends RecyclerView.Adapter<TherapistScheduleAdapter.ViewHolder> {

    private Context context;
    private List<TherapistList> userList;

    public TherapistScheduleAdapter(Context context, List<TherapistList> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.patient_displayed,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final TherapistList USER= userList.get(position);

        holder.username.setText(USER.getusername());
        holder.email.setText(USER.getemail());

        Glide.with(context).load(USER.getprofileimage()).into(holder.circleImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context, Schedule.class);
                intent.putExtra("profileimage", USER.getprofileimage());
                intent.putExtra("email", USER.getemail());
                intent.putExtra("username", USER.getusername());
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

            username= itemView.findViewById(R.id.username);
            email=itemView.findViewById(R.id.email);
            circleImageView= itemView.findViewById(R.id.userprofileimage);
        }
    }
}
