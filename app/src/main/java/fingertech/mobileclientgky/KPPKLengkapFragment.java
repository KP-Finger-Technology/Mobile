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
 * Created by Andarias Silvanus
 */
public class KPPKLengkapFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View rootView;

    public static KPPKLengkapFragment newInstance(String param1, String param2) {
        KPPKLengkapFragment fragment = new KPPKLengkapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public KPPKLengkapFragment() {}

    private String judul;
    private String isi;

    public KPPKLengkapFragment(String _judul, String _isi) {
        this.judul = _judul;
        this.isi = _isi;
    }

    public void generateIsiKPPK() {
        // Add LinearLayout
        LinearLayout myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_kppkLengkap);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);

        // Add LayoutParams
        LinearLayout.LayoutParams paramsJudul = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsJudul.setMargins(0, 0, 0, 0);

        LinearLayout.LayoutParams paramsJudulDanIsi = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsJudulDanIsi.setMargins(0, 15, 0, 0);

        LinearLayout.LayoutParams paramsAntarIsi = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsAntarIsi.setMargins(0, 0, 0, 0);

        if (judul != null) {
            TextView kppkTV = new TextView(getActivity());
            kppkTV.setText(judul);
            kppkTV.setTypeface(null, Typeface.BOLD);
            kppkTV.setGravity(Gravity.CENTER);
            kppkTV.setLayoutParams(paramsJudul);
            myLinearLayout.addView(kppkTV);
        }

        if (isi != null) {
            TextView kppkTV = new TextView(getActivity());
            kppkTV.setText("");
            kppkTV.setLayoutParams(paramsJudulDanIsi);
            myLinearLayout.addView(kppkTV);

            kppkTV = new TextView(getActivity());
            kppkTV.setText(isi);
            kppkTV.setGravity(Gravity.CENTER);
            kppkTV.setLayoutParams(paramsAntarIsi);
            myLinearLayout.addView(kppkTV);
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
        rootView = inflater.inflate(R.layout.fragment_kppklengkap, container, false);
        generateIsiKPPK();
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
