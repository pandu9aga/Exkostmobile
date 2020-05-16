package com.example.exkost;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LelangView extends AppCompatActivity{

    Button btnStat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lelang_view);

        btnStat = findViewById(R.id.btnStat);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            String value = extras.getString("stat");
            if (value.equals("Berlangsung")){
                btnStat.setText("Berlangsung");
                btnStat.setBackgroundColor(Color.parseColor("#0066ff"));
            } else if (value.equals("Kirim")){
                btnStat.setText("Kirim");
                btnStat.setBackgroundColor(Color.parseColor("#D10024"));
                btnStat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LelangView.this, LelangSend.class);
                        startActivity(intent);
                    }
                });
            } else if (value.equals("Selesai")){
                btnStat.setText("Selesai");
                btnStat.setBackgroundColor(Color.parseColor("#D10024"));
            }

        }

    }

    public void backHome(View v) {
        finish();
    }
}
