package fingertech.mobileclientgky;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JadwalPelayananFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JadwalPelayananFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KolportaseFragment extends Fragment {
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
    private LinearLayout myLinearLayout;
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
        /*cont.viewJadwalPelayanan();
        Log.d("Jadwal ", cont.getArrData().toString());*/
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

        rootView = inflater.inflate(R.layout.fragment_event, container, false);
        generateKolportaseContent();

        // Inflate the layout for this fragment
        return rootView;
    }

    private void generateKolportaseContent() {
        //        try{
//        Controller C = new Controller();
//        C.viewEvent();
//        JSONArray dataArr2 = C.Viewer.arr;
//        Controller.Viewer.execute(Controller.url);
//        JSONArray dataArr = Controller.Viewer.arr;
        String x = "{\"data\":[{\"judul\":\"event judul\",\"tanggal\":\"2015-05-05\",\"keterangan\":\"event keterangan\",\"gambarevent\":\"event.jpg\"},{\"judul\":\"event judul2\",\"tanggal\":\"2015-05-05\",\"keterangan\":\"event keterangan2\",\"gambarevent\":\"event2.jpg\"}]}";
//            JSONArray data = new JSONArray(x);
//            JSONArray data = result.getJSONArray("data");
//            int dataLength = data.length();
//            JSONObject temp = null;
        String judul, pengarang, keterangan, linkGambar;

        // Add LinearLayout
        myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_event);
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

        int dataLength = 3;

        LinearLayout.LayoutParams parameter = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int image_width = display.getWidth()/3;
        int image_height = (int) (display.getHeight()/4.3);

        int colorWhite = Color.WHITE;

        // Generate konten Kolportase dalam loop for
        for (int i=0; i<dataLength; i++){
//                temp = data.getJSONObject(i);
//                judul = temp.getString("judul");
//                tanggal = temp.getString("tanggal");
//                keterangan = temp.getString("keterangan");
//                linkGambar = temp.getString("gambarevent");
            judul = "Kehidupan Seorang Maso";
            pengarang = "Rita Sarah";
            keterangan = "Buku ini menceritakan kehidupan seorang gadis maso yang memiliki ketertarikan lebih terhadap lifestyle yang maso selagi menjalani masa-masa perkuliahan yang berat.";
            linkGambar = "www.masorita.com";

            // Add imageView
            GambarIV = new ImageView(getActivity());
            GambarIV.setBackgroundColor(923423432);

//                GambarIV.setPadding(0,10,10,0);
            GambarIV.setMinimumWidth(image_width);
            GambarIV.setMinimumHeight(image_height);
            GambarIV.setMaxHeight(image_height);
            GambarIV.setMaxWidth(image_width);
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
            JudulPengarangTV.setText(judul);
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

            if (i!=dataLength) {
                rowLayout.addView(colLayout);
                myLinearLayout.addView(rowLayout);
                rowLayout = new LinearLayout(getActivity());
                colLayout = new LinearLayout(getActivity());
                colLayout.setOrientation(LinearLayout.VERTICAL);
                subRowLayout = new LinearLayout(getActivity());
            }
        }
//        } catch(JSONException e){e.printStackTrace();}
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
