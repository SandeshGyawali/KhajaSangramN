package com.example.khajasangram.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khajasangram.Adaptors.RestaurantAdaptor;
import com.example.khajasangram.Classes.Restaurant_SQLite;
import com.example.khajasangram.R;
import com.example.khajasangram.RestaurantlistActivity;
import com.example.khajasangram.SQLite.Databasehelper;

import java.util.ArrayList;

public class Search_fragment extends Fragment {
    RecyclerView recyclerView;
    LinearLayout linearLayout_2km;
    Databasehelper databasehelper;
    RestaurantAdaptor restaurantAdaptor;

    ArrayList<String> name;
    ArrayList<String> id;
    ArrayList<String> address;
    ArrayList<String> contact;
    ArrayList<String> latitude;
    ArrayList<String> longitude;
    ArrayList<String> distance;
    SeekBar seekBar;
    TextView seekbartxt;
    String radius;
    Button button;
    Double value;
    int value1;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        databasehelper = new Databasehelper(getContext());
        View rootview = inflater.inflate(R.layout.search_fragment, null);

        button = rootview.findViewById(R.id.search_btn);

        seekBar = rootview.findViewById(R.id.seekbar);
        seekbartxt = rootview.findViewById(R.id.seekbartext);

        seekBar.setProgress(200); //suru ko default value
        seekBar.incrementProgressBy(100); //eti le increase hunxa
        seekBar.setMax(8000); //maximum value

        radius = "200";

        seekbartxt.setText("Radius: "+radius+"m");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                radius = String.valueOf(i);
                String radius1= String.valueOf(i/1000);
                value = Double.valueOf(radius1);
                value1 = i/100;
                seekbartxt.setText("Radius: "+radius+"m");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                recyclerView = rootview.findViewById(R.id.recyclerview);
                name = new ArrayList<>();
                id = new ArrayList<>();
                address = new ArrayList<>();
                contact = new ArrayList<>();
                latitude = new ArrayList<>();
                longitude = new ArrayList<>();
                distance = new ArrayList<>();


                //value is getting rounded-off to the upper limit always i.e 1.88 is stored as 1
                ArrayList<Restaurant_SQLite> info = databasehelper.twokmrestaurant_list(value1);

                Restaurant_SQLite restaurant_sqLite;
                for(int i=0;i<info.size();i++)
                {
                    restaurant_sqLite = info.get(i);

                    name.add(restaurant_sqLite.name);
                    address.add(restaurant_sqLite.address);
                    id.add(restaurant_sqLite.id);
                    contact.add(restaurant_sqLite.contact);
                    latitude.add(restaurant_sqLite.latitude);
                    longitude.add(restaurant_sqLite.longitude);
                    distance.add(restaurant_sqLite.distance);
                }
                restaurantAdaptor = new RestaurantAdaptor(recyclerView,getContext(),name, address,contact,id,distance);
                recyclerView.setAdapter(restaurantAdaptor);

                recyclerView.setHasFixedSize(true);

                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);
            }
        });

        linearLayout_2km = rootview.findViewById(R.id.twokm);
  /*      recyclerView = rootview.findViewById(R.id.recyclerview);
        //LinearLayout linearLayout = rootview.findViewById(R.id.container);
        name = new ArrayList<>();
        id = new ArrayList<>();
        address = new ArrayList<>();
        contact = new ArrayList<>();
        latitude = new ArrayList<>();
        longitude = new ArrayList<>();
        distance = new ArrayList<>();

        ArrayList<Restaurant_SQLite> info = databasehelper.twokmrestaurant_list();
        linearLayout_2km.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //linearLayout.removeAllViews();

                Restaurant_SQLite restaurant_sqLite;
                for(int i=0;i<info.size();i++)
                {
                    restaurant_sqLite = info.get(i);

                    name.add(restaurant_sqLite.name);
                    address.add(restaurant_sqLite.address);
                    id.add(restaurant_sqLite.id);
                    contact.add(restaurant_sqLite.contact);
                    latitude.add(restaurant_sqLite.latitude);
                    longitude.add(restaurant_sqLite.longitude);
                    distance.add(restaurant_sqLite.distance);
                }
                restaurantAdaptor = new RestaurantAdaptor(recyclerView,getContext(),name, address,contact,id,distance);
                recyclerView.setAdapter(restaurantAdaptor);

                recyclerView.setHasFixedSize(true);
                // use a linear layout manager
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);

            }
        });*/



        return rootview;
    }
}
