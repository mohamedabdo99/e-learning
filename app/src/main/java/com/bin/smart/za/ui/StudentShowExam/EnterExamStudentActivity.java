package com.bin.smart.za.ui.StudentShowExam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import com.bin.smart.za.Adpter.AdapterQuiz.CategorySupjectaddExamAdapter;
import com.bin.smart.za.Model.SubjectStudentModel;
import com.bin.smart.za.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class EnterExamStudentActivity extends AppCompatActivity {
    private Toolbar toolPar_addExam;
    private GridView gridView;
    private CategorySupjectaddExamAdapter adapter;
    public static List<SubjectStudentModel> categoryList_enterExam = new ArrayList<>();
    public static int selected_supject_student =0;
    private FirebaseFirestore firebaseFirestore =FirebaseFirestore.getInstance();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_exam_student);

        toolPar_addExam = findViewById(R.id.toolPar_exam);
        toolPar_addExam.setTitle("Choose the Subject");
       setSupportActionBar(toolPar_addExam);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        gridView = findViewById(R.id.GridView_exam);
        adapter = new CategorySupjectaddExamAdapter(categoryList_enterExam);
        gridView.setAdapter(adapter);


        categoryList_enterExam.clear();
        firebaseFirestore.collection("QUIZ").document("categories")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();

                    if (doc.exists())
                    {
                        long counter = (long)doc.get("COUNT");

                        for (int i =1 ; i<=counter;i++)
                        {
                            String catName = doc.getString("CAT"+i+"_NAME");
                            String catId = doc.getString("CAT"+i+"_ID");

                            categoryList_enterExam.add(new SubjectStudentModel(catId,catName));
                        }
                        adapter.SetCategory(categoryList_enterExam);

                    }
                    else
                    {
                        Toast.makeText(EnterExamStudentActivity.this, "No Quiz Yet", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else
                {
                    Toast.makeText(EnterExamStudentActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("***********************************");
            }
        });





    }


}