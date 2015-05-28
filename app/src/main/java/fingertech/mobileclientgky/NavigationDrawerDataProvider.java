package fingertech.mobileclientgky;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by USER on 28/05/2015.
 */
public class NavigationDrawerDataProvider {
    public static HashMap<String, List<String>> getDataHashMap() {
        HashMap<String, List<String>> parentHashMap = new HashMap<String, List<String>>();

        List<String> berandaList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.berandaArray.length; i++) {
            berandaList.add(NavigationDrawerData.berandaArray[i]);
        }
        parentHashMap.put("Beranda", berandaList);

        List<String> pelayananList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.pelayananArray.length; i++) {
            berandaList.add(NavigationDrawerData.pelayananArray[i]);
        }
        parentHashMap.put("Pelayanan", pelayananList);

        List<String> alkitabList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.alkitabArray.length; i++) {
            berandaList.add(NavigationDrawerData.alkitabArray[i]);
        }
        parentHashMap.put("Alkitab", alkitabList);

        List<String> komisiList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.komisiArray.length; i++) {
            komisiList.add(NavigationDrawerData.komisiArray[i]);
        }
        parentHashMap.put("Komisi", komisiList);

        List<String> pembinaanList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.pembinaanArray.length; i++) {
            berandaList.add(NavigationDrawerData.pembinaanArray[i]);
        }
        parentHashMap.put("Pembinaan", pembinaanList);

        List<String> eventsList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.eventsArray.length; i++) {
            berandaList.add(NavigationDrawerData.eventsArray[i]);
        }
        parentHashMap.put("Events", eventsList);

        List<String> tentangKamiList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.tentangKamiArray.length; i++) {
            berandaList.add(NavigationDrawerData.tentangKamiArray[i]);
        }
        parentHashMap.put("Tentang Kami", tentangKamiList);

        List<String> hubungiKamiList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.hubungiKamiArray.length; i++) {
            berandaList.add(NavigationDrawerData.hubungiKamiArray[i]);
        }
        parentHashMap.put("Hubungi Kami", hubungiKamiList);

        List<String> loginList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.loginArray.length; i++) {
            berandaList.add(NavigationDrawerData.loginArray[i]);
        }
        parentHashMap.put("Login", loginList);

        List<String> registerList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.registerArray.length; i++) {
            berandaList.add(NavigationDrawerData.registerArray[i]);
        }
        parentHashMap.put("Register", registerList);

        List<String> pengaturanList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.pengaturanArray.length; i++) {
            berandaList.add(NavigationDrawerData.pengaturanArray[i]);
        }
        parentHashMap.put("Pengaturan", pengaturanList);

        return parentHashMap;
    }
}
