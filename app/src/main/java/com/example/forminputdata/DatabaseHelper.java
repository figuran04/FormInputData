package com.example.forminputdata;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
            "nama TEXT, " +
            "nim TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABEL);
        onCreate(db);
    }

    public void insertMahasiswa(Mahasiswa m) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nama", m.getNama());
        cv.put("nim", m.getNim());
        db.insert(TABEL, null, cv);
    }

    public void updateMahasiswa(Mahasiswa m) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nama", m.getNama());
        cv.put("nim", m.getNim());
        db.update(TABEL, cv, "id=?", new String[]{String.valueOf(m.getId())});
    }

    public void deleteMahasiswa(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABEL, "id=?", new String[]{String.valueOf(id)});
    }

    public ArrayList<Mahasiswa> getAllMahasiswa() {
        ArrayList<Mahasiswa> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        @SuppressLint("Recycle") Cursor c = db.rawQuery("SELECT * FROM " + TABEL, null);
        while (c.moveToNext()) {
            Mahasiswa m = new Mahasiswa(
                c.getInt(0),   // id
                c.getString(1), // nama
                c.getString(2)  // nim
            );
            list.add(m);
        }
        return list;
    }

    // Opsional: Dapatkan jumlah data
    public int getCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABEL, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    // Opsional: Cari mahasiswa berdasarkan keyword
    public ArrayList<Mahasiswa> searchMahasiswa(String keyword) {
        ArrayList<Mahasiswa> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABEL + " WHERE nama LIKE ? OR nim LIKE ?",
            new String[]{"%" + keyword + "%", "%" + keyword + "%"});
        while (c.moveToNext()) {
            Mahasiswa m = new Mahasiswa(
                c.getInt(0),
                c.getString(1),
                c.getString(2)
            );
            list.add(m);
        }
        return list;
    }
}
