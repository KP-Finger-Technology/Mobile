package fingertech.mobileclientgky;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
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
public class KebaktianDoaFragment extends Fragment {
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
    private JSONArray kebaktianDoaSaved;

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            // Probably orientation change
        }
        else {
            if (kebaktianDoaSaved != null) {
                // Returning from backstack, data is fine, do nothing
                generateKontenUI(kebaktianDoaSaved);
            }
            else {
                // Newly created, compute data
                Viewer viewer = new Viewer();
                viewer.execute();
            }
        }
    }

    public static KebaktianDoaFragment newInstance(String param1, String param2) {
        KebaktianDoaFragment fragment = new KebaktianDoaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public KebaktianDoaFragment() {}

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
        rootView = inflater.inflate(R.layout.fragment_kebaktian_doa, container, false);
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
        myLinearLayout = (LinearLayout)rootView.findViewById(R.id.container_kebaktianDoa);
        imageLayout = (LinearLayout)rootView.findViewById(R.id.container_kebaktianDoa_Image);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
        params.setMargins(0, 0, 0, 4);
    }

	// Fungsi untuk membuat judul
    private void fillTextViewHeader (String target) {
        TextView TV = new TextView(getActivity());
        TV.setText(target);
        TV.setTextAppearance(getActivity().getApplicationContext(), R.style.headerDefault);
        TV.setLayoutParams(params);
        myLinearLayout.addView(TV);
    }

	// Fungsi untuk memberi isi
    private void fillTextView (String target) {
        TextView TV = new TextView(getActivity());
        TV.setText(target);
        TV.setLayoutParams(params);
        myLinearLayout.addView(TV);
    }

	// Fungsi untuk generate komponen-komponen tampilan
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
            pembina = json_arr.getJSONObject(0).getString("pembina");

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
            imageLayout.addView(gambarIV);

            fillTextViewHeader(headerJadwal1); // Pasang Header Jadwal 1
            fillTextView(isiJadwal1); // Pasang Isi Jadwal 1
            fillTextViewHeader(headerJadwal2); // Pasang Header Jadwal 2
            fillTextView(isiJadwal2); // Pasang Isi Jadwal 2
            fillTextViewHeader("Pembina"); // Pasang TextView "Pembina"
            fillTextView(pembina); // Pasang isi Pembina
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class Viewer extends AsyncTask<String, String, String> {
        JSONArray arr = new JSONArray();
        String idKebaktianDoa = "2";
        boolean isStatusOK;

        ProgressDialog progressDialog;

        public JSONArray getArr() {
            return arr;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(),"Loading", "Koneksi ke server");
        };

        @Override
        protected String doInBackground(String... params) {
            SessionManager sm = new SessionManager(getActivity().getApplicationContext());

            String result = "";
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(Controller.url + "view_pelayanan.php?id=" + idKebaktianDoa);
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

                    // Cek Status
                    String statusString = res.getString("status");
                    if (statusString.equals("ok"))
                        isStatusOK = true;
                    else
                        isStatusOK = false;
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
            if (isStatusOK) {
                kebaktianDoaSaved = new JSONArray();
                kebaktianDoaSaved = arr;
                generateKontenUI(arr);
            }
            else
                Toast.makeText(getActivity(), "Ada kesalahan pada koneksi internet atau kesalahan pada server, silahkan coba lagi", Toast.LENGTH_SHORT).show();
        }
    }
}
