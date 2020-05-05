package com.example.exkost;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Search extends AppCompatActivity{

    public static final String JARGON = "Cari";
    Toolbar toolbar;

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;

    Fragment fragment;
    FragmentTransaction transaction;

    ListView listView;
    ListViewAdapter listViewAdapter;
    ArrayList<App> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int[] iconList = new int[]{
                R.drawable.ic_image_black_24dp, R.drawable.ic_image_black_24dp, R.drawable.ic_image_black_24dp
        };

        final String[] Headline = {"Meja", "Kasur", "Lemari"
        };
        String[] Subhead = {"Meja", "Kasur", "Lemari"
        };

        listView = findViewById(R.id.listview);

        for (int i = 0; i < Headline.length; i++) {
            App app = new App(Headline[i], Subhead[i], iconList[i]);
            arrayList.add(app);

            listViewAdapter = new ListViewAdapter(this, arrayList);
            listView.setAdapter(listViewAdapter);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cari");

        handleIntent(getIntent());
    }

    //searchmenu start
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (TextUtils.isEmpty(s)) {
                    listViewAdapter.filter("");
                    listView.clearTextFilter();
                } else {
                    listViewAdapter.filter(s);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }
//searchmenu end

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            // use the query to search the data somehow

            listViewAdapter.filter(query);

            Bundle appData = intent.getBundleExtra(SearchManager.APP_DATA);
            if (appData != null) {
                boolean jargon = appData.getBoolean(Search.JARGON);
                // use the context data to refine our search
            }
        }
    }

    public void goTo(View v) {
        Intent intent = new Intent(Search.this, BarangView.class);
        startActivity(intent);
    }

}