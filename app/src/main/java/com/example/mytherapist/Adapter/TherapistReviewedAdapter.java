package com.example.mytherapist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mytherapist.Model.TherapistReviewModel;
import com.example.mytherapist.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TherapistReviewedAdapter extends RecyclerView.Adapter<TherapistReviewedAdapter.ViewHolder> {

    private Context context;
    private List<TherapistReviewModel> userList;

    public TherapistReviewedAdapter(Context context, ArrayList<TherapistReviewModel> userList) {
        this.context = context;
        this.userList = userList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.therapist_reviewed,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        final TherapistReviewModel tr= userList.get(position);

        holder.review.setText(tr.getreview());


        Glide.with(context).load(tr.getprofilepic()).into(holder.circleImageView);





    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView circleImageView;
        private TextView review;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView= itemView.findViewById(R.id.userprofileimage);
            review=itemView.findViewById(R.id.review);


        }
    }
}
