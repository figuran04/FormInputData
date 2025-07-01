package com.example.forminputdata;

import android.content.Intent;
import android.os.Bundle;
// import android.widget.Toast; // Hapus baris ini

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar; // Tambahkan ini
import com.google.android.material.textfield.TextInputEditText; // Pastikan ini ada

public class FormActivity extends AppCompatActivity {
    TextInputEditText etNama, etNim; // Menggunakan TextInputEditText
    MaterialButton btnSimpan, btnKembali;
    DatabaseHelper db;
    Mahasiswa m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        // Inisialisasi UI
        etNama = findViewById(R.id.etNama);
        etNim = findViewById(R.id.etNim);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnKembali = findViewById(R.id.btnKembali);
        db = new DatabaseHelper(this);

        // Ambil data jika sedang dalam mode edit
        Intent i = getIntent();
        if (i != null && i.hasExtra("mahasiswa")) {
            m = (Mahasiswa) i.getSerializableExtra("mahasiswa");
            if (m != null) {
                etNama.setText(m.getNama());
                etNim.setText(m.getNim());
            }
        }

        // Aksi tombol Simpan
        btnSimpan.setOnClickListener(v -> {
            String nama = etNama.getText().toString().trim();
            String nim = etNim.getText().toString().trim();

            if (nama.isEmpty()) {
                etNama.setError("Nama tidak boleh kosong!"); // Menampilkan error spesifik pada TextInputEditText
                Snackbar.make(findViewById(R.id.formCoordinatorLayout),
                    "Nama wajib diisi!", Snackbar.LENGTH_SHORT).show();
                return;
            }

            if (nim.isEmpty()) {
                etNim.setError("NIM tidak boleh kosong!"); // Menampilkan error spesifik pada TextInputEditText
                Snackbar.make(findViewById(R.id.formCoordinatorLayout),
                    "NIM wajib diisi!", Snackbar.LENGTH_SHORT).show();
                return;
            }

            // Hapus error jika input sudah tidak kosong
            etNama.setError(null);
            etNim.setError(null);

            if (m == null) {
                long result = db.insertMahasiswa(new Mahasiswa(nama, nim));
                if (result > 0) {
                    Snackbar.make(findViewById(R.id.formCoordinatorLayout),
                        "Data berhasil ditambahkan", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(findViewById(R.id.formCoordinatorLayout),
                        "Gagal menambahkan data", Snackbar.LENGTH_SHORT).show();
                }
            } else {
                m.setNama(nama);
                m.setNim(nim);
                int result = db.updateMahasiswa(m);
                if (result > 0) {
                    Snackbar.make(findViewById(R.id.formCoordinatorLayout),
                        "Data berhasil diperbarui", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(findViewById(R.id.formCoordinatorLayout),
                        "Gagal memperbarui data", Snackbar.LENGTH_SHORT).show();
                }
            }
            finish();
        });

        // Aksi tombol Kembali
        btnKembali.setOnClickListener(v -> finish());
    }
}