package com.mhaa98.pakarbangunan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GejalaAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<ModelGejala> gejalas;

    public GejalaAdapter(Context context, int layout, ArrayList<ModelGejala> gejalas) {
        this.context = context;
        this.layout = layout;
        this.gejalas = gejalas;
    }

    @Override
    public int getCount() {
        return gejalas.size();
    }

    @Override
    public Object getItem(int position) {
        return gejalas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder{
        TextView textGejala;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if (row == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);
            holder.textGejala= row.findViewById(R.id.tv_daftar_gejala);
            row.setTag(holder);
        }
        else {
            holder = (GejalaAdapter.ViewHolder)row.getTag();
        }

        ModelGejala model = gejalas.get(position);

        holder.textGejala.setText(model.getGejala());

        return row;
    }
}
