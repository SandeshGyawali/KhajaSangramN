package com.example.khajasangram.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.khajasangram.SQLite.Databasehelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataToSQLite {
    public Context context;
    ContentValues cv;
    Databasehelper db;
    ArrayList<String> id;
    ArrayList<String> name;
    ArrayList<String> new_id = new ArrayList<>();
    ArrayList<String> new_address = new ArrayList<>();
    ArrayList<String> new_contact = new ArrayList<>();
    ArrayList<String> new_latitude = new ArrayList<>();
    ArrayList<String> new_longitude = new ArrayList<>();
    ArrayList<String> new_distance = new ArrayList<>();
    ArrayList<String> new_name = new ArrayList<>();
    ArrayList<String> address;
    ArrayList<String> contact;
    ArrayList<String> distance;

    ArrayList<String> latitude;
    ArrayList<String> longitude;
    ArrayList<String> rating;
    int index;
    int index_tracker = 0;
    int geda=0;
    Databasehelper db1;


    public DataToSQLite(Context context) {
        this.context = context;
    }

    public void populate_twokmtable(Context context, ArrayList<String> id, ArrayList<String> name, ArrayList<String> address, ArrayList<String> contact, ArrayList<String> latitude, ArrayList<String> longitude, ArrayList<String> distance) {
        this.context = context;
        this.id = id;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;

        db1 = new Databasehelper(context);
        db1.delete_content_compare();
        findRestaurantWithRating();
        //see_ratingvalue();

        //db = new Databasehelper(context);


       /* cv = new ContentValues();
        int size = id.size();
        for(int i=0;i<size;i++)
        {
            cv.put("id",id.get(i));
            cv.put("name", name.get(i));
            cv.put("address", address.get(i));
            cv.put("contact", contact.get(i));
            cv.put("distance", distance.get(i));
            cv.put("latitude", latitude.get(i));
            cv.put("longitude", longitude.get(i));
        }*/
    }



    private void findRestaurantWithRating() {
        final int[] j = {0};
        //final int[] k = {0};
        DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference("Rating");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int i = 0; i < (id.size()); i++) {
                    //Toast.makeText(context,"id= "+id.get(i),Toast.LENGTH_SHORT).show();
                    if (dataSnapshot.hasChild(id.get(i))) {

                        //index_tracker.add(String.valueOf(k[0]+1));
                        new_id.add(j[0], id.get(i));
                        new_name.add(j[0], name.get(i));
                        new_address.add(j[0], address.get(i));
                        new_contact.add(j[0], contact.get(i));
                        new_latitude.add(j[0], latitude.get(i));
                        new_longitude.add(j[0], longitude.get(i));
                        //new_distance.add(j[0],distance.get(i));

                        j[0]++;
                    }
                    //k[0]++;

                }
                //Toast.makeText(context,"new_id= "+new_id,Toast.LENGTH_SHORT).show();
               set_ratingValue();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }




    public void set_ratingValue() {
        rating = new ArrayList<>();
        final Double[] rating_sum_final = new Double[1];
        DatabaseReference reference;
        for (int i = 0; i < new_id.size(); i++) {
            final Double[] rating_sum = {0.0};

            //Toast.makeText(context, "id= "+new_id.get(i),Toast.LENGTH_SHORT).show();
            index = i;
            reference = FirebaseDatabase.getInstance().getReference("Rating").child(new_id.get(i));
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Double stars = Double.parseDouble(snapshot.child("stars").getValue(String.class));
                        rating_sum[0] = rating_sum[0] + stars;
                    }
                    rating_sum_final[0] = rating_sum[0] / dataSnapshot.getChildrenCount();
                    Float f = rating_sum_final[0].floatValue();
                    //Toast.makeText(context, "rating= "+f,Toast.LENGTH_SHORT).show();

                    rating.add(String.valueOf(f));
                    //Toast.makeText(context,"rating= "+rating,Toast.LENGTH_SHORT).show();
                  /* index_tracker++;
                    if(index_tracker == new_id.size()) {*/
                    see_ratingvalue();
                    //}


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });

        }
        //see_ratingvalue();
    }
    public void see_ratingvalue() {

        //Toast.makeText(context, "see wala", Toast.LENGTH_SHORT).show();

        Databasehelper db;
        db = new Databasehelper(context);

        Long count = db.comparetable_rowcount();
        //if(count >= new_id.size())
        //{
          //  db.delete_content_compare();
            cv = new ContentValues();
            cv.put("id", new_id.get(geda));
            cv.put("name", new_name.get(geda));
            cv.put("address", new_address.get(geda));
            cv.put("contact", new_contact.get(geda));
            cv.put("latitude", new_latitude.get(geda));
            cv.put("longitude", new_longitude.get(geda));
            cv.put("rating", rating.get(geda));
            db.populate_comparisiontable(cv);
            geda++;
        //}
        //else {
            //for (int i = 0; i < new_id.size(); i++) {
            /*cv = new ContentValues();
            cv.put("id", new_id.get(geda));
            cv.put("name", new_name.get(geda));
            cv.put("address", new_address.get(geda));
            cv.put("contact", new_contact.get(geda));
            cv.put("latitude", new_latitude.get(geda));
            cv.put("longitude", new_longitude.get(geda));
            cv.put("rating", rating.get(geda));
            db.populate_comparisiontable(cv);
            geda++;*/

        //}
       // }
        //Toast.makeText(context, "id= " + new_id + " name= " + new_name + " rating= " + rating, Toast.LENGTH_SHORT).show();


    }



}
