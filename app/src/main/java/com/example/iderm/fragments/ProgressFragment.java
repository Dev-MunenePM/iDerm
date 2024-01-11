package com.example.iderm.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.two.DetailActivity;
import com.example.two.Postinfo;
import com.example.two.PostsRecyclerAdapter;
import com.example.two.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static java.util.Objects.requireNonNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgressFragment extends Fragment implements PostsRecyclerAdapter.OnItemClickListener {
    FirebaseAuth auth;
    RecyclerView recyclerView;
    DatabaseReference myRef;
    ArrayList<Postinfo> postinfor;
    private PostsRecyclerAdapter recyclerAdapter;
    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_CREATOR = "Owner";
    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_AGENT = "Agent";
    public static final String EXTRA_TYPE="Type";
    FloatingActionButton uploadFab;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProgressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgressFragment newInstance(String param1, String param2) {
        ProgressFragment fragment = new ProgressFragment();
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
        return inflater.inflate(R.layout.fragment_history, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        // getSupportActionBar().setTitle("LostAndFound");
        /*recyclerView = getActivity().findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setHasFixedSize(true);*/
        recyclerView = getActivity().findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        myRef = FirebaseDatabase.getInstance().getReference();
        postinfor = new ArrayList<>();


        clearAll();
        getDataFromDataBase();

   /* uploadFab.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v) {
            startActivity(new Intent(getActivity(), UploadMainActivity.class));
        }
    });*/
    }

    private void getDataFromDataBase() {

        Query query = myRef.child("posts");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearAll();
                for (DataSnapshot npSnapshot : snapshot.getChildren()) {
                    Postinfo postinfo = new Postinfo();
                    postinfo.setImageUrl((npSnapshot.child("imageURL").getValue()).toString());
                    postinfo.setOwnerName((npSnapshot.child("ownerName").getValue()).toString());
//                    postinfo.setType(requireNonNull(npSnapshot.child("docType").getValue()).toString());
                  //  postinfo.setNumber(requireNonNull(npSnapshot.child("number").getValue()).toString());
                    postinfo.setTimePosted((npSnapshot.child("timePosted").getValue()).toString());
                    postinfo.setAgent(requireNonNull(npSnapshot.child("agent").getValue()).toString());

                    postinfor.add(postinfo);
                }
                recyclerAdapter = new PostsRecyclerAdapter(getActivity(), postinfor);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
                recyclerAdapter.setOnItemClickListener(ProgressFragment.this::onItemClick);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void clearAll() {

        if (postinfor != null) {
            postinfor.clear();
            if (recyclerAdapter != null) {
                recyclerAdapter.notifyDataSetChanged();
            }
        } else {
            postinfor = new ArrayList<>();
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent detailedIntent=new Intent(getActivity(), DetailActivity.class);
        Postinfo clickedItem=postinfor.get(position);
        detailedIntent.putExtra(EXTRA_URL,clickedItem.getImageUrl());
        detailedIntent.putExtra( EXTRA_CREATOR,clickedItem.getOwnerName());
        detailedIntent.putExtra( EXTRA_ID,clickedItem.getNumber());
        detailedIntent.putExtra( EXTRA_AGENT,clickedItem.getAgent());
        detailedIntent.putExtra( EXTRA_TYPE,clickedItem.getType());
        startActivity(detailedIntent);
    }
}
