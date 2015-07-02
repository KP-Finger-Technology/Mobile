package fingertech.mobileclientgky;

import android.content.Context;
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
 * Created by Andarias Silvanus
 */
public class JadwalIbadahFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View rootView;

    private OnFragmentInteractionListener mListener;

    // Untuk komponen-komponen
    private LinearLayout myLinearLayout;
    private TableLayout myTableLayout;
    private TableRow TR;
    private TextView JudulTabel;
    private TextView IsiTabelHeader;
    private TextView IsiTabel;
    private LinearLayout.LayoutParams params;
    private TableLayout.LayoutParams tableParams;
    private TableLayout.LayoutParams rowTableParams;
    private HorizontalScrollView HSV;

    // utk saved instances
    private ArrayList<String> jadwalSaved;

    public static JadwalIbadahFragment newInstance(String param1, String param2) {
        JadwalIbadahFragment fragment = new JadwalIbadahFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public JadwalIbadahFragment() {}

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("jadwalSaved",jadwalSaved);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            // Probably orientation change
            jadwalSaved = savedInstanceState.getStringArrayList("jadwalSaved");
            generateKontenJadwal();
        }
        else {
            if (jadwalSaved != null) {
                // Returning from backstack, data is fine, do nothing
                generateKontenJadwal();
            }
            else {
                // Newly created, compute data
                Viewer viewer = new Viewer();
                viewer.execute();
            }
        }
    }

    private void generateKontenJadwal() {
        setUpLayout();

        // Judul kolom
        IsiTabelHeader("Tanggal");  // Tanggal
        IsiTabelHeader("Isi");      // Isi
        myTableLayout.addView(TR);  // Add row to table

        for (int i = 0; i < jadwalSaved.size(); i = i + 2) {
            fillingTable(jadwalSaved.get(i), jadwalSaved.get(i+1));
        }
        HSV.addView(myTableLayout);
        myLinearLayout.addView(HSV);
    }

    private void IsiTabelHeader (String text) {
        IsiTabelHeader = new TextView(getActivity());
        IsiTabelHeader.setText(text);
        IsiTabelHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        IsiTabelHeader.setBackground(getResources().getDrawable(R.drawable.header_tabel));
        IsiTabelHeader.setTextColor(getResources().getColor(R.color.white));
        TR.addView(IsiTabelHeader);
    }

    private void IsiTabel (String text) {
        IsiTabel = new TextView(getActivity());
        IsiTabel.setText(text);
        IsiTabel.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        IsiTabel.setBackground(getResources().getDrawable(R.drawable.background_tabel));
        IsiTabel.setTextColor(getResources().getColor(R.color.fontTabel));
        TR.addView(IsiTabel);
    }

    private void setUpLayout() {
        myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_jadwalIbadah);

        // Add LayoutParams
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
        params.setMargins(0, 10, 20, 0);

        tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        rowTableParams = new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

        myTableLayout = new TableLayout(getActivity());
        myTableLayout.setLayoutParams(tableParams);
        HSV = new HorizontalScrollView(getActivity());

        TR = new TableRow(getActivity());
        TR.setLayoutParams(tableParams);
    }

    private void fillingTable (String tanggal, String isi) {
        TR = new TableRow(getActivity());
        TR.setLayoutParams(rowTableParams);

        IsiTabel(tanggal);  // Tanggal
        IsiTabel(isi);      // Isi
        myTableLayout.addView(TR, tableParams); // Add row to table
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
        rootView = inflater.inflate(R.layout.fragment_jadwal_ibadah, container, false);
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
        // Untuk komponen-komponen
        private LinearLayout myLinearLayout;
        private TableLayout myTableLayout;
        private TableRow TR;
        private TextView IsiTabelHeader;
        private TextView IsiTabel;
        private LinearLayout.LayoutParams params;

        JSONArray arr = new JSONArray();

        public JSONArray getArr() {
            return arr;
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... params) {
            if(isNetworkAvailable()) {
                String result = "";
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(Controller.url + "view_jadwalibadah.php");
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
                        // Data
                        JSONObject res = new JSONObject(result);
                        arr = res.getJSONArray("data");
                        Log.d("Array data", arr.toString());
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

        @Override
        protected void onPostExecute(String result) {
            if (arr.length() == 0 && isNetworkAvailable()){
                Toast.makeText(getActivity().getApplicationContext(), "Tidak ada jadwal ibadah", Toast.LENGTH_SHORT).show();
            }

            setUpLayout();
            jadwalSaved = new ArrayList<String>();

            int dataLength = arr.length();

            String tanggal = null, isi = null;

            // Judul kolom
            IsiTabelHeader("Tanggal");  // Tanggal
            IsiTabelHeader("Isi");      // Isi
            myTableLayout.addView(TR);  // Add row to table

            // Generate konten Jadwal Ibadah dalam loop for
            for (int i = 0; i < dataLength; i++){
                JSONObject jsonobj = null;

                try {
                    jsonobj = arr.getJSONObject(i);
                    tanggal = jsonobj.getString("tanggal");
                    isi = jsonobj.getString("isi");

                    jadwalSaved.add(tanggal);
                    jadwalSaved.add(isi);

                    fillingTable(tanggal, isi);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(isNetworkAvailable()) {
                HSV.addView(myTableLayout);
                myLinearLayout.addView(HSV);
            }
        }
    }
}
