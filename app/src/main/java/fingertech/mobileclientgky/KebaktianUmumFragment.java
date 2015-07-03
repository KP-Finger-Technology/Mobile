package fingertech.mobileclientgky;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
public class KebaktianUmumFragment extends Fragment {
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

    private JSONArray kebaktianUmumSaved;

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
            if (kebaktianUmumSaved != null) {
                // Returning from backstack, data is fine, do nothing
                generateKontenUI(kebaktianUmumSaved);
            }
            else {
                // Newly created, compute data
                Viewer viewer = new Viewer();
                viewer.execute();
            }
        }
    }

    public static KebaktianUmumFragment newInstance(String param1, String param2) {
        KebaktianUmumFragment fragment = new KebaktianUmumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public KebaktianUmumFragment() {}

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
        rootView = inflater.inflate(R.layout.fragment_kebaktian_umum, container, false);
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
        myLinearLayout = (LinearLayout)rootView.findViewById(R.id.container_kebaktianUmum);
        imageLayout = (LinearLayout)rootView.findViewById(R.id.container_kebaktianUmum_Image);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
        params.setMargins(0, 0, 0, 4);
    }

    private void fillTextViewHeader (String target) {
        TextView TV = new TextView(getActivity());
        TV.setText(target);
        TV.setTextAppearance(getActivity().getApplicationContext(), R.style.headerKomisiPelayanan);
        TV.setLayoutParams(params);
        myLinearLayout.addView(TV);
    }

    private void fillTextView (String target) {
        TextView TV = new TextView(getActivity());
        TV.setText(target);
        TV.setLayoutParams(params);
        myLinearLayout.addView(TV);
    }

    private void generateKontenUI (JSONArray json_arr) {
        setUpLayout();

        String linkGambar = null, headerJadwal1 = null, headerJadwal2 = null, isiJadwal1 = null, isiJadwal2 = null, pembina = null;
        try {
            // Ambil string yang diperlukan dari JSON Array
            linkGambar = Controller.urlgambar ;
            linkGambar += json_arr.getJSONObject(0).getString("gambar");
            headerJadwal1 = json_arr.getJSONObject(0).getString("header1");
            headerJadwal2 = json_arr.getJSONObject(0).getString("header2");
            isiJadwal1 = json_arr.getJSONObject(0).getString("jadwal1");
            isiJadwal2 = json_arr.getJSONObject(0).getString("jadwal2");

            // Pasang gambar
            ImageView GambarIV = (ImageView)rootView.findViewById(R.id.image_kebaktianUmum);
            if (GambarIV.getParent() != null)
                ((ViewGroup) GambarIV.getParent()).removeView(GambarIV);
            Picasso.with(getActivity())
                    .load(linkGambar)
                    .into(GambarIV);
            GambarIV.setLayoutParams(params);
            imageLayout.addView(GambarIV);

            // Pasang Header Jadwal 1
            fillTextViewHeader(headerJadwal1);
            // Pasang Isi Jadwal 1
            fillTextView(isiJadwal1);
            // Pasang Header Jadwal 2
            fillTextViewHeader(headerJadwal2);
            // Pasang Isi Jadwal 2
            fillTextView(isiJadwal2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class Viewer extends AsyncTask<String, String, String> {
        JSONArray arr = new JSONArray();
        String idKebaktianUmum = "3";

        ProgressDialog progressDialog;

        public JSONArray getArr() {
            return arr;
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
            HttpGet request = new HttpGet(Controller.url + "view_pelayanan.php?id="+idKebaktianUmum);
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
                    Log.d("Array", arr.toString());
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
            kebaktianUmumSaved = new JSONArray();
            kebaktianUmumSaved = arr;
            generateKontenUI(arr);
            progressDialog.dismiss();
        }
    }
}
