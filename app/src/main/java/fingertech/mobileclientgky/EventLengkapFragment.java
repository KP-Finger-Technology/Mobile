package fingertech.mobileclientgky;

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
 * Created by Andarias Silvanus
 */
public class EventLengkapFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private LinearLayout myLinearLayout;
    private View rootView;

    private TextView TitleEventTV;
    private TextView JudulEventTV;
    private TextView TitleTanggalTV;
    private TextView JudulTanggalTV;
    private TextView TitleWaktuTV;
    private TextView JudulWaktuTV;
    private TextView TitleKeteranganTV;
    private TextView IsiKeteranganTV;
    int colorBlack = Color.BLACK;
    private String judul = null, tanggal = null, keterangan = null, linkGambar = null;

    public static EventLengkapFragment newInstance(String param1, String param2) {
        EventLengkapFragment fragment = new EventLengkapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public EventLengkapFragment() {}

    public EventLengkapFragment(String _judul, String _tanggal, String _keterangan, String _linkGambar) {
        this.judul = _judul;
        this.tanggal = _tanggal;
        this.keterangan = _keterangan;
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
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_event_lengkap, container, false);
        generateEventLengkap();
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
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    public void generateEventLengkap() {
        myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_eventLengkap);

        // Add LayoutParams
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
        params.setMargins(0, 10, 20, 0);

        LinearLayout rowLayout = new LinearLayout(getActivity());
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Buat linear layout vertical untuk menampung kata2
        LinearLayout colLayout = new LinearLayout(getActivity());
        colLayout.setOrientation(LinearLayout.VERTICAL);
        colLayout.setPadding(0,10,10,0);

        LinearLayout subRowLayout = new LinearLayout(getActivity());
        subRowLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Add image View
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int image_width = display.getWidth()/3;
        int image_height = (int) (display.getHeight()/4.3);

        ImageView GambarIV = new ImageView(getActivity());

        // Loading image from below url into imageView
        Picasso.with(getActivity())
                .load(linkGambar)
                .resize(image_height, image_width)
                .into(GambarIV);
        GambarIV.setLayoutParams(params);
        rowLayout.addView(GambarIV);

        // Add text View TitleEventTV
        TitleEventTV = new TextView(getActivity());
        TitleEventTV.setText("Event: ");
        TitleEventTV.setLayoutParams(params);
        TitleEventTV.setTextColor(colorBlack);
        subRowLayout.addView(TitleEventTV);

        // Add text View JudulEventTV
        JudulEventTV = new TextView(getActivity());
        JudulEventTV.setText(judul);
        JudulEventTV.setLayoutParams(params);
        subRowLayout.addView(JudulEventTV);
        colLayout.addView(subRowLayout);
        subRowLayout = new LinearLayout(getActivity());

        // Add text View TitleTanggalTV
        TitleTanggalTV = new TextView(getActivity());
        TitleTanggalTV.setText("Tanggal: ");
        TitleTanggalTV.setTextColor(colorBlack);
        TitleTanggalTV.setLayoutParams(params);
        subRowLayout.addView(TitleTanggalTV);

        // Add text View JudulTanggalTV
        JudulTanggalTV= new TextView(getActivity());
        JudulTanggalTV.setText(tanggal);
        JudulTanggalTV.setLayoutParams(params);
        subRowLayout.addView(JudulTanggalTV);
        colLayout.addView(subRowLayout);
        subRowLayout = new LinearLayout(getActivity());

        // Add text View TitleWaktuTV
        TitleWaktuTV = new TextView(getActivity());
        TitleWaktuTV.setText("Waktu: ");
        TitleWaktuTV.setTextColor(colorBlack);
        TitleWaktuTV.setLayoutParams(params);
        subRowLayout.addView(TitleWaktuTV);

        // Add text View JudulWaktuTV
        JudulWaktuTV = new TextView(getActivity());
        JudulWaktuTV.setText(tanggal);
        JudulWaktuTV.setLayoutParams(params);
        subRowLayout.addView(JudulWaktuTV);
        colLayout.addView(subRowLayout);
        subRowLayout = new LinearLayout(getActivity());

        // Add text View TitleKeteranganTV
        TitleKeteranganTV = new TextView(getActivity());
        TitleKeteranganTV.setText("Keterangan: ");
        TitleKeteranganTV.setTextColor(colorBlack);
        TitleKeteranganTV.setLayoutParams(params);
        subRowLayout.addView(TitleKeteranganTV);

        // Add text View IsiKeteranganTV
        IsiKeteranganTV = new TextView(getActivity());
        IsiKeteranganTV.setText(keterangan);
        IsiKeteranganTV.setLayoutParams(params);
        subRowLayout.addView(IsiKeteranganTV);
        colLayout.addView(subRowLayout);

        rowLayout.addView(colLayout);
        myLinearLayout.addView(rowLayout);
    }
}
