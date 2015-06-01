package fingertech.mobileclientgky;

import java.util.Date;
import java.util.Timer;

/**
 * Created by ASUS on 5/29/2015.
 */
public class Pelayanan {

    private String jenis;
    private Date tanggal;
    private String gedung;
    private String kebaktian;
    private Timer waktumulai;
    private Timer waktuselesai;
    private String judul;

    public String getJenis() {
        return jenis;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public String getGedung() {
        return gedung;
    }

    public String getKebaktian() {
        return kebaktian;
    }

    public Timer getWaktumulai() {
        return waktumulai;
    }

    public Timer getWaktuselesai() {
        return waktuselesai;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public void setGedung(String gedung) {
        this.gedung = gedung;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public void setKebaktian(String kebaktian) {
        this.kebaktian = kebaktian;
    }

    public void setWaktumulai(Timer waktumulai) {
        this.waktumulai = waktumulai;
    }

    public void setWaktuselesai(Timer waktuselesai) {
        this.waktuselesai = waktuselesai;
    }
}
