package fingertech.mobileclientgky;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
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


/**
 * Created by William Stefan Hartono
 */
public class RekamanKhotbahFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View rootView;
    private LinearLayout myLinearLayout;
    private LinearLayout rowLayout;
    private LinearLayout subRowLayout;
    private LinearLayout colLayout;

    private SearchView sv;
    private LinearLayout crk;
    private String keyword = null;

    // Untuk komponen-komponen
    private TextView TitleJudulTV;
    private TextView JudulTV;
    private TextView TitleIsiTV;
    private TextView IsiTV;
    private TextView TitleTanggalTV;
    private TextView TanggalTV;
    private TextView TitlePembicaraTV;
    private TextView PembicaraTV;

    private OnFragmentInteractionListener mListener;

    private Viewer v = new Viewer();

    public static RekamanKhotbahFragment newInstance(String param1, String param2) {
        RekamanKhotbahFragment fragment = new RekamanKhotbahFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RekamanKhotbahFragment() {}

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
        rootView = inflater.inflate(R.layout.fragment_rekaman_khotbah, container, false);
        v.execute();

        sv = (SearchView) rootView.findViewById(R.id.rekamanKhotbah_searchview);
        crk = (LinearLayout) rootView.findViewById(R.id.container_rekamanKhotbah);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                keyword = s;
                Toast.makeText(getActivity(), "Rekaman khotbah yang Anda cari: " + keyword, Toast.LENGTH_LONG).show();

                crk.removeAllViews();
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

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    class Viewer extends AsyncTask<String, String, String> {
        JSONArray arr = new JSONArray();
        public JSONArray getArr() {
            return arr;
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... params) {
            String result = "";

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(Controller.url + "view_kotbah.php");
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

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            myLinearLayout = (LinearLayout) rootView.findViewById(R.id.container_rekamanKhotbah);
            myLinearLayout.setOrientation(LinearLayout.VERTICAL);

            // Add LayoutParams
            LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params0.setMargins(0, 0, 0, 0);

            LinearLayout.LayoutParams paramsJarakJudulDenganIsi = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsJarakJudulDenganIsi.setMargins(0, 10, 0, 0);

            LinearLayout.LayoutParams paramsJarakAntarIsi = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsJarakAntarIsi.setMargins(0, 0, 0, 0);

            rowLayout = new LinearLayout(getActivity());
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);

            // Buat linear layout vertical untuk menampung kata-kata
            colLayout = new LinearLayout(getActivity());
            colLayout.setOrientation(LinearLayout.VERTICAL);
            colLayout.setPadding(0, 10, 10, 0);

            subRowLayout = new LinearLayout(getActivity());
            subRowLayout.setOrientation(LinearLayout.HORIZONTAL);

            int dataLength = arr.length();
            int defaultColor = getResources().getColor(R.color.defaultFont);
            String container, judul = null, isi = null, tanggal = null, pembicara = null;

            // Generate konten Khotbah dalam loop for
            for (int i = 0; i < dataLength; i++) {
                JSONObject jsonobj = null;
                judul = "";
                try {
                    jsonobj = arr.getJSONObject(i);
                    Log.d("JSONObject", arr.getJSONObject(i).toString());
                    judul = jsonobj.getString("judul");
                    isi = jsonobj.getString("isi");
                    tanggal = jsonobj.getString("tanggal");
                    pembicara = jsonobj.getString("pembicara");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Add text View TitleJudulTV
                TitleJudulTV = new TextView(getActivity());
                TitleJudulTV.setText("Judul: ");
                TitleJudulTV.setLayoutParams(paramsJarakJudulDenganIsi);
                TitleJudulTV.setTextColor(getResources().getColor(R.color.defaultFont));
                subRowLayout.addView(TitleJudulTV);

                // Add text View JudulTV
                JudulTV = new TextView(getActivity());
                JudulTV.setText(judul);
                JudulTV.setLayoutParams(paramsJarakJudulDenganIsi);
                subRowLayout.addView(JudulTV);
                colLayout.addView(subRowLayout);
                subRowLayout = new LinearLayout(getActivity());

                // Add text View TitleIsiTV
                TitleIsiTV = new TextView(getActivity());
                TitleIsiTV.setText("Khotbah ");
                TitleIsiTV.setLayoutParams(paramsJarakAntarIsi);
                TitleIsiTV.setTextColor(getResources().getColor(R.color.defaultFont));
                subRowLayout.addView(TitleIsiTV);

                // Add text View IsiTV
                IsiTV = new TextView(getActivity());
                IsiTV.setText(Html.fromHtml("<a href=\"" + isi + "\">" + "dapat di dengarkan di sini" + "</a>"));
                IsiTV.setClickable(true);
                IsiTV.setMovementMethod(LinkMovementMethod.getInstance());
                IsiTV.setLayoutParams(paramsJarakAntarIsi);
                subRowLayout.addView(IsiTV);
                colLayout.addView(subRowLayout);
                subRowLayout = new LinearLayout(getActivity());

                // Add text View TitleTanggalTV
                TitleTanggalTV = new TextView(getActivity());
                TitleTanggalTV.setText("Tanggal: ");
                TitleTanggalTV.setLayoutParams(paramsJarakAntarIsi);
                TitleTanggalTV.setTextColor(getResources().getColor(R.color.defaultFont));
                subRowLayout.addView(TitleTanggalTV);

                // Add text View TanggalTV
                TanggalTV = new TextView(getActivity());
                TanggalTV.setText(tanggal);
                TanggalTV.setLayoutParams(paramsJarakAntarIsi);
                subRowLayout.addView(TanggalTV);
                colLayout.addView(subRowLayout);
                subRowLayout = new LinearLayout(getActivity());

                // Add text View TitlePembicaraTV
                TitlePembicaraTV = new TextView(getActivity());
                TitlePembicaraTV.setText("Pembicara: ");
                TitlePembicaraTV.setLayoutParams(paramsJarakAntarIsi);
                TitlePembicaraTV.setTextColor(getResources().getColor(R.color.defaultFont));
                subRowLayout.addView(TitlePembicaraTV);

                // Add text View PembicaraTV
                PembicaraTV = new TextView(getActivity());
                PembicaraTV.setText(pembicara);
                PembicaraTV.setLayoutParams(paramsJarakAntarIsi);
                subRowLayout.addView(PembicaraTV);
                colLayout.addView(subRowLayout);
                subRowLayout = new LinearLayout(getActivity());

                if (i != dataLength) {
                    rowLayout.addView(colLayout);
                    myLinearLayout.addView(rowLayout);
                    rowLayout = new LinearLayout(getActivity());
                    colLayout = new LinearLayout(getActivity());
                    colLayout.setOrientation(LinearLayout.VERTICAL);
                    subRowLayout = new LinearLayout(getActivity());
                }
            }
        }
    }

    class ViewerSearch extends AsyncTask<String, String, String> {
        JSONArray arr = new JSONArray();
        public JSONArray getArr() {
            return arr;
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... params) {
            String result = "";

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(Controller.url + "view_kotbahsearch.php?kw=" + keyword);
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

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            myLinearLayout = (LinearLayout) rootView.findViewById(R.id.container_rekamanKhotbah);
            myLinearLayout.setOrientation(LinearLayout.VERTICAL);

            // Add LayoutParams
            LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params0.setMargins(0, 0, 0, 0);

            LinearLayout.LayoutParams paramsJarakJudulDenganIsi = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsJarakJudulDenganIsi.setMargins(0, 10, 0, 0);

            LinearLayout.LayoutParams paramsJarakAntarIsi = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsJarakAntarIsi.setMargins(0, 0, 0, 0);

            rowLayout = new LinearLayout(getActivity());
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);

            // Buat linear layout vertical untuk menampung kata-kata
            colLayout = new LinearLayout(getActivity());
            colLayout.setOrientation(LinearLayout.VERTICAL);
            colLayout.setPadding(0, 10, 10, 0);

            subRowLayout = new LinearLayout(getActivity());
            subRowLayout.setOrientation(LinearLayout.HORIZONTAL);

            int dataLength = arr.length();
            int defaultColor = getResources().getColor(R.color.defaultFont);
            String container, judul = null, isi = null, tanggal = null, pembicara = null;

            // Generate konten Khotbah dalam loop for
            for (int i = 0; i < dataLength; i++) {
                JSONObject jsonobj = null;
                judul = "";
                try {
                    jsonobj = arr.getJSONObject(i);
                    Log.d("JSONObject", arr.getJSONObject(i).toString());
                    judul = jsonobj.getString("judul");
                    isi = jsonobj.getString("isi");
                    tanggal = jsonobj.getString("tanggal");
                    pembicara = jsonobj.getString("pembicara");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Add text View TitleJudulTV
                TitleJudulTV = new TextView(getActivity());
                TitleJudulTV.setText("Judul: ");
                TitleJudulTV.setLayoutParams(paramsJarakJudulDenganIsi);
                TitleJudulTV.setTextColor(getResources().getColor(R.color.defaultFont));
                subRowLayout.addView(TitleJudulTV);

                // Add text View JudulTV
                JudulTV = new TextView(getActivity());
                JudulTV.setText(judul);
                JudulTV.setLayoutParams(paramsJarakJudulDenganIsi);
                subRowLayout.addView(JudulTV);
                colLayout.addView(subRowLayout);
                subRowLayout = new LinearLayout(getActivity());

                // Add text View TitleIsiTV
                TitleIsiTV = new TextView(getActivity());
                TitleIsiTV.setText("Khotbah ");
                TitleIsiTV.setLayoutParams(paramsJarakAntarIsi);
                TitleIsiTV.setTextColor(getResources().getColor(R.color.defaultFont));
                subRowLayout.addView(TitleIsiTV);

                // Add text View IsiTV
                IsiTV = new TextView(getActivity());
                IsiTV.setText(Html.fromHtml("<a href=\"" + isi + "\">" + "dapat di dengarkan di sini" + "</a>"));
                IsiTV.setClickable(true);
                IsiTV.setMovementMethod(LinkMovementMethod.getInstance());
                IsiTV.setLayoutParams(paramsJarakAntarIsi);
                subRowLayout.addView(IsiTV);
                colLayout.addView(subRowLayout);
                subRowLayout = new LinearLayout(getActivity());

                // Add text View TitleTanggalTV
                TitleTanggalTV = new TextView(getActivity());
                TitleTanggalTV.setText("Tanggal: ");
                TitleTanggalTV.setLayoutParams(paramsJarakAntarIsi);
                TitleTanggalTV.setTextColor(getResources().getColor(R.color.defaultFont));
                subRowLayout.addView(TitleTanggalTV);

                // Add text View TanggalTV
                TanggalTV = new TextView(getActivity());
                TanggalTV.setText(tanggal);
                TanggalTV.setLayoutParams(paramsJarakAntarIsi);
                subRowLayout.addView(TanggalTV);
                colLayout.addView(subRowLayout);
                subRowLayout = new LinearLayout(getActivity());

                // Add text View TitlePembicaraTV
                TitlePembicaraTV = new TextView(getActivity());
                TitlePembicaraTV.setText("Pembicara: ");
                TitlePembicaraTV.setLayoutParams(paramsJarakAntarIsi);
                TitlePembicaraTV.setTextColor(getResources().getColor(R.color.defaultFont));
                subRowLayout.addView(TitlePembicaraTV);

                // Add text View PembicaraTV
                PembicaraTV = new TextView(getActivity());
                PembicaraTV.setText(pembicara);
                PembicaraTV.setLayoutParams(paramsJarakAntarIsi);
                subRowLayout.addView(PembicaraTV);
                colLayout.addView(subRowLayout);
                subRowLayout = new LinearLayout(getActivity());

                if (i != dataLength) {
                    rowLayout.addView(colLayout);
                    myLinearLayout.addView(rowLayout);
                    rowLayout = new LinearLayout(getActivity());
                    colLayout = new LinearLayout(getActivity());
                    colLayout.setOrientation(LinearLayout.VERTICAL);
                    subRowLayout = new LinearLayout(getActivity());
                }
            }
        }
    }
}
