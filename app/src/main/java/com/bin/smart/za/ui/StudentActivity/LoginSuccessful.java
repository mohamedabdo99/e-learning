package com.bin.smart.za.ui.StudentActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bin.smart.za.Model.Users;

import com.bin.smart.za.R;
import com.bin.smart.za.ui.StudentShowExam.EnterExamStudentActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import us.zoom.sdksample.ui.InitAuthSDKActivity;

public class LoginSuccessful extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar_home;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private Button bn_second_level,bn_third_level,bn_fourth_level;
    private RadioButton rb_first ,rb_second;
    private   TextView username;
    private TextView userDepartment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_successful);
        //inflate Tool
        toolbar_home = findViewById(R.id.toolPar_home);
        toolbar_home.setTitle(getString(R.string.toolparhome));
        setSupportActionBar(toolbar_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //inflate button
        bn_second_level = (Button)findViewById(R.id.btn_secondLevel_login);
        bn_third_level = (Button)findViewById(R.id.btn_thirdLevel_login);
        bn_fourth_level = (Button)findViewById(R.id.btn_forthLevel_login);

        //inflate radio button
        rb_first = (RadioButton)findViewById(R.id.radio_firstTirm);
        rb_second = (RadioButton) findViewById(R.id.radio_secondTirm);


        bn_second_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(LoginSuccessful.this
                        , DetailsActivity.class);
                Bundle b = new Bundle();

                if (rb_first.isChecked())
                {
                    b.putString("tirm","one");

                }
                else
                {
                    b.putString("tirm","two");

                }
                b.putString("level","one");
                intent.putExtras(b);
                startActivity(intent);

            }
        });
        bn_third_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(LoginSuccessful.this
                        , DetailsActivity.class);
                Bundle b = new Bundle();

                if (rb_first.isChecked())
                {
                    b.putString("tirm","one");

                }
                else
                {
                    b.putString("tirm","two");

                }
                b.putString("level","two");
                intent.putExtras(b);
                startActivity(intent);

            }
        });
        bn_fourth_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(LoginSuccessful.this
                        , DetailsActivity.class);
                Bundle b = new Bundle();

                if (rb_first.isChecked())
                {
                    b.putString("tirm","one");

                }
                else
                {
                    b.putString("tirm","two");

                }
                b.putString("level","there");
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        //drawer inflate
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar_home
                ,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        if(drawerLayout != null)
        {
            drawerLayout.addDrawerListener(toggle);
        }
        toggle.syncState();

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navdrawer_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView =(NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//getName


        View headerView= navigationView.getHeaderView(0);

        username = headerView.findViewById(R.id.text_user_profile);
        userDepartment = headerView.findViewById(R.id.text_department_profile);


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
//                          Log.d("TAG","*******"+username);
                      }
                      else
                      {
                          Toast.makeText(LoginSuccessful.this, "Error can not found userName", Toast.LENGTH_SHORT).show();
                  }
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {
                      Log.d("TAG","******* error"+error.getMessage());
                  }
              });





    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        int id  = menuItem.getItemId();
        if (id==R.id.menu_createMeeting)
        {
            Intent intent = new Intent(LoginSuccessful.this, StudentCourses.class);
            startActivity(intent);

        }
        else if (id==R.id.menu_joinMeeting)
        {
            Intent intent = new Intent(LoginSuccessful.this,ViewSubject.class);
            startActivity(intent);

        }
        else if (id==R.id.menu_chat)
        {
            Intent intent = new Intent(LoginSuccessful.this
                    , InitAuthSDKActivity.class);
            startActivity(intent);


        }
        else if (id==R.id.menu_exam)
        {
            Intent intent = new Intent(LoginSuccessful.this, EnterExamStudentActivity.class);
            startActivity(intent);


        }
        else if (id==R.id.menu_Logout)
        {
            Intent intent = new Intent(LoginSuccessful.this, MainActivity.class);
            startActivity(intent);
            LoginSuccessful.this.finish();


        }

        return false;
    }

    @Override
    public void onBackPressed() {
        
    }
}