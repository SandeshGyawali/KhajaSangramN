package com.example.khajasangram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuListview extends AppCompatActivity {
    LinearLayout menu_container, submenu_container;
    DatabaseReference reference_menu, reference_submenu;

    String intent_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_listview);

        menu_container = findViewById(R.id.menu_item_container);
        submenu_container = findViewById(R.id.submenu_item_container);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        intent_id = extras.getString("id");

        display_menulist();
    }

    private void display_menulist() {

        final int[] foodlist_index = {0};

        View view = LayoutInflater.from(this).inflate(R.layout.menu_item_list,null);

        TextView foodname = view.findViewById(R.id.food_name);
        LinearLayout menu_item = view.findViewById(R.id.menu_item);

        reference_menu = FirebaseDatabase.getInstance().getReference("Foodlist").child(intent_id);
        reference_menu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(int i=0;i<dataSnapshot.getChildrenCount();i++)
                {
                    String name = dataSnapshot.child("name"+ foodlist_index[0]).getValue(String.class);
                    foodlist_index[0]++;

                    foodname.setText(name);

                    menu_container.addView(view);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
