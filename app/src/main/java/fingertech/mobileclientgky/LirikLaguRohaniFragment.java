package fingertech.mobileclientgky;

import android.graphics.Color;
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
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LirikLaguRohaniFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LirikLaguRohaniFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LirikLaguRohaniFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LirikLaguRohaniFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LirikLaguRohaniFragment newInstance(String param1, String param2) {
        LirikLaguRohaniFragment fragment = new LirikLaguRohaniFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    Viewer v = new Viewer();

    public LirikLaguRohaniFragment() {
        // Required empty public constructor
    }

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
            // probably orientation change
            Log.d("from lagu: mencoba ambil arrayList yg disave..","..");
            laguSaved = savedInstanceState.getStringArrayList("laguSaved");
            if (!DBH.isTableExists("LirikLaguRohani"))
                generateKontenLirikLaguRohani(false);
            Log.d("from lagu: berhasil ambil arrayList yang telah di-save","..");
        }
        else {
            if (laguSaved!=null) {
                //returning from backstack, data is fine, do nothing
                Log.d("from KPPK, si arrayList!=null","..");
                if (!DBH.isTableExists("LirikLaguRohani")) {
                    Log.d("tabel lirik lagu tidak exist","..");
                    generateKontenLirikLaguRohani(false);
                }
            }
            else {
                //newly created, compute data
                Log.d("from KPPK, new Viewer & execute","..");
                if (!DBH.isTableExists("LirikLaguRohani")) {
                    Log.d("tabel lirik lagu tidak exist","..");
                    v = new Viewer();
                    v.execute();
                }
            }
        }
    }

    private LinearLayout myLinearLayout;

    private void generateKontenLirikLaguRohani(boolean mode) {
        // mode == true utk load dr database
        // mode == false utk load dr konten yg telah di save dr server

        ArrayList<String> containerString = new ArrayList<String>();

        if (mode)
            containerString = DB.getLirikLaguRohani();
        else
            containerString = laguSaved;

        myLinearLayout = (LinearLayout) rootView.findViewById(R.id.container_lirikLaguRohani);

        // Add LayoutParams
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
        params.setMargins(0, 10, 20, 0);

        int dataLength = containerString.size();
        Button ListLirikLaguRohani;

        int cnt = 1;
        for (int i=0; i<dataLength; i=i+2) {
            String container = "LirikLaguRohani " + Integer.toString(cnt) + " - " + containerString.get(i);
            cnt++;

            // Add Button Judul Lirik Lagu Rohani
            ListLirikLaguRohani = new Button(getActivity());
            ListLirikLaguRohani.setText(container);
            ListLirikLaguRohani.setLayoutParams(params);
            ListLirikLaguRohani.setBackgroundColor(0);

            final String _isi = containerString.get(i+1);

            // Add button listener here
            ListLirikLaguRohani.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Masuk ke konstruktor parameter LirikLaguRohaniLiengkapFragment dengan parameter isi
                            frag = new LirikLaguRohaniLengkapFragment(_isi);
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
            lirikLaguRohani_download.setVisibility(View.INVISIBLE);
            Log.d("tabel LirikLaguRohani sudah exist","..");
            generateKontenLirikLaguRohani(true);
        }
        else {
            // Belum pernah download LirikLaguRohani, maka tampilkan dari ambil JSON ke server

//            isLirikLaguRohaniExist = false;
//            v.execute();

        }

        sv = (SearchView) rootView.findViewById(R.id.lirikLaguRohani_searchview);
        cllr = (LinearLayout) rootView.findViewById(R.id.container_lirikLaguRohani);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                keyword = s;
                Toast.makeText(getActivity(), "Lagu yang Anda cari: " + keyword, Toast.LENGTH_LONG).show();

                if (DB.isTableExists("LirikLaguRohani")) {
                    arrLirik = DB.searchLirik(keyword);

                    if(arrLirik.size() != 0) {
                        adaLirik = true;
                    }
                }

                cllr.removeAllViews();
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

    @Override
    public void onClick(View view) {
        Toast.makeText(getActivity(), "Downloading..", Toast.LENGTH_LONG).show();
        v.downloadLirikLaguRohani();
        Toast.makeText(getActivity(), "Download Success!", Toast.LENGTH_LONG).show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
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
            String result = "";
            String status ="";

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(Controller.url+"view_lagu.php");
            HttpResponse response;

            try {
                response = client.execute(request);

                // Get the response
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line = "";
                while ((line = rd.readLine()) != null) {
                    result += line;
                }

                Log.d("Result", result);

                try {
                    JSONObject res = new JSONObject(result);
                    arr = res.getJSONArray("data");
                    Log.d("Array", arr.toString());
                    status = "ok";

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
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
                    Log.d("JSONObject", arr.getJSONObject(i).toString());
                    judul = jsonobj.getString("judul");
                    isi = jsonobj.getString("isi");
                    tmp.add(judul);
                    tmp.add(isi);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (DB.isTableExists("LirikLaguRohani")) {
                Log.d("tabel LirikLaguRohani exist! persiapan delete tabel liriklagu..","..");
                DB.deleteTableLirikLaguRohani();
            }
            Log.d("persiapan membuat tabel liriklagu baru..","..");
            DB.createTableLirikLaguRohani();
            Log.d("persiapan insert data pada tabel liriklagu..","..");
            DB.insertDataLirikLaguRohani(tmp);
        }

        @Override
        protected void onPostExecute(String result) {
            myLinearLayout = (LinearLayout) rootView.findViewById(R.id.container_lirikLaguRohani);
            //add LayoutParams
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            myLinearLayout.setOrientation(LinearLayout.VERTICAL);
            params.setMargins(0, 10, 20, 0);

            int dataLength = arr.length();

            int colorBlack = Color.BLACK;

            String container, judul, isi = null;

            // Generate konten LirikLaguRohani dalam loop for
            for (int i = 0; i < dataLength; i++) {
                JSONObject jsonobj = null;
                judul = "";
                try {
                    jsonobj = arr.getJSONObject(i);
                    Log.d("JSONObject", arr.getJSONObject(i).toString());
                    judul = jsonobj.getString("judul");
                    isi = jsonobj.getString("isi");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                container = "LirikLaguRohani " + Integer.toString(i + 1) + " - " + judul;

                // Add Button Judul Lirik Lagu Rohani
                ListLirikLaguRohani = new Button(getActivity());
                ListLirikLaguRohani.setText(container);
                ListLirikLaguRohani.setLayoutParams(params);
                ListLirikLaguRohani.setTextColor(colorBlack);
                ListLirikLaguRohani.setBackgroundColor(0);

                final String _isi = isi;

                // Add button listener here
                ListLirikLaguRohani.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Masuk ke konstruktor parameter LirikLaguRohaniLengkapFragment dengan parameter isi
                                frag = new LirikLaguRohaniLengkapFragment(_isi);
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
            String result = "";
            String status ="";

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(Controller.url+"view_lagusearch.php?kw=" + keyword);
            HttpResponse response;

            try {
                response = client.execute(request);

                // Get the response
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line = "";
                while ((line = rd.readLine()) != null) {
                    result += line;
                }

                Log.d("Result", result);

                try {
                    JSONObject res = new JSONObject(result);
                    arr = res.getJSONArray("data");
                    Log.d("Array", arr.toString());
                    status = "ok";

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
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
                    Log.d("JSONObject", arr.getJSONObject(i).toString());
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
                Log.d("tabel LirikLaguRohani exist! persiapan delete tabel liriklagu..","..");
                DB.deleteTableLirikLaguRohani();
            }
            Log.d("persiapan membuat tabel liriklagu baru..","..");
            DB.createTableLirikLaguRohani();
            Log.d("persiapan insert data pada tabel liriklagu..","..");
            DB.insertDataLirikLaguRohani(tmp);
        }

        @Override
        protected void onPostExecute(String result) {
            myLinearLayout = (LinearLayout) rootView.findViewById(R.id.container_lirikLaguRohani);
            //add LayoutParams
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            myLinearLayout.setOrientation(LinearLayout.VERTICAL);
            params.setMargins(0, 10, 20, 0);

            int dataLength = arr.length();

            int colorBlack = Color.BLACK;

            String container = null, judul = null, isi = null;

            // Cari dari server
            if (!adaLirik) {
                Log.d("!adaLirik", "generate dari server");
                Toast.makeText(getActivity(), "Lagu yang Anda cari: " + keyword + " digenerate dari server", Toast.LENGTH_LONG).show();
                // Generate konten LirikLaguRohani dalam loop for
                for (int i = 0; i < dataLength; i++) {
                    JSONObject jsonobj = null;
                    try {
                        jsonobj = arr.getJSONObject(i);
                        Log.d("JSONObject", arr.getJSONObject(i).toString());
                        judul = jsonobj.getString("judul");
                        isi = jsonobj.getString("isi");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    container = "LirikLaguRohani " + Integer.toString(i + 1) + " - " + judul;

                    // Add Button Judul Lirik Lagu Rohani
                    ListLirikLaguRohani = new Button(getActivity());
                    ListLirikLaguRohani.setText(container);
                    ListLirikLaguRohani.setLayoutParams(params);
                    ListLirikLaguRohani.setTextColor(colorBlack);
                    ListLirikLaguRohani.setBackgroundColor(0);

                    final String _isi = isi;

                    // Add button listener here
                    ListLirikLaguRohani.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Masuk ke konstruktor parameter LirikLaguRohaniLengkapFragment dengan parameter isi
                                    frag = new LirikLaguRohaniLengkapFragment(_isi);
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
                Log.d("adaLirik", "generate dari DB");
                Toast.makeText(getActivity(), "Lagu yang Anda cari: " + keyword + " digenerate dari DB", Toast.LENGTH_LONG).show();
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
                    ListLirikLaguRohani.setLayoutParams(params);
                    ListLirikLaguRohani.setTextColor(colorBlack);
                    ListLirikLaguRohani.setBackgroundColor(0);

                    final String _isi = isi;

                    // Add button listener here
                    ListLirikLaguRohani.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Masuk ke konstruktor parameter LirikLaguRohaniLengkapFragment dengan parameter isi
                                    frag = new LirikLaguRohaniLengkapFragment(_isi);
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
