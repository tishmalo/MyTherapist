package com.example.mytherapist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mytherapist.Model.TherapistList;
import com.example.mytherapist.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.ViewHolder> {

    private Context context;
    private List<TherapistList> userList;

    public PatientListAdapter(Context context, List<TherapistList> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.activity_therapistlist,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final TherapistList therapistList= userList.get(position);

        holder.username.setText(therapistList.getUsername());
        holder.email.setText(therapistList.getEmail());

        Glide.with(context).load(therapistList.getProfileimage()).into(holder.circleImageView);


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
