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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

//import android.app.Fragment;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlkitabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlkitabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlkitabFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;
    private Button kitabBtn;
    private LinearLayout myLinearLayout;
    private DataBaseHelper DB;

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
     * @return A new instance of fragment AlkitabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlkitabFragment newInstance(String param1, String param2) {
        AlkitabFragment fragment = new AlkitabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AlkitabFragment() {
        // Required empty public constructor
    }

    public AlkitabFragment(Context _context) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void generateBtnKitab(int mode, String req) {
        // mode == 0 utk default, ambil dari database lokal
        // mode == 1 utk mode pencarian
        // mode == 2 utk mode tidak ada result dari pencarian

        //add LinearLayout
        myLinearLayout=(LinearLayout) rootView.findViewById(R.id.container_alkitab);
        //add LayoutParams
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
//        params.setMargins(0, 10, 20, 0);

        if ((mode <2) && (mode>-1)) {
            if (mode == 0) {
                DB.getDaftarKitab();
            }
            else if (mode == 1) {
                DB.searchKitab(req);
            }
            int length = DB.getJumlahPasal().size();

            for (int i = 0; i < length; i++) {
                kitabBtn = new Button(getActivity());
                kitabBtn.setText(DB.getPasalAlkitab().get(i));
                kitabBtn.setLayoutParams(params);
//            kitabBtn.setBackgroundColor(0);
                final int finalI = i;
                kitabBtn.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            frag = new PasalAlkitabFragment(DB.getPasalAlkitab().get(finalI), DB.getJumlahPasal().get(finalI));
                            switchFragment();
                        }
                    }
                );
                myLinearLayout.addView(kitabBtn);
            }
//            DB.closeDataBase();
        }
        else {
            TextView TV = new TextView(getActivity());
            TV.setText("Pencarian terhadap kata "+req+" tidak ditemukan");
            TV.setLayoutParams(params);
            myLinearLayout.addView(TV);
        }
    }

    private void switchFragment() {
        fragManager = getActivity().getSupportFragmentManager();
        fragTransaction = fragManager.beginTransaction();
        fragTransaction.replace(R.id.container, frag);
        fragTransaction.addToBackStack(null);
        fragTransaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_alkitab, container, false);

        DB = new DataBaseHelper(getActivity().getApplicationContext());
        Log.d("Fro AlkitabFragment","Persiapan buka database..");
        DB.openDataBase();
        generateBtnKitab(0,"");

        SearchView SV = (SearchView) rootView.findViewById(R.id.alkitab_searchView);
        SV.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit (String s) {
            // Penanganan setelah user input string dan mem-submit
                DB.searchKitab(s);
                // Reset view
                myLinearLayout=(LinearLayout) rootView.findViewById(R.id.container_alkitab);
                myLinearLayout.removeAllViews();
                if (DB.getJumlahPasal().size()>0) {
                    // Terdapat hasil pencarian generate UI
                    generateBtnKitab(1,s);
                }
                else {
                    // Tidak terdapat hasil pencarian
                    generateBtnKitab(2,s);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return rootView;
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
