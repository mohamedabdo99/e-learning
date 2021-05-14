package com.bin.smart.za.Adpter.AdapterQuiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bin.smart.za.Model.SubjectStudentModel;
import com.bin.smart.za.R;
import com.bin.smart.za.ui.DoctorActivity.AddLectureForSubjectDoctorDetails;
import com.bin.smart.za.ui.StudentShowExam.EnterExamStudentActivity;
import com.bin.smart.za.ui.StudentShowExam.SetsNumberOfQuiz;

import java.util.List;

public class CategorySupjectaddExamAdapter extends BaseAdapter {

    private List<SubjectStudentModel> list;


    public CategorySupjectaddExamAdapter(List<SubjectStudentModel> list) {
        this.list = list;
    }

    public void SetCategory(List<SubjectStudentModel> list)
    {
        this.list = list;
        notifyDataSetChanged();
    }



    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        View view1;
        if (view == null)
        {
            view1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category_subject,viewGroup,false);

        }
        else
            {
                view1 = view;
            }

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnterExamStudentActivity.selected_supject_student = i;
                Intent intent = new Intent(viewGroup.getContext() , SetsNumberOfQuiz.class);
                intent.putExtra("CategoryName",list.get(i).getName());
                viewGroup.getContext().startActivity(intent);
                ((Activity)viewGroup.getContext()).finish();

            }
        });

        ((TextView)view1.findViewById(R.id.cat_Name_item)).setText(list.get(i).getName());

        return view1;

    }

}
