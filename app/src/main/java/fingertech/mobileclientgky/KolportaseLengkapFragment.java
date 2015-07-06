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
 * Created by William Stefan Hartono
 */
public class KolportaseLengkapFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private LinearLayout myLinearLayout;
    private View rootView;

    private ImageView GambarIV;
    private TextView titleBukuTV;
    private TextView judulBukuTV;
    private TextView titlePengarangTV;
    private TextView judulPengarangTV;
    private TextView titleKeteranganTV;
    private TextView isiKeteranganTV;

    // Untuk komponen-komponen
    private String judul = null, pengarang = null, keterangan = null, linkGambar = null;

    public static KolportaseLengkapFragment newInstance(String param1, String param2) {
        KolportaseLengkapFragment fragment = new KolportaseLengkapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public KolportaseLengkapFragment() {}

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
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_kolportase_lengkap, container, false);
        generateKolportaseLengkap();
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

    public void generateKolportaseLengkap() {
        // Add LinearLayout
        myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_kolportaseLengkap);

        // Add LayoutParams
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
        params.setMargins(0, 15, 20, 0);

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

        int defaultColor = getResources().getColor(R.color.defaultFontColor);

        // Add image View
        ImageView GambarIV = new ImageView(getActivity());

        // Loading image from below url into imageView
        Picasso.with(getActivity())
                .load(linkGambar)
                .resize(image_height, image_width)
                .into(GambarIV);
        GambarIV.setLayoutParams(params);
        rowLayout.addView(GambarIV);

        // Add textView titleBukuTV
        titleBukuTV = new TextView(getActivity());
        titleBukuTV.setText("Judul: ");
        titleBukuTV.setLayoutParams(params);
        titleBukuTV.setTextColor(getResources().getColor(R.color.defaultFontColor));
        subRowLayout.addView(titleBukuTV);

        // Add textView judulBukuTV
        judulBukuTV = new TextView(getActivity());
        judulBukuTV.setText(judul);
        judulBukuTV.setLayoutParams(params);
        subRowLayout.addView(judulBukuTV);
        colLayout.addView(subRowLayout);
        subRowLayout = new LinearLayout(getActivity());

        // Add textView titlePengarangTV
        titlePengarangTV = new TextView(getActivity());
        titlePengarangTV.setText("Pengarang: ");
        titlePengarangTV.setLayoutParams(params);
        titlePengarangTV.setTextColor(getResources().getColor(R.color.defaultFontColor));
        subRowLayout.addView(titlePengarangTV);

        // Add textView judulPengarangTV
        judulPengarangTV = new TextView(getActivity());
        judulPengarangTV.setText(pengarang);
        judulPengarangTV.setLayoutParams(params);
        subRowLayout.addView(judulPengarangTV);
        colLayout.addView(subRowLayout);
        subRowLayout = new LinearLayout(getActivity());

        // Add textView titleKeteranganTV
        titleKeteranganTV = new TextView(getActivity());
        titleKeteranganTV.setText("Keterangan: ");
        titleKeteranganTV.setTextColor(getResources().getColor(R.color.defaultFontColor));
        titleKeteranganTV.setLayoutParams(params);
        subRowLayout.addView(titleKeteranganTV);

        // Add text View isiKeteranganTV
        isiKeteranganTV = new TextView(getActivity());
        isiKeteranganTV.setText(keterangan);
        isiKeteranganTV.setLayoutParams(params);
        colLayout.addView(subRowLayout);

        rowLayout.addView(colLayout);
        myLinearLayout.addView(rowLayout);
    }
}
