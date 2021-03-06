package com.example.exkost;

import android.app.ProgressDialog;
import android.os.Bundle;

//import android.app.Fragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.exkost.Api.Url;
import com.example.exkost.Model.ModelLelang;
import com.example.exkost.adapter.AdapterLelang;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class LelangBerlangsung extends Fragment {

    View view;
    Spinner splelangb;
    private SwipeRefreshLayout swipeLb;
    SessionManager sessionManager;
    private RequestQueue queue;

    String kategori;

    List<ModelLelang> mItems;
    RecyclerView mRecyclerview;
    RecyclerView.LayoutManager mManager;
    RecyclerView.Adapter mAdapter;
    ProgressDialog pd;

    public LelangBerlangsung() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.lelang_layout, container, false);

        queue = Volley.newRequestQueue(getActivity());

        swipeLb = view.findViewById(R.id.swipeL);
        swipeLb.setColorSchemeResources(R.color.red, R.color.red2);
        swipeLb.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
                if(swipeLb.isRefreshing()) {
                    swipeLb.setRefreshing(false);
                }
            }
        });

        reload();

        splelangb = (Spinner) view.findViewById(R.id.spLelang);
        addItemsOnSpinner();

        return view;
    }

    ArrayList<String> list;

    public void addItemsOnSpinner() {
        list = new ArrayList<String>();
        list.add("semua");

        StringRequest reqData = new StringRequest(Request.Method.GET, Url.API_JENIS,
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
                        pd.cancel();
                        Log.d("volley", "error : " + error.getMessage());
                    }
                });
        queue.add(reqData);
    }

    private void setSpinnerAdapter(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        splelangb.setAdapter(adapter);
        splelangb.setSelection(0);
        splelangb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                kategori = splelangb.getItemAtPosition(i).toString();
                //Toast.makeText(getActivity(), ""+ kategori, Toast.LENGTH_SHORT).show();
                reload();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void reload(){
        mRecyclerview = (RecyclerView) view.findViewById(R.id.recL);
        pd = new ProgressDialog(getActivity());
        mItems = new ArrayList<>();

        loadLb();

        mManager = new GridLayoutManager(getActivity(),2);
        mRecyclerview.setLayoutManager(mManager);
        mAdapter = new AdapterLelang(getActivity(),mItems);
        mRecyclerview.setAdapter(mAdapter);
    }

    private void loadLb() {
        SessionManager sessionManager = new SessionManager(getActivity());
        final String id_akun = sessionManager.getIdAkun();

        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        //pd.show();

        StringRequest reqData = new StringRequest(Request.Method.POST, Url.AUC_BERLANGSUNG,
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
                                    ModelLelang md = new ModelLelang();
                                    md.setIdBarang(data.getString("id_barang"));
                                    md.setNamaBarang(data.getString("nama_barang"));
                                    md.setNamaJenis(data.getString("nama_jenis_barang"));
                                    md.setHargaBarang(data.getString("harga_barang"));
                                    md.setWaktuLelang(data.getString("waktu_lelang"));
                                    md.setGambarBarang(data.getString("nama_gambar_barang"));
                                    md.setStatBid(data.getString("status_lelang"));
                                    md.setStatTrans(data.getString("status_transfer"));
                                    md.setStatFail(data.getString("status_gagal"));
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
                        //pd.cancel();
                        Log.d("volley", "error : " + error.getMessage());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id_akun);
                params.put("keyword",kategori);
                return params;
            }
        };

        queue.add(reqData);
    }



}