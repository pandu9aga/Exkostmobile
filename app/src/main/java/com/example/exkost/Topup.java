package com.example.exkost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Topup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);
    }

    public void pindahcheckout(View v){
        Intent i = new Intent(Topup.this, CheckoutActivity.class); //LoginActivity adalah aktivity awal ,praktikum1Activity activity yang akan di tuju
        startActivity(i);
    }
}
