package com.example.iderm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class DiseaseInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_information);

        Bundle bun=getIntent().getExtras();
        int val=bun.getInt("VAL");
        LinearLayout layout1 = findViewById(R.id.linearLayout1);
        LinearLayout layout2 = findViewById(R.id.linearLayout2);
        LinearLayout layout3 = findViewById(R.id.linearLayout3);
        LinearLayout layout4 = findViewById(R.id.linearLayout4);
        LinearLayout layout5 = findViewById(R.id.linearLayout5);
        Intent intent = getIntent();
        if(val == 1){
            layout1.setVisibility(View.VISIBLE);
        }
        else if(val == 2){
            layout2.setVisibility(View.VISIBLE);
        }
        else if(val == 3){
            layout3.setVisibility(View.VISIBLE);
        }
        else if(val == 4){
            layout4.setVisibility(View.VISIBLE);
        }
        else if(val == 5){
            layout5.setVisibility(View.VISIBLE);
        }
    }
}