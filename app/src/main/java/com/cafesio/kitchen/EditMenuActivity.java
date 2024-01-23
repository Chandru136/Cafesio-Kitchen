package com.cafesio.kitchen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.skydoves.elasticviews.ElasticLayout;

import java.util.HashMap;
import java.util.Map;

public class EditMenuActivity extends AppCompatActivity {

    EditText itemName, itemCount, itemImage, itemPrice, itemDescription, itemType, itemCategory;
    ImageButton submit;
    String dateStamp, itemID;
    ImageView backBtn;
    Map<String, Object> addItem = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);

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

        Bundle bundle = getIntent().getExtras();

        itemName.setText(bundle.getString("iName"));
        itemCategory.setText(bundle.getString("iCategory"));
        itemDescription.setText(bundle.getString("iDescription"));
        itemImage.setText(bundle.getString("iImage"));
        itemCount.setText(bundle.getString("iCount"));
        itemPrice.setText(bundle.getString("iPrice"));
        itemType.setText(bundle.getString("iType"));

        dateStamp = bundle.getString("dateStamp");
        itemID = bundle.getString("iID");

        submit.setOnClickListener(v -> {
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
                    .updateChildren(addItem)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show());
        });
    }
}
//hello commit