package com.example.exkost;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LelangSend extends AppCompatActivity{

    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_winner);

        btnSend = findViewById(R.id.btnKirim);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LelangSend.this, LelangView.class);
                startActivity(intent);
            }
        });

    }

    public void backHome(View v) {
        finish();
    }
}
