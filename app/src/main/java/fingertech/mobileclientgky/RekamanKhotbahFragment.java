package fingertech.mobileclientgky;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private TextView titleJudulTV;
    private TextView judulTV;
    private TextView titleIsiTV;
    private TextView isiTV;
    private TextView titleTanggalTV;
    private TextView tanggalTV;
    private TextView titlePembicaraTV;
    private TextView pembicaraTV;
    private Button downloadButton;

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
        /*sv.setIconified(false);*/
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

                // Add text View titleJudulTV
                titleJudulTV = new TextView(getActivity());
                titleJudulTV.setText("Judul: ");
                titleJudulTV.setLayoutParams(paramsJarakJudulDenganIsi);
                titleJudulTV.setTextColor(getResources().getColor(R.color.defaultFont));
                subRowLayout.addView(titleJudulTV);

                // Add text View judulTV
                judulTV = new TextView(getActivity());
                judulTV.setText(judul);
                judulTV.setLayoutParams(paramsJarakJudulDenganIsi);
                subRowLayout.addView(judulTV);
                colLayout.addView(subRowLayout);
                subRowLayout = new LinearLayout(getActivity());

                // Add text View titleIsiTV
                titleIsiTV = new TextView(getActivity());
                titleIsiTV.setText("Khotbah ");
                titleIsiTV.setLayoutParams(paramsJarakAntarIsi);
                titleIsiTV.setTextColor(getResources().getColor(R.color.defaultFont));
                subRowLayout.addView(titleIsiTV);

                // Add text View isiTV
                isiTV = new TextView(getActivity());
                isiTV.setText(Html.fromHtml("<a href=\"" + Controller.urlaudio + isi + "\">" + "dapat di dengarkan di sini" + "</a>"));
                isiTV.setClickable(true);
                isiTV.setMovementMethod(LinkMovementMethod.getInstance());
                isiTV.setLayoutParams(paramsJarakAntarIsi);
                subRowLayout.addView(isiTV);
                colLayout.addView(subRowLayout);
                subRowLayout = new LinearLayout(getActivity());

                // Add text View titleTanggalTV
                titleTanggalTV = new TextView(getActivity());
                titleTanggalTV.setText("Tanggal: ");
                titleTanggalTV.setLayoutParams(paramsJarakAntarIsi);
                titleTanggalTV.setTextColor(getResources().getColor(R.color.defaultFont));
                subRowLayout.addView(titleTanggalTV);

                // Add text View tanggalTV
                tanggalTV = new TextView(getActivity());
                tanggalTV.setText(tanggal);
                tanggalTV.setLayoutParams(paramsJarakAntarIsi);
                subRowLayout.addView(tanggalTV);
                colLayout.addView(subRowLayout);
                subRowLayout = new LinearLayout(getActivity());

                // Add text View titlePembicaraTV
                titlePembicaraTV = new TextView(getActivity());
                titlePembicaraTV.setText("Pembicara: ");
                titlePembicaraTV.setLayoutParams(paramsJarakAntarIsi);
                titlePembicaraTV.setTextColor(getResources().getColor(R.color.defaultFont));
                subRowLayout.addView(titlePembicaraTV);

                // Add text View pembicaraTV
                pembicaraTV = new TextView(getActivity());
                pembicaraTV.setText(pembicara);
                pembicaraTV.setLayoutParams(paramsJarakAntarIsi);
                subRowLayout.addView(pembicaraTV);
                colLayout.addView(subRowLayout);
                subRowLayout = new LinearLayout(getActivity());

                // Add button for download
                final String linkDownload = Controller.urlaudio + isi;
                final String finalTanggal = tanggal;
                final String finalJudul = judul;

                downloadButton = new Button(getActivity());
                downloadButton.setText("Download");
                downloadButton.setTextColor(getResources().getColor(R.color.white));
                downloadButton.setBackgroundColor(getResources().getColor(R.color.header));
                subRowLayout.addView(downloadButton);
                colLayout.addView(subRowLayout);
                subRowLayout = new LinearLayout(getActivity());

                downloadButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DownloadFiles(linkDownload, finalTanggal, finalJudul);
                            }
                        }
                );

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

                // Add text View titleJudulTV
                titleJudulTV = new TextView(getActivity());
                titleJudulTV.setText("Judul: ");
                titleJudulTV.setLayoutParams(paramsJarakJudulDenganIsi);
                titleJudulTV.setTextColor(getResources().getColor(R.color.defaultFont));
                subRowLayout.addView(titleJudulTV);

                // Add text View judulTV
                judulTV = new TextView(getActivity());
                judulTV.setText(judul);
                judulTV.setLayoutParams(paramsJarakJudulDenganIsi);
                subRowLayout.addView(judulTV);
                colLayout.addView(subRowLayout);
                subRowLayout = new LinearLayout(getActivity());

                // Add text View titleIsiTV
                titleIsiTV = new TextView(getActivity());
                titleIsiTV.setText("Khotbah ");
                titleIsiTV.setLayoutParams(paramsJarakAntarIsi);
                titleIsiTV.setTextColor(getResources().getColor(R.color.defaultFont));
                subRowLayout.addView(titleIsiTV);

                // Add text View isiTV
                isiTV = new TextView(getActivity());
                isiTV.setText(Html.fromHtml("<a href=\"" + Controller.urlaudio + isi + "\">" + "dapat di dengarkan di sini" + "</a>"));
                isiTV.setClickable(true);
                isiTV.setMovementMethod(LinkMovementMethod.getInstance());
                isiTV.setLayoutParams(paramsJarakAntarIsi);
                subRowLayout.addView(isiTV);
                colLayout.addView(subRowLayout);
                subRowLayout = new LinearLayout(getActivity());

                // Add text View titleTanggalTV
                titleTanggalTV = new TextView(getActivity());
                titleTanggalTV.setText("Tanggal: ");
                titleTanggalTV.setLayoutParams(paramsJarakAntarIsi);
                titleTanggalTV.setTextColor(getResources().getColor(R.color.defaultFont));
                subRowLayout.addView(titleTanggalTV);

                // Add text View tanggalTV
                tanggalTV = new TextView(getActivity());
                tanggalTV.setText(tanggal);
                tanggalTV.setLayoutParams(paramsJarakAntarIsi);
                subRowLayout.addView(tanggalTV);
                colLayout.addView(subRowLayout);
                subRowLayout = new LinearLayout(getActivity());

                // Add text View titlePembicaraTV
                titlePembicaraTV = new TextView(getActivity());
                titlePembicaraTV.setText("Pembicara: ");
                titlePembicaraTV.setLayoutParams(paramsJarakAntarIsi);
                titlePembicaraTV.setTextColor(getResources().getColor(R.color.defaultFont));
                subRowLayout.addView(titlePembicaraTV);

                // Add text View pembicaraTV
                pembicaraTV = new TextView(getActivity());
                pembicaraTV.setText(pembicara);
                pembicaraTV.setLayoutParams(paramsJarakAntarIsi);
                subRowLayout.addView(pembicaraTV);
                colLayout.addView(subRowLayout);
                subRowLayout = new LinearLayout(getActivity());

                // Add button for download
                final String linkDownload = Controller.urlaudio + isi;
                final String finalTanggal = tanggal;
                final String finalJudul = judul;

                downloadButton = new Button(getActivity());
                downloadButton.setText("Download");
                downloadButton.setTextColor(getResources().getColor(R.color.white));
                downloadButton.setBackgroundColor(getResources().getColor(R.color.header));

                downloadButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DownloadFiles(linkDownload, finalTanggal, finalJudul);
                            }
                        }
                );

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

    // Untuk download rekaman khotbah
    private void DownloadFiles(String url, String deskripsi, String judul) {
        String urlWeb = url;

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlWeb.replace("https://", "http://")));
        request.setDescription(deskripsi);
        request.setTitle(judul);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, deskripsi + judul);

        DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }
}