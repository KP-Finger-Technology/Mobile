package fingertech.mobileclientgky;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

//import android.app.Fragment;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AyatAlkitabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AyatAlkitabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AyatAlkitabFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View rootView;
    private LinearLayout myLinearLayout;
    private DataBaseHelper DB;

    private String kitab;
    private int pasal;
    private Context context;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AyatAlkitabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AyatAlkitabFragment newInstance(String param1, String param2) {
        AyatAlkitabFragment fragment = new AyatAlkitabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AyatAlkitabFragment() {
        // Required empty public constructor
    }

    public AyatAlkitabFragment (String _kitab, int _pasal) {
        this.kitab = _kitab;
        this.pasal = _pasal;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        DB = new DataBaseHelper(getActivity().getApplicationContext());
        DB.openDataBase();

//        try {
//            DB.createDataBase();
//            DB.openDataBase();
//        } catch (IOException e) {
////            e.printStackTrace();
//            Log.d("gagal create & open database!", "");
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_ayat_alkitab, container, false);
        generateAyatAlkitab();
        return rootView;
//        return inflater.inflate(R.layout.fragment_ayat_alkitab, container, false);
    }

    public void generateAyatAlkitab() {
        //add LinearLayout
        myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_ayatAlkitab);
        //add LayoutParams
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
        params.setMargins(0, 10, 0, 0);

        Log.d("from AyatAlkitabFragment","kitab="+kitab+" & pasal="+Integer.toString(pasal));
        if (kitab!=null) {
            DB.getPasal(kitab, pasal);
            String ayat = DB.getResultQueryPasal();
            TextView ayatTV = new TextView(getActivity());
            ayatTV.setText(ayat);
            ayatTV.setLayoutParams(params);
            myLinearLayout.addView(ayatTV);
        }
        DB.closeDataBase();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

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
