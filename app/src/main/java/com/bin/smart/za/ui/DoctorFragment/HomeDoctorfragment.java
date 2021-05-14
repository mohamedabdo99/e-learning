package com.bin.smart.za.ui.DoctorFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bin.smart.za.Adpter.AdapterDoctorPost.AdapterPost;
import com.bin.smart.za.Model.ModelPost;
import com.bin.smart.za.R;
import com.bin.smart.za.ui.DoctorActivity.AddLectureDoctorActivity;
import com.bin.smart.za.ui.DoctorActivity.addPostActivity;
import com.bin.smart.za.ui.LoginTypeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeDoctorfragment extends Fragment
{
    FirebaseAuth firebaseAuth;
    ImageButton postBtn;
    RecyclerView recyclerView;
    List<ModelPost> postList;
    AdapterPost adapterPost;
    private ImageButton floatingActionButton;
    SharedPreferences sharedPreferences;
    String uidDoctor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_home_doctorfragment, container, false);


        firebaseAuth=FirebaseAuth.getInstance();

        uidDoctor= loadData("phoneDoctor");
        recyclerView=view.findViewById(R.id.postsRecycler);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        postBtn=view.findViewById(R.id.postBtn);

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), addPostActivity.class));

            }
        });

        postList=new ArrayList<>();
        loadPosts();


        // Inflate the layout for this fragment
        return view;


    }

    private void loadPosts() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    ModelPost modelPost=ds.getValue(ModelPost.class);
                    postList.add(modelPost);
                    adapterPost=new AdapterPost(getActivity(),postList);
                    recyclerView.setAdapter(adapterPost);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public String loadData(String key)
    {
        sharedPreferences =getActivity().getSharedPreferences(
                "Garage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String value = sharedPreferences.getString(key , "000");
        return value;

    }
}