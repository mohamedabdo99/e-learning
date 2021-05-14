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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bin.smart.za.Model.CodeModel;
import com.bin.smart.za.Model.Users;
import com.bin.smart.za.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private Toolbar toolbar_rgis;
    private EditText et_fullName,et_code,et_phoneNumber,et_password
            ,et_confirmPassword , et_email;
    private TextView et_university,et_faculty,et_department;
    private Button bn_signUp;
    private CheckBox checkBox;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");
    private Spinner spinner1,spinner2,spinner3;
    private String code,fullName,university,email,faculty,department,phoneNumber,password,confirmPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //inflate Tool
        toolbar_rgis = (Toolbar) findViewById(R.id.toolpar_rgister);
        toolbar_rgis.setTitle(getString(R.string.toolparregister));
        setSupportActionBar(toolbar_rgis);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        //inflate edit
        checkBox = findViewById(R.id.check_showPassword);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        et_fullName =(EditText)findViewById(R.id.et_fullName_register);
        et_code =(EditText)findViewById(R.id.et_code_register);
        et_university = (TextView) findViewById(R.id.et_unifersty_register);
        et_faculty = (TextView) findViewById(R.id.et_faculty_register);
        et_department = (TextView) findViewById(R.id.et_departMent_register);
        et_phoneNumber = (EditText)findViewById(R.id.et_phoneNumber_register);
        et_password = (EditText)findViewById(R.id.et_password_register);
        et_confirmPassword = (EditText)findViewById(R.id.et_confirm_password_register);
        et_email = (EditText)findViewById(R.id.et_email_register);

        spinner1 = findViewById(R.id.register_spinner1);
        spinner2 = findViewById(R.id.register_spinner2);
        spinner3 = findViewById(R.id.register_spinner3);

        bn_signUp = (Button) findViewById(R.id.btn_register);

        auth = FirebaseAuth.getInstance();

       bn_signUp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               fullName = et_fullName.getText().toString().trim();
               code = et_code.getText().toString().trim();
               email = et_email.getText().toString().trim();
               phoneNumber = et_phoneNumber.getText().toString().trim();
               password = et_password.getText().toString().trim();
               confirmPassword = et_confirmPassword.getText().toString().trim();

               if (TextUtils.isEmpty(fullName))
               {

                   et_fullName.setError(getString(R.string.PleaseEnterYourName));
                   et_fullName.requestFocus();
                   return;
               }
               else if (TextUtils.isEmpty(code))
               {
                   et_code.setError(getString(R.string.PleaseEnterYourcode));
                   et_code.requestFocus();
                   return;
               }
               else if (TextUtils.isEmpty(phoneNumber))
               {
                   et_phoneNumber.setError(getString(R.string.PleaseEnterYourphoneNumber));
                   et_phoneNumber.requestFocus();
                   return;
               }

               if (TextUtils.isEmpty(password))
               {
                   et_password.setError(getString(R.string.PleaseEnterYourpassword));
                   et_password.requestFocus();
                   return;
               }
               else if (TextUtils.isEmpty(confirmPassword))
               {
                   et_confirmPassword.setError(getString(R.string.PleaseEnterYourconfirmPassword));
                   et_password.requestFocus();
                   return;
               }
               else if (TextUtils.isEmpty(email))
               {
                   et_email.setError(getString(R.string.PleaseEnterYourEmail));
                   et_password.requestFocus();
                   return;
               }

               if (password.length()<8)
               {
                   et_password.setError(getString(R.string.passwordmustcontaina8characters));
                   et_password.requestFocus();
                   return;

               }

               if (password.equals(confirmPassword))
               {
                   makeSureCodeCode();
               }
               else
               {
                   et_confirmPassword.setError(getString(R.string.theyarenotthesame));
               }

           }

       });

       String[] universitySpinner = new String[]{"Zagazig University"};
       ArrayAdapter<String> adapterUniversity ;
        adapterUniversity = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,universitySpinner);
        adapterUniversity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapterUniversity);

        String[] facultySpinner = new String[]{"faculty of science"};
        ArrayAdapter<String> adapterFaculty ;
        adapterFaculty = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,facultySpinner);
        adapterFaculty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapterFaculty);

        String[] departmentSpinner = new String[]{"Math and computer science","computer science"
        ,"physics and computer science","chemistry and computer science"};
        ArrayAdapter<String> adapterDepartment ;
        adapterDepartment = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,departmentSpinner);
        adapterDepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapterDepartment);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                et_university.setText(parent.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                et_faculty.setText(parent.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                et_department.setText(parent.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    public void makeSureCodeCode()
    {
        final DatabaseReference Refreance;
        Refreance = FirebaseDatabase.getInstance().getReference();
        Refreance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.child("Code").child(code).exists())
                {
                    CodeModel codeModel = snapshot.child("Code")
                            .child(code).getValue(CodeModel.class);

                    if (codeModel.getCode().toString().equals(code+""))
                    {
                        progressDialog.setTitle(getString(R.string.wait));
                        progressDialog.setMessage(getString(R.string.waitwhencheck));
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(RegisterActivity.this
                                        , new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful())
                                        {
                                            Users users = new Users(fullName,code,spinner1.getSelectedItem().toString()
                                                    ,spinner2.getSelectedItem().toString()
                                                    ,spinner3.getSelectedItem().toString(),phoneNumber,email);
                                            FirebaseDatabase.getInstance().getReference("Users")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if (task.isSuccessful()) {
                                                        Intent intent = new Intent(RegisterActivity.this, LoginSuccessful.class);
                                                        startActivity(intent);
                                                        finish();

                                                        Toast.makeText(RegisterActivity.this, getString(R.string.UserhasbeenregisterSuccessful), Toast.LENGTH_SHORT).show();
                                                        progressDialog.dismiss();
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(RegisterActivity.this, getString(R.string.Failedtoregister), Toast.LENGTH_SHORT).show();
                                                        progressDialog.dismiss();
                                                    }
                                                }
                                            });
                                        }
                                        else
                                        {
                                            Toast.makeText(RegisterActivity.this, getString(R.string.Failedtocreateaccount), Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                            }
                        });
                    }
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "you are not registered " +
                                    "in the college"
                            , Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

    }
}