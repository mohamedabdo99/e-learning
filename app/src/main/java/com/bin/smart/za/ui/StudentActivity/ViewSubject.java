package com.bin.smart.za.ui.StudentActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bin.smart.za.Model.Member;
import com.bin.smart.za.Model.Users;
import com.bin.smart.za.R;
import com.bin.smart.za.ui.StudentShowExam.ShowScoreActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ViewSubject extends AppCompatActivity {
    TextView tv1,tv2,tv3,tv4,tv5,tv6;
    private TextView studentName,StudentDepartment;
    private Toolbar toolbar_ViewSubject;
    DatabaseReference mRef;
    private ListView listView;
    FirebaseDatabase database;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        //inflate Tool
        toolbar_ViewSubject = (Toolbar) findViewById(R.id.toolPar_ViewSubject);
        toolbar_ViewSubject.setTitle("Student Courses");
        setSupportActionBar(toolbar_ViewSubject);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = findViewById(R.id.list_view_courses_view);

//        tv1=findViewById(R.id.text1);
//        tv2=findViewById(R.id.text2);
//        tv3=findViewById(R.id.text3);
//        tv4=findViewById(R.id.text4);
//        tv5=findViewById(R.id.text5);
//        tv6=findViewById(R.id.text6);
        studentName = findViewById(R.id.tv_studentName);
        StudentDepartment = findViewById(R.id.tv_studentDepartment);

        database=FirebaseDatabase.getInstance();
        mRef=database.getReference("subjects");
        GetUserData();

        mRef
         .child(FirebaseAuth.getInstance().getCurrentUser()
                 .getUid())
         .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Member member = dataSnapshot.getValue(Member.class);

//                tv1.setText(member.getSubject1());
//                tv2.setText(member.getSubject2());
//                tv3.setText(member.getSubject3());
//                tv4.setText(member.getSubject4());
//                tv5.setText(member.getSubject5());
//                tv6.setText(member.getSubject6());
                String [] listCourses = {member.getSubject1(),member.getSubject2(),
                        member.getSubject3(),member.getSubject4(),
                        member.getSubject5(),member.getSubject6()};
                if(listCourses.length !=0)
                {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewSubject.this,
                            android.R.layout.simple_list_item_1,android.R.id.text2,listCourses);
                    listView.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(ViewSubject.this
                            , "you do not have courses", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                    studentName.setText(users.getFullName());
                    StudentDepartment.setText(users.getDepartment());


                }
                else
                {
                    Toast.makeText(ViewSubject.this, "Error can not found userName", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG","******* error"+error.getMessage());
            }
        });
    }
}