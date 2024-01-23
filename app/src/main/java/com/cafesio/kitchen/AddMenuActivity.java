package com.cafesio.kitchen;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skydoves.elasticviews.ElasticLayout;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class AddMenuActivity extends AppCompatActivity {

    EditText itemName, itemCount, itemImage, itemPrice, itemDescription, itemType, itemCategory;
    ImageButton submit;
    String dateStamp;
    ImageView backBtn;
    Map<String, Object> addItem = new HashMap<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        itemName = findViewById(R.id.itemName);
        itemCategory = findViewById(R.id.itemCategory);
        itemDescription = findViewById(R.id.itemDescription);
        itemImage = findViewById(R.id.itemImage);
        itemCount = findViewById(R.id.itemCount);
        itemPrice = findViewById(R.id.itemPrice);
        itemType = findViewById(R.id.itemType);
        backBtn = findViewById(R.id.backBtn);
        submit = findViewById(R.id.submit);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDateTime date = LocalDateTime.now();
        dateStamp = dateTimeFormatter.format(date);

        submit.setOnClickListener(v -> {

            String itemID = genID();

            addItem.put("itemName", itemName.getText().toString());
            addItem.put("itemImage", itemImage.getText().toString());
            addItem.put("itemCount", itemCount.getText().toString());
            addItem.put("itemPrice", itemPrice.getText().toString());
            addItem.put("itemType", itemType.getText().toString());
            addItem.put("itemID", itemID);
            addItem.put("itemCategory", itemCategory.getText().toString());
            addItem.put("itemDescription", itemDescription.getText().toString());

            reference.child("Menu")
                    .child("01092021")
                    .child(itemID)
                    .setValue(addItem)
                    .addOnSuccessListener(unused -> Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show());
        });
    }

    public String genID(){
        long tsLong = System.currentTimeMillis()/1000;
        return tsLong + "0";
    }
}