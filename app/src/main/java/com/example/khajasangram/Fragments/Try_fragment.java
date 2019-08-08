package com.example.khajasangram.Fragments;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khajasangram.Adaptors.RestaurantAdaptor;
import com.example.khajasangram.R;
import com.example.khajasangram.SQLite.Databasehelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Try_fragment extends Fragment {
    DatabaseReference reference, reference_restaurant;
    SharedPreferences preferences;

    ArrayList<String> dname;
    ArrayList<String> daddress;
    ArrayList<String> dcontact;
    ArrayList<String> ddistance;
    ArrayList<String> did;
    ArrayList<String> drating;


    ContentValues contentValues;

    Databasehelper databasehelper,databasehelper1;


    RestaurantAdaptor adaptor;

    Double value;
    FirebaseAuth auth;
    String user_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //databasehelper1 = new Databasehelper(getContext());
        //databasehelper1.delete_content();


        View view = inflater.inflate(R.layout.home_fragment, null);



        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);

        if (!isConnected(getContext())) buildDialog(getContext()).show();

        else {


             databasehelper = new Databasehelper(getContext());

            dname = new ArrayList<>();
            daddress = new ArrayList<>();
            dcontact = new ArrayList<>();
            ddistance = new ArrayList<>();
            did = new ArrayList<>();
            drating = new ArrayList<>();



            auth = FirebaseAuth.getInstance();
            user_id = auth.getCurrentUser().getUid();
            Location loc1 = new Location("");

            preferences = getActivity().getSharedPreferences("uidpreference",0);

            reference = FirebaseDatabase.getInstance().getReference("Users").child(user_id);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String user_latitude = dataSnapshot.child("latitude").getValue(String.class);
                    String user_longitude = dataSnapshot.child("longitude").getValue(String.class);
                    loc1.setLatitude(Double.valueOf(user_latitude));
                    loc1.setLongitude(Double.valueOf(user_longitude));

                    Toast.makeText(getContext(), "lat= "+user_latitude, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



            reference_restaurant = FirebaseDatabase.getInstance().getReference("Restaurants");
            reference_restaurant.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int child_count = (int) dataSnapshot.getChildrenCount();

                    databasehelper1 = new Databasehelper(getContext());
                    Long db_row = databasehelper1.row_count();

                    if (db_row.intValue() >= child_count || db_row.intValue() == 0) {
                            Databasehelper db = new Databasehelper(getContext());
                            db.delete_content();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            DatabaseReference review, image;
                            review = FirebaseDatabase.getInstance().getReference("Rating");
                            image = FirebaseDatabase.getInstance().getReference("Images");

                            contentValues = new ContentValues();
                            String name = snapshot.child("name").getValue(String.class);
                            String address = snapshot.child("address").getValue(String.class);
                            String contact = snapshot.child("contact").getValue(String.class);

                            String id = snapshot.child("id").getValue(String.class);
                            String lat = snapshot.child("latitude").getValue(String.class);

                            String lng = snapshot.child("longitude").getValue(String.class);
                            final Double[] final_rating_value = new Double[1];

                            review.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Double rating_value = 0.0;
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Double star = Double.parseDouble(snapshot.child("stars").getValue(String.class));
                                        rating_value = rating_value + star;
                                    }
                                    if (dataSnapshot.getChildrenCount() == 0) {
                                        final_rating_value[0] = 0.0;
                                        value = final_rating_value[0];

                                    } else {
                                        Double d = rating_value / dataSnapshot.getChildrenCount();
                                        final_rating_value[0] = d;
                                        value = final_rating_value[0];

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            //Toast.makeText(getContext(), "rating= " +value, Toast.LENGTH_SHORT).show();

                            drating.add(String.valueOf(final_rating_value[0]));

                            dname.add(name);

                            daddress.add(address);

                            dcontact.add(contact);

                            did.add(id);


                            Location loc2 = new Location("");
                            loc2.setLatitude(Double.valueOf(lat));
                            loc2.setLongitude(Double.valueOf(lng));

                            float distanceInMeters = loc1.distanceTo(loc2);
                            float distanceinkm = (distanceInMeters / 1000);
                            ddistance.add(String.valueOf(distanceinkm));

                            contentValues.put("distance", String.valueOf(distanceinkm));
                            contentValues.put("id", id);
                            contentValues.put("name", name);
                            contentValues.put("address", address);
                            contentValues.put("contact", contact);
                            contentValues.put("latitude", lat);
                            contentValues.put("longitude", lng);

                            contentValues.put("rating", String.valueOf(final_rating_value[0]));


                            databasehelper.populate_2kmtable(contentValues);
                            //Toast.makeText(getContext(),"final value= "+ final_rating_value[0],Toast.LENGTH_SHORT).show();

                            adaptor = new RestaurantAdaptor(recyclerView, getContext(), dname, daddress, dcontact, did, ddistance);
                            recyclerView.setAdapter(adaptor);

                            recyclerView.setHasFixedSize(true);
                            // use a linear layout manager
                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(mLayoutManager);


                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
            return view;
    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        } else

            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setCancelable(false);
        builder.setMessage("You need to have Mobile Data or Wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });

        return builder;
    }

}
