package com.bin.smart.za.Adpter.AdabterStudent;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bin.smart.za.R;
import com.bin.smart.za.ui.StudentActivity.LectureDetails;

import java.util.ArrayList;

public class AdapterSubjectName extends RecyclerView.Adapter<AdapterSubjectName.SubjectViewHolder> {

    ArrayList<String> arrayList ;
    String level = "";
    String tirm = "";


    public AdapterSubjectName(String level, String tirm) {
        this.level = level;
        this.tirm = tirm;
    }

    public void SetArrayList(ArrayList<String>arrayList)
    {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject_name,parent,false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, final int position)
    {
        holder.btn_subject.setText(arrayList.get(position));
        holder.btn_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),LectureDetails.class);
                intent.putExtra("subjectName",arrayList.get(position));
                intent.putExtra("level1",level);
                intent.putExtra("tirm1",tirm);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (arrayList != null)
        {
        return arrayList.size();
        }
        else
            {
                return 0;
            }
    }

    public class SubjectViewHolder extends RecyclerView.ViewHolder
    {
        Button btn_subject;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);

            btn_subject = itemView.findViewById(R.id.subject_one_item);
        }
    }
}
