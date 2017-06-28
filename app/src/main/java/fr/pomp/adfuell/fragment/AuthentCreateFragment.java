package fr.pomp.adfuell.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.OnClick;
import fr.pomp.adfuell.R;
import fr.pomp.adfuell.model.CountryPhoneModel;
import fr.pomp.adfuell.model.TokenModel;
import fr.pomp.adfuell.utils.comon.CommonUtils;
import fr.pomp.adfuell.utils.comon.Config;
import fr.pomp.adfuell.utils.munix.Strings;
import fr.pomp.adfuell.ws.IWSDelegate;
import fr.pomp.adfuell.ws.WSService;

/**
 * Created by edena on 31/03/2017.
 */

public class AuthentCreateFragment extends BaseFragment {

    @BindView(R.id.create_compte_firstname)
    public EditText _firstnameEditText;
    @BindView(R.id.create_compte_lastname)
    public EditText _lastnameEditText;
    @BindView(R.id.create_compte_email)
    public EditText _emailEditText;
    @BindView(R.id.create_compte_password)
    public EditText _passwordEditText;
    @BindView(R.id.create_compte_phone)
    public EditText _phoneEditText;
    @BindView(R.id.create_compte_button)
    public Button _createCompteButton;
    @BindView(R.id.spn_code)
    public Spinner _codeSpinner;

    @BindView(R.id.account_view_logo)
    public View _logoView;
    @BindView(R.id.main_layout)
    public View _mainView;

    private int _poolAuth = 0;
    private ArrayList<CountryPhoneModel> _listCountryCode = new ArrayList<>();
    private CodeAdapter _codeAdapter = new CodeAdapter();
    private Integer _positionCode = null;

    public AuthentCreateFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_authent_create, container, false);
        _edButterKniff.view(this, v);
        _mainActivity.headerShow(false);
        _mainActivity.lockedDrawer(true);
        /*_mainActivity.headerRightShowNext();
        _mainActivity.headerShowLeftBack();
        _mainActivity.changeHeaderTitle(getString(R.string.text_new_compte));*/
        _codeSpinner.setAdapter(_codeAdapter);
        _codeSpinner.setOnItemSelectedListener(_onItemSelectedListener);
        getCountryCode(_app.getJsonFromAsset(Config.JSON_CODE_COUNTRY));

        _mainView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = _mainView.getRootView().getHeight() - _mainView.getHeight();
                if (heightDiff > dpToPx(_mainActivity, 200)) {
                    CommonUtils.log("soft visible true");
                    _logoView.setVisibility(View.GONE);
                }else {
                    CommonUtils.log("soft visible false");
                    _logoView.setVisibility(View.VISIBLE);
                }
            }
        });
        return v;
    }

    public float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    @OnClick(R.id.create_compte_button)
    public void creerCompte(Button btn) {
        _poolAuth = 0;
        _mainActivity.hideKeyBoard();
        if (verifyEditText())
            runCreateCompte();
    }

    private void goToAddCar() {
        CarAddFragment fragment = new CarAddFragment();
        _mainActivity.replaceMainFragment(fragment, true);
    }


    private void runCreateCompte() {
        JsonObject jobj = new JsonObject();


        jobj.addProperty("firstname", _firstnameEditText.getText().toString());
        jobj.addProperty("lastname", _lastnameEditText.getText().toString());
        jobj.addProperty("email", _emailEditText.getText().toString());
        jobj.addProperty("phone", _listCountryCode.get(_positionCode).code+_phoneEditText.getText().toString());
        jobj.addProperty("plainPassword", _passwordEditText.getText().toString());
        jobj.addProperty("acquisitionChannel", "pompAndroid");

        //CommonUtils.log(_listCountryCode.get(_positionCode).code+" "+_phoneEditText.getText().toString());


        // donnee test
        /*jobj.addProperty("firstname", "mambs");
        jobj.addProperty("lastname", "tsihoarana");
        jobj.addProperty("email", "mandimbisoa.tsihoarana@gmail.com");
        jobj.addProperty("phone", "55894255567");
        jobj.addProperty("plainPassword", "123456789");*/

        createCompte(jobj);


    }

    private void createCompte(JsonObject body) {
        _mainActivity.loadingShow(true);
        WSService.getInstance(_app.getRetrofitWithAuth()).createCompte(body, new IWSDelegate() {
            @Override
            public void onFaillure(Object obj) {
                _mainActivity.loadingShow(false);
                if (obj != null) {
                    int codeError = (int) obj;
                    CommonUtils.log("codeError " + codeError);
                    if (codeError == 401 && _poolAuth == 0) getToken();
                    else CommonUtils.alert(getStringRes(R.string.faillure_request),"",0,null,_mainActivity);

                }else
                    CommonUtils.alert(getStringRes(R.string.faillure_request),"",0,null,_mainActivity);

            }

            @Override
            public void onSuccess(Object obj) {
                _mainActivity.loadingShow(false);
                if (obj != null) {

                    String jsonStr = (String) obj;
                    _app.userSaveJson(jsonStr);
                    CommonUtils.log(jsonStr);
                    if (_app.user() != null)
                        _app.userSaveObject(_app.user());
                    CommonUtils.alert(getString(R.string.succes_create_account), null, 0, null, getContext());
                    goToAddCar();
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
            if (_poolAuth == 0)
                runCreateCompte();
            _poolAuth = 1;
        }

    }

    public boolean verifyEditText() {
        boolean isOk = false;
        String message = "";
        int emptyField = 0;
        if (_lastnameEditText.getText().toString().isEmpty()) {
            message = getStringRes(R.string.hint_lastname);
            emptyField++;
        }
        if (_firstnameEditText.getText().toString().isEmpty()) {
            emptyField++;
            if (emptyField > 1) {
                message = message + ", " + getStringRes(R.string.hint_firstname);
            } else {
                message = getStringRes(R.string.hint_firstname);
            }
        }
        if (_emailEditText.getText().toString().isEmpty()) {
            emptyField++;
            if (emptyField > 1) {
                message = message + ", " + getStringRes(R.string.hint_email);
            } else {
                message = getStringRes(R.string.hint_email);
            }
        }
        if (_passwordEditText.getText().toString().isEmpty()) {
            emptyField++;
            if (emptyField > 1) {
                message = message + ", " + getStringRes(R.string.hint_password);
            } else {
                message = getStringRes(R.string.hint_password);
            }
        }
        if (_phoneEditText.getText().toString().isEmpty()) {
            emptyField++;
            if (emptyField > 1) {
                message = message + ", " + getStringRes(R.string.hint_tel);
            } else {
                message = getStringRes(R.string.hint_tel);
            }
        }
        if (emptyField > 0) {
            if (emptyField > 1) {
                message = getStringRes(R.string.empty_fields) + " " + message;
                CommonUtils.alert(message, null, 0, null, getContext());
            } else {
                message = getStringRes(R.string.empty_field) + " " + message;
                CommonUtils.alert(message, null, 0, null, getContext());
            }
        } else {
            if (Strings.isValidEmail(_emailEditText.getText().toString())) {
                if (_passwordEditText.getText().toString().length() >= 8) {
                    if(_phoneEditText.getText().toString().length() == 10 && _positionCode != null){
                        isOk = true;
                    }else {
                        CommonUtils.alert(getString(R.string.invalid_phone), null, 0, null, getContext());
                    }
                } else {
                    CommonUtils.alert(getString(R.string.invalid_password), null, 0, null, getContext());
                }
            } else {
                CommonUtils.alert(getString(R.string.invalid_email), null, 0, null, getContext());
            }
        }
        return isOk;
    }

    public void getCountryCode(String json) {
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                Iterator<String> iter = jsonObject.keys();
                while (iter.hasNext()) {
                    CountryPhoneModel countryPhoneModel = new CountryPhoneModel();
                    String key = iter.next();
                    String value = jsonObject.getString(key);
                    countryPhoneModel.name = key;
                    countryPhoneModel.code = value;
                    _listCountryCode.add(countryPhoneModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            _codeAdapter.notifyDataSetChanged();
        }

    }

    public class CodeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return _listCountryCode.size();
        }

        @Override
        public Object getItem(int i) {
            return _listCountryCode.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CodeViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_code_item, null);
                holder = new CodeViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.item_code);
                holder.name = (TextView) convertView.findViewById(R.id.item_name);
                convertView.setTag(holder);
            } else {
                holder = (CodeViewHolder) convertView.getTag();
            }
            holder.code.setText("("+_listCountryCode.get(position).code+")");
            holder.name.setText(_listCountryCode.get(position).name);
            return convertView;
        }
    }

    public class CodeViewHolder {
        public TextView code;
        public TextView name;
    }

    AdapterView.OnItemSelectedListener _onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            /*if(view != null)
                view.findViewById(R.id.item_name).setVisibility(View.GONE);*/
            _positionCode = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

}
