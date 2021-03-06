package fingertech.mobileclientgky;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParsePush;
import com.parse.SaveCallback;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.Date;
import java.util.TimerTask;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;


/**
 * Created by Rita on 5/27/2015.
 */
public class Controller {
    public static final String basicurl = "http://finger-technology.com/";
    public static final String url = basicurl + "gky/";
    public static final String urlgambar = url + "gereja/assets/images/";
    public static final String urlaudio = url + "gereja/video/";

    private JSONArray arrData = new JSONArray();
    private String writeResponse = null;
    private Context context;

    public Controller() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public Controller(Context _context){
        this.context = _context;
    }

    public JSONArray getArrData(){
        return arrData;
    }

    // Untuk mengecek apakah suatu array kosong atau tidak
    // Mengembalikan true jika kosong dan false jika tidak
    public boolean isArrEmpty(){
        if(arrData.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean viewEvent() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            Viewer v = new Viewer();
            @Override
            public void run() {
                v.execute(url + "view_event.php");
                while(isArrEmpty()) {}
            }
        });
        return true;
    }

    public boolean viewJadwalPelayanan() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            Viewer v = new Viewer();
            @Override
            public void run() {
                v.execute(url + "view_jadwalpelayanan.php");
                while(isArrEmpty()){}
            }
        });
        return true;
    }

    public void addDoa(final String nama, final int umur, final String email , final String tlp , final String jk , final String isiDoa) {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Writer().execute(url + "add_doa.php?nama=" + nama + "&umur=" + umur + "&email=" + email + "&nomortelepon=" + tlp + "&jeniskelamin=" + jk + "&doa=" + isiDoa);
            }
        });

        // Mengirimkan isi form berupa email kepada anggota gereja
		try {
            GMailSender sender = new GMailSender("pt.gaia.persada@gmail.com", "gaia0913");
            sender.sendMail("Permohonan Doa",
                    "Dari: " + nama + " dengan umur " + umur + " dan jenis kelamin " + jk + " tahun" + "\nEmail: " + email + "\nTelepon: " + tlp + "\nIsi doa: " + isiDoa,
                    "pt.gaia.persada@gmail.com",
                    "clickandbuykohana@gmail.com, parkmonitoringsystem@gmail.com, pt.gaia.persada@gmail.com");
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }
    }

    public void register(final String nama, final String password ,final String email , final String tlp , final String alamat , final String tgllahir, final String idbaptis, final String komisi ,final String pelayanan ) {
        // Post to server
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Writer().execute(url + "register.php?nama=" + nama + "&pass=" + password + "&email=" + email + "&no=" + tlp + "&alamat=" + alamat + "&idbaptis=" + idbaptis + "&komisi=" + komisi + "&pel=" + pelayanan + "&tgl=" + tgllahir);
            }
        });
    }

    public void editprofil(final String nama, final String email , final String tlp , final String alamat ,final String idbaptis, final String komisi ,final String pelayanan ) {
        // Post to server
        SessionManager sm = new SessionManager(context);
        final String id = sm.pref.getAll().get("id").toString();
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Writer().execute(url + "edit_profil.php?nama=" + nama + "&email=" + email + "&no=" + tlp + "&alamat=" + alamat + "&idbaptis=" + idbaptis + "&komisi=" + komisi + "&pel=" + pelayanan + "&id=" + id);
            }
        });
    }

    public void editPass(final String pass) {
        // Post to server
        SessionManager sm = new SessionManager(context);
        final String id = sm.pref.getAll().get("id").toString();
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Writer().execute(url + "edit_pass.php?pass=" + pass + "&id=" + id);
            }
        });
    }

    public void login (final String email, final String password ) {
        Writer w = new Writer();
        w.execute(url + "login.php?email=" + email + "&password=" + password);
    }

    // Untuk mengecek apakah ada koneksi internet
    public boolean isNetworkAvailable() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    class Viewer extends AsyncTask<String, String, String> {
        JSONArray arr = new JSONArray();

        public JSONArray getArr() {
            return arr;
        }

        @Override
        protected void onPreExecute() {}

        protected void onPostExecute(Long result) {
            try {
                for (int i = 0; i < 2; i++) {
                    JSONObject judulobj = arr.getJSONObject(i);
                    String judulx = judulobj.getString("judul");
                    // Use the same for remaining values
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            if(isNetworkAvailable()) {
                String result = "";
                for (String urlp : params) {
                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet(urlp);
                    HttpResponse response;

                    try {
                        response = client.execute(request);

                        // Get the response
                        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                        String line = "";
                        while ((line = rd.readLine()) != null) {
                            result += line;
                        }

                        try {
                            JSONObject res = new JSONObject(result);
                            arrData = res.getJSONArray("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Toast.makeText(context, "Anda tidak terhubung ke internet", Toast.LENGTH_SHORT).show();
            }

            return "";
        }
    }

    public class Writer extends AsyncTask<String, Void, JSONObject> {
        private JSONObject result = null;

        @Override
        protected JSONObject doInBackground(String... params) {
            if(isNetworkAvailable()) {
                String sendMessage;
                String urlpost = null;
                for (String urlp : params) {
                    urlpost = urlp;
                }

                try {
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpPost httpPostRequest = new HttpPost(urlpost);
                    JSONObject sendObject = new JSONObject();

                    sendMessage = sendObject.toString();

                    StringEntity se;
                    se = new StringEntity(sendMessage);

                    // Set HTTP parameters
                    httpPostRequest.setEntity(se);
                    httpPostRequest.setHeader("Accept", "application/json");
                    httpPostRequest.setHeader("Content-type", "application/json");

                    long t = System.currentTimeMillis();
                    HttpResponse response = (HttpResponse) httpclient.execute(httpPostRequest);

                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        InputStream instream = entity.getContent();

                        String resultString = convertStreamToString(instream);
                        instream.close();

                        result = new JSONObject(resultString);
                        writeResponse = result.getString("status");

                        return result;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if(isNetworkAvailable()) {
                String operation = null;
                try {
                    operation = result.getString("operation");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (operation.equals("login")) {

                    String nama = null, id = null, email = null, alamat = null, telepon = null, idbaptis = null, tgllahir = null, komisi = null, pelayanan = null, pass = null, namakomisi = null;
                    try {
                        nama = result.getString("nama");
                        pass = result.getString("pass");
                        id = result.getString("id");
                        email = result.getString("email");
                        alamat = result.getString("alamat");
                        telepon = result.getString("telepon");
                        idbaptis = result.getString("idbaptis");
                        tgllahir = result.getString("tgllahir");
                        komisi = result.getString("komisi");
                        pelayanan = result.getString("pelayanan");
                        namakomisi = result.getString("namakomisi");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    
					if (writeResponse.equals("ok")) {
                        SessionManager smn = new SessionManager(context);
                        smn.createLoginSession(nama, pass, id, email, alamat, telepon, idbaptis, tgllahir, komisi, pelayanan, namakomisi);
                        try {
                            JSONArray arrKomisi = new JSONArray(smn.pref.getAll().get("namakomisi").toString());

                            // Mendaftarkan komisi pilihan user untuk push notification
							for (int i = 0; i < arrKomisi.length(); i++) {
                                ParsePush.subscribeInBackground(arrKomisi.get(i).toString().replace(" ", "").replace("&", ""), new SaveCallback() {
                                    @Override
                                    public void done(com.parse.ParseException e) {
                                        if (e == null) {
                                            Log.d("com.parse.push", "successfully subscribed to the komisi channel.");
                                        } else {
                                            Log.e("com.parse.push", "failed to subscribe for push to the komisi", e);
                                        }
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(context, "Login success", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Login " + writeResponse, Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Operation.eq("add doa") , edit profil , register
                    if (writeResponse.equals("ok")) {
                        Toast.makeText(context, operation + " Success", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, operation + "" + writeResponse, Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(context, "Anda tidak terhubung ke internet", Toast.LENGTH_SHORT).show();
            }
        }

        private String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }
    }
}
