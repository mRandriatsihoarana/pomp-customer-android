package fr.pomp.adfuell.fragment;


import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.OnClick;
import fr.pomp.adfuell.R;
import fr.pomp.adfuell.model.TokenModel;
import fr.pomp.adfuell.utils.comon.CommonUtils;
import fr.pomp.adfuell.ws.IWSDelegate;
import fr.pomp.adfuell.ws.WSService;


/**
 * Created by edena on 31/03/2017.
 */
public class OrderHomeFragment extends BaseFragment implements OnMapReadyCallback{

    static View _view;


    @BindView(R.id.id_order_now)
    Button _orderButton;
    @BindView(R.id.id_order_img)
    ImageView _orderImg;
//    @BindView(R.id.id_order_search)
//    EditText _orderSearch;

    private GoogleMap _map;
    int _pool = 0;

    public OrderHomeFragment() {

    }

    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (_view != null) {
            ViewGroup parent = (ViewGroup) _view.getParent();
            if (parent != null)
                parent.removeView(_view);
        }
        try {
            _view = inflater.inflate(R.layout.fragment_order_home, container, false);
        } catch (InflateException e) {

        }

        _edButterKniff.view(this,_view);
        _mainActivity.headerShow(true);
        _mainActivity.lockedDrawer(false);
        _mainActivity.headerShowLeftMenu();
        _mainActivity.headerRightBtnShow(false);
        _mainActivity.headerHideLogo();
        //_mainActivity.headerShowLogo();
        /*
        SupportMapFragment mapSupport = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapSupport.getMapAsync(this);
        _orderSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });
        //getLocation();
        */
        return   _view;
    }

    /*
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            SupportMapFragment mapSupport = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            if (mapSupport != null) getFragmentManager().beginTransaction().remove(mapSupport).commit();

        } catch (IllegalStateException e) {

        }
    }
    */

    @Override
    public void onResume() {
        _app._isOrderConfirmedView = false;
        _app.schedulesListSaveJson("");
        _app.carListSaveJson("");
        _app.positionHoraireSaveInteger(0);
        _app.positionVehiculeSaveInteger(0);
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        _map = googleMap;

        LatLng paris = new LatLng(48.866667, 2.333333);
//        _orderSearch.setText("Paris");
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(paris, 17);
        _map.animateCamera(cameraUpdate);
        _map.setMyLocationEnabled(true);

    }
    @OnClick(R.id.layout_order)
    public void orderNow(View btn){
        _mainActivity.replaceMainFragment(new ConfirmationFragment(),true);
    }

    /**
     * Success token
     * @param obj
     */
    @Override
    public void onSuccess(Object obj) {
        _mainActivity.loadingShow(false);
        if(obj != null){
            TokenModel token = (TokenModel) obj;
            CommonUtils.log(token.accessToken);
            if(_pool == 0)
                getLocation();
            _pool = 1;
        }

    }

//    void search(){
//        String search = _orderSearch.getText().toString().trim();
//    }
    private void getLocation(){
        _mainActivity.loadingShow(true);
        WSService.getInstance(_app.getRetrofitWithAuth()).getLocations(_app.user().id, new IWSDelegate() {
            @Override
            public void onFaillure(Object obj) {
                _mainActivity.loadingShow(false);
                if(obj != null){
                    int codeError = (int) obj;
                    CommonUtils.log("codeError "+codeError);
                    if(codeError == 401) getToken();

                }

            }

            @Override
            public void onSuccess(Object obj) {
                _mainActivity.loadingShow(false);
                if(obj != null){
                    String jsonStr = (String)obj;
                    CommonUtils.log(jsonStr);
                }

            }
        });
    }


}
