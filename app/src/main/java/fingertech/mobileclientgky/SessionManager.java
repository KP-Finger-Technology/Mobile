package fingertech.mobileclientgky;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;


/**
 * Created by Rita Sarah
 */
public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    public static final String KEY_PASS = "pass";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ALAMAT = "alamat";
    public static final String KEY_TELEPON = "telepon";
    public static final String KEY_IDBAPTIS = "idbaptis";
    public static final String KEY_TGL = "tgllahir";
    public static final String KEY_KOMISI = "komisi";
    public static final String KEY_PELAYANAN = "pelayanan";
    public static final String KEY_NKOMISI = "namakomisi";

    // Email address (make variable public to access from outside)
    public static final String KEY_ID = "id";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        boolean login = pref.getBoolean(IS_LOGIN, false);

        if(login != true) {
            editor.putBoolean(IS_LOGIN, false);
            editor.commit();
        }
    }

    public void createLoginSession(String name, String pass, String id, String email, String alamat, String telepon, String idbaptis, String tgllahir, String komisi, String pelayanan,String namakomisi) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_PASS, pass);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ALAMAT, alamat);
        editor.putString(KEY_TELEPON, telepon);
        editor.putString(KEY_IDBAPTIS, idbaptis);
        editor.putString(KEY_TGL, tgllahir);
        editor.putString(KEY_KOMISI, komisi);
        editor.putString(KEY_PELAYANAN, pelayanan);
        editor.putString(KEY_NKOMISI, namakomisi);

        // Storing email in pref
        editor.putString(KEY_ID, id);

        // Commit changes
        editor.commit();
    }

    public void editLoginSession(String name, String email, String alamat, String telepon, String idbaptis, String komisi, String pelayanan) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ALAMAT, alamat);
        editor.putString(KEY_TELEPON, telepon);
        editor.putString(KEY_IDBAPTIS, idbaptis);
        editor.putString(KEY_KOMISI, komisi);
        editor.putString(KEY_PELAYANAN, pelayanan);

        // Commit changes
        editor.commit();
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // User name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // User email id
        user.put(KEY_ID, pref.getString(KEY_ID, null));

        // Return user
        return user;
    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();editor.commit();
    }
}
