package com.example.mytherapist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mytherapist.Model.NotificationModel;
import com.example.mytherapist.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TherapistScheduledAdapter extends RecyclerView.Adapter<TherapistScheduledAdapter.ViewHolder> {

    Context context;
    List<NotificationModel> userList;

    public TherapistScheduledAdapter(Context context, List<NotificationModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.therapist_scheduled,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        NotificationModel nm=userList.get(position);

        holder.email.setText(nm.getemail());
        holder.date.setText(nm.getdate());

        Glide.with(context).load(nm.getprofileimage()).into(holder.imageView);





    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView email, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.userprofileimage);
            email=itemView.findViewById(R.id.email);
            date=itemView.findViewById(R.id.date);


        }
    }
}
