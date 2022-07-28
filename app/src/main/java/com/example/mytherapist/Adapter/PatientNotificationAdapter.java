package com.example.mytherapist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytherapist.Model.NotificationModel;
import com.example.mytherapist.R;

import java.util.List;

public class PatientNotificationAdapter extends RecyclerView.Adapter<PatientNotificationAdapter.ViewHolder> {


    Context context;
    private List<NotificationModel> userList;

    public PatientNotificationAdapter(Context context, List<NotificationModel> userList) {
        this.context = context;
        this.userList = userList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.confirmed_schedule,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        NotificationModel user= userList.get(position);

        String schedule= "SCHEDULED VISIT";

        holder.username.setText(schedule);
        holder.date.setText(user.getdate());


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView username, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            username=itemView.findViewById(R.id.username);
            date=itemView.findViewById(R.id.date);


        }
    }
}
