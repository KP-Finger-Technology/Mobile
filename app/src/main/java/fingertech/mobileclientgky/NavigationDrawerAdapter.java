package fingertech.mobileclientgky;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;


/**
 * Created by William Stefan Hartono
 */
public class NavigationDrawerAdapter extends BaseExpandableListAdapter {

    private Context context;
    private LinkedHashMap<String, ArrayList<String>> parentHashMap;
    private ArrayList<String> parentList;

    private static final int[] EMPTY_STATE_SET = {};
    private static final int[] GROUP_EXPANDED_STATE_SET =
            {android.R.attr.state_expanded};
    private static final int[][] GROUP_STATE_SETS = {
            EMPTY_STATE_SET, // 0
            GROUP_EXPANDED_STATE_SET // 1
    };

    public NavigationDrawerAdapter (Context _context, LinkedHashMap<String, ArrayList<String>> _parentHashMap, ArrayList<String> _parentList) {
        parentHashMap = _parentHashMap;
        this.context = _context;
        this.parentHashMap = _parentHashMap;
        this.parentList = _parentList;
    }

	// Fungsi untuk menghitung ada berapa menu utama pada navigation drawer
    @Override
    public int getGroupCount() {
        return parentHashMap.size();
    }

	// Fungsi untuk menghitung ada berapa submenu dari suatu menu utama pada navigation drawer
    @Override
    public int getChildrenCount(int groupPosition) {
        return parentHashMap.get(parentList.get(groupPosition)).size();
    }

	// Fungsi untuk mendapatkan menu utama tertentu pada navigation drawer
    @Override
    public Object getGroup(int groupPosition) {
        return parentList.get(groupPosition);
    }

	// Fungsi untuk mendapatkan submenu tertentu dari suatu menu utama pada navigation drawer
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return parentHashMap.get(parentList.get(groupPosition)).get(childPosition);
    }

	// Fungsi untuk mendapatkan id menu utama
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

	// Fungsi untuk mendapatkan id submenu
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

	// Fungsi untuk menyiapkan tampilan menu utama
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String groupTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.parent_layout, parent, false);
        }
        TextView parentTextView = (TextView) convertView.findViewById(R.id.textViewParent);
        parentTextView.setText(groupTitle);

        // Set group indicator icon
        View view = convertView;
        View ind = view.findViewById( R.id.explist_indicator);
        if( ind != null ) {
            ImageView indicator = (ImageView)ind;
            // Jika menu utama tidak memiliki sub menu, hapus gambar indikator
			if(getChildrenCount( groupPosition ) == 0) {
                indicator.setVisibility( View.INVISIBLE );
            } else { // Jika menu utama memiliki sub menu, berikan gambar indikator
                indicator.setVisibility( View.VISIBLE );
                int stateSetIndex = ( isExpanded ? 1 : 0) ;
                Drawable drawable = indicator.getDrawable();
                drawable.setState(GROUP_STATE_SETS[stateSetIndex]);
            }
        }

        return convertView;
    }

	// Fungsi untuk menyiapkan tampilan submenu
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childTitle = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_layout, parent, false);
        }
        TextView childTextView = (TextView) convertView.findViewById(R.id.textViewChild);
        childTextView.setText(childTitle);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}