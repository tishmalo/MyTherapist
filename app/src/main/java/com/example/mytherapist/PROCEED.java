package com.example.mytherapist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PROCEED extends AppCompatActivity {

    /**
     * Initialize xml elements
     * @param savedInstanceState
     */

    private Button patient, therapist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_r_o_c_e_e_d);

        /**
         * Link java to xml
         */
        patient= findViewById(R.id.patient);
        therapist=findViewById(R.id.therapist);

        /**
         * Set event that happens when then buttons are clicked
         */

        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(PROCEED.this,Patient.class);
                startActivity(intent);
                finish();
            }
        });

        therapist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PROCEED.this, Patient.class);
                startActivity(intent);
                finish();

            }

        });

        /**
         * Now test this activity
         */


    }
}