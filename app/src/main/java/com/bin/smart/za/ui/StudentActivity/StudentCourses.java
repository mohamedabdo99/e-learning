package com.bin.smart.za.ui.StudentActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.bin.smart.za.Model.Member;
import com.bin.smart.za.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentCourses extends AppCompatActivity {
    Button button;
    CheckBox c1,c2,c3,c4,c5,c6;

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Member member;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        reference=database.getInstance().getReference().child("subjects");

        member=new Member();
        button=findViewById(R.id.btn_save);

        c1=findViewById(R.id.digital_image);
        c2=findViewById(R.id.Computer_Graphics);
        c3=findViewById(R.id.Artificial_Intelligence);
        c4=findViewById(R.id.Introduction_To_Partial_Differential_Equations);
        c5=findViewById(R.id.Functional_Analysis);
        c6=findViewById(R.id.Complex_Variable);

        final String s1="digital image";
        final String s2="Computer Graphics";
        final String s3="Artificial Intelligence";
        final String s4="Introduction To Partial Differential Equations";
        final String s5="Functional Analysis";
        final String s6="Complex Variable";

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    i=(int)dataSnapshot.getChildrenCount();}
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(c1.isChecked()){
                    member.setSubject1(s1);
                    reference.child(FirebaseAuth.getInstance().getCurrentUser()
                            .getUid())
                            .setValue(member);
            }
                else {

                }
                if(c2.isChecked()){
                    member.setSubject2(s2);
                    reference.child(FirebaseAuth.getInstance().getCurrentUser()
                            .getUid())
                            .setValue(member);
                }
                else {

                }
                if(c3.isChecked()){
                    member.setSubject3(s3);
                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(member);
                }
                else {

                }
                if(c4.isChecked()){
                    member.setSubject4(s4);
                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(member);
                }
                else {

                }
                if(c5.isChecked()){
                    member.setSubject5(s5);
                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(member);
                }
                else {

                }
                if(c6.isChecked()){
                    member.setSubject6(s6);
                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(member);
                }
                else
                 {
                     Toast.makeText(StudentCourses.this, "Checked the Subject", Toast.LENGTH_SHORT).show();

                }
                startActivity( new Intent(StudentCourses.this, ViewSubject.class));

            }
        });
    }
}