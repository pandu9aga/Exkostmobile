package com.example.exkost;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.exkost.Api.Url;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuProfileFragment extends Fragment {

    private RequestQueue queue;
    View view;
    String iD;
    SessionManager sessionManager;
    ScrollView profilView;
    Button saveP,selectP;
    CircularImageView foto;

    private final int IMG_REQUEST = 1;
    private String mBitmapName;
    Bitmap bitmap;
    ProgressDialog progressDialog;

    public MenuProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.menu_fragment_profile, container, false);
        profilView = view.findViewById(R.id.viewProfil);

        queue = Volley.newRequestQueue(getActivity());
        sessionManager = new SessionManager(getActivity());

        viewProfile();

        foto = view.findViewById(R.id.fotoProfil);

        saveP = view.findViewById(R.id.saveProfil);
        selectP = view.findViewById(R.id.gantiFoto);
        selectP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        saveP.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profile");

    }
    private void viewProfile() {
        iD = sessionManager.getIdAkun();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.API_PROFIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");

                    String nama = data.getString("nama_akun");
                    String alamat = data.getString("alamat_akun");
                    String email = data.getString("email_akun");
                    String rekening = data.getString("rekening_akun");
                    String pprofil = data.getString("pp_akun");
                    String password = data.getString("pass_akun");
                    String notelp = data.getString("no_telp_akun");

                    setdata(nama,alamat,email,rekening,pprofil,password,notelp);
                } catch (Exception e) {
                    Snackbar.make(profilView, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(profilView, error.toString(), Snackbar.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", iD);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void setdata(String nama, String alamat, String email, String rekening, String pprofil, String password,String notelp){
        TextInputEditText namav = (TextInputEditText) view.findViewById(R.id.namaText);
        TextInputEditText alamatv = (TextInputEditText) view.findViewById(R.id.alamatText);
        TextInputEditText emailv = (TextInputEditText) view.findViewById(R.id.emailText);
        TextInputEditText rekeningv = (TextInputEditText) view.findViewById(R.id.norekText);
        TextInputEditText passwordv = (TextInputEditText) view.findViewById(R.id.passwordText);
        TextInputEditText notelpv = (TextInputEditText) view.findViewById(R.id.notelpText);
        CircularImageView pprofilv = (CircularImageView) view.findViewById(R.id.fotoProfil);
        namav.setText(nama);
        alamatv.setText(alamat);
        emailv.setText(email);
        rekeningv.setText(rekening);
        passwordv.setText(password);
        notelpv.setText(notelp);
        if (!pprofil.equals("")){
            Picasso.get().load(Url.ASSET_PROFIL+pprofil).into(pprofilv);
        }
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void updateProfile() {
        iD = sessionManager.getIdAkun();
        final TextInputEditText namav = (TextInputEditText) view.findViewById(R.id.namaText);
        final TextInputEditText alamatv = (TextInputEditText) view.findViewById(R.id.alamatText);
        final TextInputEditText emailv = (TextInputEditText) view.findViewById(R.id.emailText);
        final TextInputEditText rekeningv = (TextInputEditText) view.findViewById(R.id.norekText);
        final TextInputEditText passwordv = (TextInputEditText) view.findViewById(R.id.passwordText);
        final TextInputEditText notelpv = (TextInputEditText) view.findViewById(R.id.notelpText);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.UPDATE_PROFIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("stat");

                    progressDialog.dismiss();
                    if(status.equals("true")){
                        Intent intent = getActivity().getIntent();
                        getActivity().finish();
                        getActivity().startActivity(intent);
                        Toast.makeText(getActivity(), "Update Sukses", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Snackbar.make(profilView, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", iD);
                params.put("nama", namav.getText().toString().trim());
                params.put("alamat", alamatv.getText().toString().trim());
                params.put("norek", rekeningv.getText().toString().trim());
                params.put("notelp", notelpv.getText().toString().trim());
                params.put("email", emailv.getText().toString().trim());
                params.put("password", passwordv.getText().toString().trim());
                params.put("foto", imageToString(bitmap));

                return params;
            }
        };

        queue.add(stringRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
//            mengambil alamat file gambar
            Uri path = data.getData();

            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(path);
                mBitmapName = path.getPath();
                bitmap = BitmapFactory.decodeStream(inputStream);

                CircularImageView pprofilv = (CircularImageView) view.findViewById(R.id.fotoProfil);
                pprofilv.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void reLoadFragment()
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
}
