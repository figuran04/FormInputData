package com.example.forminputdata;

import java.io.Serializable;

public class Mahasiswa implements Serializable {

    private int id;
    private String nama;
    private String nim;

    public Mahasiswa(int id, String nama, String nim) {
        this.id = id;
        this.nama = nama;
        this.nim = nim;
    }

    public Mahasiswa(String nama, String nim) {
        this.id = -1; // default, nanti akan diperbarui setelah insert
        this.nama = nama;
        this.nim = nim;
    }

    // Getter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    @Override
    public String toString() {
        return nama + " (" + nim + ")";
    }
}
