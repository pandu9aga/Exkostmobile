package com.example.exkost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MenuActivity extends AppCompatActivity {

    Menu menu_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contohmenu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Exkost");
        }

        final NavigationView nav_view = findViewById(R.id.nav_view);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                //updateCounter(nav_view);
                super.onDrawerOpened(drawerView);
            }
        };

        //updateCounter(nav_view);
        menu_nav = nav_view.getMenu();

        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Toast.makeText(getApplicationContext(), menuItem.getTitle() + " Selected",
                        Toast.LENGTH_SHORT).show();
                drawer.closeDrawers();
                return true;
            }
        });
    }

    private void updateCounter(NavigationView nav) {
        Menu menu = nav.getMenu();
        TextView badgeText = menu.findItem(R.id.nav_notification).getActionView().findViewById(R.id.text);
        badgeText.setText("3 new");
        badgeText.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    }
}