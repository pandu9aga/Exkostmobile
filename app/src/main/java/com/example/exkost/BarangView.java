package com.example.exkost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.exkost.Api.Url;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BarangView extends AppCompatActivity{

    ScrollView barangView;
    TextInputLayout inputBid;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barang_view);

        inputBid = findViewById(R.id.inputBid);

        barangView = findViewById(R.id.parent_scroll);
        queue = Volley.newRequestQueue(BarangView.this);

        loginProcess();
    }

    private void loginProcess() {
        Intent intent = getIntent();
        final String id_barang = intent.getStringExtra("id_barang");
        SessionManager sessionManager = new SessionManager(this);
        final String id_akun = sessionManager.getIdAkun();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.API_BARANG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject bid = jsonObject.getJSONObject("bid");
                    JSONObject highobject = jsonObject.getJSONObject("high");

                    String nama = data.getString("nama_barang");
                    String jenis = data.getString("nama_jenis_barang");
                    String waktu = data.getString("waktu_lelang");
                    String high = highobject.getString("jumlah_tawaran");
                    String your = bid.getString("jumlah_tawaran");
                    String pelelang = data.getString("nama_akun");
                    String alamat = data.getString("alamat_akun");
                    String catatan = data.getString("info_barang");
                    String statusl = data.getString("status_lelang");

                    setdata(nama,jenis,waktu,high,your,pelelang,alamat,catatan,statusl);
                } catch (Exception e) {
                    Snackbar.make(barangView, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(barangView, error.toString(), Snackbar.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id_barang);
                params.put("akun", id_akun);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void setdata(String nama, String jenis, String waktu, final String high, String your, String pelelang, String alamat, String catatan, String statusl){
        TextView namav = (TextView) findViewById(R.id.namaBarang);
        TextView jenisv = (TextView) findViewById(R.id.namaJenis);
        TextView waktuv = (TextView) findViewById(R.id.waktuLelang);
        TextView highv = (TextView) findViewById(R.id.highBid);
        TextView yourv = (TextView) findViewById(R.id.yourBid);
        TextView pelelangv = (TextView) findViewById(R.id.pelelang);
        TextView alamatv = (TextView) findViewById(R.id.alamat);
        TextView catatanv = (TextView) findViewById(R.id.catatan);
        Button btnbid = (Button) findViewById(R.id.letBid);
        namav.setText(nama);
        jenisv.setText(jenis);
        waktuv.setText(waktu);
        highv.setText(high);
        yourv.setText(your);
        pelelangv.setText(pelelang);
        alamatv.setText(alamat);
        catatanv.setText(catatan);

        final String min = high;

        if (!statusl.equals("berlangsung")){
            btnbid.setVisibility(View.GONE);
        }
        btnbid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateBid(min);
            }
        });
    }

    public void backHome(View v) {
        finish();
    }

    private boolean validateBid(String min) {
        String bidInput = inputBid.getEditText().getText().toString().trim();
        if (bidInput.isEmpty()) {
            inputBid.setError("Tawaran tidak boleh kosong");
            return false;
        }else {
            int thebid = Integer.parseInt(bidInput);
            int minim = Integer.parseInt(min);
            if (thebid <= minim){
                inputBid.setError("Tawaran harus lebih tinggi dari "+minim);
                return false;
            }else {
                inputBid.setError(null);
                return true;
            }
        }
    }
}

