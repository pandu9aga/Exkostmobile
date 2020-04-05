package com.example.exkost;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.FrameLayout;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity{
    LinearLayout linearLayout;
    FrameLayout navbar, content, topbar;
    Button buttonSearch, buttonMenu, home, myCart, myAuction, mySaldo;
    EditText textSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

// get the reference of Button's
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonMenu = (Button) findViewById(R.id.buttonMenu);
        home = (Button) findViewById(R.id.home);
        myCart = (Button) findViewById(R.id.myCart);
        myAuction = (Button) findViewById(R.id.myAuction);
        mySaldo = (Button) findViewById(R.id.mySaldo);

        content = (FrameLayout) findViewById(R.id.content);

        FragmentManager fm = getFragmentManager();

// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.content, new HomeFragment());
        fragmentTransaction.commit(); // save the changes

// perform setOnClickListener event on First Button
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

// load First Fragment
                loadFragment(new HomeFragment());

            }
        });

// perform setOnClickListener event on Second Button
        myCart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

// load First Fragment
                loadFragment(new CartFragment());

            }
        });

        myAuction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

// load First Fragment
                loadFragment(new LelangFragment());

            }
        });

        mySaldo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

// load First Fragment
                loadFragment(new SaldoFragment());

            }
        });

    }


    private void loadFragment(Fragment fragment) {

// create a FragmentManager
        FragmentManager fm = getFragmentManager();

// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit(); // save the changes

    }


}
