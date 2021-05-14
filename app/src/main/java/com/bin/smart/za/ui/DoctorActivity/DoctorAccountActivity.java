package com.bin.smart.za.ui.DoctorActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bin.smart.za.R;
import com.bin.smart.za.ui.DoctorFragment.AddExame_fragment;
import com.bin.smart.za.ui.DoctorFragment.AddLecture_doctor_fragment;
import com.bin.smart.za.ui.DoctorFragment.Chat_Doctor_fragment;
import com.bin.smart.za.ui.DoctorFragment.CreateMetting_fragmentDoctor;
import com.bin.smart.za.ui.DoctorFragment.HomeDoctorfragment;
import com.bin.smart.za.ui.LoginTypeActivity;
import com.bin.smart.za.ui.StudentActivity.LoginSuccessful;
import com.bin.smart.za.ui.StudentActivity.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DoctorAccountActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_account);
        bottomNavigationView= findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.framlayout,
               new HomeDoctorfragment()).commit();

        Intent i =getIntent();
        Toast.makeText(this, "DocotrPhone"+i.getStringExtra("phoneDoctor")
                ,Toast.LENGTH_SHORT).show();
        String phoneDoctor =i.getStringExtra("phoneDoctor");
        saveData("phoneDoctor",phoneDoctor);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
                {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId())
                    {
                        case R.id.menu_home:
                            selectedFragment = new HomeDoctorfragment();
                            break;
                        case R.id.menu_createMeetingDoctor:
                            selectedFragment = new CreateMetting_fragmentDoctor();
                            break;
                        case R.id.menu_chatDoctor:
                Intent intent= new Intent(DoctorAccountActivity.this, LoginTypeActivity.class);
                startActivity(intent);
                DoctorAccountActivity.this.finishAffinity();
                         break;
                        case R.id.add_lectureDoctor:
                            selectedFragment = new AddLecture_doctor_fragment();
                            break;
                        case R.id.add_examDoctor:
                            selectedFragment = new AddExame_fragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.framlayout
                    ,selectedFragment).commit();

                    return true;
                }
            };


    public void saveData(String key , String value) {
        sharedPreferences = DoctorAccountActivity.this.getSharedPreferences(
                "Garage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.open_profile){
            startActivity(new Intent(DoctorAccountActivity.this, MyProfileActivity.class));

        }

        return super.onOptionsItemSelected(item);
    }

}