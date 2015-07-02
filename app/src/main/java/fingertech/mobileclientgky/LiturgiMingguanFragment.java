package fingertech.mobileclientgky;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Created by William Stefan Hartono
 */
public class LiturgiMingguanFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private JSONArray liturgiSaved;

    // Untuk komponen-komponen
    private LinearLayout myLinearLayout;
    private View rootView;
    private TableLayout myTableLayout;
    private TableRow TR;
    private TextView IsiTabelHeader;
    private TextView IsiTabel;
    private TableLayout.LayoutParams tableParams;
    private HorizontalScrollView HSV;

    public static LiturgiMingguanFragment newInstance(String param1, String param2) {
        LiturgiMingguanFragment fragment = new LiturgiMingguanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public LiturgiMingguanFragment() {}

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putStringArrayList("jadwalSaved",jadwalSaved);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            // Probably orientation change
        }
        else {
            if (liturgiSaved != null) {
                // Returning from backstack, data is fine, do nothing
                generateKontenLiturgi(liturgiSaved);
            }
            else {
                // Newly created, compute data
                Viewer viewer = new Viewer();
                viewer.execute();
            }
        }
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
        rootView = inflater.inflate(R.layout.fragment_liturgi_mingguan, container, false);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        // TODO: Update argument type and name
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

    private void IsiTabelHeader(String text) {
        IsiTabelHeader = new TextView(getActivity());
        IsiTabelHeader.setText(text);
        IsiTabelHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        IsiTabelHeader.setBackground(getResources().getDrawable(R.drawable.header_tabel));
        IsiTabelHeader.setTextColor(getResources().getColor(R.color.white));
        TR.addView(IsiTabelHeader);
    }

    private void IsiTabel(String text) {
        IsiTabel = new TextView(getActivity());
        IsiTabel.setText(text);
        IsiTabel.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        IsiTabel.setBackground(getResources().getDrawable(R.drawable.background_tabel));
        IsiTabel.setTextColor(getResources().getColor(R.color.fontTabel));
        TR.addView(IsiTabel);
    }

    private void setUpLayout() {
        // Add LinearLayout
        myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_liturgi_mingguan);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);

        // Add layout untuk tabel
        tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        HSV = null;
        myTableLayout = new TableLayout(getActivity());
        myTableLayout.setLayoutParams(tableParams);
    }

    private void setHeaderTable (String idLiturgi, String judulAcara) {
        HSV = new HorizontalScrollView(getActivity());
        TR = new TableRow(getActivity());
        TR.setLayoutParams(tableParams);

        // Tambah atribut header tabel
        IsiTabelHeader(idLiturgi);
        IsiTabelHeader(judulAcara);
        IsiTabelHeader(""); // utk kolom ketiga
        myTableLayout.addView(TR);
    }

    private void fillingTable (String keterangan, String idSubAcara, String subAcara) {
        if (keterangan != null && keterangan != "null" && idSubAcara != "null" && idSubAcara != null && subAcara != null && subAcara != "null") {
            TR = new TableRow(getActivity());
            TR.setLayoutParams(tableParams);

            // Masukkan ke cell tabel
            IsiTabel(""); // utk tambal kolom pertama
            IsiTabel(idSubAcara + ". " + subAcara);
            IsiTabel(keterangan);
            myTableLayout.addView(TR, tableParams);
        }
    }

    private void generateKontenLiturgi (JSONArray json_arr) {
        setUpLayout();

        int dataLength = json_arr.length();
        String idLiturgi = null, judulAcara = null, subAcara = null, keterangan = null, idSubAcara = null;
        // Generate konten Liturgi Mingguan dalam loop for
        for (int i = 0; i < dataLength; i++) {
            JSONObject jsonobj = null;
            try {
                jsonobj = json_arr.getJSONObject(i);
                JSONArray jsonArr = jsonobj.getJSONArray("atribut");

                idLiturgi = jsonobj.getString("idliturgi");
                judulAcara = jsonobj.getString("judulacara");

                setHeaderTable(idLiturgi, judulAcara);

                for(int j = 0; j < jsonArr.length(); j++) {
                    keterangan = jsonArr.getJSONObject(j).getString("keterangan");
                    idSubAcara = jsonArr.getJSONObject(j).getString("idsubacara");
                    subAcara = jsonArr.getJSONObject(j).getString("subacara");

                    fillingTable(keterangan, idSubAcara, subAcara);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(isNetworkAvailable()) {
            HSV.addView(myTableLayout);
            myLinearLayout.addView(HSV);
        }
    }

    class Viewer extends AsyncTask<String, String, String> {
        JSONArray arr = new JSONArray();

        public JSONArray getArr() {
            return arr;
        }

        @Override
        protected void onPreExecute() {};

        @Override
        protected String doInBackground(String... params) {
            if(isNetworkAvailable()) {
                String result = "";
                String statu = "";
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(Controller.url + "view_liturgi.php");
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
                        arr = res.getJSONArray("data");
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
            if (arr.length() == 0 && isNetworkAvailable()){
                Toast.makeText(getActivity().getApplicationContext(), "Tidak ada liturgi mingguan", Toast.LENGTH_SHORT).show();
            }
            liturgiSaved = new JSONArray();

            liturgiSaved = arr;
            generateKontenLiturgi(arr);
        }
    }
}