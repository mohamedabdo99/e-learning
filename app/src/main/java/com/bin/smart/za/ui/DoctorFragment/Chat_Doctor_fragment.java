package com.bin.smart.za.ui.DoctorFragment;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bin.smart.za.Model.ModelGroupChat;
import com.bin.smart.za.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class Chat_Doctor_fragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
View view =inflater.inflate(R.layout.fragment_chat__doctor_fragment, container, false);



  //  Toolbar toolbar = view.findViewById(R.id.tool_main);


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
    }
