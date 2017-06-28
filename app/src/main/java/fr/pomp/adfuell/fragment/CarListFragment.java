package fr.pomp.adfuell.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import fr.pomp.adfuell.R;
import fr.pomp.adfuell.model.CarModel;
import fr.pomp.adfuell.model.TokenModel;
import fr.pomp.adfuell.model.UserModel;
import fr.pomp.adfuell.utils.comon.CommonUtils;
import fr.pomp.adfuell.ws.IWSDelegate;
import fr.pomp.adfuell.ws.WSService;

/**
 * Created by edena on 11/04/2017.
 */

public class CarListFragment extends BaseFragment {
    @BindView(R.id.car_list_contener)
    LinearLayout _contenerList;
    @BindView(R.id.car_list_empty_msg)
    View _emptyView;
    @BindView(R.id.car_list_scroll)
    ScrollView _scrollList;
    @BindView(R.id.car_list_add_bas)
    Button _addCarButton;

    LayoutInflater _layoutInflater;
    ArrayList<CarModel> _list;

    private int _poolAuth = 0;
    public String _action = "";
    public String _idVehicule = "";

    public CarListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_car_list, container, false);

        _edButterKniff.view(this, v);
        _mainActivity.headerShow(true);
        _mainActivity.changeHeaderTitle(getString(R.string.text_vehicule));
        _mainActivity.headerRightBtnShow(false);
        _mainActivity.headerShowLeftMenu();
        _layoutInflater = (LayoutInflater) _mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        getList();
        return v;
    }

    void updateList() {

        _contenerList.removeAllViews();

        RelativeLayout view = null;
        int listSize = _list != null ? _list.size() : 0;
        if (listSize > 0) {
            _emptyView.setVisibility(View.GONE);
            for (int index = 0; index < listSize; index++) {

                final CarModel car = _list.get(index);
                view = (RelativeLayout) _layoutInflater.inflate(R.layout.view_car_list, null);
                TextView title = (TextView) view.findViewById(R.id.car_title);
                TextView titleBas = (TextView) view.findViewById(R.id.car_title_bas);
                ImageView img_delete = (ImageView) view.findViewById(R.id.car_del);


                title.setText(car.brand + " - " + car.model);
                titleBas.setText(car.fuelType);
                if (car.orders == 0) {
                    img_delete.setVisibility(View.VISIBLE);
                    img_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            CommonUtils.alert(getStringRes(R.string.supress_voiture), null, 0, new CommonUtils.IDialog() {
                                @Override
                                public void no(Object obj) {

                                }

                                @Override
                                public void yes(Object obj) {
                                    _idVehicule = car.id;
                                    delete(_idVehicule);
                                }
                            }, _mainActivity);


                        }
                    });
                } else {
                    img_delete.setVisibility(View.GONE);
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        _app._carUpdate = car;
                        goToCarUpdate();
                    }
                });

                _contenerList.addView(view);
                _contenerList.addView(separtor());
            }
        } else {
            _emptyView.setVisibility(View.VISIBLE);
        }

    }

    private void goToCarUpdate() {
        _mainActivity.replaceMainFragment(new CarUpdateFragment(), true);
    }

    void getList() {
        _poolAuth = 0;
        _action = "getList";
        runCarList(_app.userObject().id);
    }

    void delete(String id) {
        _poolAuth = 0;
        _action = "delete";
        runDeleteCar(id);
    }

    View separtor() {
        View view = new View(_mainActivity);
        view.setBackgroundResource(R.color.gris);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        return view;
    }


    @OnClick(R.id.car_list_add_bas)
    public void create(Button btn) {
        _mainActivity.replaceMainFragment(new CarAddFragment(), true);

    }

    private void runCarList(String id) {
        _mainActivity.loadingShow(true);
        WSService.getInstance(_app.getRetrofitWithAuth()).getCarList(id, new IWSDelegate() {
            @Override
            public void onFaillure(Object obj) {
                _mainActivity.loadingShow(false);
                if (obj != null) {
                    int codeError = (int) obj;
                    CommonUtils.log("codeError " + codeError);
                    if (codeError == 401 && _poolAuth == 0) getToken();
                    else CommonUtils.alert(getStringRes(R.string.faillure_request),"",0,null,_mainActivity);
                }else CommonUtils.alert(getStringRes(R.string.faillure_request),"",0,null,_mainActivity);
            }

            @Override
            public void onSuccess(Object obj) {
                _mainActivity.loadingShow(false);
                if (obj != null) {

                    String jsonStr = (String) obj;
                    _app.carListSaveJson(jsonStr);
                    CommonUtils.log(jsonStr);
                    CommonUtils.log("size car list= " + _app.carList().size());
                    _list = _app.carList();
                    updateList();
                    UserModel user = _app.userObject();
                    user.vehicles = _app.carList().size();
                    _app.userSaveObject(user);
                }
            }
        });
    }

    /**
     * Success token
     *
     * @param obj
     */
    @Override
    public void onSuccess(Object obj) {
        _mainActivity.loadingShow(false);
        if (obj != null) {
            TokenModel token = (TokenModel) obj;
            CommonUtils.log(token.accessToken);
            if (_poolAuth == 0){
                if(_action.equals("getList")){
                    runCarList(_app.userObject().id);
                }else {
                    runDeleteCar(_idVehicule);
                }
                //getList();
            }
            _poolAuth = 1;
        }

    }

    private void runDeleteCar(String id) {
        _mainActivity.loadingShow(true);
        WSService.getInstance(_app.getRetrofitWithAuth()).deleteCar(id, new IWSDelegate() {
            @Override
            public void onFaillure(Object obj) {
                _mainActivity.loadingShow(false);
                if (obj != null) {
                    int codeError = (int) obj;
                    CommonUtils.log("codeError " + codeError);
                    if (codeError == 401 && _poolAuth == 0) {
                        getToken();
                    } else {
                        CommonUtils.alertCustom(getString(R.string.succes_delete_car), null, R.drawable.ok, null, getContext());
                        getList();

                    }
                }else CommonUtils.alert(getStringRes(R.string.faillure_request),"",0,null,_mainActivity);
            }

            @Override
            public void onSuccess(Object obj) {
                _mainActivity.loadingShow(false);
                CommonUtils.log("Suppression voiture effectué");
                CommonUtils.alertCustom(getString(R.string.succes_delete_car), null, R.drawable.ok, null, getContext());
                getList();
            }
        });
    }

}
