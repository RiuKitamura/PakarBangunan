package com.mhaa98.pakarbangunan;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btn_add;
    Button btn_info;

    ListView mListView;
    ArrayList<Model> mList;
    HistoryListAdapter mAdapter = null;

    final int CAMERA_REQUEST_CODE1 = 1;
    final int CAMERA_REQUEST_CODE2 = 0;

    ImageView imageViewIcon;
    ImageView imgPesan;
    TextView btnTentang;

    public static SQLiteHelper mSQLiteHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_add = findViewById(R.id.add_btn);
        TextView pesan = findViewById(R.id.pesan);
        imgPesan = findViewById(R.id.pesan_img);
        btn_info = findViewById(R.id.ll_info);
        btnTentang = findViewById(R.id.btn_tentang);

        mListView = findViewById(R.id.list1);
        mList = new ArrayList<>();
        mAdapter = new HistoryListAdapter(this, R.layout.history_layout, mList);
        mListView.setAdapter(mAdapter);

        mSQLiteHelper = new SQLiteHelper(this, "kerusakan_bangunan.sqlite", null, 1);
        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS data_bangunan(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nama_bangunan VARCHAR, jumlah_lantai VARCHAR, tahun VARCHAR, alamat_bangunan VARCHAR, " +
                "poto BLOB, nama VARCHAR, alamat VARCHAR, nomor_hp VARCHAR, hasil_diagnosis VARCHAR(3), tingkat_kepercayaan double)");

        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS data_kerusakan(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_bangunan INTEGER, struktur INTEGER, level_kerusakan INTEGER)");

        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS data_gambar(data_g VARCHAR)");

        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS ds_gejala(id_gejala INTEGER, " +
                "nama_gejala VARCHAR)");
        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS ds_level(id_level INTEGER, " +
                "level VARCHAR, kondisi VARCHAR, penaganan VARCHAR)");
        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS ds_rules(level INTEGER, gejala INTEGER, cf DOUBLE)");

        //mSQLiteHelper.getReadableDatabase();
        //GET ALL DATA FROM SQLITE

        boolean terisi = false;
        Cursor curs = mSQLiteHelper.getData("SELECT id_gejala FROM ds_gejala");
        while (curs.moveToNext()){
            terisi = true;
            break;
        }

        if(terisi == false){
            mSQLiteHelper.insertGejala();
            mSQLiteHelper.insertLevel();
            mSQLiteHelper.insertRules();
        }

        Cursor cursor = mSQLiteHelper.getData("SELECT id, nama_bangunan, jumlah_lantai, tahun, alamat_bangunan, " +
                "nama, alamat, nomor_hp, tingkat_kepercayaan FROM data_bangunan ORDER BY id DESC");
        mList.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String nama_b = cursor.getString(1);
            String lantai = cursor.getString(2);
            String thn = cursor.getString(3);
            String alamat_b = cursor.getString(4);
//            String lati = cursor.getString(5);
//            String longi = cursor.getString(6);
            //byte[] poto = cursor.getBlob(7);
            String nama = cursor.getString(5);
            String alamat = cursor.getString(6);
            String nomor = cursor.getString(7);
            double kepercayaan = cursor.getDouble(8);

            mList.add(new Model(id, nama_b, lantai, thn, alamat_b, nama, alamat, nomor,kepercayaan));

        }
        mAdapter.notifyDataSetChanged();
        pesan.setVisibility(View.GONE);
        imgPesan.setVisibility(View.GONE);
        if(mList.size()==0){
            pesan.setVisibility(View.VISIBLE);
            imgPesan.setVisibility(View.VISIBLE);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = mSQLiteHelper.getData("SELECT id, tingkat_kepercayaan FROM data_bangunan ORDER BY id DESC");
                ArrayList<Integer> arrID = new ArrayList<Integer>();
                ArrayList<Double> arrKepercayaan = new ArrayList<Double>();
                while (c.moveToNext()){
                    arrID.add(c.getInt(0));
                    arrKepercayaan.add(c.getDouble(1));
                }

                moveToDiagonis(arrID.get(position));
//                if(arrKepercayaan.get(position)==0) {
//                    moveToDiagonis(arrID.get(position));
//                }
//                else{
//                    moveToHasilDiagnosis(arrID.get(position));
//                }

            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                CharSequence[] items = {"Update", "Delete"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                dialog.setTitle("Pilih sebuah aksi");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            Cursor c = mSQLiteHelper.getData("SELECT id FROM data_bangunan ORDER BY id DESC");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            moveToUpdate(arrID.get(position));

                            //showDialogUpdate(MainActivity.this, arrID.get(position));
                        }
                        if (which == 1){
                            Cursor c = mSQLiteHelper.getData("SELECT id FROM data_bangunan ORDER BY id DESC");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent i = new Intent(MainActivity.this,FormActivity.class);
                startActivity(i);
            }
        });

        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

        btnTentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TentangActivity.class);
                startActivity(intent);
            }
        });
    }


    void moveToDiagonis(final int id){
        Intent i = new Intent(this, DiagnosisActivity.class);
        Bundle bun = new Bundle();
        bun.putInt("id", id);
        i.putExtras(bun);
        startActivity(i);
    }

    void  moveToHasilDiagnosis(final  int id){
        Intent i = new Intent(this, HasilDiagnosisActivity.class);
        Bundle bun = new Bundle();
        bun.putInt("id", id);
        i.putExtras(bun);
        startActivity(i);
    }

    private void showDialogDelete(final int id) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(MainActivity.this);
        dialogDelete.setTitle("Warning!!");
        dialogDelete.setMessage("Apa anda yakin untuk menghapus?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    mSQLiteHelper.deleteData(id);
                    mSQLiteHelper.deleteData2(id);
                    Toast.makeText(MainActivity.this, "Penghapusan berhasil", Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
                catch (Exception e){
                    Log.e("error", e.getMessage());
                }
                updateList();
            }
        });
        dialogDelete.setNegativeButton("Cencel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    public void moveToUpdate(final int position){
        Intent i = new Intent(this, UpdateActivity.class);
        Bundle bun = new Bundle();
        bun.putInt("id", position);
        i.putExtras(bun);
        startActivity(i);
    }


    private void updateList() {
        Cursor cursor = mSQLiteHelper.getData("SELECT id, nama_bangunan, jumlah_lantai, tahun, alamat_bangunan, " +
                "nama, alamat, nomor_hp FROM data_bangunan ORDER BY id DESC");
        mList.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String namaB = cursor.getString(1);
            String lantai = cursor.getString(2);
            String thn = cursor.getString(3);
            String alamatB = cursor.getString(4);
//            String lati = cursor.getString(5);
//            String longi = cursor.getString(6);
//            byte[] image = cursor.getBlob(7);
            String nama = cursor.getString(5);
            String alamat = cursor.getString(6);
            String nomor = cursor.getString(7);
        }
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(resultCode== Activity.RESULT_OK){

            if(requestCode==CAMERA_REQUEST_CODE1){

                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                imageViewIcon.setImageBitmap(bmp);

            }else if(requestCode==CAMERA_REQUEST_CODE2){

                Uri selectedImageUri = data.getData();
                imageViewIcon.setImageURI(selectedImageUri);
            }

        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }



}