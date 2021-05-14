package com.bin.smart.za.Adpter.AdapterDoctorPost;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bin.smart.za.Model.ModelPost;
import com.bin.smart.za.Model.Users;
import com.bin.smart.za.R;
import com.bin.smart.za.ui.DoctorActivity.MyProfileActivity;
import com.bin.smart.za.ui.DoctorActivity.theProfileActivity;
import com.google.firebase.firestore.auth.User;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.MyHolder> {

    Context context;
    List<ModelPost> postList;

    public AdapterPost(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_posts,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final String uid= postList.get(position).getUid();
        String uName= postList.get(position).getuName();
        String pTitle= postList.get(position).getPTitle();
        String Ptimestamp= postList.get(position).getpTime();

        //convert timestamp to dd/mm/yy hh:mm am/pm
        Calendar cal= Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(Ptimestamp));
        String pTime = DateFormat.format("hh:mm aa",cal).toString();

        holder.uName.setText(uName);
        holder.pTitle.setText(pTitle);
        holder.pTimeTv.setText(pTime);



        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Comment", Toast.LENGTH_SHORT).show();
            }
        });

        holder.profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, theProfileActivity.class);
                intent.putExtra("uid", uid);
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView uName,pTimeTv,pTitle;
        LinearLayout profileLayout;
        Button commentBtn;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            uName=itemView.findViewById(R.id.unameTv);
            profileLayout=itemView.findViewById(R.id.profileLayout);
            commentBtn=itemView.findViewById(R.id.commentBtn);
            pTimeTv=itemView.findViewById(R.id.PTimeTv);
            pTitle=itemView.findViewById(R.id.pTitleTv);


        }
    }
}
