package com.example.mytherapist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytherapist.Model.PaymentModel;
import com.example.mytherapist.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    Context context;
    List<PaymentModel> userList;

    public PaymentAdapter(Context context, List<PaymentModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.payment_displayed,parent,false);




        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        PaymentModel pm= userList.get(position);

        holder.useremail.setText(pm.getuseremail());
        holder.therapist.setText(pm.gettherapistemail());
        holder.amount.setText(pm.getamount());
        holder.number.setText(pm.getnumber());

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref;
                ref= FirebaseDatabase.getInstance().getReference("Payment");
               if(ref.child("useremail").equals(pm.getuseremail())){
                   ref.removeValue();
               }

            }
        });



    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView useremail,therapist,number,amount;
        private Button btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            useremail=itemView.findViewById(R.id.useremail);
            therapist=itemView.findViewById(R.id.therapistemail);
            number=itemView.findViewById(R.id.number);
            amount=itemView.findViewById(R.id.amount);
            btn=itemView.findViewById(R.id.payment);



        }
    }
}
