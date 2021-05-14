package com.bin.smart.za.ui.McqExamDoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bin.smart.za.Adpter.AdapterQuiz.SetNoDoctorAdapter;
import com.bin.smart.za.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.bin.smart.za.ui.DoctorFragment.AddExame_fragment.categoryList;
import static com.bin.smart.za.ui.DoctorFragment.AddExame_fragment.selected_ctegory_index;

public class SetsQuizDoctorActivity extends AppCompatActivity
{
    private RecyclerView recyclerViewSet;
    private Button btn_addSet;
    private FirebaseFirestore fireStore;
    private SetNoDoctorAdapter adapter ;
    private Dialog progressDialog;
    private Toolbar toolbar;
    public static List<String> SetId = new ArrayList<>();
    public static  int selected_set_Index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets_quiz_doctor);
        btn_addSet = findViewById(R.id.add_newSet_exam);
        recyclerViewSet = findViewById(R.id.recycle_exam_NSet);

        toolbar = findViewById(R.id.toolPr_doctor_Set);
        toolbar.setTitle("Quiz Details");

        progressDialog= new Dialog(SetsQuizDoctorActivity.this);
        progressDialog.setContentView(R.layout.progress_dialog_loding_sets_ofquiz);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);



        btn_addSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                addSetsQuiz();
            }
        });

        fireStore = FirebaseFirestore.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewSet.setLayoutManager(layoutManager);

        LoadSets();

    }

    private void addSetsQuiz()
    {
        progressDialog.show();
        final String current_id = categoryList.get(selected_ctegory_index).getId();
        final String current_counter = categoryList.get(selected_ctegory_index).getSetsCounter();

        Map<String,Object> setMAp = new ArrayMap<>();
        setMAp.put("COUNT","0");

        fireStore.collection("QUIZ").document(current_id).collection(current_counter)
                .document("QUESTION_LIST")
                .set(setMAp)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Map<String,Object> setDoc = new ArrayMap<>();
                        setDoc.put("COUNTER",String.valueOf(Integer.valueOf(current_counter) + 1 ));
                        setDoc.put("SET"+String.valueOf(SetId.size() + 1) + "_ID" ,current_counter);
                        setDoc.put("SETS" , SetId.size() + 1);

                        fireStore.collection("QUIZ").document(current_id)
                                .update(setDoc)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid)
                                    {
                                        Toast.makeText(SetsQuizDoctorActivity.this, "Quiz Add successful", Toast.LENGTH_SHORT).show();

                                        SetId.add(current_counter);
                                        categoryList.get(selected_ctegory_index)
                                                .setNoOfSets(String.valueOf(SetId.size()));
                                        categoryList.get(selected_ctegory_index)
                                                .setSetsCounter(String.valueOf(Integer.valueOf(current_counter) + 1 ));

                                         adapter.notifyItemInserted(SetId.size());
                                         progressDialog.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    private void LoadSets()
    {
        SetId.clear();
        progressDialog.show();

        fireStore.collection("QUIZ")
                .document(categoryList.get(selected_ctegory_index).getId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot)
                    {
                        long noOfSets = (long)documentSnapshot.get("SETS");

                        for ( int i = 1 ; i <= Long.valueOf(noOfSets); i++)
                        {
                            SetId.add(documentSnapshot.getString("SET"+String.valueOf(i)+"_ID"));

                        }
                        categoryList.get(selected_ctegory_index)
                                .setSetsCounter(documentSnapshot.getString("COUNTER"));
                        categoryList.get(selected_ctegory_index)
                                .setNoOfSets(String.valueOf(noOfSets));


                        adapter = new SetNoDoctorAdapter(SetId);
                        recyclerViewSet.setAdapter(adapter);

                        progressDialog.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(SetsQuizDoctorActivity.this, "Error ", Toast.LENGTH_SHORT).show();

                    }
                });




    }
}