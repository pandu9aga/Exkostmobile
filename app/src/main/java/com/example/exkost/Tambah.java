package com.example.exkost;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.exkost.Api.Url;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Tambah extends AppCompatActivity{

    ImageView gambarv;

    private RequestQueue queue;
    SessionManager sessionManager;

    TextInputLayout nama,harga,waktu,catatan;
    Button gambar,upload;

    String kategori;
    Spinner spupload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_barang);

        queue = Volley.newRequestQueue(Tambah.this);
        sessionManager = new SessionManager(Tambah.this);

        nama = findViewById(R.id.namaBarang);
        harga = findViewById(R.id.hargaBarang);
        waktu = findViewById(R.id.waktuLelang);
        catatan = findViewById(R.id.catatan);
        gambar = findViewById(R.id.uploadButton);
        upload = findViewById(R.id.tambahBarang);

        spupload = (Spinner) findViewById(R.id.spUpload);
        addItemsOnSpinner();

        gambarv = findViewById(R.id.imageUpload);
        //get Screen Dimensions
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        //NOTE: If you want to square, just use one of these value.
        //set as half of dimens
        gambarv.getLayoutParams().width = width/5*4;
        gambarv.getLayoutParams().height = width/2;



    }

    ArrayList<String> list;

    public void addItemsOnSpinner() {
        list = new ArrayList<String>();
        //list.add("semua");

        StringRequest reqData = new StringRequest(Request.Method.GET, Url.API_JENIS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //pd.cancel();
                        Log.d("volley","response : " + response.toString());
                        try {
                            JSONArray j = new JSONArray(response);
                            for(int i = 0 ; i < response.length(); i++)
                            {
                                try {
                                    JSONObject data = j.getJSONObject(i);
                                    list.add(data.getString("nama_jenis_barang"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            setSpinnerAdapter();
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //pd.cancel();
                        Log.d("volley", "error : " + error.getMessage());
                    }
                });
        queue.add(reqData);
    }

    private void setSpinnerAdapter(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Tambah.this, android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spupload.setAdapter(adapter);
        spupload.setSelection(0);
        spupload.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                kategori = spupload.getItemAtPosition(i).toString();
                //Toast.makeText(getActivity(), ""+ kategori, Toast.LENGTH_SHORT).show();
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
