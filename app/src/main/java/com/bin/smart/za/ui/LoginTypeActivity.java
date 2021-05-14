package com.bin.smart.za.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bin.smart.za.R;
import com.bin.smart.za.ui.DoctorActivity.LoginDoctorActivity;
import com.bin.smart.za.ui.StudentActivity.MainActivity;

public class LoginTypeActivity extends AppCompatActivity
{
    private Button btn_Instructor,btn_Student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_type);

        btn_Instructor = findViewById(R.id.btn_loginTypeInstructor);
        btn_Student = findViewById(R.id.btn_loginTypeStudent);

        btn_Student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginTypeActivity.this
                        , MainActivity.class);
                startActivity(intent);
            }
        });

        btn_Instructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginTypeActivity.this, LoginDoctorActivity.class);
                startActivity(intent);
            }
        });
    }
}