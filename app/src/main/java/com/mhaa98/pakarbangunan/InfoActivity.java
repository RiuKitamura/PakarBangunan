package com.mhaa98.pakarbangunan;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<ModelGejala> mList;
    private GejalaAdapter mAdapter = null;

    private Button btnInfoRingan, btnInfoSedang, btnInfoBerat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        getSupportActionBar().setTitle("Info");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnInfoRingan = findViewById(R.id.btn_info_ringan);
        btnInfoSedang = findViewById(R.id.btn_info_sedang);
        btnInfoBerat = findViewById(R.id.btn_info_berat);

        mListView = findViewById(R.id.lv_daftar_gejala);
        mList = new ArrayList<>();
        mAdapter = new GejalaAdapter(this, R.layout.list_gejala_layout, mList);
        mListView.setAdapter(mAdapter);

        Cursor cursor = MainActivity.mSQLiteHelper.getData("SELECT nama_gejala FROM ds_gejala");
        mList.clear();
        while (cursor.moveToNext()){
            String nama_gejala = cursor.getString(0);
            mList.add(new ModelGejala(nama_gejala));
        }
        mAdapter.notifyDataSetChanged();

        btnInfoRingan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDialog alert = new ViewDialog();
                alert.showDialog(InfoActivity.this, 1);
            }
        });

        btnInfoSedang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDialog alert = new ViewDialog();
                alert.showDialog(InfoActivity.this, 2);
            }
        });

        btnInfoBerat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDialog alert = new ViewDialog();
                alert.showDialog(InfoActivity.this, 3);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    public class ViewDialog {
        public void showDialog(Activity activity, int id) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.detail_popup);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            TextView txtJudul = dialog.findViewById(R.id.tv_judul_detail);
            TextView txtDetail = dialog.findViewById(R.id.tv_detail);
            TextView txtPenanganan = dialog.findViewById(R.id.tv_penanganan);
            ImageView imgKerusakan = dialog.findViewById(R.id.iv_gambar_kerusakan);
            ImageView imgContohRusak1 = dialog.findViewById(R.id.iv_rusak_1);
            ImageView imgContohRusak2 = dialog.findViewById(R.id.iv_rusak_2);
            ImageView imgContohRusak3 = dialog.findViewById(R.id.iv_rusak_3);

            Cursor cursor = MainActivity.mSQLiteHelper.getData("SELECT * FROM ds_level WHERE id_level="+id);
            while (cursor.moveToNext()){
                txtJudul.setText(cursor.getString(1));
                txtDetail.setText(cursor.getString(2));
                txtPenanganan.setText(cursor.getString(3));
            }
            if (id == 1){
                imgKerusakan.setImageResource(R.drawable.rusak_ringan);
                imgContohRusak1.setImageResource(R.drawable.rr1);
                imgContohRusak2.setImageResource(R.drawable.rr2);
                imgContohRusak3.setImageResource(R.drawable.rr3);
            }
            else if (id == 2) {
                imgKerusakan.setImageResource(R.drawable.rusak_sedang);
                imgContohRusak1.setImageResource(R.drawable.rs1);
                imgContohRusak2.setImageResource(R.drawable.rs2);
                imgContohRusak3.setImageResource(R.drawable.rs3);
            }
            else if (id == 3) {
                imgKerusakan.setImageResource(R.drawable.rusak_berat);
                imgContohRusak1.setImageResource(R.drawable.rb1);
                imgContohRusak2.setImageResource(R.drawable.rb2);
                imgContohRusak3.setImageResource(R.drawable.rb3);
            }


            ImageButton mDialogNo = dialog.findViewById(R.id.imgb_close);
            mDialogNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

    }
}
