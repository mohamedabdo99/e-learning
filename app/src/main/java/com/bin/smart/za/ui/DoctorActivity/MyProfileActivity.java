package com.bin.smart.za.ui.DoctorActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.bin.smart.za.Adpter.AdapterDoctorPost.AdapterPost;
import com.bin.smart.za.Model.Admins;
import com.bin.smart.za.Model.ModelPost;
import com.bin.smart.za.Model.Users;
import com.bin.smart.za.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyProfileActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView postRecycler;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseuser;
    FirebaseDatabase firebaseDatabase;
    List<ModelPost> postList;
    AdapterPost adapterPost;
    String uid;
    DatabaseReference databaseReference;
    TextView nameTv,emailTv;
    SharedPreferences saSharedPreferences;
    String DoctorDetails;
    String name,email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        DoctorDetails = loadData("phoneDoctor");
        Toast.makeText(this, "phoneDoctor = "+DoctorDetails
                , Toast.LENGTH_SHORT).show();
//        GetDetailsDoctor();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseuser = firebaseAuth.getCurrentUser();
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference = firebaseDatabase.getReference("Users");


        postRecycler = findViewById(R.id.recyclerview_post);
//        checkUser();

        postList = new ArrayList<>();
        nameTv = findViewById(R.id.user_name);
        emailTv = findViewById(R.id.user_email);

        final DatabaseReference Refreance;
        Refreance = FirebaseDatabase.getInstance().getReference("Admins")
                .child(DoctorDetails);
        Refreance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
//                        for (DataSnapshot sp : snapshot.getChildren())
//                        {
//                            name =snapshot.child("doctorName").getValue()+"";
//                            email = snapshot.child("Email").getValue()+"";
//                            uid = snapshot.child("uidDoctor").getValue()+"";

                HashMap<String ,String> fetchData =
                        (HashMap<String, String>) snapshot.getValue();
                name= fetchData.get("doctorName");
                email= fetchData.get("Email");
                uid = fetchData.get("uidDoctor");
                nameTv.setText(name);
                emailTv.setText(email);

//                        }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

      loadMyPost();

//        Query query = databaseReference.orderByChild("email").equalTo(firebaseuser.getEmail());
//        // Query query=databaseReference.orderByChild("fullName").equalTo(user.get));
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    String name = "" + ds.child("fullName").getValue();
//                    String email = "" + ds.child("email").getValue();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

 private void loadMyPost() {
            LinearLayoutManager layoutManager=new LinearLayoutManager(this);
            //show posts new first
            layoutManager.setStackFromEnd(true);
            layoutManager.setReverseLayout(true);
            postRecycler.setLayoutManager(layoutManager);
            DatabaseReference ref= FirebaseDatabase.getInstance()
                    .getReference("Posts");
//           Query query=ref.orderByChild("uid").equalTo(uid);
//             Log.i("loadMyPost","loadMyPost"+query);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    for (DataSnapshot sp:snapshot.getChildren())
                    {
                        if (sp.getKey().equals(uid))
                        {
                        ModelPost myPosts=sp.getValue(ModelPost.class);
                        postList.add(myPosts);
                        adapterPost=new AdapterPost(MyProfileActivity.this,postList);
                        postRecycler.setAdapter(adapterPost);
                        }


                    }

//                    postList.clear();
//                    for (DataSnapshot ds: snapshot.getChildren())
//                    {
//                        ModelPost myPosts=ds.getValue(ModelPost.class);
//                        postList.add(myPosts);
//                        adapterPost=new AdapterPost(MyProfileActivity.this,postList);
//                        postRecycler.setAdapter(adapterPost);
//                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MyProfileActivity.this, ""+error.getMessage()
                            , Toast.LENGTH_SHORT).show();

                }
            });
        }

//        private void checkUser() {
//            FirebaseUser user = firebaseAuth.getCurrentUser();
//            if (user != null){
//
//                uid=user.getUid();
//            }
//        }
    private void loadPost1()
    {
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
                    adapterPost=new AdapterPost(MyProfileActivity.this,postList);
                    postRecycler.setAdapter(adapterPost);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyProfileActivity.this, ""+error.getMessage()
                        , Toast.LENGTH_SHORT).show();

            }
        });

    }

    public String loadData(String key) {
        saSharedPreferences = MyProfileActivity.this.getSharedPreferences(
                "Garage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = saSharedPreferences.edit();
        String value = saSharedPreferences.getString(key, "000");
        return value;

    }
    }
