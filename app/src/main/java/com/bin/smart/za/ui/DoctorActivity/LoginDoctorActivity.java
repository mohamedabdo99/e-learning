package com.bin.smart.za.ui.DoctorActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bin.smart.za.Model.Admins;
import com.bin.smart.za.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginDoctorActivity extends AppCompatActivity
{
    private EditText email_login_doctor , password_login_doctor;
    private Button btn_signIn_doctor;
    private ProgressDialog progressDialog_login;
    private static String parentDbName ="Admins";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_doctor);

        progressDialog_login = new ProgressDialog(this);
        FirebaseApp.initializeApp(this);

        //inflate edit
        email_login_doctor = (EditText) findViewById(R.id.et_email_sigin_Doctor);
        password_login_doctor = (EditText)findViewById(R.id.et_password_sigin_Doctor);
        //btn
        btn_signIn_doctor = (Button)findViewById(R.id.btn_sigin_Docotr);

        btn_signIn_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginAccount();

            }
        });
    }

    private void LoginAccount() {

        final String phone = email_login_doctor.getText().toString().trim();
        String password = password_login_doctor.getText().toString().trim();

        if (TextUtils.isEmpty(phone))
        {
            email_login_doctor.setError(getString(R.string.Emailisrequired));
            email_login_doctor.requestFocus();
            return;
        }

        else if (TextUtils.isEmpty(password))
        {
            password_login_doctor.setError(getString(R.string.passwordisrequired));
            password_login_doctor.requestFocus();
            return;
        }

        else if (password.length()<8)
        {
            password_login_doctor.setError(getString(R.string.passwordmustcontaina8characters));
            password_login_doctor.requestFocus();
            return;
        }
        else {
            progressDialog_login.setTitle(getString(R.string.wait));
            progressDialog_login.setMessage(getString(R.string.waitwhencheck));
            progressDialog_login.setCanceledOnTouchOutside(false);
            progressDialog_login.show();

            SuccesLogin(phone,password);

        }

    }

    private void SuccesLogin(final String phone, final String password)
    {

        final DatabaseReference Refreance;
        Refreance = FirebaseDatabase.getInstance().getReference();
        Refreance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.child(parentDbName).child(phone).exists())
                {
                    Admins admins = snapshot.child(parentDbName)
                            .child(phone).getValue(Admins.class);
                    if (admins.getPhone().equals(phone))
                    {
                        if (admins.getPassword().equals(password))
                        {
                            if (parentDbName.equals("Admins"))
                            {
                                Toast.makeText(LoginDoctorActivity.this
                                        , "wellcom doctor", Toast.LENGTH_SHORT).show();
                                progressDialog_login.dismiss();

                                Intent my_Intent_admin = new Intent(LoginDoctorActivity.this,
                                        DoctorAccountActivity.class);
                                my_Intent_admin.putExtra("phoneDoctor",phone);
                                startActivity(my_Intent_admin);
                                LoginDoctorActivity.this.finish();

                            }
                        }
                    }
//                        Admins admins = snapshot.child(parentDbName)
//                                .child("Email1").getValue(Admins.class);

//                        if (admins.getEmail().equals(email)) {
//                            if (parentDbName.equals("Admins")) {
//
//                                if (admins.getPassword().equals("password"))
//                                {
//                                    Toast.makeText(LoginDoctorActivity.this
//                                            , "wellcom doctor", Toast.LENGTH_SHORT).show();
//                                progressDialog_login.dismiss();
//
//                                Intent my_Intent_admin = new Intent(LoginDoctorActivity.this,
//                                        DoctorAccountActivity.class);
//                                startActivity(my_Intent_admin);
//                            }
//                            }
//
//
//                        } else {
//                            progressDialog_login.dismiss();
//                            Toast.makeText(LoginDoctorActivity.this
//                                    , "Email is Error ", Toast.LENGTH_SHORT).show();
//                        }
//
//
                }

                else {
                    Toast.makeText(LoginDoctorActivity.this
                            , "you dont have account", Toast.LENGTH_SHORT).show();
                    progressDialog_login.dismiss();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void GetDetailsDoctor()
    {
        final DatabaseReference Refreance;
        Refreance = FirebaseDatabase.getInstance().getReference();
        Refreance.child("Admins")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        for (DataSnapshot sp : snapshot.getChildren())
                        {
                            String Key = sp.getKey();
//                             if ( Key == phone )
                             {
                                 Admins admins = sp.getValue(Admins.class);


                             }
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });

    }
    }
