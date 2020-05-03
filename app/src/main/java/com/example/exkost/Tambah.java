package com.example.exkost;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Tambah extends AppCompatActivity{

    Button btnStat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_barang);


    }

    public void backHome(View v) {
        finish();
    }
}
