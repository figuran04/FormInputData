package com.example.forminputdata;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "db_mahasiswa";
    private static final int DB_VERSION = 1;
    private static final String TABEL = "mahasiswa";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABEL + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nama TEXT, nim TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABEL);
        onCreate(db);
    }

    public long insertMahasiswa(Mahasiswa m) { // Ubah tipe kembalian ke long
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nama", m.getNama());
        cv.put("nim", m.getNim());
        long result = db.insert(TABEL, null, cv); // Tangkap nilai kembalian
        db.close(); // Penting: tutup database setelah selesai
        return result; // Kembalikan hasilnya
    }

    public int deleteMahasiswa(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete("mahasiswa", "id=?", new String[]{String.valueOf(id)});
        Log.d("DB_DELETE", "Delete mahasiswa id=" + id + " result=" + result);
        return result; // result = jumlah baris terhapus
    }

    public int updateMahasiswa(Mahasiswa m) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nama", m.getNama());
        cv.put("nim", m.getNim());
        int result = db.update("mahasiswa", cv, "id=?", new String[]{String.valueOf(m.getId())});
        Log.d("DB_UPDATE", "Update mahasiswa id=" + m.getId() + " result=" + result);
        return result; // result = jumlah baris yang ter-update
    }


    public ArrayList<Mahasiswa> getAllMahasiswa() {
        ArrayList<Mahasiswa> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM mahasiswa", null);
        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndexOrThrow("id"));
            String nama = c.getString(c.getColumnIndexOrThrow("nama"));
            String nim = c.getString(c.getColumnIndexOrThrow("nim"));
            list.add(new Mahasiswa(id, nama, nim)); // ✅ pastikan id diset!
        }
        c.close();
        return list;
    }

}
