package com.example.khajasangram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.khajasangram.Classes.RestaurantClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RestaurantDetails extends AppCompatActivity {

    String dcreated_date;

    ImageView imgview;
    TextView name,address,contact;

    DatabaseReference reference;
    DatabaseReference reference_img;
    String intent_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        imgview = findViewById(R.id.res_image);

        name = findViewById(R.id.res_name);
        address = findViewById(R.id.res_address);
        contact = findViewById(R.id.res_contact);

        Bundle bundle = getIntent().getExtras();
        intent_id = bundle.getString("id");
        //Toast.makeText(this, "id= "+intent_id, Toast.LENGTH_SHORT).show();

        getrestaurant_details();

    }

    public void getrestaurant_details()
    {
        reference = FirebaseDatabase.getInstance().getReference("Restaurants");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    String id = snapshot.child("id").getValue(String.class);
                   Toast.makeText(getApplicationContext(), "database id= "+id+" and pased id= "+intent_id, Toast.LENGTH_SHORT).show();

                    if(intent_id.equals(id))
                    {
                        //Toast.makeText(getApplicationContext(), "id= "+id, Toast.LENGTH_SHORT).show();
                        String dname = snapshot.child("name").getValue(String.class);
                        String daddress = snapshot.child("address").getValue(String.class);
                        String dcontact = snapshot.child("contact").getValue(String.class);

                        name.setText(dname);
                        address.setText(daddress);
                        contact.setText(dcontact);

                        setrestaurant_picture(id);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setrestaurant_picture(String id)
    {
        reference_img = FirebaseDatabase.getInstance().getReference("Images");
        reference_img.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if(intent_id.equals(id)) {
                        String id = snapshot.child("rid").getValue(String.class);
                        String imgpath = snapshot.child("imgpath").getValue(String.class);

                        Picasso.get().load(imgpath).into(imgview);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

   /* public void displayrestaurant_details(RestaurantClass restaurantClass)
    {
        Toast.makeText(this, "name= "+restaurantClass.getR_name(), Toast.LENGTH_SHORT).show();

        dcreated_date = restaurantClass.getR_created_date();
        reference = FirebaseDatabase.getInstance().getReference("Images");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    String id = snapshot.child("rid").getValue(String.class);
                    String imgpath = snapshot.child("imgpath").getValue(String.class);

                    if(id == restaurantClass.getR_id())
                    {
                        name.setText(restaurantClass.getR_name());
                        address.setText(restaurantClass.getR_address());
                        contact.setText(restaurantClass.getR_contact());

                        Picasso.get().load(imgpath).into(imgview);
                        break;

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/
   /* public void setrestaurant_details(String name, String address, String contact, String id, String distance)
    {
        this.dname = name;
        this.daddress = address;
        this.dcontact = contact;
        this.ddistance = distance;
        this.did = id;

        //Toast.makeText(this, "name= "+name, Toast.LENGTH_SHORT).show();

    }*/
}
