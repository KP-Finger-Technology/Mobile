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
    String url = "http://192.168.0.101/server/";
    boolean lock = true;
    private JSONArray arrData = new JSONArray();

    public JSONArray getArrData(){
        return arrData;
    }

    public void setEmptyArr(){
        JSONArray newEmpty = new JSONArray();
        arrData = newEmpty;
    }

    public boolean viewEvent() {
        Log.d("Now running","run");
        final Handler handler = new Handler();
                handler.post(new Runnable() {
                    Viewer v = new Viewer();
                    @Override
                    public void run() {
                        v.execute(url + "view_event.php");
                        Log.d("Now running", "execute viewer");
//                        while(lock){
////                        Log.d("Print isi array",v.arr.toString());
//                            Log.d("arrData",arrData.toString());
//                        }
                        Log.d("arrData",arrData.toString());
                    }
        });
        return true;
    }

    public boolean viewKolportase(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            Viewer v = new Viewer();
            @Override
            public void run() {
                v.execute(url + "view_kolportase.php");
                Log.d("Now running", "execute viewer");
                Log.d("arrData",arrData.toString());
            }
        });
        return true;
    }

    public boolean viewWarta(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            Viewer v = new Viewer();
            @Override
            public void run() {
                v.execute(url + "view_warta.php");
                Log.d("arrData",arrData.toString());
            }
        });
        return true;
    }

    public boolean viewGema(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            Viewer v = new Viewer();
            @Override
            public void run() {
                v.execute(url + "view_gema.php");
                Log.d("arrData",arrData.toString());
            }
        });
        return true;
    }

    public boolean viewJadwalKotbah(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            Viewer v = new Viewer();
            @Override
            public void run() {
                v.execute(url + "view_jadwalkotbah.php");
                Log.d("arrData",arrData.toString());
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
                Log.d("arrData",arrData.toString());
            }
        });
        return true;
    }
    public boolean viewJadwalTeduhPagi(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            Viewer v = new Viewer();
            @Override
            public void run() {
                v.execute(url + "view_jadwalteduhpagi.php");
                Log.d("arrData",arrData.toString());
            }
        });
        return true;
    }
    public boolean viewJadwalKomisiPembicara(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            Viewer v = new Viewer();
            @Override
            public void run() {
                v.execute(url + "view_jadwalkomisipembicara.php");
                Log.d("arrData",arrData.toString());
            }
        });
        return true;
    }

    public boolean viewJadwalKomisiPenerjemah(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            Viewer v = new Viewer();
            @Override
            public void run() {
                v.execute(url + "view_jadwalkomisipenerjemah.php");
                Log.d("arrData",arrData.toString());
            }
        });
        return true;
    }

    public void addDoa(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Writer().execute(url + "add_doa.php");
            }
        });
    }

    public void reader (final String urlp){

        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONArray arr = new JSONArray();
                String result = "";
                String statu ="";
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
        });
    }

    class Viewer extends AsyncTask<String, String, String> {
        JSONArray arr = new JSONArray();

        public JSONArray getArr() {
            return arr;
        }

        protected void onPostExecute(Long result) {
            lock = false;
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            String statu ="";
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
                        arrData = res.getJSONArray("data");
                        Log.d("Array", arrData.toString());
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

