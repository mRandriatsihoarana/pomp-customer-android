package fr.pomp.adfuell.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.pomp.adfuell.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocalisationFragment extends BaseFragment {


    public LocalisationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_localisation, container, false);
        _edButterKniff.view(this,v);
        _mainActivity.headerShow(true);
        _mainActivity.headerRightBtnShow(false);
        _mainActivity.changeHeaderTitle(getStringRes(R.string.text_localisation_));
        return v;
    }

}
