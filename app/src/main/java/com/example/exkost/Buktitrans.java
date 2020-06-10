package com.example.exkost;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.exkost.Api.Url;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Buktitrans extends AppCompatActivity {

    private RequestQueue queue;
    SessionManager sessionManager;

    ProgressDialog progressDialog;
    private final int IMG_REQUEST = 1;
    private String mBitmapName;
    Bitmap bitmap;

    SwipeRefreshLayout swipeBarang;

    String idTopup;
    String from;
    LinearLayout detilView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buktitrans);

        queue = Volley.newRequestQueue(Buktitrans.this);
        sessionManager = new SessionManager(Buktitrans.this);

        detilView = findViewById(R.id.detailToplayout);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            idTopup = extras.getString("idtopup");
            from = extras.getString("from");
        }

        swipeBarang = findViewById(R.id.swipeDetop);
        swipeBarang.setColorSchemeResources(R.color.red, R.color.red2);
        swipeBarang.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cekTopup();
                if(swipeBarang.isRefreshing()) {
                    swipeBarang.setRefreshing(false);
                }
            }
        });

        cekTopup();
    }

    private void cekTopup() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.API_DETAILTOP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");

                    String namap = data.getString("nama_rekening");
                    String namab = data.getString("nama_bank_admin");
                    String rek = data.getString("no_rek_admin");
                    String bukti = data.getString("bukti_transfer");
                    String statustop = data.getString("status_topup");
                    String nominal = data.getString("nominal");

                    setdata(namap,namab,rek,bukti,statustop,nominal);
                } catch (Exception e) {
                    Snackbar.make(detilView, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Buktitrans.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", idTopup);

                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void setdata(String namap, String namab, String rek, String bukti, String status, String nominal){
        TextView txtstatus = (TextView) findViewById(R.id.status);
        TextView namatrans = (TextView) findViewById(R.id.namaPengirim);
        TextView namabank = (TextView) findViewById(R.id.bankAdmin);
        TextView norek = (TextView) findViewById(R.id.norekAdmin);
        TextView nom = (TextView) findViewById(R.id.nomTopup);
        ImageView gambarbukti = (ImageView) findViewById(R.id.imageUpload);
        Button select = (Button) findViewById(R.id.selectimage);
        Button upload = (Button) findViewById(R.id.uploadBukti);
        namatrans.setText(namap);
        namabank.setText(namab);
        norek.setText(rek);
        nom.setText("Rp. "+nominal);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmInputUpload();
            }
        });

        if (status.equals("belum")){

        }else if (status.equals("menunggu")){
            txtstatus.setText("Tunggu Konfirmasi Admin");
            select.setVisibility(View.GONE);
            upload.setVisibility(View.GONE);
            Picasso.get().load(Url.ASSET_TOPUP + bukti).into(gambarbukti);
        }else if (status.equals("sukses")){
            txtstatus.setTextColor(Color.parseColor("#3385ff"));
            txtstatus.setText("Sukses Menambah Saldo");
            select.setVisibility(View.GONE);
            upload.setVisibility(View.GONE);
            Picasso.get().load(Url.ASSET_TOPUP + bukti).into(gambarbukti);
        }else if (status.equals("gagal")){
            txtstatus.setTextColor(Color.parseColor("#d10024"));
            txtstatus.setText("Topup digagalkan Admin");
            select.setVisibility(View.GONE);
            upload.setVisibility(View.GONE);
            Picasso.get().load(Url.ASSET_TOPUP + bukti).into(gambarbukti);
        }

        //get Screen Dimensions
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        //NOTE: If you want to square, just use one of these value.
        //set as half of dimens
        gambarbukti.getLayoutParams().width = width/5*4;
        gambarbukti.getLayoutParams().height = width/2;

    }

    //    fungsi untuk memilih gambar dari galery
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }

    //    konversi gambar menjadi string
    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadBukti() {
        progressDialog = new ProgressDialog(Buktitrans.this);
        progressDialog.setMessage("Proses");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.UPLOAD_BUKTI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("stat");
                    String id = jsonObject.getString("id");

                    progressDialog.dismiss();
                    if(status.equals("sukses")){
                        Intent intent = new Intent(Buktitrans.this, Buktitrans.class);
                        intent.putExtra("idtopup",id);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(Buktitrans.this, "Error", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Snackbar.make(detilView, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Buktitrans.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", idTopup);
                params.put("status", "menunggu");
                params.put("foto", imageToString(bitmap));
                return params;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == Buktitrans.this.RESULT_OK && data != null) {
//            mengambil alamat file gambar
            Uri path = data.getData();

            try {
                InputStream inputStream = Buktitrans.this.getContentResolver().openInputStream(path);
                mBitmapName = path.getPath();
                bitmap = BitmapFactory.decodeStream(inputStream);

                ImageView imgup = (ImageView) findViewById(R.id.imageUpload);
                imgup.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Toast.makeText(Buktitrans.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private boolean validateFoto() {
        if (bitmap==null) {
            Toast.makeText(Buktitrans.this, "Gambar harus dipilih", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }
    public void confirmInputUpload() {
        if (validateFoto()) {
            uploadBukti();
        }
    }

    public void pindahhome(View v){
        if (from.equals("checkout")){
            Intent i = new Intent(Buktitrans.this, HomeActivity.class);
            startActivity(i);
        }else {
            finish();
        }
    }


    public void confirkecheckout(View v){
        Intent i = new Intent(Buktitrans.this, CheckoutActivity.class);
        startActivity(i);
    }

}
