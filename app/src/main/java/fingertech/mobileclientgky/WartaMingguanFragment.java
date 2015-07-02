package fingertech.mobileclientgky;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
public class WartaMingguanFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private View rootView;

    private Viewer viewer;
    private ArrayList<JSONArray> wartaSaved; // idx 0 utk jadwal. 1 utk warta,

    // Untuk komponen-komponen
    private LinearLayout myLinearLayout;
    private TableLayout myTableLayout;
    private TableRow TR;
    private TextView JudulTabel;
    private TextView IsiTabelHeader;
    private TextView IsiTabel;
    private TextView judulTV;
    private TextView deskripsiTV;
    private LinearLayout.LayoutParams params;
    private LinearLayout.LayoutParams paramsDeskripsi;
    private TableLayout.LayoutParams tableParams;
    private TableLayout.LayoutParams rowTableParams;
    private HorizontalScrollView HSV;

    public static WartaMingguanFragment newInstance(String param1, String param2) {
        WartaMingguanFragment fragment = new WartaMingguanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public WartaMingguanFragment() {}

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
            if (wartaSaved != null) {
                // Returning from backstack, data is fine, do nothing
                generateKontenWarta(wartaSaved.get(0), wartaSaved.get(1));
            }
            else {
                // Newly created, compute data
                viewer = new Viewer();
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
        rootView = inflater.inflate(R.layout.fragment_warta_mingguan, container, false);
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

    private void IsiTabelHeader (String text) {
        IsiTabelHeader = new TextView(getActivity());
        IsiTabelHeader.setText(text);
        IsiTabelHeader.setTextColor(getResources().getColor(R.color.white));
        IsiTabelHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        IsiTabelHeader.setBackground(getResources().getDrawable(R.drawable.header_tabel));
        TR.addView(IsiTabelHeader);
    }

    private void IsiTabel (String text) {
        IsiTabel = new TextView(getActivity());
        IsiTabel.setText(text);
        IsiTabel.setTextColor(getResources().getColor(R.color.fontTabel));
        IsiTabel.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        IsiTabel.setBackground(getResources().getDrawable(R.drawable.background_tabel));
        TR.addView(IsiTabel);
    }

    private void setUpLayout() {
        myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_wartaMingguan);

        // Add LayoutParams
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
        params.setMargins(0, 10, 0, 0);

        // Param untuk deskripsi
        paramsDeskripsi = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
        paramsDeskripsi.setMargins(0, 0, 0, 0);

        tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        rowTableParams = new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

        // Untuk tag "warta"
        LinearLayout rowLayout = new LinearLayout(getActivity());
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Membuat linear layout vertical untuk menampung kata-kata
        LinearLayout colLayout = new LinearLayout(getActivity());
        colLayout.setOrientation(LinearLayout.VERTICAL);
        colLayout.setPadding(0, 5, 0, 0);
    }

    private void generateKontenWarta (JSONArray jadwal, JSONArray warta) {
        setUpLayout();

        int dataLength = jadwal.length();
//        Log.d("length obj.length",Integer.toString(obj.length()));
//        Log.d("length jadwal.length",Integer.toString(jadwal.length()));

        String tanggal = null, kebaktian = null, pengkotbah = null, judul = null, deskripsi = null,gedung =null, penerjemah = null, liturgis = null, pianis = null ,paduansuara = null;
        // Generate konten Warta Mingguan dalam loop for
        for (int i=0; i < dataLength; i++){
            try {
                JSONArray jsonAtribut = jadwal.getJSONObject(i).getJSONArray("atribut");

                tanggal = jadwal.getJSONObject(i).getString("tanggal");

                myTableLayout = new TableLayout(getActivity());
                myTableLayout.setLayoutParams(tableParams);
                HSV = new HorizontalScrollView(getActivity());

                TR = new TableRow(getActivity());
                TR.setLayoutParams(rowTableParams);

                // Judul kolom
                IsiTabelHeader("Kebaktian");  // Kebaktian
                IsiTabelHeader("Gedung");
                IsiTabelHeader("Pengkotbah"); // Pengkotbah
                IsiTabelHeader("Penerjemah");
                IsiTabelHeader("Liturgis");
                IsiTabelHeader("Pianis");
                IsiTabelHeader("Paduan Suara");

                myTableLayout.addView(TR, tableParams);  // Add row to table

                JudulTabel = new TextView(getActivity());
                JudulTabel.setText("Jadwal khotbah pada tanggal "+tanggal);
                JudulTabel.setLayoutParams(params);
                myLinearLayout.addView(JudulTabel);

                int length2 = jsonAtribut.length();
                for (int j = 0; j < length2; j++) {
                    pengkotbah = jsonAtribut.getJSONObject(j).getString("pengkotbah");
                    kebaktian = jsonAtribut.getJSONObject(j).getString("kebaktianumum");
                    gedung=jsonAtribut.getJSONObject(j).getString("gedung");
                    penerjemah = jsonAtribut.getJSONObject(j).getString("penerjemah");
                    liturgis = jsonAtribut.getJSONObject(j).getString("liturgis");
                    pianis = jsonAtribut.getJSONObject(j).getString("pianis");
                    paduansuara = jsonAtribut.getJSONObject(j).getString("paduansuara");


                    TR = new TableRow(getActivity());
                    TR.setLayoutParams(rowTableParams);
                    // Kebaktian
                    IsiTabel(kebaktian);
                    IsiTabel(gedung);
                    // Pengkotbah
                    IsiTabel(pengkotbah);
                    IsiTabel(penerjemah);
                    IsiTabel(liturgis);
                    IsiTabel(pianis);
                    IsiTabel(paduansuara);
                    // Add row to table
                    myTableLayout.addView(TR, tableParams);
                }
                HSV.setScrollbarFadingEnabled(false);
                HSV.addView(myTableLayout);
                myLinearLayout.addView(HSV);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        dataLength = warta.length();

        for (int i=0; i < dataLength; i++){
            JSONObject jsonobj = null;
            try {
                jsonobj = warta.getJSONObject(i);
                Log.d("JSONObject", warta.getJSONObject(i).toString());
                judul = jsonobj.getString("judul");
                deskripsi = jsonobj.getString("deskripsi");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Judul warta
            TextView wartaTV = new TextView(getActivity());
            wartaTV.setText("Judul: " + judul);
            wartaTV.setLayoutParams(params);
            myLinearLayout.addView(wartaTV);

            // Deskripsi warta
            wartaTV = new TextView(getActivity());
            wartaTV.setText("Deskripsi: " + deskripsi);
            wartaTV.setLayoutParams(paramsDeskripsi);
            myLinearLayout.addView(wartaTV);
        }
    }

    class Viewer extends AsyncTask<String, String, String> {
        JSONObject obj = new JSONObject();

        public JSONObject getObj() {
            return obj;
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... params) {
            if(isNetworkAvailable()) {
                String result = "";
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(Controller.url + "view_wartamingguan.php");
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
                        obj = res.getJSONObject("data");
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
            if (obj.length() == 0 && isNetworkAvailable()){
                Toast.makeText(getActivity().getApplicationContext(), "Tidak ada warta mingguan", Toast.LENGTH_SHORT).show();
            }
            wartaSaved = new ArrayList<JSONArray>();

            JSONArray jadwal = new JSONArray();
            JSONArray warta = new JSONArray();
            try {
                jadwal = obj.getJSONArray("jadwal");
                wartaSaved.add(jadwal);
                warta = obj.getJSONArray("warta");
                wartaSaved.add(warta);
            }
            catch (JSONException e) {
                Log.d("excp olah atribut jdwal","..");
            }
            generateKontenWarta(jadwal, warta);
        }
    }
}