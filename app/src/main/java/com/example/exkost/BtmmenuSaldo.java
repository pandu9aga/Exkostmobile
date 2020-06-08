package com.example.exkost;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class BtmmenuSaldo extends Fragment {

    SessionManager sessionManager;
    private RequestQueue queue;
    TextView saldoakun;
    LinearLayout frameLayout;


    public BtmmenuSaldo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.content_saldo, container, false);
        ListView listview =(ListView) rootView.findViewById(R.id.list);
        final String[] items = new String[] {"+ Rp 50.000", "+ Rp 100.000", "+ Rp 100.000"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (getActivity(), android.R.layout.simple_list_item_1, items);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity().getApplicationContext(), items[i], Toast.LENGTH_SHORT).show();
            }
        });

        saldoakun = rootView.findViewById(R.id.nomSaldo);
        frameLayout = rootView.findViewById(R.id.saldoLayout);

        sessionManager = new SessionManager(getActivity());
        queue = Volley.newRequestQueue(getActivity());
        dataAkun();

        return rootView;
    }

    private void dataAkun() {
        SessionManager sessionManager = new SessionManager(getActivity());
        final String id_akun = sessionManager.getIdAkun();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.API_AKUN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");

                    String saldo = data.getString("saldo_akun");
                    saldoakun.setText("Rp."+saldo);

                } catch (Exception e) {
                    Snackbar.make(frameLayout, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(frameLayout, error.toString(), Snackbar.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id_akun);
                return params;
            }
        };

        queue.add(stringRequest);
    }

}