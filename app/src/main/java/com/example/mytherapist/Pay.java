package com.example.mytherapist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mytherapist.Model.AccessToken;
import com.example.mytherapist.Model.STKPush;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.example.mytherapist.Constants.BUSINESS_SHORT_CODE;
import static com.example.mytherapist.Constants.CALLBACKURL;
import static com.example.mytherapist.Constants.PARTYA;
import static com.example.mytherapist.Constants.PARTYB;
import static com.example.mytherapist.Constants.PASSKEY;
import static com.example.mytherapist.Constants.TRANSACTION_TYPE;

import java.util.HashMap;


public class Pay extends AppCompatActivity implements View.OnClickListener{


    private DarajaApiClient mApiClient;
    private ProgressDialog mProgressDialog;

    Toolbar toolbar;

    @BindView(R.id.amount)
    EditText mAmount;
    @BindView(R.id.phone)
    EditText mPhone;
    @BindView(R.id.pay)
    Button mPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ButterKnife.bind(this);

        mProgressDialog = new ProgressDialog(this);
        mApiClient = new DarajaApiClient();
        mApiClient.setIsDebug(true); //Set True to enable logging, false to disable.

        mPay.setOnClickListener(this);

        mPhone.setTextColor(Color.WHITE);
        mAmount.setTextColor(Color.WHITE);

        getAccessToken();


    }


    public void getAccessToken() {
        mApiClient.setGetAccessToken(true);
        mApiClient.mpesaService().getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {

                if (response.isSuccessful()) {
                    mApiClient.setAuthToken(response.body().accessToken);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view== mPay){
            String phone_number = mPhone.getText().toString();
            String amount = mAmount.getText().toString();
            performSTKPush(phone_number,amount);


            DatabaseReference reference, databaseReference, userRef;
            reference= FirebaseDatabase.getInstance().getReference("chosen");
            userRef=FirebaseDatabase.getInstance().getReference("TherapistContact");
            databaseReference= FirebaseDatabase.getInstance().getReference("Payment");

            Query v= reference.orderByChild("useremail").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            v.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot sn: snapshot.getChildren()){
                        final String THERAPISTEMAIL= sn.child("therapistemail").getValue().toString();
                        final String USEREMAIL=FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        final String PAY= mAmount.getText().toString();

                        Query a= userRef.orderByChild("email").equalTo(THERAPISTEMAIL);

                        a.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {


                                for (DataSnapshot da: snapshot.getChildren()){
                                    final String PHONE= da.child("number").getValue().toString();
                                    final String USERID= FirebaseAuth.getInstance().getCurrentUser().getUid();


                                    HashMap map= new HashMap();

                                    map.put("therapistemail",THERAPISTEMAIL);
                                    map.put("useremail", USEREMAIL);
                                    map.put("amount",PAY);
                                    map.put("number", PHONE);


                                    databaseReference.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {


                                            if (task.isSuccessful()){

                                                Toast.makeText(getApplicationContext(), "PAYMENT CAPTURED",Toast.LENGTH_SHORT).show();
                                            }


                                        }
                                    });



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
    }


    public void performSTKPush(String phone_number,String amount) {
        mProgressDialog.setMessage("Processing your request");
        mProgressDialog.setTitle("Please Wait...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        String timestamp = Utils.getTimestamp();
        STKPush stkPush = new STKPush(
                BUSINESS_SHORT_CODE,
                Utils.getPassword(BUSINESS_SHORT_CODE, PASSKEY, timestamp),
                timestamp,
                TRANSACTION_TYPE,
                String.valueOf(amount),
                Utils.sanitizePhoneNumber(phone_number),
                PARTYB,
                Utils.sanitizePhoneNumber(phone_number),
                CALLBACKURL,
                "MPESA Android Test", //Account reference
                "Testing"  //Transaction description
        );

        mApiClient.setGetAccessToken(false);

        //Sending the data to the Mpesa API, remember to remove the logging when in production.
        mApiClient.mpesaService().sendPush(stkPush).enqueue(new Callback<STKPush>() {
            @Override
            public void onResponse(@NonNull Call<STKPush> call, @NonNull Response<STKPush> response) {
                mProgressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        Timber.d("post submitted to API. %s", response.body());
                    } else {
                        Timber.e("Response %s", response.errorBody().string());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<STKPush> call, @NonNull Throwable t) {
                mProgressDialog.dismiss();
                Timber.e(t);
            }
        });
    }


}