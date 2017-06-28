package fr.pomp.adfuell.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import fr.pomp.adfuell.App;
import fr.pomp.adfuell.MainActivity;
import fr.pomp.adfuell.R;
import fr.pomp.adfuell.utils.edena.EDButterKniff;
import fr.pomp.adfuell.utils.edena.EDEventBus;
import fr.pomp.adfuell.ws.IWSDelegate;

/**
 * Created by edena on 28/03/2017.
 */

public class BaseFragment extends Fragment implements IWSDelegate {
    public EDButterKniff _edButterKniff;
    public EDEventBus _edEventBus;
    public MainActivity _mainActivity;
    public App _app;
    public BaseFragment(){

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            _mainActivity.headerShow(true);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _edButterKniff = EDButterKniff.getInstance();
        _edEventBus = EDEventBus.getInstance(this);
        _mainActivity = (MainActivity) getActivity();
        _app = _mainActivity._app;
        _mainActivity.headerShow(true);

    }

    @Override
    public void onPause() {
        super.onPause();
        if(_edEventBus != null)
            _edEventBus.unRegister();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(_edEventBus != null)
            _edEventBus.register();
        _app.setFragmentCurrent(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void doThis(EDEventBus intentServiceResult) {

    }

    @Override
    public void onFaillure(Object obj) {
        _mainActivity.loadingShow(false);
    }
    @Override
    public void onSuccess(Object obj) {
        _mainActivity.loadingShow(false);
    }

    public void getToken(){
        _mainActivity.loadingShow(true);
        _mainActivity.getToken(this);
    }

    public String getStringRes(int resID){
        return getResources().getString(resID);
    }



    public String valueStatusString(String input){
        String output = "";
        if(input.equalsIgnoreCase("toValidate")){
            output = getStringRes(R.string.to_validate);
        }
        if(input.equalsIgnoreCase("incoming")){
            output = getStringRes(R.string.incoming);
        }
        if(input.equalsIgnoreCase("toBePaid")){
            output = getStringRes(R.string.to_be_paid);
        }
        if(input.equalsIgnoreCase("paid")){
            output = getStringRes(R.string.paid);
        }
        if(input.equalsIgnoreCase("cancelled")){
            output = getStringRes(R.string.cancelled);
        }
        if(input.equalsIgnoreCase("failed")){
            output = getStringRes(R.string.failed);
        }
        return output;
    }

}
