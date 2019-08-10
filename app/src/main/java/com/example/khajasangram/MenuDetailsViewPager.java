package com.example.khajasangram;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.khajasangram.Fragments.ViewPagerFragments.FragmentParent;

public class MenuDetailsViewPager extends AppCompatActivity {
    Button buttonAddPage;
    //FragmentParent fragmentParent;
    TextView textView;
    FragmentParent fragmentParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_details_view_pager);
        getIDs();
        setEvents();

    }

    private void getIDs() {
        buttonAddPage = (Button) findViewById(R.id.buttonAddPage);
        //fragmentParent = (FragmentParent) this.getSupportFragmentManager().findFragmentById(R.id.fragmentParent);
        fragmentParent = (FragmentParent) this.getSupportFragmentManager().findFragmentById(R.id.fragmentParent);
        textView = (TextView) findViewById(R.id.editTextPageName);
    }

    private void setEvents() {
        buttonAddPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!textView.getText().toString().equals("")) {
                    fragmentParent.addPage(textView.getText() + "");
                    textView.setText("");
                } else {
                    Toast.makeText(MenuDetailsViewPager.this, "Page name is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
