package com.bin.smart.za.ui.StudentShowExam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bin.smart.za.Model.Users;
import com.bin.smart.za.R;
import com.bin.smart.za.ui.DoctorActivity.AddLectureDoctorActivity;
import com.bin.smart.za.ui.StudentActivity.LoginSuccessful;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static com.bin.smart.za.ui.StudentShowExam.EnterExamStudentActivity.categoryList_enterExam;
import static com.bin.smart.za.ui.StudentShowExam.EnterExamStudentActivity.selected_supject_student;

public class ShowScoreActivity extends AppCompatActivity {
    private TextView result_tv;
    private Button done_btn;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String  send_score;
    private   TextView username;
    private TextView userDepartment, tv_quizName;
    private String Get_UserName , Get_userDepartment,quizName ;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_score);

        result_tv = findViewById(R.id.tv_total_score);
        done_btn = findViewById(R.id.button_done_quiz);
        tv_quizName = findViewById(R.id.student_Department_quizNme);

        username = findViewById(R.id.Student_name);
        userDepartment = findViewById(R.id.student_Department_score);

       quizName = loadData("QuizName");
       tv_quizName.setText(quizName);
       System.out.println("********************* = = ="+quizName);


        databaseReference = FirebaseDatabase.getInstance().getReference();

        String show_final = getIntent().getStringExtra("RESULT");
        result_tv.setText(show_final);
        send_score = result_tv.getText().toString();
        GetUserData();
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveScore();
                Intent intent =  new Intent(ShowScoreActivity.this, LoginSuccessful.class);
               startActivity(intent);
              ShowScoreActivity.this.finish();
            }
        });


    }

    private void SaveScore()
    {
        HashMap map =  new HashMap();
        map.put("Result",send_score);
        map.put("StudentName",Get_UserName);
        map.put("StudentDepartment",Get_userDepartment);
        map.put("subjectQuiz",quizName);

        databaseReference.child("Score").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(ShowScoreActivity.this, "You have successfully completed the quiz"
                            , Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ShowScoreActivity.this, "Someting went wrong"
                            , Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void GetUserData()
    {
        DatabaseReference reference =  FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.getValue()!=null)
                {
                    Users users = snapshot.getValue(Users.class);
                    username.setText(users.getFullName());
                    userDepartment.setText(users.getDepartment());

                    Get_UserName = username.getText().toString();
                    Get_userDepartment = userDepartment.getText().toString();
                }
                else
                {
                    Toast.makeText(ShowScoreActivity.this, "Error can not found userName", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG","******* error"+error.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed()
    {

    }
    public String loadData(String key)
    {
        sharedPreferences = ShowScoreActivity.this.getSharedPreferences(
                "Garage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String value = sharedPreferences.getString(key , "000");
        return value;

    }
}