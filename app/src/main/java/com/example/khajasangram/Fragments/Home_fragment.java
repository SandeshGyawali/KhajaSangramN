package com.example.khajasangram.Fragments;

import android.app.AlertDialog;
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
import com.example.khajasangram.RestaurantlistActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home_fragment extends Fragment {

    ArrayList<String> dname;
    ArrayList<String> daddress;
    ArrayList<String> dcontact;
    ArrayList<String> dcreated_date;
    ArrayList<String> ddistance;
    ArrayList<String> did;

    RestaurantAdaptor adaptor;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.home_fragment, null);
        // 1. get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);

        // 2. set layoutManger

        sharedPreferences = getActivity().getSharedPreferences("UserDataHome", 0);

        if (!isConnected(getContext())) buildDialog(getContext()).show();

        else {
            //setContentView(R.layout.activity_restaurantlist);
            //recyclerView = (RecyclerView)findViewById(R.id.myrecylerView);

            dname = new ArrayList<>();
            daddress = new ArrayList<>();
            dcontact = new ArrayList<>();
            ddistance = new ArrayList<>();
            dcreated_date = new ArrayList<>();
            did = new ArrayList<>();

            DatabaseReference reference;
            reference = FirebaseDatabase.getInstance().getReference("Restaurants");

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String name = snapshot.child("name").getValue(String.class);
                        String address = snapshot.child("address").getValue(String.class);

                        String contact = snapshot.child("contact").getValue(String.class);
                        String created_date = snapshot.child("created_date").getValue(String.class);

                        //Long idvalue = snapshot.child("id").getValue(Long.class);
                        //String id = String.valueOf(idvalue);
                        String id = snapshot.child("id").getValue(String.class);
                        String lat = snapshot.child("latitude").getValue(String.class);
                        String lng = snapshot.child("longitude").getValue(String.class);

                        //Toast.makeText(getActivity(), "contact = "+contact, Toast.LENGTH_SHORT).show();

                        String user_lat = sharedPreferences.getString("latitude", null);
                        String user_lng = sharedPreferences.getString("longitude", null);


                        dname.add(name);
                        daddress.add(address);
                        dcontact.add(contact);
                        did.add(id);
                        dcreated_date.add(created_date);

                        Location loc1 = new Location("");
                        loc1.setLatitude(Double.valueOf(user_lat));
                        loc1.setLongitude(Double.valueOf(user_lng));

                        Location loc2 = new Location("");
                        loc2.setLatitude(Double.valueOf(lat));
                        loc2.setLongitude(Double.valueOf(lng));

                        float distanceInMeters = loc1.distanceTo(loc2);
                        float distanceinkm = (distanceInMeters / 1000);
                        ddistance.add(String.valueOf(distanceinkm));

                        adaptor = new RestaurantAdaptor(recyclerView, getContext(), dname, daddress, dcontact, did, dcreated_date, ddistance);
                        recyclerView.setAdapter(adaptor);

                        recyclerView.setHasFixedSize(true);
                        // use a linear layout manager
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(mLayoutManager);


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            // this is data for recycler view


        }

        return rootView;
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