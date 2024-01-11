package com.example.iderm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.two.fragments.HomeFragment;
import com.squareup.picasso.Picasso;

import static com.example.two.fragments.ProgressFragment.EXTRA_AGENT;
import static com.example.two.fragments.ProgressFragment.EXTRA_CREATOR;
import static com.example.two.fragments.ProgressFragment.EXTRA_ID;
import static com.example.two.fragments.ProgressFragment.EXTRA_TYPE;
import static com.example.two.fragments.ProgressFragment.EXTRA_URL;


public class DetailActivity extends AppCompatActivity {

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_URL);
        String ownerName = intent.getStringExtra(EXTRA_CREATOR);
        String id = intent.getStringExtra(EXTRA_ID);
        String agent = intent.getStringExtra(EXTRA_AGENT);
        String type = intent.getStringExtra(EXTRA_TYPE);


        ImageView imageView = findViewById(R.id.image_view_detail);
        TextView textViewOwner = findViewById(R.id.textViewOwner);
        TextView textViewId = findViewById(R.id.textViewId);


        Picasso.get().load(imageUrl).fit().centerInside().into(imageView);
        textViewOwner.setText(ownerName);
        //textViewId.setText(id);
        //textViewAgent.setText(type+" "+agent);
        textViewOwner.setTextColor(R.color.activityBackgroundColor);
        //textViewId.setTextColor(R.color.activityBackgroundColor);
    }
}