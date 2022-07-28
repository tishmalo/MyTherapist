package com.example.mytherapist.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytherapist.AdminVerifyPatient;
import com.example.mytherapist.Model.TherapistList;
import com.example.mytherapist.R;
import com.example.mytherapist.AdminVerifyTherapist;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ProbationAdapter extends RecyclerView.Adapter<ProbationAdapter.ViewHolder> {

    Context context;
    List<TherapistList> userList;


    public ProbationAdapter(Context context, List<TherapistList> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view= LayoutInflater.from(context).inflate(R.layout.probation_list, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TherapistList user= userList.get(position);

        holder.email.setText(user.getemail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference, ref;
                reference= FirebaseDatabase.getInstance().getReference("Probation");
                ref=FirebaseDatabase.getInstance().getReference("PatientProfile");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot ds: snapshot.getChildren()){
                            final String EMAIL= ds.child("email").getValue().toString();


                            Query v= reference.orderByChild("email").equalTo(EMAIL);
                            v.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if(snapshot.exists()){
                                        Intent intent=new Intent(context, AdminVerifyPatient.class);
                                        intent.putExtra("email", user.getemail());

                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                        context.startActivity(intent);

                                    }else{
                                        Intent intent2=new Intent(context, AdminVerifyTherapist.class);
                                        intent2.putExtra("email", user.getemail());

                                        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                        context.startActivity(intent2);

                                    }



                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });



    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView email;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            email=itemView.findViewById(R.id.email);


        }
    }
}
