package com.example.forminputdata;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    FloatingActionButton fab;
    DatabaseHelper db;
    ArrayList<Mahasiswa> list;
    MahasiswaAdapter adapter;
    TextView tvHasil;

    @Override
    protected void onResume() {
        super.onResume();
        loadData(); // Refresh data setiap kembali dari FormActivity
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        fab = findViewById(R.id.fab);
        db = new DatabaseHelper(this);
        tvHasil = findViewById(R.id.tvHasil);

        // Tombol tambah data
        fab.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FormActivity.class));
        });

        // Klik item untuk edit data
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(MainActivity.this, FormActivity.class);
            i.putExtra("mahasiswa", (Mahasiswa) adapter.getItem(position)); // pastikan Mahasiswa implement Serializable
            startActivity(i);
        });

        // Klik lama untuk hapus data
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(MainActivity.this)
                .setTitle("Hapus Data")
                .setMessage("Yakin ingin menghapus?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    int idToDelete = ((Mahasiswa) adapter.getItem(position)).getId();
                    db.deleteMahasiswa(idToDelete);
                    loadData(); // Refresh setelah hapus
                    Snackbar.make(findViewById(R.id.coordinatorLayout), "Data berhasil dihapus", Snackbar.LENGTH_SHORT).show();
                })
                .setNegativeButton("Batal", null)
                .show();
            return true;
        });

        // Load data awal
        loadData();
    }

    void loadData() {
        list = db.getAllMahasiswa();
        adapter = new MahasiswaAdapter(this, list);
        listView.setAdapter(adapter);
        tvHasil.setText("Jumlah data: " + list.size());

        if (list.isEmpty()) {
            Snackbar.make(findViewById(R.id.coordinatorLayout), "Belum ada data", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        if (searchView != null) {
            searchView.setQueryHint("Cari nama/NIM...");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    if (adapter != null) {
                        adapter.filter(newText);
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }
            });
        }
        return true;
    }

}
