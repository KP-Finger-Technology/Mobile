package fingertech.mobileclientgky;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

//import android.app.Fragment;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PasalAlkitabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PasalAlkitabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PasalAlkitabFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String kitab;
    private int pasal;

    private View rootView;
    private LinearLayout myLinearLayout;

    private OnFragmentInteractionListener mListener;
    private Fragment frag;
    private FragmentTransaction fragTransaction;
    private FragmentManager fragManager;

    private Context context;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PasalAlkitabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PasalAlkitabFragment newInstance(String param1, String param2) {
        PasalAlkitabFragment fragment = new PasalAlkitabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PasalAlkitabFragment() {
        // Required empty public constructor
    }

    public PasalAlkitabFragment(String _kitab, int _pasal) {
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

//        try {
//            DB.createDataBase();
//            DB.openDataBase();
//        } catch (IOException e) {
////            e.printStackTrace();
//            Log.d("gagal create & open database!", "");
//        }
    }

    private void generateBtnPasal(){
        //add LinearLayout
        myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_pasalAlkitab);
        //add LayoutParams
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
        params.setMargins(0, 0, 10, 15);

        // Judul Kitab yang dipilih
        LinearLayout rowLayout = new LinearLayout(getActivity());
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView namaKitab = new TextView(getActivity());
        namaKitab.setText(kitab);
        namaKitab.setLayoutParams(params);
        rowLayout.addView(namaKitab);
        myLinearLayout.addView(rowLayout);

        // Button-button untuk kitab tersebut sebanyak jumlah pasal
        rowLayout = new LinearLayout(getActivity());
        LinearLayout colLayout = new LinearLayout(getActivity());
        colLayout.setOrientation(LinearLayout.VERTICAL);
        int cnt = 0;
        Button pasalBtn;
//        Log.d("jumlah pasal "+kitab,Integer.toString(pasal));
        for (int i=0; i<pasal; i++) {
            cnt++;
            pasalBtn = new Button(getActivity());
            pasalBtn.setText(Integer.toString(i+1));
            pasalBtn.setLayoutParams(params);

            final int finalI = i+1;
            pasalBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("From PasalAlkitabFragment","kitab="+kitab+" & pasal="+Integer.toString(finalI));
                        frag = new AyatAlkitabFragment(kitab, finalI);
                        fragManager = getActivity().getSupportFragmentManager();
                        fragTransaction = fragManager.beginTransaction();
                        fragTransaction.replace(R.id.container, frag);
                        fragTransaction.addToBackStack(null);
                        fragTransaction.commit();
                    }
                }
            );

            // NGIDE
            if (pasalBtn.getParent()!=null)
                ((ViewGroup)pasalBtn.getParent()).removeView(pasalBtn);
            rowLayout.addView(pasalBtn);
            if (cnt>=5) {
                cnt = 0;
                colLayout.addView(rowLayout);
                rowLayout = new LinearLayout(getActivity());
            }
        }
        if (pasal>5)
            myLinearLayout.addView(colLayout);
        else
            myLinearLayout.addView(rowLayout);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_pasal_alkitab, container, false);
        generateBtnPasal();
        return rootView;
//        return inflater.inflate(R.layout.fragment_pasal_alkitab, container, false);
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