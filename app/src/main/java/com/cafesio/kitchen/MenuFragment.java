package com.cafesio.kitchen;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cafesio.kitchen.adapters.MenuAdapter;
import com.cafesio.kitchen.models.MenuModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MenuFragment extends Fragment {

    RecyclerView menuRecyclerView;
    String dateStamp;
    FloatingActionButton fabAdd;
    ArrayList<MenuModel> menuList;
    MenuAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        menuRecyclerView = view.findViewById(R.id.menuRecyclerView);
        fabAdd = view.findViewById(R.id.fabAdd);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDateTime date = LocalDateTime.now();
        dateStamp = dateTimeFormatter.format(date);

        reference.child("Menu").child("01092021").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menuList = new ArrayList<>();
                if (snapshot.exists()){
                    for(DataSnapshot snap: snapshot.getChildren())
                    {
                        MenuModel menu = snap.getValue(MenuModel.class);
                        menuList.add(menu);
                    }
                    adapter = new MenuAdapter(getActivity(), menuList, dateStamp);
                    menuRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    menuRecyclerView.setAdapter(adapter);
                }
                else{
                    Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddMenuActivity.class);
            startActivity(intent);
        });

        return view;
    }
}