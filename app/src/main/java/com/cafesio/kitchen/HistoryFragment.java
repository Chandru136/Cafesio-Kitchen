package com.cafesio.kitchen;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cafesio.kitchen.models.HistoryModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.skydoves.elasticviews.ElasticLayout;

public class HistoryFragment extends Fragment {

    RecyclerView historyRecyclerView;
    FirestoreRecyclerAdapter adapter;
    TextView totalOrdersNumber, totalRevenueNumber, activeOrdersNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        historyRecyclerView = view.findViewById(R.id.historyRecyclerView);
        totalOrdersNumber = view.findViewById(R.id.totalOrdersNumber);
        totalRevenueNumber = view.findViewById(R.id.totalRevenueNumber);
        activeOrdersNumber = view.findViewById(R.id.activeOrdersNumber);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query query = db.collection("Orders").orderBy("timeStamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<HistoryModel> options1 = new FirestoreRecyclerOptions.Builder<HistoryModel>()
                .setQuery(query, HistoryModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<HistoryModel, ViewHolder>(options1) {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull HistoryModel model) {

                holder.orderID.setText(model.getOrderID());
                holder.totalItems.setText("Total Items : " + model.getNumberOfItems());
                holder.card.setOnClickListener(v -> showDialog(model.getDateStamp(), model.getOrderID(), model.getOrderStatus(), getContext()));

                switch (model.getOrderStatus()){
                    case "Active":
                        holder.status.setText(model.getOrderStatus());
                        holder.status.setTextColor(0xff0E8B42);
                        break;
                    case "Prepared":
                        holder.status.setText(model.getOrderStatus());
                        holder.status.setTextColor(0xff304FFE);
                        break;
                    case "Cancelled":
                        holder.status.setText(model.getOrderStatus());
                        holder.status.setTextColor(0xffB62828);
                        break;
                    default:
                        holder.status.setText(model.getOrderStatus());
                        holder.status.setTextColor(0xff848484);
                }

                holder.orderDate.setText(model.getDateStamp().substring(0, 2) + "." + model.getDateStamp().substring(2, 4) + "." + model.getDateStamp().substring(4));
                holder.orderPrice.setText("₹ " + model.getOrderPrice());

//                if (getItemCount() - 1 == holder.getAdapterPosition()) {
//                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120);
//                    params.setMargins(40, 40, 40, 50);
//                    holder.card.setLayoutParams(params);
//                }
            }
        };

        historyRecyclerView.setHasFixedSize(true);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        historyRecyclerView.setAdapter(adapter);

        db.collection("Orders")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int totalCount = queryDocumentSnapshots.size();
                    totalOrdersNumber.setText(String.valueOf(totalCount));
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "1 : " + e.getMessage(), Toast.LENGTH_SHORT).show());

        db.collection("Orders")
                .whereEqualTo("orderStatus", "Active")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int totalCount = queryDocumentSnapshots.size();
                    activeOrdersNumber.setText(String.valueOf(totalCount));
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "1 : " + e.getMessage(), Toast.LENGTH_SHORT).show());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Revenue").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalRevenueNumber.setText("₹ " + snapshot.child("Revenue").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        return view;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderID, totalItems, status, orderDate, orderPrice;
        RelativeLayout card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderID = itemView.findViewById(R.id.orderID);
            totalItems = itemView.findViewById(R.id.totalItems);
            status = itemView.findViewById(R.id.status);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderPrice = itemView.findViewById(R.id.orderPrice);
            card = itemView.findViewById(R.id.card);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    void showDialog(String dateStamp, String orderID, String orderStatus, Context context) {

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.history_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.order_item_bg);

        TextView orderid, orderstatus;
        TextView item1Name, item2Name, item3Name, item4Name, item5Name;
        TextView item1Count, item2Count, item3Count, item4Count, item5Count;
        RelativeLayout item1, item2, item3, item4, item5;
        View v1, v2, v3, v4, v5;
        ElasticLayout closeBtn;

        orderid = dialog.findViewById(R.id.orderID);
        orderstatus = dialog.findViewById(R.id.orderStatus);

        item1Name = dialog.findViewById(R.id.item1Name);
        item1Count = dialog.findViewById(R.id.item1Count);

        item2Name = dialog.findViewById(R.id.item2Name);
        item2Count = dialog.findViewById(R.id.item2Count);

        item3Name = dialog.findViewById(R.id.item3Name);
        item3Count = dialog.findViewById(R.id.item3Count);

        item4Name = dialog.findViewById(R.id.item4Name);
        item4Count = dialog.findViewById(R.id.item4Count);

        item5Name = dialog.findViewById(R.id.item5Name);
        item5Count = dialog.findViewById(R.id.item5Count);

        item1 = dialog.findViewById(R.id.item1);
        item2 = dialog.findViewById(R.id.item2);
        item3 = dialog.findViewById(R.id.item3);
        item4 = dialog.findViewById(R.id.item4);
        item5 = dialog.findViewById(R.id.item5);

        v1 = dialog.findViewById(R.id.v1);
        v2 = dialog.findViewById(R.id.v2);
        v3 = dialog.findViewById(R.id.v3);
        v4 = dialog.findViewById(R.id.v4);
        v5 = dialog.findViewById(R.id.v5);

        closeBtn = dialog.findViewById(R.id.closeBtn);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Orders").child(dateStamp).child(orderID);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    if (snapshot.child("1").exists()) {
                        item1.setVisibility(View.VISIBLE);
                        v1.setVisibility(View.VISIBLE);

                        item1Name.setText(snapshot.child("1").child("itemName").getValue().toString());
                        item1Count.setText("x " + snapshot.child("1").child("itemCount").getValue().toString());
                    } else {
                        item1.setVisibility(View.GONE);
                        v1.setVisibility(View.GONE);
                    }

                    if (snapshot.child("2").exists()) {
                        item2.setVisibility(View.VISIBLE);
                        v2.setVisibility(View.VISIBLE);

                        item2Name.setText(snapshot.child("2").child("itemName").getValue().toString());
                        item2Count.setText("x " + snapshot.child("2").child("itemCount").getValue().toString());
                    } else {
                        item2.setVisibility(View.GONE);
                        v2.setVisibility(View.GONE);
                    }

                    if (snapshot.child("3").exists()) {
                        item3.setVisibility(View.VISIBLE);
                        v3.setVisibility(View.VISIBLE);

                        item3Name.setText(snapshot.child("3").child("itemName").getValue().toString());
                        item3Count.setText("x " + snapshot.child("3").child("itemCount").getValue().toString());
                    } else {
                        item3.setVisibility(View.GONE);
                        v3.setVisibility(View.GONE);
                    }

                    if (snapshot.child("4").exists()) {
                        item4.setVisibility(View.VISIBLE);
                        v4.setVisibility(View.VISIBLE);

                        item4Name.setText(snapshot.child("4").child("itemName").getValue().toString());
                        item4Count.setText("x " + snapshot.child("4").child("itemCount").getValue().toString());
                    } else {
                        item4.setVisibility(View.GONE);
                        v4.setVisibility(View.GONE);
                    }

                    if (snapshot.child("5").exists()) {
                        item5.setVisibility(View.VISIBLE);
                        //v5.setVisibility(View.VISIBLE);

                        item5Name.setText(snapshot.child("5").child("itemName").getValue().toString());
                        item5Count.setText("x " + snapshot.child("5").child("itemCount").getValue().toString());
                    } else {
                        item5.setVisibility(View.GONE);
                        //v5.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        orderid.setText(orderID);

        switch (orderStatus){
            case "Active":
                orderstatus.setText(orderStatus);
                orderstatus.setTextColor(0xff0E8B42);
                break;
            case "Prepared":
                orderstatus.setText(orderStatus);
                orderstatus.setTextColor(0xff304FFE);
                break;
            case "Cancelled":
                orderstatus.setText(orderStatus);
                orderstatus.setTextColor(0xffB62828);
                break;
            default:
                orderstatus.setText(orderStatus);
                orderstatus.setTextColor(0xff848484);
        }

        closeBtn.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}