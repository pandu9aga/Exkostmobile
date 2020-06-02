package com.example.exkost.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exkost.BarangView;
import com.example.exkost.Model.ModelHome;
import com.example.exkost.R;

import java.util.List;

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.HolderData> {
    private List<ModelHome> mItems ;
    private Context context;

    public AdapterHome (Context context, List<ModelHome> items)
    {
        this.mItems = items;
        this.context = context;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_home,parent,false);
        HolderData holderData = new HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(HolderData holder, int position) {
        ModelHome md  = mItems.get(position);
        holder.namaBarang.setText(md.getNamaBarang());
        holder.namaJenis.setText(md.getNamaJenis());
        holder.hargaBarang.setText(md.getHargaBarang());
        holder.waktuLelang.setText(md.getWaktuLelang());

        holder.md = md;


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class HolderData extends RecyclerView.ViewHolder
    {
        TextView namaBarang, namaJenis, hargaBarang, waktuLelang;
        Button toBarang;
        String idBarang;
        ModelHome md;

        public  HolderData (View view)
        {
            super(view);

            namaBarang = (TextView) view.findViewById(R.id.namaBarang);
            namaJenis = (TextView) view.findViewById(R.id.namaJenis);
            hargaBarang = (TextView) view.findViewById(R.id.hargaBarang);
            waktuLelang = (TextView) view.findViewById(R.id.waktuLelang);

            toBarang = (Button) view.findViewById(R.id.toBarang);

            toBarang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent update = new Intent(context, BarangView.class);
                    update.putExtra("id_barang",md.getIdBarang());

                    context.startActivity(update);
                }
            });
        }
    }
}
