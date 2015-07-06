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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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
public class KomisiAnakFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View rootView;
    private LinearLayout myLinearLayout;
    private LinearLayout rowLayout;
    private LinearLayout colLayout;
    private LinearLayout subRowLayout;

    private TableLayout myTableLayout;
    private TableRow TR;
    private TextView JudulTabel;
    private TextView IsiTabelHeader;
    private TextView IsiTabel;
    private LinearLayout.LayoutParams params;
    private HorizontalScrollView HSV;
    private TableLayout.LayoutParams tableParams;
    private TableLayout.LayoutParams rowTableParams;

    private JSONArray arrIsiKomisiAnak;
    private JSONArray arrDataKomisiAnak;

    private OnFragmentInteractionListener mListener;

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putStringArrayList("kppkSaved",kppkSaved);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            // Probably orientation change
//            kppkSaved = savedInstanceState.getStringArrayList("kppkSaved");
        }
        else {
            if ((arrIsiKomisiAnak != null) && (arrDataKomisiAnak != null)){
                // Returning from backstack, data is fine, do nothing
                generateKontenUI(arrIsiKomisiAnak, arrDataKomisiAnak);
            }
            else {
                // Newly created, compute data
                Viewer v = new Viewer();
                v.execute();
            }
        }
    }

    public static KomisiAnakFragment newInstance(String param1, String param2) {
        KomisiAnakFragment fragment = new KomisiAnakFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public KomisiAnakFragment() {}

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
        rootView = inflater.inflate(R.layout.fragment_komisi_anak, container, false);
        myLinearLayout = (LinearLayout) rootView.findViewById(R.id.container_komisiAnak);

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
        myLinearLayout = (LinearLayout) rootView.findViewById(R.id.container_komisiAnak);

        // Add LayoutParams
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
        params.setMargins(0, 10, 20, 0);

        tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        rowTableParams = new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
    }

    private void createHeaderTable() {
        myTableLayout = new TableLayout(getActivity());
        myTableLayout.setLayoutParams(tableParams);
        HSV = new HorizontalScrollView(getActivity());

        TR = new TableRow(getActivity());
        TR.setLayoutParams(tableParams);

        IsiTabelHeader("Kebaktian/Waktu");
        IsiTabelHeader("Kelas");
        IsiTabelHeader("Tempat");
        myTableLayout.addView(TR);      // Add row to table
    }

    private void fillingTable(String kebaktianWaktu, String kelas, String tempat) {
        TR = new TableRow(getActivity());
        TR.setLayoutParams(rowTableParams);
        IsiTabel(kebaktianWaktu);      // Tanggal
        IsiTabel(kelas);       // Gedung
        IsiTabel(tempat);    // Kebaktian
        // Add row to table
        myTableLayout.addView(TR, tableParams);
    }

    private void createTitleTable(String text) {
        TextView TV = new TextView(getActivity());
        TV.setText(text);
        TV.setLayoutParams(params);
//            TV.setTextAppearance(getActivity().getApplicationContext(), R.style.headerKomisiPelayanan);
        myLinearLayout.addView(TV);
    }

    private void generateKontenUI (JSONArray arrIsi, JSONArray arrData) {
        setUpLayout();

        if (arrIsi != null) {
            // Mengambil isi JSON dari array isikomisi
            try {
                String lokasi = arrIsi.getJSONObject(0).getString("lokasi");
                String pembina = arrIsi.getJSONObject(0).getString("pembimbing");
                String linkGambar = Controller.urlgambar;
                linkGambar += arrIsi.getJSONObject(0).getString("gambar");

                // Memasang Gambar
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                int image_width = display.getWidth();
                int image_height = (int) (display.getHeight() / 4.3);

                // Loading image from below url into imageView
                ImageView gambarIV = new ImageView(getActivity());
                Picasso.with(getActivity())
                        .load(linkGambar)
                        .resize(image_width, image_height)
                        .centerCrop()
                        .into(gambarIV);
                gambarIV.setLayoutParams(params);
                LinearLayout imageLayout = (LinearLayout) rootView.findViewById(R.id.container_komisiAnak_image);
                imageLayout.addView(gambarIV);

                //
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (arrData != null) {
            // antisipasi jika kode ini mau dipakai di berbagai komisi, ada komisi yg tidak ada JSON "data"
            // Membuat Tabel
            int dataLength = arrData.length();
            for (int i = 0; i < dataLength; i++) {
                // Looping membuat 1 tabel
                JSONObject jsonobj = null;
                try {

                    jsonobj = arrData.getJSONObject(i);
                    String tempat = jsonobj.getString("tempat");
                    // Membuat title tabel
                    createTitleTable(tempat);
                    // Membuat header tabel
                    createHeaderTable();

                    JSONArray jsonArrAtr = jsonobj.getJSONArray("atribut");
                    int length2 = jsonArrAtr.length();
                    String kebaktian_tmp = "";
                    for (int j = 0; j < length2; j++) {
                        // Looping mengisi row tabel
                        String kebaktian = jsonArrAtr.getJSONObject(j).getString("kebaktian");
                        String gedung = jsonArrAtr.getJSONObject(j).getString("gedung");
                        String waktuMulai = jsonArrAtr.getJSONObject(j).getString("waktumulai");
                        String waktuSelesai = jsonArrAtr.getJSONObject(j).getString("waktuselesai");
                        String kelas = jsonArrAtr.getJSONObject(j).getString("kelas");
                        String kebaktianWaktu = null;

                        if (kebaktianWaktu == kebaktian_tmp)
                            kebaktianWaktu = "";
                        else
                            kebaktianWaktu = kebaktian + " (" + waktuMulai + "-" + waktuSelesai + ")";
                        kebaktian_tmp = kebaktianWaktu;

                        fillingTable(kebaktianWaktu, kelas, gedung);
                    }
                    HSV.addView(myTableLayout);
                    myLinearLayout.addView(HSV);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Viewer extends AsyncTask<String, String, String> {
        private String idKomisiAnak = "1";

        JSONArray arrData = new JSONArray();
        public JSONArray getArrData() {
            return arrData;
        }

        JSONArray arrIsi = new JSONArray();
        public JSONArray getArrIsi() {
            return arrIsi;
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... params) {
            if(isNetworkAvailable()) {
                String result = "";
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(Controller.url + "view_komisi.php?id="+idKomisiAnak);
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
                        arrIsi = res.getJSONArray("isikomisi");
                        Log.d("Komisi anak isikomisi", arrIsi.toString());

                        arrData = res.getJSONArray("data");
                        Log.d("Komisi anak data", arrData.toString());
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
            if (arrData.length() == 0 && isNetworkAvailable()){
                Toast.makeText(getActivity().getApplicationContext(), "Tidak ada jadwal komisi Anak", Toast.LENGTH_SHORT).show();
            }
            arrIsiKomisiAnak = arrIsi;
            arrDataKomisiAnak = arrData;
            generateKontenUI(arrIsi, arrData);
        }
    }
}
