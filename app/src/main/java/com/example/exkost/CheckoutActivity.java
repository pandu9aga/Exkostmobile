package com.example.exkost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
    }

    public void pindahrekening(View v){
        Intent i = new Intent(CheckoutActivity.this, Rekening.class); //LoginActivity adalah aktivity awal ,praktikum1Activity activity yang akan di tuju
        startActivity(i);
    }

    public void pindahbukti(View v){
        Intent i = new Intent(CheckoutActivity.this, Buktitrans.class); //LoginActivity adalah aktivity awal ,praktikum1Activity activity yang akan di tuju
        startActivity(i);
    }

    public void pindahtopup(View v){
        Intent i = new Intent(CheckoutActivity.this, Topup.class); //LoginActivity adalah aktivity awal ,praktikum1Activity activity yang akan di tuju
        startActivity(i);
    }
}
