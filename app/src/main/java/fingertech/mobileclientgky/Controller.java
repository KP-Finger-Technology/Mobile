package fingertech.mobileclientgky;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
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
                        Log.d("Now running","execute viewer");
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

    public void addDoa(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Viewer().execute(url+"add_doa.php");
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
}
