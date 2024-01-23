package com.cafesio.kitchen;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cafesio.kitchen.adapters.TakeawayAdapter;
import com.cafesio.kitchen.models.TakeawayModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TakeawayFragment extends Fragment {

    RecyclerView takeawayRecyclerView;
    String dateStamp;
    TakeawayAdapter adapter;
    RelativeLayout layout;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_takeaway, container, false);

        takeawayRecyclerView = view.findViewById(R.id.takeawayRecyclerView);
        layout = view.findViewById(R.id.layout);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDateTime date = LocalDateTime.now();
        dateStamp = dateTimeFormatter.format(date);

        takeawayRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Orders").child(dateStamp).orderByChild("orderStatus").equalTo("Prepared");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() == 0) {
                    layout.setVisibility(View.VISIBLE);
                } else {
                    layout.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        FirebaseRecyclerOptions<TakeawayModel> options =
                new FirebaseRecyclerOptions.Builder<TakeawayModel>()
                        .setQuery(query, TakeawayModel.class)
                        .build();

        adapter = new TakeawayAdapter(options, dateStamp, getContext());
        takeawayRecyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}