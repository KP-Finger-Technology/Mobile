package fingertech.mobileclientgky;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by William Stefan Hartono
 */
public class LirikLaguRohaniLengkapFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View rootView;

    public static LirikLaguRohaniLengkapFragment newInstance(String param1, String param2) {
        LirikLaguRohaniLengkapFragment fragment = new LirikLaguRohaniLengkapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public LirikLaguRohaniLengkapFragment() {}

    private String judul;
    private String isi;

    public LirikLaguRohaniLengkapFragment(String _judul, String _isi) {
        this.judul = _judul;
        this.isi = _isi;
    }

    public void generateIsiLirikLaguRohani() {
        // Add LinearLayout
        LinearLayout myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_lirikLaguRohaniLengkap);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);

        // Add LayoutParams
        LinearLayout.LayoutParams paramsJudul = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsJudul.setMargins(0, 0, 0, 0);

        LinearLayout.LayoutParams paramsJudulDanIsi = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsJudulDanIsi.setMargins(0, 15, 0, 0);

        LinearLayout.LayoutParams paramsAntarIsi = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsAntarIsi.setMargins(0, 0, 0, 0);

        int defaultColor = getResources().getColor(R.color.defaultFontColor);

        if (judul != null) {
            TextView lirikLaguRohaniTV = new TextView(getActivity());
            lirikLaguRohaniTV.setText(judul);
            lirikLaguRohaniTV.setTextColor(defaultColor);
            lirikLaguRohaniTV.setTypeface(null, Typeface.BOLD);
            lirikLaguRohaniTV.setGravity(Gravity.CENTER);
            lirikLaguRohaniTV.setLayoutParams(paramsJudul);
            myLinearLayout.addView(lirikLaguRohaniTV);
        }

        if (isi != null) {
            TextView lirikLaguRohaniTV = new TextView(getActivity());
            lirikLaguRohaniTV.setText("");
            lirikLaguRohaniTV.setLayoutParams(paramsJudulDanIsi);
            myLinearLayout.addView(lirikLaguRohaniTV);

            lirikLaguRohaniTV = new TextView(getActivity());
            lirikLaguRohaniTV.setText(isi);
            lirikLaguRohaniTV.setTextColor(defaultColor);
            lirikLaguRohaniTV.setGravity(Gravity.CENTER);
            lirikLaguRohaniTV.setLayoutParams(paramsAntarIsi);
            myLinearLayout.addView(lirikLaguRohaniTV);
        }
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
        rootView = inflater.inflate(R.layout.fragment_lirik_lagu_rohani_lengkap, container, false);
        generateIsiLirikLaguRohani();
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
}
