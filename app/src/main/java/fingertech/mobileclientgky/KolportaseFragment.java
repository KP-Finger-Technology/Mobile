package fingertech.mobileclientgky;

import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JadwalPelayananFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JadwalPelayananFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KolportaseFragment extends Fragment {
    private Fragment frag;
    private FragmentTransaction fragTransaction;
    private FragmentManager fragManager;

    private String judul;
    private String pengarang;
    private String keterangan;
    private String gambar;

    public String getJudul() {
        return judul;
    }

    public String getPengarang() {
        return pengarang;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getGambar() {
        return gambar;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public void setPengarang(String pengarang) {
        this.pengarang = pengarang;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Untuk komponen-komponen
    private ImageView GambarIV;
    private TextView TitleBukuTV;
    private TextView JudulBukuTV;
    private TextView TitlePengarangTV;
    private TextView JudulPengarangTV;
    private TextView TitleKeteranganTV;
    private TextView IsiKeteranganTV;
    private Button SelengkapnyaBtn;
    private View rootView;

    // Controller cont = new Controller();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JadwalPelayananFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KolportaseFragment newInstance(String param1, String param2) {
        KolportaseFragment fragment = new KolportaseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public KolportaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Viewer v = new Viewer();
        v.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        View rootView = inflater.inflate(R.layout.fragment_jadwal_pelayanan, container, false);
//        LinearLayout myLinearLayout;
//        Log.d("masuk 1:", "yes");
//        myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_jadwalPelayanan);
//        Log.d("masuk 2:", "yes");
//        //add LayoutParams
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
//        Log.d("masuk 3:", "yes");
//
//        Button dummy = new Button(getActivity());
//        dummy.setText("dummy!");
//        dummy.setLayoutParams(params);
//        myLinearLayout.addView(dummy);

        rootView = inflater.inflate(R.layout.fragment_kolportase, container, false);
//        generateKolportaseContent();

        // Inflate the layout for this fragment
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


    class Viewer extends AsyncTask<String, String, String> {
        private LinearLayout myLinearLayout;
        private ImageView GambarIV;
        private TextView TitleKeteranganTV;
        private TextView IsiKeteranganTV;
        private Button SelengkapnyaBtn;

        JSONArray arr = new JSONArray();

        public JSONArray getArr() {
            return arr;
        }

        @Override
        protected void onPreExecute()
        {
        };

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            String statu ="";
//            for (String urlp : params) {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(Controller.url+"view_kolportase.php"); // ngikutin ip disini loh
            HttpResponse response;

            try {

                response = client.execute(request);

                // Get the response
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line = "";
                while ((line = rd.readLine()) != null) {
                    result += line;
                }
//            result = result.substring(result.indexOf("{"), result.indexOf("}") + 1);
                Log.d("Result", result);

                try {
                    JSONObject res = new JSONObject(result);
                    arr = res.getJSONArray("data");
                    Log.d("Array", arr.toString());
                    statu = "ok";

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

//            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            String judul = null, pengarang = null, keterangan = null, linkGambar = null;

            // Add LinearLayout
            View v = rootView.findViewById(R.id.container_kolportase);

            myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_kolportase);
            // Add LayoutParams
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            myLinearLayout.setOrientation(LinearLayout.VERTICAL);
            params.setMargins(0, 10, 20, 0);

            LinearLayout rowLayout = new LinearLayout(getActivity());
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);

            // Membuat linear layout vertical untuk menampung kata-kata
            LinearLayout colLayout = new LinearLayout(getActivity());
            colLayout.setOrientation(LinearLayout.VERTICAL);
            colLayout.setPadding(0,10,10,0);

            LinearLayout subRowLayout = new LinearLayout(getActivity());
            subRowLayout.setOrientation(LinearLayout.HORIZONTAL);


            LinearLayout.LayoutParams parameter = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            Display display = getActivity().getWindowManager().getDefaultDisplay();
            int image_width = display.getWidth()/3;
            int image_height = (int) (display.getHeight()/4.3);

            int colorWhite = Color.WHITE;

            int dataLength = arr.length();

            // Generate konten Kolportase dalam loop for
            for (int i = 0; i < dataLength; i++) {
                JSONObject jsonobj = null;
                try {
                    jsonobj = arr.getJSONObject(i);
                    Log.d("JSONObject", arr.getJSONObject(i).toString());
                    judul = jsonobj.getString("judulbuku");
                    pengarang = jsonobj.getString("pengarang");
                    keterangan = jsonobj.getString("keterangan");
                    linkGambar = Controller.url + "res/kolportase/";
                    linkGambar += jsonobj.getString("gambarbuku");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //add image View
                ImageView GambarIV = new ImageView(getActivity());

                //Loading image from below url into imageView
                Picasso.with(getActivity())
                        .load(linkGambar)
                        .resize(image_height, image_width)
                        .into(GambarIV);
                GambarIV.setLayoutParams(params);
                rowLayout.addView(GambarIV);

                // Add textView TitleBukuTV
                TitleBukuTV = new TextView(getActivity());
                TitleBukuTV.setText("Judul: ");
                TitleBukuTV.setLayoutParams(params);
                TitleBukuTV.setTextColor(colorWhite);
                subRowLayout.addView(TitleBukuTV);

                // Add textView JudulBukuTV
                JudulBukuTV = new TextView(getActivity());
                JudulBukuTV.setText(judul);
                JudulBukuTV.setLayoutParams(params);
                subRowLayout.addView(JudulBukuTV);
                colLayout.addView(subRowLayout);
                subRowLayout = new LinearLayout(getActivity());

                // Add textView TitlePengarangTV
                TitlePengarangTV = new TextView(getActivity());
                TitlePengarangTV.setText("Pengarang: ");
                TitlePengarangTV.setLayoutParams(params);
                TitlePengarangTV.setTextColor(colorWhite);
                subRowLayout.addView(TitlePengarangTV);

                // Add textView JudulPengarangTV
                JudulPengarangTV = new TextView(getActivity());
                JudulPengarangTV.setText(pengarang);
                JudulPengarangTV.setLayoutParams(params);
                subRowLayout.addView(JudulPengarangTV);
                colLayout.addView(subRowLayout);
                subRowLayout = new LinearLayout(getActivity());

                // Add textView TitleKeteranganTV
                TitleKeteranganTV = new TextView(getActivity());
                TitleKeteranganTV.setText("Keterangan: ");
                TitleKeteranganTV.setTextColor(colorWhite);
                TitleKeteranganTV.setLayoutParams(params);
                subRowLayout.addView(TitleKeteranganTV);

                // Add text View IsiKeteranganTV
                IsiKeteranganTV = new TextView(getActivity());
                if (keterangan.length()>80) {
                    keterangan = keterangan.substring(0, 80);
                    keterangan = keterangan + "...";
                }
                IsiKeteranganTV.setText(keterangan);
                IsiKeteranganTV.setLayoutParams(params);
                colLayout.addView(subRowLayout);
                subRowLayout = new LinearLayout(getActivity());

                // Add selengkapnya button
                SelengkapnyaBtn = new Button(getActivity());
                SelengkapnyaBtn.setText("Selengkapnya");
                SelengkapnyaBtn.setLayoutParams(params);
                SelengkapnyaBtn.setBackgroundColor(0);
                subRowLayout.addView(SelengkapnyaBtn);
                colLayout.addView(subRowLayout);

                final String finalJudul = judul;
                final String finalPengarang = pengarang;
                final String finalKeterangan = keterangan;
                final String finalLinkGambar = linkGambar;
                SelengkapnyaBtn.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // masuk ke konstruktor parameter LirikLaguRohaniLengkapFragment dgn parameternya: isi
                                Log.d("Kolportase: ", "masuk onClickListener");
                                frag = new KolportaseLengkapFragment(finalJudul, finalPengarang, finalKeterangan, finalLinkGambar);
                                fragManager = getActivity().getSupportFragmentManager();
                                fragTransaction = fragManager.beginTransaction();
                                fragTransaction.replace(R.id.container, frag);
                                fragTransaction.addToBackStack(null);
                                fragTransaction.commit();
                                Log.d("Kolportase: ", "selesai onClickListener");
                            }
                        }
                );

                if (i!=dataLength) {
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

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

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
}
