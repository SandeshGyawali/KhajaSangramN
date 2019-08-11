//package com.example.khajasangram.Fragments;
//
//import android.app.AlertDialog;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.SharedPreferences;
//import android.location.Location;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.khajasangram.Adaptors.RestaurantAdaptor;
//import com.example.khajasangram.HomepageActivity;
//import com.example.khajasangram.R;
//import com.example.khajasangram.SQLite.Databasehelper;
//import com.example.khajasangram.UservaluesReturner;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//
//public class Home_fragment extends Fragment {
//
//    ArrayList<String> dname;
//    ArrayList<String> daddress;
//    ArrayList<String> dcontact;
//    ArrayList<String> ddistance;
//    ArrayList<String> did;
//    ArrayList<Float> drating;
//
//    RestaurantAdaptor adaptor;
//
//    String user_lat_receive, user_lng_receive;
//    Databasehelper databasehelper, databasehelper1;
//    ContentValues contentValues;
//
//    SharedPreferences sharedPreferences;
//
//    DatabaseReference reference_restaurant, reference_user;
//    final int check = 0;
//
//    FirebaseAuth auth;
//    DatabaseReference reference;
//
//    String uid;
//    Double rating_value=0.0,final_rating_value=0.0;
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        databasehelper1 = new Databasehelper(getContext());
//        databasehelper = new Databasehelper(getContext());
//
//        databasehelper1.delete_content();
//        //auth = FirebaseAuth.getInstance();
//        //String uiid = auth.getCurrentUser().getUid();
//        View rootView = inflater.inflate(R.layout.home_fragment, null);
//
//
//
//        sharedPreferences = getActivity().getSharedPreferences("uidpreference",0);
//        uid = sharedPreferences.getString("uid",null);
//        reference_user = FirebaseDatabase.getInstance().getReference("Users").child(uid);
//        reference_user.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String latitude = dataSnapshot.child("latitude").getValue(String.class);
//                Toast.makeText(getContext(), "latitude "+latitude, Toast.LENGTH_SHORT).show();
//                user_lat_receive = latitude;
//                user_lng_receive = latitude;
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//        // 1. get a reference to recyclerView
//        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
//
//
//        if (!isConnected(getContext())) buildDialog(getContext()).show();
//
//        else {
//
//
//            dname = new ArrayList<>();
//            daddress = new ArrayList<>();
//            dcontact = new ArrayList<>();
//            ddistance = new ArrayList<>();
//            did = new ArrayList<>();
//            drating = new ArrayList<>();
//
//
//            reference_restaurant = FirebaseDatabase.getInstance().getReference("Restaurants");
//
//            reference_restaurant.addListenerForSingleValueEvent(new ValueEventListener() {
//
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    databasehelper1.delete_content();
//
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        DatabaseReference review, image;
//                        review = FirebaseDatabase.getInstance().getReference("Rating");
//                        image = FirebaseDatabase.getInstance().getReference("Images");
//
//                        contentValues = new ContentValues();
//                        String name = snapshot.child("name").getValue(String.class);
//                        String address = snapshot.child("address").getValue(String.class);
//
//                        String contact = snapshot.child("contact").getValue(String.class);
//
//                        String id = snapshot.child("id").getValue(String.class);
//                        String lat = snapshot.child("latitude").getValue(String.class);
//                        String lng = snapshot.child("longitude").getValue(String.class);
//
//                        review.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                for(DataSnapshot snapshot : dataSnapshot.getChildren())
//                                {
//                                    Double star = Double.parseDouble(snapshot.child("stars").getValue(String.class));
//                                    rating_value = rating_value + star;
//                                }
//                                Double d = rating_value/dataSnapshot.getChildrenCount() ;
//                                final_rating_value = d;
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                        drating.add(final_rating_value.floatValue());
//                        dname.add(name);
//                        daddress.add(address);
//                        dcontact.add(contact);
//                        did.add(id);
//                        //dcreated_date.add(created_date);
//
//                        Location loc1 = new Location("");
//                        loc1.setLatitude(Double.valueOf(user_lat_receive));
//                        loc1.setLongitude(Double.valueOf(user_lng_receive));
//
//                        Location loc2 = new Location("");
//                        loc2.setLatitude(Double.valueOf(lat));
//                        loc2.setLongitude(Double.valueOf(lng));
//
//                        float distanceInMeters = loc1.distanceTo(loc2);
//                        float distanceinkm = (distanceInMeters / 1000);
//                        ddistance.add(String.valueOf(distanceinkm));
//
//                        if(distanceinkm<2) {
//                            contentValues.put("id", id);
//                            contentValues.put("name", name);
//                            contentValues.put("address", address);
//                            contentValues.put("contact", contact);
//                            contentValues.put("latitude", lat);
//                            contentValues.put("longitude", lng);
//                            contentValues.put("distance", String.valueOf(distanceinkm));
//                            contentValues.put("rating",final_rating_value);
//                            databasehelper.populate_2kmtable(contentValues);
//
//                        }
//                        adaptor = new RestaurantAdaptor(recyclerView, getContext(), dname, daddress, dcontact, did, ddistance, drating);
//                        recyclerView.setAdapter(adaptor);
//
//                        recyclerView.setHasFixedSize(true);
//                        // use a linear layout manager
//                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
//                        recyclerView.setLayoutManager(mLayoutManager);
//
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//
//
//            // this is data for recycler view
//
//
//        }
//        return rootView;
//    }
//
//    public boolean isConnected(Context context) {
//
//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netinfo = cm.getActiveNetworkInfo();
//
//        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
//            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//
//            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
//                return true;
//            else return false;
//        } else
//
//            return false;
//    }
//
//    public AlertDialog.Builder buildDialog(Context c) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(c);
//        builder.setTitle("No Internet Connection");
//        builder.setCancelable(false);
//        builder.setMessage("You need to have Mobile Data or Wifi to access this. Press ok to Exit");
//
//        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                getActivity().finish();
//            }
//        });
//
//        return builder;
//    }
//
//}
