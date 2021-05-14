package com.bin.smart.za.ui.StudentShowExam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bin.smart.za.Adpter.AdapterQuiz.QuestionAdapterDoctor;
import com.bin.smart.za.Model.QuestionDoctorModel;
import com.bin.smart.za.Model.Questions;
import com.bin.smart.za.R;
import com.bin.smart.za.ui.McqExamDoctor.InsertQuestionDoctorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.bin.smart.za.ui.DoctorFragment.AddExame_fragment.categoryList;
import static com.bin.smart.za.ui.DoctorFragment.AddExame_fragment.selected_ctegory_index;
import static com.bin.smart.za.ui.McqExamDoctor.SetsQuizDoctorActivity.SetId;
import static com.bin.smart.za.ui.McqExamDoctor.SetsQuizDoctorActivity.selected_set_Index;
import static com.bin.smart.za.ui.StudentShowExam.EnterExamStudentActivity.categoryList_enterExam;
import static com.bin.smart.za.ui.StudentShowExam.EnterExamStudentActivity.selected_supject_student;
import static com.bin.smart.za.ui.StudentShowExam.SetsNumberOfQuiz.SetId_student;

public class Questions_showActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView qNumber,question,qTimer;
    private Button answer1,answer2,answer3,answer4;
    private List<Questions> questionsList_student;
    private int  questionsNumber;
    private  CountDownTimer countDownTimer;
    private FirebaseFirestore firebaseFirestore;
    private int results;
    private boolean flag = true;
    private int setNo_id;
    private Dialog progressDialog;
    private int index;
    private int currentIndex = 0;
    private Button btn_previous,btn_next,btn_button_submit;
    private  Questions questionsModel= new Questions();
    private Questions questionsIndex;
    private boolean isAnswerdOnc = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_show);
        firebaseFirestore = FirebaseFirestore.getInstance();
        questionsList_student = new ArrayList<>();

        setNo_id = getIntent().getIntExtra("SETNO",1);
        definition();
        getQuestionsList();
        startTimerDown();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        progressDialog= new Dialog(Questions_showActivity.this);
        progressDialog.setContentView(R.layout.progress_dialog_loding_sets_ofquiz);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        progressDialog.show();

        results =0;
    }



    //this m method for add a question to array
 private void getQuestionsList()
  {
        questionsList_student.clear();
        firebaseFirestore.collection("QUIZ")
                .document(categoryList_enterExam.get(selected_supject_student).getId())
                .collection(SetId_student.get(setNo_id))
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots)
                    {
                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                        for (QueryDocumentSnapshot doc :queryDocumentSnapshots)
                        {
                            docList.put(doc.getId(),doc);
                        }

                        QueryDocumentSnapshot questionListDoc =  docList.get("QUESTION_LIST");

                        final String Count = questionListDoc.getString("COUNT");

                        for (int i =0 ; i < Integer.valueOf(Count) ; i++)
                        {
                            String  qu_id = questionListDoc.getString("Q"+String.valueOf(i+1)+"_ID");
                            // Array random Question
                            Log.e("QId",qu_id+"");
                            QueryDocumentSnapshot quesDoc = docList.get(qu_id);

                            questionsList_student.add(new Questions(
                                    quesDoc.getString("QUESTION")
                                    ,
                                    quesDoc.getString("A")
                                    ,
                                    quesDoc.getString("B")
                                    ,
                                    quesDoc.getString("C")
                                    ,
                                    quesDoc.getString("D")
                                    ,
                                    quesDoc.getString("ANSWER")
                                    ,
                                    Integer.valueOf(quesDoc.getString("MARK"))
                            ));
                            // to sure the dupplected code
                        }
                        Collections.shuffle(questionsList_student);
                        SetQuestion(currentIndex);  //0
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(Questions_showActivity.this, "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
   }
    // this method for get a question
 private void SetQuestion(int currentIndexPosttion)
  {

//      List<Integer> questionsList = new ArrayList<>();
//      while (questionsList.size()<50)
//      {
//          int random = new Random().nextInt(500 + 1) + 1;
//
//          if (!questionsList.contains(random))
//          {
//              questionsList.add(random);
//          }
//          else
//          {
//              //Question already added to list
//          }
//
//
//      }
      question.setText(questionsList_student.get(currentIndexPosttion).getQuestion().trim());
      answer1.setText(questionsList_student.get(currentIndexPosttion).getAnswer1().trim());
      answer2.setText(questionsList_student.get(currentIndexPosttion).getAnswer2().trim());
      answer3.setText(questionsList_student.get(currentIndexPosttion).getAnswer3().trim());
      answer4.setText(questionsList_student.get(currentIndexPosttion).getAnswer4().trim());

    // to get the number of question and the total
    qNumber.setText(String.valueOf(1)+ "/" + String.valueOf(questionsList_student.size()));
//    startTimerDown();
    questionsNumber = 0;

  }
// this method for get timer down
private void startTimerDown()
    {
         countDownTimer = new CountDownTimer(60*30*1000,1000) {
            @Override
            public void onTick(long l)
            {
//                qTimer.setText(String.valueOf(l/1000));
                qTimer.setText(""+String.format("%d:%d ",
                        TimeUnit.MILLISECONDS.toMinutes(l),
                        TimeUnit.MILLISECONDS.toSeconds(l) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))));

            }

            @Override
            public void onFinish() {
                // this method when the timer there was zero change question
//                changeQuestion();
                Intent intent = new Intent(Questions_showActivity.this, ShowScoreActivity.class);
                intent.putExtra("RESULT",String.valueOf(results));
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Questions_showActivity.this.finish();

            }
        };
        countDownTimer.start();
    }

    @Override
  public void onClick(View view)
    {
         questionsIndex = questionsList_student.get(questionsNumber);
         questionsIndex.setAnswered(true);
        Log.i("onClick","onClickAfter = "+questionsIndex.getQuestion());
        String selectedAnswer;
        switch (view.getId())
        {
            case R.id.Answer1_button:
                selectedAnswer = answer1.getText().toString();
                // to send selected answer
                questionsIndex.setChooseAnswer(selectedAnswer);
                checkAnswer(selectedAnswer);
                answer1.setBackgroundTintList(ColorStateList
                        .valueOf(Color.parseColor("#212A6B")));
                Log.i("onClick","onClickBefore = "+questionsIndex.getAnswer1());
                break;
            case R.id.Answer2_button:
                selectedAnswer = answer2.getText().toString();
                // to send selected answer
                questionsIndex.setChooseAnswer(selectedAnswer);
                checkAnswer(selectedAnswer);
                answer2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#212A6B")));
                break;
            case R.id.Answer3_button:
                selectedAnswer = answer3.getText().toString();
                // to send selected answer
                questionsIndex.setChooseAnswer(selectedAnswer);
                checkAnswer(selectedAnswer);
                answer3.setBackgroundTintList(ColorStateList.valueOf(Color
                        .parseColor("#212A6B")));
                break;
            case R.id.Answer4_button:
                selectedAnswer = answer4.getText().toString();
                // to send selected answer
                questionsIndex.setChooseAnswer(selectedAnswer);
                checkAnswer(selectedAnswer);
                answer4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#212A6B")));
                break;



        }
//        switch (view.getId()){
//
//            case R.id.Answer1_button:
//                selectedAnswer=1;
//                break;
//            case R.id.Answer2_button:
//                selectedAnswer =2;
//                break;
//            case R.id.Answer3_button:
//                selectedAnswer = 3;
//                break;
//            case R.id.Answer4_button:
//                selectedAnswer=4;
//                break;
//
//            default:
//        }
    }

    private void getAnswersQuestion()
    {
       Log.i("getAnswersQuestion","questionsList_student = "
               +questionsList_student.size());
        if(questionsList_student.get(questionsNumber).isAnswered())
        {
         answer1.setEnabled(false);
         answer2.setEnabled(false);
         answer3.setEnabled(false);
         answer4.setEnabled(false);
        }
        else
        {
            answer1.setEnabled(true);
            answer2.setEnabled(true);
            answer3.setEnabled(true);
            answer4.setEnabled(true);
        }
    }

    private void checkAnswer(String selectedAnswer)
    {
//        countDownTimer.cancel();
        if (flag)
        {
            flag = false;
         if (selectedAnswer.equals(questionsList_student.get(questionsNumber).getCorrectAns()))
         {
//            // correct Answer
//            ((Button)viewGroup).setBackgroundTintList(ColorStateList
//                    .valueOf(Color.parseColor("#EFF1F6")));
         int mark1=questionsList_student.get(questionsNumber).getMark();
         results = results + mark1;
        }
        else
        {
//            //Wrong Answer
//            ((Button)viewGroup).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EFF1F6")));
//
//            switch (questionsList.get(questionsNumber).getCorrectAns())
//            {
//                case 1:
//                    answer1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EFF1F6")));
//                        break;
//                case 2:
//                    answer2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EFF1F6")));
//                    break;
//                case 3:
//                    answer3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EFF1F6")));
//                    break;
//                case 4:
//                    answer4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EFF1F6")));
//                    break;
        }

        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeQuestion();
            }
        }
        ,1000);


        }


    private void changeQuestion()
    {
        if (questionsNumber < questionsList_student.size() - 1)
        {
            Log.i("changeQuestion", "enter fun: "+ questionsNumber);
            flag =true;
            questionsNumber++;
            getAnswersQuestion();
            Log.i("changeQuestion", "++: : "+ questionsNumber);
            // Display another question
            playAnimation(question,0,0);
            playAnimation(answer1,0,1);
            playAnimation(answer2,0,2);
            playAnimation(answer3,0,3);
            playAnimation(answer4,0,4);
            qNumber.setText(String.valueOf(questionsNumber+1)+"/"+String.valueOf(questionsList_student.size()));
//            startTimerDown();
        }
        else
        {
            // go to Score activity
            Intent intent = new Intent(Questions_showActivity.this, ShowScoreActivity.class);
            intent.putExtra("RESULT",String.valueOf(results));
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Questions_showActivity.this.finish();

        }
    }

    private void playAnimation(final View view , final int value , final int viewNumber)
    {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator)
                    {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator)
                    {
                        if (value == 0)
                        {
                            switch (viewNumber)
                            {
                                case 0:
                                    ((TextView)view).setText(questionsList_student
                                            .get(questionsNumber).getQuestion());
                                    break;
                                case 1:
                                    ((Button)view).setText(questionsList_student
                                            .get(questionsNumber).getAnswer1());
                                    break;
                                case 2:
                                ((Button)view).setText(questionsList_student
                                        .get(questionsNumber).getAnswer2());
                                break;
                                case 3:
                                    ((Button)view).setText(questionsList_student
                                            .get(questionsNumber).getAnswer3());
                                    break;
                                case 4:
                                    ((Button)view).setText(questionsList_student
                                            .get(questionsNumber).getAnswer4());
                                    break;
                            }
                            if (viewNumber !=0)
                            {
                                ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EFF1F6")));
                            }
                            playAnimation(view,1,viewNumber);
                        }

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

    }

    @Override
    public void onBackPressed()
    {
        Toast.makeText(this, "لا يمكن الخروج من الامتحان ", Toast.LENGTH_SHORT).show();
    }

    public void definition()
    {
        qNumber= findViewById(R.id.number_OfQuestion);
        question = findViewById(R.id.Question_tv);
        qTimer = findViewById(R.id.counter_question);

        answer1 = findViewById(R.id.Answer1_button);
        answer2 = findViewById(R.id.Answer2_button);
        answer3 = findViewById(R.id.Answer3_button);
        answer4 = findViewById(R.id.Answer4_button);
        btn_previous = findViewById(R.id.button_previous);
        btn_next = findViewById(R.id.button_next);
        btn_button_submit = findViewById(R.id.button_submit);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (questionsNumber < questionsList_student.size()-1)
                {
                    Log.i("btn_next","before"+questionsNumber);
//                    currentIndex += 1;
                    Log.i("btn_next","after"+questionsNumber);
//                    SetQuestion(currentIndex); // questionsNumber =
                    changeQuestion();
                    getAnswersQuestion();

                }
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                // go back
                Log.i("btn_previous", "enter** : " + questionsNumber);

                if (questionsNumber >= 1)
                {
                        Log.i("btn_previous", "if : " + questionsNumber);
                        flag = true;
                        questionsNumber--;
                    getAnswersQuestion();
                    Log.i("btn_previous", "if : " + questionsNumber);
                        // Display another question
                        playAnimation(question, 0, 0);
                        playAnimation(answer1, 0, 1);
                        playAnimation(answer2, 0, 2);
                        playAnimation(answer3, 0, 3);
                        playAnimation(answer4, 0, 4);

                        qNumber.setText(String.valueOf(questionsNumber + 1) + "/" + String.valueOf(questionsList_student.size()));
//            startTimerDown();

                }
            }
        });

        btn_button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // go to Score activity
                Intent intent = new Intent(Questions_showActivity.this, ShowScoreActivity.class);
                intent.putExtra("RESULT",String.valueOf(results));
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Questions_showActivity.this.finish();
            }
        });

        answer1.setOnClickListener(this);
        answer2.setOnClickListener(this);
        answer3.setOnClickListener(this);
        answer4.setOnClickListener(this);

    }



}