package fingertech.mobileclientgky;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedHashMap;


/**
 * Created by William Stefan Hartono
 */
public class NavigationDrawerDataProvider {

    private static Context context;

    public NavigationDrawerDataProvider(Context _context) {
        this.context = _context;
    }

    public static LinkedHashMap<String, ArrayList<String>> getDataHashMap(boolean isLogin) {
        LinkedHashMap<String, ArrayList<String>> parentHashMap = new LinkedHashMap<String, ArrayList<String>>();

        ArrayList<String> berandaList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.berandaArray.length; i++) {
            berandaList.add(NavigationDrawerData.berandaArray[i]);
        }

        ArrayList<String> alkitabList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.alkitabArray.length; i++) {
            alkitabList.add(NavigationDrawerData.alkitabArray[i]);
        }

        ArrayList<String> komisiList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.komisiArray.length; i++) {
            komisiList.add(NavigationDrawerData.komisiArray[i]);
        }

        ArrayList<String> pelayananList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.pelayananArray.length; i++) {
            pelayananList.add(NavigationDrawerData.pelayananArray[i]);
        }

        ArrayList<String> pembinaanList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.pembinaanArray.length; i++) {
            pembinaanList.add(NavigationDrawerData.pembinaanArray[i]);
        }

        ArrayList<String> eventsList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.eventsArray.length; i++) {
            eventsList.add(NavigationDrawerData.eventsArray[i]);
        }

        ArrayList<String> tentangKamiList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.tentangKamiArray.length; i++) {
            tentangKamiList.add(NavigationDrawerData.tentangKamiArray[i]);
        }

        ArrayList<String> hubungiKamiList = new ArrayList<String>();
        for (int i = 0; i < NavigationDrawerData.hubungiKamiArray.length; i++) {
            hubungiKamiList.add(NavigationDrawerData.hubungiKamiArray[i]);
        }

        ArrayList<String> loginList = new ArrayList<String>();
        ArrayList<String> registerList = new ArrayList<String>();
        ArrayList<String> sesudahLoginList = new ArrayList<String>();
        if (!isLogin) {
            for (int i = 0; i < NavigationDrawerData.loginArray.length; i++) {
                loginList.add(NavigationDrawerData.loginArray[i]);
            }
            for (int i = 0; i < NavigationDrawerData.registerArray.length; i++) {
                registerList.add(NavigationDrawerData.registerArray[i]);
            }
        }
        else {
            for (int i = 0; i < NavigationDrawerData.kontenLoginArray.length; i++) {
                sesudahLoginList.add(NavigationDrawerData.kontenLoginArray[i]);
            }
        }

        parentHashMap.put(" Beranda", berandaList);             // position 0
        parentHashMap.put(" Alkitab", alkitabList);             // position 1
        parentHashMap.put(" Komisi", komisiList);               // position 2
        parentHashMap.put(" Pelayanan", pelayananList);         // position 3
        parentHashMap.put(" Pembinaan", pembinaanList);         // position 4
        parentHashMap.put(" Events", eventsList);               // position 5
        parentHashMap.put(" Tentang Kami", tentangKamiList);    // position 6
        parentHashMap.put(" Hubungi Kami", hubungiKamiList);    // position 7
        if (!isLogin) {
            parentHashMap.put(" Login", loginList);             // position 8
            parentHashMap.put(" Register", registerList);       // position 9
        }
        else {
            parentHashMap.put(" Manajemen Konten", sesudahLoginList);   // position 8
        }

        return parentHashMap;
    }
}