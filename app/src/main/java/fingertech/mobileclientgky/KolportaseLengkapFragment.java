package fingertech.mobileclientgky;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link KolportaseLengkapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link KolportaseLengkapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KolportaseLengkapFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private LinearLayout myLinearLayout;
    private View rootView;

    private ImageView GambarIV;
    private TextView TitleBukuTV;
    private TextView JudulBukuTV;
    private TextView TitlePengarangTV;
    private TextView JudulPengarangTV;
    private TextView TitleKeteranganTV;
    private TextView IsiKeteranganTV;

    // Untuk komponen-komponen
    private String judul = null, pengarang = null, keterangan = null, linkGambar = null;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KolportaseLengkapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KolportaseLengkapFragment newInstance(String param1, String param2) {
        KolportaseLengkapFragment fragment = new KolportaseLengkapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public KolportaseLengkapFragment() {
        // Required empty public constructor
    }

    public KolportaseLengkapFragment(String _judul, String _pengarang, String _keterangan, String _linkGambar) {
        this.judul = _judul;
        this.pengarang = _pengarang;
        this.keterangan= _keterangan;
        this.linkGambar = _linkGambar;
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
        rootView = inflater.inflate(R.layout.fragment_kolportase_lengkap, container, false);
        // Inflate the layout for this fragment
        /*return inflater.inflate(R.layout.fragment_kolportase_lengkap, container, false);*/
        generateKolportaseLengkap();
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    public void generateKolportaseLengkap() {
        // Add LinearLayout
        View v = rootView.findViewById(R.id.container_kolportaseLengkap);

        myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_kolportaseLengkap);
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

        int colorBlack = Color.BLACK;

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
        TitleBukuTV.setTextColor(colorBlack);
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
        TitlePengarangTV.setTextColor(colorBlack);
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
        TitleKeteranganTV.setTextColor(colorBlack);
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

        rowLayout.addView(colLayout);
        myLinearLayout.addView(rowLayout);
    }

}
