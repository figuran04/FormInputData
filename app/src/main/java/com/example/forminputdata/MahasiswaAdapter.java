package com.example.forminputdata;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MahasiswaAdapter extends BaseAdapter {
    private final Activity activity;
    private final ArrayList<Mahasiswa> originalList; // data asli
    private ArrayList<Mahasiswa> filteredList;       // data yang ditampilkan

    public MahasiswaAdapter(Activity activity, ArrayList<Mahasiswa> list) {
        this.activity = activity;
        this.originalList = new ArrayList<>(list); // salin data awal
        this.filteredList = new ArrayList<>(list);
    }

    public void filter(String keyword) {
        keyword = keyword.toLowerCase();
        filteredList.clear();
        if (keyword.isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            for (Mahasiswa m : originalList) {
                if (m.getNama().toLowerCase().contains(keyword) ||
                    m.getNim().toLowerCase().contains(keyword)) {
                    filteredList.add(m);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return filteredList.get(position).getId();
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(activity).inflate(android.R.layout.simple_list_item_2, parent, false);
        TextView tv1 = view.findViewById(android.R.id.text1);
        TextView tv2 = view.findViewById(android.R.id.text2);

        Mahasiswa m = filteredList.get(position); // ✅ langsung akses list
        tv1.setText(m.getNama());
        tv2.setText("NIM: " + m.getNim());
        return view;
    }
}
