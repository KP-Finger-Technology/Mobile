package fingertech.mobileclientgky;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by Andarias Silvanus
 */
public class KumpulanBtnAyatAlkitabFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static KumpulanBtnAyatAlkitabFragment newInstance(String param1, String param2) {
        KumpulanBtnAyatAlkitabFragment fragment = new KumpulanBtnAyatAlkitabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public KumpulanBtnAyatAlkitabFragment() {}

    private String kitab;
    private int pasal;

    public KumpulanBtnAyatAlkitabFragment(String _kitab, int _pasal) {
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
    }

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_kumpulan_btn_ayat_alkitab, container, false);
        generateBtnAyat();
        return rootView;
    }

    private LinearLayout myLinearLayout;
    private Fragment frag;
    private FragmentTransaction fragTransaction;
    private FragmentManager fragManager;

    private void generateBtnAyat() {
        DataBaseHelper DB = new DataBaseHelper(getActivity().getApplicationContext());
        DB.openDataBase();
        int jumAyat = DB.getJumlahAyat(kitab, pasal);

        // Add LinearLayout
        myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_btnAyatAlkitab);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);

        int rightMargin = 5;
        int sumMargin = rightMargin;

        // Untuk menghitung batas horisontal button yang dibuat
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int displayWidth = display.getWidth();
        int sumPaddingDP = 35;
        int sumPadding = (int) (sumPaddingDP * (metrics.xdpi / 160));
        int jumlahDraw = 5;

        int btnWidth = (displayWidth - sumPadding - (rightMargin * jumlahDraw)) / (jumlahDraw);
        int btnHeight = (int) (btnWidth * 0.65);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(btnWidth, btnHeight);
        params.setMargins(0, 0, rightMargin, 5);

        // Judul Kitab yang dipilih
        LinearLayout rowLayout = new LinearLayout(getActivity());
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView namaKitab = new TextView(getActivity());
        namaKitab.setText(kitab + " " + Integer.toString(pasal));
        namaKitab.setTextAppearance(getActivity().getApplicationContext(), R.style.judulPasal);
        namaKitab.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        rowLayout.addView(namaKitab);
        myLinearLayout.addView(rowLayout);

        // Tambah garis divider
        View divider = new View(getActivity());
        LinearLayout.LayoutParams LP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,5);
        LP.setMargins(0, 0, 0, 20);
        divider.setLayoutParams(LP);
        divider.setBackgroundColor(getResources().getColor(R.color.dividerLine));
        myLinearLayout.addView(divider);

        // Tambah text view utk memberi penjelasan jika sedang berada di halaman pasal
        TextView halAyat = new TextView(getActivity());
        halAyat.setTextAppearance(getActivity().getApplicationContext(), R.style.judulPasal);
        halAyat.setText("Ayat");
        halAyat.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        rowLayout = new LinearLayout(getActivity());
        rowLayout.addView(halAyat);
        myLinearLayout.addView(rowLayout);

        // Button-button untuk pasal tersebut sebanyak jumlah ayat
        rowLayout = new LinearLayout(getActivity());
        LinearLayout colLayout = new LinearLayout(getActivity());
        colLayout.setOrientation(LinearLayout.VERTICAL);
        int cnt = 0;
        Button pasalBtn;

        for (int i = 0; i < jumAyat; i++) {
            cnt++;
            pasalBtn = new Button(getActivity());
            pasalBtn.setText(Integer.toString(i+1));
            pasalBtn.setBackground(getResources().getDrawable(R.drawable.alkitabbuttonstyle));
            pasalBtn.setTextAppearance(getActivity().getApplicationContext(), R.style.pasalAyatButtonStyle);
            pasalBtn.setLayoutParams(params);

            final int finalI = i + 1;
            pasalBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        frag = new AyatAlkitabFragment(kitab, pasal, finalI);
                        fragManager = getActivity().getSupportFragmentManager();
                        fragTransaction = fragManager.beginTransaction();
                        fragTransaction.replace(R.id.container, frag);
                        fragTransaction.addToBackStack(null);
                        fragTransaction.commit();
                    }
                }
            );

            // Coba-coba
            if (pasalBtn.getParent() != null) {
                ((ViewGroup) pasalBtn.getParent()).removeView(pasalBtn);
            }

            rowLayout.addView(pasalBtn);
            if (cnt >= jumlahDraw) {
                cnt = 0;
                colLayout.addView(rowLayout);
                rowLayout = new LinearLayout(getActivity());
            }
            if ((i == jumAyat - 1) && (jumAyat > jumlahDraw)) {
                colLayout.addView(rowLayout);
                rowLayout = new LinearLayout(getActivity());
            }
        }
        if (jumAyat > jumlahDraw)
            myLinearLayout.addView(colLayout);
        else
            myLinearLayout.addView(rowLayout);
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