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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LirikLaguRohaniLengkapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LirikLaguRohaniLengkapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LirikLaguRohaniLengkapFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View rootView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LirikLaguRohaniLengkapFragment.
     */
    // TODO: Rename and change types and number of parameters
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
//        params.setMargins(0, 10, 20, 0);

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
