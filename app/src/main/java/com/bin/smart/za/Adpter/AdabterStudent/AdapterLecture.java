package com.bin.smart.za.Adpter.AdabterStudent;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bin.smart.za.Model.NumberLecture;
import com.bin.smart.za.R;
import com.bin.smart.za.ui.StudentActivity.VideoAndTextDetails;

import java.util.ArrayList;


public class AdapterLecture extends RecyclerView.Adapter<AdapterLecture.ViewHolder> {
    ArrayList<NumberLecture> arrayList;



    public void SetArryList(ArrayList<NumberLecture>arrayList)
    {
        this.arrayList=arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject_name
                ,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
    {
        holder.btn_lecture.setText("lecture"+(position+1)) ;

        holder.btn_lecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NumberLecture numberLecture = new NumberLecture();
            Intent intent = new Intent(view.getContext(), VideoAndTextDetails.class);
            intent.putExtra("url",arrayList.get(position).getVideo());
            intent.putExtra("text_url",arrayList.get(position).getText());
            intent.putExtra("uri_pdf",arrayList.get(position).getPdf());
            view.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        if (arrayList !=null)
        {
            return arrayList.size();
        }
        else
        {
            return 0;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        Button btn_lecture;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_lecture = itemView.findViewById(R.id.subject_one_item);

        }
    }
}
