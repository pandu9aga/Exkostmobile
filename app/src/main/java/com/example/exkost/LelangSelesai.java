package com.example.exkost;

import android.os.Bundle;

//import android.app.Fragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class LelangSelesai extends Fragment {

    View view;
    Spinner splelangs;
    String[] kategori = {
            "Semua",
            "Lemari",
            "Kasur",
            "Meja",
            "Kursi",
            "Kipas",
            "Jam",
            "Accessories",
            "Elektronik",
            "Mainan",
            "Buku"
    };


    public LelangSelesai() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.lelang_selesai, container, false);

        splelangs = (Spinner) view.findViewById(R.id.spLelangs);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, kategori);

        splelangs.setAdapter(adapter);

        splelangs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(getActivity(), ""+ adapter.getItem(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

}