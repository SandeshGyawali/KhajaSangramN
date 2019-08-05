package com.example.khajasangram.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khajasangram.Classes.RestaurantClass;
import com.example.khajasangram.R;
import com.example.khajasangram.RestaurantDetails;

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

    public RestaurantAdaptor(RecyclerView recyclerView, Context context, ArrayList<String> name, ArrayList<String> address,ArrayList<String> contact, ArrayList<String> id,ArrayList<String> distance) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.name = name;
        this.address = address;
        this.id = id;
        this.contact = contact;
        this.distance = distance;
        //this.created_date = created_date;

        restaurantDetails = new RestaurantDetails();
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.restaurant_list_layout,parent,false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {

        holder.r_name.setText(name.get(position));
        holder.r_address.setText(address.get(position));
        holder.r_contact.setText(contact.get(position));
        holder.r_distance.setText(distance.get(position));

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


        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);

            r_name = itemView.findViewById(R.id.res_name);
            r_address = itemView.findViewById(R.id.res_address);
            r_contact = itemView.findViewById(R.id.res_contact);
            r_distance = itemView.findViewById(R.id.distance);
            llayout = itemView.findViewById(R.id.linearlayout);

        }
    }
}
