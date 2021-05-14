package com.bin.smart.za.ui.StudentActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bin.smart.za.Model.Admins;
import com.bin.smart.za.R;
import com.bin.smart.za.ui.DoctorActivity.DoctorAccountActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
{
    private Toolbar toolbar_log;
    private EditText email_login , password_login;
    private Button btn_signin;
    private TextView tv_forgetPassword,tv_doNot_HaveAccount;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog_login;
    private static String parentDbName ="Admins";
    private CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog_login = new ProgressDialog(this);

        //inflate edit
        email_login = (EditText) findViewById(R.id.et_email_sigin);
        password_login = (EditText)findViewById(R.id.et_password_sigin);
        //btn
        btn_signin = (Button)findViewById(R.id.btn_sigin);
        //inflate TextView
        tv_forgetPassword = (TextView)findViewById(R.id.tv_forgetPassword_signIn);
        tv_doNot_HaveAccount =(TextView)findViewById(R.id.tv_donthaveaccount_signin);

        checkBox = findViewById(R.id.check_showPassword);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    password_login.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    password_login.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();

        tv_doNot_HaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent myIntent_login =new Intent (MainActivity.this, RegisterActivity.class);
                startActivity(myIntent_login);
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginAccount();

            }
        });

        tv_forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    public void LoginAccount()
    {
        final String email = email_login.getText().toString().trim();
        String password = password_login.getText().toString().trim();

        if (TextUtils.isEmpty(email))
        {
            email_login.setError(getString(R.string.Emailisrequired));
            email_login.requestFocus();
            return;
        }

       else if (TextUtils.isEmpty(password))
        {
            password_login.setError(getString(R.string.passwordisrequired));
            password_login.requestFocus();
            return;
        }

       else if (password.length()<8)
        {
            password_login.setError(getString(R.string.passwordmustcontaina8characters));
            password_login.requestFocus();
            return;
        }
       else
           {

        progressDialog_login.setTitle(getString(R.string.wait));
        progressDialog_login.setMessage(getString(R.string.waitwhencheck));
        progressDialog_login.setCanceledOnTouchOutside(false);
        progressDialog_login.show();

      mAuth.signInWithEmailAndPassword(email, password)
                           .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                   if (task.isSuccessful()) {
                                       // Sign in success, update UI with the signed-in user's information

                                       Intent intent = new Intent(MainActivity.this, LoginSuccessful.class);
                                       startActivity(intent);
                                       MainActivity.this.finish();
                                       Toast.makeText(MainActivity.this, getString(R.string.SigninSuccessful), Toast.LENGTH_SHORT).show();
                                       progressDialog_login.dismiss();

                                   } else {
                                       // If sign in fails, display a message to the user.
                                       Toast.makeText(MainActivity.this, getString(R.string.Signinfailed), Toast.LENGTH_SHORT).show();
                                       progressDialog_login.dismiss();
                                   }
                               }
                           });
        }
    }
}

