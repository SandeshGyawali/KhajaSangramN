package com.example.khajasangram;

import android.os.Bundle;

import com.example.khajasangram.Fragments.Dashboard_fragment;
import com.example.khajasangram.Fragments.Home_fragment;
import com.example.khajasangram.Fragments.Notification_fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;
import android.widget.TextView;

public class HomepageActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);

        loadFragment(new Home_fragment());
    }

    private boolean loadFragment(Fragment fragment)
    {
        if (fragment != null) {

            getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.fragment_container,fragment).
                    commit();

            return true;
        }

            return false;

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;

        switch(menuItem.getItemId())
        {
            case R.id.navigation_home:

                fragment = new Home_fragment();
                break;
        case R.id.navigation_dashboard:

                fragment = new Dashboard_fragment();
                break;
        case R.id.navigation_notifications:

                fragment = new Notification_fragment();
                break;
        }


        return loadFragment(fragment);
    }
}
