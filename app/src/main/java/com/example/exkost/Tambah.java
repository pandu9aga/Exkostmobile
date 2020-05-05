package com.example.exkost;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Tambah extends AppCompatActivity{

    Spinner spupload;
    String[] kategori = {
            "Lemari",
            "Kasur",
            "Meja",
            "Kursi",
            "Kipas",
            "Jam",
            "Accessories",
            "Elektronik",
            "Mainan",
            "Buku"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_barang);

        spupload = (Spinner) findViewById(R.id.spUpload);

        // inisialiasi Array Adapter dengan memasukkan string array di atas
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, kategori);

        // mengeset Array Adapter tersebut ke Spinner
        spupload.setAdapter(adapter);

        // mengeset listener untuk mengetahui saat item dipilih
        spupload.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
                Toast.makeText(Tambah.this, ""+ adapter.getItem(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void backHome(View v) {
        finish();
    }
}
