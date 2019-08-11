package com.example.khajasangram.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khajasangram.Classes.DataToSQLite;
import com.example.khajasangram.Classes.RestaurantClass;
import com.example.khajasangram.Classes.Restaurant_SQLite;
import com.example.khajasangram.R;
import com.example.khajasangram.RestaurantDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RestaurantAdaptor extends RecyclerView.Adapter<RestaurantAdaptor.RestaurantViewHolder> {

    RecyclerView recyclerView;
    Context context;

    RestaurantDetails restaurantDetails;
    RestaurantClass restaurantClass;

    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> distance = new ArrayList<>();
    ArrayList<String> address = new ArrayList<>();
    ArrayList<String> contact = new ArrayList<>();
    ArrayList<String> id = new ArrayList<>();
    ArrayList<String> latitude = new ArrayList<>();
    ArrayList<String> longitude = new ArrayList<>();
    ArrayList<String> rating = new ArrayList<>();
    ArrayList<String> value = new ArrayList<>();
    DatabaseReference review, reference_img, reference;
    DataToSQLite obj;



    public RestaurantAdaptor(RecyclerView recyclerView, Context context, ArrayList<String> name, ArrayList<String> address,ArrayList<String> contact, ArrayList<String> id,ArrayList<String> distance, ArrayList<String> latitude, ArrayList<String> longitude ) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.name = name;
        this.address = address;
        this.id = id;
        this.contact = contact;
        this.distance = distance;
        this.latitude = latitude;
        this.longitude = longitude;
        //this.created_date = created_date;

        int j=0;

        restaurantDetails = new RestaurantDetails();
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.restaurant_layout,parent,false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {

        holder.r_name.setText(name.get(position));
        holder.r_address.setText(address.get(position));
        holder.r_contact.setText(contact.get(position));
        holder.r_distance.setText(distance.get(position));
        final Double[] rating_sum = {0.0};
        final Double[] rating_sum_final = new Double[1];
        review = FirebaseDatabase.getInstance().getReference("Rating").child(id.get(position));
        review.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Double stars = Double.parseDouble(snapshot.child("stars").getValue(String.class));
                    rating_sum[0] = rating_sum[0] + stars;
                }
                rating_sum_final[0] = rating_sum[0]/dataSnapshot.getChildrenCount();
                Float f = rating_sum_final[0].floatValue();
                rating.add(position,String.valueOf(f));
                //Toast.makeText(context, "value= "+rating, Toast.LENGTH_SHORT).show();
                holder.ratingBar.setRating(f);
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Float f = Float.valueOf(value.get(position));
        //holder.ratingBar.setRating(f);

        reference_img = FirebaseDatabase.getInstance().getReference("Images").child(id.get(position));
        reference_img.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String imgpath = dataSnapshot.child("imgpath").getValue(String.class);
                Picasso.get().load(imgpath).into(holder.imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        restaurantClass = new RestaurantClass(id.get(position), name.get(position),address.get(position),contact.get(position));
        //Toast.makeText(context, "name= "+restaurantClass.getR_name(), Toast.LENGTH_SHORT).show();

        holder.llayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //restaurantDetails.setrestaurant_details(name.get(position),address.get(position),contact.get(position), id.get(position), distance.get(position));
                //Intent i = new Intent(context,RestaurantDetails.class);
                //Toast.makeText(context, "name= "+restaurantClass.getR_name(), Toast.LENGTH_SHORT).show();
                //restaurantDetails.displayrestaurant_details(restaurantClass);

                Intent i= new Intent(view.getContext(),RestaurantDetails.class);
                Bundle extras = new Bundle();
                //view.getContext().startActivity(new
                  //      Intent(view.getContext(),RestaurantDetails.class));
                extras.putString("id",id.get(position));
                extras.putString("contact",contact.get(position));
                extras.putString("name",name.get(position));
                i.putExtras(extras);
                //i.putExtra("id",id.get(position));
                view.getContext().startActivity(i);


            }
        });

    }

    @Override
    public int getItemCount() { return name.size();    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder {

        TextView r_name;
        TextView r_address;
        TextView r_contact;
        TextView r_id;
        TextView r_distance;
        TextView r_created_date;
        LinearLayout llayout;
        RatingBar ratingBar;
        ImageView imageView;


        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);

            r_name = itemView.findViewById(R.id.res_name);
            r_address = itemView.findViewById(R.id.res_address);
            r_contact = itemView.findViewById(R.id.res_contact);
            r_distance = itemView.findViewById(R.id.distance);
            llayout = itemView.findViewById(R.id.linearlayout);
            ratingBar = itemView.findViewById(R.id.restaurant_rating_bar);
            imageView = itemView.findViewById(R.id.img_restaurant);

        }


    }

}
