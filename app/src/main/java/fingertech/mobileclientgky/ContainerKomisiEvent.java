package fingertech.mobileclientgky;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Andarias Silvanus on 15/07/01.
 */
public class ContainerKomisiEvent {
    private ArrayList<JSONObject> json;
    private String namaKomisi;

    // Konstruktor
    public ContainerKomisiEvent() {
        namaKomisi = "";
        json = new ArrayList<JSONObject>();
    }

    public void setNamaKomisi (String _namaKomisi) {
        this.namaKomisi = _namaKomisi;
    }

    public void setJSON (JSONObject _json) {
        json.add(_json);
    }

    public String getNamaKomisi () {
        return this.namaKomisi;
    }

    public ArrayList<JSONObject> getJSON () {
        return json;
    }

    public String printArrayJSON() {
        String res = "";
        for (int i=0; i<json.size(); i++) {
            res = res + json.get(i).toString()+"\n";
        }
        return res;
    }
}
