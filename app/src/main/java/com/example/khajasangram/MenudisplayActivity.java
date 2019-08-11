package com.example.khajasangram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.khajasangram.Adaptors.MenuItemAdaptor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenudisplayActivity extends AppCompatActivity {

    RecyclerView recyclerView, recyclerview_submenu_item;
    ArrayList<String> namelist;

    DatabaseReference reference;
    String intent_id, title;
    int foodlist_index = 0;
    MenuItemAdaptor adaptor;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menudisplay);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        intent_id = extras.getString("id");
        title = extras.getString("title");

        namelist = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerview);
        recyclerview_submenu_item = findViewById(R.id.subitem_list_container);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Foodlist").child(intent_id);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Toast.makeText(MenudisplayActivity.this, "count= "+dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                for(int i=0; i< dataSnapshot.getChildrenCount();i++)
                {
                    String name = dataSnapshot.child("name"+foodlist_index).getValue(String.class);
                    foodlist_index++;

                    namelist.add(name);
                    adaptor = new MenuItemAdaptor(MenudisplayActivity.this, namelist, intent_id, recyclerview_submenu_item);

                    recyclerView.setAdapter(adaptor);

                    recyclerView.setHasFixedSize(true);

                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(MenudisplayActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(mLayoutManager);

                }
                //Toast.makeText(MenudisplayActivity.this, "name"+foodlist_index+"= "+namelist, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
