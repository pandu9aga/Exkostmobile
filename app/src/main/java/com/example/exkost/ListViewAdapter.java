package com.example.exkost;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class ListViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<App> appList;
    private ArrayList<App> arraylist;

    ListViewAdapter(Context context, List<App> appList) {
        this.appList = appList;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(appList);
    }

    public class ViewHolder {
        TextView TextTitle, TextSubtitle;
        ImageView ImageIcon;
    }

    @Override
    public int getCount() {
        return appList.size();
    }

    @Override
    public App getItem(int position) {
        return appList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);

            holder.TextTitle = view.findViewById(R.id.text_headline);
            holder.TextSubtitle = view.findViewById(R.id.text_subhead);
            holder.ImageIcon = view.findViewById(R.id.imageList);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.TextTitle.setText(appList.get(position).getTitle());
        holder.TextSubtitle.setText(appList.get(position).getSubtitle());
        holder.ImageIcon.setImageResource(appList.get(position).getIcon());

        return view;
    }

    //Filter
    void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        appList.clear();
        if (charText.length() == 0) {
            appList.addAll(arraylist);
        } else {
            for (App an : arraylist) {
                if (an.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    appList.add(an);
                }
            }
        }
        notifyDataSetChanged();
    }
}