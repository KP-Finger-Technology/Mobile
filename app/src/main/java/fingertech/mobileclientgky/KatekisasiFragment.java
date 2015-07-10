package fingertech.mobileclientgky;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * Created by William Stefan Hartono
 */
public class KatekisasiFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Untuk generate konten UI
    private View rootView;
    private LinearLayout myLinearLayout;
    private LinearLayout imageLayout;
    private LinearLayout.LayoutParams params;

    public static KatekisasiFragment newInstance(String param1, String param2) {
        KatekisasiFragment fragment = new KatekisasiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public KatekisasiFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_katekisasi, container, false);
        Viewer v = new Viewer();
        v.execute();
        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    // Untuk mengecek apakah ada koneksi internet
    public boolean isNetworkAvailable() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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
        JSONArray arrData = new JSONArray();
        ProgressDialog progressDialog;
        TextView isiTV;

        public JSONArray getArrData() {
            return arrData;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(), "Loading", "Koneksi ke server");
        }

        @Override
        protected String doInBackground(String... params) {
            if (isNetworkAvailable()) {
                SessionManager sm = new SessionManager(getActivity().getApplicationContext());

                String result = "";
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(Controller.url + "view_katekisasi.php?");
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
                        Log.d("Katekisasi data", arrData.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(), "Anda tidak terhubung ke internet", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            if (arrData.length() == 0 && isNetworkAvailable()) {
                Toast.makeText(getActivity().getApplicationContext(), "Tidak ada konten katekisasi", Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();

            myLinearLayout = (LinearLayout) rootView.findViewById(R.id.container_katekisasi);
            myLinearLayout.setOrientation(LinearLayout.VERTICAL);

            // Add LayoutParams
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 10, 0, 0);

            if (arrData != null) {
                // Ambil string yang diperlukan dari JSONArray

                int dataLength = arrData.length();

                for (int i = 0; i < dataLength; i++) {
                    String isi = null;
                    JSONObject jsonobj = null;

                    try {
                        jsonobj = arrData.getJSONObject(0);
                        isi = jsonobj.getString("isi");

                        Log.d("Katekisasi, isi", isi.toString());

                        isiTV = new TextView(getActivity());
                        isiTV.setText(isi);
                        isiTV.setTextColor(getResources().getColor(R.color.defaultFontColor));
                        isiTV.setLayoutParams(params);
                        myLinearLayout.addView(isiTV);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}