package com.example.khajasangram.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.khajasangram.Classes.Restaurant_SQLite;
import com.example.khajasangram.R;
import com.example.khajasangram.RestaurantDetails;
import com.example.khajasangram.SQLite.Databasehelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Notification_fragment extends Fragment {

    Databasehelper databasehelper;
    LinearLayout searchby_name_place, search_layout, search_method, search_nearby,
                    nearby_alt_layout;
    LinearLayout name, place, nearby, container1;
    EditText search_txt;
    ImageView search_icon;
    TextView seekbar_txt, nearby_alt_txt;
    SeekBar seekbar;
    Button button;
    String radius;
    int int_radius=0;
    int value;

    ArrayList<String> namee;
    ArrayList<String> id;
    ArrayList<String> address;
    ArrayList<String> contact;
    ArrayList<String> latitude;
    ArrayList<String> longitude;
    ArrayList<String> ddistance;
    ArrayList<String> rating;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        radius = "200";
        databasehelper = new Databasehelper(getContext());

        View view = inflater.inflate(R.layout.notification_fragment,null);

        searchby_name_place = view.findViewById(R.id.search_by_name_place);
        search_layout = view.findViewById(R.id.search_layout);
        search_method = view.findViewById(R.id.search_method);
        search_nearby = view.findViewById(R.id.search_nearby);
        nearby_alt_layout = view.findViewById(R.id.nearby_alt_layout);

        seekbar_txt = view.findViewById(R.id.seekbartext);
        nearby_alt_txt = view.findViewById(R.id.nearby_alt_txt);

        container1 = view.findViewById(R.id.container);

        seekbar = view.findViewById(R.id.seekbar);
        button = view.findViewById(R.id.search_btn);

        searchby_name_place.setVisibility(View.INVISIBLE);
        search_layout.setVisibility(View.VISIBLE);
        search_method.setVisibility(View.INVISIBLE);
        search_nearby.setVisibility(View.INVISIBLE);

        name = view.findViewById(R.id.name);
        place = view.findViewById(R.id.place);
        nearby = view.findViewById(R.id.nearby);

        search_txt = view.findViewById(R.id.search_txt);
        search_icon = view.findViewById(R.id.search_icon);

        seekbar.setProgress(200); //suru ko default value
        seekbar.incrementProgressBy(100); //eti le increase hunxa
        seekbar.setMax(8000); //maximum value

        seekbar_txt.setText("Radius: "+radius);

        search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_method.setVisibility(View.VISIBLE);
                search_layout.setVisibility(View.INVISIBLE);
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                namee = new ArrayList<>();
                id = new ArrayList<>();
                address = new ArrayList<>();
                contact = new ArrayList<>();
                latitude = new ArrayList<>();
                longitude = new ArrayList<>();
                ddistance = new ArrayList<>();
                rating = new ArrayList<>();

                nearby.setBackgroundResource(R.drawable.rec_box_rounded_corner);
                place.setBackgroundResource(R.drawable.rec_box_rounded_corner);
                name.setBackgroundResource(R.drawable.menu_item_pressed);
                search_nearby.setVisibility(View.INVISIBLE);
                searchby_name_place.setVisibility(View.VISIBLE);
                nearby_alt_layout.setVisibility(View.INVISIBLE);


                container1.removeAllViews();

                search_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference reference, reference_image;

                        nearby_alt_layout.setVisibility(View.VISIBLE);

                        container1.removeAllViews();

                        String restaurant_name = search_txt.getText().toString();
                        nearby_alt_txt.setText("Result for: "+restaurant_name);

                        ArrayList<Restaurant_SQLite> info = databasehelper.twokmrestaurantname_list((restaurant_name));
                        Restaurant_SQLite restaurant_sqLite;

                        for(int i=0;i<info.size();i++)
                        {
                            restaurant_sqLite = info.get(i);
                            View view1 = LayoutInflater.from(getContext()).inflate(R.layout.restaurant_layout,null);
                            view1.setPadding(6,6,6,6);
                            TextView res_name,res_address,res_contact,distance;
                            RatingBar ratingBar;
                            ImageView img;

                            ratingBar = view1.findViewById(R.id.restaurant_rating_bar);
                            res_name = view1.findViewById(R.id.res_name);
                            res_address = view1.findViewById(R.id.res_address);
                            res_contact = view1.findViewById(R.id.res_contact);
                            distance = view1.findViewById(R.id.distance);
                            img = view1.findViewById(R.id.img_restaurant);

                            namee.add(restaurant_sqLite.name);
                            address.add(restaurant_sqLite.address);
                            id.add(restaurant_sqLite.id);
                            contact.add(restaurant_sqLite.contact);
                            latitude.add(restaurant_sqLite.latitude);
                            longitude.add(restaurant_sqLite.longitude);
                            ddistance.add(restaurant_sqLite.distance);

                            final Double[] rating_sum = {0.0};
                            final Double[] rating_sum_final = new Double[1];
                            reference = FirebaseDatabase.getInstance().getReference("Rating").child(restaurant_sqLite.id);
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   for(DataSnapshot snapshot : dataSnapshot.getChildren())
                                    {
                                        Double stars = Double.parseDouble(snapshot.child("stars").getValue(String.class));
                                        rating_sum[0] = rating_sum[0] + stars;
                                    }
                                    rating_sum_final[0] = rating_sum[0]/dataSnapshot.getChildrenCount();
                                    Float f = rating_sum_final[0].floatValue();
                                    ratingBar.setRating(f);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            //float f = Float.valueOf(restaurant_sqLite.rating);
                            //rating.add( f);
                            reference_image = FirebaseDatabase.getInstance().getReference("Images").child(restaurant_sqLite.id);
                            reference_image.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String imgpath = dataSnapshot.child("imgpath").getValue(String.class);
                                    Picasso.get().load(imgpath).into(img);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                            res_name.setText(restaurant_sqLite.name);
                            res_address.setText(restaurant_sqLite.address);
                            res_contact.setText(restaurant_sqLite.contact);
                            distance.setText(restaurant_sqLite.distance);

                            Restaurant_SQLite finalRestaurant_sqLite = restaurant_sqLite;
                            view1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(getContext(), RestaurantDetails.class);
                                    Bundle extras = new Bundle();
                                    //view.getContext().startActivity(new
                                    //      Intent(view.getContext(),RestaurantDetails.class));
                                    extras.putString("id", finalRestaurant_sqLite.id);
                                    extras.putString("contact",finalRestaurant_sqLite.contact);
                                    i.putExtras(extras);
                                    //i.putExtra("id",id.get(position));
                                    view.getContext().startActivity(i);
                                }
                            });

                            container1.addView(view1);


                        }


                    }
                });
            }
        });

        place.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                namee = new ArrayList<>();
                id = new ArrayList<>();
                address = new ArrayList<>();
                contact = new ArrayList<>();
                latitude = new ArrayList<>();
                longitude = new ArrayList<>();
                ddistance = new ArrayList<>();

                name.setBackgroundResource(R.drawable.rec_box_rounded_corner);
                nearby.setBackgroundResource(R.drawable.rec_box_rounded_corner);
                place.setBackgroundResource(R.drawable.menu_item_pressed);
                search_nearby.setVisibility(View.INVISIBLE);
                searchby_name_place.setVisibility(View.VISIBLE);
                nearby_alt_layout.setVisibility(View.INVISIBLE);

                container1.removeAllViews();

                search_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference reference, reference_image;

                        nearby_alt_layout.setVisibility(View.VISIBLE);

                        container1.removeAllViews();

                        String placename = search_txt.getText().toString();
                        nearby_alt_txt.setText("Result for: "+placename);


                        ArrayList<Restaurant_SQLite> info = databasehelper.twokmrestaurant_list((placename));
                        Restaurant_SQLite restaurant_sqLite;

                        for(int i=0;i<info.size();i++)
                        {
                            restaurant_sqLite = info.get(i);
                            View view1 = LayoutInflater.from(getContext()).inflate(R.layout.restaurant_layout,null);
                            view1.setPadding(6,6,6,6);

                            TextView res_name,res_address,res_contact,distance;
                            RatingBar ratingBar;
                            ImageView img;

                            ratingBar = view1.findViewById(R.id.restaurant_rating_bar);
                            res_name = view1.findViewById(R.id.res_name);
                            res_address = view1.findViewById(R.id.res_address);
                            res_contact = view1.findViewById(R.id.res_contact);
                            distance = view1.findViewById(R.id.distance);
                            img = view1.findViewById(R.id.img_restaurant);

                            final Double[] rating_sum = {0.0};
                            final Double[] rating_sum_final = new Double[1];
                            reference = FirebaseDatabase.getInstance().getReference("Rating").child(restaurant_sqLite.id);
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                                    {
                                        Double stars = Double.parseDouble(snapshot.child("stars").getValue(String.class));
                                        rating_sum[0] = rating_sum[0] + stars;
                                    }
                                    rating_sum_final[0] = rating_sum[0]/dataSnapshot.getChildrenCount();
                                    Float f = rating_sum_final[0].floatValue();
                                    ratingBar.setRating(f);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            namee.add(restaurant_sqLite.name);
                            address.add(restaurant_sqLite.address);
                            id.add(restaurant_sqLite.id);
                            contact.add(restaurant_sqLite.contact);
                            latitude.add(restaurant_sqLite.latitude);
                            longitude.add(restaurant_sqLite.longitude);
                            ddistance.add(restaurant_sqLite.distance);
//                            float f = Float.valueOf(restaurant_sqLite.rating);
                            //rating.add( f);
                            reference_image = FirebaseDatabase.getInstance().getReference("Images").child(restaurant_sqLite.id);
                            reference_image.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String imgpath = dataSnapshot.child("imgpath").getValue(String.class);
                                    Picasso.get().load(imgpath).into(img);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                            res_name.setText(restaurant_sqLite.name);
                            res_address.setText(restaurant_sqLite.address);
                            res_contact.setText(restaurant_sqLite.contact);
                            distance.setText(restaurant_sqLite.distance);

                            Restaurant_SQLite finalRestaurant_sqLite = restaurant_sqLite;
                            view1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(getContext(), RestaurantDetails.class);
                                    Bundle extras = new Bundle();
                                    //view.getContext().startActivity(new
                                    //      Intent(view.getContext(),RestaurantDetails.class));
                                    extras.putString("id", finalRestaurant_sqLite.id);
                                    extras.putString("contact",finalRestaurant_sqLite.contact);
                                    i.putExtras(extras);
                                    //i.putExtra("id",id.get(position));
                                    view.getContext().startActivity(i);
                                }
                            });

                            container1.addView(view1);


                        }


                    }
                });
            }
        });

        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setBackgroundResource(R.drawable.rec_box_rounded_corner);
                place.setBackgroundResource(R.drawable.rec_box_rounded_corner);
                nearby.setBackgroundResource(R.drawable.menu_item_pressed);
                search_nearby.setVisibility(View.VISIBLE);
                searchby_name_place.setVisibility(View.INVISIBLE);
                nearby_alt_layout.setVisibility(View.INVISIBLE);

                container1.removeAllViews();
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                radius = String.valueOf(i);
                seekbar_txt.setText("Radius: "+radius);
                value = (i/1000);
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
                container1.removeAllViews();
                display_nearby_restaurant();

            }


        });


        return view;

    }
    private void display_nearby_restaurant() {
        DatabaseReference reference, reference_image;
        Databasehelper databasehelper9 = new Databasehelper(getContext());

        namee = new ArrayList<>();
        id = new ArrayList<>();
        address = new ArrayList<>();
        contact = new ArrayList<>();
        latitude = new ArrayList<>();
        longitude = new ArrayList<>();
        ddistance = new ArrayList<>();
        rating = new ArrayList<>();

        //value is getting rounded-off to the upper limit always i.e 1.88 is stored as 1
        ArrayList<Restaurant_SQLite> info = databasehelper9.twokmrestaurant_list((value));
        Toast.makeText(getContext(), "value = "+value, Toast.LENGTH_SHORT).show();

        Restaurant_SQLite restaurant_sqLite;
        for(int i=0;i<info.size();i++)
        {
            restaurant_sqLite = info.get(i);
            View view1 = LayoutInflater.from(getContext()).inflate(R.layout.restaurant_layout,null);
            //view1.setPadding(6,6,6,6);

            TextView res_name,res_address,res_contact,distance;
            RatingBar ratingBar;
            ImageView img;

            ratingBar = view1.findViewById(R.id.restaurant_rating_bar);
            res_name = view1.findViewById(R.id.res_name);
            res_address = view1.findViewById(R.id.res_address);
            res_contact = view1.findViewById(R.id.res_contact);
            distance = view1.findViewById(R.id.distance);
            img = view1.findViewById(R.id.img_restaurant);


            final Double[] rating_sum = {0.0};
            final Double[] rating_sum_final = new Double[1];
            reference = FirebaseDatabase.getInstance().getReference("Rating").child(restaurant_sqLite.id);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        Double stars = Double.parseDouble(snapshot.child("stars").getValue(String.class));
                        rating_sum[0] = rating_sum[0] + stars;
                    }
                    rating_sum_final[0] = rating_sum[0]/dataSnapshot.getChildrenCount();
                    Float f = rating_sum_final[0].floatValue();
                    ratingBar.setRating(f);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            reference_image = FirebaseDatabase.getInstance().getReference("Images").child(restaurant_sqLite.id);
            reference_image.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String imgpath = dataSnapshot.child("imgpath").getValue(String.class);
                    Picasso.get().load(imgpath).into(img);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            res_name.setText(restaurant_sqLite.name);
            res_address.setText(restaurant_sqLite.address);
            res_contact.setText(restaurant_sqLite.contact);
            distance.setText(restaurant_sqLite.distance);
            //ratingBar.setRating(f);

            Restaurant_SQLite finalRestaurant_sqLite = restaurant_sqLite;
            view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(), RestaurantDetails.class);
                    Bundle extras = new Bundle();
                    //view.getContext().startActivity(new
                    //      Intent(view.getContext(),RestaurantDetails.class));
                    extras.putString("id", finalRestaurant_sqLite.id);
                    extras.putString("contact",finalRestaurant_sqLite.contact);
                    i.putExtras(extras);
                    //i.putExtra("id",id.get(position));
                    view.getContext().startActivity(i);
                }
            });
            container1.addView(view1);


        }


    }
}
