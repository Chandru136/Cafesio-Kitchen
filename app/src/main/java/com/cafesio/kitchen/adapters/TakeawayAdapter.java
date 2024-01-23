package com.cafesio.kitchen.adapters;

import android.app.Dialog;
import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cafesio.kitchen.OrdersFragment;
import com.cafesio.kitchen.R;
import com.cafesio.kitchen.TakeawayFragment;
import com.cafesio.kitchen.models.TakeawayModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.skydoves.elasticviews.ElasticLayout;

import java.util.HashMap;
import java.util.Map;

public class TakeawayAdapter extends FirebaseRecyclerAdapter<TakeawayModel, TakeawayAdapter.ViewHolder> {

    Context context;
    String dateStamp;
    int x =0;

    public TakeawayAdapter(@NonNull FirebaseRecyclerOptions<TakeawayModel> options, String dateStamp, Context context) {
        super(options);
        this.dateStamp = dateStamp;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull TakeawayModel model) {
        String orderID = model.getOrderID();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Orders").child(dateStamp).child(orderID);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (getItemCount() == 0) {
                    //Toast.makeText(context, "No Orders ready for takeaway", Toast.LENGTH_SHORT).show();
                } else {
                    if (snapshot.exists()) {
                        if (snapshot.child("1").exists()) {
                            holder.item1.setVisibility(View.VISIBLE);
                            holder.v1.setVisibility(View.VISIBLE);

                            holder.item1Name.setText(snapshot.child("1").child("itemName").getValue().toString());
                            holder.item1Count.setText("x " + snapshot.child("1").child("itemCount").getValue().toString());
                        } else {
                            holder.item1.setVisibility(View.GONE);
                            holder.v1.setVisibility(View.GONE);
                        }

                        if (snapshot.child("2").exists()) {
                            holder.item2.setVisibility(View.VISIBLE);
                            holder.v2.setVisibility(View.VISIBLE);

                            holder.item2Name.setText(snapshot.child("2").child("itemName").getValue().toString());
                            holder.item2Count.setText("x " + snapshot.child("2").child("itemCount").getValue().toString());
                        } else {
                            holder.item2.setVisibility(View.GONE);
                            holder.v2.setVisibility(View.GONE);
                        }

                        if (snapshot.child("3").exists()) {
                            holder.item3.setVisibility(View.VISIBLE);
                            holder.v3.setVisibility(View.VISIBLE);

                            holder.item3Name.setText(snapshot.child("3").child("itemName").getValue().toString());
                            holder.item3Count.setText("x " + snapshot.child("3").child("itemCount").getValue().toString());
                        } else {
                            holder.item3.setVisibility(View.GONE);
                            holder.v3.setVisibility(View.GONE);
                        }

                        if (snapshot.child("4").exists()) {
                            holder.item4.setVisibility(View.VISIBLE);
                            holder.v4.setVisibility(View.VISIBLE);

                            holder.item4Name.setText(snapshot.child("4").child("itemName").getValue().toString());
                            holder.item4Count.setText("x " + snapshot.child("4").child("itemCount").getValue().toString());
                        } else {
                            holder.item4.setVisibility(View.GONE);
                            holder.v4.setVisibility(View.GONE);
                        }

                        if (snapshot.child("5").exists()) {
                            holder.item5.setVisibility(View.VISIBLE);
                            holder.v5.setVisibility(View.VISIBLE);

                            holder.item5Name.setText(snapshot.child("5").child("itemName").getValue().toString());
                            holder.item5Count.setText("x " + snapshot.child("5").child("itemCount").getValue().toString());
                        } else {
                            holder.item5.setVisibility(View.GONE);
                            holder.v5.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(context, "No Orders ready for takeaway", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context.getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        if (getItemCount() - 1 == holder.getAdapterPosition()) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(40, 40, 40, 50);
            holder.card.setLayoutParams(params);
        }

        holder.orderID.setText("Order ID : " + orderID);

        holder.card.setOnClickListener(v -> showDialog(dateStamp, orderID, model.getUidNumber()));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderID;
        TextView item1Name, item2Name, item3Name, item4Name, item5Name;
        TextView item1Count, item2Count, item3Count, item4Count, item5Count;
        RelativeLayout item1, item2, item3, item4, item5;
        View v1, v2, v3, v4, v5;
        RelativeLayout card;

        public ViewHolder(View itemView) {
            super(itemView);

            orderID = itemView.findViewById(R.id.orderID);

            item1Name = itemView.findViewById(R.id.item1Name);
            item1Count = itemView.findViewById(R.id.item1Count);

            item2Name = itemView.findViewById(R.id.item2Name);
            item2Count = itemView.findViewById(R.id.item2Count);

            item3Name = itemView.findViewById(R.id.item3Name);
            item3Count = itemView.findViewById(R.id.item3Count);

            item4Name = itemView.findViewById(R.id.item4Name);
            item4Count = itemView.findViewById(R.id.item4Count);

            item5Name = itemView.findViewById(R.id.item5Name);
            item5Count = itemView.findViewById(R.id.item5Count);

            item1 = itemView.findViewById(R.id.item1);
            item2 = itemView.findViewById(R.id.item2);
            item3 = itemView.findViewById(R.id.item3);
            item4 = itemView.findViewById(R.id.item4);
            item5 = itemView.findViewById(R.id.item5);

            v1 = itemView.findViewById(R.id.v1);
            v2 = itemView.findViewById(R.id.v2);
            v3 = itemView.findViewById(R.id.v3);
            v4 = itemView.findViewById(R.id.v4);
            v5 = itemView.findViewById(R.id.v5);

            card = itemView.findViewById(R.id.card);
        }
    }

    void showDialog(String dateStamp, String orderID, String phoneNumber) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.takeaway_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.order_item_bg);

        TextView orderid, close;
        ElasticLayout completed;
        orderid = dialog.findViewById(R.id.orderID);
        completed = dialog.findViewById(R.id.completedBtn);
        close = dialog.findViewById(R.id.closeBtn);
        Map<String, Object> updateStore = new HashMap<>();
        updateStore.put("orderStatus", "Delivered");

        orderid.setText("Order ID : " + orderID);

        close.setOnClickListener(v -> dialog.dismiss());
        completed.setOnClickListener(view -> {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Orders").child(dateStamp).child(orderID);
            reference.child("orderStatus")
                    .setValue("Delivered")
                    .addOnSuccessListener(unused -> {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Orders")
                                .document(orderID)
                                .set(updateStore, SetOptions.merge())
                                .addOnSuccessListener(unused1 -> {

                                    db.collection("Orders")
                                            .document(orderID)
                                            .get()
                                            .addOnSuccessListener(documentSnapshot -> {

                                                String orderPrice = documentSnapshot.get("orderPrice").toString();

                                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Revenue");
                                                reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                        String revenue = snapshot.child("Revenue").getValue().toString();
                                                        reference1.child("Revenue")
                                                                .setValue(String.valueOf(Integer.parseInt(revenue) + Integer.parseInt(orderPrice)))
                                                                .addOnSuccessListener(unused2 -> {

                                                                    sendMessage(phoneNumber, orderID);
                                                                    dialog.dismiss();
                                                                })
                                                                .addOnFailureListener(e -> Log.d("asdfg2", e.getMessage()));
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });
                                            })
                                            .addOnFailureListener(e -> Log.d("asdfg3", e.getMessage()));
                                })
                                .addOnFailureListener(e -> Log.d("asdfg4", e.getMessage()));
                    })
                    .addOnFailureListener(e -> Log.d("asdfg5", e.getMessage()));
        });
        dialog.show();
    }

    private void sendMessage(String phoneNumber, String orderID) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, "Your Order with Order ID : " + orderID + " has been Delivered successfully." + "\n Enjoy your meal." + "\n\nregards,\nCafesio", null, null);
    }
}
