package com.cafesio.kitchen.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cafesio.kitchen.EditMenuActivity;
import com.cafesio.kitchen.R;
import com.cafesio.kitchen.constants.imgLinks;
import com.cafesio.kitchen.models.MenuModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skydoves.elasticviews.ElasticLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {
    Context context;
    ArrayList<MenuModel> menu;
    String dateStamp;

    public MenuAdapter(Context context, ArrayList<MenuModel> menu, String dateStamp) {
        this.context = context;
        this.menu = menu;
        this.dateStamp = dateStamp;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemName.setText(menu.get(position).getItemName());
        holder.itemCategory.setText(menu.get(position).getItemCategory());
        holder.itemPrice.setText("₹ " + menu.get(position).getItemPrice());
        holder.itemCount.setText("Available : " + menu.get(position).getItemCount());

        if (menu.get(position).getItemType().equals("veg")) {
            holder.itemType.setImageResource(R.drawable.veg);
        } else if (menu.get(position).getItemType().equals("egg")) {
            holder.itemType.setImageResource(R.drawable.egg);
        } else {
            holder.itemType.setImageResource(R.drawable.nonveg);
        }

        imgLinks links = new imgLinks();
        Glide.with(context)
                .load(links.link(menu.get(position).getItemName()))
                .centerCrop()
                .into(holder.itemImage);

        if (getItemCount() - 1 == holder.getAdapterPosition()) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 430);
            params.setMargins(40, 30, 40, 50);
            holder.card.setLayoutParams(params);
        }

        holder.card.setOnClickListener(v -> {
            showDialog(menu.get(position).getItemName(),
                    menu.get(position).getItemPrice(),
                    menu.get(position).getItemCount(),
                    menu.get(position).getItemImage(),
                    menu.get(position).getItemCategory(),
                    menu.get(position).getItemDescription(),
                    menu.get(position).getItemType(),
                    menu.get(position).getItemID(),
                    dateStamp);
        });
    }

    @Override
    public int getItemCount() {
        return menu.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemCategory, itemPrice, itemCount;
        ImageView itemType, itemImage;
        RelativeLayout card;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.itemName);
            itemCategory = itemView.findViewById(R.id.itemCategory);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemCount = itemView.findViewById(R.id.itemCount);
            itemType = itemView.findViewById(R.id.itemType);
            itemImage = itemView.findViewById(R.id.itemImage);
            card = itemView.findViewById(R.id.card);
        }
    }

    void showDialog(String itemNameStr,
                    String itemPriceStr,
                    String itemCountStr,
                    String itemImageStr,
                    String itemCategoryStr,
                    String itemDescriptionStr,
                    String itemTypeStr,
                    String itemIDstr,
                    String dateStamp) {

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.menu_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.order_item_bg);

        ElasticLayout editBtn, deleteBtn;
        TextView itemName, itemPrice, itemDescription;
        editBtn = dialog.findViewById(R.id.editBtn);
        deleteBtn = dialog.findViewById(R.id.deleteBtn);
        itemName = dialog.findViewById(R.id.itemName);
        itemDescription = dialog.findViewById(R.id.itemDescription);
        itemPrice = dialog.findViewById(R.id.itemPrice);

        itemName.setText(itemNameStr);
        itemPrice.setText("₹ " + itemPriceStr);
        itemDescription.setText(itemDescriptionStr);

        editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditMenuActivity.class);
            intent.putExtra("iName", itemNameStr);
            intent.putExtra("iPrice", itemPriceStr);
            intent.putExtra("iCount", itemCountStr);
            intent.putExtra("iImage", itemImageStr);
            intent.putExtra("iCategory", itemCategoryStr);
            intent.putExtra("iDescription", itemDescriptionStr);
            intent.putExtra("iType", itemTypeStr);
            intent.putExtra("iID", itemIDstr);
            intent.putExtra("dateStamp", dateStamp);
            context.startActivity(intent);
        });

        deleteBtn.setOnClickListener(v -> {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Menu").child("01092021").child(itemIDstr);
            reference.removeValue()
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());
        });
        dialog.show();
    }
}
