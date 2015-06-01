package fingertech.mobileclientgky;

import java.util.Date;

/**
 * Created by ASUS on 5/29/2015.
 */
public class Renungan {
    private String judul;
    private Date tanggal;
    private int idAlkitab;
    private String gambar;
    private String deskripsi;

    public String getJudul() {
        return judul;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public int getIdAlkitab() {
        return idAlkitab;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getGambar() {
        return gambar;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public void setIdAlkitab(int idAlkitab) {
        this.idAlkitab = idAlkitab;
    }
}
