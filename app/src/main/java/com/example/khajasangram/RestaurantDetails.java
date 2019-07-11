package com.example.khajasangram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.khajasangram.Classes.RestaurantClass;
import com.google.firebase.auth.FirebaseAuth;
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
    LinearLayout menu,location,phone;
    Toolbar toolbar;

    DatabaseReference reference;
    DatabaseReference userloc_ref;
    DatabaseReference restaurantloc_ref;
    DatabaseReference reference_img;

    String user_lat,user_lon;
    String res_lat,res_lon;

    FirebaseAuth mAuth;
    String intent_id, intent_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        imgview = findViewById(R.id.res_image);

        location = findViewById(R.id.res_location);

        name = findViewById(R.id.res_name);

        address = findViewById(R.id.res_address);
        //contact = findViewById(R.id.res_contact);
        phone = findViewById(R.id.res_contact);

        //Bundle bundle = getIntent().getExtras();
        //intent_id = bundle.getString("id");

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        intent_id = extras.getString("id");
        intent_contact = extras.getString("contact");

        mAuth = FirebaseAuth.getInstance();

        //Toast.makeText(this, "id= "+intent_id, Toast.LENGTH_SHORT).show();

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_VIEW);
                callIntent.setData(Uri.parse("tel:" + intent_contact));
                startActivity(callIntent);
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callgoogle_map();
            }
        });
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
                   //Toast.makeText(getApplicationContext(), "database id= "+id+" and pased id= "+intent_id, Toast.LENGTH_SHORT).show();

                    if(intent_id.equals(id))
                    {
                        //Toast.makeText(getApplicationContext(), "id= "+id, Toast.LENGTH_SHORT).show();
                        String dname = snapshot.child("name").getValue(String.class);
                        String daddress = snapshot.child("address").getValue(String.class);
                        res_lat = snapshot.child("latitude").getValue(String.class);
                        res_lon = snapshot.child("longitude").getValue(String.class);
                        String dcontact = snapshot.child("contact").getValue(String.class);

                        name.setText(dname);
                        address.setText(daddress);
                       // contact.setText(dcontact);

                        setrestaurant_picture();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setrestaurant_picture()
    {
        reference_img = FirebaseDatabase.getInstance().getReference("Images");
        reference_img.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.child("rid").getValue(String.class);

                    if(intent_id.equals(id)) {
                        Toast.makeText(getApplicationContext(), "database id= "+id+" and pased id= "+intent_id, Toast.LENGTH_SHORT).show();
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
    public void callgoogle_map()
    {
        String uid = mAuth.getCurrentUser().getUid();
        userloc_ref = FirebaseDatabase.getInstance().getReference("Users");
        userloc_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    String id = snapshot.child("id").getValue(String.class);
                    if(uid.equals(id))
                    {
                     user_lat = snapshot.child("latitude").getValue(String.class);
                     user_lon = snapshot.child("longitude").getValue(String.class);
                     break;
                    }
                    break;
                }
                String uri = "http://maps.google.com/maps?saddr=" + user_lat + "," + user_lon + "&daddr=" + res_lat + "," + res_lon;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
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
