package com.example.exkost;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

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

public class LogFirstFragment extends Fragment {

    View view;
    CardView firstButton;
    TextView firstFragment;
    TextInputLayout password,email;
    RelativeLayout fragView;

    SessionManager sessionManager;

    private RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.login_fragment_one, container, false);

        queue = Volley.newRequestQueue(getActivity());
        sessionManager = new SessionManager(getActivity());

        if (sessionManager.isLogin() == true){
            Intent main = new Intent(getActivity(), HomeActivity.class);
            startActivity(main);
        }

        firstFragment = (TextView) view.findViewById(R.id.firstFragment);
        firstFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if(activity instanceof MainActivity){
                    MainActivity myactivity = (MainActivity) activity;
                    myactivity.loadFragment(new LogSecondFragment());
                }
            }
        });

        email = view.findViewById(R.id.loginEmail);
        password = view.findViewById(R.id.loginPassword);

        fragView = view.findViewById(R.id.loginFrag);

        firstButton = (CardView) view.findViewById(R.id.buttonMasuk);
        firstButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                confirmInputLogin();
            }
        });

        return view;
    }

    private boolean validateEmail() {
        String emailInput = email.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            email.setError("Email harus diisi");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = password.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            password.setError("Password harus diisi");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private void _loginProcess() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.API_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equals("false")) {
                        Snackbar.make(fragView, message, Snackbar.LENGTH_LONG).show();
                    } else {
                        JSONObject data = jsonObject.getJSONObject("data");

                        String id = data.getString("id_akun");
                        String email = data.getString("email_akun");
                        String nama = data.getString("nama_akun");
                        String saldo = data.getString("saldo_akun");

                        sessionManager.createSession(email, nama, id, saldo);

                        Intent main = new Intent(getActivity(), MainActivity.class);
                        main.putExtra("EMAIL", email);
                        main.putExtra("NAMA", nama);
                        main.putExtra("ID", id);
                        startActivity(main);
                    }
                } catch (Exception e) {
                    Snackbar.make(fragView, "Data tidak ditemukan", Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(fragView, "Kesalahan dalam mengakses server", Snackbar.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email.getEditText().getText().toString().trim());
                params.put("password", password.getEditText().getText().toString().trim());
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void confirmInputLogin() {
        if (validateEmail() | validatePassword()) {
            _loginProcess();
        }
    }

}
