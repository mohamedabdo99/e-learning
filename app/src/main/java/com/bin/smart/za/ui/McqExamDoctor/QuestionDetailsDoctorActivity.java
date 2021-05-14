package com.bin.smart.za.ui.McqExamDoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bin.smart.za.Model.QuestionDoctorModel;
import com.bin.smart.za.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import static com.bin.smart.za.Adpter.AdapterQuiz.QuestionAdapterDoctor.questionDoctorModels;
import static com.bin.smart.za.ui.DoctorFragment.AddExame_fragment.categoryList;
import static com.bin.smart.za.ui.DoctorFragment.AddExame_fragment.selected_ctegory_index;
import static com.bin.smart.za.ui.McqExamDoctor.SetsQuizDoctorActivity.SetId;
import static com.bin.smart.za.ui.McqExamDoctor.SetsQuizDoctorActivity.selected_set_Index;

public class QuestionDetailsDoctorActivity extends AppCompatActivity
{
    private EditText Qu_et,Answer_etA,Answer_etB,Answer_etC,Answer_etD,et_correct_answer,etMark;
    private Button add_question;
    private Toolbar toolbar;
    private Dialog loadingDialog;
    private String question,answerA,answerB,answerC,answerD,correctAnswer,mark="1";
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_details_doctor);

        toolbar = findViewById(R.id.details_toolpar_doctorQu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Question "+String.valueOf(questionDoctorModels.size() + 1));

        loadingDialog= new Dialog(QuestionDetailsDoctorActivity.this);
        loadingDialog.setContentView(R.layout.progress_dialog_loding_sets_ofquiz);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT
                ,ViewGroup.LayoutParams.WRAP_CONTENT);

    Qu_et = findViewById(R.id.et_enterQuestion);
    Answer_etA = findViewById(R.id.et_enterAnswerA);
    Answer_etB = findViewById(R.id.et_enterAnswerB);
    Answer_etC = findViewById(R.id.et_enterAnswerC);
    Answer_etD = findViewById(R.id.et_enterAnswerD);
    et_correct_answer = findViewById(R.id.et_enterCorrectAnswer);
    add_question = findViewById(R.id.add_Question_btn);
    etMark = findViewById(R.id.et_enterDegreeAnswer);

    firebaseFirestore = FirebaseFirestore.getInstance();

    add_question.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            question = Qu_et.getText().toString();
            answerA = Answer_etA.getText().toString();
            answerB = Answer_etB.getText().toString();
            answerC = Answer_etC.getText().toString();
            answerD = Answer_etD.getText().toString();
            correctAnswer = et_correct_answer.getText().toString();
            mark = etMark.getText().toString();

            if (question.isEmpty())
            {
                Qu_et.setError("question Required");
                return;
            }
            if (mark.isEmpty())
            {
                etMark.setError("Mark Required");
                return;
            }
            if (answerA.isEmpty())
            {
                Answer_etA.setError("Answer A Required");
                return;
            }
            if (answerB.isEmpty())
            {
                Answer_etB.setError("Answer B Required");
                return;
            }
            if (answerC.isEmpty())
            {
                Answer_etC.setError("Answer C Required");
                return;
            }
            if (answerD.isEmpty())
            {
                Answer_etD.setError("Answer D Required");
                return;
            }
            if (correctAnswer.isEmpty())
            {
                et_correct_answer.setError("correct answer Required");
                return;
            }

            addNewQuestion();


        }
    });


    }

    private void addNewQuestion()
    {
        loadingDialog.show();

        final Map<String,Object> quesData = new ArrayMap<>();
        quesData.put("QUESTION",question);
        quesData.put("A",answerA);
        quesData.put("B",answerB);
        quesData.put("C",answerC);
        quesData.put("D",answerD);
        quesData.put("ANSWER",correctAnswer);
        quesData.put("MARK",mark);


     final String doc_Id =  firebaseFirestore.collection("QUIZ")
             .document(categoryList.get(selected_ctegory_index).getId())
                .collection(SetId.get(selected_set_Index)).document().getId();

     firebaseFirestore.collection("QUIZ").document(categoryList.get(selected_ctegory_index).getId())
             .collection(SetId.get(selected_set_Index))
             .document(doc_Id)
             .set(quesData)
             .addOnSuccessListener(new OnSuccessListener<Void>() {
                 @Override
                 public void onSuccess(Void aVoid)
                 {
                     Map<String,Object> quesDoc = new ArrayMap<>();
                     quesDoc.put("Q"+String.valueOf(questionDoctorModels.size() + 1) + "_ID",doc_Id);
                     quesDoc.put("COUNT",String.valueOf(questionDoctorModels.size() + 1));

                     firebaseFirestore.collection("QUIZ").document(categoryList.get(selected_ctegory_index).getId())
                             .collection(SetId.get(selected_set_Index)).document("QUESTION_LIST")
                             .update(quesDoc)
                             .addOnSuccessListener(new OnSuccessListener<Void>() {
                                 @Override
                                 public void onSuccess(Void aVoid)
                                 {
                                     Toast.makeText(QuestionDetailsDoctorActivity.this, "Question Add Successful"
                                             , Toast.LENGTH_SHORT).show();
                                     questionDoctorModels.add(new QuestionDoctorModel(
                                             doc_Id
                                             ,question
                                             ,answerA
                                             ,answerB
                                             ,answerC
                                             ,answerD
                                             ,correctAnswer
                                             ,Integer.valueOf(mark)
                                     ));

                                     loadingDialog.dismiss();
                                     QuestionDetailsDoctorActivity.this.finish();


                                 }
                             })
                             .addOnFailureListener(new OnFailureListener() {
                                 @Override
                                 public void onFailure(@NonNull Exception e)
                                 {
                                     Toast.makeText(QuestionDetailsDoctorActivity.this, "Error"+ e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        loadingDialog.dismiss();
                                 }
                             });

                 }
             })
             .addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {
                     Toast.makeText(QuestionDetailsDoctorActivity.this, "Error"+ e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                     loadingDialog.dismiss();
                 }
             });

    }
}