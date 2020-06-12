package com.example.exkost;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.exkost.Api.Url;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.view.GravityCompat;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    Fragment fragment;
    FragmentTransaction transaction;

    SessionManager sessionManager;
    private RequestQueue queue;

    // a static variable to get a reference of our application context
    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }

    String id,saldo,nama;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);


        sessionManager = new SessionManager(this);
        queue = Volley.newRequestQueue(HomeActivity.this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        View headerView = navigationView.getHeaderView(0);
        TextView namaakun = (TextView) headerView.findViewById(R.id.namaAkun);
        //TextView saldoakun = (TextView) headerView.findViewById(R.id.saldoAkun);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);
        firstFragmentDisplay(R.id.nav_home);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            id = extras.getString("ID");
        }else {
            id = sessionManager.getIdAkun();
        }

        contextOfApplication = getApplicationContext();

        dataAkun();

    }

//optionmenu start
    private void firstFragmentDisplay(int itemId) {

        fragment = null;

        switch (itemId) {
            case R.id.nav_home:
                fragment = new MenuHomeFragment();
                break;
            case R.id.nav_account:
                fragment = new MenuProfileFragment();
                break;
            case R.id.nav_logout:
                sessionManager.logout();
                break;
        }

        if (fragment != null) {
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fLayout, fragment);
            transaction.commit();
        }

        drawer.closeDrawers();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        firstFragmentDisplay(item.getItemId());
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }
//optionmenu end

//searchmenu start
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchmenu, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent searchIntent = new Intent(getApplicationContext(), Search.class);
                searchIntent.putExtra(SearchManager.QUERY, query);

                Bundle appData = new Bundle();
                appData.putBoolean(Search.JARGON, true); // put extra data to Bundle
                searchIntent.putExtra(SearchManager.APP_DATA, appData); // pass the search context data
                searchIntent.setAction(Intent.ACTION_SEARCH);

                startActivity(searchIntent);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
//searchmenu end

//pindah activity
        public void pindahactiv(View v){
            Intent i = new Intent(HomeActivity.this,Topup.class); //LoginActivity adalah aktivity awal ,praktikum1Activity activity yang akan di tuju
            startActivity(i);
        }
//pindah activity end

        public void goTo(View v) {
            Intent intent = new Intent(HomeActivity.this, BarangView.class);
            startActivity(intent);
        }

        public String dataId(){
            id = sessionManager.getIdAkun();
            return id;
        }

    private void dataAkun() {
        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        View headerView = navigationView.getHeaderView(0);
        final TextView namaakun = (TextView) headerView.findViewById(R.id.namaAkun);
        //final TextView saldoakun = (TextView) headerView.findViewById(R.id.saldoAkun);
        final FrameLayout frameLayout = findViewById(R.id.fLayout);
        SessionManager sessionManager = new SessionManager(this);
        final String id_akun = sessionManager.getIdAkun();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.API_AKUN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");

                    String nama = data.getString("nama_akun");
                    String saldo = data.getString("saldo_akun");

                    //namaakun.setText(nama);
                    //saldoakun.setText("Saldo: Rp."+saldo);

                } catch (Exception e) {
                    Snackbar.make(frameLayout, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(frameLayout, error.toString(), Snackbar.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id_akun);
                return params;
            }
        };

        queue.add(stringRequest);
    }

}