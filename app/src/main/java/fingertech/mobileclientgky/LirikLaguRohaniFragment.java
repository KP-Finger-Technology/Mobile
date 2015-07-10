package fingertech.mobileclientgky;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


/**
 * Created by William Stefan Hartono
 */
public class LirikLaguRohaniFragment extends Fragment implements View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View rootView;

    private Fragment frag;
    private FragmentTransaction fragTransaction;
    private FragmentManager fragManager;

    private SearchView sv;
    private LinearLayout cllr;
    private String keyword = null;

    private ArrayList<String> arrLirik;
    private Boolean adaLirik = false;

    public static LirikLaguRohaniFragment newInstance(String param1, String param2) {
        LirikLaguRohaniFragment fragment = new LirikLaguRohaniFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    Viewer v = new Viewer();

    public LirikLaguRohaniFragment() {}

    private DataBaseHelper DB;
    private Button lirikLaguRohani_download;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private ArrayList<String> laguSaved;
    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("laguSaved",laguSaved);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DataBaseHelper DBH = new DataBaseHelper(getActivity().getApplicationContext());
        DBH.openDataBase();
        if (savedInstanceState != null) {
            // Probably orientation change
            laguSaved = savedInstanceState.getStringArrayList("laguSaved");
            if (!DBH.isTableExists("LirikLaguRohani"))
                generateKontenLirikLaguRohani(false);
        }
        else {
            if (laguSaved!=null) {
                // Returning from backstack, data is fine, do nothing
                if (!DBH.isTableExists("LirikLaguRohani")) {
                    generateKontenLirikLaguRohani(false);
                }
                else
                    generateKontenLirikLaguRohani(true);
            }
            else {
                // Newly created, compute data
                if (!DBH.isTableExists("LirikLaguRohani")) {
                    v = new Viewer();
                    v.execute();
                }
                else
                    generateKontenLirikLaguRohani(true);
            }
        }
    }

    private LinearLayout myLinearLayout;

    private void generateKontenLirikLaguRohani(boolean mode) {
        // Mode == true untuk load dr database
        // Mode == false untuk load dr konten yg telah di save dr server

        ArrayList<String> containerString = new ArrayList<String>();

        if (mode)
            containerString = DB.getLirikLaguRohani();
        else
            containerString = laguSaved;

        myLinearLayout = (LinearLayout) rootView.findViewById(R.id.container_lirikLaguRohani);

        // Add LayoutParams
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
        params.setMargins(0, 10, 20, 0);

        int dataLength = containerString.size();
        int defaultColor = getResources().getColor(R.color.defaultFontColor);
        Button ListLirikLaguRohani;

        int cnt = 1;
        for (int i = 0; i < dataLength; i = i + 2) {
            String container = "LirikLaguRohani " + Integer.toString(cnt) + " - " + containerString.get(i);
            cnt++;

            // Add Button Judul Lirik Lagu Rohani
            ListLirikLaguRohani = new Button(getActivity());
            ListLirikLaguRohani.setText(container);
            ListLirikLaguRohani.setTextColor(defaultColor);
            ListLirikLaguRohani.setLayoutParams(params);
            ListLirikLaguRohani.setBackground(getResources().getDrawable(R.drawable.kppkliturgibutton));
            ListLirikLaguRohani.setTextAppearance(getActivity().getApplicationContext(), R.style.kppkLiturgiButtonStyle);

            final String _judul = containerString.get(i + 1);
            final String _isi = containerString.get(i + 1);

            // Add button listener here
            ListLirikLaguRohani.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Masuk ke konstruktor parameter LirikLaguRohaniLiengkapFragment dengan parameter judul dan isi
                            frag = new LirikLaguRohaniLengkapFragment(_judul, _isi);
                            fragManager = getActivity().getSupportFragmentManager();
                            fragTransaction = fragManager.beginTransaction();
                            fragTransaction.replace(R.id.container, frag);
                            fragTransaction.addToBackStack(null);
                            fragTransaction.commit();
                        }
                    }
            );

            myLinearLayout.addView(ListLirikLaguRohani);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_lirik_lagu_rohani, container, false);
        lirikLaguRohani_download = (Button) rootView.findViewById(R.id.lirikLaguRohani_download);
        lirikLaguRohani_download.setOnClickListener(this);

        DB = new DataBaseHelper(getActivity().getApplicationContext());
        DB.openDataBase();
        if (DB.isTableExists("LirikLaguRohani")) {
            // Jika tabel LirikLaguRohani exist, berarti sudah pernah di-download. Tampilkan daftar LirikLaguRohani dari database
            lirikLaguRohani_download.setText("Update Lirik");
        }

        sv = (SearchView) rootView.findViewById(R.id.lirikLaguRohani_searchview);
        cllr = (LinearLayout) rootView.findViewById(R.id.container_lirikLaguRohani);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                try {
                    keyword = URLEncoder.encode(s, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getActivity(), "Lagu yang Anda cari: " + keyword, Toast.LENGTH_LONG).show();

                if (DB.isTableExists("LirikLaguRohani")) {
                    arrLirik = DB.searchLirik(keyword);

                    if(arrLirik.size() != 0) {
                        adaLirik = true;
                    }
                }

                ViewerSearch vs = new ViewerSearch();
                vs.execute();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

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

    @Override
    public void onClick(View view) {
        Toast.makeText(getActivity(), "Downloading..", Toast.LENGTH_LONG).show();
        v.downloadLirikLaguRohani();
        Toast.makeText(getActivity(), "Download Succeed!", Toast.LENGTH_LONG).show();
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
        private Button ListLirikLaguRohani;
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
                HttpGet request = new HttpGet(Controller.url + "view_lagu.php");
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

        public void downloadLirikLaguRohani() {
            int dataLength = arr.length();
            ArrayList<String> tmp = new ArrayList<String>();
            String judul = null, isi = null;

            // Generate konten Event dalam loop for
            for (int i = 0; i < dataLength; i++) {
                JSONObject jsonobj = null;
                try {
                    jsonobj = arr.getJSONObject(i);
                    judul = jsonobj.getString("judul");
                    isi = jsonobj.getString("isi");
                    tmp.add(judul);
                    tmp.add(isi);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (DB.isTableExists("LirikLaguRohani")) {
                DB.deleteTableLirikLaguRohani();
            }
            DB.createTableLirikLaguRohani();
            DB.insertDataLirikLaguRohani(tmp);
        }

        @Override
        protected void onPostExecute(String result) {
            if (arr.length() == 0 && isNetworkAvailable()){
                Toast.makeText(getActivity().getApplicationContext(), "Tidak ada lirik lagu", Toast.LENGTH_SHORT).show();
            }

            myLinearLayout = (LinearLayout) rootView.findViewById(R.id.container_lirikLaguRohani);

            // Add LayoutParams
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            myLinearLayout.setOrientation(LinearLayout.VERTICAL);
            params.setMargins(0, 10, 20, 0);

            int dataLength = arr.length();
            int defaultColor = getResources().getColor(R.color.defaultFontColor);
            String container, judul, isi = null;

            // Generate konten LirikLaguRohani dalam loop for
            for (int i = 0; i < dataLength; i++) {
                JSONObject jsonobj = null;
                judul = "";
                try {
                    jsonobj = arr.getJSONObject(i);
                    judul = jsonobj.getString("judul");
                    isi = jsonobj.getString("isi");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                container = "LirikLaguRohani " + Integer.toString(i + 1) + " - " + judul;

                // Add Button Judul Lirik Lagu Rohani
                ListLirikLaguRohani = new Button(getActivity());
                ListLirikLaguRohani.setText(container);
                ListLirikLaguRohani.setTextColor(defaultColor);
                ListLirikLaguRohani.setLayoutParams(params);
                ListLirikLaguRohani.setBackground(getResources().getDrawable(R.drawable.kppkliturgibutton));
                ListLirikLaguRohani.setTextAppearance(getActivity().getApplicationContext(), R.style.kppkLiturgiButtonStyle);

                final String _judul = judul;
                final String _isi = isi;

                // Add button listener here
                ListLirikLaguRohani.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Masuk ke konstruktor parameter LirikLaguRohaniLengkapFragment dengan parameter judul dan isi
                                frag = new LirikLaguRohaniLengkapFragment(_judul, _isi);
                                fragManager = getActivity().getSupportFragmentManager();
                                fragTransaction = fragManager.beginTransaction();
                                fragTransaction.replace(R.id.container, frag);
                                fragTransaction.addToBackStack(null);
                                fragTransaction.commit();
                            }
                        }
                );

                myLinearLayout.addView(ListLirikLaguRohani);
            }
        }
    }

    class ViewerSearch extends AsyncTask<String, String, String> {
        private Button ListLirikLaguRohani;
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
                HttpGet request = new HttpGet(Controller.url + "view_lagusearch.php?kw=" + keyword);
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

        public void downloadLirikLaguRohani() {
            int dataLength = arr.length();
            ArrayList<String> tmp = new ArrayList<String>();

            String judul = null, isi = null;
            laguSaved = new ArrayList<String>();

            // Generate konten Event dalam loop for
            for (int i = 0; i < dataLength; i++) {
                JSONObject jsonobj = null;
                try {
                    jsonobj = arr.getJSONObject(i);
                    judul = jsonobj.getString("judul");
                    isi = jsonobj.getString("isi");
                    tmp.add(judul);
                    tmp.add(isi);

                    laguSaved.add(judul);
                    laguSaved.add(isi);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (DB.isTableExists("LirikLaguRohani")) {
                DB.deleteTableLirikLaguRohani();
            }
            DB.createTableLirikLaguRohani();
            DB.insertDataLirikLaguRohani(tmp);
        }

        @Override
        protected void onPostExecute(String result) {
            // Hapus semua tampilan terlebih dahulu sebelum menampilkan hasil pencarian
            cllr.removeAllViews();
			
			if (arr.length() == 0 && isNetworkAvailable()){
                Toast.makeText(getActivity().getApplicationContext(), "Lagu yang Anda cari tidak ditemukan", Toast.LENGTH_SHORT).show();
            }
            myLinearLayout = (LinearLayout) rootView.findViewById(R.id.container_lirikLaguRohani);

            // Add LayoutParams
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            myLinearLayout.setOrientation(LinearLayout.VERTICAL);
            params.setMargins(0, 0, 0, 10);

            int dataLength = arr.length();
            int defaultColor = getResources().getColor(R.color.defaultFontColor);
            String container = null, judul = null, isi = null;

            // Cari dari server
            if (!adaLirik) {
                // Generate konten LirikLaguRohani dalam loop for
                for (int i = 0; i < dataLength; i++) {
                    JSONObject jsonobj = null;
                    try {
                        jsonobj = arr.getJSONObject(i);
                        judul = jsonobj.getString("judul");
                        isi = jsonobj.getString("isi");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    container = "LirikLaguRohani " + Integer.toString(i + 1) + " - " + judul;

                    // Add Button Judul Lirik Lagu Rohani
                    ListLirikLaguRohani = new Button(getActivity());
                    ListLirikLaguRohani.setText(container);
                    ListLirikLaguRohani.setTextColor(defaultColor);
                    ListLirikLaguRohani.setLayoutParams(params);
                    ListLirikLaguRohani.setBackground(getResources().getDrawable(R.drawable.kppkliturgibutton));
                    ListLirikLaguRohani.setTextAppearance(getActivity().getApplicationContext(), R.style.kppkLiturgiButtonStyle);

                    final String _judul = judul;
                    final String _isi = isi;

                    // Add button listener here
                    ListLirikLaguRohani.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Masuk ke konstruktor parameter LirikLaguRohaniLengkapFragment dengan parameter judul dan isi
                                    frag = new LirikLaguRohaniLengkapFragment(_judul, _isi);
                                    fragManager = getActivity().getSupportFragmentManager();
                                    fragTransaction = fragManager.beginTransaction();
                                    fragTransaction.replace(R.id.container, frag);
                                    fragTransaction.addToBackStack(null);
                                    fragTransaction.commit();
                                }
                            }
                    );

                    myLinearLayout.addView(ListLirikLaguRohani);
                }
            }

            // Cari dari basis data LirikLaguRohani
            else {
                // Generate konten LirikLaguRohani dalam loop for
                for (int i = 0; i < arrLirik.size(); i = i + 2) {
                    try {
                        judul = arrLirik.get(i);
                        isi = arrLirik.get(i + 1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    container = "LirikLaguRohani " + Integer.toString(i + 1) + " - " + judul;

                    // Add Button Judul Lirik Lagu Rohani
                    ListLirikLaguRohani = new Button(getActivity());
                    ListLirikLaguRohani.setText(container);
                    ListLirikLaguRohani.setTextColor(defaultColor);
                    ListLirikLaguRohani.setLayoutParams(params);
                    ListLirikLaguRohani.setBackground(getResources().getDrawable(R.drawable.kppkliturgibutton));
                    ListLirikLaguRohani.setTextAppearance(getActivity().getApplicationContext(), R.style.kppkLiturgiButtonStyle);

                    final String _judul = judul;
                    final String _isi = isi;

                    // Add button listener here
                    ListLirikLaguRohani.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Masuk ke konstruktor parameter LirikLaguRohaniLengkapFragment dengan parameter jduul dan isi
                                    frag = new LirikLaguRohaniLengkapFragment(_judul, _isi);
                                    fragManager = getActivity().getSupportFragmentManager();
                                    fragTransaction = fragManager.beginTransaction();
                                    fragTransaction.replace(R.id.container, frag);
                                    fragTransaction.addToBackStack(null);
                                    fragTransaction.commit();
                                }
                            }
                    );

                    myLinearLayout.addView(ListLirikLaguRohani);
                }
            }
        }
    }
}