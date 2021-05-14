package com.bin.smart.za.Adpter.AdapterQuiz;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bin.smart.za.R;
import com.bin.smart.za.ui.StudentShowExam.Questions_showActivity;

public class SetsNumberOfQuizAdapter extends BaseAdapter {

    private int numberOfSets;

    public SetsNumberOfQuizAdapter(int numberOfSets) {
        this.numberOfSets = numberOfSets;
    }

    @Override
    public int getCount() {
        return numberOfSets;
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
    public View getView(final int i, View view, final ViewGroup viewGroup)
    {
        View converter;

        if (view == null)
        {
            converter = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.setsnumberof_quiz_item,viewGroup
                            ,false);

        }
        else
        {
            converter=view;
        }

        converter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewGroup.getContext(), Questions_showActivity.class);
                intent.putExtra("SETNO",i);
                viewGroup.getContext().startActivity(intent);
                ((Activity)viewGroup.getContext()).finish();

            }
        });
        ((TextView)converter.findViewById(R.id.num_ofSets_tv)).setText(String.valueOf(i+1));

        return converter;
    }
}
