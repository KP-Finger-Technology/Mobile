package fingertech.mobileclientgky;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by ASUS on 6/15/2015.
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
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ALAMAT = "alamat";
    public static final String KEY_TELEPON = "telepon";

    // Email address (make variable public to access from outside)
    public static final String KEY_ID = "id";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        boolean login = pref.getBoolean(IS_LOGIN, false);

        if(login != true) {
            editor.putBoolean(IS_LOGIN, false);
            editor.commit();
        }
    }

    public void createLoginSession(String name ,String id,String email , String alamat, String telepon){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ALAMAT, alamat);
        editor.putString(KEY_TELEPON, telepon);

        // Storing email in pref
        editor.putString(KEY_ID, id);

        // commit changes
        editor.commit();

       Log.d("Preferen",pref.getAll().toString());
    }
    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_ID, pref.getString(KEY_ID, null));

        // return user
        return user;
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.putBoolean(IS_LOGIN, false);

        editor.commit();
    }
}
