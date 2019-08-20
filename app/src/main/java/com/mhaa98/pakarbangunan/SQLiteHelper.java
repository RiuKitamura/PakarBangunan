package com.mhaa98.pakarbangunan;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLiteHelper extends SQLiteOpenHelper {
    SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    public void queryData (String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String namaB, String lantai, String thn, String alamatB, String lati,
                           String longi, byte[] image, String nama, String alamat, String hp){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "INSERT INTO data_bangunan VALUES(NULL,?,?,?,?,?,?,?,?,?,?,null,null)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,namaB);
        statement.bindString(2,lantai);
        statement.bindString(3,thn);
        statement.bindString(4,alamatB);
        statement.bindString(5,lati);
        statement.bindString(6,longi);
        statement.bindBlob(7,image);
        statement.bindString(8,nama);
        statement.bindString(9,alamat);
        statement.bindString(10,hp);

        statement.executeInsert();

    }

    public void insertGejala(){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO ds_gejala VALUES" +
                "(1,'Total Kolom Rusak Ringan > 0% dan < 20%'), " +
                "(2,'Total Kolom Rusak Ringan >= 20% dan < 50%'), " +
                "(3,'Total Kolom Rusak Ringan > =50%'), " +
                "(4,'Total Kolom Rusak Sedang > 0% dan < 20%'), " +
                "(5,'Total Kolom Rusak Sedang >= 20% dan < 50%'), " +
                "(6,'Total Kolom Rusak Sedang > =50%'), " +
                "(7,'Total Kolom Rusak Berat > 0% dan < 20%'), " +
                "(8,'Total Kolom Rusak Berat >= 20% dan < 50%'), " +
                "(9,'Total Kolom Rusak Berat > =50%'), " +
                "(10,'Total Balok Rusak Ringan > 0% dan < 20%'), " +
                "(11,'Total Balok Rusak Ringan >= 20% dan < 50%'), " +
                "(12,'Total Balok Rusak Ringan > =50%'), " +
                "(13,'Total Balok Rusak Sedang > 0% dan < 20%'), " +
                "(14,'Total Balok Rusak Sedang >= 20% dan < 50%'), " +
                "(15,'Total Balok Rusak Sedang > =50%'), " +
                "(16,'Total Balok Rusak Berat > 0% dan < 20%'), " +
                "(17,'Total Balok Rusak Berat >= 20% dan < 50%'), " +
                "(18,'Total Balok Rusak Berat > =50%'), " +
                "(19,'Total Dinding Rusak Ringan > 0% dan < 20%'), " +
                "(20,'Total Dinding Rusak Ringan >= 20% dan < 50%'), " +
                "(21,'Total Dinding Rusak Ringan > =50%'), " +
                "(22,'Total Dinding Rusak Sedang > 0% dan < 20%'), " +
                "(23,'Total Dinding Rusak Sedang >= 20% dan < 50%'), " +
                "(24,'Total Dinding Rusak Sedang > =50%'), " +
                "(25,'Total Dinding Rusak Berat > 0% dan < 20%'), " +
                "(26,'Total Dinding Rusak Berat >= 20% dan < 50%'), " +
                "(27,'Total Dinding Rusak Berat > =50%')";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.execute();
        database.close();
    }

    public void insertLevel(){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO ds_level VALUES" +
                "(0,'Tidak Rusak', 'Bangunan tidak mengalami kerusakan'), " +
                "(1,'Rusak Ringan', 'Bangunan masih aman unuk dimasuki'), " +
                "(2,'Rusak Sedang', 'Bangunan masih bisa unuk dimasuki tapi harus dilakukan perbaikan'), " +
                "(3,'Rusak Berat', 'Bangunan tidak aman unuk dimasuki')";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.execute();
        database.close();
    }

    public void insertRules(){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO ds_rules VALUES" +
                "(1, 1, 0.30), " +
                "(1, 2, 0.60), " +
                "(2, 2, 0.60), " +
                "(2, 3, 0.80), " +
                "(1, 4, 0.35), " +
                "(2, 4, 0.35), " +
                "(2, 5, 0.65), " +
                "(2, 6, 0.90), " +
                "(2, 7, 0.40), " +
                "(3, 7, 0.40), " +
                "(3, 8, 0.70), " +
                "(3, 9, 0.95), " +
                "(1, 10, 0.30), " +
                "(1, 11, 0.45), " +
                "(2, 11, 0.45), " +
                "(2, 12, 0.65), " +
                "(1, 13, 0.35), " +
                "(2, 13, 0.35), " +
                "(2, 14, 0.60), " +
                "(2, 15, 0.80), " +
                "(2, 16, 0.35), " +
                "(3, 16, 0.35), " +
                "(3, 17, 0.65), " +
                "(3, 18, 0.85), " +
                "(1, 19, 0.30), " +
                "(1, 20, 0.45), " +
                "(1, 21, 0.65), " +
                "(2, 21, 0.65), " +
                "(1, 22, 0.30), " +
                "(1, 23, 0.55), " +
                "(2, 23, 0.55), " +
                "(2, 24, 0.65), " +
                "(1, 25, 0.35), " +
                "(2, 25, 0.35), " +
                "(1, 26, 0.55), " +
                "(2, 26, 0.55), " +
                "(2, 27, 0.68), " +
                "(3, 27, 0.68) ";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.execute();
        database.close();
    }

    public void updateDataLevel(String data, double persen, int id){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE data_bangunan SET hasil_diagnosis = ?, tingkat_kepercayaan = ? WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindString(1,data);
        statement.bindDouble(2, persen);
        statement.bindDouble(3,(double) id);

        statement.execute();
        database.close();

    }

    public void insertDataKerusakan (int id_bangunan, int struktur, int level_kerusakan){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "INSERT INTO data_kerusakan VALUES(NULL,?,?,?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindDouble(1,(double)id_bangunan);
        statement.bindDouble(2,(double)struktur);
        statement.bindDouble(3,(double)level_kerusakan);

        statement.executeInsert();

    }
    public void insertDataGambar (String data){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "INSERT INTO data_gambar VALUES(?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,data);

        statement.executeInsert();
    }

    public void updateData(String namaB, String lantai, String thn, String alamatB, String lati,
                           String longi, byte[] image, String nama, String alamat, String hp, int id){

        SQLiteDatabase database = getWritableDatabase();

        String  sql = "UPDATE data_bangunan SET nama_bangunan = ?, jumlah_lantai = ?, tahun = ?," +
                "alamat_bangunan = ?, latitude = ?, longitude = ?, poto = ?, nama = ?, alamat = ?, " +
                "nomor_hp = ? WHERE id = ?";

        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1,namaB);
        statement.bindString(2,lantai);
        statement.bindString(3,thn);
        statement.bindString(4,alamatB);
        statement.bindString(5,lati);
        statement.bindString(6,longi);
        statement.bindBlob(7,image);
        statement.bindString(8,nama);
        statement.bindString(9,alamat);
        statement.bindString(10,hp);
        statement.bindDouble(11, (double)id);

        statement.execute();
        database.close();

    }

    public void updateDataKerusakan (int level, int id){
        System.out.println("ini dddddiaaa "+level+"  "+id);

        SQLiteDatabase database = getWritableDatabase();

        String  sql = "UPDATE data_kerusakan SET level_kerusakan = ? WHERE id = ?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindDouble(1,(double)level);
        statement.bindDouble(2,(double)id);
        statement.execute();
        database.close();

    }

    public void deleteData(int id){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM data_bangunan WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public void deleteData2(int id){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM data_kerusakan WHERE id_bangunan = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public void deleteDataKerusakan(int id){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM data_kerusakan WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return  database.rawQuery(sql,null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
