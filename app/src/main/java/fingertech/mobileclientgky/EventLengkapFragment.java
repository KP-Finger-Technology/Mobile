package fingertech.mobileclientgky;

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

    private TextView titleEventTV;
    private TextView judulEventTV;
    private TextView titleTanggalTV;
    private TextView judulTanggalTV;
    private TextView titleWaktuTV;
    private TextView judulWaktuTV;
    private TextView titleKeteranganTV;
    private TextView isiKeteranganTV;
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
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);

        // Add LayoutParams
        LinearLayout.LayoutParams paramsJarakAntarEvent = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsJarakAntarEvent.setMargins(0, 15, 20, 0);

        LinearLayout.LayoutParams paramsJarakAntarIsi = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsJarakAntarIsi.setMargins(5, 0, 0, 0);

        LinearLayout.LayoutParams paramsJarakIsiDenganButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsJarakIsiDenganButton.setMargins(5, 5, 0, 15);

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

        ImageView gambarIV = new ImageView(getActivity());

        // Loading image from below url into imageView
        Picasso.with(getActivity())
                .load(linkGambar)
                .resize(image_height, image_width)
                .into(gambarIV);
        gambarIV.setLayoutParams(paramsJarakAntarEvent);
        rowLayout.addView(gambarIV);

        int defaultColor = getResources().getColor(R.color.defaultFontColor);

        // Add text View titleEventTV
        titleEventTV = new TextView(getActivity());
        titleEventTV.setText("Event: ");
        titleEventTV.setTextColor(defaultColor);
        titleEventTV.setLayoutParams(paramsJarakAntarIsi);
        titleEventTV.setTextColor(getResources().getColor(R.color.defaultFontColor));
        subRowLayout.addView(titleEventTV);

        // Add text View judulEventTV
        judulEventTV = new TextView(getActivity());
        judulEventTV.setText(judul);
        judulEventTV.setTextColor(defaultColor);
        judulEventTV.setLayoutParams(paramsJarakAntarIsi);
        subRowLayout.addView(judulEventTV);
        colLayout.addView(subRowLayout);
        subRowLayout = new LinearLayout(getActivity());

        // Add text View titleTanggalTV
        titleTanggalTV = new TextView(getActivity());
        titleTanggalTV.setText("Tanggal: ");
        titleTanggalTV.setTextColor(defaultColor);
        titleTanggalTV.setTextColor(getResources().getColor(R.color.defaultFontColor));
        titleTanggalTV.setLayoutParams(paramsJarakAntarIsi);
        subRowLayout.addView(titleTanggalTV);

        // Add text View judulTanggalTV
        judulTanggalTV = new TextView(getActivity());
        judulTanggalTV.setText(tanggal);
        judulTanggalTV.setTextColor(defaultColor);
        judulTanggalTV.setLayoutParams(paramsJarakAntarIsi);
        subRowLayout.addView(judulTanggalTV);
        colLayout.addView(subRowLayout);
        subRowLayout = new LinearLayout(getActivity());

        // Add text View titleWaktuTV
        titleWaktuTV = new TextView(getActivity());
        titleWaktuTV.setText("Waktu: ");
        titleWaktuTV.setTextColor(defaultColor);
        titleWaktuTV.setTextColor(getResources().getColor(R.color.defaultFontColor));
        titleWaktuTV.setLayoutParams(paramsJarakAntarIsi);
        subRowLayout.addView(titleWaktuTV);

        // Add text View judulWaktuTV
        judulWaktuTV = new TextView(getActivity());
        judulWaktuTV.setText(tanggal);
        judulWaktuTV.setTextColor(defaultColor);
        judulWaktuTV.setLayoutParams(paramsJarakAntarIsi);
        subRowLayout.addView(judulWaktuTV);
        colLayout.addView(subRowLayout);
        subRowLayout = new LinearLayout(getActivity());

        // Add text View titleKeteranganTV
        titleKeteranganTV = new TextView(getActivity());
        titleKeteranganTV.setText("Keterangan: ");
        titleKeteranganTV.setTextColor(defaultColor);
        titleKeteranganTV.setTextColor(getResources().getColor(R.color.defaultFontColor));
        titleKeteranganTV.setLayoutParams(paramsJarakAntarIsi);
        subRowLayout.addView(titleKeteranganTV);

        // Add text View isiKeteranganTV
        isiKeteranganTV = new TextView(getActivity());
        isiKeteranganTV.setText(keterangan);
        isiKeteranganTV.setTextColor(defaultColor);
        isiKeteranganTV.setLayoutParams(paramsJarakAntarIsi);
        subRowLayout.addView(isiKeteranganTV);
        colLayout.addView(subRowLayout);

        rowLayout.addView(colLayout);
        myLinearLayout.addView(rowLayout);
    }
}
