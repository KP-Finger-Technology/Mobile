package fingertech.mobileclientgky;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

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

/**
 * Created by Rita on 5/27/2015.
 */
public class Controller {
    public static final String url = "http://192.168.0.107/gky_web_service/";

    private JSONArray arrData = new JSONArray();
    private String writeResponse = null;
    private Context context;

    public Controller() {}

    public Controller(Context _context){
        this.context = _context;
    }

    public JSONArray getArrData(){
        return arrData;
    }

    public boolean isArrEmpty(){
        if(arrData.length()==0) {
            return true;
        }
        else {
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
                while(isArrEmpty()){}
            }
        });
        return true;
    }

    public boolean viewJadwalPelayanan(){
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

    public void addDoa(final String nama, final int umur, final String email , final String tlp , final String jk , final String isiDoa){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Writer().execute(url + "add_doa.php?nama=" + nama + "&umur=" + umur + "&email=" + email + "&nomortelepon=" + tlp + "&jeniskelamin=" + jk + "&doa=" + isiDoa);
                Log.d("URL", url + "add_doa.php?nama=" + nama + "&umur=" + umur + "&email=" + email + "&nomortelepon=" + tlp + "&jeniskelamin=" + jk + "&doa=" + isiDoa);
            }
        });
    }

    public void register(final String nama, final String password ,final String email , final String tlp , final String alamat , final String tgllahir, final String idbaptis, final String komisi ,final String pelayanan ){
        // Post to server
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Writer().execute(url + "register.php?nama=" + nama + "&pass=" + password + "&email=" + email + "&no=" + tlp + "&alamat=" + alamat + "&idbaptis=" + idbaptis + "&komisi=" + komisi + "&pel=" + pelayanan + "&tgl=" + tgllahir);
             }
        });
    }

    public void editprofil(final String nama, final String email , final String tlp , final String alamat ,final String idbaptis, final String komisi ,final String pelayanan ){
        // Post to server
        SessionManager sm = new SessionManager(context);
        final String id = sm.pref.getAll().get("id").toString();
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Writer().execute(url + "edit_profil.php?nama=" + nama + "&email=" + email + "&no=" + tlp + "&alamat=" + alamat + "&idbaptis=" + idbaptis + "&komisi=" + komisi + "&pel=" + pelayanan+"&id="+id);
            }
        });
    }

    public void editPass(final String pass){
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

    public void login (final String nama,final String password ){
        Writer w = new Writer();
        w.execute(url + "login.php?nama=" + nama + "&password=" + password);
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
                    Log.d("Process", "Parsing json");
                    JSONObject judulobj = arr.getJSONObject(i);
                    Log.d("JSONObject",arr.getJSONObject(i).toString());
                    String judulx = judulobj.getString("judul");
                    Log.d("Judulx", judulx);
                    // Use the same for remaining values
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        @Override
        protected String doInBackground(String... params) {
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
            return "";
        }
    }

    public class Writer extends AsyncTask<String, Void, JSONObject> {
        private JSONObject result = null;

        @Override
        protected JSONObject doInBackground(String... params) {
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
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            String operation = null;
                try {
                    operation = result.getString("operation");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            if(operation.equals("login")){
                String nama = null, id = null, email = null, alamat = null, telepon = null, idbaptis = null, tgllahir = null, komisi = null, pelayanan = null , pass = null;
                try {
                    nama = result.getString("nama");
                    pass = result.getString("pass");
                    id = result.getString("id");
                    email = result.getString("email");
                    alamat = result.getString("alamat");
                    telepon= result.getString("telepon");
                    idbaptis = result.getString("idbaptis");
                    tgllahir = result.getString("tgllahir");
                    komisi = result.getString("komisi");
                    pelayanan = result.getString("pelayanan");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (writeResponse.equals("ok")) {
                    SessionManager smn = new SessionManager(context);
                    smn.createLoginSession(nama, pass, id, email, alamat, telepon, idbaptis, tgllahir, komisi,pelayanan);
                    Toast.makeText(context, "Login success", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Login " + writeResponse, Toast.LENGTH_LONG).show();
                }
            }
                else {
                // Operation.eq("add doa") , edit profil , register
                if (writeResponse.equals("ok")) {
                    Toast.makeText(context, operation +" Success", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, operation+""+ writeResponse, Toast.LENGTH_LONG).show();
                }
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
