package fr.pomp.adfuell.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import fr.pomp.adfuell.R;

/**
 * Created by Mandimbisoa on 13/04/2017.
 */

public class Confirmation1Fragment extends BaseFragment {

    @BindView(R.id.expandableListView)
    ExpandableListView _expendableView;
    TreeMap<String, Object> _expendableData;

    public Confirmation1Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_confirmation_1, container, false);
        _edButterKniff.view(this,v);
        _mainActivity.headerShow(true);
        _mainActivity.lockedDrawer(false);
        _mainActivity.headerRightBtnShow(false);
        _mainActivity.headerShowLeftBack();
        _mainActivity.changeHeaderTitle("Confirmation");
        _expendableView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousItem )
                    _expendableView.collapseGroup(previousItem );
                previousItem = groupPosition;
            }
        });
        getData();
        return v;
    }

    /**
     * Recuperer les données
     */
    void getData(){
        _expendableData = new TreeMap<>();
        _expendableData.put(getStringRes(R.string.text_localisation),null);
        _expendableData.put(getStringRes(R.string.text_horaire),null);
        _expendableData.put(getStringRes(R.string.text_vehicule),null);
        _expendableData.put(getStringRes(R.string.text_commande),null);
        _expendableView.setAdapter(new ExpendableAdapter());
    }

    public class ExpendableAdapter extends BaseExpandableListAdapter {

        private Context context;
        private List<String> expandableListTitle;
        LayoutInflater layoutInflater;

        public ExpendableAdapter() {
            this.context = getActivity();
            this.expandableListTitle = new ArrayList<String>(_expendableData.keySet());
            layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public Object getChild(int listPosition, int expandedListPosition) {
            return _expendableData.get(this.expandableListTitle.get(listPosition));

        }

        @Override
        public long getChildId(int listPosition, int expandedListPosition) {
            return expandedListPosition;
        }

        @Override
        public View getChildView(int listPosition, final int expandedListPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            final String expandedListText = (String) getChild(listPosition, expandedListPosition);
            switch (listPosition ) {
                case 0:
                convertView = layoutInflater.inflate(R.layout.view_localisation, null);
                    break;
                case 1:
                    convertView = layoutInflater.inflate(R.layout.view_horaire, null);
                    break;
                case 2:
                    convertView = layoutInflater.inflate(R.layout.view_vehicule, null);
                    break;
                case 3:
                    convertView = layoutInflater.inflate(R.layout.view_commande, null);
                    break;
            }

            return convertView;
        }

        @Override
        public int getChildrenCount(int listPosition) {
            return 1;
        }


        @Override
        public Object getGroup(int listPosition) {
            return this.expandableListTitle.get(listPosition);
        }

        @Override
        public int getGroupCount() {
            return this.expandableListTitle.size();
        }

        @Override
        public long getGroupId(int listPosition) {
            return listPosition;
        }

        @Override
        public View getGroupView(int listPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String listTitle = (String) getGroup(listPosition);
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.view_expendable_head, null);
            }
            TextView listTitleTextView = (TextView) convertView
                    .findViewById(R.id.exp_head_title);
            listTitleTextView.setText(listTitle);

            ImageView imgLeft = (ImageView) convertView.findViewById(R.id.exp_head_left_img);
            switch (listPosition ) {
                case 0:
                    imgLeft.setImageResource(R.drawable.localisation);
                    break;
                case 1:
                    imgLeft.setImageResource(R.drawable.time);
                    break;
                case 2:
                    imgLeft.setImageResource(R.drawable.car);
                    break;
                case 3:
                    imgLeft.setImageResource(R.drawable.bid);
                    break;
            }
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int listPosition, int expandedListPosition) {
            return true;
        }
    }




}
