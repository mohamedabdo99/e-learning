package com.bin.smart.za.ui.StudentShowExam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.bin.smart.za.Adpter.AdapterQuiz.SetNoDoctorAdapter;
import com.bin.smart.za.Adpter.AdapterQuiz.SetsNumberOfQuizAdapter;
import com.bin.smart.za.R;
import com.bin.smart.za.ui.DoctorActivity.AddLectureForSubjectDoctorDetails;
import com.bin.smart.za.ui.McqExamDoctor.SetsQuizDoctorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static com.bin.smart.za.ui.DoctorFragment.AddExame_fragment.categoryList;
import static com.bin.smart.za.ui.DoctorFragment.AddExame_fragment.selected_ctegory_index;
import static com.bin.smart.za.ui.McqExamDoctor.SetsQuizDoctorActivity.SetId;
import static com.bin.smart.za.ui.StudentShowExam.EnterExamStudentActivity.categoryList_enterExam;
import static com.bin.smart.za.ui.StudentShowExam.EnterExamStudentActivity.selected_supject_student;

public class SetsNumberOfQuiz extends AppCompatActivity {
    private Toolbar toolPar_set;
    private GridView gridView_setNumber_quiz;
    private SetsNumberOfQuizAdapter setsNumberOfQuizAdapter;
    private FirebaseFirestore firebaseFirestore;
    public static List<String> SetId_student = new ArrayList<>();
    private Dialog progressDialog;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets_number_of_quiz);

        String title = getIntent().getStringExtra("CategoryName");

        saveData("QuizName",title);
        toolPar_set =findViewById(R.id.toolPar_setNumberQuiz);
        setSupportActionBar(toolPar_set);
        toolPar_set.setTitle(title);

        firebaseFirestore = FirebaseFirestore.getInstance();

        gridView_setNumber_quiz = findViewById(R.id.grid_setNumberQuiz);


        progressDialog= new Dialog(SetsNumberOfQuiz.this);
        progressDialog.setContentView(R.layout.progress_dialog_loding_sets_ofquiz);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        progressDialog.show();

        LoadSets();



    }

    private void LoadSets()
    {
        SetId_student.clear();

        firebaseFirestore.collection("QUIZ")
                .document(categoryList_enterExam.get(selected_supject_student)
                        .getId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot)
                    {
                        long noOfSets = (long)documentSnapshot.get("SETS");

                        for ( int i = 1 ; i <= noOfSets ; i++)
                        {
                            SetId_student.add(documentSnapshot.getString("SET"+String.valueOf(i)+"_ID"));

                        }

                        setsNumberOfQuizAdapter = new SetsNumberOfQuizAdapter(SetId_student.size());
                        gridView_setNumber_quiz.setAdapter(setsNumberOfQuizAdapter);
                        progressDialog.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(SetsNumberOfQuiz.this, "Error ", Toast.LENGTH_SHORT).show();

                    }
                });







    }
    public void saveData(String key , String value)
    {
        sharedPreferences = SetsNumberOfQuiz.this.getSharedPreferences(
                "Garage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key , value);
        editor.commit();
    }
}