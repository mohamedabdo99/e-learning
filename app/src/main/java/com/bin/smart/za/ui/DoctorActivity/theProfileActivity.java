package com.bin.smart.za.ui.DoctorActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bin.smart.za.Adpter.AdapterDoctorPost.AdapterPost;
import com.bin.smart.za.Model.ModelPost;
import com.bin.smart.za.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class theProfileActivity extends AppCompatActivity {
    RecyclerView postRecycler;

    List<ModelPost> postList;

    AdapterPost adapterPost;
    String uid;
    FirebaseAuth firebaseAuth;
    ActionBar actionBar;
    TextView nameTv,emailTv;
    private SharedPreferences sharedPreferences;
    String uidDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_profile);
        postRecycler=findViewById(R.id.recyclerview_post);
        firebaseAuth= FirebaseAuth.getInstance();
        nameTv=findViewById(R.id.user_name);
        emailTv=findViewById(R.id.user_email);

        uidDoctor= loadData("phoneDoctor");

        //    actionBar = getSupportActionBar();
        //     actionBar.setTitle("Profile");
//        actionBar.setDisplayShowHomeEnabled(true);
        //   actionBar.setDisplayHomeAsUpEnabled(true);




        Query query= FirebaseDatabase.getInstance().getReference("Users")
                .orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    String name =""+ds.child("name").getValue();
                    String email =""+ds.child("email").getValue();

                    nameTv.setText(name);
                    emailTv.setText(email);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        postList=new ArrayList<>();
        checkUser();
        loadHisPosts();

        postList=new ArrayList<>();
    }
    private void loadHisPosts() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        //show posts new first
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        postRecycler.setLayoutManager(layoutManager);
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Posts");
        Query query=ref.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    ModelPost myPosts=ds.getValue(ModelPost.class);

                    postList.add(myPosts);

                    adapterPost=new AdapterPost(theProfileActivity.this,postList);
                    postRecycler.setAdapter(adapterPost);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(theProfileActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){

        }

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();;
        return super.onSupportNavigateUp();
    }
    public String loadData(String key)
    {
        sharedPreferences =theProfileActivity.this.getSharedPreferences(
                "Garage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String value = sharedPreferences.getString(key , "000");
        return value;

    }
}