package com.example.khajasangram.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.khajasangram.Classes.Restaurant_SQLite;
import com.example.khajasangram.Classes.Restaurant_SQLite_comparision;

import java.util.ArrayList;

public class Databasehelper extends SQLiteOpenHelper {
    static String name = "NearbyRestaurants";

    static int version = 1;
    Context context;

    String create2kmtable = "CREATE TABLE if not exists\"twokmtable\"( `id` TEXT, `name` TEXT, `address` TEXT, `contact` TEXT, `latitude` TEXT, `longitude` TEXT, `distance` TEXT, `rating` TEXT, `image` BLOB )";
    String createcomparisiotable = "CREATE TABLE if not exists\"comparisiontable\"( `id` TEXT, `name` TEXT, `address` TEXT, `contact` TEXT, `latitude` TEXT, `longitude` TEXT, `distance` TEXT, `rating` TEXT, `image` BLOB )";

    public Databasehelper(Context context) {
        super(context, name, null, version);
        this.context = context;
        getWritableDatabase().execSQL(create2kmtable);
        getWritableDatabase().execSQL(createcomparisiotable);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long row_count()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Long value = DatabaseUtils.longForQuery(db, "Select count(*) from twokmtable", null);
        return value;
    }
    public long comparetable_rowcount()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Long value = DatabaseUtils.longForQuery(db, "Select count(*) from comparisiontable",null);
        return value;
    }

    public void populate_2kmtable(ContentValues cv)
    {
        getWritableDatabase().insert("twokmtable","",cv);
    }
    public void populate_comparisiontable(ContentValues cv)
    {
        getWritableDatabase().insert("comparisiontable","",cv);
    }
    public void drop_twokmtable()
    {
        String delete = "drop table `twokmtable`";
        getWritableDatabase().execSQL(delete);
    }
    public void delete_content()
    {
        String query = "delete from twokmtable";
        getWritableDatabase().execSQL(query);
    }
    public void delete_content_compare()
    {
        String query = "delete from comparisiontable";
        getWritableDatabase().execSQL(query);
    }

    public ArrayList<Restaurant_SQLite_comparision> comparision_rating()
    {
        String q = "Select * from comparisiontable order by rating desc";
        Cursor c = getReadableDatabase().rawQuery(q,null);

        ArrayList<Restaurant_SQLite_comparision> list  = new ArrayList<>();

        while (c.moveToNext())
        {
            Restaurant_SQLite_comparision obj = new Restaurant_SQLite_comparision();
            obj.id = c.getString(c.getColumnIndex("id"));
            obj.name = c.getString(c.getColumnIndex("name"));
            obj.address = c.getString(c.getColumnIndex("address"));
            obj.contact = c.getString(c.getColumnIndex("contact"));
            obj.latitude = c.getString(c.getColumnIndex("latitude"));
            obj.longitude = c.getString(c.getColumnIndex("longitude"));
            obj.rating = c.getString(c.getColumnIndex("rating"));

            list.add(obj);
        }
        c.close();
        return  list;
    }

    public ArrayList<Restaurant_SQLite> twokmrestaurant_list()
    {
        String query = "select distinct * from twokmtable order by distance";
        Cursor c = getReadableDatabase().rawQuery(query,null);

        ArrayList<Restaurant_SQLite> list = new ArrayList<>();

        while(c.moveToNext())
        {
            Restaurant_SQLite restaurant_sqLite = new Restaurant_SQLite();
            restaurant_sqLite.id = c.getString(c.getColumnIndex("id"));
            restaurant_sqLite.name = c.getString(c.getColumnIndex("name"));
            restaurant_sqLite.address = c.getString(c.getColumnIndex("contact"));
            restaurant_sqLite.contact = c.getString(c.getColumnIndex("address"));
            restaurant_sqLite.latitude = c.getString(c.getColumnIndex("latitude"));
            restaurant_sqLite.longitude = c.getString(c.getColumnIndex("longitude"));
            restaurant_sqLite.distance = c.getString(c.getColumnIndex("distance"));

            list.add(restaurant_sqLite);
        }
        c.close();
        return list;

    }
    public ArrayList<Restaurant_SQLite> twokmrestaurant_list(int distance)
    {
        String query = "select distinct * from twokmtable where distance < "+distance+" order by distance";
        Cursor c = getReadableDatabase().rawQuery(query,null);

        ArrayList<Restaurant_SQLite> list = new ArrayList<>();

        while(c.moveToNext())
        {
            Restaurant_SQLite restaurant_sqLite = new Restaurant_SQLite();
            restaurant_sqLite.id = c.getString(c.getColumnIndex("id"));
            restaurant_sqLite.name = c.getString(c.getColumnIndex("name"));
            restaurant_sqLite.address = c.getString(c.getColumnIndex("contact"));
            restaurant_sqLite.contact = c.getString(c.getColumnIndex("address"));
            restaurant_sqLite.latitude = c.getString(c.getColumnIndex("latitude"));
            restaurant_sqLite.longitude = c.getString(c.getColumnIndex("longitude"));
            restaurant_sqLite.distance = c.getString(c.getColumnIndex("distance"));
            restaurant_sqLite.rating = (c.getString(c.getColumnIndex("rating")));

            list.add(restaurant_sqLite);
        }
        c.close();
        return list;

    }
    public ArrayList<Restaurant_SQLite> twokmrestaurant_list(String place)
    {
        String query = "select distinct * from twokmtable where address ="+"'"+place+"'"+ "order by distance";
        Cursor c = getReadableDatabase().rawQuery(query,null);

        ArrayList<Restaurant_SQLite> list = new ArrayList<>();

        while(c.moveToNext())
        {
            Restaurant_SQLite restaurant_sqLite = new Restaurant_SQLite();
            restaurant_sqLite.id = c.getString(c.getColumnIndex("id"));
            restaurant_sqLite.name = c.getString(c.getColumnIndex("name"));
            restaurant_sqLite.address = c.getString(c.getColumnIndex("contact"));
            restaurant_sqLite.contact = c.getString(c.getColumnIndex("address"));
            restaurant_sqLite.latitude = c.getString(c.getColumnIndex("latitude"));
            restaurant_sqLite.longitude = c.getString(c.getColumnIndex("longitude"));
            restaurant_sqLite.distance = c.getString(c.getColumnIndex("distance"));
            restaurant_sqLite.rating = (c.getString(c.getColumnIndex("rating")));

            list.add(restaurant_sqLite);
        }
        c.close();
        return list;

    }



    public ArrayList<Restaurant_SQLite> twokmrestaurantname_list(String name)
    {
        String query = "select distinct * from twokmtable where name ="+"'"+name+"'"+ "order by distance";
        Cursor c = getReadableDatabase().rawQuery(query,null);

        ArrayList<Restaurant_SQLite> list = new ArrayList<>();

        while(c.moveToNext())
        {
            Restaurant_SQLite restaurant_sqLite = new Restaurant_SQLite();
            restaurant_sqLite.id = c.getString(c.getColumnIndex("id"));
            restaurant_sqLite.name = c.getString(c.getColumnIndex("name"));
            restaurant_sqLite.address = c.getString(c.getColumnIndex("contact"));
            restaurant_sqLite.contact = c.getString(c.getColumnIndex("address"));
            restaurant_sqLite.latitude = c.getString(c.getColumnIndex("latitude"));
            restaurant_sqLite.longitude = c.getString(c.getColumnIndex("longitude"));
            restaurant_sqLite.distance = c.getString(c.getColumnIndex("distance"));
            restaurant_sqLite.rating = (c.getString(c.getColumnIndex("rating")));

            list.add(restaurant_sqLite);
        }
        c.close();
        return list;

    }
}
