package com.bin.smart.za.ui.StudentActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.bin.smart.za.Adpter.AdabterStudent.AdapterSubjectName;
import com.bin.smart.za.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity
{
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference ;
    private ArrayList arr = new ArrayList();
    private AdapterSubjectName adapterSubjectName;
    private RecyclerView recyclerView;
    private Toolbar toolbar_details;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle intent = getIntent().getExtras();
        String tirm = intent.getString("tirm");
        String level = intent.getString("level");
        //inflate Tool
        toolbar_details = (Toolbar) findViewById(R.id.toolpar_details);
        toolbar_details.setTitle("subject");
        setSupportActionBar(toolbar_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapterSubjectName = new AdapterSubjectName(level,tirm);

        recyclerView = (RecyclerView)findViewById(R.id.recycle_details);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterSubjectName);

//        System.out.println(tirm);
//        System.out.println(level);

        collectionReference =firebaseFirestore.collection("years").document(level)
                .collection("tirm").document(tirm).collection("Subject");
        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        for (int i =0 ; i < task.getResult().getDocuments().size() ; i++ )
                        {
                            arr.add(task.getResult().getDocuments().get(i).get("name"));


                        }
                        adapterSubjectName.SetArrayList(arr);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.getMessage();
            }
        });


    }
}