package fingertech.mobileclientgky;

import java.util.Date;

/**
 * Created by ASUS on 5/29/2015.
 */
public class Event {
    private String judul;
    private Date tanggal;
    private String keterangan;
    private String gambar;

    public String getJudul() {
        return judul;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getGambar() {
        return gambar;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

}
