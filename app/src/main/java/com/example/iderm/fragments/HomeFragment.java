package com.example.iderm.fragments;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.two.ClassificationActivity;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import com.example.two.DiseaseInformationActivity;
import com.example.two.HelperClasses.infoHelper;
import com.example.two.HelperClasses.melanomaInfo;

import com.example.two.PostsRecyclerAdapter;


import com.example.two.ImageActivity;
import com.example.two.R;



public class HomeFragment extends Fragment implements melanomaInfo.ListItemClickListener {
    RecyclerView phoneRecycler;
    RecyclerView.Adapter adapter;
    FirebaseAuth auth;
    DatabaseReference myRef;
    private PostsRecyclerAdapter recyclerAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button imgBtn = getActivity().findViewById(R.id.btn_pick_photo);
        auth = FirebaseAuth.getInstance();
        auth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        phoneRecycler = getActivity().findViewById(R.id.my_recycler);
        phoneRecycler();

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClassificationActivity.class);
                startActivity(intent);
            }
        });


    }

    private void phoneRecycler() {

        //All Gradients
        GradientDrawable gradient2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffd4cbe5, 0xffd4cbe5});
        GradientDrawable gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff7adccf, 0xff7adccf});
        GradientDrawable gradient3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xfff7c59f, 0xFFf7c59f});
        GradientDrawable gradient4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffb8d7f5, 0xffb8d7f5});


        phoneRecycler.setHasFixedSize(true);
        phoneRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        ArrayList<infoHelper> phonelocations = new ArrayList<>();
        phonelocations.add(new infoHelper(gradient1, "What is Skin Cancer?"));
        phonelocations.add(new infoHelper(gradient4,  "Melanoma Risk Factors"));
        phonelocations.add(new infoHelper(gradient2,  "Melanoma Warning Signs"));
        phonelocations.add(new infoHelper(gradient4, "Melanoma Stages"));
        phonelocations.add(new infoHelper(gradient2,  "Melanoma Treatment"));


        adapter = new melanomaInfo(phonelocations, this);
        phoneRecycler.setAdapter(adapter);

    }

    @Override
    public void onphoneListClick(int clickedItemIndex) {

        Intent mIntent;
        switch (clickedItemIndex) {
            case 0: //first item in Recycler view
                mIntent = new Intent(getActivity(), DiseaseInformationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("VAL", 1);
                mIntent.putExtras(bundle);
                startActivity(mIntent);
                break;
            case 1:
                mIntent = new Intent(getActivity(), DiseaseInformationActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putInt("VAL", 2);
                mIntent.putExtras(bundle2);
                startActivity(mIntent);
                break;
            case 2:
                mIntent = new Intent(getActivity(), DiseaseInformationActivity.class);
                Bundle bundle3 = new Bundle();
                bundle3.putInt("VAL", 3);
                mIntent.putExtras(bundle3);
                startActivity(mIntent);
                break;
            case 3:
                mIntent = new Intent(getActivity(), DiseaseInformationActivity.class);
                Bundle bundle4 = new Bundle();
                bundle4.putInt("VAL", 4);
                mIntent.putExtras(bundle4);
                startActivity(mIntent);
                break;
            case 4:
                mIntent = new Intent(getActivity(), DiseaseInformationActivity.class);
                Bundle bundle5 = new Bundle();
                bundle5.putInt("VAL", 5);
                mIntent.putExtras(bundle5);
                startActivity(mIntent);
                break;

        }
    }
}