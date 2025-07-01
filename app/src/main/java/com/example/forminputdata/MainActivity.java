package com.example.forminputdata;

import android.content.Intent;
import android.graphics.Color; // Although imported, Color is not used in the provided snippet.
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText; // Although imported, EditText is not used in the provided snippet.
import android.widget.ListView;
import android.widget.TextView;
// import android.widget.Toast; // REMOVED: No longer needed

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar; // Ensure this is imported

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    FloatingActionButton fab;
    DatabaseHelper db;
    ArrayList<Mahasiswa> list;
    MahasiswaAdapter adapter;
    TextView tvHasil;
    SearchView searchView;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onResume() {
        super.onResume();
        resetSearch();
        loadData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        // Open drawer when menu button is pressed
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        listView = findViewById(R.id.listView);
        fab = findViewById(R.id.fab);
        db = new DatabaseHelper(this);
        tvHasil = findViewById(R.id.tvHasil);

        fab.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FormActivity.class));
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Mahasiswa mhs = (Mahasiswa) adapter.getItem(position);
            Intent i = new Intent(MainActivity.this, FormActivity.class);
            i.putExtra("mahasiswa", mhs);
            startActivity(i);
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Mahasiswa selected = (Mahasiswa) adapter.getItem(position);

            int idToDelete = selected.getId();
            Log.d("HAPUS", "ID yang akan dihapus: " + idToDelete);

            new AlertDialog.Builder(MainActivity.this)
                .setTitle("Hapus Data")
                .setMessage("Yakin ingin menghapus " + selected.getNama() + "?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    int result = db.deleteMahasiswa(idToDelete);
                    if (result > 0) {
                        Snackbar.make(findViewById(R.id.coordinatorLayout),
                            "Data berhasil dihapus", Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(findViewById(R.id.coordinatorLayout),
                            "Gagal menghapus data", Snackbar.LENGTH_SHORT).show();
                    }
                    resetSearch();
                    loadData();
                })
                .setNegativeButton("Batal", null)
                .show();
            return true;
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // This was already Snackbar, no change needed.
                Snackbar.make(drawerLayout, "Beranda diklik", Snackbar.LENGTH_SHORT).show();
            } else if (id == R.id.nav_keluar) {
                new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Konfirmasi")
                    .setMessage("Apakah Anda yakin ingin keluar?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        finish(); // exit the application
                    })
                    .setNegativeButton("Batal", null)
                    .show();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        loadData();
    }

    void loadData() {
        list = db.getAllMahasiswa();

        if (adapter == null) {
            adapter = new MahasiswaAdapter(this, list);
            listView.setAdapter(adapter);
        } else {
            adapter.updateData(list);
        }

        tvHasil.setText("Jumlah data: " + list.size());

        if (list.isEmpty()) {
            // This was already Snackbar, no change needed.
            Snackbar.make(findViewById(R.id.coordinatorLayout), "Belum ada data", Snackbar.LENGTH_SHORT).show();
        }
    }

    void resetSearch() {
        if (searchView != null) {
            searchView.setQuery("", false);
            searchView.clearFocus();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();

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

    @SuppressWarnings("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(MainActivity.this)
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin keluar?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    finish(); // exit
                })
                .setNegativeButton("Batal", null)
                .show();
        }
    }
}