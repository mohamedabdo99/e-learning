package com.bin.smart.za.ui.DoctorFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.bin.smart.za.R;
import com.bin.smart.za.ui.DoctorActivity.DetailsDoctorChooesSubjectActivity;


public class AddLecture_doctor_fragment extends Fragment {
    private Button bn_second_level_doctor,bn_third_level_doctor,bn_fourth_level_doctor;
    private RadioButton rb_first_doctor ,rb_second_doctor;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view =inflater.inflate(R.layout.fragment_add_lecture_doctor_fragment, container, false);

        //inflate button
        bn_second_level_doctor = (Button)view.findViewById(R.id.btn_secondLevel_doctor);
        bn_third_level_doctor = (Button)view.findViewById(R.id.btn_thirdLevel_doctor);
        bn_fourth_level_doctor = (Button)view.findViewById(R.id.btn_forthLevel_doctor);

        //inflate radio button
        rb_first_doctor = (RadioButton)view.findViewById(R.id.radio_firstTirm_dotor);
        rb_second_doctor = (RadioButton) view.findViewById(R.id.radio_secondTirm_doctor);

        bn_second_level_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent2 = new Intent(getActivity(), DetailsDoctorChooesSubjectActivity.class);


                if (rb_first_doctor.isChecked())
                {
                    intent2.putExtra("tirm","one");

                }
                else
                {
                    intent2.putExtra("tirm","two");

                }
                intent2.putExtra("level","one");
                getActivity().startActivity(intent2);

            }
        });
        bn_third_level_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity() , DetailsDoctorChooesSubjectActivity.class);


                if (rb_first_doctor.isChecked())
                {
                    intent.putExtra("tirm","one");

                }
                else
                {
                    intent.putExtra("tirm","two");

                }

                intent.putExtra("level","two");
                getActivity().startActivity(intent);


            }
        });
        bn_fourth_level_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity() , DetailsDoctorChooesSubjectActivity.class);

                if (rb_first_doctor.isChecked())
                {
                    intent.putExtra("tirm","one");

                }
                else
                {
                    intent.putExtra("tirm","two");

                }
                intent.putExtra("level","there");
                getActivity().startActivity(intent);

            }
        });


        return view;
    }


}