package com.example.forminputdata;

import java.io.Serializable;

public class Mahasiswa implements Serializable {
    private int id;
    private String nama, nim;

    public Mahasiswa(int id, String nama, String nim) {
        this.id = id;
        this.nama = nama;
        this.nim = nim;
    }

    public Mahasiswa(String nama, String nim) {
        this.nama = nama;
        this.nim = nim;
    }

    // Getter
    public int getId() { return id; }
    public String getNama() { return nama; }
    public String getNim() { return nim; }

    // Setter
    public void setId(int id) { this.id = id; }
    public void setNama(String nama) { this.nama = nama; }
    public void setNim(String nim) { this.nim = nim; }

    @Override
    public String toString() {
        return nama + " (" + nim + ")";
    }
}
