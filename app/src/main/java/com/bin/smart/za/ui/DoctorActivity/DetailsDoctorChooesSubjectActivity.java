package com.bin.smart.za.ui.DoctorActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.bin.smart.za.Adpter.AdabterDoctor.AdapterSubjectDoctor;
import com.bin.smart.za.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DetailsDoctorChooesSubjectActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference ;
    private ArrayList arr = new ArrayList();
    private AdapterSubjectDoctor adapterSubjectDoctor;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_doctor_chooes_subject);

        Intent intent = getIntent();
        String tirm = intent.getStringExtra("tirm");
        String level = intent.getStringExtra("level");
        adapterSubjectDoctor = new AdapterSubjectDoctor(level,tirm);

        System.out.println("***************************"+tirm);
        System.out.println("***************************"+level);

        recyclerView = (RecyclerView)findViewById(R.id.recycle_doctor_subject);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterSubjectDoctor);

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
                        adapterSubjectDoctor.SetArrayListD(arr);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.getMessage();
            }
        });


    }
}