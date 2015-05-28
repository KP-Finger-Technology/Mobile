package fingertech.mobileclientgky;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

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
import java.util.TimerTask;

/**
 * Created by ASUS on 5/27/2015.
 */
public class Controller {
    String url = "http://192.168.0.111/server/";

    public void viewEvent() {
        Log.d("Now running","run");
        final Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        new Viewer().execute(url+"view_event.php");
                        Log.d("Now running", "execute viewer");
                    }
        });
    }

    public void viewKolportase(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Viewer().execute(url+"view_kolportase.php");
            }
        });
    }

    public void viewWarta(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Viewer().execute(url+"view_warta.php");
            }
        });
    }

    public void viewGema(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Viewer().execute(url+"view_gema.php");
            }
        });
    }

    public void viewJadwalKotbah(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Viewer().execute(url+"view_jadwalkotbah.php");
            }
        });
    }

    public void viewJadwalPelayanan(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Viewer().execute(url+"view_jadwalpelayanan.php");
            }
        });
    }
    public void viewJadwalTeduhPagi(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Viewer().execute(url+"view_jadwalteduhpagi.php");
            }
        });
    }
    public void viewJadwalKomisiPembicara(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Viewer().execute(url+"view_jadwalkomisipembicara.php");
            }
        });
    }

    public void viewJadwalKomisiPenerjemah(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Viewer().execute(url+"view_jadwalkomisipenerjemah.php");
            }
        });
    }

    public void addDoa(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Writer().execute(url+"add_doa.php");
            }
        });
    }


    class Viewer extends AsyncTask<String, String, String> {
        String result = "";
        String statu ="";

        @Override
        protected String doInBackground(String... params) {
            for (String urlp : params) {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(urlp); // ngikutin ip disini loh
                HttpResponse response;

                Log.d("now running","do in bg");

                try {

                    response = client.execute(request);

                    // Get the response
                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                    String line = "";
                    while ((line = rd.readLine()) != null) {
                        result += line;
                    }
//            result = result.substring(result.indexOf("{"), result.indexOf("}") + 1);
                    Log.d("Result", result);

                    try {
                        JSONObject res = new JSONObject(result);
                        JSONArray arr = new JSONArray();
                        arr = res.getJSONArray("data");
                        Log.d("Array", arr.toString());
                        statu = "ok";

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }
            return "";
        }
    }

    public class Writer extends AsyncTask<String, Void, JSONObject> {
        private final String TAG = "HttpClient";
        private JSONObject result = null;

        @Override
        protected JSONObject doInBackground(String... params) {
            String sendMessage;

            try {
                for (String urlp : params) {
                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPostRequest = new HttpPost(urlp);

                JSONObject sendObject = new JSONObject();
                sendObject.put("nama","13512009");
                sendObject.put("email","postMessage");
                sendObject.put("nomortelepon","13512009");
                sendObject.put("jeniskelamin","postMessage");
                sendObject.put("umur","13512009");
                sendObject.put("doa","postMessage");

                sendMessage = sendObject.toString();

                StringEntity se;
                se = new StringEntity(sendMessage);

                // Set HTTP parameters
                httpPostRequest.setEntity(se);
                httpPostRequest.setHeader("Accept", "application/json");
                httpPostRequest.setHeader("Content-type", "application/json");

                long t = System.currentTimeMillis();
                HttpResponse response = (HttpResponse) httpclient.execute(httpPostRequest);
                Log.i(TAG, "HTTPResponse received in [" + (System.currentTimeMillis() - t) + "ms]");

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // Read the content stream
                    InputStream instream = entity.getContent();

                    // convert content stream to a String
                    String resultString = convertStreamToString(instream);
                    instream.close();
                    resultString = resultString.substring(1, resultString.length() - 1); // remove wrapping "[" and "]"

                    JSONObject jsonObjRecv = new JSONObject(resultString);

                    // Raw DEBUG output of our received JSON object:
                    Log.i(TAG, "<JSONObject>\n" + jsonObjRecv.toString() + "\n</JSONObject>");

                    return jsonObjRecv;
                }
            }
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
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

