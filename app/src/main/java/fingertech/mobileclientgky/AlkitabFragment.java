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


/**
 * Created by Andarias Silvanus
 */
public class AlkitabFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

    public static AlkitabFragment newInstance(String param1, String param2) {
        AlkitabFragment fragment = new AlkitabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AlkitabFragment() {}

    public AlkitabFragment(Context _context) {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void generateBtnKitab(int mode, String req) {
        // Mode == 0 untuk default, ambil dari database lokal
        // Mode == 1 untuk mode pencarian
        // Mode == 2 untuk mode tidak ada result dari pencarian

        // Add LinearLayout
        myLinearLayout=(LinearLayout) rootView.findViewById(R.id.container_alkitab);

        LinearLayout containerKitabLayout = new LinearLayout(getActivity());
        containerKitabLayout.setOrientation(LinearLayout.VERTICAL);

        // Add LayoutParams
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
        myLinearLayout.setPadding(0,0,0,10);
        params.setMargins(0,0,0,10);

        if ((mode < 2) && (mode > -1)) {
            if (mode == 0) {
                DB.getDaftarKitab();
            }
            else if (mode == 1) {
                DB.searchKitab(req);
            }
            int length = DB.getJumlahPasal().size();

            for (int i = 0; i < length; i++) {
                kitabBtn = new Button(getActivity());
                kitabBtn.setText(DB.getPasalAlkitab().get(i).toUpperCase());
                kitabBtn.setLayoutParams(params);

                if (i < 39) // Perjanjian Lama
                    kitabBtn.setBackground(getResources().getDrawable(R.drawable.alkitabbuttonplstyle));
                else      // Perjanjian Baru
                    kitabBtn.setBackground(getResources().getDrawable(R.drawable.alkitabbuttonpbstyle));

                kitabBtn.setTextAppearance(getActivity().getApplicationContext(), R.style.alkitabButtonStyle);

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
                containerKitabLayout.addView(kitabBtn);
            }
        }
        else {
            TextView TV = new TextView(getActivity());
            TV.setText("Pencarian terhadap kata " + req + " tidak ditemukan");
            TV.setLayoutParams(params);
            containerKitabLayout.addView(TV);
        }
        myLinearLayout.addView(containerKitabLayout);
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
        DB.openDataBase();
        generateBtnKitab(0, "");

        SearchView SV = (SearchView) rootView.findViewById(R.id.alkitab_searchView);
        SV.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit (String s) {
            // Penanganan setelah user input string dan mem-submit
                DB.searchKitab(s);
                // Reset view
                myLinearLayout=(LinearLayout) rootView.findViewById(R.id.container_alkitab);
                myLinearLayout.removeAllViews();
                if (DB.getJumlahPasal().size() > 0) {
                    // Terdapat hasil pencarian generate UI
                    generateBtnKitab(1, s);
                }
                else {
                    // Tidak terdapat hasil pencarian
                    generateBtnKitab(2, s);
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