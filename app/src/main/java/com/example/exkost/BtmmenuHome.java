package com.example.exkost;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.exkost.Api.Url;
import com.example.exkost.Model.ModelHome;
import com.example.exkost.adapter.AdapterHome;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.net.URI;
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

    List<ModelHome> mItems;
    RecyclerView mRecyclerview;
    RecyclerView.LayoutManager mManager;
    RecyclerView.Adapter mAdapter;
    ProgressDialog pd;

    public BtmmenuHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.content_home, container, false);

        queue = Volley.newRequestQueue(getActivity());

        swipeHome = view.findViewById(R.id.swipeHome);

        swipeHome.setColorSchemeResources(R.color.red, R.color.red2);

        swipeHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
                if(swipeHome.isRefreshing()) {
                    swipeHome.setRefreshing(false);
                }
            }
        });

        reload();

        return view;
    }

    public void reload(){
        mRecyclerview = (RecyclerView) view.findViewById(R.id.recHome);
        pd = new ProgressDialog(getActivity());
        mItems = new ArrayList<>();

        loadHome();

        mManager = new GridLayoutManager(getActivity(),2);
        mRecyclerview.setLayoutManager(mManager);
        mAdapter = new AdapterHome(getActivity(),mItems);
        mRecyclerview.setAdapter(mAdapter);
    }

    private void loadHome() {
        SessionManager sessionManager = new SessionManager(getActivity());
        final String id_akun = sessionManager.getIdAkun();

        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();

        StringRequest reqData = new StringRequest(Request.Method.POST, Url.API_HOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        Log.d("volley","response : " + response.toString());
                        try {
                            JSONArray j = new JSONArray(response);
                            for(int i = 0 ; i < response.length(); i++)
                            {
                                try {
                                    JSONObject data = j.getJSONObject(i);
                                    ModelHome md = new ModelHome();
                                    md.setIdBarang(data.getString("id_barang"));
                                    md.setNamaBarang(data.getString("nama_barang"));
                                    md.setNamaJenis(data.getString("nama_jenis_barang"));
                                    md.setHargaBarang(data.getString("harga_barang"));
                                    md.setWaktuLelang(data.getString("waktu_lelang"));
                                    md.setGambarBarang(data.getString("nama_gambar_barang"));
                                    mItems.add(md);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Log.d("volley", "error : " + error.getMessage());
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

        queue.add(reqData);
    }

}

