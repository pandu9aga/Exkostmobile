package com.example.exkost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Buktitrans extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buktitrans);
    }

    public void pindahhome(View v){
        Intent i = new Intent(Buktitrans.this, HomeActivity.class); //MainActivity adalah aktivity awal ,praktikum1Activity activity yang akan di tuju
        startActivity(i);
    }


    public void confirkecheckout(View v){
        Intent i = new Intent(Buktitrans.this, CheckoutActivity.class); //MainActivity adalah aktivity awal ,praktikum1Activity activity yang akan di tuju
        startActivity(i);
    }

}
