package com.example.forminputdata;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FormActivity extends AppCompatActivity {
    EditText etNama, etNim;
    Button btnSimpan;
    DatabaseHelper db;
    Mahasiswa m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        etNama = findViewById(R.id.etNama);
        etNim = findViewById(R.id.etNim);
        btnSimpan = findViewById(R.id.btnSimpan);
        db = new DatabaseHelper(this);

        // Cek apakah FormActivity menerima data mahasiswa untuk edit
        Intent i = getIntent();
        if (i != null && i.hasExtra("mahasiswa")) {
            m = (Mahasiswa) i.getSerializableExtra("mahasiswa");
            if (m != null) {
                etNama.setText(m.getNama());
                etNim.setText(m.getNim());
            }
        }

        btnSimpan.setOnClickListener(v -> {
            String nama = etNama.getText().toString().trim();
            String nim = etNim.getText().toString().trim();

            if (nama.isEmpty() || nim.isEmpty()) {
                Toast.makeText(this, "Nama dan NIM tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (m == null) {
                db.insertMahasiswa(new Mahasiswa(nama, nim));
                Toast.makeText(this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            } else {
                m.setNama(nama);
                m.setNim(nim);
                db.updateMahasiswa(m);
                Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
            }

            finish(); // kembali ke MainActivity
        });
    }
}
