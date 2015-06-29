
package fingertech.mobileclientgky;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * Created by Andarias Silvanus
 */
public class AyatAlkitabFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View rootView;
    private LinearLayout myLinearLayout;
    private DataBaseHelper DB;

    private String kitab;
    private int pasal, ayat;
    private Context context;

    public static AyatAlkitabFragment newInstance(String param1, String param2) {
        AyatAlkitabFragment fragment = new AyatAlkitabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AyatAlkitabFragment() {}

    public AyatAlkitabFragment (String _kitab, int _pasal, int _ayat) {
        this.kitab = _kitab;
        this.pasal = _pasal;
        this.ayat = _ayat;
        Log.d("AyatAlkitab", Integer.toString(_ayat));
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_ayat_alkitab, container, false);
        generateAyatAlkitab();
        return rootView;
    }

    int x_TV = 0, y_TV = 0;

    public void generateAyatAlkitab() {
        // Add LinearLayout
        myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_ayatAlkitab);

        // Add LayoutParams
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
        params.setMargins(0, 10, 0, 0);

        if (kitab!=null) {
            ArrayList<String> daftarAyat = DB.getPasal(kitab, pasal);
            int len = daftarAyat.size();

            int height_ayat = 0;
            WindowManager wm = (WindowManager) getActivity().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int sum_pading = 35;
            int deviceWidth  = size.x - sum_pading;

            boolean Mark = false;
            TextView ayatTV = null;
            for (int i=0; i<len; i++) {
                ayatTV = new TextView(getActivity());
                ayatTV.setText(Integer.toString(i+1)+" "+daftarAyat.get(i));
                ayatTV.setLayoutParams(params);
                myLinearLayout.addView(ayatTV);

                if (!Mark) {
                    int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
                    int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    ayatTV.measure(widthMeasureSpec, heightMeasureSpec);
                    height_ayat = height_ayat + ayatTV.getMeasuredHeight();
                }

                if ((i+1)==this.ayat) {
                    final TextView finalAyatTV = ayatTV;
                    ayatTV.post(new Runnable() {
                        @Override
                        public void run() {
                            x_TV = finalAyatTV.getLeft();
                            y_TV = finalAyatTV.getTop();
                        }
                    });
                    Mark = true;
                }
            }

            ScrollView SV = (ScrollView) rootView.findViewById(R.id.scroll_ayatAlkitab);
            SV.post(new Runnable() {
                public void run(){
                    ScrollView SV = (ScrollView) rootView.findViewById(R.id.scroll_ayatAlkitab);
                    SV.scrollTo(0, y_TV);
                }
            });
        }
        DB.closeDataBase();
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
