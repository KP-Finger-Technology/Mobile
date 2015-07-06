package fingertech.mobileclientgky;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
public class KomisiPemudaDewasaFragment extends Fragment {
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

    // Untuk load ketika back
    private JSONArray komisiPemudaSaved;
    private JSONArray arrIsiKomisiPemuda;

    public static KomisiPemudaDewasaFragment newInstance(String param1, String param2) {
        KomisiPemudaDewasaFragment fragment = new KomisiPemudaDewasaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public KomisiPemudaDewasaFragment() {}

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
            if (arrIsiKomisiPemuda != null){
                // Returning from backstack, data is fine, do nothing
                generateKontenUI(arrIsiKomisiPemuda);
            }
            else {
                // Newly created, compute data
                Viewer v = new Viewer();
                v.execute();
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
        rootView = inflater.inflate(R.layout.fragment_komisi_pemuda_dewasa, container, false);

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

    private void setUpLayout() {
        myLinearLayout = (LinearLayout)rootView.findViewById(R.id.container_komisiPemudaDewasa);
        imageLayout = (LinearLayout)rootView.findViewById(R.id.container_komisiPemudaDewasa_image);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
        params.setMargins(0, 0, 0, 4);
    }

    private void fillTextViewHeader (String target) {
        TextView TV = new TextView(getActivity());
        TV.setText(target);
        TV.setTextAppearance(getActivity().getApplicationContext(), R.style.headerDefault);
        TV.setLayoutParams(params);
        myLinearLayout.addView(TV);
    }

    private void fillTextView (String target) {
        TextView TV = new TextView(getActivity());
        TV.setText(target);
        TV.setLayoutParams(params);
        myLinearLayout.addView(TV);
    }

    private void generateKontenUI (JSONArray arrIsi) {
        setUpLayout();

        String linkGambar = null, isiLokasi = null, isiPembina = null, isiJadwal = null;
        String isiKebaktian = null, isiKelas = null, isiWaktuMulai = null, isiWaktuSelesai = null, isiGedung = null, isiTempat = null;

        if (arrIsi != null) {
            // Ambil string yang diperlukan dari JSONArray json_arrIsi
            try {
                isiLokasi = arrIsi.getJSONObject(0).getString("lokasi");
                isiPembina = arrIsi.getJSONObject(0).getString("pembimbing");
                isiJadwal = arrIsi.getJSONObject(0).getString("waktu");
                linkGambar = Controller.urlgambar;
                linkGambar += arrIsi.getJSONObject(0).getString("gambar");

                // Memasang Gambar
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                int image_width = display.getWidth();
                int image_height = (int) (display.getHeight() / 4.3);

                ImageView GambarIV = (ImageView) rootView.findViewById(R.id.image_komisiPemudaDewasa);
                if (GambarIV.getParent() != null)
                    ((ViewGroup) GambarIV.getParent()).removeView(GambarIV);
                Picasso.with(getActivity())
                        .load(linkGambar)
                        .resize(image_width, image_height)
                        .centerCrop()
                        .into(GambarIV);
                GambarIV.setLayoutParams(params);
                imageLayout.addView(GambarIV);

                fillTextViewHeader("Lokasi");
                fillTextView(isiLokasi);

                fillTextViewHeader("Pembina");
                fillTextView(isiPembina);

                fillTextViewHeader("Jadwal");
                fillTextView(isiJadwal);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class Viewer extends AsyncTask<String, String, String> {
        JSONArray arrData = new JSONArray();
        String id = "4";

        ProgressDialog progressDialog;

        public JSONArray getArrData() {
            return arrData;
        }

        JSONArray arrIsi = new JSONArray();
        public JSONArray getArrIsi() {
            return arrIsi;
        }

        @Override
        protected void onPreExecute()
        {
            progressDialog = ProgressDialog.show(getActivity(),"Loading", "Koneksi ke server");
        };

        @Override
        protected String doInBackground(String... params) {
            SessionManager sm = new SessionManager(getActivity().getApplicationContext());

            String result = "";
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(Controller.url + "view_komisi.php?id=" + id);
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
                    arrIsi = res.getJSONArray("isikomisi");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            /*komisiPemudaSaved = new JSONArray();
            komisiPemudaSaved = arrData;*/
            arrIsiKomisiPemuda = arrIsi;
            generateKontenUI(arrIsi);
        }
    }
}
