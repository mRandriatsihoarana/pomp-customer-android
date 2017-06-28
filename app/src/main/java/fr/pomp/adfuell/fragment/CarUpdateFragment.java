package fr.pomp.adfuell.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import fr.pomp.adfuell.model.CarModel;
import fr.pomp.adfuell.model.TokenModel;
import fr.pomp.adfuell.utils.comon.CommonUtils;
import fr.pomp.adfuell.ws.IWSDelegate;
import fr.pomp.adfuell.ws.WSService;

/**
 * Created by Mandimbisoa on 12/04/2017.
 */

public class CarUpdateFragment extends BaseFragment{

    @BindView(R.id.update_car_marque)
    public EditText _marqueEditText;
    @BindView(R.id.update_car_type)
    public EditText _typeEditText;
    @BindView(R.id.update_car_immatriculation)
    public EditText _immatriculationEditText;
    @BindView(R.id.update_car_couleur)
    public EditText _couleurEditText;
    @BindView(R.id.update_car_reservoir)
    public EditText _reservoirEditText;
    @BindView(R.id.update_car_carburant_spinner)
    public Spinner _carburantSpinner;
    @BindView(R.id.update_car_button)
    public Button _carButton;

    private int _poolAuth = 0;
    private boolean _enableUpdateButton = false;

    public ArrayAdapter<String> _adapter = null;

    public CarUpdateFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_car_update, container, false);
        _edButterKniff.view(this, v);
        _mainActivity.headerShow(true);
        _mainActivity.lockedDrawer(false);
        _mainActivity.headerShowLeftBack();
        _mainActivity.headerRightBtnShow(false);
        _mainActivity.changeHeaderTitle(getString(R.string.button_update_car));

        String[] list = getResources().getStringArray(R.array.array_carburant);

        _adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, list);
        _adapter.setDropDownViewResource(R.layout.spinner_item);
        _carburantSpinner.setAdapter(_adapter);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(_app._carUpdate != null){
            CarModel car = _app._carUpdate;
            _marqueEditText.setText(car.brand);
            _typeEditText.setText(car.model);
            _immatriculationEditText.setText(car.imat);
            _couleurEditText.setText(car.color);
            if(car.tankSize == null)
                _reservoirEditText.setText("");
            else
                _reservoirEditText.setText(""+car.tankSize);
            int position = _adapter.getPosition(car.fuelType);
            _carburantSpinner.setSelection(position);
            if(_app._carUpdate.orders != 0){
                desableFieldFocus();
            }else {
                _enableUpdateButton = true;
            }
        }
    }

    public void desableFieldFocus() {
        _enableUpdateButton = false;
        _marqueEditText.setFocusableInTouchMode(false);
        _typeEditText.setFocusableInTouchMode(false);
        _immatriculationEditText.setFocusableInTouchMode(false);
        _carburantSpinner.setEnabled(false);
        _carburantSpinner.setClickable(false);
        _couleurEditText.setFocusableInTouchMode(false);
        _reservoirEditText.setFocusableInTouchMode(false);
        _carButton.setVisibility(View.GONE);
    }

    @OnClick(R.id.update_car_button)
    public void updateCarButton() {
        _poolAuth = 0;
        _mainActivity.hideKeyBoard();
        if(_enableUpdateButton && verifyEditText())
            runUpdateCar();
    }

    public void runUpdateCar() {
        JsonObject jobj = new JsonObject();

        jobj.addProperty("imat", _immatriculationEditText.getText().toString());
        jobj.addProperty("fuelType", _carburantSpinner.getSelectedItem().toString());
        jobj.addProperty("brand", _marqueEditText.getText().toString());
        jobj.addProperty("color", _couleurEditText.getText().toString());
        jobj.addProperty("model", _typeEditText.getText().toString());
        jobj.addProperty("tankSize", _reservoirEditText.getText().toString());

        updateCar(_app._carUpdate.id,jobj);
    }

    public void updateCar(String id, JsonObject jsonBody) {
        _mainActivity.loadingShow(true);
        WSService.getInstance(_app.getRetrofitWithAuth()).updateCar(id, jsonBody, new IWSDelegate() {
            @Override
            public void onFaillure(Object obj) {
                _mainActivity.loadingShow(false);
                if (obj != null) {
                    int codeError = (int) obj;
                    CommonUtils.log("codeError " + codeError);
                    if (codeError == 401 && _poolAuth == 0) {
                        getToken();
                    }else CommonUtils.alert(getStringRes(R.string.faillure_request),"",0,null,_mainActivity);

                }else CommonUtils.alert(getStringRes(R.string.faillure_request),"",0,null,_mainActivity);

            }

            @Override
            public void onSuccess(Object obj) {
                _mainActivity.loadingShow(false);
                if (obj != null) {

                    String jsonStr = (String) obj;
                    CommonUtils.log(jsonStr);
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
            if (_poolAuth == 0) {
                runUpdateCar();
            }

            _poolAuth = 1;
        }

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
