package com.bin.smart.za.ui.StudentActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bin.smart.za.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private EditText ed_email;
    private Button btn_resetPassword;
    private Toolbar toolbar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        toolbar = findViewById(R.id.toolPar_resetPassword);
        toolbar.setTitle("Reset Password");

        ed_email = findViewById(R.id.editText_forgetPassword);
        btn_resetPassword = findViewById(R.id.buttonResetPassword);
        progressBar = findViewById(R.id.progressBar_resetPassword);

        mAuth = FirebaseAuth.getInstance();

        btn_resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            resetPassword();
            }
        });

    }

    private void resetPassword()
    {
        String email = ed_email.getText().toString().trim();

        if (TextUtils.isEmpty(email))
        {
         ed_email.setError("Enter your e-mail");
         ed_email.requestFocus();
         return;
        }


        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful())
                        {
                            Toast.makeText(ForgetPasswordActivity.this
                                    , "check your Email", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(ForgetPasswordActivity.this
                                    , "sorry , We do not have this email", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}