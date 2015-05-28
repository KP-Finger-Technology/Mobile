package fingertech.mobileclientgky;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by USER on 28/05/2015.
 */
public class NavigationDrawerDataProvider {
    public static LinkedHashMap<String, ArrayList<String>> getDataHashMap() {
        LinkedHashMap<String, ArrayList<String>> parentHashMap = new LinkedHashMap<String, ArrayList<String>>();

        ArrayList<String> berandaList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.berandaArray.length; i++) {
            berandaList.add(NavigationDrawerData.berandaArray[i]);
        }

        ArrayList<String> alkitabList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.alkitabArray.length; i++) {
            berandaList.add(NavigationDrawerData.alkitabArray[i]);
        }

        ArrayList<String> komisiList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.komisiArray.length; i++) {
            komisiList.add(NavigationDrawerData.komisiArray[i]);
        }

        ArrayList<String> pelayananList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.pelayananArray.length; i++) {
            berandaList.add(NavigationDrawerData.pelayananArray[i]);
        }

        ArrayList<String> pembinaanList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.pembinaanArray.length; i++) {
            berandaList.add(NavigationDrawerData.pembinaanArray[i]);
        }

        ArrayList<String> eventsList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.eventsArray.length; i++) {
            berandaList.add(NavigationDrawerData.eventsArray[i]);
        }

        ArrayList<String> tentangKamiList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.tentangKamiArray.length; i++) {
            berandaList.add(NavigationDrawerData.tentangKamiArray[i]);
        }

        ArrayList<String> hubungiKamiList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.hubungiKamiArray.length; i++) {
            berandaList.add(NavigationDrawerData.hubungiKamiArray[i]);
        }

        ArrayList<String> loginList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.loginArray.length; i++) {
            berandaList.add(NavigationDrawerData.loginArray[i]);
        }

        ArrayList<String> registerList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.registerArray.length; i++) {
            berandaList.add(NavigationDrawerData.registerArray[i]);
        }

        ArrayList<String> pengaturanList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.pengaturanArray.length; i++) {
            berandaList.add(NavigationDrawerData.pengaturanArray[i]);
        }

        parentHashMap.put("Beranda", berandaList);
        parentHashMap.put("Alkitab", alkitabList);
        parentHashMap.put("Komisi", komisiList);
        parentHashMap.put("Pelayanan", pelayananList);
        parentHashMap.put("Pembinaan", pembinaanList);
        parentHashMap.put("Events", eventsList);
        parentHashMap.put("Tentang Kami", tentangKamiList);
        parentHashMap.put("Hubungi Kami", hubungiKamiList);
        parentHashMap.put("Login", loginList);
        parentHashMap.put("Register", registerList);
        parentHashMap.put("Pengaturan", pengaturanList);

        return parentHashMap;
    }
}
