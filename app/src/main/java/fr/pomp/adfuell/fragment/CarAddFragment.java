package fr.pomp.adfuell.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.OnClick;
import fr.pomp.adfuell.R;
import fr.pomp.adfuell.model.TokenModel;
import fr.pomp.adfuell.model.UserModel;
import fr.pomp.adfuell.utils.comon.CommonUtils;
import fr.pomp.adfuell.ws.IWSDelegate;
import fr.pomp.adfuell.ws.WSService;

/**
 * Created by Mandimbisoa on 07/04/2017.
 */

public class CarAddFragment extends BaseFragment {

    @BindView(R.id.add_car_marque)
    public EditText _marqueEditText;
    @BindView(R.id.add_car_type)
    public EditText _typeEditText;
    @BindView(R.id.add_car_immatriculation)
    public EditText _immatriculationEditText;
    @BindView(R.id.add_car_couleur)
    public EditText _couleurEditText;
    @BindView(R.id.add_car_reservoir)
    public EditText _reservoirEditText;
    @BindView(R.id.add_car_button)
    public Button _addCarButton;
    @BindView(R.id.add_car_carburant_spinner)
    public Spinner _carburantSpinner;

    private int _poolAuth = 0;

    public CarAddFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_car_add, container, false);
        _edButterKniff.view(this, v);
        _mainActivity.headerShow(true);
        _mainActivity.lockedDrawer(false);
        _mainActivity.headerShowLeftBack();
        _mainActivity.headerRightBtnShow(false);
        _mainActivity.changeHeaderTitle(getString(R.string.text_new_car));

        String[] list = getResources().getStringArray(R.array.array_carburant);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, list);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        _carburantSpinner.setAdapter(adapter);
        return v;
    }

    @OnClick(R.id.add_car_button)
    public void creerCar() {
        _poolAuth = 0;
        _mainActivity.hideKeyBoard();
        if(verifyEditText())
            runAddCar();
    }

    private void runAddCar() {
        JsonObject jobj = new JsonObject();

        jobj.addProperty("imat", _immatriculationEditText.getText().toString());
        jobj.addProperty("fuelType", _carburantSpinner.getSelectedItem().toString());
        jobj.addProperty("brand", _marqueEditText.getText().toString());
        jobj.addProperty("color", _couleurEditText.getText().toString());
        jobj.addProperty("model", _typeEditText.getText().toString());
        jobj.addProperty("tankSize", _reservoirEditText.getText().toString());


        /*jobj.addProperty("imat", "WWT-5948");
        jobj.addProperty("fuelType", "diesel");
        jobj.addProperty("brand", "JAGUAR");
        jobj.addProperty("color", "ROUGE");
        jobj.addProperty("model", "F-PACE PURE");
        jobj.addProperty("tankSize", "50");*/

        addCar(jobj, _app.user().id);
    }

    private void addCar(JsonObject jobj, String id) {
        _mainActivity.loadingShow(true);
        WSService.getInstance(_app.getRetrofitWithAuth()).createCar(id, jobj, new IWSDelegate() {
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
                String jsonStr = (String) obj;
                _app.carSaveJson(jsonStr);
                CommonUtils.log(jsonStr);
                CommonUtils.log(_app.car().id);
                CommonUtils.alertCustom(getString(R.string.succes_add_car), null, R.drawable.ok, null, getContext());
                UserModel user = _app.userObject();
                if(user.vehicles == 0){
                    goToOrder();
                    user.vehicles = 1;
                    _app.userSaveObject(user);
                    _app.carListSaveJson("["+jsonStr+"]");

                }
                else{
                    goToCarList();
                }
            }
        });
    }

    private void goToCarList() {
        CarListFragment fragment = new CarListFragment();
        _mainActivity.replaceMainFragment(fragment, true);
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
            if (_poolAuth == 0)
                runAddCar();
            _poolAuth = 1;
        }

    }

    private void goToOrder() {
        OrderHomeFragment fragment = new OrderHomeFragment();
        _mainActivity.replaceMainFragment(fragment, false);
    }


    public boolean verifyEditText(){
        boolean isOk = false;
        String message = "";
        int emptyField = 0;
        if(_marqueEditText.getText().toString().isEmpty()){
            message = getStringRes(R.string.text_marque);
            emptyField++;
        }
        if(_typeEditText.getText().toString().isEmpty()){
            emptyField++;
            if(emptyField>1){
                message = message + ", "+getStringRes(R.string.text_type);
            }else {
                message = getStringRes(R.string.text_type);
            }
        }
        if(_immatriculationEditText.getText().toString().isEmpty()){
            emptyField++;
            if(emptyField>1){
                message = message + ", "+getStringRes(R.string.text_immatriculation);
            }else {
                message = getStringRes(R.string.text_immatriculation);
            }
        }
        if(_couleurEditText.getText().toString().isEmpty()){
            emptyField++;
            if(emptyField>1){
                message = message + ", "+getStringRes(R.string.text_couleur);
            }else {
                message = getStringRes(R.string.text_couleur);
            }
        }
        if (emptyField>0){
            if(emptyField>1){
                message = getStringRes(R.string.empty_fields)+" "+message;
                CommonUtils.alert(message,null,0,null,getContext());
            }else {
                message = getStringRes(R.string.empty_field)+" "+message;
                CommonUtils.alert(message,null,0,null,getContext());
            }
        }else {
            isOk = true;
        }
        return isOk;
    }
}
