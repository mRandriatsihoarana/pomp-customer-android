package fr.pomp.adfuell.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.pomp.adfuell.R;

/**
 * Created by edena on 31/03/2017.
 */

public class CGVFragment extends BaseFragment {
    public CGVFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_cgv, container, false);
        _edButterKniff.view(this,v);
        _mainActivity.headerShow(true);
        _mainActivity.headerRightBtnShow(false);
        _mainActivity.changeHeaderTitle(getStringRes(R.string.text_propos));
        return v;
    }
}
