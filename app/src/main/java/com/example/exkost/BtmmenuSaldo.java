package com.example.exkost;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.example.exkost.Model.ModelLelang;
import com.example.exkost.Model.ModelTopup;
import com.example.exkost.adapter.AdapterLelang;
import com.example.exkost.adapter.AdapterTopup;
import com.google.android.material.snackbar.Snackbar;

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
public class BtmmenuSaldo extends Fragment {

    View rootView;

    SessionManager sessionManager;
    private RequestQueue queue;
    TextView saldoakun;
    LinearLayout frameLayout;
    private SwipeRefreshLayout swipeTop;
    Button btnTopup;
    Spinner sptop;

    List<ModelTopup> mItems;
    RecyclerView mRecyclerview;
    RecyclerView.LayoutManager mManager;
    RecyclerView.Adapter mAdapter;
    ProgressDialog pd;

    String[] kategori = {
            "semua",
            "belum transfer",
            "tunggu konfirmasi",
            "sukses",
            "gagal"
    };

    String key;

    public BtmmenuSaldo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.content_saldo, container, false);



        saldoakun = rootView.findViewById(R.id.nomSaldo);
        frameLayout = rootView.findViewById(R.id.saldoLayout);
        btnTopup = rootView.findViewById(R.id.btnTopup);
        btnTopup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Topup.class);
                startActivity(intent);
            }
        });

        sessionManager = new SessionManager(getActivity());
        queue = Volley.newRequestQueue(getActivity());
        dataAkun();

        swipeTop = rootView.findViewById(R.id.swipeSaldo);
        swipeTop.setColorSchemeResources(R.color.red, R.color.red2);
        swipeTop.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataAkun();
                reload();
                if(swipeTop.isRefreshing()) {
                    swipeTop.setRefreshing(false);
                }
            }
        });

        reload();

        sptop = (Spinner) rootView.findViewById(R.id.spTop);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, kategori);
        sptop.setAdapter(adapter);
        sptop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                key = sptop.getItemAtPosition(i).toString();
                //Toast.makeText(getActivity(), ""+ adapter.getItem(i), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getActivity(), ""+ key, Toast.LENGTH_SHORT).show();
                reload();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return rootView;
    }

    public void reload(){
        mRecyclerview = (RecyclerView) rootView.findViewById(R.id.recSaldo);
        pd = new ProgressDialog(getActivity());
        mItems = new ArrayList<>();

        loadTop();

        mManager = new LinearLayoutManager(getActivity());
        mRecyclerview.setLayoutManager(mManager);
        mAdapter = new AdapterTopup(getActivity(),mItems);
        mRecyclerview.setAdapter(mAdapter);
    }
    private void loadTop() {
        SessionManager sessionManager = new SessionManager(getActivity());
        final String id_akun = sessionManager.getIdAkun();

        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        //pd.show();

        StringRequest reqData = new StringRequest(Request.Method.POST, Url.API_MYTOPUP,
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
                                    ModelTopup md = new ModelTopup();
                                    md.setId(data.getString("id_topup"));
                                    md.setNominal(data.getString("nominal"));
                                    md.setStatus(data.getString("status_topup"));
                                    md.setBukti(data.getString("bukti_transfer"));
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
                if (key.equals("semua")){
                    params.put("keyword", "semua");
                }else if (key.equals("belum transfer")){
                    params.put("keyword", "belum");
                }else if(key.equals("tunggu konfirmasi")){
                    params.put("keyword", "menunggu");
                }else if (key.equals("sukses")){
                    params.put("keyword", "sukses");
                }else if(key.equals("gagal")){
                    params.put("keyword", "gagal");
                }
                params.put("id", id_akun);
                return params;
            }
        };

        queue.add(reqData);
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