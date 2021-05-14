package com.bin.smart.za.ui.StudentActivity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.bin.smart.za.Adpter.AdabterStudent.AdapterLecture;
import com.bin.smart.za.Model.NumberLecture;
import com.bin.smart.za.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class LectureDetails extends AppCompatActivity
{
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference ;
    private ArrayList<NumberLecture> arr = new ArrayList<>();
    private RecyclerView mrecyclerView;

    private Toolbar toolbar_lecture;
    private AdapterLecture adapterLecture;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_details);

        Intent intent = getIntent();
        String subject = intent.getStringExtra("subjectName");
        String level = intent.getStringExtra("level1");
        String tirm = intent.getStringExtra("tirm1");


//        System.out.println(tirm);
//        System.out.println(level);
       System.out.println(subject);

        //inflate Tool
        toolbar_lecture = (Toolbar) findViewById(R.id.toolpar_lecture);
        toolbar_lecture.setTitle("lectures");
        setSupportActionBar(toolbar_lecture);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        adapterLecture= new AdapterLecture();

        mrecyclerView = (RecyclerView)findViewById(R.id.recycle_lecture);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mrecyclerView.setItemAnimator(new DefaultItemAnimator());
        mrecyclerView.setAdapter(adapterLecture);

        collectionReference =firebaseFirestore.collection("years").document(level)
                .collection("tirm").document(tirm)
                .collection("Subject").document(subject)
                .collection("Lectures");
        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        for (int i =0 ; i < task.getResult().getDocuments().size() ; i++ )
                        {
                            String video =task.getResult().getDocuments().get(i).get("Video")+"";
                            String text =task.getResult().getDocuments().get(i).get("text")+"";
                            String pdf =task.getResult().getDocuments().get(i).get("pdf")+"";
                            String pdfName =task.getResult().getDocuments().get(i).get("pdfName")+"";
                            NumberLecture model = new NumberLecture(video,text,pdf,pdfName);
                            arr.add(model);

                        }
                      System.out.println("*********"+task.getResult().getDocuments().size());
                        adapterLecture.SetArryList(arr);


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.getMessage();
            }
        });



    }
}