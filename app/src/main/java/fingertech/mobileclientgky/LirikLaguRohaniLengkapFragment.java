package fingertech.mobileclientgky;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    private String isi;

    public LirikLaguRohaniLengkapFragment(String _isi) {
        this.isi = _isi;
    }

    public void generateIsiLirikLaguRohani() {
        // Add LinearLayout
        LinearLayout myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_lirikLaguRohaniLengkap);

        // Add LayoutParams
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);

        if (isi!=null) {
            TextView lirikLaguRohaniTV = new TextView(getActivity());
            lirikLaguRohaniTV.setText(isi);
            lirikLaguRohaniTV.setLayoutParams(params);
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
