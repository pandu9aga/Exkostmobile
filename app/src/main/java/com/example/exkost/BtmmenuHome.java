package com.example.exkost;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.exkost.Api.Url;
import com.example.exkost.Model.ModelHome;
import com.example.exkost.adapter.AdapterHome;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class BtmmenuHome extends Fragment {

    View view;
    private SwipeRefreshLayout swipeHome;
    SessionManager sessionManager;
    private RequestQueue queue;
    String id;

    List<ModelHome> mItems;
    RecyclerView mRecyclerview;
    RecyclerView.LayoutManager mManager;
    RecyclerView.Adapter mAdapter;

    public BtmmenuHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.content_home, container, false);

        swipeHome = view.findViewById(R.id.swipeHome);

        swipeHome.setColorSchemeResources(R.color.red, R.color.red2);

        swipeHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        id = sessionManager.getIdAkun();

        mRecyclerview = (RecyclerView) view.findViewById(R.id.recHome);
        mManager = new LinearLayoutManager(getActivity());
        mRecyclerview.setLayoutManager(mManager);
        mAdapter = new AdapterHome(getActivity(),mItems);
        mRecyclerview.setAdapter(mAdapter);

        mItems = new ArrayList<>();

        loadHome();

        return view;
    }

    private void loadHome() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.API_HOME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("data");

                    for(int i = 0 ; i < response.length(); i++)
                    {
                        JSONObject data = array.getJSONObject(i);
                        ModelHome md = new ModelHome();
                        md.setIdBarang(data.getString("id_barang"));
                        md.setNamaBarang(data.getString("nama_barang"));
                        md.setNamaJenis(data.getString("nama_jenis_barang"));
                        md.setHargaBarang(data.getString("harga_barang"));
                        md.setWaktuLelang(data.getString("waktu_lelang"));
                        mItems.add(md);
                    }
                    mAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    Snackbar.make(swipeHome, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(swipeHome, error.toString(), Snackbar.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id.trim());
                return params;
            }
        };

        queue.add(stringRequest);
    }

}

