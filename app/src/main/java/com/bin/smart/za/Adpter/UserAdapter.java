package com.bin.smart.za.Adpter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bin.smart.za.Model.Users;
import com.bin.smart.za.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<Users> users;
    public UserAdapter(Context context,List<Users> users){
        this.users=users;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_users,parent,false);
        return new UserAdapter.ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users us=users.get(position);
        holder.username.setText(us.getFullName());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username=  itemView.findViewById(R.id.item_username_adapter);
            cardView = itemView.findViewById(R.id.card_item_user_chat);


        }
    }
}
