package com.bin.smart.za.ui.McqExamDoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bin.smart.za.Adpter.AdapterQuiz.QuestionAdapterDoctor;
import com.bin.smart.za.Model.QuestionDoctorModel;
import com.bin.smart.za.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.bin.smart.za.ui.DoctorFragment.AddExame_fragment.categoryList;
import static com.bin.smart.za.ui.DoctorFragment.AddExame_fragment.selected_ctegory_index;
import static com.bin.smart.za.ui.McqExamDoctor.SetsQuizDoctorActivity.SetId;
import static com.bin.smart.za.ui.McqExamDoctor.SetsQuizDoctorActivity.selected_set_Index;

public class InsertQuestionDoctorActivity extends AppCompatActivity
{
    private Toolbar toolbar_Question;
    private RecyclerView recyclerView_question;
    private Button btn_addQues;
    private FirebaseFirestore firebaseFirestore ;
    private List<QuestionDoctorModel> questionsList = new ArrayList<>();
    private Dialog progressDialog;
    private QuestionAdapterDoctor adapterDoctor ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_question_doctor);

        toolbar_Question = findViewById(R.id.toolPr_doctor_question);
        setSupportActionBar(toolbar_Question);
        getSupportActionBar().setTitle("Question");

        recyclerView_question = findViewById(R.id.recycle_question_doctor);
        btn_addQues = findViewById(R.id.button_InsertQuestionDoctor);

        firebaseFirestore = FirebaseFirestore.getInstance();

        progressDialog= new Dialog(InsertQuestionDoctorActivity.this);
        progressDialog.setContentView(R.layout.progress_dialog_loding_sets_ofquiz);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        btn_addQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InsertQuestionDoctorActivity.this,QuestionDetailsDoctorActivity.class);
                startActivity(intent);

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_question.setLayoutManager(layoutManager);

        LoadQuestion();


    }

    private void LoadQuestion()
    {
        questionsList.clear();

        progressDialog.show();

    firebaseFirestore.collection("QUIZ")
            .document(categoryList.get(selected_ctegory_index).getId())
                 .collection(SetId.get(selected_set_Index))
                .get()
                 .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                     @Override
                     public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                         Map<String, QueryDocumentSnapshot> doccList = new ArrayMap<>();

                         for (QueryDocumentSnapshot doc : queryDocumentSnapshots)
                         {
                             doccList.put(doc.getId(),doc);

                         }
                         QueryDocumentSnapshot questListDoc = doccList.get("QUESTION_LIST");
                         String count = questListDoc.getString("COUNT");

                         for (int i = 0  ; i < Integer.valueOf(count); i++)
                         {
                             String quiId = questListDoc.getString("Q" + String.valueOf(i+1) + "_ID");

                           if (null != doccList.get(quiId)) {

                               QueryDocumentSnapshot quesDoc = doccList.get(quiId);

//                               Map<String, Object> quesDoc = doccList.get(quiId).getData();

                               questionsList.add(new QuestionDoctorModel(
                                       quiId,
                                       String.valueOf(quesDoc.get("QUESTION"))
                                       ,
                                       String.valueOf(quesDoc.get("A"))
                                       ,
                                       String.valueOf(quesDoc.get("B"))
                                       ,
                                       String.valueOf(quesDoc.get("C"))
                                       ,
                                       String.valueOf(quesDoc.get("D"))
                                       ,
                                       String.valueOf(quesDoc.get("ANSWER"))
                                       ,
                                       Integer.valueOf(quesDoc.getString("MARK"))

                               ));


                           }
                         }

                         adapterDoctor = new QuestionAdapterDoctor(questionsList);
                         recyclerView_question.setAdapter(adapterDoctor);
                         progressDialog.dismiss();
                     }
                 })
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e)
                     {
                         Toast.makeText(InsertQuestionDoctorActivity.this, "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                     }
                 });






    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapterDoctor != null)
        {
            adapterDoctor.notifyDataSetChanged();
        }

    }
}